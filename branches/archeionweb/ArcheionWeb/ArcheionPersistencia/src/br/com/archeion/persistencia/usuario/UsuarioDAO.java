package br.com.archeion.persistencia.usuario;

import java.util.List;

import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.persistencia.GenericDAO;

public interface UsuarioDAO extends GenericDAO<Usuario, Long> {

	Usuario findByLogin(String login);
	
	List<Usuario> findByLogin(Usuario usuario);

	List<Usuario> find(List<Usuario> listaId);

	List<Usuario> findAll();
	
	List<Usuario> findAllAluga();

}
