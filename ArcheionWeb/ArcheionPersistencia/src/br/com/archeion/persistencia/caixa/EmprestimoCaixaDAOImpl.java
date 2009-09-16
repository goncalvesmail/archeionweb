package br.com.archeion.persistencia.caixa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.modelo.caixa.EmprestimoCaixa;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class EmprestimoCaixaDAOImpl extends JpaGenericDAO<EmprestimoCaixa, Long> implements
		EmprestimoCaixaDAO {
	
	@SuppressWarnings("unchecked")
	public List<EmprestimoCaixa> findEmprestadas(Caixa caixa, Usuario usuario, Date data) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("SELECT u FROM EmprestimoCaixa u ");
		sql.append(" WHERE u.dataDevolucao is null ");
		
		if ( caixa!=null && caixa.getId()>0 ) {
			sql.append(" AND u.caixa.id = :idCaixa ");
			parametros.put("idCaixa", caixa.getId());
		}
		if ( usuario!=null && usuario.getId()>0 ) {
			sql.append(" AND u.solicitante.id = :idUsuario ");
			parametros.put("idUsuario", usuario.getId());
		}
		if ( data!=null ) {
			sql.append(" AND u.dataEmprestimo = :dataEmprestimo ");
			parametros.put("dataEmprestimo", data);
		}
		
		List<EmprestimoCaixa> list = getJpaTemplate()
				.findByNamedParams(sql.toString(),parametros);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<EmprestimoCaixa> findDevolvidas(Caixa caixa, Usuario usuario, Date data) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("SELECT u FROM EmprestimoCaixa u ");
		sql.append(" WHERE u.dataDevolucao is not null ");

		if ( caixa!=null && caixa.getId()>0 ) {
			sql.append(" AND u.caixa.id = :idCaixa ");
			parametros.put("idCaixa", caixa.getId());
		}
		if ( usuario!=null && usuario.getId()>0 ) {
			sql.append(" AND u.solicitante.id = :idUsuario ");
			parametros.put("idUsuario", usuario.getId());
		}
		if ( data!=null ) {
			sql.append(" AND u.dataEmprestimo = :dataEmprestimo ");
			parametros.put("dataEmprestimo", data);
		}
		
		List<EmprestimoCaixa> list = getJpaTemplate()
				.findByNamedParams(sql.toString(),parametros);
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<EmprestimoCaixa> findByCaixa(Caixa caixa) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("SELECT u FROM EmprestimoCaixa u ");
		sql.append(" WHERE u.caixa.id = :idCaixa ");
		parametros.put("idCaixa", caixa.getId());
		
		List<EmprestimoCaixa> list = getJpaTemplate()
				.findByNamedParams(sql.toString(),parametros);
		
		return list;
	}
}
