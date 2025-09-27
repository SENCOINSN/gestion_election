package com.sid.gl.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    Page<User> findAllByOrderByIdDesc(Pageable pageable);

    //requete ou tu recupere l'utilisateur soit par email ou username
    //@Query("SELECT u FROM User u WHERE u.email = :email OR u.username = :username")
    //Optional<User> findByEmailOrUsername(@Param("email") String email, @Param("username") String username);

    @Query("select u from User u where u.username = ?1 or u.email = ?2")
    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    @Query("select u from User u inner join u.roles roles where roles.roleName = ?1")
    List<User> findByRoles_RoleName(String roleName);


    //liste des candidats (admin)

    //liste des superviseurs (admin)


}
