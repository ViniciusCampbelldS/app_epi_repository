package com.projeto.api_epi.repositories;
import com.projeto.api_epi.entities.Epi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface EpiRepository extends JpaRepository<Epi, Long> {

	// NOVO na Aula 7: quantos EPIs têm a validade do CA antes da data
	long countByValidadeCaBefore(LocalDate data);
}