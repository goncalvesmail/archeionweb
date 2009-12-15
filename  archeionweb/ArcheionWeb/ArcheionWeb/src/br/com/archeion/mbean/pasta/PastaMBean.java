package br.com.archeion.mbean.pasta;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import br.com.archeion.jsf.Constants;
import br.com.archeion.jsf.Util;
import br.com.archeion.mbean.ArcheionBean;
import br.com.archeion.mbean.ExceptionManagedBean;
import br.com.archeion.modelo.SituacaoExpurgo;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.itemdocumental.ItemDocumental;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.modelo.ttd.TTD;
import br.com.archeion.negocio.empresa.EmpresaBO;
import br.com.archeion.negocio.itemdocumental.ItemDocumentalBO;
import br.com.archeion.negocio.local.LocalBO;
import br.com.archeion.negocio.pasta.PastaBO;
import br.com.archeion.negocio.relatoriotxt.RelatorioTxtBO;
import br.com.archeion.negocio.ttd.TTDBO;

public class PastaMBean extends ArcheionBean {
	
	private List<Pasta> listaPasta;
	private List<Pasta> listaPastaTarget;
	private List<SelectItem> listaEmpresa;
	private List<SelectItem> listaLocal;
	private List<SelectItem> listaItemDocumental;
	private List<SelectItem> listaSituacao;	
	
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
	
	private Pasta pasta;
	
	private TTD ttd;
	
	private PastaBO pastaBO = (PastaBO) Util.getSpringBean("pastaBO");
	private EmpresaBO empresaBO = (EmpresaBO) Util.getSpringBean("empresaBO");
	private LocalBO localBO = (LocalBO) Util.getSpringBean("localBO");
	private RelatorioTxtBO relatorioTxtBO = (RelatorioTxtBO) Util.getSpringBean("relatorioTxtBO");
	private ItemDocumentalBO itemDocumentalBO = (ItemDocumentalBO) Util.getSpringBean("itemDocumentalBO");
	private TTDBO ttdBO = (TTDBO) Util.getSpringBean("ttdBO");
	
	private String nomeEventoContagem;
	private boolean visualizar = false;
	
	public PastaMBean() {
		pasta = new Pasta();
		listaPasta = new ArrayList<Pasta>();
		listaPastaTarget = new ArrayList<Pasta>();
	}
	
	public String goToVisualizar() {
		visualizar=true;
		return preparaTelaAlterar();
	}
	
	public void valueChangedEmpresa(ValueChangeEvent event) {
		Long empId = (Long)event.getNewValue();
		Empresa empresa = new Empresa();
		empresa.setId(empId);
		
		pasta.getLocal().setEmpresa(empresa);
		
		preencherCombos();
		
		List<Local> locais = localBO.findByEmpresa(empresa);
		if ( locais!=null && locais.size()>0 ) {
			pasta.setLocal(locais.get(0));
		}
		
		ItemDocumental item = pasta.getItemDocumental();
		Local local = pasta.getLocal();
		
		List<TTD> ttdList = ttdBO.findByEmpresaLocalItemDocumental(0, 
				local.getId().intValue(), item.getId().intValue());
		
		atualizarTTD(ttdList);
	}
	
	public void valueChangedLista(ValueChangeEvent event) {

		Long empId = (Long)event.getNewValue();
		Empresa empresa = new Empresa();
		empresa.setId(empId);
		
		pasta.getLocal().setEmpresa(empresa);
		
		preencherCombos();	
	}

	public void valueChangedData(ValueChangeEvent event) {

		Date d = (Date)event.getNewValue();
		if ( d!=null ) {
			try {				
				pasta.setDataAbertura(d);
			}
			catch (Exception e) {
				pasta.setPrevisaoRecolhimento(null);
				pasta.setPrevisaoExpurgo(null);
			}
		}
		
		Local local = pasta.getLocal();
		ItemDocumental item = pasta.getItemDocumental();
		
		List<TTD> ttdList = ttdBO.findByEmpresaLocalItemDocumental(0, 
				local.getId().intValue(), item.getId().intValue());
		
		atualizarTTD(ttdList);	
	}	
	
	private void atualizarTTD(List<TTD> ttdList) {
		if ( ttdList!=null && ttdList.size()>0 ) {
			ttd = ttdList.get(0);
			if ( ttd.getArquivoIntermediario() ) {
				nomeEventoContagem = "Intermediário";
			}
			else  if ( ttd.getArquivoPermanente() ) {
				nomeEventoContagem = "Permanente";
			}
			else {
				nomeEventoContagem = "";
			}
			

			atualizarPrevisoes();
		}
		else {
			ttd = new TTD();
			nomeEventoContagem = "";

			pasta.setPrevisaoRecolhimento(null);
			pasta.setPrevisaoExpurgo(null);
		}
	}	
	
