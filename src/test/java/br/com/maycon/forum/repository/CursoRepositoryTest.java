package br.com.maycon.forum.repository;



import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.maycon.forum.modelo.Curso;

@RunWith(SpringRunner.class) //carrega o spring
@DataJpaTest //isso aqui é especifica para testar repositorios

//aqui diz para o spring não trocar o banco de dados que está configurado na minha dependencia
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@ActiveProfiles("test") //força a ativação do profile test
public class CursoRepositoryTest {
	
	@Autowired
	private CursoRepository cursoRepository;
	
	//TestEntityManager te da alguns metodos a mais para teste
	@Autowired
	private TestEntityManager em;
	
	@org.junit.Test
	public void deveriaCarregarUmCursoAoBuscarPeloSeuNome() {
		String nomeCurso = "HTML 6";
		
		//sempre lembre de carregar dados para cada cenario pois a base esta vazia agora.
		Curso html6 = new Curso();
		html6.setNome(nomeCurso);
		html6.setCategoria("Programacao");
		em.persist(html6);
		
		Curso curso = cursoRepository.findByNome(nomeCurso);
		
		Assert.assertNotNull(curso);//verifica se não veio nulo
		//verifica se o nomeCurso passado é igual ao carregado da base.
		Assert.assertEquals(nomeCurso, curso.getNome());
	}
	
	@org.junit.Test
	public void naoDeveriaCarregarUmCursoQueNaoEstejaCadastrado() {
		String nomeCurso = "IA";
		Curso curso = cursoRepository.findByNome(nomeCurso);
		
		Assert.assertNull(curso);//verifica se veio nulo
		
	}

}
