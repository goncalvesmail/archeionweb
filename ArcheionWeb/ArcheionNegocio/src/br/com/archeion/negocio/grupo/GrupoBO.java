package br.com.archeion.negocio.grupo;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.grupo.Grupo;
import br.com.archeion.util.Log;

/**
 * 
 * Interface que contém todos os métodos disponíveis para a entidade Grupo.
 * 
 */
public interface GrupoBO {

	/**
	 * Retorna a lista com todos os grupos.
	 * 
	 * @return List<Grupo>
	 */
	List<Grupo> findAll();

	/**
	 * Retorna o Grupo a partir do Id informado.
	 * 
	 * @param id
	 *            Id do grupo
	 * @return Grupo
	 */
	Grupo findById(Long id);
	
	/**
	 * Retorna o Grupo a partir do nome informado.
	 * 
	 * @param name
	 *            nome do grupo
	 * @return Grupo
	 */
	Grupo findByName(String name);

	/**
	 * Retorna os Grupos a partir de uma lista de grupos com id's preenchidos.
	 * 
	 * @param Lista de Grupos com id's preenchidos
	 *            
	 * @return Lista de Grupos completos
	 */
	List<Grupo> findById(List<Grupo> listaId);
	
	/**
	 * Salva um grupo no Banco de Dados.
	 * 
	 * @param grupo
	 *            Grupo
	 * @return Grupo
	 * @throws CadastroDuplicadoException 
	 */
	@Transactional
	@Log(descricao="Inclusão")
	Grupo persist(Grupo grupo) throws CadastroDuplicadoException;
	
	@Transactional
	@Log(descricao="Alteração")
	Grupo merge(Grupo grupo) throws BusinessException, CadastroDuplicadoException;
	@Transactional
	@Log(descricao="Exclusão")
	void remove(Grupo grupo) throws BusinessException;

}
