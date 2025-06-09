package com.ryuunomi.inmotech.mapper;

import com.ryuunomi.inmotech.dto.ConsultaDTO;
import com.ryuunomi.inmotech.entities.Consulta;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;

public class ConsultaMapper {

    public static ConsultaDTO toDTO(Consulta c) {
        return new ConsultaDTO(
                c.getId(),
                c.getMensaje(),
                c.getFecha() != null ? c.getFecha().toString() : null,
                c.getUsuario() != null ? c.getUsuario().getId() : null,
                c.getPropiedad() != null ? c.getPropiedad().getId() : null
        );
    }

    public static Consulta fromDTO(ConsultaDTO dto) {
        Consulta c = new Consulta();
        c.setId(dto.id());
        c.setMensaje(dto.mensaje());

        if (dto.idUsuario() != null) {
            Usuario u = new Usuario();
            u.setId(dto.idUsuario());
            c.setUsuario(u);
        }

        if (dto.idPropiedad() != null) {
            Propiedad p = new Propiedad();
            p.setId(dto.idPropiedad());
            c.setPropiedad(p);
        }

        return c;
    }
}
