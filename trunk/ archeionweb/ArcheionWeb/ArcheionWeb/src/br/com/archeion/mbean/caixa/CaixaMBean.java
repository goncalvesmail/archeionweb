package br.com.archeion.mbean.caixa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;

import org.acegisecurity.AccessDeniedException;

import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.jsf.Constants;
import br.com.archeion.jsf.Util;
import br.com.archeion.mbean.ArcheionBean;
import br.com.archeion.mbean.ExceptionManagedBean;
import br.com.archeion.mbean.pasta.PastaMBean;
import br.com.archeion.modelo.SituacaoExpurgo;
import br.com.archeion.modelo.TipoArquivo;
import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.enderecocaixa.EnderecoCaixa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.modelo.ttd.TTD;
import br.com.archeion.negocio.caixa.CaixaBO;
import br.com.archeion.negocio.empresa.EmpresaBO;
import br.com.archeion.negocio.enderecocaixa.EnderecoCaixaBO;
import br.com.archeion.negocio.local.LocalBO;
import br.com.archeion.negocio.pasta.PastaBO;
import br.com.archeion.negocio.relatoriotxt.RelatorioTxtBO;
import br.com.archeion.negocio.ttd.TTDBO;

public class CaixaMBean extends ArcheionBean {

	private Caixa caixa;
	private List<Pasta> listaPastas;
	private List<Caixa> listaCaixa;
	private List<SelectItem> listaEmpresa;
	private List<SelectItem> listaLocal;
	private List<SelectItem> listaVao;
	private List<SelectItem> listaNumeroVao;
	private List<SelectItem> listaTipo;	
	private List<SelectItem> listaSituacao;	

	private Caixa caixaSelecionada;
	private int tipo;
	private List<Caixa> listaCaixaSource;
	private List<Caixa> listaCaixaTarget;
	private List<SelectItem> listaCaixaCombo;

	private CaixaBO caixaBO = (CaixaBO) Util.getSpringBean("caixaBO");
	private PastaBO pastaBO = (PastaBO) Util.getSpringBean("pastaBO");
	private LocalBO localBO = (LocalBO) Util.getSpringBean("localBO");
	private EmpresaBO empresaBO = (EmpresaBO) Util.getSpringBean("empresaBO");
	private TTDBO ttdBO = (TTDBO) Util.getSpringBean("ttdBO");
	private EnderecoCaixaBO enderecoCaixaBO = (EnderecoCaixaBO) Util.getSpringBean("enderecoCaixaBO");
	private RelatorioTxtBO relatorioTxtBO = (RelatorioTxtBO) Util.getSpringBean("relatorioTxtBO");

	private Map<Long, Boolean> selecionados;
	private boolean visualizar = false;
	
	private int listaPastaSize;

	public CaixaMBean() {
		caixa = new Caixa();
		caixa.setLocal(new Local());
		listaEmpresa = new ArrayList<SelectItem>();
		selecionados = new HashMap<Long, Boolean>();
	}

	public void valueChangedEmpresa(ValueChangeEvent event) {
		Long empId = (Long)event.getNewValue();
		Empresa empresa = empresaBO.findById(empId);

		if ( empresa!=null ) {
			caixa.getLocal().setId(null);
			caixa.getLocal().setEmpresa(empresa);	
			
			preencherCombos();
			atualizaListaPasta();
			
			List<Local> locais = localBO.findByEmpresa(caixa.getLocal().getEmpresa());
			
			if ( locais!=null && locais.size()>0 ) {
				caixa.setLocal(locais.get(0));
			}
		}
		else {
			listaLocal = new ArrayList<SelectItem>();
		}
		
	}

	public void valueChangedLocal(ValueChangeEvent event) {
		Long localId = (Long)event.getNewValue();
		Local local = localBO.findById(localId);

		if ( local != null ) {
			caixa.setLocal(local);
		}		
		atualizaListaPasta();		
	}

