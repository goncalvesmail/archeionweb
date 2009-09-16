package br.com.archeion.persistencia.caixa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.SituacaoExpurgo;
import br.com.archeion.modelo.TipoArquivo;
import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class CaixaDAOImpl extends JpaGenericDAO<Caixa, Long> implements
		CaixaDAO {
	
	@SuppressWarnings("unchecked")
	public List<Caixa> findVaoOcupados(String nomeVao) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("SELECT u FROM Caixa u ");
		sql.append(" WHERE u.vao.vao = :nomeVao ");
		sql.append(" AND u.situacao <> :situacao ");
		
		parametros.put("nomeVao", nomeVao);
		parametros.put("situacao", SituacaoExpurgo.EXPURGADA);
		
		
		List<Caixa> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		return list;
	}	
	
	@SuppressWarnings("unchecked")
	public List<Caixa> findByEndereco(Long id) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("SELECT u FROM Caixa u ");
		sql.append(" WHERE u.vao.id = :id ");		
		parametros.put("id", id);				
		List<Caixa> list = getJpaTemplate().findByNamedParams(sql.toString(),parametros);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Caixa> findByEmpresaLocal(int emp, int local) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder("SELECT u FROM Caixa u ");
		
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
		
		List<Caixa> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Caixa> findByEmpresaLocalSituacao(int emp, int local, SituacaoExpurgo situacao) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder("SELECT u FROM Caixa u ");
		
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
		
		List<Caixa> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Caixa> consultaEtiquetaCaixa(int caixaId, Date date, TipoArquivo tipo) {
		
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("SELECT u FROM Caixa u ");
		

		StringBuilder where = new StringBuilder("where");

		where.append(" u.situacao <> :situacao ");
		parametros.put("situacao", SituacaoExpurgo.EXPURGADA);	
	
		if ( date!=null ) {
			where.append(" and u.dataCriacao = :date ");
			parametros.put("date", date);
		}
		
		if(caixaId != -1){
			where.append(" and u.id = :id ");
			parametros.put("id", caixaId);
		}

		if(tipo!=null && tipo != TipoArquivo.TODOS){
			where.append(" and u.tipo = :tipo ");
			parametros.put("tipo", tipo);
		} 
		
		sql.append(where.toString());
		
		
		List<Caixa> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public Caixa findByVaoNumero(String vao, int numero) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("vao", vao);
		parametros.put("n", numero);
		
		StringBuilder sql = new StringBuilder("SELECT u FROM Caixa u ");
		sql.append(" where u.numeroVao = :n ");
		sql.append(" and u.vao.vao = :vao ");
		
		List<Caixa> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		Caixa result = null;
		if((list != null) && (list.size() > 0)){
			result = list.get(0);
		}
			
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Caixa findByVaoNumeroAtiva(String vao, int numero) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("vao", vao);
		parametros.put("n", numero);
		parametros.put("situacao", SituacaoExpurgo.EXPURGADA);
		
		StringBuilder sql = new StringBuilder("SELECT u FROM Caixa u ");
		sql.append(" where u.numeroVao = :n ");
		sql.append(" and u.vao.vao = :vao ");
		sql.append(" and u.situacao <> :situacao ");	
		
		List<Caixa> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		Caixa result = null;
		if((list != null) && (list.size() > 0)){
			result = list.get(0);
		}
			
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Caixa> findCaixaAtivasExpurgo() {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("SELECT u FROM Caixa u ");
		sql.append(" WHERE u.situacao = :situacao ");
		sql.append(" AND u.dataPrevisaoExpurgo <= :dataAtual ");
		
		parametros.put("dataAtual", new Date());
		parametros.put("situacao", SituacaoExpurgo.ATIVA);
		
		
		List<Caixa> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Caixa> findCaixaAtivasEmprestimo() {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("SELECT u FROM Caixa u ");
		sql.append(" WHERE u.id not in ( ");
		sql.append(" SELECT e.caixa.id from EmprestimoCaixa e WHERE e.dataDevolucao is null ) ");
		
		sql.append(" and u.situacao <> :situacao ");
		parametros.put("situacao", SituacaoExpurgo.EXPURGADA);	
		
		List<Caixa> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		return list;
	}
}
