package br.com.archeion.mbean.enderecocaixa;

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
import br.com.archeion.modelo.enderecocaixa.EnderecoCaixa;
import br.com.archeion.negocio.enderecocaixa.EnderecoCaixaBO;
import br.com.archeion.negocio.relatoriotxt.RelatorioTxtBO;

public class EnderecoCaixaMBean extends ArcheionBean {

	private EnderecoCaixa enderecoCaixa;
	private List<EnderecoCaixa> listaEnderecoCaixa;

	private EnderecoCaixaBO enderecoCaixaBO = (EnderecoCaixaBO) Util.getSpringBean("enderecoCaixaBO");
	private RelatorioTxtBO relatorioTxtBO = (RelatorioTxtBO) Util.getSpringBean("relatorioTxtBO");
	
	public EnderecoCaixaMBean() {
		enderecoCaixa = new EnderecoCaixa();
	}

	public String incluir() {
		try {
			incluirMBean();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return goToForm();
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
			return goToForm();
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
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return goToForm();
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
			return goToForm();
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
		
		enderecoCaixa.setId(null);
		enderecoCaixaBO.persist(enderecoCaixa);

		addMessage(FacesMessage.SEVERITY_INFO, "geral.inclusao.sucesso",ArcheionBean.PERSIST_SUCESS);

	}

	public String goToAlterar() {
		try {
			Long id = Long.valueOf(Util.getParameter("_id"));
			enderecoCaixa = enderecoCaixaBO.findById(id);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util
			.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return "formularioAlterarEnderecoCaixa";
	}

	public String alterar() {
		try {
			enderecoCaixa = enderecoCaixaBO.merge(enderecoCaixa);

			addMessage(FacesMessage.SEVERITY_INFO, "geral.alteracao.sucesso",
					ArcheionBean.PERSIST_SUCESS);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
			return goToForm();
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return "formularioAlterarEnderecoCaixa";
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
			enderecoCaixa.setId(id);
			enderecoCaixa = enderecoCaixaBO.findById(id);
			enderecoCaixaBO.remove(enderecoCaixa);

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
			listaEnderecoCaixa = enderecoCaixaBO.findAll();
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 
		return "listaEnderecoCaixa";
	}

	public String goToForm() {
		try {
			enderecoCaixa = new EnderecoCaixa();
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 
		return "formularioEnderecoCaixa";
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
					+ "/ArcheionEnderecoCaixa.jasper";
			Relatorio relatorio = enderecoCaixaBO.getRelatorio(
					new HashMap<String, Object>(), pathJasper);
			relatorio.exportarParaPdfStream(responseStream);

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
			"filename=\"RelacaoEnderecoCaixa.pdf\"");
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
			StringBuilder sb = new StringBuilder("select b.vao_endereco_caixa as vao, b.nu_inicial as inicial, b.nu_final as final ");
			sb.append("from tb_endereco_caixa b ");
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

	public EnderecoCaixa getEnderecoCaixa() {
		return enderecoCaixa;
	}

	public void setEnderecoCaixa(EnderecoCaixa enderecoCaixa) {
		this.enderecoCaixa = enderecoCaixa;
	}

	public List<EnderecoCaixa> getListaEnderecoCaixa() {
		return listaEnderecoCaixa;
	}

	public void setListaEnderecoCaixa(List<EnderecoCaixa> listaEnderecoCaixa) {
		this.listaEnderecoCaixa = listaEnderecoCaixa;
	}

	public void setEnderecoCaixaBO(EnderecoCaixaBO enderecoCaixaBO) {
		this.enderecoCaixaBO = enderecoCaixaBO;
	}

}
