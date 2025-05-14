package com.ryuunomi.inmotech.services;

import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.repository.PropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class PropiedadServiceImpl implements IPropiedadService{
    @Autowired
    private PropiedadRepository propiedadRepository;

    @Override
    public List<Propiedad> findAll() {
        return propiedadRepository.findAll();
    }

    @Override
    public Optional<Propiedad> findById(Long id) {
        return propiedadRepository.findById(id);
    }

    @Override
    public Propiedad save(Propiedad propiedad) {
        return propiedadRepository.save(propiedad);
    }

    @Override
    public Optional<Propiedad> deleteById(Long id) {
        Optional<Propiedad> optionalPropiedad = propiedadRepository.findById(id);
        return propiedadRepository.delete(optionalPropiedad);
    }

    @Override
    public List<Propiedad> findByUsuarioId(Long idUsuario) {
        return propiedadRepository.findByUsuarioId(idUsuario);
    }

    @Override
    public List<Propiedad> findByAgenciaId(Long idAgencia) {
        return propiedadRepository.findByAgenciaId(idAgencia);
    }
}
