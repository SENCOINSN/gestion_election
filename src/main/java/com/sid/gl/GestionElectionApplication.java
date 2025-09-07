package com.sid.gl;

import com.sid.gl.users.Role;
import com.sid.gl.users.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class GestionElectionApplication implements CommandLineRunner {
/*	@Autowired
   private RoleRepository roleRepository;*/
	public static void main(String[] args) {
		SpringApplication.run(GestionElectionApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception {
		/*List<String> names = List.of("ELECTOR","CANDIDAT","ADMIN","SUPERVISOR");
		names.forEach(name->{
			Role role = new Role();
			role.setRoleName(name);
			roleRepository.save(role);
		});*/
	}
}
