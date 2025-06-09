package com.ryuunomi.inmotech.mapper;

import com.ryuunomi.inmotech.dto.PropiedadDTO;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Agencia;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.entities.ImagenPropiedad;

public class PropiedadMapper {

    public static PropiedadDTO toDTO(Propiedad p) {
        return new PropiedadDTO(
                p.getId(),
                p.getTitulo(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getSuperficie(),
                p.getDireccion(),
                p.getCiudad(),
                p.getProvincia(),
                p.getCodigoPostal(),
                p.getLatitud(),
                p.getLongitud(),
                p.getFechaPublicacion() != null ? p.getFechaPublicacion().toString() : null,
                p.getUsuario() != null ? p.getUsuario().getId() : null,
                p.getAgencia() != null ? p.getAgencia().getId() : null,
                p.getImagenPortada() != null ? p.getImagenPortada().getUrl() : null
        );
    }

    public static Propiedad fromDTO(PropiedadDTO dto) {
        Propiedad p = new Propiedad();
        p.setId(dto.id());
        p.setTitulo(dto.titulo());
        p.setDescripcion(dto.descripcion());
        p.setPrecio(dto.precio());
        p.setSuperficie(dto.superficie());
        p.setDireccion(dto.direccion());
        p.setCiudad(dto.ciudad());
        p.setProvincia(dto.provincia());
        p.setCodigoPostal(dto.codigoPostal());

        if (dto.idUsuario() != null) {
            Usuario u = new Usuario();
            u.setId(dto.idUsuario());
            p.setUsuario(u);
        }

        if (dto.idAgencia() != null) {
            Agencia a = new Agencia();
            a.setId(dto.idAgencia());
            p.setAgencia(a);
        }

        if (dto.imagenPortadaUrl() != null) {
            ImagenPropiedad img = new ImagenPropiedad();
            img.setUrl(dto.imagenPortadaUrl());
            p.setImagenPortada(img);
        }

        return p;
    }
} 