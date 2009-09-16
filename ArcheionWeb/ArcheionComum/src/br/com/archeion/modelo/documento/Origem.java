package br.com.archeion.modelo.documento;

public enum Origem {
	/**
	 * Origem do Sistema Emitido
	 */
	EMITIDO(1, "Emitido"),

	/**
	 * Origem do Sistema Recebido
	 */
	RECEBIDO(2, "Recebido");

	/**
	 * Valor (Ordinal) da enumeração
	 */
	private Integer id;

	/**
	 * Descrição (String) da enumeração
	 */
	private String descricao;

	/**
	 * Construtor da enumeração
	 * 
	 * @param id Identificador
	 * @param descricao Descrição
	 */
	private Origem(final Integer id, final String descricao) {
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
