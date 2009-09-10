/**
 * 
 */
package br.com.archeion.exception;

import br.com.archeion.resources.MessagesResources;


/**
 * Exceção para todos os campos onde o preenchimento é obrigatório.
  */
public class RequiredFieldException extends BusinessException {

	/**
	 * serialVersionUID do objeto RequiredFieldException.java
	 */
	private static final long serialVersionUID = 9011613300227261131L;

	/**
	 * Mensagem que será exibida para o usuário.
	 */
	private static final String MSG_EXCEPTION = "error.business.requiredField";

	/**
	 * Campo que gerou a exceção.
	 */
	private final String field;

	/**
	 * @param field
	 *            Campo que gerou a exceção.
	 */
	public RequiredFieldException(final String field) {
		super(MSG_EXCEPTION);
		this.field = field;
	}


	@Override
	public String getErrorI18n() {
		return super.getErrorI18n(new String[]{field});
	}

	/**
	 * @return O campo field
	 */
	public String getField() {
		return field;
	}
	
	/**
	 * @see br.com.archeion.exception.ArcheionException#getMessage()
	 * @return String
	 */
	@Override
	public String getMessage() {
		String fieldName = MessagesResources.getBundleMessage(this.getField());
		return super.getErrorI18n(new String[]{fieldName});
	}
}
