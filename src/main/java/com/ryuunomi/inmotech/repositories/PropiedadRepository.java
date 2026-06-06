package com.ryuunomi.inmotech.repositories;

import com.ryuunomi.inmotech.entities.Propiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//JpaRepository  -- Extiende a CrudRepository y también a PagingAndSortingRepository.
@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {


    List<Propiedad> findByUsuarioId(Long idUsuario);

    List<Propiedad> findByAgenciaId(Long idAgencia);

    long countByUsuarioId(Long idUsuario);

    long countByAgenciaId(Long idAgencia);

}
