package br.com.archeion.mbean.graficos;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.jfree.data.category.DefaultCategoryDataset;

import br.com.archeion.jsf.Util;
import br.com.archeion.mbean.ArcheionBean;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.itemdocumental.ItemDocumental;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.ttd.TTD;
import br.com.archeion.negocio.empresa.EmpresaBO;
import br.com.archeion.negocio.itemdocumental.ItemDocumentalBO;
import br.com.archeion.negocio.local.LocalBO;
import br.com.archeion.negocio.ttd.TTDBO;

public class GraficoDocumentoMBean extends ArcheionBean {

	private List<TTD> listaTTD;
	private Empresa empresa;
	private Local local;
	private ItemDocumental item;
		
	private List<SelectItem> listaEmpresa;
	private List<SelectItem> listaLocal;
	private List<SelectItem> listaTipoDocumento;

	private EmpresaBO empresaBO = (EmpresaBO) Util.getSpringBean("empresaBO");
	private LocalBO localBO = (LocalBO) Util.getSpringBean("localBO");
	private TTDBO ttdBO = (TTDBO) Util.getSpringBean("ttdBO");
	private ItemDocumentalBO tipoDocumentoBO = (ItemDocumentalBO) Util.getSpringBean("itemDocumentalBO");

	public String goToGrafico() {
		listaLocal = new ArrayList<SelectItem>();
		listaTipoDocumento = new ArrayList<SelectItem>();
		listaEmpresa = new ArrayList<SelectItem>();
		
		empresa = new Empresa();
		empresa.setId(-1l);
		
		local = new Local();
		local.setId(-1l);
		
		List<ItemDocumental> tipos = tipoDocumentoBO.findAll();		
		listaTipoDocumento = new ArrayList<SelectItem>();
		for(ItemDocumental e:tipos) {
			SelectItem select = new SelectItem(e.getId(),e.getNome());
			listaTipoDocumento.add(select);
		}
		item = new ItemDocumental();
		item.setId(-1l);
		
		preencherCombos();
		return "graficoDocumento";
	}
	
	
	public String pesquisar() {
		return "graficoDocumento";
	}
	
	public void valueChangedLista(ValueChangeEvent event) {

		Long empId = (Long)event.getNewValue();
		empresa = new Empresa();
		empresa.setId(empId);
		
		preencherCombos();	
	}
	
	private void preencherCombos() {
		listaEmpresa = new ArrayList<SelectItem>();
		listaLocal = new ArrayList<SelectItem>();
		
		List<Empresa> empresas = empresaBO.findAll();
		for(Empresa emp: empresas) {
			listaEmpresa.add(new SelectItem(emp.getId(),emp.getNome()));
		}
		
		if ( empresa!=null && empresa.getId()>0 ) {
			List<Local> locais = localBO.findByEmpresa(empresa);
			for(Local loc: locais){
				listaLocal.add(new SelectItem(loc.getId(),loc.getNome())); 
			}
			local = new Local();
			local.setId(-1l);
		}
	}
	
	public DefaultCategoryDataset getCategoryDataset() {

		listaTTD = ttdBO.findByEmpresaLocalItemDocumental(empresa.getId().intValue(), 
				local.getId().intValue(), item.getId().intValue());
		
		Hashtable<String,DadosGrafico> dados = new Hashtable<String,DadosGrafico>();
		
		for ( TTD ttd:listaTTD ) {
			String label = ttd.getLocal().getEmpresa().getNome()+"/"+ttd.getLocal().getNome();
			
			DadosGrafico dadosGrafico = dados.get(label);
			if ( dadosGrafico!=null ) {
				dadosGrafico.setValor( dadosGrafico.getValor()+1 );
			}
			else {
				dadosGrafico = new DadosGrafico();
				dadosGrafico.setEmpresa(ttd.getLocal().getEmpresa().getNome());
				dadosGrafico.setLabel(label);
				dadosGrafico.setValor(1l);
			}
			dados.put(label, dadosGrafico);
		}

		DefaultCategoryDataset categoryDataSet = new DefaultCategoryDataset();
		
		Enumeration<DadosGrafico> elementos = dados.elements();
		while ( elementos.hasMoreElements() ) {
			DadosGrafico dado = elementos.nextElement();
			
			categoryDataSet.addValue(dado.getValor(), "", dado.getLabel());			
		}

		
		return categoryDataSet;
	}
	
	public List<TTD> getListaTTD() {
		return listaTTD;
	}

	public void setListaTTD(List<TTD> listaTTD) {
		this.listaTTD = listaTTD;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public ItemDocumental getItem() {
		return item;
	}

	public void setItem(ItemDocumental item) {
		this.item = item;
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

	public List<SelectItem> getListaTipoDocumento() {
		return listaTipoDocumento;
	}

	public void setListaTipoDocumento(List<SelectItem> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
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
