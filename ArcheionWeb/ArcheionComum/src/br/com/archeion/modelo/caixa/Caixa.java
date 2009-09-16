package br.com.archeion.modelo.caixa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.archeion.modelo.AbstractTO;
import br.com.archeion.modelo.SituacaoExpurgo;
import br.com.archeion.modelo.TipoArquivo;
import br.com.archeion.modelo.enderecocaixa.EnderecoCaixa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.Pasta;

@Entity
@Table(name = "TB_CAIXA")
public class Caixa extends AbstractTO implements Serializable {

	private static final long serialVersionUID = 7950257502750314821L;

	@Id
	@Column(name = "ID_CAIXA")
	@GeneratedValue(strategy=GenerationType.AUTO)	
	private Long id;
	
	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name="ID_LOCAL")  
	private Local local;
	
	@ManyToOne (cascade={CascadeType.REFRESH},optional=true)
	@JoinColumn(name="ID_ENDERECO_CAIXA")  
	private EnderecoCaixa vao;
	
	@Column(name = "NU_VAO_ENDERECO_CAIXA")
	private Integer numeroVao;

	@Column(name = "CS_TIPO_ARQUIVO")
	private TipoArquivo tipo;
	
    @OneToMany(mappedBy="caixa", fetch=FetchType.EAGER, cascade={CascadeType.REFRESH})
    private List<Pasta> pastas;

	@Temporal(TemporalType.DATE)
	@Column(name = "DT_CRIACAO")
	private Date dataCriacao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_EXPURGO")
	private Date dataExpurgo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_PREVISAO_EXPURGO")
	private Date dataPrevisaoExpurgo;
	
	
	@Column(name = "CS_SITUACAO")
	private SituacaoExpurgo situacao;
    
	public Caixa() {
		pastas = new ArrayList<Pasta>();
		local = new Local();
		vao = new EnderecoCaixa();
		dataCriacao = new Date();
		situacao = SituacaoExpurgo.ATIVA;
	}
	
    public String getTitulo() {
    	if ( vao!=null ) return vao.getVao()+"-"+numeroVao;
    	else return situacao.getDescricao();
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

	public EnderecoCaixa getVao() {
		return vao;
	}

	public void setVao(EnderecoCaixa vao) {
		this.vao = vao;
	}

	public Integer getNumeroVao() {
		return numeroVao;
	}

	public void setNumeroVao(Integer numeroVao) {
		this.numeroVao = numeroVao;
	}

	public TipoArquivo getTipo() {
		return tipo;
	}

	public void setTipo(TipoArquivo tipo) {
		this.tipo = tipo;
	}

	public List<Pasta> getPastas() {
		return pastas;
	}

	public void setPastas(List<Pasta> pastas) {
		this.pastas = pastas;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataExpurgo() {
		return dataExpurgo;
	}

	public void setDataExpurgo(Date dataExpurgo) {
		this.dataExpurgo = dataExpurgo;
	}

	public SituacaoExpurgo getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoExpurgo situacao) {
		this.situacao = situacao;
	}

	public Date getDataPrevisaoExpurgo() {
		return dataPrevisaoExpurgo;
	}

	public void setDataPrevisaoExpurgo(Date dataPrevisaoExpurgo) {
		this.dataPrevisaoExpurgo = dataPrevisaoExpurgo;
	}	

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("CAIXA[ID:");
		buff.append(this.id);
		buff.append(",");
		
		buff.append("TITULO:");
		buff.append(this.getTitulo());		
		buff.append(",");
		
		buff.append("LOCAL:");
		buff.append(this.local.getNome());		
		buff.append(",");
		
		if ( this.tipo!=null ) {
			buff.append("TIPO:");
			buff.append(this.tipo.getDescricao());
		}
		
		buff.append("]");
		
		return buff.toString();
	}
}
