package com.ryuunomi.inmotech.services.consulta;

import com.ryuunomi.inmotech.entities.Consulta;

import java.util.List;
import java.util.Optional;

public interface IConsultaService {
    Consulta guardarConsulta(Consulta consulta);
    Consulta actualizarConsulta(Long id, Consulta consulta);
    List<Consulta> obtenerTodas();
    Optional<Consulta> obtenerPorId(Long id);
    void eliminar(Long id);
    List<Consulta> obtenerPorUsuario(Long idUsuario);
    List<Consulta> obtenerPorPropiedad(Long idPropiedad);

}
