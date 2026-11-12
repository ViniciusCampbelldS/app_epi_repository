package com.projeto.api_epi.repositories;
import com.projeto.api_epi.entities.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

	// Empréstimos de um funcionário, do mais recente para o mais antigo
	List<Emprestimo> findByFuncionarioIdOrderByIdDesc(Long funcionarioId);

	// O funcionário já está com este EPI e ainda não devolveu?
	boolean existsByFuncionarioIdAndEpiIdAndStatus(
			Long funcionarioId,
			Long epiId,
			String status
	);

	// NOVO na Aula 7: quantos empréstimos estão com este status
	long countByStatus(String status);

	// NOVO no exercicio da Aula 7: emprestimos de um funcionario com este status
	long countByFuncionarioIdAndStatus(Long funcionarioId, String status);

}