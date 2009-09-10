package br.com.archeion.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CompareDateException;
import br.com.archeion.exception.InvalidFieldException;
import br.com.archeion.exception.InvalidFieldType;

/**
 * Classe utilitária responsável pelas validações.
 *  
 */
public final class Validator {

	/**
	 * LOG utilizado na classe.
	 */
	private static final Logger LOG = Logger.getLogger(Validator.class);

	/**
	 * Critério para comparação de datas levando em consideração somente Ano, Mês e Dia.
	 */
	public static final int[] CRITERIO_YYYYMMDD = { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH };

	/**
	 * Verifica se uma data é anterior (excluindo o dia de hoje) a outra
	 * compararando os critérios especificados.
	 * 
	 * @param criterios Os critérios a serem usados (Constants da classe Calendar)
	 * @param dataA Uma data
	 * @param dataB Outra data
	 * 
	 * @return <code>true</code> se dataA é anterior a dataB,
	 *         <code>false</code> senão.
	 */
	public static boolean isDataAnterior(final int[] criterios,
			final Date dataA, final Date dataB) {
		final Calendar calendar = Calendar.getInstance();
		Arrays.sort(criterios);

		if (dataA == null && dataB == null) {
			return true;
		}
		if (dataA == null || dataB == null) {
			return false;
		}

		boolean b = false;
		final int[] values = new int[criterios.length];

		calendar.setTime(dataA);
		for (int i = 0; i < criterios.length; i++) {
			values[i] = calendar.get(criterios[i]);
		}
		calendar.setTime(dataB);
		for (int i = 0; i < criterios.length; i++) {
			final int x = calendar.get(criterios[i]);
			if (values[i] < x) {
				b = true;
				break;
			} else if (values[i] > x) {
				break;
			}
		}
		return b;
	}

	/**
	 * Verifica se uma data é posterior (excluindo o dia de hoje) a outra
	 * compararando os critérios especificados.
	 * 
	 * @param criterios Os critérios a serem usados (Constants da classe Calendar)
	 * @param dataA Uma data
	 * @param dataB Outra data
	 * 
	 * @return <code>true</code> se dataA posterior a dataB,
	 *         <code>false</code> senão.
	 */
	public static boolean isDataPosterior(final int[] criterios,
			final Date dataA, final Date dataB) {
		final Calendar calendar = Calendar.getInstance();
		Arrays.sort(criterios);

		if (dataA == null && dataB == null) {
			return true;
		}
		if (dataA == null || dataB == null) {
			return false;
		}

		boolean b = false;
		final int[] values = new int[criterios.length];

		calendar.setTime(dataA);
		for (int i = 0; i < criterios.length; i++) {
			values[i] = calendar.get(criterios[i]);
		}
		calendar.setTime(dataB);
		for (int i = 0; i < criterios.length; i++) {
			final int x = calendar.get(criterios[i]);
			if (values[i] > x) {
				b = true;
				break;
			} else if (values[i] < x) {
				break;
			}
		}
		return b;
	}

	/**
	 * Lista de exceções.
	 */
	private List<BusinessException> exceptions;

	/**
	 * Contém todos os caracteres especiais.
	 */
	private final String CARACTERES_ESPECIAIS = "\"!@#$%¨&*()_+'¹²³£¢¬¢-§=´`[{ª~^]}º<,>.:;?/°\\";

	/**
	 * É possível instanciar esta classe.
	 */
	public Validator() {
		exceptions = new ArrayList<BusinessException>();
	}

	/**
	 * Adiciona um erro a lista de erros.
	 * 
	 * @param e
	 *            Erro que será guardado.
	 */
	public void addError(final BusinessException e) {
		exceptions.add(e);
	}

	/**
	 * @return False caso não exista erro.
	 */
	public boolean existError() {
		return !exceptions.isEmpty();
	}

	/**
	 * @return O campo exceptions
	 */
	public List<BusinessException> getExceptions() {
		return exceptions;
	}

	/**
	 * Verifica se o campo está preechido e sem os caracteres especiais.
	 * 
	 * @param field Valor do campo que será validado.
	 * @param msg Mensagem que será exibida.
	 */
	public void validarCampoObrigatorio(final Object field, final String msg) {

		String campo = null;

		if (field == null) {
			exceptions.add(new BusinessException(msg));
		} else {
			campo = field.toString();
			/*
			 * Faz a iteração até que um caractere que não seja especial seja
			 * encontrado.
			 */
			boolean campoValido = false;
			for (int i = 0; i < campo.length(); i++) {
				if (!CARACTERES_ESPECIAIS.contains("" + campo.charAt(i))) {
					campoValido = true;
					break;
				}
			}

			if (!campoValido) {
				exceptions.add(new BusinessException(msg));
			}
		}

	}

	/**
	 * Valida emails.
	 * 
	 * @param email Email que será validado
	 */
	public void validarEmail(final String email) {
		validarEmail(email, null);
	}

