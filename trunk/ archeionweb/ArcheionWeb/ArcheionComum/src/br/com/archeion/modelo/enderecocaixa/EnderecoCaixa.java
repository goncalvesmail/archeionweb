package br.com.archeion.modelo.enderecocaixa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.archeion.modelo.AbstractTO;

@Entity
@Table(name = "TB_ENDERECO_CAIXA")
public class EnderecoCaixa extends AbstractTO implements Serializable {
	
	
	private static final long serialVersionUID = -6913083829560050525L;

	@Id
	@Column(name = "ID_ENDERECO_CAIXA")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name = "VAO_ENDERECO_CAIXA")
	private String vao;
	
	@Column(name = "NU_INICIAL")
	private Integer vaoInicial;

	@Column(name = "NU_FINAL")
	private Integer vaoFinal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVao() {
		return vao;
	}

	public void setVao(String vao) {
		this.vao = vao;
	}

	public Integer getVaoInicial() {
		return vaoInicial;
	}

	public void setVaoInicial(Integer vaoInicial) {
		this.vaoInicial = vaoInicial;
	}

	public Integer getVaoFinal() {
		return vaoFinal;
	}

	public void setVaoFinal(Integer vaoFinal) {
		this.vaoFinal = vaoFinal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (!(obj instanceof EnderecoCaixa)) return false;
		final EnderecoCaixa other = (EnderecoCaixa) obj;
		
		if (id == null && other.id != null) {
			return false;
		} 
		else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("ENDERECO_CAIXA[ID:");
		buff.append(this.id);
		buff.append(",");
		
		buff.append("VAO:");
		buff.append(this.vao);
		buff.append(",");
		
		buff.append("INICIAL:");
		buff.append(this.vaoInicial);
		buff.append(",");
		
		buff.append("FINAL:");
		buff.append(this.vaoFinal);
		buff.append("]");
		
		return buff.toString();
	}
}
