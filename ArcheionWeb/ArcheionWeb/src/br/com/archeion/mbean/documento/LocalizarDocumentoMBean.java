package br.com.archeion.mbean.documento;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import util.ChaveDocumento;
import util.ChavePesquisaDocumento;
import util.Operadores;
import util.OperadoresBoleanos;
import util.Relatorio;
import br.com.archeion.jsf.Constants;
import br.com.archeion.jsf.Util;
import br.com.archeion.mbean.ArcheionBean;
import br.com.archeion.modelo.documento.Documento;
import br.com.archeion.negocio.documento.DocumentoBO;

public class LocalizarDocumentoMBean extends ArcheionBean {

	private List<SelectItem> listaChaves1;
	private List<SelectItem> listaOperadores1;

	private List<SelectItem> listaChaves2;
	private List<SelectItem> listaOperadores2;

	private List<SelectItem> listaChaves3;
	private List<SelectItem> listaOperadores3;

	private List<SelectItem> listaOperadoresBoleanos;

	private int chave1;
	private int chave2;
	private int chave3;
	private int operador1;
	private int operador2;
	private int operador3;
	private String valor1;
	private String valor2;
	private String valor3;
	private int operadorBoleano1;
	private int operadorBoleano2;

	private ChavePesquisaDocumento chavesPesquisa = new ChavePesquisaDocumento();

	private Documento documento;
	private List<Documento> listaDocumento;

	private DocumentoBO documentoBO = (DocumentoBO) Util.getSpringBean("documentoBO");

	public String goToLocalizarDocumento() {
		documento = new Documento();		
		listaDocumento = new ArrayList<Documento>();

		this.chave1=-1;
		this.chave2=-1;
		this.chave3=-1;
		
		this.operador1=1;
		this.operador2=1;
		this.operador3=1;
		
		this.valor1="";
		this.valor2="";
		this.valor3="";
		
		listaOperadoresBoleanos = new ArrayList<SelectItem>();
		for(OperadoresBoleanos opb: OperadoresBoleanos.values()) {
			listaOperadoresBoleanos.add(new SelectItem(opb.getId(),opb.getLabel()));
		}

		//1
		listaChaves1 = new ArrayList<SelectItem>();
		listaOperadores1 = new ArrayList<SelectItem>();

		for(ChaveDocumento c: chavesPesquisa.getChaveDocumento() ){
			listaChaves1.add(new SelectItem(c.getId(),c.getLabel()));
		}		
		for(Operadores op : chavesPesquisa.getTodosOperadores()){
			listaOperadores1.add(new SelectItem(op.getId(),op.getLabel()));
		}

		//2
		listaChaves2 = new ArrayList<SelectItem>();
		listaOperadores2 = new ArrayList<SelectItem>();

		for(ChaveDocumento c: chavesPesquisa.getChaveDocumento() ){
			listaChaves2.add(new SelectItem(c.getId(),c.getLabel()));
		}		
		for(Operadores op : chavesPesquisa.getTodosOperadores()){
			listaOperadores2.add(new SelectItem(op.getId(),op.getLabel()));
		}

		//3
		listaChaves3 = new ArrayList<SelectItem>();
		listaOperadores3 = new ArrayList<SelectItem>();

		for(ChaveDocumento c: chavesPesquisa.getChaveDocumento() ){
			listaChaves3.add(new SelectItem(c.getId(),c.getLabel()));
		}		
		for(Operadores op : chavesPesquisa.getTodosOperadores()){
			listaOperadores3.add(new SelectItem(op.getId(),op.getLabel()));
		}

		return "formularioLocalizarDocumento";
	}

	public String localizarDocumento() {
		this.consultarDocumento();
		return "formularioLocalizarDocumento";
	}

	public String localizarTodosDocumento() {
		listaDocumento = documentoBO.findAll();
		return "formularioLocalizarDocumento";
	}

	public void changedChave1(ValueChangeEvent event) {
		Integer id = (Integer)event.getNewValue();
		ChaveDocumento chave = (ChaveDocumento)chavesPesquisa.getChaves(id);

		listaOperadores1 = new ArrayList<SelectItem>();
		Operadores[] operadores = chavesPesquisa.getTodosOperadores();
		if ( chave != null ) {
			operadores = chavesPesquisa.getOperadores(chave.getId());
		}

		for(Operadores op : operadores){
			listaOperadores1.add(new SelectItem(op.getId(),op.getLabel()));
		}
	}

	public void changedChave2(ValueChangeEvent event) {
		Integer id = (Integer)event.getNewValue();
		ChaveDocumento chave = (ChaveDocumento)chavesPesquisa.getChaves(id);

		listaOperadores2 = new ArrayList<SelectItem>();
		Operadores[] operadores = chavesPesquisa.getTodosOperadores();
		if ( chave != null ) {
			operadores = chavesPesquisa.getOperadores(chave.getId());
		}

		for(Operadores op : operadores){
			listaOperadores2.add(new SelectItem(op.getId(),op.getLabel()));
		}
	}

