package br.com.archeion.persistencia.empresa;

import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class EmpresaDAOImpl extends JpaGenericDAO<Empresa, Long> implements
		EmpresaDAO {


	@SuppressWarnings("unchecked")
	public List<Empresa> findAll() {
		final String sql = "select o from Empresa o where o.visivel = 1";
		return getJpaTemplate().find(sql);
	}
	
	@SuppressWarnings("unchecked")
	public List<Empresa> findAllInvisivel() {
		final String sql = "select o from Empresa o";
		return getJpaTemplate().find(sql);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Empresa> findRoots() {
		StringBuilder sql = new StringBuilder(
			"SELECT u FROM Empresa u WHERE u.idPai is null");

		List<Empresa> list = getJpaTemplate().find(sql.toString());
		for ( Empresa e:list ) {
			refreshRecursivo(e);
		}
		
		return list;
	}
	
	private void refreshRecursivo(Empresa empresa) {
		
		refresh(empresa);
		for ( Empresa e:empresa.getFilhos() ) {
			refreshRecursivo(e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public Empresa findByName(String name) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		parametros.put("nome", name);

		StringBuilder sql = new StringBuilder(
			"SELECT u FROM Empresa u WHERE u.nome = :nome");

		List<Empresa> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);

		Empresa emp = null;
		if (list != null && list.size() > 0) {
			emp = list.get(0);
		}

		return emp;
	}

}
