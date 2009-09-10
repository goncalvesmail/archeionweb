package br.com.archeion.modelo.pasta;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.archeion.modelo.AbstractTO;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.modelo.usuario.Usuario;

@Entity
@Table(name = "TB_EMPRESTIMO_PASTA")
public class EmprestimoPasta extends AbstractTO implements Serializable {

	private static final long serialVersionUID = -4695059059889589442L;

	@Id
	@Column(name = "ID_EMPRESTIMO_PASTA")
	@GeneratedValue(strategy=GenerationType.AUTO)	
	private Long id;
	
	@ManyToOne(optional=false) 
    @JoinColumn(referencedColumnName="ID_USUARIO",name = "ID_USUARIO_RESPONSAVEL", nullable=false)
	private Usuario responsavel;
	
	@ManyToOne(optional=false) 
    @JoinColumn(referencedColumnName="ID_USUARIO",name = "ID_USUARIO_SOLICITANTE", nullable=true)
	private Usuario solicitante;
	
	@Column(name = "TX_SOLICITANTE_EXTERNO")
	private String solicitanteExterno;
	
	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name="ID_PASTA")  
	private Pasta pasta;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_EMPRESTIMO")
	private Date dataEmprestimo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_PREVISAO_DEVOLUCAO")
	private Date previsaoDevolucao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_DEVOLUCAO")
	private Date dataDevolucao;
	
	public EmprestimoPasta() {
		this.responsavel = new Usuario();
		this.solicitante = new Usuario();
		this.pasta = new Pasta();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public Usuario getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Usuario solicitante) {
		this.solicitante = solicitante;
	}

	public String getSolicitanteExterno() {
		return solicitanteExterno;
	}

	public void setSolicitanteExterno(String solicitanteExterno) {
		this.solicitanteExterno = solicitanteExterno;
	}

	public Pasta getPasta() {
		return pasta;
	}

	public void setPasta(Pasta pasta) {
		this.pasta = pasta;
	}

	public Date getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(Date dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}

	public Date getPrevisaoDevolucao() {
		return previsaoDevolucao;
	}

	public void setPrevisaoDevolucao(Date previsaoDevolucao) {
		this.previsaoDevolucao = previsaoDevolucao;
	}

	public Date getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(Date dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	
	public String getNomeSolicitante() {
		String nome = null;
		if ( solicitante == null ) nome = solicitanteExterno;
		else nome = solicitante.getNome();
		return nome;
	}
	
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("EMP. PASTA[ID:");
		buff.append(this.id);
		buff.append(",");
		
		buff.append("PASTA:");
		buff.append(this.pasta.getTitulo());
		buff.append(",");
		
		buff.append("RESPONSAVEL:");
		buff.append(this.responsavel.getNome());
		buff.append(",");
		
		buff.append("SOLICITANTE:");
		buff.append(this.getSolicitante());
		buff.append("]");
		
		return buff.toString();
	}
}
