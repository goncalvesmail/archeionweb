/**
 * 
 */
package br.com.archeion.exception;

/**
 * Exceção retornada em caso de entidade encontrada.
 * 
 */
public class EntityFoundException extends BusinessException {

	/**
	 * serialVersionUID de EntityFoundException.
	 */
	private static final long serialVersionUID = 6205059783156737675L;

	/**
	 * Mensagem que será exibida para o usuário.
	 */
	private static final String MSG_EXCEPTION = "error.business.entityFound";

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
	public EntityFoundException(final String entity) {
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
		String[] fields = new String[] { this.getEntity() };
		return super.getErrorI18n(fields);
	}

	/**
	 * @return entity
	 */
	public String getEntity() {
		return entity;
	}

}
