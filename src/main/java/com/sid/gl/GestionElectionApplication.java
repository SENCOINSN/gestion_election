package com.sid.gl;

import com.sid.gl.users.Role;
import com.sid.gl.users.RoleRepository;
import com.sid.gl.users.User;
import com.sid.gl.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

//@EnableAsync
@EnableScheduling
@SpringBootApplication
public class GestionElectionApplication implements CommandLineRunner {
	@Autowired
   private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;
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

		//todo create admin user
		if(!userRepository.existsByUsername("admin")){
			User user = new User();
			user.setFirstName("pape");
			user.setLastName("Seye");
			user.setEmail("admin@gmail.com");
			user.setUsername("admin");
			user.setPassword(bCryptPasswordEncoder().encode("PapeDemba123@"));
			user.setRoles(Set.of(roleRepository.findByRoleName("ADMIN").get()));
			userRepository.save(user);
		}



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

		//done : implementation du process souscription candidat (supervisor) (creation du bulletin du candidat)
		// todo upload une image d'un candidat
		// progress un cron qui va déclencher l'ouverture d'une election à la date venue à chaque 9h
		//done  process envoi mail de notification aux électeurs de la plateforme

		// todo new feature:
		// todo mettre le process de scrutin
		// todo mettre un listener pour chaque vote effectué
		//todo dans le listener, vérifier si l'election est terminée
		// todo si election non terminée, on va mettre le process de scrutin et sauvegarder le scrutin
		//todo si election terminée, on va sauvegarder le scrutin et fermer l'election,
		//todo comptage des voix sur chaque bulletin du scrutin et custom de la liste des candidats avec le nombre de voix
		// generation du pdf de résultat
		//todo soit envoyer au superviseur ou envoyer aux electeurs
		// qui met fin au process de scrutin




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
