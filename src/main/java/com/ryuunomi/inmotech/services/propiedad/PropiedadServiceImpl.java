package com.ryuunomi.inmotech.services.propiedad;

import com.ryuunomi.inmotech.dto.BusquedaDTO;
import com.ryuunomi.inmotech.dto.FacetaDTO;
import com.ryuunomi.inmotech.entities.ImagenPropiedad;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.repositories.AgenciaRepository;
import com.ryuunomi.inmotech.repositories.ImagenPropiedadRepository;
import com.ryuunomi.inmotech.repositories.PropiedadRepository;
import com.ryuunomi.inmotech.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PropiedadServiceImpl implements IPropiedadService {

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private AgenciaRepository agenciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ImagenPropiedadRepository imagenPropiedadRepository;


    @Override
    public List<Propiedad> findAll() {
//        return propiedadRepository.findAll();
        List<Propiedad> todas = propiedadRepository.findAll();
        for (Propiedad p : todas) {
            List<ImagenPropiedad> imgs = imagenPropiedadRepository.findByPropiedadId(p.getId());
            p.setImagenes(imgs);
            imgs.stream()
                    .filter(img -> img.getOrden() != null && img.getOrden() == 0)
                    .findFirst()
                    .ifPresent(p::setImagenPortada);
        }
        return todas;
    }

    @Override
    public Optional<Propiedad> findById(Long id) {
//        Optional<Propiedad> propiedadOpt = propiedadRepository.findById(id);
//        return propiedadRepository.findById(id);
        Optional<Propiedad> opt = propiedadRepository.findById(id);
        if (opt.isEmpty()) {
            return Optional.empty();
        }
        Propiedad p = opt.get();
        List<ImagenPropiedad> imgs = imagenPropiedadRepository.findByPropiedadId(p.getId());
        p.setImagenes(imgs);

        if (!imgs.isEmpty()) {
            p.setImagenPortada(imgs.get(0));

        }
        return Optional.of(p);
    }

    
    @Override
    public Propiedad save(Propiedad propiedad) {
        //Verificar que hay un usuario o idUsuario
        if (propiedad.getUsuario() == null ||
                propiedad.getUsuario().getId() == null) {
            throw new IllegalArgumentException("ERROR: No se ha especificado un usuario");
        }

        if (!usuarioRepository.existsById(propiedad.getUsuario().getId())) {
            throw new IllegalArgumentException("El usuario especificado no existe");
        }


        if (propiedad.getAgencia() != null &&
                propiedad.getAgencia().getId() != null &&
                !agenciaRepository.existsById(propiedad.getAgencia().getId())) {
            throw new IllegalArgumentException("La agencia especificada no existe");
        }


        Propiedad propGuardada = propiedadRepository.save(propiedad);


        List<ImagenPropiedad> nuevasImagenes = new ArrayList<>();

        if (propiedad.getImagenes() != null) {
            int orden = 0;
            for (ImagenPropiedad img : propiedad.getImagenes()) {
                img.setPropiedad(propGuardada);
                img.setOrden(orden++);
                ImagenPropiedad imgGuardada = imagenPropiedadRepository.save(img);
                nuevasImagenes.add(imgGuardada);
            }
        }

        propGuardada.setImagenes(nuevasImagenes);


        if (!nuevasImagenes.isEmpty()) {
            propGuardada.setImagenPortada(nuevasImagenes.get(0));
        }

        return propGuardada;
    }

    @Override
    @Transactional
    public Propiedad update(Long id, Propiedad propiedad) {
        Propiedad existente = propiedadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propiedad no encontrada"));

        existente.setTitulo(propiedad.getTitulo());
        existente.setDescripcion(propiedad.getDescripcion());
        existente.setPrecio(propiedad.getPrecio());
        existente.setSuperficie(propiedad.getSuperficie());
        existente.setDireccion(propiedad.getDireccion());
        existente.setCiudad(propiedad.getCiudad());
        existente.setProvincia(propiedad.getProvincia());
        existente.setCodigoPostal(propiedad.getCodigoPostal());
        existente.setLatitud(propiedad.getLatitud());
        existente.setLongitud(propiedad.getLongitud());
        existente.setImagenPortada(propiedad.getImagenPortada());

        //existente.setUsuario(propiedad.getUsuario());
        //existente.setAgencia(propiedad.getAgencia());


        // Actualizar agencia
        if (propiedad.getAgencia() != null && propiedad.getAgencia().getId() != null) {
            if (!agenciaRepository.existsById(propiedad.getAgencia().getId())) {
                throw new IllegalArgumentException("La agencia especificada no existe");
            }
            existente.setAgencia(propiedad.getAgencia());
        } else {
            existente.setAgencia(null);
        }


        // poner id_imagen_portada = NULL para poder modificar la bd
        existente.setImagenPortada(null);
        // Con saveAndFlush() forzamos a que Hibernate envíe ya el UPDATE a la base
        propiedadRepository.saveAndFlush(existente);
        // A este punto, MySQL ya habrá hecho:
        //   UPDATE propiedad
        //     SET id_imagen_portada = NULL
        //   WHERE id = :id


        // Borrar todos los registros previos de ImagenPropiedad de esta Propiedad
        imagenPropiedadRepository.deleteByPropiedadId(id);
        // equivalente a: DELETE FROM imagenes_propiedades WHERE id_propiedad = :id

        // Limpiamos también la lista en memoria (opcional; por si usamos el getter más adelante)
        existente.getImagenes().clear();


        // nuevas imágenes que vienen en el JSON
        List<ImagenPropiedad> nuevasImagenes = new ArrayList<>();
        if (propiedad.getImagenes() != null) {
            int orden = 0;
            for (ImagenPropiedad imgIn : propiedad.getImagenes()) {
                ImagenPropiedad nueva = new ImagenPropiedad();
                // Sólo copiamos URL y orden; no “reutilizamos” la entidad antigua
                nueva.setUrl(imgIn.getUrl());
                nueva.setOrden(orden++);
                nueva.setPropiedad(existente);
                ImagenPropiedad guardada = imagenPropiedadRepository.save(nueva);
                nuevasImagenes.add(guardada);
            }
        }
        existente.setImagenes(nuevasImagenes);

        if (!nuevasImagenes.isEmpty()) {
            existente.setImagenPortada(nuevasImagenes.get(0));
        }

        return propiedadRepository.save(existente);
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
            return Optional.of(propiedades.get(0));
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

    @Override
    public Page<Propiedad> findAllActivas(Pageable pageable) {
        return propiedadRepository.findByEliminadaFalse(pageable);
    }

    @Override
    public Page<Propiedad> buscarConFiltros(BusquedaDTO dto, Pageable pageable) {
        BigDecimal precioMin = dto.precioMin() != null && !dto.precioMin().isBlank()
            ? new BigDecimal(dto.precioMin()) : null;
        BigDecimal precioMax = dto.precioMax() != null && !dto.precioMax().isBlank()
            ? new BigDecimal(dto.precioMax()) : null;
        BigDecimal superficieMin = dto.superficieMin() != null && !dto.superficieMin().isBlank()
            ? new BigDecimal(dto.superficieMin()) : null;
        BigDecimal superficieMax = dto.superficieMax() != null && !dto.superficieMax().isBlank()
            ? new BigDecimal(dto.superficieMax()) : null;

        return propiedadRepository.buscarConFiltros(
            dto.ciudad(), dto.provincia(), precioMin, precioMax,
            superficieMin, superficieMax, dto.tipo(), dto.texto(),
            pageable
        );
    }

    @Override
    public FacetaDTO getFacetas() {
        Map<String, Long> ciudades = new HashMap<>();
        for (Object[] row : propiedadRepository.countByCiudadGrouped()) {
            ciudades.put((String) row[0], (Long) row[1]);
        }

        Map<String, Long> tipos = new HashMap<>();
        for (Object[] row : propiedadRepository.countByTipoGrouped()) {
            tipos.put((String) row[0], (Long) row[1]);
        }

        return new FacetaDTO(ciudades, tipos);
    }


}
