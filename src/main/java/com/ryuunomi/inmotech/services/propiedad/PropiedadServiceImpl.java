package com.ryuunomi.inmotech.services.propiedad;

import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.repository.PropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropiedadServiceImpl implements IPropiedadService{
    @Autowired
    private PropiedadRepository propiedadRepository;

    /*
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
    public Propiedad update(Long id, Propiedad propiedad) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        Optional<Propiedad> optionalPropiedad = propiedadRepository.findById(id);
        //return propiedadRepository.deleteById(optionalPropiedad.get().getId());
        propiedadRepository.delete(optionalPropiedad.get());
    }


    @Override
    public List<Propiedad> findByUsuarioId(Long idUsuario) {
        return propiedadRepository.findByUsuarioId(idUsuario);
    }

    @Override
    public List<Propiedad> findByAgenciaId(Long idAgencia) {
        return propiedadRepository.findByAgenciaId(idAgencia);
    }

     */



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
        public Propiedad update(Long id, Propiedad propiedad) {
            Optional<Propiedad> optional = propiedadRepository.findById(id);
            if (optional.isPresent()) {
                Propiedad existente = optional.get();
                existente.setTitulo(propiedad.getTitulo());
                existente.setDescripcion(propiedad.getDescripcion());
                existente.setPrecio(propiedad.getPrecio());
                //existente.setUbicacion(propiedad.getUbicacion());
                existente.setUsuario(propiedad.getUsuario());
                existente.setAgencia(propiedad.getAgencia());
                // Agrega más setters si hay más campos
                return propiedadRepository.save(existente);
            }
            return null;
        }

        @Override
        public Optional<Propiedad> deleteById(Long id) {
            Optional<Propiedad> optionalPropiedad = propiedadRepository.findById(id);
            if (optionalPropiedad.isPresent()) {
                propiedadRepository.delete(optionalPropiedad.get());
            }
            return optionalPropiedad;
        }

        @Override
        public Optional<Propiedad> delete(Usuario usuario) {
            List<Propiedad> propiedades = propiedadRepository.findByUsuarioId(usuario.getId());
            if (!propiedades.isEmpty()) {
                propiedadRepository.deleteAll(propiedades);
                return Optional.of(propiedades.get(0)); // o cambiar la lógica según necesidad
            }
            return Optional.empty();
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
