package util;

import java.io.Serializable;

public class SelectCombo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nome;
	
	public SelectCombo() {
		// TODO Auto-generated constructor stub
	}

	public SelectCombo(Long id, String nome) {
		this.setId(id);
		this.setNome(nome);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
	
	
}
