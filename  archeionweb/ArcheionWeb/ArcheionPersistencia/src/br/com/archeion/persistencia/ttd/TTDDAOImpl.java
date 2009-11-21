package br.com.archeion.persistencia.ttd;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.TipoArquivo;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.modelo.ttd.TTD;
import br.com.archeion.persistencia.impl.JpaGenericDAO;
import br.com.archeion.persistencia.pasta.PastaDAO;

public class TTDDAOImpl extends JpaGenericDAO<TTD,Long> implements TTDDAO {
	
	public PastaDAO pastaDAO;
	
	@SuppressWarnings("unchecked")
	public List<TTD> findByEmpresaLocalItemDocumental(int emp, int local, int item) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder("SELECT u FROM TTD u ");
		
		boolean where = false;
		if(emp > 0) {
			parametros.put("emp", emp);
			sql.append(" WHERE u.local.empresa.id = :emp ");
			where = true;
		}
		
		if(local > 0) {
			parametros.put("local", local);
			if ( where ) {
				sql.append(" AND u.local.id = :local ");
			}
			else {
				sql.append(" WHERE u.local.id = :local ");
				where = true;
			}
		}
		
		if(item > 0) {
			parametros.put("item", item);
			if ( where ) {
				sql.append(" AND u.itemDocumental.id = :item ");
			}
			else {
				sql.append(" WHERE u.itemDocumental.id = :item ");
				where = true;
			}
		}		
		List<TTD> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public TTD findByTTD(TTD ttd) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		parametros.put("local", ttd.getLocal().getId());
		parametros.put("item", ttd.getItemDocumental().getId());
		parametros.put("evento", ttd.getEventoContagem().getId());
		
		StringBuilder sql = new StringBuilder("SELECT u FROM TTD u WHERE u.local.id = :local ");
		sql.append("AND u.itemDocumental.id = :item AND u.eventoContagem.id = :evento ");

		List<TTD> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);
		return ((list != null) && (list.size()>0))?list.get(0):null;
	}
	
	@SuppressWarnings("unchecked")
	public List<TTD> findByEvento(Long idEvento) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("evento", idEvento);		
		StringBuilder sql = new StringBuilder("SELECT u FROM TTD u WHERE u.eventoContagem.id = :evento ");
		List<TTD> list = getJpaTemplate().findByNamedParams(sql.toString(),parametros);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<TTD> setPastaPermanente(TTD ttd) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("local", ttd.getLocal().getId());
		parametros.put("itemDoc", ttd.getItemDocumental().getId());
		StringBuilder sql = new StringBuilder("update PASTA set previsaoExpurgo= u WHERE u.eventoContagem.id = :evento ");
		List<TTD> list = getJpaTemplate().findByNamedParams(sql.toString(),parametros);
		return list;
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void atualizarPastasTTD(TTD ttd) {
		
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("local", ttd.getLocal().getId());
		parametros.put("itemDoc", ttd.getItemDocumental().getId());
		
		//Selecionar as pastas daquela TTD
		StringBuilder sql = new StringBuilder("SELECT p from Pasta p " +
				"WHERE p.local.id = :local " +
				"AND p.itemDocumental.id = :itemDoc");
		List<Pasta> pastas = getJpaTemplate().findByNamedParams(sql.toString(),parametros);
		Integer corrente = ttd.getTempoArquivoCorrente();
		
		//PARA TODOS: Setar previsão de recolhimento (referência+tempo corrente)
		for( Pasta p:pastas ) {
			Date ref = (Date)p.getDataReferencia().clone();
			ref.setYear( ref.getYear()+corrente.intValue() );
			
			p.setPrevisaoRecolhimento(ref);
			
			//Se for intermediário, setar a previsão de expurgo
			if ( ttd.isArquivoIntermediario() ) {
				Integer intermediario = ttd.getTempoArquivoIntermediario();
				
				Date rec = (Date)p.getPrevisaoRecolhimento().clone();
				rec.setYear( rec.getYear()+intermediario.intValue() );
				
				p.setPrevisaoExpurgo(rec);
				
				//Retirar da caixa caso tenha mudado de tipo
				if ( p.getCaixa()!=null && !p.getCaixa().getTipo().equals(TipoArquivo.INTERMEDIARIO) ) {
					p.setCaixa(null);
				}
			}
			else { //Se não, limpar a previsão expurgo
				p.setPrevisaoExpurgo(null);
				
				//Retirar da caixa caso tenha mudado de tipo
				if ( p.getCaixa()!=null && p.getCaixa().getTipo().equals(TipoArquivo.INTERMEDIARIO) ) {
					p.setCaixa(null);
				}
			}
			
			pastaDAO.merge(p);
		}
	}
	

	public PastaDAO getPastaDAO() {
		return pastaDAO;
	}

	public void setPastaDAO(PastaDAO pastaDAO) {
		this.pastaDAO = pastaDAO;
	}

}
