package br.com.archeion.negocio.pasta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.SituacaoExpurgo;
import br.com.archeion.modelo.documento.Documento;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.EmprestimoPasta;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.modelo.ttd.TTD;
import br.com.archeion.persistencia.documento.DocumentoDAO;
import br.com.archeion.persistencia.pasta.EmprestimoPastaDAO;
import br.com.archeion.persistencia.pasta.PastaDAO;
import br.com.archeion.persistencia.ttd.TTDDAO;

public class PastaBOImpl implements PastaBO {
	
	private PastaDAO pastaDAO;
	//Estou injetando o DAO para evitar dependencia ciclica com os dois BOs
	private DocumentoDAO documentoDAO;
	private EmprestimoPastaDAO emprestimoPastaDAO;
	private TTDDAO ttdDAO;

	public List<Pasta> findAll() {
		return pastaDAO.findAll();
	}

	public Pasta findById(Long id) {
		return pastaDAO.findById(id);
	}
	
	public Pasta findByTituloLocalEmpresa(String titulo,String nomeLocal, String nomeEmpresa){
		return pastaDAO.findByTituloLocalEmpresa(titulo,nomeLocal,nomeEmpresa);
	}
	
	public Pasta findByTitulo(String titulo) {
		return pastaDAO.findByTitulo(titulo);
	}
	
	public List<Pasta> consultaEtiquetaPasta(String where){
		return pastaDAO.consultaEtiquetaPasta(where);
	}
	
	public List<Pasta> findPastaAtivasEmprestimo() {
		return pastaDAO.findPastaAtivasEmprestimo();
	}

	public Pasta merge(Pasta pasta) throws BusinessException,
			CadastroDuplicadoException {
		
		List<TTD> ttds = ttdDAO.findByEmpresaLocalItemDocumental(0, pasta.getLocal().getId().intValue(), 
				pasta.getItemDocumental().getId().intValue());
		
		if ( ttds==null || ttds.size()<=0 ) {
			throw new BusinessException("pasta.erro.alteracao.ttd");
		}
		
		return pastaDAO.merge(pasta);
	}

	public Pasta persist(Pasta pasta) throws CadastroDuplicadoException, BusinessException {
		validaPasta(pasta);
		return pastaDAO.persist(pasta);
	}
	
	public List<Pasta> findByEmpresaLocalEmprestimo(Long emp, Long local) {
		return pastaDAO.findByEmpresaLocalEmprestimo(emp,local);
	}
	
	public List<Pasta> findByEmpresaLocalSituacao(int emp, int local, SituacaoExpurgo situacao){

		return pastaDAO.findByEmpresaLocalSituacao(emp,local,situacao);
	}

	public void remove(Pasta pasta) throws BusinessException {
		
		//Verificar Documento
		List<Documento> documentos = documentoDAO.findByPasta(pasta.getId());
		if ( documentos!=null && documentos.size()>0 ) {
			throw new BusinessException("pasta.erro.documento");
		}
		
		//Verificar Caixa
		if ( pasta.getCaixa()!=null && pasta.getCaixa().getId()!=null &&
				pasta.getCaixa().getId()!=0 ) {
			throw new BusinessException("pasta.erro.caixa");
		}
		
		//Verifica Emprestimo
		List<EmprestimoPasta> emprestimos = emprestimoPastaDAO.findByPasta(pasta);
		if ( emprestimos!=null && emprestimos.size()>0 ) {
			throw new BusinessException("pasta.erro.emprestimo");
		}
		
		pastaDAO.remove(pasta);
	}
	
	public List<Pasta> findByEmpresaLocal(int emp, int local) {
		return pastaDAO.findByEmpresaLocal(emp, local);
	}
	
	public List<Pasta> findByEmpresaLocalExpurgo(int emp, int local) {
		return pastaDAO.findByEmpresaLocalExpurgo(emp, local);
	}
	
	public List<Pasta> consultaPermanenteRecolhimento(Local local) {
		return pastaDAO.consultaPermanenteRecolhimento(local);
	}
	
	public List<Pasta> consultaTemporarioRecolhimento(Local local) {
		return pastaDAO.consultaTemporarioRecolhimento(local);
	}

	public PastaDAO getPastaDAO() {
		return pastaDAO;
	}

	public void setPastaDAO(PastaDAO pastaDAO) {
		this.pastaDAO = pastaDAO;
	}
	
	public Relatorio getRelatorio(HashMap<String, Object> parameters,
			String localRelatorio) {
		Connection conn = pastaDAO.getConnection();
		try {
			Relatorio relatorio = new Relatorio(conn,parameters,localRelatorio);
			return relatorio;
		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public void setDocumentoDAO(DocumentoDAO documentoDAO) {
		this.documentoDAO = documentoDAO;
	}
	
	
	private void validaPasta(Pasta pasta) throws CadastroDuplicadoException, BusinessException{

		List<TTD> ttds = ttdDAO.findByEmpresaLocalItemDocumental(0, pasta.getLocal().getId().intValue(), 
				pasta.getItemDocumental().getId().intValue());
		
		if ( ttds==null || ttds.size()<=0 ) {
			throw new BusinessException("pasta.erro.inclusao.ttd");
		}
		
		if ( (pasta.getLimiteDataFinal()==null && pasta.getLimiteDataInicial()==null) &&
				((pasta.getLimiteNomeFinal()==null && pasta.getLimiteNomeInicial()==null) ||
					(pasta.getLimiteNomeFinal().equals("") && pasta.getLimiteNomeInicial().equals(""))) && 
				((pasta.getLimiteNumeroInicial()==null && pasta.getLimiteNumeroFinal()==null) || 
					(pasta.getLimiteNumeroInicial()==0 && pasta.getLimiteNumeroFinal()==0)) ) {
			throw new BusinessException("pasta.erro.limites");
		}
	}

	public void setEmprestimoPastaDAO(EmprestimoPastaDAO emprestimoPastaDAO) {
		this.emprestimoPastaDAO = emprestimoPastaDAO;
	}
	

	public List<Pasta> consultaTemporarioRecolhimentoIntervalo(Empresa empresa, Local local, 
			Date inicio, Date fim) {
		return pastaDAO.consultaTemporarioRecolhimentoIntervalo(empresa,local, inicio, fim);
	}

	public List<Pasta> consultaPermanenteRecolhimentoIntervalo(Empresa empresa, Local local, 
			Date inicio, Date fim) {
		return pastaDAO.consultaPermanenteRecolhimentoIntervalo(empresa, local, inicio, fim);
	}

	public TTDDAO getTtdDAO() {
		return ttdDAO;
	}

	public void setTtdDAO(TTDDAO ttdDAO) {
		this.ttdDAO = ttdDAO;
	}

	public DocumentoDAO getDocumentoDAO() {
		return documentoDAO;
	}

	public EmprestimoPastaDAO getEmprestimoPastaDAO() {
		return emprestimoPastaDAO;
	}

}
