package br.com.archeion.negocio.local;

import java.util.List;

import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.modelo.documento.Documento;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.modelo.ttd.TTD;
import br.com.archeion.persistencia.caixa.CaixaDAO;
import br.com.archeion.persistencia.documento.DocumentoDAO;
import br.com.archeion.persistencia.local.LocalDAO;
import br.com.archeion.persistencia.pasta.PastaDAO;
import br.com.archeion.persistencia.ttd.TTDDAO;

public class LocalBOImpl implements LocalBO {

	private LocalDAO localDAO;
	
	//Estou injetando o DAO para evitar dependencia ciclica com os dois BOs
	private DocumentoDAO documentoDAO;
	private PastaDAO pastaDAO;
	private CaixaDAO caixaDAO;
	private TTDDAO ttdDAO;
	
	public Local persist(Local local) throws CadastroDuplicadoException {
		this.valida(local);
		return localDAO.persist(local);
	}

	public List<Local> findAll() {
		return localDAO.findAll();
	}

	public void setLocalDAO(LocalDAO localDAO) {
		this.localDAO = localDAO;
	}

	public Local findById(Long id) {
		return localDAO.findById(id);
	}
	
	public List<Local> findByEmpresa(Empresa empresa) {
		return localDAO.findByEmpresa(empresa);
	}

	public Local merge(Local local) throws BusinessException, CadastroDuplicadoException {	
		this.valida(local);
		return localDAO.merge(local);
	}

	public void remove(Local local) throws BusinessException {
		
		//Verificar TTD
		List<TTD> ttds = ttdDAO.findByEmpresaLocalItemDocumental(0, local.getId().intValue(), 0);
		if ( ttds!=null && ttds.size()>0 ) {
			throw new BusinessException("local.erro.ttd");
		}
		
		//Verificar Documento
		List<Documento> documentos = documentoDAO.findByLocal(local.getId());
		if ( documentos!=null && documentos.size()>0 ) {
			throw new BusinessException("local.erro.documento");
		}
		
		//Verificar Pastas
		List<Pasta> pastas = pastaDAO.findByEmpresaLocal(0, local.getId().intValue());
		if ( pastas!=null && pastas.size()>0 ) {
			throw new BusinessException("local.erro.pasta");
		}
		
		//Verificar Caixas
		List<Caixa> caixas = caixaDAO.findByEmpresaLocal(0, local.getId().intValue());
		if ( caixas!=null && caixas.size()>0 ) {
			throw new BusinessException("local.erro.caixa");
		}
		
		localDAO.remove(local);		
	}	
	
	private void valida(Local local) throws CadastroDuplicadoException {
		Local l = localDAO.findByLocal(local);
		if(l != null){
			if ( local.equals(l)) {
				return;
			}
			throw new CadastroDuplicadoException();
		}
	}

	public void setDocumentoDAO(DocumentoDAO documentoDAO) {
		this.documentoDAO = documentoDAO;
	}

	public void setPastaDAO(PastaDAO pastaDAO) {
		this.pastaDAO = pastaDAO;
	}

	public void setCaixaDAO(CaixaDAO caixaDAO) {
		this.caixaDAO = caixaDAO;
	}

	public TTDDAO getTtdDAO() {
		return ttdDAO;
	}

	public void setTtdDAO(TTDDAO ttdDAO) {
		this.ttdDAO = ttdDAO;
	}

	public LocalDAO getLocalDAO() {
		return localDAO;
	}

	public DocumentoDAO getDocumentoDAO() {
		return documentoDAO;
	}

	public PastaDAO getPastaDAO() {
		return pastaDAO;
	}

	public CaixaDAO getCaixaDAO() {
		return caixaDAO;
	}
	
}
