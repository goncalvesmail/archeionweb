package br.com.archeion.mbean.documento;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

import util.Operadores;
import util.OperadoresBoleanos;
import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.exception.CompareDateException;
import br.com.archeion.jsf.Constants;
import br.com.archeion.jsf.Util;
import br.com.archeion.mbean.ArcheionBean;
import br.com.archeion.mbean.ExceptionManagedBean;
import br.com.archeion.modelo.documento.Documento;
import br.com.archeion.modelo.documento.Origem;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.modelo.tipodocumento.TipoDocumento;
import br.com.archeion.negocio.documento.DocumentoBO;
import br.com.archeion.negocio.empresa.EmpresaBO;
import br.com.archeion.negocio.local.LocalBO;
import br.com.archeion.negocio.pasta.PastaBO;
import br.com.archeion.negocio.tipodocumento.TipoDocumentoBO;

public class DocumentoMBean extends ArcheionBean {

	private Documento documento;
	private List<Documento> listaDocumento;
	private List<SelectItem> listaLocal;
	private List<SelectItem> listaEmpresa;
	private List<SelectItem> listaTipoDocumento;
	private List<SelectItem> listaOrigem;
	private List<SelectItem> listaPasta;
	
	private List<SelectItem> listaChaves;
	private List<SelectItem> listaOperadores;
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
	
	private LocalBO localBO = (LocalBO) Util.getSpringBean("localBO");
	private PastaBO pastaBO = (PastaBO) Util.getSpringBean("pastaBO");
	private EmpresaBO empresaBO = (EmpresaBO) Util.getSpringBean("empresaBO");
	private TipoDocumentoBO tipoDocumentoBO = (TipoDocumentoBO) Util.getSpringBean("tipoDocumentoBO");
	private DocumentoBO documentoBO = (DocumentoBO) Util.getSpringBean("documentoBO");
	
	private boolean visualizar = false;
	
	public DocumentoMBean() {
		documento = new Documento();
		listaLocal = new ArrayList<SelectItem>();
		listaTipoDocumento = new ArrayList<SelectItem>();
		listaEmpresa = new ArrayList<SelectItem>();
		listaPasta = new ArrayList<SelectItem>();
	}
	
	public void gerarReferencia() {
		Local local = localBO.findById(documento.getLocal().getId());
		Long ultimoDoc = local.getUltimoDocumento();
		ultimoDoc++;
		documento.setReferencia(new GregorianCalendar().get(Calendar.YEAR)+"/"+ultimoDoc);
	}

	
	public boolean validate() {
		boolean erro=false;
		if ( documento.getDestinatario()==null
				|| documento.getDestinatario().equals("") ) {
			
			addMessage(FacesMessage.SEVERITY_ERROR, 
					"documento.erro.destinatario",ArcheionBean.PERSIST_FAILURE);
			erro=true;
		}
		if ( documento.getRemetente()==null
				|| documento.getRemetente().equals("") ) {
			
			addMessage(FacesMessage.SEVERITY_ERROR, 
					"documento.erro.remetente",ArcheionBean.PERSIST_FAILURE);
			erro=true;
		}
		if ( documento.getReferencia()==null
				|| documento.getReferencia().equals("") ) {
			
			addMessage(FacesMessage.SEVERITY_ERROR, 
					"documento.erro.referencia",ArcheionBean.PERSIST_FAILURE);
			erro=true;
		}
		if ( documento.getData()==null
				|| documento.getData().equals("") ) {
			
			addMessage(FacesMessage.SEVERITY_ERROR, 
					"documento.erro.data",ArcheionBean.PERSIST_FAILURE);
			erro=true;
		}
		return erro;
	}
	
	public String incluir() {
		try {		
			
			if (validate()) {
				preencherCombos();
				return "formularioDocumento";
			}		
			incluirMBean();
			
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CompareDateException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.dataFutura",ArcheionBean.PERSIST_FAILURE);
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
			if (validate()) {
				preencherCombos();
				return "formularioDocumento";
			}
			incluirMBean();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CompareDateException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.dataFutura",ArcheionBean.PERSIST_FAILURE);
			return goToForm();
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}			
		return goToForm();
	}

	public void incluirMBean() throws AccessDeniedException, BusinessException, CadastroDuplicadoException {	
		documento.setId(null);
		
		Local l = localBO.findById(documento.getLocal().getId());
		documento.setLocal(l);
		
		TipoDocumento t = tipoDocumentoBO.findById(documento.getTipoDocumento().getId());
		documento.setTipoDocumento(t);
		
		documentoBO.persist(documento);
		addMessage(FacesMessage.SEVERITY_INFO,"geral.inclusao.sucesso",ArcheionBean.PERSIST_SUCESS);
	}

