package com.ryuunomi.inmotech.services.imagenpropiedad;

import com.ryuunomi.inmotech.entities.ImagenPropiedad;
import com.ryuunomi.inmotech.entities.Propiedad;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class ImagenPropiedadServiceImpl implements IImagenPropiedadService {

    // Ruta absoluta hacia tu carpeta de recursos donde irán las subcarpetas por propiedad.
    private static final String BASE_DIR =
            "C:\\Users\\jmonv\\OneDrive\\Escritorio\\Proyecto_inmotech\\Backend-SpringBoot\\"
                    + "Inmotech-Backend\\src\\main\\resources\\imagenesPropiedades";

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private ImagenPropiedadRepository imagenPropiedadRepository;

    @Override
    public List<ImagenPropiedad> listarPorPropiedad(Long propiedadId) throws ResourceNotFoundException {
        // 1) Verificar que la Propiedad existe
        propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada con id: " + propiedadId));

        // 2) Devolver lista de imágenes ordenada automáticamente (gracias a @OrderBy en la entidad).
        return imagenPropiedadRepository.findByPropiedadId(propiedadId);
    }

    @Override
    @Transactional
    public List<ImagenPropiedad> subirImagenes(Long propiedadId, MultipartFile[] files)
            throws IOException, ResourceNotFoundException, IllegalArgumentException {

        // 1) Verificar propiedad
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada con id: " + propiedadId));

        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("Debe proporcionar al menos un archivo en 'files'.");
        }

        // 2) Crear carpeta de la propiedad si no existe:
        Path carpetaPropiedad = Paths.get(BASE_DIR, propiedadId.toString());
        if (!Files.exists(carpetaPropiedad)) {
            Files.createDirectories(carpetaPropiedad);
        }

        // 3) Contar cuántas hay actualmente para asignar orden inicial
        //    Esto evita solaparse si se suben en lotes diferentes.
        List<ImagenPropiedad> existentes = imagenPropiedadRepository.findByPropiedadId(propiedadId);
        int ordenInicial = existentes.size();

        List<ImagenPropiedad> guardadas = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            // 3.1) Validaciones básicas
            if (file.isEmpty()) {
                continue; // saltar archivos vacíos
            }
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            if (originalFilename.contains("..")) {
                throw new IllegalArgumentException("Nombre de archivo inválido: " + originalFilename);
            }

            // 3.2) Extraer extensión (ej: "jpg", "png", etc.)
            String ext = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex >= 0) {
                ext = originalFilename.substring(dotIndex + 1);
            }

            // 3.3) Nombre físico: "<propiedadId>_<ordenActual>.<ext>"
            int ordenActual = ordenInicial + i;
            String nuevoNombre = propiedadId + "_" + ordenActual +
                    (ext.isEmpty() ? "" : ("." + ext));

            Path destino = carpetaPropiedad.resolve(nuevoNombre);

            // 3.4) Guardar el archivo en disco
            Files.copy(file.getInputStream(), destino);

            // 3.5) Crear entidad y persistir en BD
            ImagenPropiedad img = new ImagenPropiedad();
            img.setPropiedad(propiedad);
            img.setUrl("/imagenesPropiedades/" + propiedadId + "/" + nuevoNombre);
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

        // 1) Verificar propiedad
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada con id: " + propiedadId));

        // 2) Verificar imagen existe y pertenece a esa Propiedad
        ImagenPropiedad imagen = imagenPropiedadRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con id: " + imageId));

        if (!imagen.getPropiedad().getId().equals(propiedadId)) {
            throw new IllegalArgumentException("La imagen no pertenece a la propiedad indicada.");
        }

        // 3) Borrar archivo físico
        //    Su url es "/imagenesPropiedades/{propiedadId}/{nombre}", así que extraemos nombre.
        String url = imagen.getUrl();
        // url ejemplo: "/imagenesPropiedades/5/5_0.jpg"
        String nombreArchivo = url.substring(url.lastIndexOf('/') + 1);
        Path rutaArchivo = Paths.get(BASE_DIR, propiedadId.toString(), nombreArchivo);
        Files.deleteIfExists(rutaArchivo);

        // 4) Borrar registro en BD
        imagenPropiedadRepository.delete(imagen);
    }

    @Override
    @Transactional
    public ImagenPropiedad actualizarOrden(Long propiedadId, Long imageId, Integer nuevoOrden)
            throws ResourceNotFoundException, IllegalArgumentException {

        // 1) Verificar propiedad
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada con id: " + propiedadId));

        // 2) Verificar imagen
        ImagenPropiedad imagen = imagenPropiedadRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen no encontrada con id: " + imageId));

        if (!imagen.getPropiedad().getId().equals(propiedadId)) {
            throw new IllegalArgumentException("La imagen no pertenece a la propiedad indicada.");
        }

        if (nuevoOrden < 0) {
            throw new IllegalArgumentException("El campo 'orden' no puede ser negativo.");
        }

        // 3) Asignar nuevo orden y guardar
        imagen.setOrden(nuevoOrden);
        return imagenPropiedadRepository.save(imagen);
    }
}