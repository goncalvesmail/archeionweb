package br.com.archeion.modelo;

public enum TipoPrevisao {
	PERMANENTE(1, "Recolhimento (AC -> AP)"),
	INTERMEDIARIO(2, "Transferencia (AC -> AI)");

	private Integer id;
	private String descricao;
	
	private TipoPrevisao(final Integer id, final String descricao) {
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
