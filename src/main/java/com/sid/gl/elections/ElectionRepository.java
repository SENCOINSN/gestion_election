package com.sid.gl.elections;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectionRepository extends JpaRepository<Election, Long> {
    <Optional> Election findByName(String name);
        
}
