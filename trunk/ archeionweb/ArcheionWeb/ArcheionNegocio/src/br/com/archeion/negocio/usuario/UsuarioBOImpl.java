package br.com.archeion.negocio.usuario;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.grupo.Grupo;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.persistencia.grupo.GrupoDAO;
import br.com.archeion.persistencia.usuario.UsuarioDAO;
import br.com.archeion.util.Validator;


/**
 * Implementação da interface UsuarioBO
 * 
 */
public class UsuarioBOImpl implements UsuarioBO, UserDetailsService {
	
	/**
	 * DAO do Usuario
	 */
	private UsuarioDAO usuarioDAO;
	
	/**
	 * DAO do Grupo de usuarios
	 */
	private GrupoDAO grupoDAO;
	
	/**
	 * Classe utilizada para fazer as validações do BO.
	 */
	private Validator validator;
	

	public Usuario salvar(Usuario usuario) {
		String senha = usuario.getSenha();
		senha = criptografaSenha(senha);
		usuario.setSenha(senha);
		return this.usuarioDAO.persist(usuario);
	}

	
	public UserDetails loadUserByUsername(String login ) throws UsernameNotFoundException, DataAccessException {
			Usuario userArcheion = usuarioDAO.findByLogin(login);
			
			return userArcheion;
	}

	public List<Usuario> findByLogin(final Usuario usuario) {
		return this.usuarioDAO.findByLogin(usuario);
	}

	public List<Usuario> findAll() {
		return this.usuarioDAO.findAll();
	}

	/**
	 * retorna um usuario passando como parametro um id
	 */
	public Usuario findById(Long id) {
		return this.usuarioDAO.findById(id);
	}

	/**
	 * retorna uma lista de usuários passando como parametro uma lista de id's
	 */
	public List<Usuario> find(List<Usuario> listaId) {
		return this.usuarioDAO.find(listaId);
	}

	/**
	 * retorna uma lista de grupos de usuarios
	 */
	public List<Grupo> findAllGrupo() {
		return this.grupoDAO.findAll();
	}
	
	/**
	 * retorna uma lista de grupos de usuarios passando como parametro um lista de id's
	 */
	public List<Grupo> findGrupobyId( List<Grupo> listaId ) {
		return this.grupoDAO.findById(listaId);
	}

