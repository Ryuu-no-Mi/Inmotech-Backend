package com.ryuunomi.inmotech.services.propiedad;

import com.ryuunomi.inmotech.entities.ImagenPropiedad;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.exceptions.ResourceNotFoundException;
import com.ryuunomi.inmotech.repository.AgenciaRepository;
import com.ryuunomi.inmotech.repository.ImagenPropiedadRepository;
import com.ryuunomi.inmotech.repository.PropiedadRepository;
import com.ryuunomi.inmotech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * El JSON de entrada para crear la Propiedad no suele traer URLs de imágenes, sino ficheros multipart
     * A continuación, llamar a /api/property/{id}/images para subir ficheros multipart.
     * En cambio, este método save(...) intenta leer propiedad.getImagenes() como si ya hubiese URLs en el JSON. Si el front no incluye esos URLs
     * Conclusión: No hay “error” en sí, pero la lógica asume que el JSON de creación ya trae objetos ImagenPropiedad con campo url.
     * Si el flujo real es “primero creo Propiedad, luego subo fotos con MultipartFile”, entonces la parte de if(propiedad.getImagenes()!=null)
     * quedará inactiva (no persistirá nada), lo cual es correcto, pero hay que asegurarse de que el front siga exactamente ese orden y no envie URLs en la misma petición.
     * @param propiedad
     * @return
     */
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

        //Validando si existe la agencia
        if (propiedad.getAgencia() != null &&
                propiedad.getAgencia().getId() != null &&
                !agenciaRepository.existsById(propiedad.getAgencia().getId())) {
            throw new IllegalArgumentException("La agencia especificada no existe");
        }

        // Guardar la propiedad; Hibernate asigna el ID
        Propiedad propGuardada = propiedadRepository.save(propiedad);

        //4) Si vienen imágenes en el JSON, persístelas con el FK correcto
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

        // 5) Fijar la portada como la primera (orden = 0), si existe
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

        // 1) Actualizar datos básicos
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
        //existente.setUsuario(propiedad.getUsuario());
        //existente.setAgencia(propiedad.getAgencia());

        // 2) Actualizar usuario -- por ahora no se puede, cuando hay una agencia su admin podra actualizar su id
//        if (propiedad.getUsuario() != null && propiedad.getUsuario().getId() != null) {
//            if (!usuarioRepository.existsById(propiedad.getUsuario().getId())) {
//                throw new IllegalArgumentException("El usuario especificado no existe");
//            }
//            existente.setUsuario(propiedad.getUsuario());
//        }

        // 3) Actualizar agencia
        if (propiedad.getAgencia() != null && propiedad.getAgencia().getId() != null) {
            if (!agenciaRepository.existsById(propiedad.getAgencia().getId())) {
                throw new IllegalArgumentException("La agencia especificada no existe");
            }
            existente.setAgencia(propiedad.getAgencia());
        } else {
            existente.setAgencia(null);
        }


        // 5) “Romper” la relación de portada en la BD: poner id_imagen_portada = NULL
        existente.setImagenPortada(null);
        // Con saveAndFlush() forzamos a que Hibernate envíe ya el UPDATE a la base
        propiedadRepository.saveAndFlush(existente);
        // A este punto, MySQL ya habrá hecho:
        //   UPDATE propiedad
        //     SET id_imagen_portada = NULL
        //   WHERE id = :id

        // ——————————————
        // 6) Borrar todos los registros previos de ImagenPropiedad de esta Propiedad
        imagenPropiedadRepository.deleteByPropiedadId(id);
        // equivalente a: DELETE FROM imagenes_propiedades WHERE id_propiedad = :id

        // 7) Limpiamos también la lista en memoria (opcional; por si usamos el getter más adelante)
        existente.getImagenes().clear();

        // ——————————————
        // 8) Insertar las nuevas imágenes que vienen en el JSON
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

        // ——————————————
        // 9) Si hay al menos una imagen nueva, la primera (orden=0) la convertimos en portada
        if (!nuevasImagenes.isEmpty()) {
            existente.setImagenPortada(nuevasImagenes.get(0));
        }

        // 10) Guardar y devolver
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
