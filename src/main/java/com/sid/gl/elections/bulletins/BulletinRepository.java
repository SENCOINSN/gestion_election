package com.sid.gl.elections.bulletins;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BulletinRepository extends JpaRepository<Bulletin, Long> {
    Optional<Bulletin> findBulletinById(Long id);

    @Query("select b from Bulletin b where b.electionId = ?1 order by b.id")
    List<Bulletin> findByElectionIdOrderByIdAsc(Long electionId);




}
