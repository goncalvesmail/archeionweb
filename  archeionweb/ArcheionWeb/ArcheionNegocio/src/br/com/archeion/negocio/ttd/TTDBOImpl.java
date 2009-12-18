package br.com.archeion.negocio.ttd;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.modelo.ttd.TTD;
import br.com.archeion.persistencia.pasta.PastaDAO;
import br.com.archeion.persistencia.ttd.TTDDAO;

public class TTDBOImpl implements TTDBO {
	
	private TTDDAO ttdDAO;
	private PastaDAO pastaDAO;
	
	public List<TTD> findAll() {
		return ttdDAO.findAll();
	}
	public List<TTD> findByEmpresaLocalItemDocumental(int emp, int local,
			int item) {
		return ttdDAO.findByEmpresaLocalItemDocumental(emp, local, item);
	}
	public TTD findById(Long id) {
		return ttdDAO.findById(id);
	}
	public List<TTD> findByEvento(Long idEvento) {
		return ttdDAO.findByEvento(idEvento);
	}
	
	public Relatorio getRelatorio(HashMap<String, Object> parameters,
			String localRelatorio) {
		Connection conn = ttdDAO.getConnection();
		try {
			Relatorio relatorio = new Relatorio(conn,parameters,localRelatorio);
			return relatorio;
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}
	public TTD merge(TTD ttd) throws BusinessException,
			CadastroDuplicadoException {
		this.validaTTD(ttd);

		/*
		 * Retirada a validação para permitir a alteração
		 * Nova regra:
		 * Não é possível alterar o Item Documental, Empresa e o Local a qual a TTD esta associada.
		 * Quando for alterado o tempo de permanencia no arquivo corrente todas as pastas que possuem aquela TTD serão atualizadas, 
		 * a não ser as pastas já EXPURGADAS.
		 * Quando for alterado o tempo de permanencia no arquivo temporário todas as pastas que possuem aquela TTD serão atualizadas, 
		 * a não ser as pastas já EXPURGADAS.
         * Poderá ser alterada a Temporalidade (Temporaria para Permanente e vice-versa).
         * Em nenhum caso o sistema irá retirar Pastas de dentro de uma Caixa.
		 */
		//Verificar se existe pasta		
		/*List<Pasta> pastas = pastaDAO.findByLocalItemDocumental(ttd.getItemDocumental().getId(), 
				ttd.getLocal().getId());
		
		if ( pastas!=null && pastas.size()>0 ) {
			throw new BusinessException("ttd.erro.alterar.pasta");
		}*/
		
		ttd = ttdDAO.merge(ttd);
		
		//Atualiza as pastas de acordo com a nova regra
		/*
		 * Nova regra:
		 * Não é possível alterar o Item Documental, Empresa e o Local a qual a TTD esta associada.
		 * Quando for alterado o tempo de permanencia no arquivo corrente todas as pastas que possuem aquela TTD serão atualizadas, 
		 * a não ser as pastas já EXPURGADAS.
		 * Quando for alterado o tempo de permanencia no arquivo temporário todas as pastas que possuem aquela TTD serão atualizadas, 
		 * a não ser as pastas já EXPURGADAS.
         * Poderá ser alterada a Temporalidade (Temporaria para Permanente e vice-versa).
         * Em nenhum caso o sistema irá retirar Pastas de dentro de uma Caixa.
		 */
		ttdDAO.atualizarPastasTTD(ttd);
		
		return ttd;
	}
	public TTD persist(TTD ttd) throws CadastroDuplicadoException, BusinessException {
		this.validaTTD(ttd);
		return ttdDAO.persist(ttd);
	}
	public void remove(TTD ttd) throws BusinessException {
		//Verificar se existe pasta
		List<Pasta> pastas = pastaDAO.findByLocalItemDocumental(ttd.getItemDocumental().getId(), 
				ttd.getLocal().getId());
		
		if ( pastas!=null && pastas.size()>0 ) {
			throw new BusinessException("ttd.erro.removerr.pasta");
		}
		ttdDAO.remove(ttd);
	}
	public TTDDAO getTtdDAO() {
		return ttdDAO;
	}
	public void setTtdDAO(TTDDAO ttdDAO) {
		this.ttdDAO = ttdDAO;
	}	

	public PastaDAO getPastaDAO() {
		return pastaDAO;
	}
	public void setPastaDAO(PastaDAO pastaDAO) {
		this.pastaDAO = pastaDAO;
	}
	
	private void validaTTD(TTD ttd) throws CadastroDuplicadoException, BusinessException{
		TTD t = ttdDAO.findByTTD(ttd);
		
		if(t != null) {
			if ( ttd.equals(t)) {
				return;
			}
			
			throw new CadastroDuplicadoException();
		}
		
		if ( ttd.isArquivoIntermediario() && (
				ttd.getTempoArquivoIntermediario()==null ||
				ttd.getTempoArquivoIntermediario()<=0 ) ) {
			throw new BusinessException("ttd.erro.intermediario.zero");
		}
	}
}
