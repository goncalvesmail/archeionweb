package br.com.archeion.mbean.graficos;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.jfree.data.category.DefaultCategoryDataset;

import br.com.archeion.jsf.Util;
import br.com.archeion.mbean.ArcheionBean;
import br.com.archeion.modelo.TipoPrevisao;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.negocio.empresa.EmpresaBO;
import br.com.archeion.negocio.local.LocalBO;
import br.com.archeion.negocio.pasta.PastaBO;

public class GraficoTemporalidadeMBean extends ArcheionBean {

	private Pasta pasta;
	private Date inicio;
	private Date fim;
	private List<Pasta> listaPasta;	
	private TipoPrevisao previsao;
	private boolean diario = false;
	
	private List<SelectItem> listaEmpresa;
	private List<SelectItem> listaLocal;
	private List<SelectItem> listaPrevisao;
	
	private PastaBO pastaBO = (PastaBO) Util.getSpringBean("pastaBO");
	private EmpresaBO empresaBO = (EmpresaBO) Util.getSpringBean("empresaBO");
	private LocalBO localBO = (LocalBO) Util.getSpringBean("localBO");
	
	public String goToGrafico() {
		pasta = new Pasta();
		inicio = new Date();
		fim = new Date();
		previsao = TipoPrevisao.PERMANENTE;
		
		listaPrevisao = new ArrayList<SelectItem>();
		for (TipoPrevisao previsao : TipoPrevisao.values()) {
			listaPrevisao.add(new SelectItem(previsao.toString(), previsao.getDescricao()));
		}
		
		preencherCombos();
		
		return "graficoTemporalidade";
	}
	
	public String pesquisar() {
		return "graficoTemporalidade";
	}
	
	public void valueChangedLista(ValueChangeEvent event) {

		Long empId = (Long)event.getNewValue();
		Empresa empresa = new Empresa();
		empresa.setId(empId);
		
		pasta.getLocal().setEmpresa(empresa);
		
		preencherCombos();	
	}
	
	private void preencherCombos() {
		listaEmpresa = new ArrayList<SelectItem>();
		listaLocal = new ArrayList<SelectItem>();
		
		List<Empresa> empresas = empresaBO.findAll();
		for(Empresa emp: empresas) {
			listaEmpresa.add(new SelectItem(emp.getId(),emp.getNome()));
		}
		
		if ( (pasta.getLocal().getEmpresa() == null || 
				pasta.getLocal().getEmpresa().getId() == null || 
				pasta.getLocal().getEmpresa().getId() == 0) && empresas.size()>0 ) {
			pasta.getLocal().setEmpresa(empresas.get(0));
		}
		
		List<Local> locais = localBO.findByEmpresa(pasta.getLocal().getEmpresa());
		for(Local loc: locais){
			listaLocal.add(new SelectItem(loc.getId(),loc.getNome())); 
		}
		
		if ( (locais!=null && locais.size()>0 && 
				(pasta.getLocal()==null || pasta.getLocal().getId()==null 
						|| pasta.getLocal().getId()==0)) && locais.size()>0 ) {
			pasta.setLocal(locais.get(0));
		}
	}
	
	@SuppressWarnings("deprecation")
	public DefaultCategoryDataset getCategoryDataset() {

		Empresa empresa = pasta.getLocal().getEmpresa();
		Local local = pasta.getLocal();
		
		if ( previsao == TipoPrevisao.INTERMEDIARIO )
			listaPasta = pastaBO.consultaTemporarioRecolhimentoIntervalo(empresa, local, inicio, fim);
		else if ( previsao == TipoPrevisao.PERMANENTE )
			listaPasta = pastaBO.consultaPermanenteRecolhimentoIntervalo(empresa, local, inicio, fim);
		
		Hashtable<String,DadosGrafico> dados = new Hashtable<String,DadosGrafico>();		
		for ( Pasta pasta:listaPasta ) {

			StringBuffer label = new StringBuffer();
			if ( diario ) {
				label.append(String.valueOf(pasta.getPrevisaoRecolhimento().getDate()));
				label.append("/");
			}
			label.append(String.valueOf(pasta.getPrevisaoRecolhimento().getMonth()+1));
			label.append("/");
			label.append(String.valueOf(pasta.getPrevisaoRecolhimento().getYear()+1900));
			
			DadosGrafico dadosGrafico = dados.get(label.toString());
			if ( dadosGrafico!=null ) {
				dadosGrafico.setValor( dadosGrafico.getValor()+1 );
			}
			else {
				dadosGrafico = new DadosGrafico();
				dadosGrafico.setLabel(label.toString());
				dadosGrafico.setValor(1l);
			}
			
			dados.put(label.toString(), dadosGrafico);
		}

		DefaultCategoryDataset categoryDataSet = new DefaultCategoryDataset();
		
		Enumeration<DadosGrafico> elementos = dados.elements();
		while ( elementos.hasMoreElements() ) {
			DadosGrafico dado = elementos.nextElement();			
			categoryDataSet.addValue(dado.getValor(), "", dado.getLabel());			
		}

		
		return categoryDataSet;
	}

	public Pasta getPasta() {
		return pasta;
	}


	public void setPasta(Pasta pasta) {
		this.pasta = pasta;
	}


	public Date getInicio() {
		return inicio;
	}


	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}


	public Date getFim() {
		return fim;
	}


	public void setFim(Date fim) {
		this.fim = fim;
	}


	public List<Pasta> getListaPasta() {
		return listaPasta;
	}


	public void setListaPasta(List<Pasta> listaPasta) {
		this.listaPasta = listaPasta;
	}


	public List<SelectItem> getListaEmpresa() {
		return listaEmpresa;
	}


	public void setListaEmpresa(List<SelectItem> listaEmpresa) {
		this.listaEmpresa = listaEmpresa;
	}


	public List<SelectItem> getListaLocal() {
		return listaLocal;
	}


	public void setListaLocal(List<SelectItem> listaLocal) {
		this.listaLocal = listaLocal;
	}

	

	public TipoPrevisao getPrevisao() {
		return previsao;
	}


	public void setPrevisao(TipoPrevisao previsao) {
		this.previsao = previsao;
	}

	public List<SelectItem> getListaPrevisao() {
		return listaPrevisao;
	}


	public void setListaPrevisao(List<SelectItem> listaPrevisao) {
		this.listaPrevisao = listaPrevisao;
	}

	public boolean isDiario() {
		return diario;
	}


	public void setDiario(boolean diario) {
		this.diario = diario;
	}



	class DadosGrafico {
		String label;
		Long valor;
		String empresa;
		
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public Long getValor() {
			return valor;
		}
		public void setValor(Long valor) {
			this.valor = valor;
		}
		public String getEmpresa() {
			return empresa;
		}
		public void setEmpresa(String empresa) {
			this.empresa = empresa;
		}
		
	}
}
