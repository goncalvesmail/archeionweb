package br.com.archeion.negocio.funcionalidade;

import java.util.List;

import br.com.archeion.modelo.funcionalidade.Funcionalidade;
import br.com.archeion.persistencia.funcionalidade.FuncionalidadeDAO;

/**
 * Implementação do B.O da Funcionalidade
 * 
 */
public class FuncionalidadeBOImpl implements FuncionalidadeBO {

	/**
	 * DAO da Funcionalidade.
	 */
	private FuncionalidadeDAO funcionalidadeDAO;

	/**
	 * @see br.gov.dataprev.siprev.negocio.funcionalidade.FuncionalidadeBO#findAll()
	 * @return List<Funcionalidade>
	 */
	public List<Funcionalidade> findAll() {
		return funcionalidadeDAO.findAll();
	}
	
	public Funcionalidade findByName(String nome) {
		return funcionalidadeDAO.findByName(nome);
	}
	
	public Funcionalidade findByDescricao(String descricao) {
		return funcionalidadeDAO.findByDescricao(descricao);
	}

	/**
	 * @param funcionalidadeDAO
	 *            the funcionalidadeDAO to set
	 */
	public void setFuncionalidadeDAO(final FuncionalidadeDAO funcionalidadeDAO) {
		this.funcionalidadeDAO = funcionalidadeDAO;
	}

}
