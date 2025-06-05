package com.ryuunomi.inmotech.repository;

import com.ryuunomi.inmotech.entities.ImagenPropiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenPropiedadRepository extends JpaRepository<ImagenPropiedad, Long> {
    List<ImagenPropiedad> findByPropiedadId(Long propiedadId);
    //void deleteByPropiedadId(Long propiedadId);
    @Modifying
    @Query("DELETE FROM ImagenPropiedad i WHERE i.propiedad.id = :propiedadId")
    void deleteByPropiedadId(@Param("propiedadId") Long propiedadId);
}
