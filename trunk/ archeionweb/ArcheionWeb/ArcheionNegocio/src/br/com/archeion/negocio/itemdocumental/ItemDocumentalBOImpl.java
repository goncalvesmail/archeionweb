package br.com.archeion.negocio.itemdocumental;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.itemdocumental.ItemDocumental;
import br.com.archeion.modelo.ttd.TTD;
import br.com.archeion.persistencia.itemdocumental.ItemDocumentalDAO;
import br.com.archeion.persistencia.ttd.TTDDAO;

public class ItemDocumentalBOImpl implements ItemDocumentalBO {
	
	private ItemDocumentalDAO itemDocumentalDAO;
	private TTDDAO ttdDAO;
	
	public ItemDocumental persist(ItemDocumental item) throws CadastroDuplicadoException {
		this.valida(item);
		return itemDocumentalDAO.persist(item);
	}

	public List<ItemDocumental> findAll() {
		return itemDocumentalDAO.findAll();
	}

	public void setItemDocumentalDAO(ItemDocumentalDAO itemDocumentalDAO) {
		this.itemDocumentalDAO = itemDocumentalDAO;
	}

	public ItemDocumental findById(Long id) {
		return itemDocumentalDAO.findById(id);
	}

	public ItemDocumental merge(ItemDocumental item) throws BusinessException, CadastroDuplicadoException {		
		this.valida(item);
		return itemDocumentalDAO.merge(item);
	}

	public void remove(ItemDocumental item) throws BusinessException {
		
		//Verificar TTD
		List<TTD> ttds = ttdDAO.findByEmpresaLocalItemDocumental(0, 0, item.getId().intValue());
		if ( ttds!=null && ttds.size()>0 ) {
			throw new BusinessException("itemdocumental.erro.ttd");
		}
		
		itemDocumentalDAO.remove(item);		
	}
	
	public void valida(ItemDocumental itd) throws CadastroDuplicadoException {
		ItemDocumental i = itemDocumentalDAO.findByName(itd.getNome());
		if(i != null){
			if ( itd.equals(i)) {
				return;
			}
			throw new CadastroDuplicadoException();
		}
	}

	public Relatorio getRelatorio(HashMap<String, Object> parameters,
			String localRelatorio) {
		Connection conn = itemDocumentalDAO.getConnection();
		try {
			Relatorio relatorio = new Relatorio(conn,parameters,localRelatorio);
			return relatorio;
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setTtdDAO(TTDDAO ttdDAO) {
		this.ttdDAO = ttdDAO;
	}
	
}
