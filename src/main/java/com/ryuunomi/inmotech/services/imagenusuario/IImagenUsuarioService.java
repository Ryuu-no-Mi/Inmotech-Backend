package com.ryuunomi.inmotech.services.imagenusuario;

import com.ryuunomi.inmotech.entities.ImagenUsuario;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface IImagenUsuarioService {
    ImagenUsuario subirImagen(Long userId, MultipartFile file) throws IOException;
    void eliminarImagen(Long userId) throws IOException;
    ImagenUsuario obtenerPorUsuario(Long userId);
}
