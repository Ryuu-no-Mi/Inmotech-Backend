package com.ryuunomi.inmotech.services.favorito;

import com.ryuunomi.inmotech.entities.Favorito;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.repositories.FavoritoRepository;
import com.ryuunomi.inmotech.repositories.PropiedadRepository;
import com.ryuunomi.inmotech.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoritoServiceImpl implements IFavoritoService {

    @Autowired
    private  FavoritoRepository favoritoRepo;
    @Autowired
    private  UsuarioRepository usuarioRepo;
    @Autowired
    private PropiedadRepository propiedadRepo;

    public Favorito agregarFavorito(Long userId, Long propiedadId) {
        Usuario usuario = usuarioRepo.findById(userId).orElseThrow();
        Propiedad propiedad = propiedadRepo.findById(propiedadId).orElseThrow();

        if (!favoritoRepo.existsByUsuarioAndPropiedad(usuario, propiedad)) {
            Favorito favorito = new Favorito(usuario, propiedad, java.time.LocalDateTime.now());
            return favoritoRepo.save(favorito);
        }

        return null;
    }

    public void eliminarFavorito(Long userId, Long propiedadId) {
        Usuario usuario = usuarioRepo.findById(userId).orElseThrow();
        Propiedad propiedad = propiedadRepo.findById(propiedadId).orElseThrow();
        favoritoRepo.deleteByUsuarioAndPropiedad(usuario, propiedad);
    }

    public List<Favorito> obtenerFavoritosPorUsuario(Long userId) {
        return favoritoRepo.findByUsuarioId(userId);
    }

    public boolean esFavorito(Long userId, Long propiedadId) {
        Usuario usuario = usuarioRepo.findById(userId).orElseThrow();
        Propiedad propiedad = propiedadRepo.findById(propiedadId).orElseThrow();
        return favoritoRepo.existsByUsuarioAndPropiedad(usuario, propiedad);
    }
}