	public void changedChave3(ValueChangeEvent event) {
		Integer id = (Integer)event.getNewValue();
		ChaveDocumento chave = (ChaveDocumento)chavesPesquisa.getChaves(id);

		listaOperadores3 = new ArrayList<SelectItem>();
		Operadores[] operadores = chavesPesquisa.getTodosOperadores();
		if ( chave != null ) {
			operadores = chavesPesquisa.getOperadores(chave.getId());
		}

		for(Operadores op : operadores){
			listaOperadores3.add(new SelectItem(op.getId(),op.getLabel()));
		}
	}

	private void consultarDocumento() {
		documento = new Documento();		
		listaDocumento = new ArrayList<Documento>();
		String conv1 = ChaveDocumento.getConversorValue(this.chave1);

		StringBuffer sb = new StringBuffer();

		if(this.chave1 != -1 && !this.valor1.equals("")){
			if(conv1 == null){
				sb.append("UPPER("+ChaveDocumento.getDataBaseValue(this.chave1)+")");
			} else {
				sb.append(ChaveDocumento.getDataBaseValue(this.chave1));
			}
			
			sb.append(Operadores.getDataBaseValue(this.operador1));
			sb.append("'");
			if ( this.operador1==Operadores.CONTEM.getId() ) sb.append("%");
			
			if(conv1 != null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				try {
					sb.append(new SimpleDateFormat(conv1).format(sdf.parse(this.valor1)) );
				} catch (ParseException e) {
					addMessage(FacesMessage.SEVERITY_INFO, 
							"error.business.dataInvalida",ArcheionBean.PERSIST_FAILURE);
					return;
				}					
			} else {
				sb.append(this.valor1.toUpperCase());
			}
			if ( this.operador1==Operadores.CONTEM.getId() ) sb.append("%");
			sb.append("'");


			if(this.chave2 != -1 && !this.valor2.equals("")){
				String conv2 = ChaveDocumento.getConversorValue(this.chave2);
				sb.append(OperadoresBoleanos.getDataBaseValue(this.operadorBoleano1));
				if(conv2 == null){
					sb.append("UPPER("+ChaveDocumento.getDataBaseValue(this.chave2)+")");
				} else {
					sb.append(ChaveDocumento.getDataBaseValue(this.chave2));
				}
				sb.append(Operadores.getDataBaseValue(this.operador2));
				sb.append("'");
				if ( this.operador2==Operadores.CONTEM.getId() ) sb.append("%");
				
				if(conv2 != null){
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					try {
						sb.append(new SimpleDateFormat(conv2).format(sdf.parse(this.valor2)) );
					} catch (ParseException e) {
						addMessage(FacesMessage.SEVERITY_INFO, 
								"error.business.dataInvalida",ArcheionBean.PERSIST_FAILURE);
						return;
					}
				} else {
					sb.append(this.valor2.toUpperCase());
				}
				if ( this.operador2==Operadores.CONTEM.getId() ) sb.append("%");
				sb.append("'");


				if(this.chave3 != -1 && !this.valor3.equals("")){
					String conv3 = ChaveDocumento.getConversorValue(this.chave3);
					sb.append(OperadoresBoleanos.getDataBaseValue(this.operadorBoleano2));
					if(conv3 == null){
						sb.append("UPPER("+ChaveDocumento.getDataBaseValue(this.chave3)+")");
					} else {
						sb.append(ChaveDocumento.getDataBaseValue(this.chave3));
					}
						
					sb.append(Operadores.getDataBaseValue(this.operador3));
					sb.append("'");
					if ( this.operador3==Operadores.CONTEM.getId() ) sb.append("%");
					
					if(conv3 != null){
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						try {
							sb.append(new SimpleDateFormat(conv3).format(sdf.parse(this.valor3)) );
						} catch (ParseException e) {
							addMessage(FacesMessage.SEVERITY_INFO, 
									"error.business.dataInvalida",ArcheionBean.PERSIST_FAILURE);
							return;
						}
					} else {
						sb.append(this.valor3.toUpperCase());
					}
					if ( this.operador3==Operadores.CONTEM.getId() ) sb.append("%");
					sb.append("'");
				}
			}
		}

		if ( !sb.toString().equals("") ) {
			listaDocumento = documentoBO.consultarDocumento(sb.toString());	
		}
		else {
			listaDocumento = documentoBO.findAll();
		}
	}


