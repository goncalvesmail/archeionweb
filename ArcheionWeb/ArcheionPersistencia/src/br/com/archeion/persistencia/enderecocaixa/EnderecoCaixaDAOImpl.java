package br.com.archeion.persistencia.enderecocaixa;

import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.enderecocaixa.EnderecoCaixa;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class EnderecoCaixaDAOImpl extends JpaGenericDAO<EnderecoCaixa, Long> implements
		EnderecoCaixaDAO {
	
	@SuppressWarnings("unchecked")
	public EnderecoCaixa findByName(String name) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		parametros.put("nome", name);

		StringBuilder sql = new StringBuilder(
			"SELECT u FROM EnderecoCaixa u WHERE u.vao = :nome");

		List<EnderecoCaixa> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);

		EnderecoCaixa emp = null;
		if (list != null && list.size() > 0) {
			emp = list.get(0);
		}

		return emp;
	}
	
	@SuppressWarnings("unchecked")
	public EnderecoCaixa findIntervalo(EnderecoCaixa endereco) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("nome", endereco.getVao());
		parametros.put("inicial", endereco.getVaoInicial());
		parametros.put("final", endereco.getVaoFinal());

		StringBuilder sql = new StringBuilder(
			"SELECT u FROM EnderecoCaixa u WHERE u.vao <> :nome and ( ");
		sql.append(" (u.vaoInicial <= :inicial and u.vaoFinal >= :inicial) ");
		sql.append(" or (u.vaoInicial <= :final and u.vaoFinal >= :final) ");
		sql.append(" or (u.vaoInicial >= :inicial and u.vaoFinal <= :final) ");
		sql.append(" )");
		
		List<EnderecoCaixa> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);

		EnderecoCaixa emp = null;
		if (list != null && list.size() > 0) {
			emp = list.get(0);
		}

		return emp;
	}
	
	
	

}
