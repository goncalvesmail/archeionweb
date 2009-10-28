package br.com.archeion.mbean.ttd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.acegisecurity.AccessDeniedException;

import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.jsf.Constants;
import br.com.archeion.jsf.Util;
import br.com.archeion.mbean.ArcheionBean;
import br.com.archeion.mbean.ExceptionManagedBean;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.eventocontagem.EventoContagem;
import br.com.archeion.modelo.itemdocumental.ItemDocumental;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.ttd.TTD;
import br.com.archeion.negocio.empresa.EmpresaBO;
import br.com.archeion.negocio.eventocontagem.EventoContagemBO;
import br.com.archeion.negocio.itemdocumental.ItemDocumentalBO;
import br.com.archeion.negocio.local.LocalBO;
import br.com.archeion.negocio.relatoriotxt.RelatorioTxtBO;
import br.com.archeion.negocio.ttd.TTDBO;

public class TTDMBean extends ArcheionBean {

	private TTD ttd;
	private List<TTD> listaTTD;
	private TTDBO ttdBO = (TTDBO) Util.getSpringBean("ttdBO");
	private EmpresaBO empresaBO = (EmpresaBO) Util.getSpringBean("empresaBO");
	private LocalBO localBO = (LocalBO) Util.getSpringBean("localBO");
	private ItemDocumentalBO itemDocumentalBO = (ItemDocumentalBO) Util.getSpringBean("itemDocumentalBO");
	private EventoContagemBO eventoContagemBO = (EventoContagemBO) Util.getSpringBean("eventoContagemBO");
	private RelatorioTxtBO relatorioTxtBO = (RelatorioTxtBO) Util.getSpringBean("relatorioTxtBO");
	
	private List<SelectItem> listaEmpresa;
	private List<SelectItem> listaLocal;
	private List<SelectItem> listaItemDocumental;
	private List<SelectItem> listaEventoContagem;

	private int id;
	private boolean visualizar = false;

	public TTDMBean() {
		ttd = new TTD();
		listaTTD = new ArrayList<TTD>();
	}

	public String goToVisualizar() {
		visualizar=true;
		return goToAlterar();
	}
	
	public String incluir() {
		try {
			incluirMBean();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
			return "formularioTTD";
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",
					ArcheionBean.PERSIST_FAILURE);
			return "formularioTTD";
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util
			.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return findAll();
	}

