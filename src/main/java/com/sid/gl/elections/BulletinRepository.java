package com.sid.gl.elections;

import com.sid.gl.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BulletinRepository extends JpaRepository<Bulletin, Long> {
    Optional<Bulletin> findBulletinById(Long id);
    boolean existsByUserAndElection (User user, Election election);


}
