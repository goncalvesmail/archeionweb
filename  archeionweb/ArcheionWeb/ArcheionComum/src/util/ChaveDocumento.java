package util;


public enum ChaveDocumento {
	CAIXETA(1,"Caixeta","u.pasta.caixeta"),
	DATA(2,"Data do Documento","u.data","date"),
	DESTINATARIO(3,"Destinatário","u.destinatario"),
	REMETENTE(4,"Remetente","u.remetente"),
	EMPRESA(5,"Empresa","u.local.empresa.nome"),
	LOCAL(6,"Local","u.local.nome"),
	OBSERVACAO(7,"Observação","u.observacao"),
	REFERENCIA(8,"Referência","u.referencia"),
	TITULOPASTA(9,"Título Pasta","u.pasta.titulo"),
	TIPODOCUMENTO(10,"Tipo de Documento","u.tipoDocumento.nome");
	
	ChaveDocumento(int id, String label, String dataValue) {
		this.id = id;
		this.label = label;
		this.dataValue = dataValue;
		this.conversor = null;
	}
	ChaveDocumento(int id, String label, String dataValue, String conversor) {
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
		for(ChaveDocumento c: ChaveDocumento.values()){
			if(c.getId() == id){
				return c.getLabel();
			}
		}
		return null;
	}
	
	public static ChaveDocumento get(int id){
		for(ChaveDocumento c: ChaveDocumento.values()){
			if(c.getId() == id){
				return c;
			}
		}
		return null;
	}
	
	public static String getDataBaseValue(int id) {
		for(ChaveDocumento c: ChaveDocumento.values()){
			if(c.getId() == id){
				return c.getDataValue();
			}
		}
		return null;
	}
	
	public static String getConversorValue(int id) {
		for(ChaveDocumento c: ChaveDocumento.values()){
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