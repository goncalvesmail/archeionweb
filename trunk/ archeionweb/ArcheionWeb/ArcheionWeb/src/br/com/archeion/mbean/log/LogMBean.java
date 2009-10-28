package br.com.archeion.mbean.log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;

import br.com.archeion.jsf.Constants;
import br.com.archeion.jsf.Util;
import br.com.archeion.mbean.ArcheionBean;
import br.com.archeion.modelo.log.Log;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.negocio.log.LogBusiness;
import br.com.archeion.negocio.relatoriotxt.RelatorioTxtBO;
import br.com.archeion.negocio.usuario.UsuarioBO;

public class LogMBean extends ArcheionBean {

	private List<Log> listaLog;
	private Date dataInicial;
	private Date dataFinal;
	private Usuario usuario;
	private List<SelectItem> listaUsuario;
	
	private LogBusiness logBO = (LogBusiness) Util.getSpringBean("logBusiness");
	private UsuarioBO usuarioBO = (UsuarioBO) Util.getSpringBean("usuarioBO");
	private RelatorioTxtBO relatorioTxtBO = (RelatorioTxtBO) Util.getSpringBean("relatorioTxtBO");
	
	public LogMBean() {
		listaLog = new ArrayList<Log>();
		usuario = new Usuario();		
	}	
	
	public String imprimirTxt() {
		FacesContext context = getContext();
		try {
			HttpServletResponse response = (HttpServletResponse) context
			.getExternalContext().getResponse();
			
			ServletOutputStream responseStream;
			responseStream = response.getOutputStream();
			StringBuilder sb = new StringBuilder("select b.nm_usuario as usuario, a.dt_data as data, a.tx_acao as acao ");
			sb.append("from tb_log a join tb_usuarios b on (a.id_usuario = b.id_usuario) ");
			sb.append("order by 2 desc, 1 asc ");
						
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
		return"listaLog";
	}	
	
	public String pesquisar() {
		try {
			listaLog = logBO.findAll();
			
			listaUsuario = new ArrayList<SelectItem>();
			List<Usuario> usuarios = usuarioBO.findAll();
			for(Usuario e:usuarios) {
				SelectItem select = new SelectItem(e.getId(),e.getNome());
				listaUsuario.add(select);
			}		
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 
		return "listaLog";
	}
	
	public String filtrar() {
		try {
			listaLog = logBO.findAll(usuario.getId().intValue(),dataInicial,dataFinal);
			
			listaUsuario = new ArrayList<SelectItem>();
			List<Usuario> usuarios = usuarioBO.findAll();
			for(Usuario e:usuarios) {
				SelectItem select = new SelectItem(e.getId(),e.getNome());
				listaUsuario.add(select);
			}		
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 
		return "listaLog";
	}
	
	public String imprimir() {
		//TODO
		listaLog = logBO.findAll();
		return "listaLog";
	}
	

	public List<Log> getListaLog() {
		return listaLog;
	}

	public void setListaLog(List<Log> listaLog) {
		this.listaLog = listaLog;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<SelectItem> getListaUsuario() {
		return listaUsuario;
	}

	public void setListaUsuario(List<SelectItem> listaUsuario) {
		this.listaUsuario = listaUsuario;
	}
	
}
