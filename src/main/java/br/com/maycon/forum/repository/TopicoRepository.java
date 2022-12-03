package br.com.maycon.forum.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.maycon.forum.modelo.Topico;

//não precisamos colocar @Repository por que o spring já infere isso quando extend de JPA
public interface TopicoRepository extends JpaRepository<Topico, Long> {

	//pesquisa no relacionamento Curso o atributo nome
	Page<Topico> findByCursoNome(String nomeCurso, Pageable paginacao);
	
	//força a pesquisa no relacionamento Curso o atributo nome
//	List<Topico> findByCurso_Nome(String nomeCurso);
	
	
	//@query cria query personalizadas e com nossos nomes na assinatura
	//O from sempre é da tabela que está no tipo JpaRepository
	//:nomeCurso é o parametro que vamos receber
	//@Param("NomeDoParametro no select")
	@Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
	List<Topico> carregarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);
	
}
