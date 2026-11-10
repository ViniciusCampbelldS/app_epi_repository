package com.projeto.api_epi.repositories;

import com.projeto.api_epi.entities.Treinamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TreinamentoRepository extends JpaRepository<Treinamento, Long> {
	// Mét.odo que busca todos os treinamentos ordenados pelo título em ordem alfabética ascendente (A-Z)
	List<Treinamento> findAllByOrderByTituloAsc();
}