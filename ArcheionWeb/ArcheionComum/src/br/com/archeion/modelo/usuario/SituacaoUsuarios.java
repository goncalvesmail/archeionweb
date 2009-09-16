package br.com.archeion.modelo.usuario;

/**
 * Enumeração que relaciona as Situações de um Usuário.
 * 
 */
public enum SituacaoUsuarios {

	/**
	 * Usuário do Sistema ativo
	 */
	ATIVO(1, "Ativo"),

	/**
	 * Usuário do Sistema Inativo
	 */
	INATIVO(2, "Inativo");

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
	private SituacaoUsuarios(final Integer id, final String descricao) {
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



