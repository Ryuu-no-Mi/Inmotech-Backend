package com.ryuunomi.inmotech.mapper;

import com.ryuunomi.inmotech.dto.FavoritoDTO;
import com.ryuunomi.inmotech.entities.Favorito;
import com.ryuunomi.inmotech.entities.Propiedad;

public class FavoritoMapper {

    public static FavoritoDTO toDTO(Favorito f) {
        return new FavoritoDTO(
                f.getId(),
                f.getPropiedad() != null ? f.getPropiedad().getId() : null
        );
    }

    public static Favorito fromDTO(FavoritoDTO dto) {
        Favorito f = new Favorito();
        f.setId(dto.id());

        if (dto.propiedadId() != null) {
            Propiedad p = new Propiedad();
            p.setId(dto.propiedadId());
            f.setPropiedad(p);
        }

        return f;
    }
}
