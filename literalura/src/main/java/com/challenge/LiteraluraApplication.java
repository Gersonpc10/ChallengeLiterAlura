package com.challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.challenge.principal.Principal;
import com.challenge.repository.LibroRepository;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {
	//Inyección de dependencia @Autowired
	@Autowired
	private LibroRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception {
		//Inyecta en el constructor de Principal()
		Principal principal = new Principal(repository);
		principal.muestraElMenu();


	}
}