	public void valueChangedTipo(ValueChangeEvent event) {
		TipoArquivo tipo = (TipoArquivo)event.getNewValue();

		caixa.setTipo(tipo);
		atualizaListaPasta();	

		selecionados = new HashMap<Long, Boolean>();
		
		if ( caixa.getPastas()!=null && caixa.getPastas().size()!=0 ) {							
			selecionados = new HashMap<Long, Boolean>();
			Iterator<Pasta> pastas = caixa.getPastas().iterator();		
			while (pastas.hasNext()) {
				Pasta p = (Pasta) pastas.next();
				
				List<TTD> ttds = ttdBO.findByEmpresaLocalItemDocumental(p.getLocal().getEmpresa().getId().intValue(),
						p.getLocal().getId().intValue(), p.getItemDocumental().getId().intValue());
						
				if ( ttds!=null && ttds.size()>0 ) {
					TTD ttd = ttds.get(0);
					
					if ( (ttd.getArquivoIntermediario() &&
							caixa.getTipo().equals(TipoArquivo.INTERMEDIARIO))
							|| (ttd.getArquivoPermanente() &&
									caixa.getTipo().equals(TipoArquivo.PERMANENTE))) {
						Long idPasta = p.getId();

						listaPastas.add(p);
						selecionados.put(idPasta, Boolean.TRUE);
					}
				}
				listaPastaSize = listaPastas.size();
			}
		}	
	}

	public void valueChangedVao(ValueChangeEvent event) {
		Long vaoId = (Long)event.getNewValue();		
		EnderecoCaixa endereco = enderecoCaixaBO.findById(vaoId);


		listaNumeroVao = new ArrayList<SelectItem>();
		List<Caixa> ocupados = caixaBO.findVaoOcupados(endereco.getVao());		
		for( int i=endereco.getVaoInicial(); i<endereco.getVaoFinal()+1; i++ ) {

			boolean ocupado = false;
			for(Caixa c:ocupados) {
				if ( c.getNumeroVao()!=null && i==c.getNumeroVao() ) {
					ocupado = true;
				}
			}

			if ( !ocupado ) {
				listaNumeroVao.add(new SelectItem(new Integer(i),String.valueOf(i)));
			}
		}		
	}

	public void valueChangedVaoAlterar(ValueChangeEvent event) {
		Long vaoId = (Long)event.getNewValue();		
		EnderecoCaixa endereco = enderecoCaixaBO.findById(vaoId);


		listaNumeroVao = new ArrayList<SelectItem>();
		List<Caixa> ocupados = caixaBO.findVaoOcupados(endereco.getVao());		
		for( int i=endereco.getVaoInicial(); i<endereco.getVaoFinal()+1; i++ ) {

			boolean ocupado = false;
			for(Caixa c:ocupados) {
				if ( c.getNumeroVao()!=null && i==c.getNumeroVao() && c.getId()!=caixa.getId()) {
					ocupado = true;
				}
			}

			if ( !ocupado ) {
				listaNumeroVao.add(new SelectItem(new Integer(i),String.valueOf(i)));
			}
		}		
	}


	private void preencherCombos() {
		listaEmpresa = new ArrayList<SelectItem>();
		listaLocal = new ArrayList<SelectItem>();
		listaVao = new ArrayList<SelectItem>();
		listaNumeroVao = new ArrayList<SelectItem>();
		listaTipo = new ArrayList<SelectItem>();

		List<EnderecoCaixa> enderecos = enderecoCaixaBO.findAll();
		for(EnderecoCaixa endereco:enderecos) {
			listaVao.add(new SelectItem(endereco.getId(),endereco.getVao()));
		}


		if ( (caixa.getVao() == null || 
				caixa.getVao().getId() == null || 
				caixa.getVao().getId() == 0) && enderecos.size()>0 ) {
			caixa.setVao(enderecos.get(0));
		}	
		
		if ( caixa.getVao()!=null && caixa.getVao().getId() != null 
				&& caixa.getVao().getId().longValue() != 0) {		
			EnderecoCaixa endereco = caixa.getVao();
			List<Caixa> ocupados = caixaBO.findVaoOcupados(endereco.getVao());

			for( int i=endereco.getVaoInicial(); i<endereco.getVaoFinal()+1; i++ ) {

				boolean ocupado = false;
				for(Caixa c:ocupados) {
					if ( c.getNumeroVao()!=null && i==c.getNumeroVao() ) {
						ocupado = true;
					}
				}

				if ( !ocupado ) {
					listaNumeroVao.add(new SelectItem(new Integer(i),String.valueOf(i)));
				}
			}
		}

		listaTipo = new ArrayList<SelectItem>();
		for (TipoArquivo tipo : TipoArquivo.values()) {
			if ( !tipo.getDescricao().equalsIgnoreCase("Nenhum") &&
					!tipo.getDescricao().equalsIgnoreCase("Todos")) {
				listaTipo.add(new SelectItem(tipo.toString(), tipo.getDescricao()));
			}
		}

		listaSituacao = new ArrayList<SelectItem>();
		for (SituacaoExpurgo situacao : SituacaoExpurgo.values()) {
			listaSituacao.add(new SelectItem(situacao.toString(), situacao.getDescricao()));
		}
		
		List<Empresa> empresas = empresaBO.findAll();
		for(Empresa emp: empresas) {
			listaEmpresa.add(new SelectItem(emp.getId(),emp.getNome()));
		}

		if ( (caixa.getLocal().getEmpresa() == null || 
				caixa.getLocal().getEmpresa().getId() == null || 
				caixa.getLocal().getEmpresa().getId() == 0) && empresas.size()>0 ) {
			caixa.getLocal().setEmpresa(empresas.get(0));			
		}
		
		if ( caixa.getLocal().getEmpresa() != null && 
				caixa.getLocal().getEmpresa().getId() != null ) {
			List<Local> locais = localBO.findByEmpresa(caixa.getLocal().getEmpresa());
			for(Local loc: locais){
				listaLocal.add(new SelectItem(loc.getId(),loc.getNome())); 
			}
	
			if ( locais!=null && locais.size()>0 && 
					(caixa.getLocal()==null||caixa.getLocal().getId()==null
							||caixa.getLocal().getId()==0) ) {
				caixa.setLocal(locais.get(0));
			}
		}
		
	}