	/**
	 * Criptografa a Senha.
	 * 
	 * @param senha
	 *            Senha.
	 * @return String Senha criptografada.
	 * @throws NoSuchAlgorithmException
	 *             Exceção.
	 */
	private String criptografaSenha(final String senha) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
			String s = hash.toString(16);
			if (s.length() % 2 != 0)
				s = "0" + s;
			return s;
		} catch (NoSuchAlgorithmException e) {
			return senha;
		}
	}

	/**
	 * @see br.gov.dataprev.siprev.negocio.usuario.UsuarioBO#persist(br.gov.dataprev.siprev.modelo.usuario.Usuario)
	 * @param usuario
	 *            Usuario a ser inserido.
	 * @return Usuario
	 * @throws BusinessException
	 *             Exceção de negócio.
	 * @throws CadastroDuplicadoException 
	 */
	public Usuario persist(Usuario usuario) throws BusinessException, CadastroDuplicadoException {
		this.valida(usuario);
		Usuario usuarioPersistido = usuario;
		validator = new Validator();
		this.validarPersist(usuarioPersistido);		
		if (validator.existError()) {
			throw new BusinessException(validator.getExceptions());
		} else {
			usuarioPersistido.setSenha(criptografaSenha(usuarioPersistido.getSenha()));
			return usuarioDAO.persist(usuarioPersistido);
		}
	}

	/**
	 * @see br.gov.dataprev.siprev.negocio.usuario.UsuarioBO#merge(br.gov.dataprev.siprev.modelo.usuario.Usuario)
	 * @param usuario
	 *            Usuario a ser alterado.
	 * @return Usuario
	 * @throws BusinessException
	 *             Exceção de negócio.
	 * @throws CadastroDuplicadoException 
	 */
	public Usuario merge(Usuario usuario) throws BusinessException, CadastroDuplicadoException {
		this.valida(usuario);
		Usuario usuarioAlterado = usuario;
		validator = new Validator();
		this.validarMerge(usuarioAlterado);		
		if (validator.existError()) {
			throw new BusinessException(validator.getExceptions());
		} else {
			return usuarioDAO.merge(usuarioAlterado);
		}
	}
	
	public Usuario mergeSenha(Usuario usuario) throws BusinessException, CadastroDuplicadoException {
		this.valida(usuario);
		Usuario usuarioAlterado = usuario;
		validator = new Validator();
		this.validarConfirmacaSenha(usuarioAlterado);
		this.validarMerge(usuarioAlterado);		
		if (validator.existError()) {
			throw new BusinessException(validator.getExceptions());
		} else {
			usuarioAlterado.setSenha(criptografaSenha(usuarioAlterado.getSenha()));
			return usuarioDAO.merge(usuarioAlterado);
		}
	}
	
	private void valida(Usuario usuario) throws CadastroDuplicadoException {
		Usuario u = usuarioDAO.findByLogin(usuario.getLogin());
		if(u != null){
			if ( usuario.getId() == u.getId()) {
				return;
			}
			throw new CadastroDuplicadoException();
		}
	}

	/**
	 * @see br.gov.dataprev.siprev.negocio.usuario.UsuarioBO#remove(br.gov.dataprev.siprev.modelo.usuario.Usuario)
	 * @param usuario
	 *            Usuario a ser removido.
	 * @throws BusinessException 
	 */
	public void remove(final Usuario usuarioRemocao) throws BusinessException {
		validator = new Validator();
		this.validarRemove(usuarioRemocao);	
		if (validator.existError()) {
			throw new BusinessException(validator.getExceptions());
		} else {
			this.usuarioDAO.removeById(usuarioRemocao.getId());
		}
	}
	
	/**
	 * Validar o usuario a ser persistido
	 * @param usuarioPersistido
	 */
	private void validarPersist(Usuario usuarioPersistido) {
		this.validarCamposObrigatorios(usuarioPersistido, false);
		this.validarCamposInvalidos(usuarioPersistido);
		this.validarConfirmacaSenha(usuarioPersistido);
		if( this.verificaUsuarioUnico(usuarioPersistido) ) {
			validator.addError(new BusinessException("MSG-208"));
		}
	}

	/**
	 * Validar o usuario a ser alterado
	 * @param usuarioPersistido
	 */
	private void validarMerge(Usuario usuarioAlterado) {
		this.validarCamposObrigatorios(usuarioAlterado, true);
		this.validarCamposInvalidos(usuarioAlterado);
		//Daniel - na alteração do usuário n faz sentido validar senha
		//this.validarConfirmacaSenha(usuarioAlterado);
		if( this.verificaUsuarioUnico(usuarioAlterado) ) {
			validator.addError(new BusinessException("MSG-208"));
		}
	}
	
	/**
	 * Validar a remoção do usuário
	 * @param usuarioRemocao
	 */
	private void validarRemove(Usuario usuarioRemocao) {
		if ( this.verificaExisteServidor(usuarioRemocao) ) {
			validator.addError(new BusinessException("MSG-209"));
		}
	}

	/**
	 * verificar se existe algum servidor vinculado ao usuário a ser removido
	 * @return boolean
	 */
	private boolean verificaExisteServidor(Usuario usuarioRemocao) {
		Boolean retorno = Boolean.FALSE;
	//	List<Servidor> lista = this.servidorBO.findByUsuario(usuarioRemocao);
	//	retorno = (lista != null && lista.size() > 0);
		return retorno;
	}

	/**
	 * Validar campos obrigatórios do usuário
	 * @param usuario
	 * @param validaUsuarioAtivo
	 */
	private void validarCamposObrigatorios(final Usuario usuario, boolean validaUsuarioAtivo) {
		validator.validarCampoObrigatorio(usuario.getNome(),"MSG-200");
		validator.validarCampoObrigatorio(usuario.getLogin(),"MSG-201");
		validator.validarCampoObrigatorio(usuario.getSenha(), "MSG-202");
		validator.validarCampoObrigatorio(usuario.getGrupos(), "MSG-204");
		if ( validaUsuarioAtivo ) {
			validator.validarCampoObrigatorio(usuario.isAtivado(), "MSG-205");
		}
	}

	/**
	 * Validar os campos com preenchimento inválido
	 * @param usuario
	 */
	private void validarCamposInvalidos(final Usuario usuario) {
		//TODO this.validator.validarSenha(usuario);
		
	}

	/**
	 * Validar se os campos senha e confirmação da senha estão iguais
	 * @param usuario
	 */
	private void validarConfirmacaSenha(Usuario usuario) {
		if ( ( usuario.getSenha() != null ) && ( !usuario.getSenha().equals(usuario.getConfirmacaoSenha()) ) ) {
				validator.addError(new BusinessException("MSG-207"));
		}
	}
	
	/**
	 * Verificar se o login do usuário a ser persistido/alterado já existe no banco de dados
	 * @param usuarioPersistido
	 * @return
	 */
	private boolean verificaUsuarioUnico(Usuario usuarioPersistido) {
		Boolean retorno = Boolean.FALSE;
		List<Usuario> lista = null;
		if( usuarioPersistido.getLogin() != null ) {
			lista = this.findByLogin(usuarioPersistido);
		}
		retorno = (lista != null && lista.size() > 0);
		return retorno;
	}
	
	/**
	 * Seta o DAO do usuário
	 * @param usuarioDAO
	 */
	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}
	
	public Relatorio getRelatorio(HashMap<String, Object> parameters,
			String localRelatorio) {
		Connection conn = usuarioDAO.getConnection();
		try {
			Relatorio relatorio = new Relatorio(conn,parameters,localRelatorio);
			return relatorio;
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Usuario> findAllAluga() {
		return usuarioDAO.findAllAluga();
	}


	public GrupoDAO getGrupoDAO() {
		return grupoDAO;
	}


	public void setGrupoDAO(GrupoDAO grupoDAO) {
		this.grupoDAO = grupoDAO;
	}


	public Validator getValidator() {
		return validator;
	}


	public void setValidator(Validator validator) {
		this.validator = validator;
	}


	public UsuarioDAO getUsuarioDAO() {
		return usuarioDAO;
	}

}
