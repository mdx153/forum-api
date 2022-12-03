package br.com.maycon.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.maycon.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.maycon.forum.controller.dto.TopicoDto;
import br.com.maycon.forum.form.AtualizacaoTopicoForm;
import br.com.maycon.forum.form.TopicoForm;
import br.com.maycon.forum.modelo.Topico;
import br.com.maycon.forum.repository.CursoRepository;
import br.com.maycon.forum.repository.TopicoRepository;

//restcontroller diz para o spring que todo retorno de método não será buscado em uma pagina
@RestController
@RequestMapping("/topicos") //tipo um prefixo, pega tudo /topicos
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	//essa é uma forma
//	@RequestMapping(value="/topicos", method = RequestMethod.GET)
	@GetMapping
	@Cacheable(value = "listaDeTopicos") //diz para guardar cache e usa uma id que é o value
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, 
			@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10 ) Pageable paginacao) {
		
		//@RequestParam torna obrigatorio a passagem de parametro na url
		//quando tem o required = false essa obrigatoridade é desabilitada
		
		if(nomeCurso == null) {
			//me retorna um page que tem uma lista e outras info
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		}
		else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			return TopicoDto.converter(topicos);
		}
	}
	
	@PostMapping
	@Transactional
	//limpa o cache com o valor X e allEntries limpa tudo
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriComponentsBuilder) {
	//UriComponentsBuilder ele permite pegar a url principal do servidor e montar paths ex: localhost:8080	
	//@RequestBody diz para o spring que o parâmetro que vamos receber tem que pegar dentro do corpo da requisição.
		
		//OBS: form.converter deixa carregado todo conteudo pego no body
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		//cria url com path e variavel dinamica
		URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		
		//da uma resposta 201 e precisa passar a nova url criada e seu corpo que foi salvo nela
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		//@PathVariable indica que a variável vira na url sem o ?. ex: /tópicos/1
		Optional<Topico> topico = topicoRepository.findById(id); //retorna um optional
		
		if(topico.isPresent()) {
			//
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form){
		//verifica se existe no banco
		Optional<Topico> optional = topicoRepository.findById(id); //retorna um optional
		
		if(optional.isPresent()) {
			Topico topico = form.atualizar(id,topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		return ResponseEntity.notFound().build();		
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	//? é para dizer que é um generico que não sabemos
	public ResponseEntity<?> remover(@PathVariable Long id){
		
		// verifica se existe no banco
		Optional<Topico> optional = topicoRepository.findById(id); // retorna um optional
		if (optional.isPresent()) {
			topicoRepository.deleteById(id);
			//precisamos buildar quando não tem body
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();	
	}
}
