package com.ryuunomi.inmotech.mapper;

import com.ryuunomi.inmotech.dto.ImagenPropiedadDTO;
import com.ryuunomi.inmotech.dto.PropiedadDTO;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Agencia;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.entities.ImagenPropiedad;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PropiedadMapper {

    public static PropiedadDTO toDTO(Propiedad p) {

        List<ImagenPropiedadDTO> imagenesDTO = p.getImagenes() != null
                ? p.getImagenes().stream()
                .map(img -> new ImagenPropiedadDTO(img.getId(),img.getUrl(), img.getOrden()))
                .toList()
                : new ArrayList<>();;

        return new PropiedadDTO(
                p.getId(),
                p.getTitulo(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getSuperficie(),
                p.getDireccion(),
                p.getCiudad(),
                p.getProvincia(),
                p.getCodigoPostal(),
                p.getLatitud(),
                p.getLongitud(),
                p.getFechaPublicacion() != null ? p.getFechaPublicacion().toString() : null,
                p.getUsuario() != null ? p.getUsuario().getId() : null,
                p.getAgencia() != null ? p.getAgencia().getId() : null,
                imagenesDTO,
                p.getTipo(),
                p.getOperacion(),
                p.getDistrito(),
                p.getBarrio(),
                p.getHabitaciones(),
                p.getBanos(),
                p.getParking(),
                p.getAnoConstruccion(),
                p.getEstado() != null ? p.getEstado().name() : null,
                p.getCertificacionEnergetica() != null ? p.getCertificacionEnergetica().name() : null
        );
    }

    public static Propiedad fromDTO(PropiedadDTO dto) {
        Propiedad p = new Propiedad();
        p.setId(dto.id());
        p.setTitulo(dto.titulo());
        p.setDescripcion(dto.descripcion());
        p.setPrecio(dto.precio());
        p.setSuperficie(dto.superficie());
        p.setDireccion(dto.direccion());
        p.setCiudad(dto.ciudad());
        p.setProvincia(dto.provincia());
        p.setCodigoPostal(dto.codigoPostal());
        p.setLatitud(dto.latitud());
        p.setLongitud(dto.longitud());
        p.setTipo(dto.tipo());
        p.setOperacion(dto.operacion());
        p.setDistrito(dto.distrito());
        p.setBarrio(dto.barrio());
        p.setHabitaciones(dto.habitaciones());
        p.setBanos(dto.banos());
        p.setParking(dto.parking());
        p.setAnoConstruccion(dto.anoConstruccion());
        if (dto.estado() != null) {
            p.setEstado(com.ryuunomi.inmotech.enums.EstadoPropiedad.valueOf(dto.estado()));
        }
        if (dto.certificacionEnergetica() != null) {
            p.setCertificacionEnergetica(com.ryuunomi.inmotech.enums.CertificacionEnergetica.valueOf(dto.certificacionEnergetica()));
        }


        if (dto.idUsuario() != null) {
            Usuario u = new Usuario();
            u.setId(dto.idUsuario());
            p.setUsuario(u);
        }

        if (dto.idAgencia() != null) {
            Agencia a = new Agencia();
            a.setId(dto.idAgencia());
            p.setAgencia(a);
        }

        if (dto.imagenes() != null) {
            List<ImagenPropiedad> imagenes = dto.imagenes().stream()
                    .map(imgDto -> {
                        ImagenPropiedad img = new ImagenPropiedad();
                        img.setUrl(imgDto.url());
                        img.setOrden(imgDto.orden());
                        img.setPropiedad(p);
                        return img;
                    }).collect(Collectors.toList());
            p.setImagenes(imagenes);
        }

        return p;
    }
} 