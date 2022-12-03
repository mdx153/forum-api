package br.com.maycon.forum.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.maycon.forum.modelo.Usuario;
import br.com.maycon.forum.repository.UsuarioRepository;

//esse service vai ser gerenciado pelo spring e ser chamado automaticamente quando abrir o formulario
@Service
public class AutenticacaoService implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//ele faz a validação pelo email e também pela senha por baixo dos panos
			Optional<Usuario> usuario = usuarioRepository.findByEmail(username);
			
			//verifica se encontrou o usuario.
			if(usuario.isPresent()) {
				return usuario.get();
			}
			
			throw new UsernameNotFoundException("Dados inválidos!");
	}

}
