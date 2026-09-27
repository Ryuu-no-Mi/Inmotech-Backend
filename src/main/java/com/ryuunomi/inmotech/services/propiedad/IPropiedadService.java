package com.ryuunomi.inmotech.services.propiedad;

import com.ryuunomi.inmotech.dto.BusquedaDTO;
import com.ryuunomi.inmotech.dto.FacetaDTO;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IPropiedadService {

        List<Propiedad> findAll();

        Optional<Propiedad> findById(Long id);

        Propiedad save(Propiedad propiedad);

        Propiedad update(Long id, Propiedad propiedad);

        Optional<Propiedad> deleteById(Long id);

        Optional<Propiedad> delete(Usuario usuario);

        List<Propiedad> findByUsuarioId(Long idUsuario);

        List<Propiedad> findByAgenciaId(Long idAgencia);

        Page<Propiedad> findAllActivas(Pageable pageable);

        Page<Propiedad> buscarConFiltros(BusquedaDTO dto, Pageable pageable);

        FacetaDTO getFacetas(BusquedaDTO filtros);
}
