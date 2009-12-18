package br.com.archeion.negocio.eventocontagem;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.eventocontagem.EventoContagem;
import br.com.archeion.modelo.ttd.TTD;
import br.com.archeion.persistencia.eventocontagem.EventoContagemDAO;
import br.com.archeion.persistencia.ttd.TTDDAO;

public class EventoContagemBOImpl implements EventoContagemBO {
	
	private EventoContagemDAO eventoContagemDAO;
	private TTDDAO ttdDAO;

	public List<EventoContagem> findAll() {
		return eventoContagemDAO.findAll();
	}

	public EventoContagem findById(Long id) {
		return eventoContagemDAO.findById(id);
	}
	
	private void valida(EventoContagem even) throws CadastroDuplicadoException {
		EventoContagem e = eventoContagemDAO.findByName(even.getNome());
		if(e != null) {
			if ( even.equals(e)) {
				return;
			}
			throw new CadastroDuplicadoException();
		}
	}

	public Relatorio getRelatorio(HashMap<String, Object> parameters,
			String localRelatorio) {
		Connection conn = eventoContagemDAO.getConnection();
		try {
			Relatorio relatorio = new Relatorio(conn,parameters,localRelatorio);
			return relatorio;
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}

	public EventoContagem merge(EventoContagem item) throws BusinessException, CadastroDuplicadoException {
		this.valida(item);
		return eventoContagemDAO.merge(item);
	}

	public EventoContagem persist(EventoContagem item) throws CadastroDuplicadoException {
		this.valida(item);
		return eventoContagemDAO.persist(item);
	}

	public void remove(EventoContagem item) throws BusinessException {

		//Verifcar TTD
		List<TTD> ttds = ttdDAO.findByEvento(item.getId());
		if ( ttds!=null && ttds.size()>0 ) {
			throw new BusinessException("eventocontagem.erro.ttd");
		}
		
		eventoContagemDAO.remove(item);
	}

	public EventoContagemDAO getEventoContagemDAO() {
		return eventoContagemDAO;
	}

	public void setEventoContagemDAO(EventoContagemDAO eventoContagemDAO) {
		this.eventoContagemDAO = eventoContagemDAO;
	}

	public TTDDAO getTtdDAO() {
		return ttdDAO;
	}

	public void setTtdDAO(TTDDAO ttdDAO) {
		this.ttdDAO = ttdDAO;
	}

}