	/**
	 * Valida emails. <BR />
	 * 
	 * @param email Email que será validado
	 * @param campo Campo que será mostrado na mensagem de erro
	 */
	public void validarEmail(final String email, final String campo) {
		Validator.LOG.info("Validando email: " + email);

		// final Pattern p = Pattern
		// .compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
		final Pattern p = Pattern
		.compile("^[a-z0-9_\\.-]{2,}@([a-z0-9_-]{2,}\\.)+[a-z]{2,4}$");
		InvalidFieldException invalidFieldException = null;
		if (campo != null) {
			invalidFieldException = new InvalidFieldException(
					InvalidFieldType.EMAIL, new String[] { campo });
		} else {
			invalidFieldException = new InvalidFieldException(
					InvalidFieldType.EMAIL);
		}

		if (email == null) {
			exceptions.add(invalidFieldException);
		} else {
			final Matcher m = p.matcher(email);
			if (!m.find()) {
				exceptions.add(invalidFieldException);
			}
		}
	}

	/**
	 * Valida ceps.
	 * 
	 * @param cep CEP que será validado
	 */
	public void validarCEP(final String cep) {
		this.validarCEP(cep, new InvalidFieldException(InvalidFieldType.CEP));
	}

	/**
	 * Valida ceps.
	 * 
	 * @param cep CEP que será validado
	 * @param exception Exceção.
	 */
	public void validarCEP(final String cep, final BusinessException exception) {
		final int tamanhoCEP = 8;

		if (cep.length() != tamanhoCEP) {
			exceptions.add(exception);
		} else {
			validarCampoNumerico(cep, InvalidFieldType.CEP);

			final char value = cep.charAt(0);
			boolean valido = false;
			for (int i = 1; i < cep.length(); i++) {
				if (cep.charAt(i) != value) {
					valido = true;
				}
			}

			if (!valido) {
				exceptions.add(exception);
			}
		}
	}

	/**
	 * Valida se a primeira data é anterior a segunda data.
	 * 
	 * @param dataInicio Data de início
	 * @param dataFim Data de término
	 */
	public void validateDatas(final Date dataInicio, final Date dataFim) {
		validateDatas(dataInicio, dataFim, null);
	}

	/**
	 * Valida se a primeira data é anterior a segunda data.
	 * 
	 * @param dataInicio Data de início
	 * @param dataFim Data de término
	 * @param mensagem Mensagem que será exibida para o usuário.
	 */
	public void validateDatas(final Date dataInicio, final Date dataFim,
			final String mensagem) {
		if ((dataInicio == null) || (dataFim == null)
				|| (dataFim.before(dataInicio))) {
			if (mensagem == null) {
				exceptions.add(new CompareDateException());
			} else {
				exceptions.add(new CompareDateException(mensagem));
			}
		}
	}

	/**
	 * Método para validar campo numérico.
	 * 
	 * @param numero número que será validado.
	 * @param invalidFieldType Tipo de validação
	 */
	public void validarCampoNumerico(final String numero, final InvalidFieldType invalidFieldType) {
		if (numero.length() > 0) {
			if (!NumberUtils.isNumber(numero)) {
				exceptions.add(new InvalidFieldException(invalidFieldType));
			}
		}
	}

	/**
	 * Método para validar uma String toda igual.
	 * 
	 * @param string String que será validado.
	 * @param invalidFieldType Tipo de validação
	 */
	public void validarStringIgual(final String string,
			final InvalidFieldType invalidFieldType) {
		if (string.length() > 0) {
			int total = 0;
			// Compara a quantidade de vezes que um caractere da String é igual
			// ao primeiro.
			for (int i = 1; i < string.length(); i++) {
				if (string.charAt(i) == string.charAt(0)) {
					total++;
				}
			}
			// Se a quantidade de caracteres iguais for igual ao tamanho da
			// String -
			// 1, pois o primeiro não é comparado, então a String é toda igual.
			if (total == (string.length() - 1)) {
				exceptions.add(new InvalidFieldException(invalidFieldType));
			}
		}
	}

	/**
	 * Valida se o primeiro número é menor que o segundo número.
	 * 
	 * @param numeroInicio Primeiro Número
	 * @param numeroFim  Segundo Número
	 */
	public void validateNumbers(final Number numeroInicio,
			final Number numeroFim) {
		validateNumbers(numeroInicio, numeroFim, null);
	}

	/**
	 * Valida se o primeiro número é menor que o segundo número.
	 * 
	 * @param numeroInicio Primeiro Número
	 * @param numeroFim Segundo Número
	 * @param mensagem Mensagem que será exibida para o usuário.
	 */
	public void validateNumbers(final Number numeroInicio,
			final Number numeroFim, final String mensagem) {
		Double primeiro = (Double) numeroInicio;
		Double segundo = (Double) numeroFim;
		if ((primeiro == null) || (segundo == null)
				|| (segundo.compareTo(primeiro) < 0)) {
			// || (segundo.equals(primeiro)) || (segundo.compareTo(primeiro) <=
			// 0) ) {
			if (mensagem == null) {
				exceptions.add(new CompareDateException());
			} else {
				exceptions.add(new CompareDateException(mensagem));
			}
		}
	}

}
