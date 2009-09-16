package br.com.archeion.negocio.funcionalidade;

import java.util.List;

import br.com.archeion.modelo.funcionalidade.Funcionalidade;

/**
 * 
 * Interface que contém todos os métodos disponíveis para a entidade
 * Funcionalidade.
 * 
 */
public interface FuncionalidadeBO {

	/**
	 * Retorna a lista com todas as funcionalidades.
	 * 
	 * @return List<Funcionalidade>
	 */
	List<Funcionalidade> findAll();
	
	Funcionalidade findByName(String nome);
	
	Funcionalidade findByDescricao(String descricao);

}
