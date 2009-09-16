/**
 * 
 */
package br.com.archeion.exception;

import br.com.archeion.resources.MessagesResources;

/**
 * Exceção retornada em caso de entidade não encontrada.
 * 
 */
public class EntityNotFoundException extends BusinessException {

	/**
	 * serialVersionUID de EntityNotFoundException.
	 */
	private static final long serialVersionUID = 6205059783156737675L;

	/**
	 * Mensagem que será exibida para o usuário.
	 */
	private static final String MSG_EXCEPTION = "error.business.entityNotFound";

	/**
	 * Campo que gerou a exceção.
	 */
	private final String entity;

	/**
	 * Construtor
	 * 
	 * @param entity
	 *            nome da entidade que gerou a exceção
	 */
	public EntityNotFoundException(final String entity) {
		super(MSG_EXCEPTION);
		this.entity = entity;

	}

	@Override
	public String getErrorI18n() {
		return super.getErrorI18n(new String[] { entity });
	}

	/**
	 * 
	 * @see br.com.archeion.exception.ArcheionException#getMessage()
	 * 
	 * @return Mensagem
	 */
	@Override
	public String getMessage() {
		String fieldName = MessagesResources.getBundleMessage(this.getEntity());
		return super.getErrorI18n(new String[] { fieldName });
	}

	/**
	 * @return entity
	 */
	public String getEntity() {
		return entity;
	}

}
