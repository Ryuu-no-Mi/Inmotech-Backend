package com.ryuunomi.inmotech.services.propiedad;

import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IPropiedadService {
    /*
        List<Propiedad> findAll ();

        Optional<Propiedad> findById (Long id);

        Propiedad save (Propiedad propiedad);

        Propiedad update (Long id, Propiedad propiedad);

        void deleteById (Long id);

        void delete (Usuario usuario);

        List<Propiedad> findByUsuarioId (Long idUsuario);

        List<Propiedad> findByAgenciaId (Long idAgencia);
    }
     */



        List<Propiedad> findAll();

        Optional<Propiedad> findById(Long id);

        Propiedad save(Propiedad propiedad);

        Propiedad update(Long id, Propiedad propiedad);

        Optional<Propiedad> deleteById(Long id);

        Optional<Propiedad> delete(Usuario usuario);

        List<Propiedad> findByUsuarioId(Long idUsuario);

        List<Propiedad> findByAgenciaId(Long idAgencia);

}
