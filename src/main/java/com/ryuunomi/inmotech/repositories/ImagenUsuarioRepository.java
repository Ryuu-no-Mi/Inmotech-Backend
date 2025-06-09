package com.ryuunomi.inmotech.repositories;

import com.ryuunomi.inmotech.entities.ImagenUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenUsuarioRepository extends JpaRepository<ImagenUsuario, Long> {
    ImagenUsuario findByUsuarioId(Long userId);
}