	private void atualizarPrevisoes() {
		Date abertura = pasta.getDataAbertura();
		pasta.setPrevisaoRecolhimento(null);
		pasta.setPrevisaoExpurgo(null);
		
		if ( abertura!=null ) {
			Calendar previsaoRecolhimento = Calendar.getInstance();
			previsaoRecolhimento.setTime(abertura);			
			if ( ttd.getTempoArquivoCorrente()!=null ) {				
				previsaoRecolhimento.add(Calendar.YEAR, ttd.getTempoArquivoCorrente());
			}
			
			if ( ttd.getArquivoIntermediario() ) {
				Calendar previsaoExpurgo = (GregorianCalendar)previsaoRecolhimento.clone();
				if ( ttd.getTempoArquivoIntermediario()!=null ) {
					previsaoExpurgo.add(Calendar.YEAR, ttd.getTempoArquivoIntermediario());
					pasta.setPrevisaoExpurgo(previsaoExpurgo.getTime());
				}
			}
			
			
			if ( ttd.getArquivoIntermediario() || ttd.getArquivoPermanente() ) {
				pasta.setPrevisaoRecolhimento(previsaoRecolhimento.getTime());	
			}
			else {
				pasta.setPrevisaoExpurgo(previsaoRecolhimento.getTime());	
			}
			
		}
	}
	
	public void valueChangedLocal(ValueChangeEvent event) {
		Long localId = (Long)event.getNewValue();
		Local local = localBO.findById(localId);
		pasta.setLocal(local);
	
		ItemDocumental item = pasta.getItemDocumental();
		
		List<TTD> ttdList = ttdBO.findByEmpresaLocalItemDocumental(0, 
				local.getId().intValue(), item.getId().intValue());
		
		atualizarTTD(ttdList);
	}
	
	public void valueChangedItem(ValueChangeEvent event) {
		Long itemId = (Long)event.getNewValue();
		ItemDocumental item = itemDocumentalBO.findById(itemId);
		pasta.setItemDocumental(item);
		
		Local local = pasta.getLocal();
		
		List<TTD> ttdList = ttdBO.findByEmpresaLocalItemDocumental(0, 
				local.getId().intValue(), item.getId().intValue());
		
		atualizarTTD(ttdList);
	}
	
	public String incluir() {
		try {
			incluirMBean();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
			return "formularioPasta";
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return "formularioPasta";
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
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
			return "formularioPasta";
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return "formularioPasta";
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}			
		return goToForm();
	}

	public void incluirMBean() throws AccessDeniedException, CadastroDuplicadoException, BusinessException {
		pasta.setId(null);
		pasta.setDataReferencia(pasta.getDataAbertura());
		pasta.setSituacao(SituacaoExpurgo.ATIVA);



		pasta = pastaBO.persist(pasta);

		pasta.setLocal(localBO.findById(pasta.getLocal().getId()));
		pasta.setItemDocumental(itemDocumentalBO.findById(pasta.getItemDocumental().getId()));

		addMessage(FacesMessage.SEVERITY_INFO,"geral.inclusao.sucesso",ArcheionBean.PERSIST_SUCESS);

	}

	public String goToAlterar() {
		visualizar=false;
		return preparaTelaAlterar();
	}

