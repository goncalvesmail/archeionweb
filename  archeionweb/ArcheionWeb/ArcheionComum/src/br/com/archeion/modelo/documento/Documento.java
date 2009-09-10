package br.com.archeion.modelo.documento;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.archeion.modelo.AbstractTO;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.modelo.tipodocumento.TipoDocumento;

@Entity
@Table(name = "TB_DOCUMENTO")
public class Documento extends AbstractTO implements Serializable {

	private static final long serialVersionUID = 2747447920361742385L;

	@Id
	@Column(name = "ID_DOCUMENTO")
	@GeneratedValue(strategy=GenerationType.AUTO)	
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.REFRESH)  
	@JoinColumn(name="ID_LOCAL")  
	private Local local;
	
	@Column(name="CS_ORIGEM")
	private Origem origem;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DT_DATA")
	private Date data;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.REFRESH)  
	@JoinColumn(name="ID_TIPO_DOCUMENTO")  
	private TipoDocumento tipoDocumento;
	
	@ManyToOne(fetch=FetchType.EAGER,cascade=CascadeType.REFRESH)  
	@JoinColumn(name="ID_PASTA")  
	private Pasta pasta;
	
	@Column(name="TX_REFERENCIA")
	private String referencia;
	
	@Column(name="TX_REMETENTE")
	private String remetente;
	
	@Column(name="TX_DESTINATARIO")
	private String destinatario;
	
	@Column(name = "TX_OBSERVACAO")
	private String observacao;

	public Documento() {
		local = new Local();
		tipoDocumento = new TipoDocumento();
		pasta = new Pasta();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getRemetente() {
		return remetente;
	}

	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
		
	public Pasta getPasta() {
		return pasta;
	}

	public void setPasta(Pasta pasta) {
		this.pasta = pasta;
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
		final Documento other = (Documento) obj;
		
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
		buff.append("DOCUMENTO[ID:");
		buff.append(this.id);
		buff.append(",");
		
		buff.append("LOCAL:");
		buff.append(this.local.getNome());
		buff.append(",");
		
		buff.append("DATA:");
		buff.append(this.data);
		buff.append("]");
		
		return buff.toString();
	}
}
