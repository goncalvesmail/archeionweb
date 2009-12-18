package br.com.archeion.negocio.tipodocumento;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.documento.Documento;
import br.com.archeion.modelo.tipodocumento.TipoDocumento;
import br.com.archeion.persistencia.documento.DocumentoDAO;
import br.com.archeion.persistencia.tipodocumento.TipoDocumentoDAO;

public class TipoDocumentoBOImpl implements TipoDocumentoBO {
	
	private TipoDocumentoDAO tipoDocumentoDAO;
	private DocumentoDAO documentoDAO;
	
	public TipoDocumento persist(TipoDocumento item) throws CadastroDuplicadoException {
		this.valida(item);
		return tipoDocumentoDAO.persist(item);
	}

	public List<TipoDocumento> findAll() {
		return tipoDocumentoDAO.findAll();
	}

	public TipoDocumento findById(Long id) {
		return tipoDocumentoDAO.findById(id);
	}

	public TipoDocumento merge(TipoDocumento item) throws BusinessException, CadastroDuplicadoException {	
		this.valida(item);
		return tipoDocumentoDAO.merge(item);
	}

	public void remove(TipoDocumento item) throws BusinessException {
		
		//Verificar Documento
		List<Documento> documentos = documentoDAO.findByTipo(item.getId());
		if ( documentos!=null && documentos.size()>0 ) {
			throw new BusinessException("tipodocumento.erro.documento");
		}
		
		tipoDocumentoDAO.remove(item);		
	}

	public void setTipoDocumentoDAO(TipoDocumentoDAO tipoDocumentoDAO) {
		this.tipoDocumentoDAO = tipoDocumentoDAO;
	}	
	
	private void valida(TipoDocumento tipoDocumento) throws CadastroDuplicadoException {
		TipoDocumento i = tipoDocumentoDAO.findByName(tipoDocumento.getNome());
		if (i != null){
			if ( tipoDocumento.equals(i)) {
				return;
			}
			throw new CadastroDuplicadoException();
		}
	}
	
	public Relatorio getRelatorio(HashMap<String, Object> parameters,
			String localRelatorio) {
		Connection conn = tipoDocumentoDAO.getConnection();
		try {
			Relatorio relatorio = new Relatorio(conn,parameters,localRelatorio);
			return relatorio;
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DocumentoDAO getDocumentoDAO() {
		return documentoDAO;
	}

	public void setDocumentoDAO(DocumentoDAO documentoDAO) {
		this.documentoDAO = documentoDAO;
	}

	public TipoDocumentoDAO getTipoDocumentoDAO() {
		return tipoDocumentoDAO;
	}
	
}
