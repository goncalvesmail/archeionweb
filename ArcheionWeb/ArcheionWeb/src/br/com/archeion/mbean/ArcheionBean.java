/**
 * 
 */
package br.com.archeion.mbean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.apache.myfaces.custom.datascroller.HtmlDataScroller;

import br.com.archeion.exception.BusinessException;
import br.com.archeion.jsf.SituacaoFormulario;
import br.com.archeion.modelo.AbstractTO;
import br.com.archeion.resources.MessagesResources;

/**
 * Base para todos os backing beans.
 * 
*/
public class ArcheionBean {

	/**
	 * Quantidade de registros que deve listar na página.
	 */
	private static final int PAGINATOR_SIZE = 10;

	/**
	 * Quantidade de páginas exibidas,
	 */
	private static final int MAX_PAGES = 10;

	/**
	 * Quantidade de itens que são saltados..
	 */
	private static final int FAST_STEP = 10;

	/**
	 * LOG utilizado na classe.
	 */
	private static final Logger LOG = Logger.getLogger(ArcheionBean.class);

	/**
	 * String de Sucesso.
	 */
	protected static final String PERSIST_SUCESS = "sucesso";

	/**
	 * String de Falha.
	 */
	protected static final String PERSIST_FAILURE = "falha";
	
	/**
	 * String de Erro de Acesso.
	 */
	protected static final String ACCESS_DENIED = "access.denied";

	/**
	 * Situação do formulário.
	 */
	private SituacaoFormulario situacaoFormulario;

