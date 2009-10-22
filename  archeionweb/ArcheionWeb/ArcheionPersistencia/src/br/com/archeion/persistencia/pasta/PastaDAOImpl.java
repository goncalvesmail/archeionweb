package br.com.archeion.persistencia.pasta;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.SituacaoExpurgo;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class PastaDAOImpl extends JpaGenericDAO<Pasta, Long> implements PastaDAO {
	@SuppressWarnings("unchecked")
	public List<Pasta> findByEmpresaLocal(int emp, int local) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder("SELECT u FROM Pasta u ");

		boolean where = false;
		if(emp > 0) {
			parametros.put("emp", emp);
			sql.append(" WHERE u.local.empresa.id = :emp ");
			where = true;
		}			

		if(local > 0) {
			parametros.put("local", local);
			if ( where ) {
				sql.append(" and u.local.id = :local ");
			}
			else {
				sql.append(" WHERE u.local.id = :local ");
				where = true;
			}
		}	

		List<Pasta> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Pasta> findByEmpresaLocalSituacao(int emp, int local, SituacaoExpurgo situacao) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder("SELECT u FROM Pasta u ");

		boolean where = false;
		if(emp > 0) {
			parametros.put("emp", emp);
			sql.append(" WHERE u.local.empresa.id = :emp ");
			where = true;
		}			

		if(local > 0) {
			parametros.put("local", local);
			if ( where ) {
				sql.append(" and u.local.id = :local ");
			}
			else {
				sql.append(" WHERE u.local.id = :local ");
				where = true;
			}
		}	
		
		if ( situacao!=null && situacao.getId()!=SituacaoExpurgo.TODOS.getId()) {
			parametros.put("situacao", situacao);
			if ( where ) {
				sql.append(" and u.situacao = :situacao ");
			}
			else {
				sql.append(" WHERE u.situacao = :situacao ");
				where = true;
			}
		}

		List<Pasta> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		return list;
	}	

	@SuppressWarnings("unchecked")
	public Pasta findByTitulo(String titulo){
		StringBuilder sql = new StringBuilder("SELECT u FROM Pasta u WHERE u.titulo = :titulo");
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("titulo", titulo);

		List<Pasta> pastas = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);

		Pasta p = null;
		if(pastas != null) {
			p = pastas.get(0);
		}
		return p;
	}
	
	@SuppressWarnings("unchecked")
	public Pasta findByTituloLocalEmpresa(String titulo,String nomeLocal, String nomeEmpresa){
		StringBuilder sql = new StringBuilder("SELECT u FROM Pasta u, Local l, Empresa e WHERE u.titulo = :titulo ");
		sql.append(" and u.local.id = l.id ");
		sql.append(" and l.empresa.id = e.id ");
		sql.append(" and l.nome = :nomeLoc ");
		sql.append(" and e.nome = :nomeEmp");
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("titulo", titulo);
		parametros.put("nomeEmp", nomeEmpresa);
		parametros.put("nomeLoc", nomeLocal);

		List<Pasta> pastas = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);

		Pasta p = null;
		if(pastas != null && pastas.size()>0) {
			p = pastas.get(0);
		}
		return p;
	}

	@SuppressWarnings("unchecked")
	public List<Pasta> consultaEtiquetaPasta(String where) {
		StringBuilder sql = new StringBuilder("SELECT u FROM Pasta u ");
		if(where.length() > 2){
			sql.append(" WHERE ");
			sql.append(where);
		}
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		return getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
	}

	@SuppressWarnings("unchecked")
	public List<Pasta> consultaPermanenteRecolhimento(Local local) {
		StringBuilder sql = new StringBuilder("SELECT u FROM Pasta u, TTD t ");
		sql.append(" WHERE t.local.id = u.local.id ");
		sql.append(" AND t.itemDocumental.id = u.itemDocumental.id ");
		sql.append(" AND t.arquivoPermanente=1");
		sql.append(" AND u.caixa is null");
		sql.append(" AND u.previsaoRecolhimento <= :data");
		sql.append(" AND u.local.id = :local");
		sql.append(" AND u.situacao = :situacao ");
		
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("data", new Date());
		parametros.put("local", local.getId());
		parametros.put("situacao", SituacaoExpurgo.ATIVA);
		
		return getJpaTemplate().findByNamedParams(sql.toString(),parametros);
	}
	
	@SuppressWarnings("unchecked")
	public List<Pasta> consultaTemporarioRecolhimento(Local local) {
		StringBuilder sql = new StringBuilder("SELECT u FROM Pasta u, TTD t ");
		sql.append(" WHERE t.local.id = u.local.id ");
		sql.append(" AND t.itemDocumental.id = u.itemDocumental.id ");
		sql.append(" AND t.arquivoIntermediario=1");
		sql.append(" AND u.caixa is null");
		sql.append(" AND u.previsaoRecolhimento <= :data");
		sql.append(" AND u.local.id = :local");
		sql.append(" AND u.situacao = :situacao ");
		
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("data", new Date());
		parametros.put("local", local.getId());
		parametros.put("situacao", SituacaoExpurgo.ATIVA);
		
		return getJpaTemplate().findByNamedParams(sql.toString(),parametros);
	}
	
	@SuppressWarnings("unchecked")
	public List<Pasta> findByEmpresaLocalExpurgo(int emp, int local) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder("SELECT u FROM Pasta u ");

		boolean where = false;
		if(emp > 0) {
			parametros.put("emp", emp);
			sql.append(" WHERE u.local.empresa.id = :emp ");
			where = true;
		}			

		if(local > 0) {
			parametros.put("local", local);
			if ( where ) {
				sql.append(" and u.local.id = :local ");
			}
			else {
				sql.append(" WHERE u.local.id = :local ");
				where = true;
			}
		}			
		
		if(local < 0 && emp < 0) {			
			sql.append(" WHERE u.situacao = :situacao ");
		}	
		else {
			sql.append(" AND u.situacao = :situacao ");
		}
		sql.append(" AND u.previsaoExpurgo <= :dataAtual ");

		parametros.put("dataAtual", new Date());
		parametros.put("situacao", SituacaoExpurgo.ATIVA);

		List<Pasta> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Pasta> findPastaAtivasEmprestimo() {
		StringBuilder sql = new StringBuilder("SELECT u FROM Pasta u ");
		sql.append(" WHERE u.id not in ( ");
		sql.append(" SELECT e.pasta.id from EmprestimoPasta e WHERE e.dataDevolucao is null ) ");
		
		
		List<Pasta> list = getJpaTemplate().find(sql.toString());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Pasta> findByEmpresaLocalEmprestimo(Long emp, Long local) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder("SELECT u FROM Pasta u ");

		boolean where = false;
		if(emp > 0) {
			parametros.put("emp", emp);
			sql.append(" WHERE u.local.empresa.id = :emp ");
			where = true;
		}			

		if(local > 0) {
			parametros.put("local", local);
			if ( where ) {
				sql.append(" and u.local.id = :local ");
			}
			else {
				sql.append(" WHERE u.local.id = :local ");
				where = true;
			}
		}	

		if ( !where ) {
			sql.append(" WHERE u.id not in ( ");
			sql.append(" SELECT e.pasta.id from EmprestimoPasta e WHERE e.dataDevolucao is null ) ");
			where = true;
		}
		else {
			sql.append(" and u.id not in ( ");
			sql.append(" SELECT e.pasta.id from EmprestimoPasta e WHERE e.dataDevolucao is null ) ");
		}
		
		List<Pasta> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Pasta> consultaTemporarioRecolhimentoIntervalo(Empresa empresa, Local local, 
			Date inicio, Date fim) {
		StringBuilder sql = new StringBuilder("SELECT u FROM Pasta u, TTD t ");
		sql.append(" WHERE t.local.id = u.local.id ");
		sql.append(" AND t.itemDocumental.id = u.itemDocumental.id ");
		sql.append(" AND t.arquivoIntermediario=1");
		sql.append(" AND u.previsaoRecolhimento >= :inicio");
		sql.append(" AND u.previsaoRecolhimento <= :fim");
		
		
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("inicio", inicio);
		parametros.put("fim", fim);
		
		if ( local!=null && local.getId()>0 ) {
			sql.append(" AND u.local.id = :local");
			parametros.put("local", local.getId());
		}
		if ( empresa!=null && empresa.getId()>0  ) {
			sql.append(" AND u.local.empresa.id = :empresa");
			parametros.put("empresa", empresa.getId());
		}		

		
		return getJpaTemplate().findByNamedParams(sql.toString(),parametros);
	}
	
	@SuppressWarnings("unchecked")
	public List<Pasta> consultaPermanenteRecolhimentoIntervalo(Empresa empresa, Local local, 
			Date inicio, Date fim) {
		StringBuilder sql = new StringBuilder("SELECT u FROM Pasta u, TTD t ");
		sql.append(" WHERE t.local.id = u.local.id ");
		sql.append(" AND t.itemDocumental.id = u.itemDocumental.id ");
		sql.append(" AND t.arquivoPermanente=1");
		sql.append(" AND u.previsaoRecolhimento >= :inicio");
		sql.append(" AND u.previsaoRecolhimento <= :fim");
		
		
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("inicio", inicio);
		parametros.put("fim", fim);
		
		if ( local!=null && local.getId()>0 ) {
			sql.append(" AND u.local.id = :local");
			parametros.put("local", local.getId());
		}
		if ( empresa!=null && empresa.getId()>0  ) {
			sql.append(" AND u.local.empresa.id = :empresa");
			parametros.put("empresa", empresa.getId());
		}		

		
		return getJpaTemplate().findByNamedParams(sql.toString(),parametros);
	}
	
	@SuppressWarnings("unchecked")
	public List<Pasta> findByLocalItemDocumental(long item, long local) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder("SELECT u FROM Pasta u ");

		boolean where = false;
		if(item > 0) {
			parametros.put("item", item);
			sql.append(" WHERE u.itemDocumental.id = :item ");
			where = true;
		}			

		if(local > 0) {
			parametros.put("local", local);
			if ( where ) {
				sql.append(" and u.local.id = :local ");
			}
			else {
				sql.append(" WHERE u.local.id = :local ");
				where = true;
			}
		}	

		List<Pasta> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		return list;
	}	
}
