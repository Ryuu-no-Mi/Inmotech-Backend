package com.ryuunomi.inmotech.services.agencia;

import com.ryuunomi.inmotech.entities.Agencia;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IAgenciaService {

    List<Agencia> listAll();

    Optional<Agencia> findById(Long id);

    Agencia save(Agencia agencia);

    void delete(Long id);

}
