package br.com.archeion.mbean.caixa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.modelo.caixa.EmprestimoCaixa;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.negocio.caixa.CaixaBO;
import br.com.archeion.negocio.caixa.EmprestimoCaixaBO;
import br.com.archeion.negocio.relatoriotxt.RelatorioTxtBO;
import br.com.archeion.negocio.usuario.UsuarioBO;

public class EmprestimoCaixaMBean extends ArcheionBean {

	private EmprestimoCaixa emprestimo;
	private List<SelectItem> listaCaixa;
	private List<SelectItem> listaResponsavel;
	private List<SelectItem> listaSolicitante;

	private List<EmprestimoCaixa> listaEmprestimoCaixa;
	
	private Caixa caixaFiltro;
	private Date dataFiltro;
	private Usuario usuarioFiltro;
	private String situacaoFiltro;
	private boolean emprestada = true;
	
	private CaixaBO caixaBO = (CaixaBO) Util.getSpringBean("caixaBO");
	private UsuarioBO usuarioBO = (UsuarioBO) Util.getSpringBean("usuarioBO");
	private EmprestimoCaixaBO emprestimoCaixaBO = (EmprestimoCaixaBO) Util.getSpringBean("emprestimoCaixaBO");
	private RelatorioTxtBO relatorioTxtBO = (RelatorioTxtBO) Util.getSpringBean("relatorioTxtBO");

	public EmprestimoCaixaMBean() {
		emprestimo = new EmprestimoCaixa();
	}

