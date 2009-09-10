package br.com.archeion.persistencia.eventocontagem;

import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.eventocontagem.EventoContagem;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class EventoContagemDAOImpl extends JpaGenericDAO<EventoContagem, Long> implements EventoContagemDAO {
	
	@SuppressWarnings("unchecked")
	public EventoContagem findByName(String name) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		parametros.put("nome", name);

		StringBuilder sql = new StringBuilder(
			"SELECT u FROM EventoContagem u WHERE u.nome = :nome");

		List<EventoContagem> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);

		EventoContagem even = null;
		if (list != null && list.size() > 0) {
			even = list.get(0);
		}

		return even;
	}

}