	/**
	 * @return Current Instance of Faces Context.
	 */
	public FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}

	/**
	 * @return Retorna o application do faces
	 */
	public Application getApplication() {
		return getContext().getApplication();
	}

	/**
	 * <p>
	 * Retorna qualquer atributo guardado nos escopos de request, session ou
	 * application com o nome passado por parâmetro. Se nenhum dos atributos for
	 * encontrado, e esse for o nome de algum managed bean registrado, ele cria
	 * uma nova instância deste managed bean, guarda no seu escopo específico e
	 * o retorna. Se o atributo não existir, e nenhum managed bean for criado,
	 * retorna <code>null</code>.
	 * </p>
	 * 
	 * @return Object
	 * @param name
	 *            nome do atributo a ser pego
	 */
	public Object getBean(final String name) {

		// TODO (Marcelo) Não utilizar métodos deprecated
		return getApplication().getVariableResolver().resolveVariable(
				getContext(), name);
	}

	/**
	 * Exibe um arquivo para download.
	 * 
	 * @param conteudoArquivo
	 *            Array de Bytes com o conteúdo do arquivo.
	 * @param nomeArquivo
	 *            Nome do arquivo.
	 */
	/*public static synchronized void downloadByte(final byte[] conteudoArquivo,
			final String nomeArquivo) {

		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();

		HttpServletResponse response = (HttpServletResponse) context
				.getResponse();

		response.reset();

		response.setHeader("Content-Disposition", "attachment;filename=\""
				+ nomeArquivo + "\"");
		response.setContentLength(conteudoArquivo.length);

		try {

			// Uso da API jMimeMagic (Java Mime Magic Library) para pegar
			// dinamicamente o tipo MIME a partir do conteúdo do arquivo.
			MagicMatch match = null;
			String contentType = null;
			String extension = null;
			try {
				match = Magic.getMagicMatch(conteudoArquivo);
				contentType = match.getMimeType();
				extension = match.getExtension();
			} catch (MagicMatchNotFoundException e) {
				contentType = "text/plain";
				extension = "txt";
			}

			response.setContentType(contentType);
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ nomeArquivo + "." + extension + "\"");

			response.getOutputStream().write(conteudoArquivo);
			response.getOutputStream().flush();

		} catch (Throwable e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro de leitura de arquivo para download.", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("erro", msg);
		}

		FacesContext.getCurrentInstance().responseComplete();

	}*/

	/**
	 * Retorna a Situação do Formulário que está sendo exibido.
	 * 
	 * @return SituacaoFormulario
	 */
	public SituacaoFormulario getSituacaoFormulario() {
		return situacaoFormulario;
	}

	/**
	 * Encapsula as mensagens de erro.
	 * 
	 * @param e
	 *            Exceção.
	 */
	protected void setError(final BusinessException e) {
		final List<BusinessException> exceptions = e.getExceptions();

		if (exceptions == null || exceptions.isEmpty()) {
			final String message = e.getMessage();
			getContext().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, message,
							message));
			LOG.error(message);
		} else {
			for (BusinessException businessException : exceptions) {
				final String message = businessException.getMessage();
				getContext().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, message,
								message));
				LOG.error(message);
			}
		}
	}

	/**
	 * Encapsula as mensagens de sucesso.
	 * 
	 * @param key
	 *            Chave do mapeamento no messages.properties.
	 */
	protected void setError(final String key) {

		LOG.info("Recuperando mensagem de erro internacionalizada: " + key);
		String i18n = MessagesResources.getBundleMessage(key);

		getContext().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, i18n, i18n));
		LOG.error("Mensagem exibida para o usuário: " + i18n);
	}

	/**
	 * 
	 * @param situacaoFormulario
	 *            Seta a situação do formulário.
	 */
	public void setSituacaoFormulario(
			final SituacaoFormulario situacaoFormulario) {
		this.situacaoFormulario = situacaoFormulario;
	}

	/**
	 * Se o formulário estiver detalhando as informações, os campos devem estar
	 * desabilitados.
	 * 
	 * @return boolean
	 */
	public boolean isDesabilitado() {
		if (this.situacaoFormulario.equals(SituacaoFormulario.DETALHANDO)) {
			return true;
		}
		return false;
	}

	/**
	 * Verifica se o formulário está habilitado para incluir ou detalhar
	 * informações. Se um campo estiver desabilitado durante o cadastro.
	 * 
	 * @return boolean
	 */
	public boolean isDesabilitadoCadastro() {
		if (this.situacaoFormulario.equals(SituacaoFormulario.INCLUINDO)
				|| this.situacaoFormulario
						.equals(SituacaoFormulario.DETALHANDO)) {
			return true;
		}
		return false;
	}

	/**
	 * Verifica se o formulário está carregado para alterar informações.
	 * 
	 * @return boolean
	 */
	public boolean isAlterando() {
		return this.situacaoFormulario.equals(SituacaoFormulario.ALTERANDO);
	}

	/**
	 * Verifica se o formulário está carregado para incluir informações.
	 * 
	 * @return boolean
	 */
	public boolean isIncluindo() {
		return this.situacaoFormulario.equals(SituacaoFormulario.INCLUINDO);
	}

	/**
	 * Verifica se o formulário está detalhando informações.
	 * 
	 * @return boolean
	 */
	public boolean isDetalhando() {
		return this.situacaoFormulario.equals(SituacaoFormulario.DETALHANDO);
	}

	/**
	 * Verifica se pelo tamanho da lista, será exibida a paginação.
	 * 
	 * @return True or false :P
	 */
	public boolean isShowPaginator() {
		final Collection<? extends AbstractTO> list = getCollection();
		return list != null && list.size() > PAGINATOR_SIZE;
	}

	/**
	 * Método que deve ser implementado pela subclasse quando esta for utilizar
	 * paginação.
	 * 
	 * @return A coleção que será utilizada para a paginação.
	 */
	protected Collection<? extends AbstractTO> getCollection() {
		return null;
	}

	/**
	 * Volta para a página anterior. <BR />
	 * 
	 * A página que chamar este método, deve ter no seu faces-config.xml um
	 * saída para o retorno "sucesso".
	 * 
	 * @return "sucesso"
	 */
	public String voltar() {
		this.situacaoFormulario = SituacaoFormulario.NENHUM;
		return "sucesso";
	}

	/**
	 * Retorna o número de registros numa lista.
	 * 
	 * @return int
	 */
	public int getMaxRows() {
		return PAGINATOR_SIZE;
	}

	/**
	 * Retorna o número de páginas exibidas numa lista.
	 * 
	 * @return int
	 */
	public int getMaxPages() {
		return MAX_PAGES;
	}

	/**
	 * Retorna o número de páginas saltadas numa lista.
	 * 
	 * @return int
	 */
	public int getFastStep() {
		return FAST_STEP;
	}

	/**
	 * Preenche o Combobox com os valores Booleanos.
	 * 
	 * @return A lista de Valores.
	 */
	public List<SelectItem> getBoolean() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(true, "Sim"));
		lista.add(new SelectItem(false, "Não"));
		return lista;
	}

	/**
	 * Reseta o paginador.
	 * 
	 * @param idDataScroller
	 *            id do componente.
	 */
	protected void resetDataScroller(final String idDataScroller) {
		HtmlDataScroller dataScroller = (HtmlDataScroller) getContext()
				.getViewRoot().findComponent(idDataScroller);
		if (dataScroller != null) {
			dataScroller.getUIData().setFirst(0);
		}
	}
	
	public void addMessage(Severity severity, String messageKey, String detail) {
		FacesMessage msg = new FacesMessage(
				severity,
				MessagesResources.getBundleMessage(messageKey),
				detail);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public String toIndex() {
		return "goToIndex";
	}
	
}
