/**
 * 
 */
package br.com.archeion.exception;

import org.apache.log4j.Logger;

import br.com.archeion.resources.MessagesResources;

/**
 * Exceção base para toda a aplicação.
  
 */
public class ArcheionException extends Exception {


	private static final long serialVersionUID = 2509265031138983635L;
	/**
	 * Log da exceção.
	 */
	private static final Logger LOG = Logger.getLogger(ArcheionException.class);

	/**
	 * 
	 */
	public ArcheionException() {
		LOG.info("Instanciando exceção");
	}

	/**
	 * @param message
	 *            Mensagem que será exibida
	 */
	public ArcheionException(final String message) {
		super(message);
		LOG.info("Instanciando exceção");
	}

	/**
	 * @param message
	 *            Mensagem que será exibida
	 * @param cause
	 *            Causa da exceção.
	 */
	public ArcheionException(final String message, final Throwable cause) {
		super(message, cause);
		LOG.info("Instanciando exceção");
	}

	/**
	 * @param cause
	 *            Causa da exceção.
	 */
	public ArcheionException(final Throwable cause) {
		super(cause);
		LOG.info("Instanciando exceção");
	}

	/**
	 * @return Retorna a mensagem de erro internacionalizável.
	 */
	public String getErrorI18n() {
		String message = super.getMessage();
		String i18n = null;
		if (message != null) {
			LOG.info("Recuperando mensagem de erro internacionalizada: " + message);
			i18n = MessagesResources.getBundleMessage(message);
		}
		return i18n;
	}

	/**
	 * @param params
	 *            Array com todos os parâmetros.
	 * @return Retorna a mensagem de erro internacionalizável.
	 */
	public String getErrorI18n(final String[] params) {
		String message = super.getMessage();
		LOG.info("Recuperando mensagem de erro internacionalizada: " + message);
		return MessagesResources.getBundleMessage(message, params);
	}

	/**
	 * 
	 * @see java.lang.Throwable#getMessage()
	 * 
	 * @return A mensagem internacionalizada.
	 */
	@Override
	public String getMessage() {
		return getErrorI18n();
	}
	
	public String getMessageCode() {
		return super.getMessage();
	}
}
