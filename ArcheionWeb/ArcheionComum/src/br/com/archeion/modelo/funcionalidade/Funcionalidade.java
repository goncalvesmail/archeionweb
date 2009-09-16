package br.com.archeion.modelo.funcionalidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.archeion.modelo.AbstractTO;

@Entity
@Table(name = "TB_FUNCIONALIDADES")
public class Funcionalidade extends AbstractTO implements Serializable {
	
	private static final long serialVersionUID = -453239442912892417L;

	@Id
	@Column(name = "ID_FUNCIONALIDADE")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name = "NM_FUNCIONALIDADE")
	private String nome;
	
	@Column(name = "DESC_FUNCIONALIDADE")
	private String descricao;

	public Long getId() {
		return id;
	}
	public void setId(Long idFuncionalidade) {
		this.id = idFuncionalidade;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
		if (!(obj instanceof Funcionalidade)) return false;
		final Funcionalidade other = (Funcionalidade) obj;
		
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
		buff.append("FUNCIONALIDADE[ID:");
		buff.append(this.id);
		buff.append(",");
		
		buff.append("NOME:");
		buff.append(this.nome);
		buff.append("]");
		
		return buff.toString();
	}
}
