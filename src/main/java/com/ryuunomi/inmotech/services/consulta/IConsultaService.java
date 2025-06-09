package com.ryuunomi.inmotech.services.consulta;

import com.ryuunomi.inmotech.entities.Consulta;

import java.util.List;
import java.util.Optional;

public interface IConsultaService {
    Consulta save(Consulta consulta);
    Consulta update(Long id, Consulta consulta);
    List<Consulta> list();
    Optional<Consulta> findById(Long id);
    void delete(Long id);
    List<Consulta> findByUser(Long idUsuario);
    List<Consulta> findByProperty(Long idPropiedad);

}