	public String goToVisualizar() {
		visualizar=true;
		return goToAlterar();
	}

	public String findByEmpresaLocal() {

		try {
			if ( caixa.getLocal().getEmpresa().getId()!=null && caixa.getLocal().getId() != null ) {
				listaCaixa = caixaBO.findByEmpresaLocalSituacao(caixa.getLocal().getEmpresa().getId().intValue(),
						caixa.getLocal().getId().intValue(), caixa.getSituacao());
			}
			else {
				listaCaixa = new ArrayList<Caixa>();
			}
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} 

		return "listaCaixa";
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
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}			
		return goToForm();
	}

	public void incluirMBean() throws AccessDeniedException, CadastroDuplicadoException, BusinessException {
		caixa.setId(null);

		if ( selecionados.keySet().size() == 0 ) {
			addMessage(FacesMessage.SEVERITY_INFO, "caixa.erro.pasta.vaiza",ArcheionBean.PERSIST_FAILURE);	
		}
		else {
			caixa.setDataCriacao(new Date());		
			caixa.setPastas(new ArrayList<Pasta>());
			caixa.setSituacao(SituacaoExpurgo.ATIVA);
			
			caixa = caixaBO.persist(caixa);

			Iterator<Long> idPastas = selecionados.keySet().iterator();
			Date dtExpurgo = null;
			while (idPastas.hasNext()) {
				Long id = (Long) idPastas.next();

				if ( selecionados.get(id) ) {
					Pasta pasta = pastaBO.findById(id);
					pasta.setCaixa(caixa);				
					pasta = pastaBO.merge(pasta);

					if ( dtExpurgo==null ) {
						dtExpurgo= pasta.getPrevisaoExpurgo();
					}
					else if ( dtExpurgo.before(pasta.getPrevisaoExpurgo()) ) {
						dtExpurgo= pasta.getPrevisaoExpurgo();
					}

					caixa.getPastas().add(pasta);
				}
			}

			caixa.setDataPrevisaoExpurgo(dtExpurgo);
			caixaBO.merge(caixa);		
			addMessage(FacesMessage.SEVERITY_INFO,"geral.inclusao.sucesso",ArcheionBean.PERSIST_SUCESS);
		}
	}

	public String goToAlterar() {
		try {
			Long id = Long.valueOf(Util.getParameter("_id"));			
			caixa = caixaBO.findById(id);

			preencherCombos();

			listaNumeroVao = new ArrayList<SelectItem>();
			if ( caixa.getVao()!=null ) {		
				EnderecoCaixa endereco = caixa.getVao();
				List<Caixa> ocupados = caixaBO.findVaoOcupados(endereco.getVao());

				for( int i=endereco.getVaoInicial(); i<endereco.getVaoFinal()+1; i++ ) {

					boolean ocupado = false;
					for(Caixa c:ocupados) {
						if ( c.getNumeroVao()!=null && i==c.getNumeroVao() && c.getId()!=caixa.getId() ) {
							ocupado = true;
						}
					}

					if ( !ocupado ) {
						listaNumeroVao.add(new SelectItem(new Integer(i),String.valueOf(i)));
					}
				}
			}

			if ( !visualizar) {
				atualizaListaPasta();
			}
			else {
				listaPastas = new ArrayList<Pasta>();
			}
			
			if ( caixa.getPastas()!=null && caixa.getPastas().size()!=0 ) {							
				selecionados = new HashMap<Long, Boolean>();
				Iterator<Pasta> pastas = caixa.getPastas().iterator();
				while (pastas.hasNext()) {
					Pasta p = (Pasta) pastas.next();
					Long idPasta = p.getId();

					listaPastas.add(p);
					selecionados.put(idPasta, Boolean.TRUE);
				}
				listaPastaSize = listaPastas.size();
			}

		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (Exception e) {
			ExceptionManagedBean excBean = (ExceptionManagedBean) Util.getManagedBean("exceptionManagedBean");
			excBean.setExc(e);
			return Constants.ERROR_HANDLER;
		}
		return "formularioAlterarCaixa";
	}	
	
	public void goToAlterarPasta() {
		PastaMBean p = new PastaMBean();
		p.goToAlterar();
	}

	public String alterar() {
		try {	


			List<Pasta> atuais = caixa.getPastas();
			for( Pasta p:atuais ) {
				long id = p.getId();
				if( selecionados.get(id)==null || !selecionados.get(id) ) {
					Pasta pasta = pastaBO.findById(id);
					pasta.setCaixa(null);				
					pastaBO.merge(pasta);		
				}
			}


			Iterator<Long> idPastas = selecionados.keySet().iterator();
			Date dtExpurgo = null;
			while (idPastas.hasNext()) {
				Long id = (Long) idPastas.next();

				if ( selecionados.get(id) ) {
					Pasta pasta = pastaBO.findById(id);
					pasta.setCaixa(caixa);				
					pasta = pastaBO.merge(pasta);

					if ( dtExpurgo==null ) {
						dtExpurgo= pasta.getPrevisaoExpurgo();
					}
					else if ( dtExpurgo.before(pasta.getPrevisaoExpurgo()) ) {
						dtExpurgo= pasta.getPrevisaoExpurgo();
					}

					caixa.getPastas().add(pasta);
				}
			}

			caixa.setDataPrevisaoExpurgo(dtExpurgo);
			caixa = caixaBO.merge(caixa);

			addMessage(FacesMessage.SEVERITY_INFO,"geral.alteracao.sucesso",ArcheionBean.PERSIST_SUCESS);
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		} catch (CadastroDuplicadoException e) {
			addMessage(FacesMessage.SEVERITY_INFO, "error.business.cadastro.duplicado",ArcheionBean.PERSIST_FAILURE);
			return "formularioAlterarLocal";
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
			caixa.setId( id );
			caixa = caixaBO.findById(id);
			
			List<Pasta> atuais = caixa.getPastas();
			for( Pasta p:atuais ) {
				long idPasta = p.getId();
				Pasta pasta = pastaBO.findById(idPasta);
				pasta.setCaixa(null);				
				pastaBO.merge(pasta);	
			}
			caixa.setPastas(new ArrayList<Pasta>());
			
			caixaBO.remove(caixa);

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
			caixa = new Caixa();
			caixa.setLocal(new Local());
			caixa.setSituacao(SituacaoExpurgo.TODOS);
			
			visualizar=false;
			listaCaixa = caixaBO.findAll();
			selecionados = new HashMap<Long, Boolean>();
			preencherCombos();
		}
		catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		}
		return "listaCaixa";
	}

	public String goToForm() {

		caixa = new Caixa();
		caixa.setLocal(new Local());
		listaEmpresa = new ArrayList<SelectItem>();
		listaPastas = new  ArrayList<Pasta>();
		selecionados = new HashMap<Long, Boolean>();
		
		caixa = new Caixa();
		caixa.setTipo(TipoArquivo.PERMANENTE);

		selecionados = new HashMap<Long, Boolean>();
		preencherCombos();

		atualizaListaPasta();

		return "formularioCaixa";
	}

	public String goToEtiqueta() {
		try { 
			caixa = new Caixa();
			listaTipo = new ArrayList<SelectItem>();
			listaCaixaSource = new ArrayList<Caixa>();
			listaCaixaTarget = new ArrayList<Caixa>();
			listaCaixaCombo  = new ArrayList<SelectItem>();
			caixaSelecionada = new Caixa();
			caixaSelecionada.setId(-1l);
			
			List<Caixa> lc = caixaBO.findAll();
			for(Caixa c: lc){
				listaCaixaCombo.add(new SelectItem(c.getId(),c.getTitulo()));
			}

			for(TipoArquivo t: TipoArquivo.values()){
				listaTipo.add(new SelectItem(t.toString(), t.getDescricao()));
			}
		} catch (AccessDeniedException aex) {
			return Constants.ACCESS_DENIED;
		}
		return "formularioEtiquetaCaixa";
	}

	public String consultaEtiquetaCaixa() {
		if(caixaSelecionada!=null && (caixaSelecionada.getId() == null || 
				caixaSelecionada.getId() == -1)){
			listaCaixaSource = caixaBO.consultaEtiquetaCaixa(-1,caixa.getDataCriacao(),caixa.getTipo());
		} else if ( caixaSelecionada!=null ) {
			listaCaixaSource = caixaBO.consultaEtiquetaCaixa(caixaSelecionada.getId().intValue(),null,null);
		} else {
			listaCaixaSource = caixaBO.consultaEtiquetaCaixa(-1,null,null);
		}

		return "formularioEtiquetaCaixa";
	}

	private void atualizaListaPasta() {
		if ( caixa.getLocal()!=null && caixa.getLocal().getId()!=null ) {			
			if ( caixa.getTipo().getId().intValue() ==
				TipoArquivo.PERMANENTE.getId().intValue() ) {
				listaPastas = pastaBO.consultaPermanenteRecolhimento(caixa.getLocal());
			}
			else {
				listaPastas = pastaBO.consultaTemporarioRecolhimento(caixa.getLocal());
			}
		}
		listaPastaSize = listaPastas.size();
	}

	public String imprimir() {
		FacesContext context = getContext();
		try {
			if(listaCaixaTarget.size() <= 0){
				addMessage(FacesMessage.SEVERITY_INFO, "caixa.error.selecione.caixa",ArcheionBean.PERSIST_FAILURE);
				return goToEtiqueta();
			}
			HttpServletResponse response = (HttpServletResponse) context
			.getExternalContext().getResponse();
			
			//Cria um cache no  C:\tmp
			JRFileVirtualizer fileVirtualizer = new JRFileVirtualizer(4, "c:\\tmp");

			ServletOutputStream responseStream;
			responseStream = response.getOutputStream();
			String pathJasper = ((ServletContext)context.getExternalContext().getContext()).getRealPath("/WEB-INF/relatorios/")+ 
			"/ArcheionEtiquetaCaixa.jasper";
			HashMap<String, Object> param = new HashMap<String, Object>();
			ParametrosReport ids = new ParametrosReport();
			for(Caixa p: listaCaixaTarget) {
				ids.add(p.getId());
			}
			param.put("idsCaixa", ids.toString());
			//Seta o parametro REPORT_VIRTUALIZER com o diretório onde será armazenado o cache
			param.put(JRParameter.REPORT_VIRTUALIZER, fileVirtualizer);
			Relatorio relatorio = empresaBO.getRelatorio(param, pathJasper);
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
		
			StringBuilder sb = new StringBuilder("select c.nm_empresa as empresa, b.nm_local as local, (d.vao_endereco_caixa || a.nu_vao_endereco_caixa) as caixa, ");
			sb.append("a.dt_criacao as data_criacao, a.dt_previsao_expurgo as data_expurgo, ");
			sb.append("(case when a.cs_tipo_arquivo = 2 then 'Permanente' when a.cs_tipo_arquivo = 3 then 'Intermediario' end) as tipo, ");
			sb.append("(case when a.cs_situacao = 1 then 'Ativa' when a.cs_situacao = 2 then 'Expurgada' end) as situacao ");
			sb.append("from tb_caixa a join tb_local b on (a.id_local = b.id_local) ");
			sb.append("join tb_empresa c on (b.id_empresa = c.id_empresa) ");
			sb.append("join tb_endereco_caixa d on (a.id_endereco_caixa = d.id_endereco_caixa) ");
			sb.append("order by 1,2,3 ");
						
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
	
	public String imprimirLista() {
		FacesContext context = getContext();
		try {
			HttpServletResponse response = (HttpServletResponse) context
			.getExternalContext().getResponse();

			ServletOutputStream responseStream;
			responseStream = response.getOutputStream();
			String pathJasper = ((ServletContext)context.getExternalContext().getContext()).getRealPath("/WEB-INF/relatorios/")+ 
			"/ArcheionImprimirCaixa.jasper";
			HashMap<String, Object> param = new HashMap<String, Object>();
			Relatorio relatorio = empresaBO.getRelatorio(param, pathJasper);
			relatorio.exportarParaPdfStream(responseStream);

			response.setContentType("application/pdf");
			response.setHeader("Content-disposition",
			"filename=\"RelacaoCaixa.pdf\"");
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
	
	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public List<Caixa> getListaCaixa() {
		return listaCaixa;
	}

	public void setListaCaixa(List<Caixa> listaCaixa) {
		this.listaCaixa = listaCaixa;
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

	public CaixaBO getCaixaBO() {
		return caixaBO;
	}

	public void setCaixaBO(CaixaBO caixaBO) {
		this.caixaBO = caixaBO;
	}

	public LocalBO getLocalBO() {
		return localBO;
	}

	public void setLocalBO(LocalBO localBO) {
		this.localBO = localBO;
	}

	public EmpresaBO getEmpresaBO() {
		return empresaBO;
	}

	public void setEmpresaBO(EmpresaBO empresaBO) {
		this.empresaBO = empresaBO;
	}

	public List<SelectItem> getListaVao() {
		return listaVao;
	}

	public void setListaVao(List<SelectItem> listaVao) {
		this.listaVao = listaVao;
	}

	public List<SelectItem> getListaTipo() {
		return listaTipo;
	}

	public void setListaTipo(List<SelectItem> listaTipo) {
		this.listaTipo = listaTipo;
	}

	public EnderecoCaixaBO getEnderecoCaixaBO() {
		return enderecoCaixaBO;
	}

	public void setEnderecoCaixaBO(EnderecoCaixaBO enderecoCaixaBO) {
		this.enderecoCaixaBO = enderecoCaixaBO;
	}

	public List<Pasta> getListaPastas() {
		return listaPastas;
	}

	public void setListaPastas(List<Pasta> listaPastas) {
		this.listaPastas = listaPastas;
	}

	public PastaBO getPastaBO() {
		return pastaBO;
	}

	public void setPastaBO(PastaBO pastaBO) {
		this.pastaBO = pastaBO;
	}

	public Map<Long, Boolean> getSelecionados() {
		return selecionados;
	}

	public void setSelecionados(Map<Long, Boolean> selecionados) {
		this.selecionados = selecionados;
	}

	public List<SelectItem> getListaNumeroVao() {
		return listaNumeroVao;
	}

	public void setListaNumeroVao(List<SelectItem> listaNumeroVao) {
		this.listaNumeroVao = listaNumeroVao;
	}


	public boolean isVisualizar() {
		return visualizar;
	}

	public void setVisualizar(boolean visualizar) {
		this.visualizar = visualizar;
	}

	public Caixa getCaixaSelecionada() {
		return caixaSelecionada;
	}

	public void setCaixaSelecionada(Caixa caixaSelecionada) {
		this.caixaSelecionada = caixaSelecionada;
	}

	public List<Caixa> getListaCaixaSource() {
		return listaCaixaSource;
	}

	public void setListaCaixaSource(List<Caixa> listaCaixaSource) {
		this.listaCaixaSource = listaCaixaSource;
	}

	public List<Caixa> getListaCaixaTarget() {
		return listaCaixaTarget;
	}

	public void setListaCaixaTarget(List<Caixa> listaCaixaTarget) {
		this.listaCaixaTarget = listaCaixaTarget;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public List<SelectItem> getListaCaixaCombo() {
		return listaCaixaCombo;
	}

	public void setListaCaixaCombo(List<SelectItem> listaCaixaCombo) {
		this.listaCaixaCombo = listaCaixaCombo;
	}

	public List<SelectItem> getListaSituacao() {
		return listaSituacao;
	}

	public void setListaSituacao(List<SelectItem> listaSituacao) {
		this.listaSituacao = listaSituacao;
	}

	public int getListaPastaSize() {
		return listaPastaSize;
	}

	public void setListaPastaSize(int listaPastaSize) {
		this.listaPastaSize = listaPastaSize;
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
