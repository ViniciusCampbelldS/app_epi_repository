package com.projeto.api_epi.service;

import com.projeto.api_epi.repositories.EmprestimoRepository;
import com.projeto.api_epi.repositories.EpiRepository;
import com.projeto.api_epi.repositories.FuncionarioRepository;
import com.projeto.api_epi.repositories.TreinamentoRealizadoRepository;
import com.projeto.api_epi.repositories.TreinamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

	private final EpiRepository epiRepository;
	private final EmprestimoRepository emprestimoRepository;
	private final FuncionarioRepository funcionarioRepository;
	private final TreinamentoRepository treinamentoRepository;
	private final TreinamentoRealizadoRepository realizadoRepository;
	private final AcessoService acessoService;

	public DashboardService(EpiRepository epiRepository,
							EmprestimoRepository emprestimoRepository,
							FuncionarioRepository funcionarioRepository,
							TreinamentoRepository treinamentoRepository,
							TreinamentoRealizadoRepository realizadoRepository,
							AcessoService acessoService) {
		this.epiRepository = epiRepository;
		this.emprestimoRepository = emprestimoRepository;
		this.funcionarioRepository = funcionarioRepository;
		this.treinamentoRepository = treinamentoRepository;
		this.realizadoRepository = realizadoRepository;
		this.acessoService = acessoService;
	}

	public Map<String, Long> indicadores(Long adminId) {
		acessoService.verificarAdmin(adminId);

		long pendentes = funcionarioRepository.count() * treinamentoRepository.count()
				- realizadoRepository.count();

		Map<String, Long> dados = new LinkedHashMap<>();
		dados.put("episCadastrados", epiRepository.count());
		dados.put("episEmprestados", emprestimoRepository.countByStatus("EMPRESTADO"));
		dados.put("episDevolvidos", emprestimoRepository.countByStatus("DEVOLVIDO"));
		dados.put("caVencidos", epiRepository.countByValidadeCaBefore(LocalDate.now()));
		dados.put("solicitacoesPendentes", emprestimoRepository.countByStatus("SOLICITADO"));
		dados.put("treinamentosPendentes", pendentes);
		return dados;
	}
}
