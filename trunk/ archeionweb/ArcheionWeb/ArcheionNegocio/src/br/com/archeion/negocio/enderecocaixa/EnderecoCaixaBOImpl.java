package br.com.archeion.negocio.enderecocaixa;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.modelo.enderecocaixa.EnderecoCaixa;
import br.com.archeion.persistencia.caixa.CaixaDAO;
import br.com.archeion.persistencia.enderecocaixa.EnderecoCaixaDAO;

public class EnderecoCaixaBOImpl implements EnderecoCaixaBO {

	private EnderecoCaixaDAO enderecoCaixaDAO;
	private CaixaDAO caixaDAO;
	
	public EnderecoCaixa persist(EnderecoCaixa enderecoCaixa) throws CadastroDuplicadoException, BusinessException  {
		validaEnderecoCaixa(enderecoCaixa);
		return enderecoCaixaDAO.persist(enderecoCaixa);
	}

	public List<EnderecoCaixa> findAll() {
		return enderecoCaixaDAO.findAll();
	}

	public void setEnderecoCaixaDAO(EnderecoCaixaDAO enderecoCaixaDAO) {
		this.enderecoCaixaDAO = enderecoCaixaDAO;
	}

	public EnderecoCaixa findById(Long id) {
		return enderecoCaixaDAO.findById(id);
	}

	public EnderecoCaixa merge(EnderecoCaixa enderecoCaixa) throws BusinessException, CadastroDuplicadoException {
		validaEnderecoCaixa(enderecoCaixa);
		return enderecoCaixaDAO.merge(enderecoCaixa);
	}

	public void remove(EnderecoCaixa enderecoCaixa) throws BusinessException {
		
		//Verificar Caixa
		List<Caixa> caixas = caixaDAO.findByEndereco(enderecoCaixa.getId());
		if ( caixas!=null && caixas.size()>0 ) {
			throw new BusinessException("enderecoCaixa.erro.caixa");
		}
		
		enderecoCaixaDAO.remove(enderecoCaixa);		
	}

	public Relatorio getRelatorio(HashMap<String, Object> parameters,
			String localRelatorio) {
		Connection conn = enderecoCaixaDAO.getConnection();
		try {
			Relatorio relatorio = new Relatorio(conn,parameters,localRelatorio);
			return relatorio;
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	private void validaEnderecoCaixa(EnderecoCaixa enderecoCaixa) throws CadastroDuplicadoException, BusinessException {
		EnderecoCaixa e = enderecoCaixaDAO.findByName(enderecoCaixa.getVao());
		
		if ( enderecoCaixa.getVaoInicial()==0 ) {
			throw new BusinessException("enderecoCaixa.err.inicial.zero");
		}
		else if( enderecoCaixa.getVaoInicial()>enderecoCaixa.getVaoFinal() ) {
			throw new BusinessException("enderecoCaixa.err.inicial.menor");			
		}
		else if ( enderecoCaixaDAO.findIntervalo(enderecoCaixa) !=null ) {
			
			if ( enderecoCaixa.getId()==null || enderecoCaixa.getId()==0 ) { 
				throw new BusinessException("enderecoCaixa.err.intervalo");	
			}
			else if ( enderecoCaixaDAO.findIntervalo(enderecoCaixa).getId().longValue() != 
						enderecoCaixa.getId().longValue() ) {
				throw new BusinessException("enderecoCaixa.err.intervalo");		
			}
		}
		else if(e != null) {
			if ( enderecoCaixa.equals(e)) {
				return;
			}
			
			throw new CadastroDuplicadoException();
		}
	}

	public CaixaDAO getCaixaDAO() {
		return caixaDAO;
	}

	public void setCaixaDAO(CaixaDAO caixaDAO) {
		this.caixaDAO = caixaDAO;
	}

	public EnderecoCaixaDAO getEnderecoCaixaDAO() {
		return enderecoCaixaDAO;
	}
	
}
