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
 * Conversor para CEP.
 
 */
public class CEPConverter implements Converter {

	/**
	 * Expressão Regular do CEP.
	 */
	private static final String[] PATTERN = { "-", "([0-9]{5})([0-9]{3})" };

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 * @param context
	 *            FacesContext
	 * @param ui
	 *            UIComponent
	 * @param value
	 *            String
	 * @return Object
	 */
	public Object getAsObject(final FacesContext context, final UIComponent ui,
			final String value) {
		String object = null;
		try {
			if (value != null) {
				object = value.trim();
				if (!value.equals("")) {
					object = object.replaceAll(PATTERN[0], "");
				}
			}
		} catch (Throwable e) {
			throw new ConverterException(e);
		}
		return object;
	}

	/**
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.Object)
	 * @param context
	 *            FacesContext
	 * @param ui
	 *            UIComponent
	 * @param object
	 *            Object
	 * @return String
	 */
	public String getAsString(final FacesContext context, final UIComponent ui,
			final Object object) {
		String value = null;
		if (object != null && !object.toString().trim().equals("")) {
			if (object instanceof String) {
				value = (String) object;
				try {
					if (object != null) {
						value = value.trim();
						if (!value.equals("")) {
							value = value.replaceAll(PATTERN[1], "$1\\-$2");
						}
					}
				} catch (Throwable e) {
					throw new ConverterException(e);
				}
			} else {
				throw new RuntimeException(
						"Não é possível dar um cast para String no objeto recebido.");
			}
		}
		return value;
	}

}
