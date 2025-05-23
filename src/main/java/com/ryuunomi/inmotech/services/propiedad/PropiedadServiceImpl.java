package com.ryuunomi.inmotech.services.propiedad;

import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.repository.PropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropiedadServiceImpl implements IPropiedadService {

    /**
     * Spring Boot, gracias a la anotación @Autowired, inyecta automáticamente una clase que implemente IPropiedadService
     */
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
    public Optional<Propiedad> update(Long id, Propiedad propiedad) {
        Optional<Propiedad> propiedadOptional = propiedadRepository.findById(id);
        if (propiedadOptional.isPresent()) {
            Propiedad propiedadDB = propiedadOptional.get();

            // Actualizas los campos necesarios
            propiedadDB.setTitulo(propiedad.getTitulo());
            propiedadDB.setDescripcion(propiedad.getDescripcion());
            propiedadDB.setPrecio(propiedad.getPrecio());

            // Guardas y devuelves el objeto actualizado
            return Optional.of(propiedadRepository.save(propiedadDB));
        }

        // Si no se encuentra, simplemente devuelves el Optional vacío
        return Optional.empty();
    }

    @Override
    public Optional<Propiedad> deleteById(Long id) {
        Optional<Propiedad> propiedadOptional = propiedadRepository.findById(id);
        propiedadOptional.ifPresent(propiedadDB -> {
            propiedadRepository.delete(propiedadDB);
        });

        return propiedadOptional;
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