	public String imprimirLocalizar() {
		FacesContext context = getContext();
		try {
			if((listaDocumento != null) && (listaDocumento.size() <= 0)){
				addMessage(FacesMessage.SEVERITY_INFO, "documento.error.selecione.documento",ArcheionBean.PERSIST_FAILURE);
				return goToLocalizarDocumento();
			}
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
			
			ServletOutputStream responseStream;
			responseStream = response.getOutputStream();
			String pathJasper = ((ServletContext)context.getExternalContext().getContext()).getRealPath("/WEB-INF/relatorios/")+ 
			"/ArcheionLocalizarDocumento.jasper";
			HashMap<String, Object> param = new HashMap<String, Object>();
			ParametrosReport ids = new ParametrosReport();
			for(Documento p: listaDocumento) {
				ids.add(p.getId());
			}
			param.put("idsDocumento", ids.toString());
			Relatorio relatorio = documentoBO.getRelatorio(param, pathJasper);
			relatorio.exportarParaPdfStream(responseStream);
			
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"filename=\"RelacaoPasta.pdf\"");
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
		return goToLocalizarDocumento();
	}

	//GETs & SETs

	public List<SelectItem> getListaChaves1() {
		return listaChaves1;
	}


	public void setListaChaves1(List<SelectItem> listaChaves1) {
		this.listaChaves1 = listaChaves1;
	}


	public List<SelectItem> getListaOperadores1() {
		return listaOperadores1;
	}


	public void setListaOperadores1(List<SelectItem> listaOperadores1) {
		this.listaOperadores1 = listaOperadores1;
	}

	public List<SelectItem> getListaChaves2() {
		return listaChaves2;
	}


	public void setListaChaves2(List<SelectItem> listaChaves2) {
		this.listaChaves2 = listaChaves2;
	}


	public List<SelectItem> getListaOperadores2() {
		return listaOperadores2;
	}


	public void setListaOperadores2(List<SelectItem> listaOperadores2) {
		this.listaOperadores2 = listaOperadores2;
	}


	public List<SelectItem> getListaChaves3() {
		return listaChaves3;
	}


	public void setListaChaves3(List<SelectItem> listaChaves3) {
		this.listaChaves3 = listaChaves3;
	}


	public List<SelectItem> getListaOperadores3() {
		return listaOperadores3;
	}


	public void setListaOperadores3(List<SelectItem> listaOperadores3) {
		this.listaOperadores3 = listaOperadores3;
	}


	public int getChave1() {
		return chave1;
	}


	public void setChave1(int chave1) {
		this.chave1 = chave1;
	}


	public int getChave2() {
		return chave2;
	}


	public void setChave2(int chave2) {
		this.chave2 = chave2;
	}


	public int getChave3() {
		return chave3;
	}


	public void setChave3(int chave3) {
		this.chave3 = chave3;
	}


	public int getOperador1() {
		return operador1;
	}


	public void setOperador1(int operador1) {
		this.operador1 = operador1;
	}


	public int getOperador2() {
		return operador2;
	}


	public void setOperador2(int operador2) {
		this.operador2 = operador2;
	}


	public int getOperador3() {
		return operador3;
	}


	public void setOperador3(int operador3) {
		this.operador3 = operador3;
	}


	public String getValor1() {
		return valor1;
	}


	public void setValor1(String valor1) {
		this.valor1 = valor1;
	}


	public String getValor2() {
		return valor2;
	}


	public void setValor2(String valor2) {
		this.valor2 = valor2;
	}


	public String getValor3() {
		return valor3;
	}


	public void setValor3(String valor3) {
		this.valor3 = valor3;
	}


	public int getOperadorBoleano1() {
		return operadorBoleano1;
	}


	public void setOperadorBoleano1(int operadorBoleano1) {
		this.operadorBoleano1 = operadorBoleano1;
	}


	public int getOperadorBoleano2() {
		return operadorBoleano2;
	}


	public void setOperadorBoleano2(int operadorBoleano2) {
		this.operadorBoleano2 = operadorBoleano2;
	}


	public ChavePesquisaDocumento getChavesPesquisa() {
		return chavesPesquisa;
	}


	public void setChavesPesquisa(ChavePesquisaDocumento chavesPesquisa) {
		this.chavesPesquisa = chavesPesquisa;
	}


	public Documento getDocumento() {
		return documento;
	}


	public void setDocumento(Documento documento) {
		this.documento = documento;
	}


	public List<Documento> getListaDocumento() {
		return listaDocumento;
	}


	public void setListaDocumento(List<Documento> listaDocumento) {
		this.listaDocumento = listaDocumento;
	}


	public DocumentoBO getDocumentoBO() {
		return documentoBO;
	}


	public void setDocumentoBO(DocumentoBO documentoBO) {
		this.documentoBO = documentoBO;
	}

	public List<SelectItem> getListaOperadoresBoleanos() {
		return listaOperadoresBoleanos;
	}

	public void setListaOperadoresBoleanos(List<SelectItem> listaOperadoresBoleanos) {
		this.listaOperadoresBoleanos = listaOperadoresBoleanos;
	}

}
