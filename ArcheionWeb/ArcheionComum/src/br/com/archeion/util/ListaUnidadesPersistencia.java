package br.com.archeion.util;

/**
 * Lista de Unidades de Persistência possíveis
 */
public enum ListaUnidadesPersistencia {

	/**
	 * Unidade utilizada para persistir os dados no BD.
	 */
	ARCHEION(1, "ARCHEION"),

	/**
	 * Unidade utilizada nos Testes Unitários.
	 */
	JUNIT(2, "JUNIT");

	/**
	 * Valor (Ordinal) da enumeração.
	 */
	private Integer id;

	/**
	 * Descrição (String) da enumeração.
	 */
	private String descricao;

	/**
	 * Construtor da enumeração.
	 * 
	 * @param id the id to set
	 */
	private ListaUnidadesPersistencia(final Integer id) {
		this.id = id;
	}

	/**
	 * Construtor da enumeração.
	 * 
	 * @param id the id to set
	 * @param descricao the descricao to set
	 */
	private ListaUnidadesPersistencia(final Integer id, final String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	/**
	 * @return Recuperando o campo descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @return Recuperando o campo id
	 */
	public Integer getId() {
		return id;
	}

}
