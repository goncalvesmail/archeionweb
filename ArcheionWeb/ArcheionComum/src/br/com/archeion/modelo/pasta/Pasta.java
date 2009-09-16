package br.com.archeion.modelo.pasta;

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
import br.com.archeion.modelo.SituacaoExpurgo;
import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.modelo.itemdocumental.ItemDocumental;
import br.com.archeion.modelo.local.Local;

@Entity
@Table(name = "TB_PASTA")
public class Pasta extends AbstractTO implements Serializable {

	private static final long serialVersionUID = -3321067683202053657L;
	
	public Pasta() {
		this.local = new Local();
		this.itemDocumental = new ItemDocumental();
		this.situacao = SituacaoExpurgo.ATIVA;
	}
	
	@Id
	@Column(name = "ID_PASTA")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ID_LOCAL")
	private Local local;
	
	@ManyToOne(fetch=FetchType.EAGER)  
	@JoinColumn(name="ID_ITEM_DOCUMENTAL")
	private ItemDocumental itemDocumental;
	
	@Column(name = "NM_TITULO")
	private String titulo;
	
	@Column(name = "NM_CAIXETA")
	private String caixeta;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_ABERTURA")
	private Date dataAbertura;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_REFERENCIA")
	private Date dataReferencia;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_DATA_LIMITE_INICIAL")
	private Date limiteDataInicial;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_DATA_LIMITE_FINAL")
	private Date limiteDataFinal;
	
	@Column(name = "NU_NUMERO_LIMITE_INICIAL")
	private Integer limiteNumeroInicial;
	
	@Column(name = "NU_NUMERO_LIMITE_FINAL")
	private Integer limiteNumeroFinal;
	
	@Column(name = "NM_NOME_LIMITE_INICIAL")
	private String limiteNomeInicial;
	
	@Column(name = "NM_NOME_LIMITE_FINAL")
	private String limiteNomeFinal;
	
	@Column(name = "TX_OBSERVACAO")
	private String observacao;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_PREVISAO_RECOLHIMENTO")
	private Date previsaoRecolhimento;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_PREVISAO_EXPURGO")
	private Date previsaoExpurgo;
	
	@ManyToOne(fetch=FetchType.EAGER, cascade={CascadeType.REFRESH})
	@JoinColumn(name="ID_CAIXA")
	private Caixa caixa;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_EXPURGO")
	private Date dataExpurgo;	
	
	@Column(name = "CS_SITUACAO_PASTA")
	private SituacaoExpurgo situacao;

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

	public ItemDocumental getItemDocumental() {
		return itemDocumental;
	}

	public void setItemDocumental(ItemDocumental itemDocumental) {
		this.itemDocumental = itemDocumental;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCaixeta() {
		return caixeta;
	}

	public void setCaixeta(String caixeta) {
		this.caixeta = caixeta;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Date getDataReferencia() {
		return dataReferencia;
	}

	public void setDataReferencia(Date dataReferencia) {
		this.dataReferencia = dataReferencia;
	}

	public Date getLimiteDataInicial() {
		return limiteDataInicial;
	}

	public void setLimiteDataInicial(Date limiteDataInicial) {
		this.limiteDataInicial = limiteDataInicial;
	}

	public Date getLimiteDataFinal() {
		return limiteDataFinal;
	}

	public void setLimiteDataFinal(Date limiteDataFinal) {
		this.limiteDataFinal = limiteDataFinal;
	}

	public Integer getLimiteNumeroInicial() {
		return limiteNumeroInicial;
	}

	public void setLimiteNumeroInicial(Integer limiteNumeroInicial) {
		this.limiteNumeroInicial = limiteNumeroInicial;
	}

	public Integer getLimiteNumeroFinal() {
		return limiteNumeroFinal;
	}

	public void setLimiteNumeroFinal(Integer limiteNumeroFinal) {
		this.limiteNumeroFinal = limiteNumeroFinal;
	}

	public String getLimiteNomeInicial() {
		return limiteNomeInicial;
	}

	public void setLimiteNomeInicial(String limiteNomeInicial) {
		this.limiteNomeInicial = limiteNomeInicial;
	}

	public String getLimiteNomeFinal() {
		return limiteNomeFinal;
	}

	public void setLimiteNomeFinal(String limiteNomeFinal) {
		this.limiteNomeFinal = limiteNomeFinal;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getPrevisaoRecolhimento() {
		return previsaoRecolhimento;
	}

	public void setPrevisaoRecolhimento(Date previsaoRecolhimento) {
		this.previsaoRecolhimento = previsaoRecolhimento;
	}

	public Date getPrevisaoExpurgo() {
		return previsaoExpurgo;
	}

	public void setPrevisaoExpurgo(Date previsaoExpurgo) {
		this.previsaoExpurgo = previsaoExpurgo;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
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
	
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("PASTA[ID:");
		buff.append(this.id);
		buff.append(",");
		
		buff.append("TITULO:");
		buff.append(this.titulo);		
		buff.append(",");
		
		buff.append("LOCAL:");
		buff.append(this.local.getNome());		
		buff.append(",");
		
		buff.append("ABERTURA:");
		buff.append(this.dataAbertura);	
		
		buff.append("]");
		
		return buff.toString();
	}
	
}
