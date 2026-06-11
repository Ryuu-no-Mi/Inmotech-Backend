package com.ryuunomi.inmotech.repositories;

import com.ryuunomi.inmotech.entities.Usuario;
import com.ryuunomi.inmotech.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Usuario> findByProviderAndProviderId(AuthProvider provider, String providerId);

    // Usuarios con suscripción próxima a expirar (para job semanal)
    @Query("SELECT u FROM Usuario u WHERE u.fechaExpiracionPremium IS NOT NULL AND u.fechaExpiracionPremium <= :fecha")
    List<Usuario> findByFechaExpiracionPremiumBefore(@Param("fecha") LocalDate fecha);

}
