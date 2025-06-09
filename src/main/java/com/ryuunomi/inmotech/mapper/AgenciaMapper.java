package com.ryuunomi.inmotech.mapper;

import com.ryuunomi.inmotech.dto.AgenciaDTO;
import com.ryuunomi.inmotech.entities.Agencia;

public class AgenciaMapper {

    public static AgenciaDTO toDTO(Agencia a) {
        return new AgenciaDTO(
                a.getId(),
                a.getNombre(),
                a.getDescripcion(),
                a.getIdUsuarioAdmin()
        );
    }

    public static Agencia fromDTO(AgenciaDTO dto) {
        Agencia a = new Agencia();
        a.setId(dto.id());
        a.setNombre(dto.nombre());
        a.setDescripcion(dto.descripcion());
        a.setIdUsuarioAdmin(dto.idUsuarioAdmin());
        return a;
    }
}