	public String incluirMais() {
		try {
			incluirMBean();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
			return "formularioTTD";
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return "formularioTTD";
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util
			.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return this.goToForm();
	}

	public void incluirMBean() throws AccessDeniedException, 
				CadastroDuplicadoException, BusinessException {

		if(ttd.getTemporaliedadeSelecionada().equals("1")){
			ttd.setArquivoPermanente(false);
			ttd.setArquivoIntermediario(false);
			ttd.setTempoArquivoIntermediario(0);
		} else if(ttd.getTemporaliedadeSelecionada().equals("2")){
			ttd.setArquivoPermanente(true);
			ttd.setArquivoIntermediario(false);
			ttd.setTempoArquivoIntermediario(0);
		} else if(ttd.getTemporaliedadeSelecionada().equals("3")){
			ttd.setArquivoPermanente(false);
			ttd.setArquivoIntermediario(true);
		}

		Local local = localBO.findById(ttd.getLocal().getId());
		ttd.setLocal(local);

		ItemDocumental item = itemDocumentalBO.findById(ttd.getItemDocumental().getId());
		ttd.setItemDocumental(item);

		EventoContagem evento = eventoContagemBO.findById(ttd.getEventoContagem().getId());
		ttd.setEventoContagem(evento);

		ttd.setId(null);
		
		ttdBO.persist(ttd);

		addMessage(FacesMessage.SEVERITY_INFO, "geral.inclusao.sucesso",ArcheionBean.PERSIST_SUCESS);

	}

	public String goToAlterar() {
		try {
			
			Long id = Long.valueOf(Util.getParameter("_id"));
			ttd = ttdBO.findById(id);

			if(ttd.getArquivoPermanente()){
				ttd.setTemporaliedadeSelecionada("2");
			} else if(ttd.getArquivoIntermediario()){
				ttd.setTemporaliedadeSelecionada("3");
			} else {
				ttd.setTemporaliedadeSelecionada("1");
			}

			this.preencherCombosAlterar();
			
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util
			.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return "formularioAlterarTTD";
	}

	public String alterar() {
		try {
			if(ttd.getTemporaliedadeSelecionada().equals("1")){
				ttd.setArquivoPermanente(false);
				ttd.setArquivoIntermediario(false);
				ttd.setTempoArquivoIntermediario(0);
			} else if(ttd.getTemporaliedadeSelecionada().equals("2")){
				ttd.setArquivoPermanente(true);
				ttd.setArquivoIntermediario(false);
				ttd.setTempoArquivoIntermediario(0);
			} else if(ttd.getTemporaliedadeSelecionada().equals("3")){
				ttd.setArquivoPermanente(false);
				ttd.setArquivoIntermediario(true);
			}

			Local local = localBO.findById(ttd.getLocal().getId());
			ttd.setLocal(local);

			ItemDocumental item = itemDocumentalBO.findById(ttd.getItemDocumental().getId());
			ttd.setItemDocumental(item);

			EventoContagem evento = eventoContagemBO.findById(ttd.getEventoContagem().getId());
			ttd.setEventoContagem(evento);
			ttd = ttdBO.merge(ttd);

			addMessage(FacesMessage.SEVERITY_INFO, "geral.alteracao.sucesso",
					ArcheionBean.PERSIST_SUCESS);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
			
			ttd = ttdBO.findById(ttd.getId());

			if(ttd.getArquivoPermanente()){
				ttd.setTemporaliedadeSelecionada("2");
			} else if(ttd.getArquivoIntermediario()){
				ttd.setTemporaliedadeSelecionada("3");
			} else {
				ttd.setTemporaliedadeSelecionada("1");
			}

			this.preencherCombosAlterar();
			
			return "formularioAlterarTTD";
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return "formularioAlterarEmpresa";
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util
			.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return findAll();
	}

	public String remover() {
		try {
			Long id = Long.valueOf(Util.getParameter("_id"));
			ttd.setId(id);
			ttd = ttdBO.findById(id);
			ttdBO.remove(ttd);

			addMessage(FacesMessage.SEVERITY_INFO, "geral.remocao.sucesso",
					ArcheionBean.PERSIST_SUCESS);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, 
					e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util
			.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return findAll();
	}

	public String findAll() {
		try {
			visualizar=false;
			this.ttd = new TTD();

			listaTTD = ttdBO.findAll();

			for(TTD t: listaTTD){
				if(t.getArquivoPermanente()){
					t.setTemporaliedadeSelecionada("Permanente");
				} else if(t.getArquivoIntermediario()){
					t.setTemporaliedadeSelecionada("Intermediário");
				} else {
					t.setTemporaliedadeSelecionada("Nenhum");
				}
			}
			this.preencherCombosListar();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 
		return "listaTTD";
	}

	public String findByEmpresaLocalItemDocumental() {
		try {			
			listaTTD = ttdBO.findByEmpresaLocalItemDocumental(ttd.getLocal().getEmpresa().getId().intValue(),
					ttd.getLocal().getId().intValue(), ttd.getItemDocumental().getId().intValue());

			for(TTD t: listaTTD){
				if(t.getArquivoPermanente()){
					t.setTemporaliedadeSelecionada("Permanente");
				} else {
					if(t.getArquivoIntermediario()){
						t.setTemporaliedadeSelecionada("Intermediário");
					} else {
						t.setTemporaliedadeSelecionada("Nenhum");
					}
				}
			}
			this.preencherCombosListar();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 
		return "listaTTD";
	}

	public String goToForm() {
		try {
			this.preencherCombosAlterar();
			ttd = new TTD();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 
		return "formularioTTD";
	}

	public void valueChanged(ValueChangeEvent event) {
		Long empId = (Long)event.getNewValue();
		Empresa empresa = new Empresa();
		empresa.setId(empId);
		
		ttd.getLocal().setEmpresa(empresa);
		
		preencherCombosAlterar();
	}
	
	private void preencherCombosListar() {
		listaEmpresa = new ArrayList<SelectItem>();
		listaLocal = new ArrayList<SelectItem>();
		listaItemDocumental = new ArrayList<SelectItem>();

		List<Empresa> empresas = empresaBO.findAll();
		for(Empresa e: empresas){
			listaEmpresa.add(new SelectItem(e.getId(),e.getNome()));
		}		

		if ( (ttd.getLocal().getEmpresa() == null || 
				ttd.getLocal().getEmpresa().getId() == null || 
				ttd.getLocal().getEmpresa().getId() == 0 )&& empresas.size()>0 ) {
			ttd.getLocal().setEmpresa(empresas.get(0));
		}

		List<Local> locais = localBO.findByEmpresa(ttd.getLocal().getEmpresa());
		for(Local l: locais){
			listaLocal.add(new SelectItem(l.getId(),l.getNome()));
		}

		List<ItemDocumental> itens = itemDocumentalBO.findAll();
		for(ItemDocumental i: itens){
			listaItemDocumental.add(new SelectItem(i.getId(),i.getNome()));
		}

	}

	private void preencherCombosAlterar() {
		listaEmpresa = new ArrayList<SelectItem>();
		listaLocal = new ArrayList<SelectItem>();
		listaItemDocumental = new ArrayList<SelectItem>();
		listaEventoContagem = new ArrayList<SelectItem>();

		List<Empresa> empresas = empresaBO.findAll();
		for(Empresa e: empresas){
			listaEmpresa.add(new SelectItem(e.getId(),e.getNome()));
		}
		
		
		List<Local> locais = localBO.findByEmpresa(ttd.getLocal().getEmpresa());
		for(Local l: locais){
			listaLocal.add(new SelectItem(l.getId(),l.getNome()));
		}

		List<ItemDocumental> itens = itemDocumentalBO.findAll();
		for(ItemDocumental i: itens){
			listaItemDocumental.add(new SelectItem(i.getId(),i.getNome()));
		}

		List<EventoContagem> eventos = eventoContagemBO.findAll();
		for(EventoContagem e: eventos){
			listaEventoContagem.add(new SelectItem(e.getId(),e.getNome()));
		}
	}

	public String imprimir() {
		FacesContext context = getContext();
		try {
			HttpServletResponse response = (HttpServletResponse) context
			.getExternalContext().getResponse();

			ServletOutputStream responseStream;
			responseStream = response.getOutputStream();
			String pathJasper = ((ServletContext) context.getExternalContext()
					.getContext()).getRealPath("/WEB-INF/relatorios/")
					+ "/ArcheionImprimirTTD.jasper";
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			ParametrosReport ids = new ParametrosReport();
			for(TTD p: listaTTD) {
				ids.add(p.getId());
			}
			param.put("idsTTD", ids.toString());
			
			Relatorio relatorio = ttdBO.getRelatorio(param, pathJasper);
			relatorio.exportarParaPdfStream(responseStream);

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
			"filename=\"RelacaoEmpresa.pdf\"");
			responseStream.flush();
			responseStream.close();
			context.renderResponse();
			context.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 

		return findAll();
	}
	
	public String imprimirTxt() {
		FacesContext context = getContext();
		try {
			HttpServletResponse response = (HttpServletResponse) context
			.getExternalContext().getResponse();
			
			ServletOutputStream responseStream;
			responseStream = response.getOutputStream();
			
			
			ParametrosReport ids = new ParametrosReport();
			for(TTD p: listaTTD) {
				ids.add(p.getId());
			}
			
			StringBuilder sb = new StringBuilder("select c.nm_empresa as empresa, b.nm_local as local, d.nm_item_documental as item_documental, ");
			sb.append("e.nm_evento_contagem as evento_contagem, "); 
			sb.append("a.nm_tempo_arq_corrente as arquivo_corrente, a.nm_tempo_arq_intermediario as arquivo_intermediario, ");
			sb.append("(case when a.cs_microfilmagem = 1 then 'Sim' else 'Nao' end) as microfilmagem, ");
			sb.append("(case when a.cs_digitalizacao = 1 then 'Sim' else 'Nao' end) as digitalizacao, ");
			sb.append("a.tx_observacao as observacao ");
			sb.append("from tb_ttd a join tb_local b on (a.id_local = b.id_local) ");
			sb.append("join tb_empresa c on (b.id_empresa = c.id_empresa) ");
			sb.append("join tb_item_documental d on (a.id_item_documental = d.id_item_documental) ");
			sb.append("join tb_evento_contagem e on (a.id_evento_contagem = e.id_evento_contagem) ");
			sb.append("where a.id_ttd in (");
			sb.append(ids.toString());
			sb.append(") ");
			sb.append("order by 1,2 ");
						
			relatorioTxtBO.geraRelatorioTxt(sb.toString(), responseStream);
			
			response.setContentType("application/txt");
			response.setHeader("Content-disposition",
			"filename=\"relatorio.txt\"");
			responseStream.flush();
			responseStream.close();
			context.renderResponse();
			context.responseComplete();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		}
		return findAll();
	}

	public TTD getTtd() {
		return ttd;
	}

	public void setTtd(TTD ttd) {
		this.ttd = ttd;
	}

	public List<TTD> getListaTTD() {
		return listaTTD;
	}

	public void setListaTTD(List<TTD> listaTTD) {
		this.listaTTD = listaTTD;
	}

	public TTDBO getTtdBO() {
		return ttdBO;
	}

	public void setTtdBO(TTDBO ttdBO) {
		this.ttdBO = ttdBO;
	}

	public LocalBO getLocalBO() {
		return localBO;
	}

	public void setLocalBO(LocalBO localBO) {
		this.localBO = localBO;
	}

	public ItemDocumentalBO getItemDocumentalBO() {
		return itemDocumentalBO;
	}

	public void setItemDocumentalBO(ItemDocumentalBO itemDocumentalBO) {
		this.itemDocumentalBO = itemDocumentalBO;
	}

	public EventoContagemBO getEventoContagemBO() {
		return eventoContagemBO;
	}

	public void setEventoContagemBO(EventoContagemBO eventoContagemBO) {
		this.eventoContagemBO = eventoContagemBO;
	}

	public List<SelectItem> getListaLocal() {
		return listaLocal;
	}

	public void setListaLocal(List<SelectItem> listaLocal) {
		this.listaLocal = listaLocal;
	}

	public List<SelectItem> getListaItemDocumental() {
		return listaItemDocumental;
	}

	public void setListaItemDocumental(List<SelectItem> listaItemDocumental) {
		this.listaItemDocumental = listaItemDocumental;
	}

	public List<SelectItem> getListaEventoContagem() {
		return listaEventoContagem;
	}

	public void setListaEventoContagem(List<SelectItem> listaEventoContagem) {
		this.listaEventoContagem = listaEventoContagem;
	}

	public List<SelectItem> getListaEmpresa() {
		return listaEmpresa;
	}

	public void setListaEmpresa(List<SelectItem> listaEmpresa) {
		this.listaEmpresa = listaEmpresa;
	}

	public EmpresaBO getEmpresaBO() {
		return empresaBO;
	}

	public void setEmpresaBO(EmpresaBO empresaBO) {
		this.empresaBO = empresaBO;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isVisualizar() {
		return visualizar;
	}

	public void setVisualizar(boolean visualizar) {
		this.visualizar = visualizar;
	}

}

class ParametrosReport extends ArrayList<Object> {
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		String result = "";
		for(int i=0;i<this.size()-1;i++){
			result += this.get(i)+",";
		}
		return result+this.get(this.size()-1);
	}
}