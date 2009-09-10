/**
 * 
 */
package br.com.archeion.converters;

import java.text.NumberFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * 
 * Conversor para DOUBLE 00.00 <=> 00,00
 *  
 */
public class DoubleConverter implements Converter {

	/**
	 * O DOUBLE será passado da camada WEB para a de negócio. <BR />
	 * 
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 * 
	 * @param context
	 *            contexto.
	 * @param component
	 *            Component
	 * @param value
	 *            Valor
	 * @return Double
	 */
	public Object getAsObject(final FacesContext context,
			final UIComponent component, final String value) {
		Double object = null;
		String temp = null;
		if (value != null && !value.trim().equals("")) {
			try {
				temp = value.replaceAll("\\.", "");
				temp = temp.replace(",", ".");
				object = new Double(temp);
			} catch (IllegalArgumentException e) {
				throw new ConverterException(e);
			}
		}
		return object;
	}

	/**
	 * O DOUBLE será passado da camada de negócio para a WEB. <BR />
	 * 
	 * Formatar DOUBLE.
	 * 
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 * 
	 * @param context
	 *            contexto.
	 * @param component
	 *            Component
	 * @param object
	 *            Valor
	 * @return DOUBLE formatado
	 */
	public String getAsString(final FacesContext context,
			final UIComponent component, final Object object) {
		String value = null;
		if (object != null && !object.toString().trim().equals("")) {
			if (object instanceof Double) {

				/* Retira a notação científica do número */
			    NumberFormat format = NumberFormat.getInstance();   
				value = object.toString();
			    Double v1 = Double.parseDouble(value);   
			    value = format.format(v1);   

				/* Retira os pontos do número */
			    value = value.replaceAll("\\.", "");
				
				/* Acrescenta uma vírgula no fim do número, caso ela não exista */
			    int pos = value.indexOf(",");
			    if (pos == -1) {
			    	value += ",";
			    }
			    
				/* Acrescenta zeros depois da vírgula, caso eles não existam */
				switch ( value.substring( value.indexOf(",") + 1).length() ) {
				case 0:
					value += "00";
					break;
				case 1:
					value += "0";
					break;
				default:
					break;
				}

				/* Separa a parte fracionária do número */
				StringBuilder parteFracionaria = new StringBuilder();
				parteFracionaria.append(value.substring(value.indexOf(",")));

				/* Separa a parte inteira do número */
				StringBuilder parteInteira = new StringBuilder();
				parteInteira.append(value.substring(0, value.indexOf(",")));

				/* Inverte a string da parte inteira do número para iniciar 
				 * o processo de inserção dos pontos */
				parteInteira.reverse();

				/* Calcula o total de pontos existentes no número */
				/* Exemplos: 1.000,00 (um ponto) */
				/*           1.000.000,00 (dois pontos) */
				/*           1.000.000.000,00 (três pontos) */
				int totalDePontos;
				int tamanho = parteInteira.length();
				if ( tamanho < 3 ) {
					totalDePontos = 0;
				}
				if ( tamanho % 3 != 0 ) {
					totalDePontos = ( tamanho )/3;
				} else { 
					totalDePontos = ( tamanho )/3 - 1;
				}

				/* Armazena as posições possíveis dos pontos no número */
				/* Exemplos: 1.000,00 --> 000.1 (posição 3) */
				/*           1.000.000,00 --> 000.000.1 (posições 3 e 6) */
				/*           1.000.000.000,00 --> 000.000.000.1 (posições 3, 6 e 9) */
				int[] pontos = null;
				
				int posicao = 3;
				if (tamanho < 3) {
					return value.toString();
				} else {
					pontos = new int[totalDePontos];
					for (int i = 0; i < totalDePontos; i++) {
						pontos[i] = posicao;
						posicao += 3;
					}
				}	
				/* Coloca os pontos nas posições corretas do número */
				/* Exemplos: 1000,00 --> 1.000,00 */
				/*           1000000,00 --> 1.000.000,00 */
				/*           1000000000,00 --> 1.000.000.000,00 */
				int start = 0;
				StringBuilder result = new StringBuilder();
			    for (int i = 0; i < totalDePontos; i++) {
					int end = pontos[i];
					result.append(parteInteira.substring(start, end));
					result.append(".");
					start = end;
				}

				/* Monta a string resultante, o número com seus pontos */
			    result.append(parteInteira.substring(start));
				result.reverse();
				result.append(parteFracionaria);
				return result.toString();
			} else {
				throw new RuntimeException(
						"Não é possível dar um cast para String no objeto recebido.");
			}
		}
		return value;
	}

}