	public String goToVisualizar() {
		visualizar=true;
		return goToAlterar();
	}
	
	public String goToLocalizarDocumento() {
		documento = new Documento();
		listaDocumento = new ArrayList<Documento>();
		listaChaves = new ArrayList<SelectItem>();
		listaOperadores = new ArrayList<SelectItem>();
		listaOperadoresBoleanos = new ArrayList<SelectItem>();
		
		for(ChavesPesquisa c: ChavesPesquisa.values()){
			listaChaves.add(new SelectItem(c.getId(),c.getLabel()));
		}		
		for(Operadores op : Operadores.values()){
			listaOperadores.add(new SelectItem(op.getId(),op.getLabel()));
		}
		for(OperadoresBoleanos opb: OperadoresBoleanos.values()) {
			listaOperadoresBoleanos.add(new SelectItem(opb.getId(),opb.getLabel()));
		}
		
		return "formularioLocalizarDocumento";
	}
	
	public String localizarDocumento() {
		documento = new Documento();		
		listaDocumento = new ArrayList<Documento>();
		listaChaves = new ArrayList<SelectItem>();
		listaOperadores = new ArrayList<SelectItem>();
		listaOperadoresBoleanos = new ArrayList<SelectItem>();
		
		for(ChavesPesquisa c: ChavesPesquisa.values()){
			listaChaves.add(new SelectItem(c.getId(),c.getLabel()));	
		}
		for(Operadores op : Operadores.values()){
			listaOperadores.add(new SelectItem(op.getId(),op.getLabel()));
		}
		for(OperadoresBoleanos opb: OperadoresBoleanos.values()) {
			listaOperadoresBoleanos.add(new SelectItem(opb.getId(),opb.getLabel()));
		}
		StringBuffer sb = new StringBuffer();
		try {
			if(this.chave1 != -1){
				sb.append(ChavesPesquisa.getDataBaseValue(this.chave1));
				sb.append(Operadores.getDataBaseValue(this.operador1));
				sb.append("'");
				String conv = ChavesPesquisa.getConversorValue(this.chave1);
				if(conv != null){
					SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
					sb.append(new SimpleDateFormat(conv).format(sdf.parse(this.valor1)) );					
				} else {
					sb.append(this.valor1);
				}
				sb.append("'");
			}
			if(this.chave2 != -1){
				sb.append(OperadoresBoleanos.getDataBaseValue(this.operadorBoleano1));
				sb.append(ChavesPesquisa.getDataBaseValue(this.chave2));
				sb.append(Operadores.getDataBaseValue(this.operador2));
				sb.append("'");
				String conv = ChavesPesquisa.getConversorValue(this.chave2);
				if(conv != null){
					SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
					sb.append(new SimpleDateFormat(conv).format(sdf.parse(this.valor2)) );
				} else {
					sb.append(this.valor2);
				}
				sb.append("'");
			}
			if(this.chave3 != -1){
				sb.append(OperadoresBoleanos.getDataBaseValue(this.operadorBoleano2));
				sb.append(ChavesPesquisa.getDataBaseValue(this.chave3));
				sb.append(Operadores.getDataBaseValue(this.operador3));
				sb.append("'");
				String conv = ChavesPesquisa.getConversorValue(this.chave3);
				if(conv != null){
					SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
					sb.append(new SimpleDateFormat(conv).format(sdf.parse(this.valor3)) );
				} else {
					sb.append(this.valor3);
				}
				sb.append("'");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		listaDocumento = documentoBO.consultarDocumento(sb.toString());
		return "formularioLocalizarDocumento";
	}
	
	public String localizarTodosDocumentos() {
		listaDocumento = documentoBO.findAll();
		return "formularioLocalizarDocumento";
	}
	
	public String goToAlterar() {
		try {
			Long id = Long.valueOf(Util.getParameter("_id"));			
			documento = documentoBO.findById(id);
			
			preencherCombos();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return "formularioAlterarDocumento";
	}	

	public String alterar() {
		try {			
			
			if (validate()) {
				preencherCombos();
				return "formularioAlterarDocumento"; 
			}
			
			Local l = localBO.findById(documento.getLocal().getId());
			documento.setLocal(l);
			
			TipoDocumento t = tipoDocumentoBO.findById(documento.getTipoDocumento().getId());
			documento.setTipoDocumento(t);			
			
			documento = documentoBO.merge(documento);

			addMessage(FacesMessage.SEVERITY_INFO,"geral.alteracao.sucesso",ArcheionBean.PERSIST_SUCESS);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CompareDateException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.dataFutura",ArcheionBean.PERSIST_FAILURE);
			return "formularioAlterarDocumento";
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
			documento.setId( id );
			documento = documentoBO.findById(id);
			documentoBO.remove(documento);

			addMessage(FacesMessage.SEVERITY_INFO,"geral.remocao.sucesso",ArcheionBean.PERSIST_SUCESS);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return findAll();
	}	

	public String findAll() {
		try {
			visualizar=false;
			listaDocumento = documentoBO.findAll();
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		}
		return "listaDocumento";
	}
	
	public void valueChanged(ValueChangeEvent event) {
		Long empId = (Long)event.getNewValue();
		Empresa empresa = new Empresa();
		empresa.setId(empId);
		
		documento.getLocal().setEmpresa(empresa);
		
		preencherCombos();
	}
	
	public void valueChangedLocal(ValueChangeEvent event) {
		Long localId = (Long)event.getNewValue();
		Local local = new Local();
		local.setId(localId);
		
		documento.setLocal(local);
		
		preencherCombos();
	}

	public void preencherCombos() {
		
		List<Empresa> empresas = empresaBO.findAll();		
		listaEmpresa = new ArrayList<SelectItem>();
		for(Empresa e:empresas) {
			SelectItem select = new SelectItem(e.getId(),e.getNome());
			listaEmpresa.add(select);
		}
		
		if ( (documento.getLocal().getEmpresa() == null || 
				documento.getLocal().getEmpresa().getId() == null || 
				documento.getLocal().getEmpresa().getId() == 0) && empresas.size()>0 ) {
			documento.getLocal().setEmpresa(empresas.get(0));
		}
		
		List<Local> locais = localBO.findByEmpresa(documento.getLocal().getEmpresa());		
		listaLocal = new ArrayList<SelectItem>();
		for(Local e:locais) {
			SelectItem select = new SelectItem(e.getId(),e.getNome());
			listaLocal.add(select);
		}
		
		if ( (documento.getLocal() == null || 
				documento.getLocal().getId() == null || 
				documento.getLocal().getId() == 0) && locais.size()>0 ) {
			documento.setLocal(locais.get(0));
		}
		
		List<Pasta> pastas = null;
		if ( documento.getLocal().getId()!=null && documento.getLocal().getId().longValue()!=0 ) {
			pastas = pastaBO.findByEmpresaLocal(documento.getLocal().getEmpresa().getId().intValue(),
					documento.getLocal().getId().intValue());
		}
		else if ( documento.getLocal().getEmpresa() != null && 
				documento.getLocal().getEmpresa().getId()!=null && 
				documento.getLocal().getEmpresa().getId().longValue()!=0 ) {
			pastas = pastaBO.findByEmpresaLocal(documento.getLocal().getEmpresa().getId().intValue(),
					-1);
		}
		else {
			pastas = new ArrayList<Pasta>();
		}

		listaPasta = new ArrayList<SelectItem>();
		for(Pasta p:pastas) {
			SelectItem select = new SelectItem(p.getId(),p.getTitulo());
			listaPasta.add(select);
		}
		
		List<TipoDocumento> tipos = tipoDocumentoBO.findAll();		
		listaTipoDocumento = new ArrayList<SelectItem>();
		for(TipoDocumento e:tipos) {
			SelectItem select = new SelectItem(e.getId(),e.getNome());
			listaTipoDocumento.add(select);
		}
		
		listaOrigem = new ArrayList<SelectItem>();
		for (Origem origem : Origem.values()) {
			listaOrigem.add(new SelectItem(origem.toString(), origem.getDescricao()));
		}
	}
	
	public String goToForm() {
		try { 
			documento = new Documento();
			
			preencherCombos();
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		}
		return "formularioDocumento";
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
					+ "/ArcheionDocumento.jasper";
			Relatorio relatorio = documentoBO.getRelatorio(
					new HashMap<String, Object>(), pathJasper);
			relatorio.exportarParaPdfStream(responseStream);

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
			"filename=\"RelacaoDocumentos.pdf\"");
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

	public Documento getEmpresa() {
		return documento;
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

	public void setDocumentoBO(DocumentoBO documentoBO) {
		this.documentoBO = documentoBO;
	}

	public List<SelectItem> getListaLocal() {
		return listaLocal;
	}

	public void setListaLocal(List<SelectItem> listaLocal) {
		this.listaLocal = listaLocal;
	}

	public List<SelectItem> getListaTipoDocumento() {
		return listaTipoDocumento;
	}

	public void setListaTipoDocumento(List<SelectItem> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}

	public LocalBO getLocalBO() {
		return localBO;
	}

	public void setLocalBO(LocalBO localBO) {
		this.localBO = localBO;
	}

	public TipoDocumentoBO getTipoDocumentoBO() {
		return tipoDocumentoBO;
	}

	public void setTipoDocumentoBO(TipoDocumentoBO tipoDocumentoBO) {
		this.tipoDocumentoBO = tipoDocumentoBO;
	}

	public DocumentoBO getDocumentoBO() {
		return documentoBO;
	}

	public List<SelectItem> getListaOrigem() {
		return listaOrigem;
	}

	public void setListaOrigem(List<SelectItem> listaOrigem) {
		this.listaOrigem = listaOrigem;
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

	public boolean isVisualizar() {
		return visualizar;
	}

	public void setVisualizar(boolean visualizar) {
		this.visualizar = visualizar;
	}

	public List<SelectItem> getListaPasta() {
		return listaPasta;
	}

	public void setListaPasta(List<SelectItem> listaPasta) {
		this.listaPasta = listaPasta;
	}

	public PastaBO getPastaBO() {
		return pastaBO;
	}

	public void setPastaBO(PastaBO pastaBO) {
		this.pastaBO = pastaBO;
	}

	public List<SelectItem> getListaChaves() {
		return listaChaves;
	}

	public void setListaChaves(List<SelectItem> listaChaves) {
		this.listaChaves = listaChaves;
	}

	public List<SelectItem> getListaOperadores() {
		return listaOperadores;
	}

	public void setListaOperadores(List<SelectItem> listaOperadores) {
		this.listaOperadores = listaOperadores;
	}

	public List<SelectItem> getListaOperadoresBoleanos() {
		return listaOperadoresBoleanos;
	}

	public void setListaOperadoresBoleanos(List<SelectItem> listaOperadoresBoleanos) {
		this.listaOperadoresBoleanos = listaOperadoresBoleanos;
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
}

enum ChavesPesquisa {
	EMPRESA(1,"Empresa","u.local.empresa.nome"),
	DESTINATARIO(2,"Destinatario","u.destinatario "),
	REMETENTE(3,"Remetente","u.remetente"),
	DATAREFERENCIA(4,"Data de Referência","u.dataReferencia","yyyy-mm-dd"),
	ITEMDOCUMENTAL(5,"Item Documental","u.itemDocumental.nome"),
	LIMITEDATA(6,"Limite Data","u.limiteDataInicial","yyyy-mm-dd"),
	LIMITENOME(7,"Limite Nome","u.limiteNomeInicial"),
	LIMITEVALOR(8,"Limite valor","u.limiteNumeroInicial"),
	LOCAL(9,"Local","u.local.nome"),
	TITULOPASTA(10,"Título Pasta","u.titulo");
	
	ChavesPesquisa(int id, String label, String dataValue) {
		this.id = id;
		this.label = label;
		this.dataValue = dataValue;
		this.conversor = null;
	}
	ChavesPesquisa(int id, String label, String dataValue, String conversor) {
		this.id = id;
		this.label = label;
		this.dataValue = dataValue;
		this.conversor = conversor;
	}
	private int id;
	private String label;
	private String dataValue;
	private String conversor;
	
	public static String getById(int id){
		for(ChavesPesquisa c: ChavesPesquisa.values()){
			if(c.getId() == id){
				return c.getLabel();
			}
		}
		return null;
	}
	
	public static String getDataBaseValue(int id) {
		for(ChavesPesquisa c: ChavesPesquisa.values()){
			if(c.getId() == id){
				return c.getDataValue();
			}
		}
		return null;
	}
	
	public static String getConversorValue(int id) {
		for(ChavesPesquisa c: ChavesPesquisa.values()){
			if(c.getId() == id){
				return c.getConversor();
			}
		}
		return null;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}

	public String getConversor() {
		return conversor;
	}

	public void setConversor(String conversor) {
		this.conversor = conversor;
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
