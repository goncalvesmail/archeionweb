package br.com.archeion.negocio.usuario;

import java.util.HashMap;
import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.grupo.Grupo;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.util.Log;

/**
 * Interface que irá representar todas as funcionalidades da entidade Usuário
 * 
 */

public interface UsuarioBO {

	@Transactional
	@Log(descricao="Inclusão")
	public Usuario salvar(Usuario usuario);
	

	/**
	 * retorna uma lista de grupos usuários
	 * @return Lista de Grupos
	 */	
	public List<Grupo> findAllGrupo();
	
	/**
	 * retorna uma lista de grupos usuários passando como parametro uma lista de id's
	 * @return Lista de Grupos
	 */
	public 	List<Grupo> findGrupobyId( List<Grupo> listaId );
	
	/**
	 * Retorna o Usuário com o login informado.
	 * 
	 * @param usuario Usuário
	 * @return Usuario
	 */
	@Secured({ "ROLE_BUSCAR_USUARIO" })
	List<Usuario> findByLogin(Usuario usuario);

	/**
	 * Retorna a lista de Usuários.
	 * 
	 * @return List<Usuario>
	 */
	@Secured({ "ROLE_BUSCAR_USUARIO" })
	List<Usuario> findAll();
	

	/**
	 * Retorna uma lista de usuários passando como parametro uma lista de id's
	 * @param listaId
	 * @return Lista de usuários
	 */
	@Secured({ "ROLE_BUSCAR_USUARIO" })
	List<Usuario> find(List<Usuario> listaId);

	
	/**
	 * Retorna um usuário passando como parametro um id
	 * @param id
	 * @return Usuario
	 */
	@Secured({ "ROLE_ATUALIZAR_USUARIO" })
	Usuario findById(Long id);
	
	
	/**
	 * Persiste a entidade Usuario passada como parametro
	 * @param usuario
	 * @return usuario persistido
	 * @throws BusinessException
	 * @throws CadastroDuplicadoException 
	 */
	@Secured( { "ROLE_INCLUIR_USUARIO" })
	@Transactional
	@Log(descricao="Inclusão")
	Usuario persist(Usuario usuario) throws BusinessException, CadastroDuplicadoException;

	/**
	 * Altera a entidade Usuario passada como parametro
	 * @param usuario
	 * @return usuario alterado
	 * @throws BusinessException
	 * @throws CadastroDuplicadoException 
	 */
	@Secured( { "ROLE_ATUALIZAR_USUARIO" })
	@Transactional
	@Log(descricao="Alteração")
	Usuario merge(Usuario usuario) throws BusinessException, CadastroDuplicadoException;

	@Log(descricao="Alteração")
	Usuario mergeSenha(Usuario usuario) throws BusinessException, CadastroDuplicadoException;
	
	/**
	 * Exclui uma entidade Usuario passada como parametro
	 * @param usuario
	 * @return 
	 * @throws BusinessException
	 */
	@Secured( { "ROLE_REMOVER_USUARIO" })
	@Transactional
	@Log(descricao="Exclusão")
	void remove(final Usuario usuario) throws BusinessException;
	
	@Secured( { "ROLE_IMPRIMIR_USUARIO" })
	Relatorio getRelatorio(HashMap<String, Object> parameters, String localRelatorio);

	List<Usuario> findAllAluga();
}


