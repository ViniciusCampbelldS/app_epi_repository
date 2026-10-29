package com.projeto.api_epi.controllers;
import com.projeto.api_epi.entities.Epi;
import com.projeto.api_epi.repositories.EpiRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EpiController {

	private final EpiRepository epiRepository;

	public EpiController(EpiRepository epiRepository) {
		this.epiRepository = epiRepository;
	}

	@GetMapping("/epis")
	public List<Epi> listar() {
		return epiRepository.findAll();
	}
}
