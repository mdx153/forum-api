package br.com.maycon.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.maycon.forum.modelo.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long>{

	//traz um curso
	Curso findByNome(String nome);
}
