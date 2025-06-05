package com.ryuunomi.inmotech.services.imagenpropiedad;

import com.ryuunomi.inmotech.entities.ImagenPropiedad;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface IImagenPropiedadService {

    /**
     * Lista todas las ImagenPropiedad de una Propiedad dado su ID, en orden ascendente.
     * @param propiedadId ID de la propiedad.
     * @return lista de ImagenPropiedad ordenadas por “orden”.
     */
    List<ImagenPropiedad> listarPorPropiedad(Long propiedadId);

    /**
     * Sube uno o varios archivos (MultipartFile[]) para la propiedad dada.
     * - Crea la carpeta /uploads/propiedades/{propiedadId} si no existe.
     * - Guarda cada archivo con un nombre único.
     * - Crea el registro en la BD.
     * @param propiedadId ID de la propiedad.
     * @param archivos     arreglo de archivos a subir.
     * @return lista de ImagenPropiedad recién creadas.
     * @throws IOException si ocurre un error al guardar en disco.
     */
    List<ImagenPropiedad> subirImagenes(Long propiedadId, org.springframework.web.multipart.MultipartFile[] archivos) throws IOException;

    /**
     * Elimina una imagen específica (imageId) de la propiedad (propiedadId).
     * - Borra el archivo físico del disco.
     * - Elimina el registro de BD.
     * @param propiedadId ID de la propiedad.
     * @param imageId     ID de la imagen a eliminar.
     * @throws IOException si ocurre un error al borrar el archivo físico.
     */
    void eliminarImagen(Long propiedadId, Long imageId) throws IOException;

    /**
     * (Opcional) Cambia solo el campo "orden" de una imagen concreta.
     * @param propiedadId ID de la propiedad.
     * @param imageId     ID de la imagen a reordenar.
     * @param nuevoOrden  Nuevo valor de orden.
     * @return ImagenPropiedad actualizada.
     */
    ImagenPropiedad actualizarOrden(Long propiedadId, Long imageId, Integer nuevoOrden);
}
