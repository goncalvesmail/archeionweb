package br.com.archeion.modelo;

public enum SituacaoExpurgo {
	
	TODOS(1, "Todas"),
	ATIVA(2, "Ativa"),
	EXPURGADA(3, "Expurgada");	

	private Integer id;
	private String descricao;
	
	private SituacaoExpurgo(final Integer id, final String descricao) {
		this.id = id;
		this.descricao = descricao;
	}
	public String getDescricao() {
		return descricao;
	}
	public Integer getId() {
		return id;
	}
}
