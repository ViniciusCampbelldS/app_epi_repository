package com.projeto.api_epi.repositories;
//Na pasta com.senac.api_epi.repositories, crie três interfaces. O Spring monta a consulta SQL a partir do nome do método: findByMatricula vira WHERE matricula = ?.
import com.projeto.api_epi.entities.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

	Optional<Funcionario> findByMatricula(String matricula);

}