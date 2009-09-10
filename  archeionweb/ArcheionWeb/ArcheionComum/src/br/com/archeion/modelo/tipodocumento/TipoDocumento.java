package br.com.archeion.modelo.tipodocumento;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.archeion.modelo.AbstractTO;

@Entity
@Table(name = "TB_TIPO_DOCUMENTO")
public class TipoDocumento extends AbstractTO implements Serializable {

	private static final long serialVersionUID = 3211626617200806596L;

	@Id
	@Column(name = "ID_TIPO_DOCUMENTO")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name = "NM_TIPO_DOCUMENTO")
	private String nome;

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
		if (!(obj instanceof TipoDocumento)) return false;
		final TipoDocumento other = (TipoDocumento) obj;
		
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
		buff.append("TIPO_DOCUMENTO[ID:");
		buff.append(this.id);
		buff.append(",");
		
		buff.append("NOME:");
		buff.append(this.nome);
		buff.append("]");
		
		return buff.toString();
	}
	
}
