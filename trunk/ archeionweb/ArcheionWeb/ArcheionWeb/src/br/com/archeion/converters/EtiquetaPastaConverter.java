package br.com.archeion.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import br.com.archeion.jsf.Util;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.negocio.pasta.PastaBO;

public class EtiquetaPastaConverter implements Converter {

	private PastaBO pastaBO = (PastaBO) Util.getSpringBean("pastaBO");
	
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2)
			throws ConverterException {
		String result[] = arg2.split("&");
		Pasta func = pastaBO.findByTituloNomeEmpresa(result[1], result[0]);
		
		return func;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ConverterException {
		Pasta p = (Pasta)arg2;
		String result = p.getLocal().getEmpresa().getNome()+"&"+p.getTitulo();
		return result;
	}

}
