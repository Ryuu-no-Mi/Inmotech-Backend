package com.ryuunomi.inmotech.repositories;

import com.ryuunomi.inmotech.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByUsuarioId(Long idUsuario);
    List<Consulta> findByPropiedadId(Long idPropiedad);
}
