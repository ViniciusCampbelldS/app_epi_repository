package com.projeto.api_epi.service;
import com.projeto.api_epi.entities.Emprestimo;
import com.projeto.api_epi.entities.Epi;
import com.projeto.api_epi.entities.Funcionario;
import com.projeto.api_epi.repositories.EmprestimoRepository;
import com.projeto.api_epi.repositories.EpiRepository;
import com.projeto.api_epi.repositories.FuncionarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

	private final EmprestimoRepository emprestimoRepository;
	private final FuncionarioRepository funcionarioRepository;
	private final EpiRepository epiRepository;

	public EmprestimoService(EmprestimoRepository emprestimoRepository, FuncionarioRepository funcionarioRepository, EpiRepository epiRepository) {
		this.emprestimoRepository = emprestimoRepository;
		this.funcionarioRepository = funcionarioRepository;
		this.epiRepository = epiRepository;
	}

	public Emprestimo solicitar(Emprestimo pedido) {
		// 1. O app enviou o funcionario e o EPI com ID???
		if (pedido.getFuncionario() == null
				|| pedido.getFuncionario().getId() == null
				|| pedido.getEpi() == null
				|| pedido.getEpi().getId() == null) {
			throw new IllegalArgumentException("Informe o funcionário e o EPI. ");
		}

		// 2. O pedido chega só com os IDs: busca os dados completos no banco
		Funcionario funcionario = funcionarioRepository.findById(pedido.getFuncionario().getId())
				.orElseThrow(() ->
						new IllegalArgumentException("Funcionario não encontrado. "));
		Epi epi = epiRepository.findById(pedido.getEpi().getId())
				.orElseThrow(() ->
						new IllegalArgumentException("EPI não encontrado. "));

		// 3. Regras de Negócio (NR-06)
		if (epi.isCaVencido()) {
			throw new IllegalArgumentException(
					"EPI com CA vencido não pode ser emprestado. ");
		}
		if (epi.getQuantidadeDisponivel() <= 0) {
			throw new IllegalArgumentException("EPI sem estoque disponível. ");
		}

		// 1. Verifica se já existe uma solicitação pendente
		boolean jaSolicitou = emprestimoRepository.existsByFuncionarioIdAndEpiIdAndStatus(funcionario.getId(), epi.getId(), "SOLICITADO");

		if (jaSolicitou) {
			throw new IllegalArgumentException("Você já tem uma solicitação aberta para este EPI. ");
		}

		// 2. DESAFIO EXTRA: Verifica se o funcionário já está com o EPI emprestado ativamente
		boolean jaEmprestado = emprestimoRepository.existsByFuncionarioIdAndEpiIdAndStatus(funcionario.getId(), epi.getId(), "EMPRESTADO");

		if (jaEmprestado) {
			throw new IllegalArgumentException("Você já possui este EPI em uso e ainda não o devolveu. ");
		}

		// 4. A API decide estes campos (não confiamos no que veio do app)
		pedido.setId(null);                    // sempre um registro novo
		pedido.setFuncionario(funcionario);    // troca o "só id" pelo objeto completo
		pedido.setEpi(epi);
		pedido.setDataEmprestimo(LocalDate.now());
		pedido.setDataDevolucao(null);
		pedido.setStatus("SOLICITADO");

		// 5. Grava na tabela emprestimo
		return emprestimoRepository.save(pedido);
	}
	public List<Emprestimo> listarPorFuncionario(Long funcionarioId) {
		return emprestimoRepository.findByFuncionarioIdOrderByIdDesc(funcionarioId);
	}
}
