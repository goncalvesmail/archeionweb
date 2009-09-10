package br.com.archeion.persistencia.documento;

import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.documento.Documento;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class DocumentoDAOImpl extends JpaGenericDAO<Documento, Long> implements
		DocumentoDAO {

	@SuppressWarnings("unchecked")
	public Documento findByDocumento(Documento d){
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		if ( d.getId()==null ) return null;
		
		parametros.put("id", d.getId());

		StringBuilder sql = new StringBuilder(
			"SELECT u FROM Documento u WHERE u.id = :id ");

		List<Documento> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);

		Documento documento = null;
		if (list != null && list.size() > 0) {
			documento = list.get(0);
		}

		return documento;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Documento> findByLocal(Long id){
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("id", id);
		StringBuilder sql = new StringBuilder("SELECT u FROM Documento u WHERE u.local.id = :id ");
		List<Documento> list = getJpaTemplate().findByNamedParams(sql.toString(),parametros);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Documento> findByTipo(Long id){
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("id", id);
		StringBuilder sql = new StringBuilder("SELECT u FROM Documento u WHERE u.tipoDocumento.id = :id ");
		List<Documento> list = getJpaTemplate().findByNamedParams(sql.toString(),parametros);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Documento> findByPasta(Long id){
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("id", id);
		StringBuilder sql = new StringBuilder("SELECT u FROM Documento u WHERE u.pasta.id = :id ");
		List<Documento> list = getJpaTemplate().findByNamedParams(sql.toString(),parametros);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Documento> consultarDocumento(String where){
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("SELECT u FROM Documento u WHERE ");
		sql.append(where);
		List<Documento> list = getJpaTemplate().findByNamedParams(sql.toString(),parametros);
		return list;
	}
	
}
