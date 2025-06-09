package com.ryuunomi.inmotech.services.favorito;

import com.ryuunomi.inmotech.entities.Favorito;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IFavoritoService {

    public Favorito agregarFavorito(Long userId, Long propiedadId);

    public void eliminarFavorito(Long userId, Long propiedadId);

    public List<Favorito> obtenerFavoritosPorUsuario(Long userId);

    public boolean esFavorito(Long userId, Long propiedadId);
}
