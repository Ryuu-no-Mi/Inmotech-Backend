package com.ryuunomi.inmotech.repository;

import com.ryuunomi.inmotech.entities.Favorito;
import com.ryuunomi.inmotech.entities.Propiedad;
import com.ryuunomi.inmotech.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito,Long> {
    List<Favorito> findByUsuarioId(Long usuarioId);

    Optional<Favorito> findByUsuarioAndPropiedad(Usuario usuario, Propiedad propiedad);

    boolean existsByUsuarioAndPropiedad(Usuario usuario, Propiedad propiedad);

    void deleteByUsuarioAndPropiedad(Usuario usuario, Propiedad propiedad);

}
