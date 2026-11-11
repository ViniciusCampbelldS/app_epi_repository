package com.projeto.api_epi.service;

import com.projeto.api_epi.entities.Funcionario;
import com.projeto.api_epi.entities.Treinamento;
import com.projeto.api_epi.entities.TreinamentoRealizado;
import com.projeto.api_epi.repositories.FuncionarioRepository;
import com.projeto.api_epi.repositories.TreinamentoRealizadoRepository;
import com.projeto.api_epi.repositories.TreinamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TreinamentoService {

	private final TreinamentoRepository treinamentoRepository;
	private final TreinamentoRealizadoRepository realizadoRepository;
	private final FuncionarioRepository funcionarioRepository;
	private final AcessoService acessoService;

	public TreinamentoService(TreinamentoRepository treinamentoRepository,
							  TreinamentoRealizadoRepository realizadoRepository,
							  FuncionarioRepository funcionarioRepository,
							  AcessoService acessoService) {
		this.treinamentoRepository = treinamentoRepository;
		this.realizadoRepository = realizadoRepository;
		this.funcionarioRepository = funcionarioRepository;
		this.acessoService = acessoService;
	}

	public List<Treinamento> listar() {
		return treinamentoRepository.findAllByOrderByTituloAsc();
	}

	// Só o administrador cadastra treinamentos
	public Treinamento cadastrar(Treinamento treinamento, Long adminId) {
		acessoService.verificarAdmin(adminId);

		if (treinamento.getTitulo() == null
				|| treinamento.getTitulo().isBlank()) {
			throw new IllegalArgumentException("Informe o título do treinamento");
		}

		treinamento.setId(null); // sempre um registro novo
		return treinamentoRepository.save(treinamento);
	}

	// Registra que o funcionário concluiu o treinamento
	public TreinamentoRealizado concluir(Long treinamentoId, Long funcionarioId) {
		Funcionario funcionario = funcionarioRepository
				.findById(funcionarioId)
				.orElseThrow(() ->
						new IllegalArgumentException("Funcionário não encontrado"));

		Treinamento treinamento = treinamentoRepository
				.findById(treinamentoId)
				.orElseThrow(() ->
						new IllegalArgumentException("Treinamento não encontrado"));

		// Só bloqueia se a última conclusão ainda estiver válida
		boolean aindaValido = realizadoRepository
				.findByFuncionarioIdOrderByDataConclusaoDesc(funcionario.getId())
				.stream()
				.filter(r -> r.getTreinamento().getId().equals(treinamento.getId()))
				.findFirst()
				.map(r -> treinamento.getValidadeMeses() == null
						|| LocalDate.now().isBefore(
								r.getDataConclusao().plusMonths(treinamento.getValidadeMeses())))
				.orElse(false);

		if (aindaValido) {
			throw new IllegalArgumentException(
					"Este treinamento já foi concluído");
		}

		TreinamentoRealizado registro = new TreinamentoRealizado();
		registro.setFuncionario(funcionario);
		registro.setTreinamento(treinamento);
		registro.setDataConclusao(LocalDate.now());

		return realizadoRepository.save(registro);
	}

	public List<TreinamentoRealizado> listarRealizados(Long funcionarioId) {
		return realizadoRepository
				.findByFuncionarioIdOrderByDataConclusaoDesc(funcionarioId);
	}
}