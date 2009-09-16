/**
 * 
 */
package br.com.archeion.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * 
 * Conversor para formatação do tipo Boolean.

 */
public class BooleanSimNaoConverter implements Converter {

	/**
	 * Retorna o objeto do tipo Boolean.
	 * 
	 * @param context
	 *            FacesContext.
	 * @param comp
	 *            UIComponent.
	 * @param value
	 *            Valor.
	 * 
	 * @return Object do tipo Boolean.
	 */
	public Object getAsObject(final FacesContext context,
			final UIComponent comp, final String value) {
		Boolean object = null;
		String valor = value;
		try {
			if (valor != null) {
				valor = valor.trim();
				if (!valor.equals("")) {
					if (valor.equals("on") || valor.equals("1")
							|| valor.toLowerCase().equals("true")) {
						object = true;
					} else {
						object = false;
					}
				}
			}
		} catch (Throwable e) {
			throw new ConverterException(e);
		}
		return object;
	}

	/**
	 * Retorna o Boolean no formato de String (Sim ou Não).
	 * 
	 * @param context
	 *            FacesContext.
	 * @param comp
	 *            UIComponent.
	 * @param object
	 *            Objeto.
	 * 
	 * @return String Ativo/Inativo.
	 */
	public String getAsString(final FacesContext context,
			final UIComponent comp, final Object object) {
		String value = "Não Informado";
		try {
			if (object != null) {
				Boolean flag = false;
				if (object instanceof java.lang.Boolean) {
					flag = (Boolean) object;
				} else if (object instanceof java.lang.String) {
					flag = object.toString().matches("(1|true|on)");
				}
				if (flag) {
					value = "Sim";
				} else {
					value = "Não";
				}
			}
		} catch (Exception e) {
			throw new ConverterException(e);
		}
		return value;
	}

}
