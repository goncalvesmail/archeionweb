package br.com.archeion.modelo.local;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.archeion.modelo.AbstractTO;
import br.com.archeion.modelo.empresa.Empresa;

@Entity
@Table(name = "TB_LOCAL")
public class Local extends AbstractTO implements Serializable {

	private static final long serialVersionUID = 2396089667502557901L;
	
	public Local() {
		this.empresa = new Empresa();
	}

	@Id
	@Column(name = "ID_LOCAL")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name = "NM_LOCAL")
	private String nome;
	
	@Column(name = "NU_ULTIMO_DOCUMENTO")
	private Long ultimoDocumento;

	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ID_EMPRESA")  
	private Empresa empresa;

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

	public Long getUltimoDocumento() {
		if (ultimoDocumento==null) ultimoDocumento = 0l;
		return ultimoDocumento;
	}

	public void setUltimoDocumento(Long ultimoDocumento) {
		this.ultimoDocumento = ultimoDocumento;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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
		if (!(obj instanceof Local)) return false;
		final Local other = (Local) obj;
		
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
		buff.append("LOCAL[ID:");
		buff.append(this.id);
		buff.append(",");
		
		buff.append("NOME:");
		buff.append(this.nome);
		buff.append(",");
		
		buff.append("ULTIMO DOC:");
		buff.append(this.ultimoDocumento);
		buff.append("]");
		
		return buff.toString();
	}
}
