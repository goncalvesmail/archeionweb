/**
 * 
 */
package br.com.archeion.exception;

/**
 * Tipos de validações que serão feitas com as suas respectivas mensagens
 * internacionalizaveis.
  */
public enum InvalidFieldType {

	/**
	 * Quando houver uma exceção com o CNPJ.
	 */
	CNPJ("MSG-23", ""),

	/**
	 * Quando houver uma exceção com o CPF.
	 */
	CPF("MSG-11", ""),

	/**
	 * Quando houver uma exceção com o RG.
	 */
	RG("MSG-12", ""),

	/**
	 * Quando houver uma exceção com o NIT.
	 */
	NIT("MSG-10", ""),

	/**
	 * Quandou houver uma exceção com o Email.
	 */
	EMAIL("MSG-21", ""),

	/**
	 * Quandou houver uma exceção com o CEP.
	 */
	CEP("MSG-18", ""),
	
	/**
	 * Quandou houver uma exceção com o ddd do Telefone.
	 */
	TELEFONE_DDD("MSG-221", ""),

	/**
	 * Quandou houver uma exceção com o número do Telefone.
	 */
	TELEFONE_NUMERO("MSG-19", ""),
	
	/**
	 * Quandou houver uma exceção com o ddd do Celular.
	 */
	CELULAR_DDD("MSG-222", ""),

	/**
	 * Quandou houver uma exceção com o número do Celular.
	 */
	CELULAR_NUMERO("MSG-20", ""),

	/**
	 * Quandou houver uma exceção com o número da CTPS.
	 */
	CTPS_NUMERO("MSG-14", ""),

	/**
	 * Quandou houver uma exceção com o número do Título Eleitoral.
	 */
	TITULO_ELEITORAL_NUMERO("MSG-16", ""),

	/**
	 * Quando houver uma exceção com um número.
	 */
	NUMERO("error.business.invalidNumero", ""),
	
	/**
	 * Quando houver uma exceção com uma senha.
	 */
	SENHA("MSG-206", "");
	

	/**
	 * Mensagem que será exibida para o usuário.
	 */
	private final String mensagem;

	/**
	 * Mensagem que será exibida para o usuário.
	 * 
	 * Essa mensagem possui parâmetros.
	 */
	private final String mensagemWithParameter;

	/**
	 * @param msg
	 *            Mensagem que será exibida para o usuário.
	 * @param msgWithParameter
	 *            Mensagem com parâmetro.
	 */
	private InvalidFieldType(final String msg, final String msgWithParameter) {
		this.mensagem = msg;
		this.mensagemWithParameter = msgWithParameter;
	}

	/**
	 * @return O campo msg
	 */
	public String getMensagem() {
		return mensagem;
	}

	/**
	 * @return O campo mensagemWithParameter
	 */
	public String getMensagemWithParameter() {
		return mensagemWithParameter;
	}
}
