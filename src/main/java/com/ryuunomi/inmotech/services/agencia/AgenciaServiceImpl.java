package com.ryuunomi.inmotech.services.agencia;

import com.ryuunomi.inmotech.entities.Agencia;
import com.ryuunomi.inmotech.repository.AgenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgenciaServiceImpl implements IAgenciaService{

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Override
    public List<Agencia> listAll() {
        return agenciaRepository.findAll();
    }

    @Override
    public Optional<Agencia> findById(Long id) {
        return agenciaRepository.findById(id);
    }

    @Override
    public Agencia save(Agencia agencia) {
        return agenciaRepository.save(agencia);
    }

    @Override
    public void delete(Long id) {
        agenciaRepository.deleteById(id);
    }

}
