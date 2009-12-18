package br.com.archeion.mbean.usuario;

import java.io.IOException;
import java.util.ArrayList;
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
import br.com.archeion.exception.ArcheionException;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.jsf.Constants;
import br.com.archeion.jsf.Util;
import br.com.archeion.mbean.ArcheionBean;
import br.com.archeion.mbean.AuthenticationController;
import br.com.archeion.mbean.ExceptionManagedBean;
import br.com.archeion.modelo.grupo.Grupo;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.negocio.grupo.GrupoBO;
import br.com.archeion.negocio.relatoriotxt.RelatorioTxtBO;
import br.com.archeion.negocio.usuario.UsuarioBO;

public class UsuarioMBean extends ArcheionBean {

	private Usuario usuario;
	private List<Usuario> listaUsuarios;

	private UsuarioBO usuarioBO = (UsuarioBO) Util.getSpringBean("usuarioBO");
	private GrupoBO grupoBO = (GrupoBO) Util.getSpringBean("grupoBO");
	private RelatorioTxtBO relatorioTxtBO = (RelatorioTxtBO) Util.getSpringBean("relatorioTxtBO");
	
	private List<Grupo> grupoSource;
	private List<Grupo> grupoTarget;

	public List<Grupo> getFuncSource() {
		return grupoSource;
	}

	public void setFuncSource(List<Grupo> funcSource) {
		this.grupoSource = funcSource;
	}

	public List<Grupo> getFuncTarget() {
		return grupoTarget;
	}

	public void setFuncTarget(List<Grupo> funcTarget) {
		this.grupoTarget = funcTarget;
	}

	public UsuarioMBean() {
		usuario = new Usuario();
		grupoSource = new ArrayList<Grupo>();
		grupoTarget = new ArrayList<Grupo>();
	}

	public String incluir() {
		try {
			incluirMBean();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return goToForm();
		} catch (ArcheionException e){
			addMessage(FacesMessage.SEVERITY_INFO, "grupo.lista.nao.selecionado"
					,ArcheionBean.PERSIST_FAILURE);
			return this.goToForm();
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
		} catch (ArcheionException e){
			addMessage(FacesMessage.SEVERITY_INFO, "grupo.lista.nao.selecionado"
					,ArcheionBean.PERSIST_FAILURE);
			return this.goToForm();
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util
			.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		} 
		
		return this.goToForm();
	}

	public void incluirMBean() throws AccessDeniedException, ArcheionException {
		if(grupoTarget != null && !(grupoTarget.size() > 0)){
			throw new ArcheionException();
		} else {
			usuario.setId(null);
			usuario.setGrupos(grupoTarget);
			usuarioBO.persist(usuario);
	
			addMessage(FacesMessage.SEVERITY_INFO, "geral.inclusao.sucesso",
					ArcheionBean.PERSIST_SUCESS);

		}
	}

	public String goToAlterar() {
		try {
			Long id = Long.valueOf(Util.getParameter("_id"));
			usuario = usuarioBO.findById(id);

			grupoTarget = usuario.getGrupos();
			grupoSource = grupoBO.findAll();
			grupoSource.removeAll(grupoTarget);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util
			.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return "formularioAlterarUsuario";
	}
	
	public String goToAlterarSenha(){
		try {
			usuario = (Usuario)((AuthenticationController) Util.getManagedBean("authenticationController")).getUsuario().clone();
			
			usuario = usuarioBO.findById(usuario.getId());
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		};
		usuario.setSenha("");
		
		return "formularioAlterarUsuarioSenha";
	}
	
	public String alterar() {
		try {
			usuario.setGrupos(grupoTarget);
			usuario = usuarioBO.merge(usuario);

			addMessage(FacesMessage.SEVERITY_INFO, "geral.alteracao.sucesso",
					ArcheionBean.PERSIST_SUCESS);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return "formularioAlterarUsuario";
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util
			.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return findAll();
	}
	
	public String alterarSenha() {
		try {
			//usuario.setGrupos(grupoTarget);
			usuario = usuarioBO.mergeSenha(usuario);
			
			addMessage(FacesMessage.SEVERITY_INFO, "geral.alteracao.sucesso",
					ArcheionBean.PERSIST_SUCESS);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return "formularioAlterarUsuario";
		} catch (BusinessException e){
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.alterar.senha",ArcheionBean.PERSIST_FAILURE);
			return "formularioAlterarUsuarioSenha";
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util
			.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return "goToIndex";
	}

	public String remover() {
		try {
			Long id = Long.valueOf(Util.getParameter("_id"));
			usuario.setId(id);
			usuario = usuarioBO.findById(id);
			usuarioBO.remove(usuario);

			addMessage(FacesMessage.SEVERITY_INFO, "geral.remocao.sucesso",
					ArcheionBean.PERSIST_SUCESS);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (Exception e) {
			/*ExceptionManagedBean excBean = (ExceptionManagedBean) Util
			.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);*/
			addMessage(FacesMessage.SEVERITY_INFO, "usuario.nao.pode.ser.removido",ArcheionBean.PERSIST_FAILURE);
			//return Constants.ERROR_HANDLER;
		}
		return findAll();
	}

	public String findAll() {
		try {
			listaUsuarios = usuarioBO.findAll();
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		}
		return "listaUsuario";
	}

	public String goToForm() {
		try {
			usuario = new Usuario();

			grupoTarget = new ArrayList<Grupo>();
			grupoSource = grupoBO.findAll();
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		}
		return "formularioUsuario";
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
					+ "/ArcheionUsuarios.jasper";
			Relatorio relatorio = usuarioBO.getRelatorio(
					new HashMap<String, Object>(), pathJasper);
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
			StringBuilder sb = new StringBuilder("select a.nm_usuario as nome, a.nm_login as login, ");
			sb.append("(case when a.cs_situacao_usuario = 1 then 'Ativo' when a.cs_situacao_usuario = 0 then 'Desativado' end) as situacao, ");
			sb.append("(case when a.cs_pode_alugar = 1 then 'Pode Alugar' when a.cs_pode_alugar = 0 then 'Nao Pode Alugar' end) as pode_alugar ");
			sb.append("from tb_usuarios a ");
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

	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}
	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public List<Grupo> getGrupoSource() {
		return grupoSource;
	}

	public void setGrupoSource(List<Grupo> grupoSource) {
		this.grupoSource = grupoSource;
	}

	public List<Grupo> getGrupoTarget() {
		return grupoTarget;
	}

	public void setGrupoTarget(List<Grupo> grupoTarget) {
		this.grupoTarget = grupoTarget;
	}


}
