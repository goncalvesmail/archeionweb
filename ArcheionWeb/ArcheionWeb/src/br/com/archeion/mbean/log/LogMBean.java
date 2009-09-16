package br.com.archeion.mbean.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.acegisecurity.AccessDeniedException;

import br.com.archeion.jsf.Constants;
import br.com.archeion.jsf.Util;
import br.com.archeion.mbean.ArcheionBean;
import br.com.archeion.modelo.log.Log;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.negocio.log.LogBusiness;
import br.com.archeion.negocio.usuario.UsuarioBO;

public class LogMBean extends ArcheionBean {

	private List<Log> listaLog;
	private Date dataInicial;
	private Date dataFinal;
	private Usuario usuario;
	private List<SelectItem> listaUsuario;
	
	private LogBusiness logBO = (LogBusiness) Util.getSpringBean("logBusiness");
	private UsuarioBO usuarioBO = (UsuarioBO) Util.getSpringBean("usuarioBO");
	
	public LogMBean() {
		listaLog = new ArrayList<Log>();
		usuario = new Usuario();		
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
