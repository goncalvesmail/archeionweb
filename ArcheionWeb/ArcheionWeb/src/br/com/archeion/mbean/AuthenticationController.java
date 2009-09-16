package br.com.archeion.mbean;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.context.HttpSessionContextIntegrationFilter;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.ui.WebAuthenticationDetails;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;

import br.com.archeion.jsf.Util;
import br.com.archeion.modelo.usuario.Usuario;


/**
 * Classe responsável por autenticar o usuário.

 */
public class AuthenticationController {


	/**
	 * Login do usuário
	 */
	private String username;

	/**
	 * Senha do usuário
	 */
	private String password;

	/**
	 * Usuário depois de autenticado.
	 */
	private Usuario usuario;

	/**
	 * Método responsável por autenticar o usuário.
	 * 
	 * @return Retorna sucesso ou falha.
	 */
	public String authenticate() {
		String outcome = "falha";

		try {
			final String userName = getUsername();
			String password = getPassword();			
			password = criptografaSenha(password);
			
			final UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(userName,
					password);

			final HttpServletRequest request = getRequest();
			authReq.setDetails(new WebAuthenticationDetails(request));

			final HttpSession session = request.getSession();
			session.setAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY, userName);

			/*
			 * perform authentication
			 */
			final Authentication auth = ((AuthenticationManager) Util.getSpringBean("authenticationManager"))
					.authenticate(authReq);

			/*
			 * initialize the security context.
			 */
			final SecurityContext secCtx = SecurityContextHolder.getContext();
			secCtx.setAuthentication(auth);
			session.setAttribute(HttpSessionContextIntegrationFilter.ACEGI_SECURITY_CONTEXT_KEY, secCtx);

			usuario = (Usuario) auth.getPrincipal();			
			outcome = "sucesso";
		} catch (Exception e) {
			outcome = "falha";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}

		return outcome;
	}


	/**
	 * Efetua o logout do usuário.
	 * 
	 * @return Retorna sempre sucesso.
	 */
	public String logout() {

		final HttpServletRequest request = getRequest();
		request.getSession(false).removeAttribute(HttpSessionContextIntegrationFilter.ACEGI_SECURITY_CONTEXT_KEY);

		/*
		 * simulate the SecurityContextLogoutHandler
		 */
		SecurityContextHolder.clearContext();
		request.getSession(false).invalidate();

		return "sucesso";
	}

	/**
	 * @return Recupera a requisição.
	 */
	private HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	/**
	 * @return the username
	 */
	public final String getUsername() {
		return username;
	}

	/**
	 * @param pUsername
	 *            the pUsername to set
	 */
	public final void setUsername(final String pUsername) {
		this.username = pUsername;
	}

	/**
	 * @return the password
	 */
	public final String getPassword() {
		return password;
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
	 * @param pPassword
	 *            the password to set
	 */
	public final void setPassword(final String pPassword) {
		this.password = pPassword;
	}


	public final Usuario getUsuario() {
		return usuario;
	}


	public final void setUsuario(final Usuario pUsuario) {
		this.usuario = pUsuario;
	}

}