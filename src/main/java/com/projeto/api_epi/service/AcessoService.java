package com.projeto.api_epi.service;

import com.projeto.api_epi.entities.Funcionario;
import com.projeto.api_epi.repositories.FuncionarioRepository;
import org.springframework.stereotype.Service;

// Regra de acesso centralizada: se ela mudar, muda so aqui.
@Service
public class AcessoService {

	private final FuncionarioRepository funcionarioRepository;

	public AcessoService(FuncionarioRepository funcionarioRepository) {
		this.funcionarioRepository = funcionarioRepository;
	}

	public void verificarAdmin(Long adminId) {
		Funcionario usuario = funcionarioRepository
				.findById(adminId)
				.orElseThrow(() ->
						new SecurityException("Usuário não encontrado"));

		if (!"ADMIN".equals(usuario.getPerfil())) {
			throw new SecurityException(
					"Apenas administradores podem fazer esta operação");
		}
	}
}
