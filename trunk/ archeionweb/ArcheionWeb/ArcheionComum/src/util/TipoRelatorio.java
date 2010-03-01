package util;

public enum TipoRelatorio {
	ETIQUETAPASTACAIXETA("Etiqueta pasta por caixeta"),
	ETIQUETAPASTACAIXA("Etiqueta pasta por caixa");
	
	TipoRelatorio(String descricao){
		this.descricao = descricao;
	}
	
	private String descricao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
