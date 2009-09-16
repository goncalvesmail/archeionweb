/**
 * 
 */
package br.com.archeion.jsf;

/**
 * Define se o Formulário é para edição, inserção, detalhamento, ...
 */
public enum SituacaoFormulario {
	/**
	 * Não está sendo realizada NENHUMA ação pelo formulário.
	 * Isto é, ele não está sendo utilizado.
	 */
	NENHUM,
	
	/**
	 * O formulário está sendo utilizado para inserção de dados.
	 */
	INCLUINDO,
	
	/**
	 * O formulário está sendo utilizado para alteração dos dados.
	 */
	ALTERANDO,
	
	/**
	 * O formulário está sendo utilizado para detalhamento dos dados.
	 */
	DETALHANDO;
}