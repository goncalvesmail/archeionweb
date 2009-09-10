package util;

public enum Operadores {
	IGUAL(1,"= igual"," = "),
	MAIOR(2,"> maior que"," > "),
	MENOR(3,"< menor que"," < "),
	MAIORIGUAL(4,">= maior ou igual"," >= "),
	MENORIGUAL(5,"<= menor ou igual"," <= "),
	CONTEM(6,"contém"," like "),	
	DIFERENTE(7,"diferente"," <> ");	
	
	Operadores(int id, String label, String dataValue){
		this.id = id;
		this.label = label;
		this.dataValue = dataValue;
	}
	
	private int id;
	private String label;
	private String dataValue;
	
	public static String getDataBaseValue(int id){
		for(Operadores p: Operadores.values()) {
			if(p.getId() == id){
				return p.getDataValue();
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
}
