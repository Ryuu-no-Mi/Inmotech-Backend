package com.ryuunomi.inmotech.services.imagenusuario;

import com.ryuunomi.inmotech.entities.ImagenUsuario;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.repositories.ImagenUsuarioRepository;
import com.ryuunomi.inmotech.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImagenUsuarioServiceImpl implements IImagenUsuarioService {

    private static final String BASE_URL =
            "C:\\imagenes_inmotech";

    @Autowired
    private ImagenUsuarioRepository imagenUsuarioRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Override
    @Transactional
    public ImagenUsuario subirImagen(Long userId, MultipartFile file) throws IOException {
        Usuario usuario = usuarioRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        ImagenUsuario imagen = imagenUsuarioRepo.findByUsuarioId(userId);

        String nombreArchivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File carpetaUsuario = new File(BASE_URL + File.separator + "usuarios" + File.separator + userId);
        carpetaUsuario.mkdirs();

        String rutaFisica = carpetaUsuario.getAbsolutePath() + File.separator + nombreArchivo;
        file.transferTo(new File(rutaFisica));
        String rutaWeb = "/imagenes/usuarios/" + userId + "/" + nombreArchivo;


        if (imagen != null) {

            File anterior = new File(BASE_URL + File.separator + imagen.getUrl().replace("/imagenes/", ""));
            if (anterior.exists()) anterior.delete();

            imagen.setNombreArchivo(nombreArchivo);
            imagen.setUrl(rutaWeb);
            return imagenUsuarioRepo.save(imagen);
        }


        ImagenUsuario nueva = new ImagenUsuario(rutaWeb, nombreArchivo, usuario);
        return imagenUsuarioRepo.save(nueva);
    }





    @Override
    public void eliminarImagen(Long userId) {
        ImagenUsuario imagen = imagenUsuarioRepo.findByUsuarioId(userId);
        if (imagen == null) {
            throw new ResourceNotFoundException("Imagen no encontrada para el usuario");
        }

        imagenUsuarioRepo.delete(imagen);

    }

    @Override
    public ImagenUsuario obtenerPorUsuario(Long userId) {
        ImagenUsuario imagen = imagenUsuarioRepo.findByUsuarioId(userId);
        if (imagen == null) {
            throw new ResourceNotFoundException("Imagen no encontrada para el usuario");
        }
        return imagen;
    }
}
