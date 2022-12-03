package br.com.maycon.forum.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.maycon.forum.config.security.TokenService;
import br.com.maycon.forum.controller.dto.TokenDto;
import br.com.maycon.forum.form.LoginForm;

@RestController
@RequestMapping("/auth")
@Profile(value = {"prod","test"})
public class AutenticacaoController {

	//por padrão não tem um injetor para AuthenticationManager por isso criamos
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form){
		//criamos um objeto com senha e login para autenticar no token
		UsernamePasswordAuthenticationToken dadosLogin = form.converte();
		
		try {
			//gerenciador auth autentica o objeto UsernamePasswordAuthenticationToken
			Authentication authenticate = authManager.authenticate(dadosLogin);
			String token = tokenService.gerarToken(authenticate); //geramos o token com nosso serviço
			
			//quando passa o body ja faz um build automaticamente
			return ResponseEntity.ok(new TokenDto(token, "Bearer"));
		}
		catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build(); ///erro 400 se não encontrou
		}

	}
}
