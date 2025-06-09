package com.ryuunomi.inmotech.services.consulta;

import com.ryuunomi.inmotech.entities.Consulta;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.repositories.ConsultaRepository;
import com.ryuunomi.inmotech.repositories.PropiedadRepository;
import com.ryuunomi.inmotech.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultaServiceImpl implements IConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Override
    public Consulta save(Consulta consulta) {
        // Cargar entidad Usuario completa
        Usuario usuario = usuarioRepository.findById(consulta.getUsuario().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + consulta.getUsuario().getId()));

        // Cargar entidad Propiedad completa
        Propiedad propiedad = propiedadRepository.findById(consulta.getPropiedad().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada con ID: " + consulta.getPropiedad().getId()));

        // Reasignar entidades completas a la consulta
        consulta.setUsuario(usuario);
        consulta.setPropiedad(propiedad);

        return consultaRepository.save(consulta);
    }

    @Override
    public Consulta update(Long id, Consulta datosActualizados) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta no encontrada con ID: " + id));

        consulta.setMensaje(datosActualizados.getMensaje());
        consulta.setEstado(datosActualizados.getEstado());

        return consultaRepository.save(consulta);
    }

    @Override
    public List<Consulta> list() {
        return consultaRepository.findAll();
    }

    @Override
    public Optional<Consulta> findById(Long id) {
        return consultaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        consultaRepository.deleteById(id);
    }

    @Override
    public List<Consulta> findByUser(Long idUsuario) {
        return consultaRepository.findByUsuarioId(idUsuario);
    }

    @Override
    public List<Consulta> findByProperty(Long idPropiedad) {
        return consultaRepository.findByPropiedadId(idPropiedad);
    }
}

