package com.ryuunomi.inmotech.repository;

import com.ryuunomi.inmotech.entities.Propiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

//JpaRepository  -- Extiende a CrudRepository y también a PagingAndSortingRepository.
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findByUsuarioId(Long idUsuario);

    List<Propiedad> findByAgenciaId(Long idAgencia);

    Optional<Propiedad> delete(Optional<Propiedad> optionalPropiedad);
}