	private void preencherCombos() {
		listaCaixa = new ArrayList<SelectItem>();
		listaResponsavel = new ArrayList<SelectItem>();
		listaSolicitante = new ArrayList<SelectItem>();
		
		List<Caixa> caixas = caixaBO.findCaixaAtivasEmprestimo();
		for( Caixa c:caixas ) {
			listaCaixa.add(new SelectItem(c.getId(),c.getTitulo()));
		}
		
		List<Usuario> responsaveis = usuarioBO.findAllAluga();
		for( Usuario u:responsaveis ) {
			listaResponsavel.add(new SelectItem(u.getId(),u.getNome()));
		}

		List<Usuario> solicitantes  = usuarioBO.findAll();
		for( Usuario u:solicitantes ) {
			listaSolicitante.add(new SelectItem(u.getId(),u.getNome()));
		}
		
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
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
			return goToForm();
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}			
		return goToForm();
	}

	public void incluirMBean() throws AccessDeniedException, CadastroDuplicadoException, BusinessException {
		emprestimo.setId(null);
		
		Caixa caixa = caixaBO.findById(emprestimo.getCaixa().getId());
		emprestimo.setCaixa(caixa);
		
		
		if ( emprestimo.getSolicitante().getId()==-1 ) {
			emprestimo.setSolicitante(null);
		}
		else {
			emprestimo.setSolicitanteExterno(null);
			Usuario solicitante = usuarioBO.findById(emprestimo.getSolicitante().getId());
			emprestimo.setSolicitante(solicitante);
		}
		
		emprestimoCaixaBO.persist(emprestimo);
		addMessage(FacesMessage.SEVERITY_INFO,"geral.inclusao.sucesso",ArcheionBean.PERSIST_SUCESS);
	}

	public String goToAlterar() {
		try {
			Long id = Long.valueOf(Util.getParameter("_id"));			
			emprestimo = emprestimoCaixaBO.findById(id);

			preencherCombos();
			
			if ( emprestimo.getSolicitante()==null || 
					emprestimo.getSolicitante().getId()==null ) {
				Usuario solicitante = new Usuario();
				solicitante.setId(-1l);
				emprestimo.setSolicitante(solicitante);
			}
			
			Caixa c = emprestimo.getCaixa();
			listaCaixa.add(new SelectItem(c.getId(),c.getTitulo()));

		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return "formularioAlterarEmprestimoCaixa";
	}	

	public String alterar() {
		try {	

			Caixa caixa = caixaBO.findById(emprestimo.getCaixa().getId());
			emprestimo.setCaixa(caixa);
			
			
			if ( emprestimo.getSolicitante().getId()==-1 ) {
				emprestimo.setSolicitante(null);
			}
			else {
				emprestimo.setSolicitanteExterno(null);
				Usuario solicitante = usuarioBO.findById(emprestimo.getSolicitante().getId());
				emprestimo.setSolicitante(solicitante);
			}
			
			emprestimo = emprestimoCaixaBO.merge(emprestimo);

			addMessage(FacesMessage.SEVERITY_INFO,"geral.alteracao.sucesso",ArcheionBean.PERSIST_SUCESS);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return "formularioAlterarEmprestimoCaixa";
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
			return "formularioAlterarEmprestimoCaixa";
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return findAll();
	}	
	
	public String goToDevolver(){
		Long id = Long.valueOf(Util.getParameter("_id"));
		emprestimo.setId( id );
		emprestimo = emprestimoCaixaBO.findById(id);
		
		emprestimo.setDataDevolucao(emprestimo.getPrevisaoDevolucao());
		return "formularioDevolverCaixa";
	}
	
	public String devolver() {
		try {			
			emprestimoCaixaBO.merge(emprestimo);
			
			addMessage(FacesMessage.SEVERITY_INFO, "emprestimo.caixa.msg.devolucao",ArcheionBean.PERSIST_SUCESS);
			
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
		}
		
		return findAll();
	}

	public String remover() {
		try {
			Long id = Long.valueOf(Util.getParameter("_id"));
			emprestimo.setId( id );
			emprestimo = emprestimoCaixaBO.findById(id);
			emprestimoCaixaBO.remove(emprestimo);

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

			List<Caixa> caixas = caixaBO.findAll();
			listaCaixa = new ArrayList<SelectItem>();
			for( Caixa c:caixas ) {
				listaCaixa.add(new SelectItem(c.getId(),c.getTitulo()));
			}
			
			List<Usuario> solicitantes  = usuarioBO.findAll();
			listaSolicitante = new ArrayList<SelectItem>();
			for( Usuario u:solicitantes ) {
				listaSolicitante.add(new SelectItem(u.getId(),u.getNome()));
			}
			
			caixaFiltro = new Caixa();
			usuarioFiltro = new Usuario();
			dataFiltro = null;
			emprestada = true;
			situacaoFiltro = "1";

			listaEmprestimoCaixa = emprestimoCaixaBO.findEmprestadas(null,null,null);
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		}
		return "listaEmprestimoCaixa";
	}

	public String pesquisar() {
		try {
			
			List<Caixa> caixas = caixaBO.findAll();
			listaCaixa = new ArrayList<SelectItem>();
			for( Caixa c:caixas ) {
				listaCaixa.add(new SelectItem(c.getId(),c.getTitulo()));
			}
			
			List<Usuario> solicitantes  = usuarioBO.findAll();
			listaSolicitante = new ArrayList<SelectItem>();
			for( Usuario u:solicitantes ) {
				listaSolicitante.add(new SelectItem(u.getId(),u.getNome()));
			}


			if ( situacaoFiltro.equals("1") ) {
				listaEmprestimoCaixa = emprestimoCaixaBO
					.findEmprestadas(caixaFiltro, usuarioFiltro, dataFiltro);
				emprestada=true;
			}
			else {
				listaEmprestimoCaixa = emprestimoCaixaBO
					.findDevolvidas(caixaFiltro, usuarioFiltro, dataFiltro);
				emprestada=false;
			}
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		}
		return "listaEmprestimoCaixa";
	}
	
	public String goToForm() {

		emprestimo = new EmprestimoCaixa();
		preencherCombos();
		
		return "formularioEmprestimoCaixa";
	}
	
	public String imprimirTxt() {
		FacesContext context = getContext();
		try {
			HttpServletResponse response = (HttpServletResponse) context
			.getExternalContext().getResponse();
			
			ServletOutputStream responseStream;
			responseStream = response.getOutputStream();
			StringBuilder sb = new StringBuilder("select (e.vao_endereco_caixa || b.nu_vao_endereco_caixa) as caixa, a.dt_emprestimo as data_emprestimo, a.dt_devolucao as data_devolucao, "); 
			sb.append("a.tx_solicitante_externo as solicitante_externo, "); 
			sb.append("c.nm_usuario as solicitante_interno, d.nm_usuario as responsavel_emprestimo "); 
			sb.append("from tb_emprestimo_caixa a join tb_caixa b on (a.id_caixa = b.id_caixa) "); 
			sb.append("join tb_endereco_caixa e on (b.id_endereco_caixa = e.id_endereco_caixa) ");
			sb.append("join tb_usuarios d on (a.id_usuario_responsavel  = d.id_usuario) "); 
			sb.append("left join tb_usuarios c on (a.id_usuario_solicitante = c.id_usuario) ");
			sb.append("order by 2,1 ");
						
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
	
	public String imprimirProtocoloEmprestimo() {
		FacesContext context = getContext();
		try {
			
			Long id = Long.valueOf(Util.getParameter("_id"));			
			emprestimo = emprestimoCaixaBO.findById(id);
			
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
			
			ServletOutputStream responseStream;
			responseStream = response.getOutputStream();
			String pathJasper = ((ServletContext)context.getExternalContext().getContext()).getRealPath("/WEB-INF/relatorios/")+ 
			"/ProtocoloEmprestimoCaixa.jasper";
			HashMap<String, Object> param = new HashMap<String, Object>();
			
			param.put("idEmprestimoCaixa", emprestimo.getId());
			Relatorio relatorio = caixaBO.getRelatorio(param, pathJasper);
			relatorio.exportarParaPdfStream(responseStream);
			
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"filename=\"ProtocoloEmprestimoCaixa.pdf\"");
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
	
	public String imprimirProtocoloDevolucao() {
		FacesContext context = getContext();
		try {
			
			Long id = Long.valueOf(Util.getParameter("_id"));			
			emprestimo = emprestimoCaixaBO.findById(id);
			
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
			
			ServletOutputStream responseStream;
			responseStream = response.getOutputStream();
			String pathJasper = ((ServletContext)context.getExternalContext().getContext()).getRealPath("/WEB-INF/relatorios/")+ 
			"/ProtocoloDevolucaoCaixa.jasper";
			HashMap<String, Object> param = new HashMap<String, Object>();
			
			param.put("idEmprestimoCaixa", emprestimo.getId());
			Relatorio relatorio = caixaBO.getRelatorio(param, pathJasper);
			relatorio.exportarParaPdfStream(responseStream);
			
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"filename=\"ProtocoloDevolucaoCaixa.pdf\"");
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

	public EmprestimoCaixa getEmprestimo() {
		return emprestimo;
	}

	public void setEmprestimo(EmprestimoCaixa emprestimo) {
		this.emprestimo = emprestimo;
	}

	public List<SelectItem> getListaCaixa() {
		return listaCaixa;
	}

	public void setListaCaixa(List<SelectItem> listaCaixa) {
		this.listaCaixa = listaCaixa;
	}

	public CaixaBO getCaixaBO() {
		return caixaBO;
	}

	public void setCaixaBO(CaixaBO caixaBO) {
		this.caixaBO = caixaBO;
	}

	public EmprestimoCaixaBO getEmprestimoCaixaBO() {
		return emprestimoCaixaBO;
	}

	public void setEmprestimoCaixaBO(EmprestimoCaixaBO emprestimoCaixaBO) {
		this.emprestimoCaixaBO = emprestimoCaixaBO;
	}

	public List<SelectItem> getListaResponsavel() {
		return listaResponsavel;
	}

	public void setListaResponsavel(List<SelectItem> listaResponsavel) {
		this.listaResponsavel = listaResponsavel;
	}

	public List<SelectItem> getListaSolicitante() {
		return listaSolicitante;
	}

	public void setListaSolicitante(List<SelectItem> listaSolicitante) {
		this.listaSolicitante = listaSolicitante;
	}

	public List<EmprestimoCaixa> getListaEmprestimoCaixa() {
		return listaEmprestimoCaixa;
	}

	public void setListaEmprestimoCaixa(List<EmprestimoCaixa> listaEmprestimoCaixa) {
		this.listaEmprestimoCaixa = listaEmprestimoCaixa;
	}

	public Caixa getCaixaFiltro() {
		return caixaFiltro;
	}

	public void setCaixaFiltro(Caixa caixaFiltro) {
		this.caixaFiltro = caixaFiltro;
	}

	public Date getDataFiltro() {
		return dataFiltro;
	}

	public void setDataFiltro(Date dataFiltro) {
		this.dataFiltro = dataFiltro;
	}

	public Usuario getUsuarioFiltro() {
		return usuarioFiltro;
	}

	public void setUsuarioFiltro(Usuario usuarioFiltro) {
		this.usuarioFiltro = usuarioFiltro;
	}

	public String getSituacaoFiltro() {
		return situacaoFiltro;
	}

	public void setSituacaoFiltro(String situacaoFiltro) {
		this.situacaoFiltro = situacaoFiltro;
	}

	public boolean isEmprestada() {
		return emprestada;
	}

	public void setEmprestada(boolean emprestada) {
		this.emprestada = emprestada;
	}
	
}
