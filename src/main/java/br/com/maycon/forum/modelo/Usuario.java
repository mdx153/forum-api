package br.com.maycon.forum.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Usuario implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	//chave primaria sequencial
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String email;
	private String senha;
	
	//1 usuario pode ter varios perfis e Varios perfis pode ter varios usuarios.
	@ManyToMany(fetch = FetchType.EAGER) //por padrão é lazzy no nosso caso coloca Eager para já carregar tudo 
	private List<Perfil> perfis = new ArrayList<>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override //precisa passar uma colection  que tenha o nivel de autorização
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.perfis; //pega o perfil de autorização
	}

	@Override
	public String getPassword() {
		return this.senha; //passe como retorno atributo que representa a senha
	}

	@Override
	public String getUsername() {
		return this.email; //passe como retorno atributo que representa esse login
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; //não vamos controlar
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; //não vamos controlar
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; //não vamos controlar
	}

	@Override
	public boolean isEnabled() {
		return true; //não vamos controlar
	}

}
