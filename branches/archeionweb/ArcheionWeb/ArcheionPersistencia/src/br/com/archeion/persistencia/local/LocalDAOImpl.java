package br.com.archeion.persistencia.local;

import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class LocalDAOImpl  extends JpaGenericDAO<Local, Long> implements LocalDAO {
	
	@SuppressWarnings("unchecked")
	public Local findByLocal(Local l){
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		parametros.put("empId", l.getEmpresa().getId());
		parametros.put("nome", l.getNome());

		StringBuilder sql = new StringBuilder(
			"SELECT u FROM Local u WHERE u.nome = :nome and u.empresa.id = :empId ");

		List<Local> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);

		Local local = null;
		if (list != null && list.size() > 0) {
			local = list.get(0);
		}

		return local;
	}

	@SuppressWarnings("unchecked")
	public List<Local> findByEmpresa(Empresa empresa){
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("empId", empresa.getId());
		StringBuilder sql = new StringBuilder("SELECT u FROM Local u WHERE u.empresa.id = :empId ");
		List<Local> list = getJpaTemplate().findByNamedParams(sql.toString(),parametros);
		return list;
	}
}
