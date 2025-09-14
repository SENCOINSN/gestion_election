package com.sid.gl;

import com.sid.gl.users.Role;
import com.sid.gl.users.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootApplication
public class GestionElectionApplication implements CommandLineRunner {
	@Autowired
   private RoleRepository roleRepository;
	public static void main(String[] args) {
		SpringApplication.run(GestionElectionApplication.class, args);
	}


	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void run(String... args) throws Exception {
		List<String> names = List.of("ELECTOR","CANDIDAT","ADMIN","SUPERVISOR");

		// depuis JAVA 8
		names.forEach(name->{
			if(!roleRepository.existsByRoleName(name)){
				Role role = new Role();
				role.setRoleName(name);
				roleRepository.save(role);
			}
		});



		//---Gitflow-----------------------------------

		// branche main ---> environment prod
		// branche dev ---> environment dev ou recette (git pull) (test qualification en environnement dev)
		// branche feature ---> environment dev (git pull)  (git checkout -b feature/add-security) (review code)
		// branche de correction --> git checkout -b fix_security (merge vers dev)
		// merge du dev vers main ( le code stabilisé) (déployé en production)

		// branche main
		// tu vas créer une branche de correction (hotfix)
		// branche hotfix_security
		// merge du hotfix sur la branche main

		// branche dev
		// tu crées le hotfix
		// merge du hotfix sur la branche dev
		// merge du dev sur la branche main



		// avant java 8 (java < 8)
		/*for(String name : names){
			if(!roleRepository.existsByRoleName(name)){
				Role role = new Role();
				role.setRoleName(name);
				roleRepository.save(role);
			}
		}*/
	}
}
