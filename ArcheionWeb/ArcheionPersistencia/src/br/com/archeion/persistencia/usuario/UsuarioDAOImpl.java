package br.com.archeion.persistencia.usuario;

import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class UsuarioDAOImpl extends JpaGenericDAO<Usuario, Long> implements	UsuarioDAO {

	@SuppressWarnings("unchecked")
	public Usuario findByLogin(String login) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		parametros.put("login", login);
		
		StringBuilder sql = new StringBuilder(
				//Daniel - descobrir pq esse upper ta virando ucase na consulta
				//"SELECT u FROM Usuario u WHERE UPPER(u.login) = :login");
		     "SELECT u FROM Usuario u WHERE u.login = :login");

		
		List<Usuario> list = getJpaTemplate().findByNamedParams(sql.toString(), parametros);
		
		Usuario user = null;
		if ( list != null && list.size()>0 ) {
			user = list.get(0);
		}
		
		return user;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> findByLogin(Usuario usuario) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		parametros.put("login", usuario.getLogin().toUpperCase());
		
		StringBuilder sql = new StringBuilder(
				//Daniel - descobrir pq esse upper ta virando ucase na consulta
				//"SELECT u FROM Usuario u WHERE UPPER(u.login) = :login");
		     "SELECT u FROM Usuario u WHERE u.login = :login");

		if (usuario.getId() != null && usuario.getId() != 0){
			parametros.put("id", usuario.getId());
			sql.append(" and u.id <> :id");
		}

		return getJpaTemplate().findByNamedParams(sql.toString(), parametros);
		
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> findAll() {
		List<Usuario> lista = getJpaTemplate().find("SELECT u FROM Usuario u");
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> find(List<Usuario> listaId) {

		StringBuffer consulta = new StringBuffer(
				"select OBJECT(u) from Usuario u where u.id in (");

		for (Usuario usuarios : listaId) {
			consulta.append(usuarios.getId().toString());
			consulta.append(",");
		}

		consulta.replace(consulta.length() - 1, consulta.length(), ")");

		List<Usuario> l = getJpaTemplate().find(consulta.toString());

		return l;

	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> findAllAluga() {
		StringBuffer consulta = new StringBuffer("select OBJECT(u) from Usuario u where u.podeAlugar=1");		
		List<Usuario> l = getJpaTemplate().find(consulta.toString());
		return l;

	}

}