	private String preparaTelaAlterar() {
		try {			
			Long id = Long.valueOf(Util.getParameter("_id"));			
			pasta = pastaBO.findById(id);
			
			preencherCombos();
			
			listaItemDocumental = new ArrayList<SelectItem>();
			List<ItemDocumental> itens = itemDocumentalBO.findAll();
			for(ItemDocumental i: itens) {
				listaItemDocumental.add(new SelectItem(i.getId(), i.getNome()));
			}
			
			Local local = pasta.getLocal();
			ItemDocumental item = pasta.getItemDocumental();
			List<TTD> ttdList = ttdBO.findByEmpresaLocalItemDocumental(0, 
					local.getId().intValue(), item.getId().intValue());
			
			atualizarTTD(ttdList);
			
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return "formularioAlterarPasta";
	}	
	
	public String alterar() {
		try {			
			pasta = pastaBO.merge(pasta);
			
			addMessage(FacesMessage.SEVERITY_INFO,"geral.alteracao.sucesso",ArcheionBean.PERSIST_SUCESS);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
			return "formularioAlterarPasta";
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return "formularioPasta";
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
			pasta.setId( id );
			pasta = pastaBO.findById(id);
			pastaBO.remove(pasta);
			
			addMessage(FacesMessage.SEVERITY_INFO,"geral.remocao.sucesso",ArcheionBean.PERSIST_SUCESS);
		} catch (BusinessException e) {
			addMessage(FacesMessage.SEVERITY_INFO, 
					e.getMessageCode(),ArcheionBean.PERSIST_FAILURE);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return findAll();
	}	
	
	public String findByEmpresaLocal() {		
		try {
			listaPasta = pastaBO.findByEmpresaLocalSituacao(pasta.getLocal().getEmpresa().getId().intValue(), 
				pasta.getLocal().getId().intValue(), pasta.getSituacao());
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 
		
		return "listaPasta";
	}
	
	public String imprimir() {
		FacesContext context = getContext();
		try {
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
			
			ServletOutputStream responseStream;
			responseStream = response.getOutputStream();
			String pathJasper = ((ServletContext)context.getExternalContext().getContext()).getRealPath("/WEB-INF/relatorios/")+ 
			"/ArcheionImprimirPasta.jasper";
			HashMap<String, Object> param = new HashMap<String, Object>();
			ParametrosReport ids = new ParametrosReport();
			for(Pasta p: listaPasta) {
				ids.add(p.getId());
			}
			param.put("ids", ids.toString());

			Relatorio relatorio = pastaBO.getRelatorio(param, pathJasper);
			relatorio.exportarParaPdfStream(responseStream);
			
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
					"filename=\"ImprimirPasta.pdf\"");
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
			StringBuilder sb = new StringBuilder("select d.nm_empresa as empresa, b.nm_local as local, c.nm_item_documental as item_documental, ");
			sb.append("a.nm_titulo as titulo, a.nm_caixeta as caixeta, a.dt_expurgo as data_expurgo, (f.vao_endereco_caixa || e.nu_vao_endereco_caixa) as caixa, ");
			sb.append("(case when a.cs_situacao_pasta = 1 then 'Ativa' when a.cs_situacao_pasta = 2 then 'Expurgada' end ) as situacao ");
			sb.append("from tb_pasta a join tb_local b on (a.id_local = b.id_local) ");
			sb.append("join tb_item_documental c on (a.id_item_documental = c.id_item_documental) ");
			sb.append("join tb_empresa d on (b.id_empresa = d.id_empresa) ");
			sb.append("left join tb_caixa e on (a.id_caixa = e.id_caixa) ");
			sb.append("left join tb_endereco_caixa f on (e.id_endereco_caixa = f.id_endereco_caixa) ");
			
			relatorioTxtBO.geraRelatorioTxt(sb.toString(), responseStream);
			
			response.setContentType("application/txt");
			response.setHeader("Content-disposition",
			"filename=\"ImprimirPasta.txt\"");
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
			visualizar=false;
			pasta = new Pasta();
			pasta.setSituacao(SituacaoExpurgo.TODOS);
			preencherCombos();
			
			listaPasta = pastaBO.findAll();
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		}
		return "listaPasta";
	}

	private void preencherCombos() {
		listaEmpresa = new ArrayList<SelectItem>();
		listaLocal = new ArrayList<SelectItem>();
		
		List<Empresa> empresas = empresaBO.findAll();
		for(Empresa emp: empresas) {
			listaEmpresa.add(new SelectItem(emp.getId(),emp.getNome()));
		}
		
		if ( (pasta.getLocal().getEmpresa() == null || 
				pasta.getLocal().getEmpresa().getId() == null || 
				pasta.getLocal().getEmpresa().getId() == 0) && empresas.size()>0 ) {
			pasta.getLocal().setEmpresa(empresas.get(0));
		}
		
		List<Local> locais = localBO.findByEmpresa(pasta.getLocal().getEmpresa());
		for(Local loc: locais){
			listaLocal.add(new SelectItem(loc.getId(),loc.getNome())); 
		}
		
		if ( (locais!=null && locais.size()>0 && 
				(pasta.getLocal()==null || pasta.getLocal().getId()==null 
						|| pasta.getLocal().getId()==0)) && locais.size()>0 ) {
			pasta.setLocal(locais.get(0));
		}
		
		listaSituacao = new ArrayList<SelectItem>();
		for (SituacaoExpurgo situacao : SituacaoExpurgo.values()) {
			listaSituacao.add(new SelectItem(situacao.toString(), situacao.getDescricao()));
		}
	}
	
	public String goToForm() {
		pasta = new Pasta();		
		preencherCombos();
		
		listaItemDocumental = new ArrayList<SelectItem>();
		List<ItemDocumental> itens = itemDocumentalBO.findAll();
		for(ItemDocumental i: itens) {
			listaItemDocumental.add(new SelectItem(i.getId(), i.getNome()));
		}
		
		if ( itens!=null && itens.size()>0 )
			pasta.setItemDocumental(itens.get(0));
		
		
		Local local = pasta.getLocal();
		ItemDocumental item = pasta.getItemDocumental();
		
		if ( local.getId()!=null && item.getId()!=null ) {
			List<TTD> ttdList = ttdBO.findByEmpresaLocalItemDocumental(0, 
					local.getId().intValue(), item.getId().intValue());
			
			atualizarTTD(ttdList);
		}
		
		return "formularioPasta";
	}
	
	public String listEtiquetaPasta() {
		pasta = new Pasta();		
		listaPasta = new ArrayList<Pasta>();
		listaPastaTarget = new ArrayList<Pasta>();
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
		
		return "formularioPastaEtiqueta";
	}
	
	public String consultaEtiquetaPasta() {
		this.consultarPasta();
		return "formularioPastaEtiqueta";
	}
	
	private void consultarPasta() {
		pasta = new Pasta();		
		listaPasta = new ArrayList<Pasta>();
		listaPastaTarget = new ArrayList<Pasta>();
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
		listaPasta = pastaBO.consultaEtiquetaPasta(sb.toString());		
	}

	public List<Pasta> getListaPasta() {
		return listaPasta;
	}

	public void setListaPasta(List<Pasta> listaPasta) {
		this.listaPasta = listaPasta;
	}

	public Pasta getPasta() {
		return pasta;
	}

	public void setPasta(Pasta pasta) {
		this.pasta = pasta;
	}

	public PastaBO getPastaBO() {
		return pastaBO;
	}

	public void setPastaBO(PastaBO pastaBO) {
		this.pastaBO = pastaBO;
	}

	public EmpresaBO getEmpresaBO() {
		return empresaBO;
	}

	public void setEmpresaBO(EmpresaBO empresaBO) {
		this.empresaBO = empresaBO;
	}

	public LocalBO getLocalBO() {
		return localBO;
	}

	public void setLocalBO(LocalBO localBO) {
		this.localBO = localBO;
	}

	public List<SelectItem> getListaEmpresa() {
		return listaEmpresa;
	}

	public void setListaEmpresa(List<SelectItem> listaEmpresa) {
		this.listaEmpresa = listaEmpresa;
	}

	public List<SelectItem> getListaLocal() {
		return listaLocal;
	}

	public void setListaLocal(List<SelectItem> listaLocal) {
		this.listaLocal = listaLocal;
	}

	public List<SelectItem> getListaItemDocumental() {
		return listaItemDocumental;
	}

	public void setListaItemDocumental(List<SelectItem> listaItemDocumental) {
		this.listaItemDocumental = listaItemDocumental;
	}

	public ItemDocumentalBO getItemDocumentalBO() {
		return itemDocumentalBO;
	}

	public void setItemDocumentalBO(ItemDocumentalBO itemDocumentalBO) {
		this.itemDocumentalBO = itemDocumentalBO;
	}

	public TTD getTtd() {
		return ttd;
	}

	public void setTtd(TTD ttd) {
		this.ttd = ttd;
	}

	public TTDBO getTtdBO() {
		return ttdBO;
	}

	public void setTtdBO(TTDBO ttdBO) {
		this.ttdBO = ttdBO;
	}

	public String getNomeEventoContagem() {
		return nomeEventoContagem;
	}

	public void setNomeEventoContagem(String nomeEventoContagem) {
		this.nomeEventoContagem = nomeEventoContagem;
	}

	public boolean isVisualizar() {
		return visualizar;
	}

	public void setVisualizar(boolean visualizar) {
		this.visualizar = visualizar;
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

	public List<Pasta> getListaPastaTarget() {
		return listaPastaTarget;
	}

	public void setListaPastaTarget(List<Pasta> listaPastaTarget) {
		this.listaPastaTarget = listaPastaTarget;
	}

	public List<SelectItem> getListaSituacao() {
		return listaSituacao;
	}

	public void setListaSituacao(List<SelectItem> listaSituacao) {
		this.listaSituacao = listaSituacao;
	}
	
	
}

//Caixa, Caixeta, Data de Abertura, Data de Referencia, Item Documental, Limite Data, 
//Limite Nome, Limite Valor, Local, Observação, Titulo Pasta
enum ChavesPesquisa {
	//CAIXA(1,"Caixa","u.caixa.id"),
	CAIXETA(2,"Caixeta","u.caixeta "),
	DATAABERTURA(3,"Data de Abertura","u.dataAbertura ","yyyy-mm-dd"),
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
