package com.ryuunomi.inmotech.repository;

import com.ryuunomi.inmotech.entities.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenciaRepository extends JpaRepository<Agencia, Long> {

}
