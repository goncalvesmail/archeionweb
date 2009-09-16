package br.com.archeion.negocio.grupo;

import java.util.List;

import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.grupo.Grupo;
import br.com.archeion.persistencia.grupo.GrupoDAO;

/**
 * Implementação do B.O do Grupo
 *  
 */
public class GrupoBOImpl implements GrupoBO {

	private GrupoDAO grupoDAO;

	public List<Grupo> findAll() {
		return grupoDAO.findAll();
	}

	public void setGrupoDAO(final GrupoDAO grupoDAO) {
		this.grupoDAO = grupoDAO;
	}

	public Grupo persist(final Grupo grupo) throws CadastroDuplicadoException {
		this.valida(grupo);
		return this.grupoDAO.persist(grupo);
	}

	public Grupo findById(final Long id) {
		return this.grupoDAO.findById(id);
	}

	public List<Grupo> findById(List<Grupo> listaId) {
		return grupoDAO.findById(listaId);
	}

	public Grupo merge(Grupo grupo) throws BusinessException, CadastroDuplicadoException {
		//this.valida(grupo);
		return grupoDAO.merge(grupo);
	}

	public void remove(Grupo grupo) throws BusinessException {
		if ( grupo.getUsuarios().size()>0 ) {
			throw new BusinessException("grupo.erro.usuario");
		}
		grupoDAO.remove(grupo);		
	}

	public Grupo findByName(String name) {
		return this.grupoDAO.findByName(name);
	}
	
	private void valida(Grupo grupo) throws CadastroDuplicadoException {
		Grupo g = grupoDAO.findByName(grupo.getNome());
		if (g != null) {
			if ( grupo.getId() == g.getId()) {
				return;
			}
			throw new CadastroDuplicadoException();
		}
	}


}
