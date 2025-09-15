package com.sid.gl.elections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ElectionRepository extends JpaRepository<Election, Long> {
    @Query("select e from Election e where e.name = ?1")
    Optional<Election> findByName(String name);

    List<Election> findByActive(boolean active);

    @Query("select e from Election e where e.active = true")
    List<Election> findByActiveTrue();  // select * from election where active = true

    @Query("select e from Election e where e.active = true")
    List<ElectionInfoProjection> getElectionIsActive();


    // select id, name, start_date from election where active = true


}
