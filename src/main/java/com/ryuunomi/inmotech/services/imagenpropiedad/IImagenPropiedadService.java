package com.ryuunomi.inmotech.services.imagenpropiedad;

import com.ryuunomi.inmotech.entities.ImagenPropiedad;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface IImagenPropiedadService {

    List<ImagenPropiedad> listarPorPropiedad(Long propiedadId);


    List<ImagenPropiedad> subirImagenes(Long propiedadId, org.springframework.web.multipart.MultipartFile[] archivos) throws IOException;


    void eliminarImagen(Long propiedadId, Long imageId) throws IOException;


    ImagenPropiedad actualizarOrden(Long propiedadId, Long imageId, Integer nuevoOrden);

    ImagenPropiedad findById(Long id);
}
