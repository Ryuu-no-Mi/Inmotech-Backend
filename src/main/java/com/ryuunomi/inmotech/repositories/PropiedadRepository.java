package com.ryuunomi.inmotech.repositories;

import com.ryuunomi.inmotech.entities.Propiedad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//JpaRepository  -- Extiende a CrudRepository y también a PagingAndSortingRepository.
@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    // Métodos con soft delete (solo propiedades no eliminadas)
    Page<Propiedad> findByEliminadaFalse(Pageable pageable);

    Page<Propiedad> findByUsuarioIdAndEliminadaFalse(Long idUsuario, Pageable pageable);

    List<Propiedad> findByUsuarioId(Long idUsuario);

    List<Propiedad> findByAgenciaId(Long idAgencia);

    long countByUsuarioId(Long idUsuario);

    long countByAgenciaId(Long idAgencia);

    // Top 2 propiedades más antiguas (para usuarios con plan gratuito expirado)
    List<Propiedad> findTop2ByUsuarioIdOrderByFechaPublicacionAsc(Long usuarioId);

    // Propiedades pausadas (offset 2)
    List<Propiedad> findByUsuarioIdAndEliminadaFalseOrderByFechaPublicacionAsc(Long usuarioId, Pageable pageable);

    // Búsqueda con filtros
    @Query("""
        SELECT p FROM Propiedad p
        JOIN p.usuario u
        JOIN u.suscripcion s
        WHERE p.eliminada = false
        AND (s.tipo = 'PREMIUM' OR s.fechaFin >= CURRENT_DATE)
        AND (:ciudad IS NULL OR LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%')))
        AND (:provincia IS NULL OR LOWER(p.provincia) LIKE LOWER(CONCAT('%', :provincia, '%')))
        AND (:precioMin IS NULL OR p.precio >= :precioMin)
        AND (:precioMax IS NULL OR p.precio <= :precioMax)
        AND (:superficieMin IS NULL OR p.superficie >= :superficieMin)
        AND (:superficieMax IS NULL OR p.superficie <= :superficieMax)
        AND (:tipo IS NULL OR p.tipo = :tipo)
        AND (:texto IS NULL OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(CAST(p.descripcion AS String)) LIKE LOWER(CONCAT('%', :texto, '%')))
        """)
    Page<Propiedad> buscarConFiltros(
        @Param("ciudad") String ciudad,
        @Param("provincia") String provincia,
        @Param("precioMin") java.math.BigDecimal precioMin,
        @Param("precioMax") java.math.BigDecimal precioMax,
        @Param("superficieMin") java.math.BigDecimal superficieMin,
        @Param("superficieMax") java.math.BigDecimal superficieMax,
        @Param("tipo") String tipo,
        @Param("texto") String texto,
        Pageable pageable
    );

    // Conteo por ciudad (para facetas)
    @Query("SELECT p.ciudad, COUNT(p) FROM Propiedad p WHERE p.eliminada = false GROUP BY p.ciudad ORDER BY COUNT(p) DESC")
    List<Object[]> countByCiudadGrouped();

    // Conteo por tipo (para facetas)
    @Query("SELECT p.tipo, COUNT(p) FROM Propiedad p WHERE p.eliminada = false GROUP BY p.tipo")
    List<Object[]> countByTipoGrouped();

}
