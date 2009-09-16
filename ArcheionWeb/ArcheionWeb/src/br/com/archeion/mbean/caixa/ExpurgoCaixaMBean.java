package br.com.archeion.mbean.caixa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.acegisecurity.AccessDeniedException;

import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.jsf.Constants;
import br.com.archeion.jsf.Util;
import br.com.archeion.mbean.ArcheionBean;
import br.com.archeion.mbean.ExceptionManagedBean;
import br.com.archeion.modelo.SituacaoExpurgo;
import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.negocio.caixa.CaixaBO;
import br.com.archeion.negocio.pasta.PastaBO;

public class ExpurgoCaixaMBean extends ArcheionBean {

	private Date dataExpurgo = new Date();
	private List<Caixa> funcSource;
	private List<Caixa> funcTarget;
	
	private CaixaBO caixaBO = (CaixaBO) Util.getSpringBean("caixaBO");
	private PastaBO pastaBO = (PastaBO) Util.getSpringBean("pastaBO");
	
	public ExpurgoCaixaMBean() {
		funcSource = new ArrayList<Caixa>();
		funcTarget = new ArrayList<Caixa>();
	}

	public String goToForm() {
		try {
			funcSource = caixaBO.findCaixaAtivasExpurgo();
			funcTarget = new ArrayList<Caixa>();
			dataExpurgo = new Date();
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 
		return "formularioExpurgoCaixa";
	}
	
	public String expurgar() {
		try {
			
			for ( Caixa caixa:funcTarget ) {
				
				caixa = caixaBO.findById(caixa.getId());
				caixa.setDataExpurgo(dataExpurgo);
				caixa.setSituacao(SituacaoExpurgo.EXPURGADA);
				
				for( Pasta pasta:caixa.getPastas() ) {
					pasta.setDataExpurgo(dataExpurgo);
					pasta.setSituacao(SituacaoExpurgo.EXPURGADA);
					pastaBO.merge(pasta);	
				}
				
				caixaBO.merge(caixa);	
			}
				
			addMessage(FacesMessage.SEVERITY_INFO,"caixa.expurgo.sucesso",ArcheionBean.PERSIST_SUCESS);
			
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

	public List<Caixa> getFuncSource() {
		return funcSource;
	}

	public void setFuncSource(List<Caixa> funcSource) {
		this.funcSource = funcSource;
	}

	public List<Caixa> getFuncTarget() {
		return funcTarget;
	}

	public void setFuncTarget(List<Caixa> funcTarget) {
		this.funcTarget = funcTarget;
	}

	public Date getDataExpurgo() {
		return dataExpurgo;
	}

	public void setDataExpurgo(Date dataExpurgo) {
		this.dataExpurgo = dataExpurgo;
	}

	public CaixaBO getCaixaBO() {
		return caixaBO;
	}

	public void setCaixaBO(CaixaBO caixaBO) {
		this.caixaBO = caixaBO;
	}
	
}
