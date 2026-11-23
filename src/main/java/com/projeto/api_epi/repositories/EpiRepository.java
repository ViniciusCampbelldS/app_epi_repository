package com.projeto.api_epi.repositories;
import com.projeto.api_epi.entities.Epi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface EpiRepository extends JpaRepository<Epi, Long> {
    // NOVO na Aula 12 (7): quantos EPIs tem a validade antes da data atual:
    long countByValidadeBefore(LocalDate data);
}