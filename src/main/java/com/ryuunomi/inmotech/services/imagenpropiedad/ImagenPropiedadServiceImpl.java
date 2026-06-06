package com.ryuunomi.inmotech.services.imagenpropiedad;

import com.ryuunomi.inmotech.entities.Agencia;
import com.ryuunomi.inmotech.entities.ImagenPropiedad;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.repositories.ImagenPropiedadRepository;
import com.ryuunomi.inmotech.repositories.PropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImagenPropiedadServiceImpl implements IImagenPropiedadService {

    private static final String BASE_DIR =
            "C:\\imagenes_inmotech";

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private ImagenPropiedadRepository imagenPropiedadRepository;

    @Override
    public List<ImagenPropiedad> listarPorPropiedad(Long propiedadId) throws ResourceNotFoundException {

        propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada con id: " + propiedadId));

        return imagenPropiedadRepository.findByPropiedadId(propiedadId);
    }

    @Override
    @Transactional
    public List<ImagenPropiedad> subirImagenes(Long propiedadId, MultipartFile[] files)
            throws IOException, ResourceNotFoundException, IllegalArgumentException {

        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada con id: " + propiedadId));

        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("Debe proporcionar al menos un archivo en 'files'.");
        }

        Agencia agencia = propiedad.getAgencia();
        Usuario usuario = propiedad.getUsuario();

        String carpetaPadre;

        if (agencia != null && agencia.getNombre() != null) {
            carpetaPadre = agencia.getId() + "-" + agencia.getNombre().replaceAll("\\s+", "_");
        } else if (usuario != null && usuario.getNombre() != null) {
            carpetaPadre = "user_" + usuario.getId() + "-" + usuario.getNombre().replaceAll("\\s+", "_");
        } else {
            carpetaPadre = "user_desconocido";
        }

        Path carpetaPropiedad = Paths.get(BASE_DIR, carpetaPadre, propiedadId.toString());
        if (!Files.exists(carpetaPropiedad)) {
            Files.createDirectories(carpetaPropiedad);
        }

        // 3) Contar cuántas hay actualmente para asignar orden inicial
        //    Esto evita  si se suben muchas imagenes diferentes.
        List<ImagenPropiedad> existentes = imagenPropiedadRepository.findByPropiedadId(propiedadId);
        int ordenInicial = existentes.size();

        List<ImagenPropiedad> guardadas = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];

            if (file.isEmpty()) continue;

            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

            if (originalFilename.contains("..")) {
                throw new IllegalArgumentException("Nombre de archivo inválido: " + originalFilename);
            }

            String ext = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex >= 0) {
                ext = originalFilename.substring(dotIndex + 1);
            }

            int ordenActual = ordenInicial + i;
            String nuevoNombre = propiedadId + "_" + ordenActual + (ext.isEmpty() ? "" : ("." + ext));

            Path destino = carpetaPropiedad.resolve(nuevoNombre);

            try {
                Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                throw new RuntimeException("No se pudo guardar la imagen: " + originalFilename, ex);
            }

            String subCarpeta = carpetaPadre + "/" + propiedadId;
            String urlGenerada = "/imagenesPropiedades/" + subCarpeta + "/" + nuevoNombre;

            // ✅ Todos los datos ANTES del save
            ImagenPropiedad img = new ImagenPropiedad();
            img.setPropiedad(propiedad);
            img.setUrl(urlGenerada); // 🔥 AQUI ES OBLIGATORIO
            System.out.println("Guardando imagen con URL: " + img.getUrl());
            img.setOrden(ordenActual);

            ImagenPropiedad imgGuardada = imagenPropiedadRepository.save(img);
            guardadas.add(imgGuardada);
        }

        return guardadas;
    }

    @Override
    @Transactional
    public void eliminarImagen(Long propiedadId, Long imageId)
            throws IOException, ResourceNotFoundException, IllegalArgumentException {


        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada con id: " + propiedadId));


        ImagenPropiedad imagen = imagenPropiedadRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con id: " + imageId));

        if (!imagen.getPropiedad().getId().equals(propiedadId)) {
            throw new IllegalArgumentException("La imagen no pertenece a la propiedad indicada.");
        }


        String url = imagen.getUrl();
        String nombreArchivo = url.substring(url.lastIndexOf('/') + 1);
        Path rutaArchivo = Paths.get(BASE_DIR, propiedadId.toString(), nombreArchivo);
        Files.deleteIfExists(rutaArchivo);


        imagenPropiedadRepository.delete(imagen);
    }

    @Override
    @Transactional
    public ImagenPropiedad actualizarOrden(Long propiedadId, Long imageId, Integer nuevoOrden)
            throws ResourceNotFoundException, IllegalArgumentException {


        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada con id: " + propiedadId));


        ImagenPropiedad imagen = imagenPropiedadRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con id: " + imageId));

        if (!imagen.getPropiedad().getId().equals(propiedadId)) {
            throw new IllegalArgumentException("La imagen no pertenece a la propiedad indicada.");
        }

        if (nuevoOrden < 0) {
            throw new IllegalArgumentException("El campo 'orden' no puede ser negativo.");
        }


        imagen.setOrden(nuevoOrden);
        return imagenPropiedadRepository.save(imagen);
    }

    @Override
    public ImagenPropiedad findById(Long id) {
        return imagenPropiedadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se ha encopntardo esta imagen"));
    }
}