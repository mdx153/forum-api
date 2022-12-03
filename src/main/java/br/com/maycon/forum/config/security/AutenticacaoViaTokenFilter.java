package br.com.maycon.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.maycon.forum.modelo.Usuario;
import br.com.maycon.forum.repository.UsuarioRepository;


//OncePerRequestFilter é um filtro que é chamado 1 vez por requisição
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter{

	private TokenService tokenService;
	private UsuarioRepository usuarioRepository; 
	
	//EM CLASSES FILTRO NÃO CONSEGUIMOS FAZER INJEÇÃO DE DEPENDENCIA POR ISSO QUANDO PRECISAMOS É NECESSARIO 
	//CRIAR UM CONSTRUTOR PADRÃO QUE RECEBA ELE DE UMA OUTRA CLASSE QUE PERMITE A INJEÇÃO
	public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = recuperarToken(request);
		boolean valido = tokenService.isTokenValid(token); //verifica se o token é valido
		
		if(valido) {
			autenticarCliente(token);
		}
		
		//doFilter diz que já fez tudo que tinha que fazer segue o fluxo
		filterChain.doFilter(request, response);
	}
	
	private void autenticarCliente(String token) {
		//lembre-se demos um subject passando id do usuario para criar token
		Long idUsuario = tokenService.getIdUsuario(token);
		Usuario usuario = usuarioRepository.findById(idUsuario).get(); //recupera os dados
		
		//aqui passamos o usuario, a senha não precisa pq se chegou aqui esta validado, e as permissoes dele
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		
		// Esse é o método para falar para o Spring considerar que está autenticado, porem precisamos passar os dados dele.
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		
		//startsWith verifica se o começa da string começa com
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		
		//corta o token pegando depois do espaço até o final
		return token.substring(7, token.length());
	}
}
