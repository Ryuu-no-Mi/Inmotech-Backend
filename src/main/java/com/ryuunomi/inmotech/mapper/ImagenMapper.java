package com.ryuunomi.inmotech.mapper;

import com.ryuunomi.inmotech.dto.ImagenPropiedadDTO;
import com.ryuunomi.inmotech.dto.ImagenUsuarioDTO;
import com.ryuunomi.inmotech.entities.ImagenPropiedad;
import com.ryuunomi.inmotech.entities.ImagenUsuario;

public class ImagenMapper {

    public static ImagenPropiedadDTO toPropiedadDTO(ImagenPropiedad img) {
        return new ImagenPropiedadDTO( img.getId(),img.getUrl(), img.getOrden());
    }

    public static ImagenUsuarioDTO toUsuarioDTO(ImagenUsuario img) {
        return new ImagenUsuarioDTO(img.getId(), img.getUrl(), img.getNombreArchivo());
    }

    public static ImagenPropiedad fromPropiedadDTO(ImagenPropiedadDTO dto) {
        ImagenPropiedad img = new ImagenPropiedad();
        img.setUrl(dto.url());
        return img;
    }

    public static ImagenUsuario fromUsuarioDTO(ImagenUsuarioDTO dto) {
        ImagenUsuario img = new ImagenUsuario();
        img.setId(dto.id());
        img.setUrl(dto.url());
        return img;
    }
}
