package com.ryuunomi.inmotech.services.imagenusuario;

import com.ryuunomi.inmotech.entities.ImagenUsuario;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.repositories.ImagenUsuarioRepository;
import com.ryuunomi.inmotech.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImagenUsuarioServiceImpl implements IImagenUsuarioService {

    private static final String BASE_URL = "C:\\Users\\jmonv\\OneDrive\\Escritorio\\Proyecto_inmotech\\Backend-SpringBoot\\Inmotech-Backend\\src\\main\\resources\\imagenesUsuarios";

    @Autowired
    private ImagenUsuarioRepository imagenUsuarioRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Override
    public ImagenUsuario subirImagen(Long userId, MultipartFile file) throws IOException {
        Usuario usuario = usuarioRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // Simular guardado físico
        String nombreArchivo = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String url = BASE_URL + nombreArchivo;

        // Si ya tiene una imagen, reemplazarla
        ImagenUsuario anterior = imagenUsuarioRepo.findByUsuarioId(userId);
        if (anterior != null) {
            imagenUsuarioRepo.delete(anterior);
        }

        ImagenUsuario nueva = new ImagenUsuario(url, nombreArchivo, usuario);
        return imagenUsuarioRepo.save(nueva);
    }

    @Override
    public void eliminarImagen(Long userId) {
        ImagenUsuario imagen = imagenUsuarioRepo.findByUsuarioId(userId);
        if (imagen == null) {
            throw new ResourceNotFoundException("Imagen no encontrada para el usuario");
        }

        imagenUsuarioRepo.delete(imagen);

        // Aquí eliminarías el archivo físico si se guardara localmente
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
