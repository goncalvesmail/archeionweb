package br.com.archeion.mbean.grupo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;

import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.jsf.Constants;
import br.com.archeion.jsf.Util;
import br.com.archeion.mbean.ArcheionBean;
import br.com.archeion.mbean.ExceptionManagedBean;
import br.com.archeion.modelo.funcionalidade.Funcionalidade;
import br.com.archeion.modelo.grupo.Grupo;
import br.com.archeion.negocio.funcionalidade.FuncionalidadeBO;
import br.com.archeion.negocio.grupo.GrupoBO;
import br.com.archeion.negocio.relatoriotxt.RelatorioTxtBO;

public class GrupoMBean extends ArcheionBean {

	private Grupo grupo;
	private List<Grupo> listaGrupo;

	private List<Funcionalidade> funcSource;
	private List<Funcionalidade> funcTarget;

	private GrupoBO grupoBO = (GrupoBO) Util.getSpringBean("grupoBO");
	private FuncionalidadeBO funcionalidadeBO = (FuncionalidadeBO) Util.getSpringBean("funcionalidadeBO");
	private RelatorioTxtBO relatorioTxtBO = (RelatorioTxtBO) Util.getSpringBean("relatorioTxtBO");

	public GrupoMBean() {
		grupo = new Grupo();
		funcSource = new ArrayList<Funcionalidade>();
		funcTarget = new ArrayList<Funcionalidade>();
	}
	
	public String imprimirTxt() {
		FacesContext context = getContext();
		try {
			HttpServletResponse response = (HttpServletResponse) context
			.getExternalContext().getResponse();
			
			ServletOutputStream responseStream;
			responseStream = response.getOutputStream();
			StringBuilder sb = new StringBuilder("select a.nm_grupo as grupo ");
			sb.append("from tb_grupos a ");
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
	
	public String incluir() {
		try {
			incluirMBean();	
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
			incluirMBean();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return goToForm();
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util
					.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return this.goToForm();
	}
	
	public void incluirMBean() throws AccessDeniedException, CadastroDuplicadoException {
		grupo.setId(null);
		grupo.setFuncionalidades(funcTarget);
		grupoBO.persist(grupo);

		addMessage(FacesMessage.SEVERITY_INFO, "geral.inclusao.sucesso",
				ArcheionBean.PERSIST_SUCESS);

	}


	public String goToAlterar() {
		try {
			Long id = Long.valueOf(Util.getParameter("_id"));			
			grupo = grupoBO.findById(id);

			funcTarget = grupo.getFuncionalidades();
			funcSource = funcionalidadeBO.findAll();
			funcSource.removeAll(funcTarget);

		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return "formularioAlterarGrupo";
	}	

	public String alterar() {
		try {			
			grupo.setFuncionalidades(funcTarget);
			grupo = grupoBO.merge(grupo);

			addMessage(FacesMessage.SEVERITY_INFO,"geral.alteracao.sucesso",ArcheionBean.PERSIST_SUCESS);			
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return "formularioAlterarGrupo";
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
			grupo.setId( id );
			grupo = grupoBO.findById(id);
			grupoBO.remove(grupo);

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

	public String findAll() {
		try {
			listaGrupo = grupoBO.findAll();
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 
		return "listaGrupo";
	}

	public String goToForm() {
		try {
			grupo = new Grupo();

			funcSource = funcionalidadeBO.findAll();
			funcTarget = new ArrayList<Funcionalidade>();
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 
		return "formularioGrupo";
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public List<Grupo> getListaGrupo() {
		return listaGrupo;
	}

	public void setListaGrupo(List<Grupo> listaGrupo) {
		this.listaGrupo = listaGrupo;
	}

	public List<Funcionalidade> getFuncSource() {
		return funcSource;
	}

	public void setFuncSource(List<Funcionalidade> funcSource) {
		this.funcSource = funcSource;
	}

	public List<Funcionalidade> getFuncTarget() {
		return funcTarget;
	}

	public void setFuncTarget(List<Funcionalidade> funcTarget) {
		this.funcTarget = funcTarget;
	}

}
