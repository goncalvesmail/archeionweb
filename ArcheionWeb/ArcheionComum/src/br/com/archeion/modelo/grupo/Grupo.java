/**
 * 
 */
package br.com.archeion.modelo.grupo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import br.com.archeion.modelo.AbstractTO;
import br.com.archeion.modelo.funcionalidade.Funcionalidade;
import br.com.archeion.modelo.usuario.Usuario;

@Entity
@Table(name = "TB_GRUPOS")
public class Grupo extends AbstractTO implements Serializable
{

	private static final long serialVersionUID = 638288521660781058L;

	@Id
	@Column(name = "ID_GRUPO")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name = "NM_GRUPO")
	private String nome;

	@ManyToMany(mappedBy = "grupos")
	private List<Usuario> usuarios;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(
        name="TB_FUNCIONALIDADES_GRUPOS",
        joinColumns={@JoinColumn(name="ID_GRUPO")},
        inverseJoinColumns={@JoinColumn(name="ID_FUNCIONALIDADE")}
    )
    private List<Funcionalidade> funcionalidades;

	public Long getId() {
		return id;
	}
	public void setId(Long idGrupo) {
		this.id = idGrupo;
	}
	public List<Funcionalidade> getFuncionalidades() {
		return funcionalidades;
	}
	public void setFuncionalidades(List<Funcionalidade> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
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
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Grupo))
			return false;
		final Grupo other = (Grupo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("GRUPO[ID:");
		buff.append(this.id);
		buff.append(",");
		
		buff.append("NOME:");
		buff.append(this.nome);
		buff.append("]");
		
		return buff.toString();
	}
	
	
}