package com.ryuunomi.inmotech.services;

import com.ryuunomi.inmotech.entities.Propiedad;
import java.util.List;
import java.util.Optional;

public interface IPropiedadService {
    List<Propiedad> findAll();

    Optional<Propiedad> findById(Long id);

    Propiedad save(Propiedad propiedad);

    Optional<Propiedad> deleteById(Long id);

    List<Propiedad> findByUsuarioId(Long idUsuario);

    List<Propiedad> findByAgenciaId(Long idAgencia);
}
