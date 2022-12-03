package br.com.maycon.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.maycon.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	//86400000 = 1 dia em milesegundos que é o que o expiration aceita, ideial é ser 30min
	//@Value diz que vamos injetar da properties e ${} é onde passa o nome da variavel.
	@Value("${forum.jwt.expiration}")
	private String expiration;
	
	//Essa senha pode pegar de um gerador de senhas longas
	@Value("${forum.jwt.secret}")
	private String secret;
	
	public String gerarToken(Authentication authenticate) {
		//getPrincipal() devolve um usuario logado.
		//getPrincipal() devolve um object e fazemos um cast
		Usuario logado = (Usuario) authenticate.getPrincipal();
		
		Date hoje = new Date();
		//pega o tempo de agora + 1 dia
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		
		return Jwts.builder() //construtor que permite você criar diferentes representações.
				.setIssuer("API Forum") //quem é(aplicação) que está gerando esse token
				.setSubject(logado.getId().toString()) //quem é o dono desse token, usuario autenticado;
				.setIssuedAt(hoje) //data de geração desse token, quando foi concedido
				.setExpiration(dataExpiracao) //data de expiração.
				.signWith(SignatureAlgorithm.HS256, secret) //passa o algoritmo e senha de criptografia
				.compact(); //compacta e transforma em uma string
	}

	public boolean isTokenValid(String token) {
		try 
		{
			//caso o parse der errado da uma exception que recuperamos no catch.
			
			//vamos decodificar com parse()
			Jwts.parser()
			.setSigningKey(this.secret) //passa a chave para descriptografar
			.parseClaimsJws(token); //claims é um objeto onde consigo recuperar o token e as informações que setei dentro do token.
			
			return true;
		} 
		catch (Exception e) 
		{
			return false;
		}
		
	}

	public Long getIdUsuario(String token) {
		//getbody pega o todas informações que foi resultado dele, o principal o subject
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject()); //subject possui nosso id de usuario
	}
}
