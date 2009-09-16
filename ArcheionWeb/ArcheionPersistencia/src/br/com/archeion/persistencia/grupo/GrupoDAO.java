package br.com.archeion.persistencia.grupo;

import java.util.List;

import org.acegisecurity.annotation.Secured;

import br.com.archeion.modelo.grupo.Grupo;
import br.com.archeion.persistencia.GenericDAO;

public interface GrupoDAO extends GenericDAO<Grupo, Long>
{
	@Secured ({ "ROLE_LISTAR_GRUPO" })
	List<Grupo> findById(List<Grupo> listaId);
	
	public Grupo findByName(String name);
	
}
