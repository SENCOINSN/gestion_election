package com.sid.gl.elections.scrutins;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ScrutinRepository extends JpaRepository<Scrutin, Long> {
    @Query("select s from Scrutin s where s.bulletinId = ?1 and s.electeurId = ?2")
    Optional<Scrutin> findByBulletinIdAndElecteurId(Long bulletinId, Long electeurId);

}
