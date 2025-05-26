package com.ryuunomi.inmotech.repository;

import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//JpaRepository  -- Extiende a CrudRepository y también a PagingAndSortingRepository.
@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findByUsuarioId(Long idUsuario);

    List<Propiedad> findByAgenciaId(Long idAgencia);


}
