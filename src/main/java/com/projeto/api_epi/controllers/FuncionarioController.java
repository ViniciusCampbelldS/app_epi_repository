package com.projeto.api_epi.controllers;

import com.projeto.api_epi.entities.Funcionario;
import com.projeto.api_epi.repositories.FuncionarioRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FuncionarioController {

	private final FuncionarioRepository repository;

	public FuncionarioController(FuncionarioRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/funcionarios")
	public List<Funcionario> listar() {
		return repository.findAll();
	}
}