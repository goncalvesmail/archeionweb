package br.com.archeion.mbean.itemdocumental;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
import br.com.archeion.modelo.itemdocumental.ItemDocumental;
import br.com.archeion.negocio.itemdocumental.ItemDocumentalBO;
import br.com.archeion.negocio.relatoriotxt.RelatorioTxtBO;

public class ItemDocumentalMBean extends ArcheionBean {

	private ItemDocumental itemDocumental;
	private List<ItemDocumental> listaDocumental;
	private RelatorioTxtBO relatorioTxtBO = (RelatorioTxtBO) Util.getSpringBean("relatorioTxtBO");
	private ItemDocumentalBO itemDocumentalBO = (ItemDocumentalBO) Util.getSpringBean("itemDocumentalBO");
	
	public ItemDocumentalMBean() {
		itemDocumental = new ItemDocumental();
	}
	
	public String incluir() {
		try {
			this.incluirMBean();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return goToForm();
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}			
		return findAll();
	}
	
	public String incluirMais() {
		try {
			this.incluirMBean();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return goToForm();
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}			
		return goToForm();
	}
	
	public void incluirMBean() throws AccessDeniedException, CadastroDuplicadoException {
		itemDocumental.setId(null);
		itemDocumentalBO.persist(itemDocumental);
		
		addMessage(FacesMessage.SEVERITY_INFO,"geral.inclusao.sucesso",ArcheionBean.PERSIST_SUCESS);
	}

	public String goToAlterar() {
		try {
			Long id = Long.valueOf(Util.getParameter("_id"));			
			itemDocumental = itemDocumentalBO.findById(id);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return "formularioAlterarItemDocumental";
	}	
	
	public String alterar() {
		try {			
			itemDocumental = itemDocumentalBO.merge(itemDocumental);
			
			addMessage(FacesMessage.SEVERITY_INFO,"geral.alteracao.sucesso",ArcheionBean.PERSIST_SUCESS);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return "formularioAlterarItemDocumental";
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return findAll();
	}	
	
	public String remover() {
		try {
			Long id = Long.valueOf(Util.getParameter("_id"));
			itemDocumental.setId( id );
			itemDocumental = itemDocumentalBO.findById(id);
			itemDocumentalBO.remove(itemDocumental);

			addMessage(FacesMessage.SEVERITY_INFO,"geral.remocao.sucesso",ArcheionBean.PERSIST_SUCESS);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, 
					e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return findAll();
	}	
	
	public String imprimir() {
		FacesContext context = getContext();
		try {
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
			
			ServletOutputStream responseStream;
			responseStream = response.getOutputStream();
			String pathJasper = ((ServletContext)context.getExternalContext().getContext()).getRealPath("/WEB-INF/relatorios/")+ "/ArcheionItensDocumentais.jasper";
			Relatorio relatorio = itemDocumentalBO.getRelatorio(new HashMap<String, Object>(), pathJasper);
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
			StringBuilder sb = new StringBuilder("select a.nm_item_documental as item_documental ");
			sb.append("from tb_item_documental a ");
			sb.append("order by 1 ");
						
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
	
	public String findAll() {
		try {
			listaDocumental = itemDocumentalBO.findAll();
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 
		return "listaItemDocumental";
	}
	
	public String goToForm() {
		itemDocumental = new ItemDocumental();
		return "formularioItemDocumental";
	}

	public ItemDocumental getItemDocumental() {
		return itemDocumental;
	}

	public void setItemDocumental(ItemDocumental itemDocumental) {
		this.itemDocumental = itemDocumental;
	}

	public List<ItemDocumental> getListaDocumental() {
		return listaDocumental;
	}

	public void setListaDocumental(List<ItemDocumental> listaDocumental) {
		this.listaDocumental = listaDocumental;
	}

	public void setItemDocumentalBO(ItemDocumentalBO itemDocumentalBO) {
		this.itemDocumentalBO = itemDocumentalBO;
	}
	
}
