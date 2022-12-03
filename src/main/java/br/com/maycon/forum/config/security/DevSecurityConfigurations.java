package br.com.maycon.forum.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@Profile("dev")
public class DevSecurityConfigurations extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//cria as regras
		http.authorizeRequests()
		.antMatchers("/**").permitAll() //permite tudo
		.anyRequest().authenticated() //para qualquer acesso ao endpoint não permitido ele vai dar erro 403.
		.and().csrf().disable(); //desabilitamos a proteção contra csrf por que estamos usando token e estamos livre
	}
}
