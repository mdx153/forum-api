package br.com.maycon.forum.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//@WebMvcTest //é usado quando queremos só testar controller, só carrega eles
@RunWith(SpringRunner.class) //carrega o spring
@SpringBootTest //carrega tudo de ponta a ponta
@AutoConfigureMockMvc  //para poder injetar a dependencia do MockMVC por que não estamos usando o @WebMvcTest 
@ActiveProfiles("test") //força a ativação do profile test
public class AutenticacaoControllerTest {

	@Autowired
	private MockMvc mockMvc; //dublador, simula um mvc, postman
	
	@Test
	public void deveriaDevolver400CasoDadosDeAutenticacaoEstejaIncorreto() throws Exception {
		URI uri = new URI("/auth");
		// \" indica que podemos colocar aspa dupla dentro de aspa dupla(esse é o formato do json)
		String json = "{\"email\":\"invalido@email.com\", \"senha\":\"123456\"}"; //body
		
		mockMvc
		.perform(MockMvcRequestBuilders //monta um requst igual postman
				.post(uri) //verbo post e a url
				.content(json) //conteudo que é body
				.contentType(MediaType.APPLICATION_JSON)) //tipo do conteudo
		.andExpect(MockMvcResultMatchers //cria um match combinação
				.status() //chamamos status que queremos saber
				.is(400)); //verifica se é um bad request isso aqui é assert
	}

}
