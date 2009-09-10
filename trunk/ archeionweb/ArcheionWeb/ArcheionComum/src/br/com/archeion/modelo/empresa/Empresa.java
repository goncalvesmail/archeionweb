package br.com.archeion.modelo.empresa;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.archeion.modelo.AbstractTO;

@Entity
@Table(name = "TB_EMPRESA")
public class Empresa extends AbstractTO implements Serializable {

	private static final long serialVersionUID = 2396089667502557901L;

	@Id
	@Column(name = "ID_EMPRESA")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name = "NM_EMPRESA")
	private String nome;
	
	@Column(name = "NM_RAZAO_SOCIAL")
	private String razaoSocial;
	
	@Column(name = "SG_EMPRESA")
	private String sigla;
	
	@Column(name = "ID_EMPRESA_PAI", nullable = true)
	private Long idPai;
	
	@Column(name = "CS_VISIVEL", nullable = false)
	private Integer visivel = 0;

	
	@OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="TB_EMPRESA",
        joinColumns={@JoinColumn(name="ID_EMPRESA_PAI")},
        inverseJoinColumns={@JoinColumn(name="ID_EMPRESA")}
    )
	private List<Empresa> filhos;
	
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
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}	
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	public List<Empresa> getFilhos() {
		return filhos;
	}
	public void setFilhos(List<Empresa> filhos) {
		this.filhos = filhos;
	}	
	public Long getIdPai() {
		return idPai;
	}
	public void setIdPai(Long idPai) {
		this.idPai = idPai;
	}
	
	public boolean isVisivel() {
		return this.visivel==1?Boolean.TRUE:Boolean.FALSE;
	}
	
	public boolean getVisivel() {
		return this.visivel==1?Boolean.TRUE:Boolean.FALSE;
	}

	public void setVisivel(boolean visivel) {
		this.visivel = visivel?1:0;
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
		if (!(obj instanceof Empresa)) return false;
		final Empresa other = (Empresa) obj;
		
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
		buff.append("EMPRESA[ID:");
		buff.append(this.id);
		buff.append(",");
		
		buff.append("NOME:");
		buff.append(this.nome);
		buff.append(",");
		
		buff.append("RAZAO SOCIAL:");
		buff.append(this.razaoSocial);
		buff.append("]");
		
		return buff.toString();
	}
}
