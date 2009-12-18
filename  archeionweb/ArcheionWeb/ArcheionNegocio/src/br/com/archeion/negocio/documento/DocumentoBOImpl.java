package br.com.archeion.negocio.documento;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.exception.CompareDateException;
import br.com.archeion.modelo.documento.Documento;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.persistencia.documento.DocumentoDAO;
import br.com.archeion.persistencia.local.LocalDAO;

public class DocumentoBOImpl implements DocumentoBO {

	private DocumentoDAO documentoDAO;
	private LocalDAO localDAO;
	
	public Documento persist(Documento documento) throws BusinessException, CadastroDuplicadoException {
		this.valida(documento);
		
		Local local = documento.getLocal();
		local.setUltimoDocumento(local.getUltimoDocumento()+1);
		localDAO.merge(local);
		
		return documentoDAO.persist(documento);
	}

	public List<Documento> findAll() {
		return documentoDAO.findAll();
	}

	public void setDocumentoDAO(DocumentoDAO documentoDAO) {
		this.documentoDAO = documentoDAO;
	}

	public Documento findById(Long id) {
		return documentoDAO.findById(id);
	}
	

	public List<Documento> findByTipo(Long id) {
		return documentoDAO.findByTipo(id);
	}
	
	public List<Documento> findByPasta(Long id) {
		return documentoDAO.findByPasta(id);
	}
	
	public List<Documento> consultarDocumento(String where) {
		return documentoDAO.consultarDocumento(where);
	}
	
	public Documento merge(Documento documento) throws BusinessException, CompareDateException, CadastroDuplicadoException {	
		this.valida(documento);
		
		Local local = documento.getLocal();
		local.setUltimoDocumento(local.getUltimoDocumento()+1);
		//localBO.merge(local);
		localDAO.merge(local);
		
		return documentoDAO.merge(documento);
	}

	public void remove(Documento documento) throws BusinessException {
		documentoDAO.remove(documento);		
	}	
	
	public List<Documento> findByLocal(Long id) {
		return documentoDAO.findByLocal(id);
	}
	
	private void valida(Documento documento) throws CompareDateException {
		Date atual = new Date();
		if ( documento.getData().after(atual) ) {
			throw new CompareDateException();
		}
	}	

	public Relatorio getRelatorio(HashMap<String, Object> parameters,
			String localRelatorio) {
		Connection conn = documentoDAO.getConnection();
		try {
			Relatorio relatorio = new Relatorio(conn,parameters,localRelatorio);
			return relatorio;
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
