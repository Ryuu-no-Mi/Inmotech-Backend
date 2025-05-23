package com.ryuunomi.inmotech.services.propiedad;

import com.ryuunomi.inmotech.entities.Propiedad;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IPropiedadService {
    List<Propiedad> findAll();

    Optional<Propiedad> findById(Long id);

    Propiedad save(Propiedad propiedad);

    Optional<Propiedad> update(Long id, Propiedad propiedads);

    Optional<Propiedad> deleteById(Long id);

    List<Propiedad> findByUsuarioId(Long idUsuario);

    List<Propiedad> findByAgenciaId(Long idAgencia);
}
