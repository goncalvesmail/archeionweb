package br.com.archeion.persistencia.pasta;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.EmprestimoPasta;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class EmprestimoPastaDAOImpl extends JpaGenericDAO<EmprestimoPasta, Long> implements
		EmprestimoPastaDAO {
	
	@SuppressWarnings("unchecked")
	public List<EmprestimoPasta> findEmprestadas(Empresa empresa, Local local, Usuario usuario, Date data) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("SELECT u FROM EmprestimoPasta u ");
		sql.append(" WHERE u.dataDevolucao is null ");
		
		if ( empresa!=null && empresa.getId()!=null && empresa.getId()>0 ) {
			sql.append(" AND u.pasta.local.empresa.id = :idEmpresa ");
			parametros.put("idEmpresa", empresa.getId());
		}
		if ( local!=null && local.getId()!=null && local.getId()>0 ) {
			sql.append(" AND u.pasta.local.id = :idLocal ");
			parametros.put("idLocal", local.getId());
		}
		if ( usuario!=null && usuario.getId()!=null && usuario.getId()>0 ) {
			sql.append(" AND u.solicitante.id = :idUsuario ");
			parametros.put("idUsuario", usuario.getId());
		}
		if ( data!=null ) {
			sql.append(" AND u.dataEmprestimo = :dataEmprestimo ");
			parametros.put("dataEmprestimo", data);
		}
		
		List<EmprestimoPasta> list = getJpaTemplate()
				.findByNamedParams(sql.toString(),parametros);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<EmprestimoPasta> findDevolvidas(Empresa empresa, Local local, Usuario usuario, Date data) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("SELECT u FROM EmprestimoPasta u ");
		sql.append(" WHERE u.dataDevolucao is not null ");

		if ( empresa!=null && empresa.getId()!=null && empresa.getId()>0 ) {
			sql.append(" AND u.pasta.local.empresa.id = :idEmpresa ");
			parametros.put("idEmpresa", empresa.getId());
		}
		if ( local!=null && local.getId()!=null && local.getId()>0 ) {
			sql.append(" AND u.pasta.local.id = :idLocal ");
			parametros.put("idLocal", local.getId());
		}
		if ( usuario!=null && usuario.getId()!=null && usuario.getId()>0 ) {
			sql.append(" AND u.solicitante.id = :idUsuario ");
			parametros.put("idUsuario", usuario.getId());
		}
		if ( data!=null ) {
			sql.append(" AND u.dataEmprestimo = :dataEmprestimo ");
			parametros.put("dataEmprestimo", data);
		}
		
		List<EmprestimoPasta> list = getJpaTemplate()
				.findByNamedParams(sql.toString(),parametros);
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<EmprestimoPasta> findByPasta(Pasta pasta) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("SELECT u FROM EmprestimoPasta u ");
		sql.append(" WHERE u.pasta.id = :idPasta ");
		parametros.put("idPasta", pasta.getId());
		
		List<EmprestimoPasta> list = getJpaTemplate()
				.findByNamedParams(sql.toString(),parametros);
		
		return list;
	}
}
