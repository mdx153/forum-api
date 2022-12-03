package br.com.maycon.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.maycon.forum.repository.UsuarioRepository;

@EnableWebSecurity // habilita a parte do Spring security
@Configuration //dizemos para o spring carregar e ler as configurações dessa classe
@Profile(value = {"prod","test"}) //carrega só no profile prod, test
public class SecurityConfigurations extends WebSecurityConfigurerAdapter{

	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired 
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	//Lembre-se WebSecurityConfigurerAdapter tem um metodo que implementa o AuthenticationManager
	@Override
	@Bean  //diz para o spring que o metodo devolve AuthenticationManager e com isso conseguimos injetar ele com autowired
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	//configurações de autenticaçao
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//userDetailsService diz para o spring qual service tem a logica de autentição.
		//BCryptPasswordEncoder cria um hash da senha para comparar
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	//Configurações de autorização(liberação de acesso a endpoint)
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//cria as regras
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET, "/topicos").permitAll() //permite GET em /topicos
		.antMatchers(HttpMethod.GET, "/topicos/*").permitAll() //permite GET em /topicos/QualquerCoisa
		.antMatchers(HttpMethod.POST, "/auth").permitAll()
		.antMatchers("/h2-console/**").permitAll() //não precisa de verbo por que é uma uri normal
		.antMatchers(HttpMethod.GET, "/actuator/**").permitAll() //permite GET em /actuator/todos os recursos para frente por isso **
		.antMatchers(HttpMethod.DELETE, "/topicos/*").hasRole("MODERADOR") //só podera executar o delete qm tem o perfil moderador.
		.anyRequest().authenticated() //para qualquer acesso ao endpoint não permitido ele vai dar erro 403.
		.and().csrf().disable() //desabilitamos a proteção contra csrf por que estamos usando token e estamos livre
		//aqui gerenciamos a sessão e dizemos que vamos criar como sem sessão.
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		//tem que indicar para o spring qual filtro vai vim antes ou depois.
		//addFilterBefore() diz qual filtro vai vim antes (filtro que quero adicionar, antes de quem esse filtro virá).
		.and().headers().frameOptions().sameOrigin()//diz que vai ser da mesma origin ou seja hospedagem com domínio e subdomio vai poder rodar as urls
		.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
	}
	
	//configurações de recursos estaticos(js, css, imagens, etc.)
	@Override
	public void configure(WebSecurity web) throws Exception {
		//pedimos para ignorar todas essas urls, isso pq o swagger carrega css, java entre outros
		web.ignoring()
		.antMatchers("/**.html", "/v2/api-docs", "/webjars/**","/configuration/**", "/swagger-resources/**");
	}
	
	//auxiliar para criar hashs
//	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("123456"));
//	}
}
