package br.com.archeion.persistencia.eventocontagem;

import br.com.archeion.modelo.eventocontagem.EventoContagem;
import br.com.archeion.persistencia.GenericDAO;

public interface EventoContagemDAO extends GenericDAO<EventoContagem, Long>{
	public EventoContagem findByName(String name);
}
