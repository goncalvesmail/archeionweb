package br.com.archeion.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import br.com.archeion.jsf.Util;
import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.negocio.caixa.CaixaBO;

public class EtiquetaCaixaConverter implements Converter {
	
	private CaixaBO caixaBO = (CaixaBO) Util.getSpringBean("caixaBO");

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2)
			throws ConverterException {
		String[] result = arg2.split("-");
		return caixaBO.findByVaoNumeroAtiva(result[0], Integer.parseInt(result[1]));
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ConverterException {
		Caixa p = (Caixa)arg2;
		return p.getTitulo();
	}

}
