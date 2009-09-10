package util;

public enum ChavesPasta {
	CAIXETA(1,"Caixeta","u.caixeta"),
	DATAREFERENCIA(2,"Data de Referência","u.dataReferencia","date"),
	EMPRESA(3,"Empresa","u.local.empresa.nome"),
	ITEMDOCUMENTAL(4,"Item Documental","u.itemDocumental.nome"),
	LIMITEDATA(5,"Limite Data","u.limiteDataInicial","date"),
	LIMITENOME(6,"Limite Nome","u.limiteNomeInicial"),
	LIMITEVALOR(7,"Limite Valor","u.limiteNumeroInicial","number"),
	LOCAL(8,"Local","u.local.nome"),
	OBSERVACAO(9,"Observação","u.observacao"),
	TITULOPASTA(10,"Título Pasta","u.titulo"),
	VAO(11,"Vão","u.caixa.vao.vao"),
	VAONUMERO(12,"Número Vão","u.caixa.numeroVao","number");
	
	ChavesPasta(int id, String label, String dataValue) {
		this.id = id;
		this.label = label;
		this.dataValue = dataValue;
		this.conversor = null;
	}
	ChavesPasta(int id, String label, String dataValue, String conversor) {
		this.id = id;
		this.label = label;
		this.dataValue = dataValue;
		this.conversor = conversor;
	}
	private int id;
	private String label;
	private String dataValue;
	private String conversor;
	
	public static String getById(int id){
		for(ChavesPasta c: ChavesPasta.values()){
			if(c.getId() == id){
				return c.getLabel();
			}
		}
		return null;
	}
	
	public static ChavesPasta get(int id){
		for(ChavesPasta c: ChavesPasta.values()){
			if(c.getId() == id){
				return c;
			}
		}
		return null;
	}
	
	public static String getDataBaseValue(int id) {
		for(ChavesPasta c: ChavesPasta.values()){
			if(c.getId() == id){
				return c.getDataValue();
			}
		}
		return null;
	}
	
	public static String getConversorValue(int id) {
		for(ChavesPasta c: ChavesPasta.values()){
			if(c.getId() == id){
				return c.getConversor();
			}
		}
		return null;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getConversor() {
		return conversor;
	}

	public void setConversor(String conversor) {
		this.conversor = conversor;
	}
}