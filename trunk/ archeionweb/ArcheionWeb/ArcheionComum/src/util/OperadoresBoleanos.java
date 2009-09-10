package util;

public enum OperadoresBoleanos {
	E(1,"E"," AND "),
	OU(2,"OU"," OR ");
	
	OperadoresBoleanos(int id, String label, String dataValue){
		this.id = id;
		this.label = label;
		this.dataValue = dataValue;
	}
	
	private int id;
	private String label;
	private String dataValue;
	
	public static String getDataBaseValue(int id){
		for(OperadoresBoleanos p: OperadoresBoleanos.values()) {
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
