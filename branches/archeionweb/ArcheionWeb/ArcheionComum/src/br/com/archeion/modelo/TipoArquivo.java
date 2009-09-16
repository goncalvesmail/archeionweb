package br.com.archeion.modelo;

public enum TipoArquivo {
	TODOS(4, "Todos"),
	NENHUM(3, "Nenhum"),
	PERMANENTE(1, "Permanente"),
	INTERMEDIARIO(2, "Intermediário");

	private Integer id;
	private String descricao;
	
	private TipoArquivo(final Integer id, final String descricao) {
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
