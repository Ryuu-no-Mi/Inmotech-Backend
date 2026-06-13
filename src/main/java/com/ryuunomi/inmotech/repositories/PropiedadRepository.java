package com.ryuunomi.inmotech.repositories;

import com.ryuunomi.inmotech.entities.Propiedad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    Page<Propiedad> findByEliminadaFalse(Pageable pageable);

    Page<Propiedad> findByUsuarioIdAndEliminadaFalse(Long idUsuario, Pageable pageable);

    List<Propiedad> findByUsuarioId(Long idUsuario);

    List<Propiedad> findByAgenciaId(Long idAgencia);

    long countByUsuarioId(Long idUsuario);

    long countByAgenciaId(Long idAgencia);

    List<Propiedad> findTop2ByUsuarioIdOrderByFechaPublicacionAsc(Long usuarioId);

    List<Propiedad> findByUsuarioIdAndEliminadaFalseOrderByFechaPublicacionAsc(Long usuarioId, Pageable pageable);

    @Query("""
        SELECT p FROM Propiedad p
        JOIN p.usuario u
        JOIN u.suscripcion s
        WHERE p.eliminada = false
        AND (s.tipo = 'PREMIUM' OR s.fechaFin >= CURRENT_DATE)
        AND (:operacion IS NULL OR p.operacion = :operacion)
        AND (:ciudad IS NULL OR LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%')))
        AND (:provincia IS NULL OR LOWER(p.provincia) LIKE LOWER(CONCAT('%', :provincia, '%')))
        AND (:distrito IS NULL OR LOWER(p.distrito) LIKE LOWER(CONCAT('%', :distrito, '%')))
        AND (:barrio IS NULL OR LOWER(p.barrio) LIKE LOWER(CONCAT('%', :barrio, '%')))
        AND (:precioMin IS NULL OR p.precio >= :precioMin)
        AND (:precioMax IS NULL OR p.precio <= :precioMax)
        AND (:superficieMin IS NULL OR p.superficie >= :superficieMin)
        AND (:superficieMax IS NULL OR p.superficie <= :superficieMax)
        AND (:tipos IS NULL OR p.tipo IN :tipos)
        AND (:texto IS NULL OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(CAST(p.descripcion AS String)) LIKE LOWER(CONCAT('%', :texto, '%')))
        """)
    Page<Propiedad> buscarConFiltros(
        @Param("operacion") String operacion,
        @Param("ciudad") String ciudad,
        @Param("provincia") String provincia,
        @Param("distrito") String distrito,
        @Param("barrio") String barrio,
        @Param("precioMin") BigDecimal precioMin,
        @Param("precioMax") BigDecimal precioMax,
        @Param("superficieMin") BigDecimal superficieMin,
        @Param("superficieMax") BigDecimal superficieMax,
        @Param("tipos") List<String> tipos,
        @Param("texto") String texto,
        Pageable pageable
    );

    @Query("""
        SELECT p.ciudad, COUNT(p) FROM Propiedad p
        JOIN p.usuario u
        JOIN u.suscripcion s
        WHERE p.eliminada = false
        AND (s.tipo = 'PREMIUM' OR s.fechaFin >= CURRENT_DATE)
        AND (:operacion IS NULL OR p.operacion = :operacion)
        AND (:provincia IS NULL OR LOWER(p.provincia) LIKE LOWER(CONCAT('%', :provincia, '%')))
        AND (:ciudad IS NULL OR LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%')))
        AND (:distrito IS NULL OR LOWER(p.distrito) LIKE LOWER(CONCAT('%', :distrito, '%')))
        AND (:barrio IS NULL OR LOWER(p.barrio) LIKE LOWER(CONCAT('%', :barrio, '%')))
        AND (:precioMin IS NULL OR p.precio >= :precioMin)
        AND (:precioMax IS NULL OR p.precio <= :precioMax)
        AND (:superficieMin IS NULL OR p.superficie >= :superficieMin)
        AND (:superficieMax IS NULL OR p.superficie <= :superficieMax)
        AND (:tipos IS NULL OR p.tipo IN :tipos)
        GROUP BY p.ciudad
        ORDER BY COUNT(p) DESC
        """)
    List<Object[]> countByCiudadGrouped(
        @Param("operacion") String operacion,
        @Param("provincia") String provincia,
        @Param("ciudad") String ciudad,
        @Param("distrito") String distrito,
        @Param("barrio") String barrio,
        @Param("precioMin") BigDecimal precioMin,
        @Param("precioMax") BigDecimal precioMax,
        @Param("superficieMin") BigDecimal superficieMin,
        @Param("superficieMax") BigDecimal superficieMax,
        @Param("tipos") List<String> tipos
    );

    @Query("""
        SELECT p.tipo, COUNT(p) FROM Propiedad p
        JOIN p.usuario u
        JOIN u.suscripcion s
        WHERE p.eliminada = false
        AND (s.tipo = 'PREMIUM' OR s.fechaFin >= CURRENT_DATE)
        AND (:operacion IS NULL OR p.operacion = :operacion)
        AND (:provincia IS NULL OR LOWER(p.provincia) LIKE LOWER(CONCAT('%', :provincia, '%')))
        AND (:ciudad IS NULL OR LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%')))
        AND (:distrito IS NULL OR LOWER(p.distrito) LIKE LOWER(CONCAT('%', :distrito, '%')))
        AND (:barrio IS NULL OR LOWER(p.barrio) LIKE LOWER(CONCAT('%', :barrio, '%')))
        AND (:precioMin IS NULL OR p.precio >= :precioMin)
        AND (:precioMax IS NULL OR p.precio <= :precioMax)
        AND (:superficieMin IS NULL OR p.superficie >= :superficieMin)
        AND (:superficieMax IS NULL OR p.superficie <= :superficieMax)
        AND (:tipos IS NULL OR p.tipo IN :tipos)
        GROUP BY p.tipo
        ORDER BY COUNT(p) DESC
        """)
    List<Object[]> countByTipoGrouped(
        @Param("operacion") String operacion,
        @Param("provincia") String provincia,
        @Param("ciudad") String ciudad,
        @Param("distrito") String distrito,
        @Param("barrio") String barrio,
        @Param("precioMin") BigDecimal precioMin,
        @Param("precioMax") BigDecimal precioMax,
        @Param("superficieMin") BigDecimal superficieMin,
        @Param("superficieMax") BigDecimal superficieMax,
        @Param("tipos") List<String> tipos
    );

    @Query("""
        SELECT p.distrito, COUNT(p) FROM Propiedad p
        JOIN p.usuario u
        JOIN u.suscripcion s
        WHERE p.eliminada = false
        AND (s.tipo = 'PREMIUM' OR s.fechaFin >= CURRENT_DATE)
        AND (:operacion IS NULL OR p.operacion = :operacion)
        AND (:ciudad IS NULL OR LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%')))
        AND (:tipos IS NULL OR p.tipo IN :tipos)
        AND (:precioMin IS NULL OR p.precio >= :precioMin)
        AND (:precioMax IS NULL OR p.precio <= :precioMax)
        AND (:superficieMin IS NULL OR p.superficie >= :superficieMin)
        AND (:superficieMax IS NULL OR p.superficie <= :superficieMax)
        AND p.distrito IS NOT NULL
        GROUP BY p.distrito
        ORDER BY COUNT(p) DESC
        """)
    List<Object[]> countByDistritoGrouped(
        @Param("operacion") String operacion,
        @Param("ciudad") String ciudad,
        @Param("tipos") List<String> tipos,
        @Param("precioMin") BigDecimal precioMin,
        @Param("precioMax") BigDecimal precioMax,
        @Param("superficieMin") BigDecimal superficieMin,
        @Param("superficieMax") BigDecimal superficieMax
    );

    @Query("""
        SELECT p.barrio, COUNT(p) FROM Propiedad p
        JOIN p.usuario u
        JOIN u.suscripcion s
        WHERE p.eliminada = false
        AND (s.tipo = 'PREMIUM' OR s.fechaFin >= CURRENT_DATE)
        AND (:operacion IS NULL OR p.operacion = :operacion)
        AND (:ciudad IS NULL OR LOWER(p.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%')))
        AND (:distrito IS NULL OR LOWER(p.distrito) LIKE LOWER(CONCAT('%', :distrito, '%')))
        AND (:tipos IS NULL OR p.tipo IN :tipos)
        AND (:precioMin IS NULL OR p.precio >= :precioMin)
        AND (:precioMax IS NULL OR p.precio <= :precioMax)
        AND (:superficieMin IS NULL OR p.superficie >= :superficieMin)
        AND (:superficieMax IS NULL OR p.superficie <= :superficieMax)
        AND p.barrio IS NOT NULL
        GROUP BY p.barrio
        ORDER BY COUNT(p) DESC
        """)
    List<Object[]> countByBarrioGrouped(
        @Param("operacion") String operacion,
        @Param("ciudad") String ciudad,
        @Param("distrito") String distrito,
        @Param("tipos") List<String> tipos,
        @Param("precioMin") BigDecimal precioMin,
        @Param("precioMax") BigDecimal precioMax,
        @Param("superficieMin") BigDecimal superficieMin,
        @Param("superficieMax") BigDecimal superficieMax
    );

}
