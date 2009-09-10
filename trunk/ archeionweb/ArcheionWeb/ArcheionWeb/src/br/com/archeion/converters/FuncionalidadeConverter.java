package br.com.archeion.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import br.com.archeion.jsf.Util;
import br.com.archeion.modelo.funcionalidade.Funcionalidade;
import br.com.archeion.negocio.funcionalidade.FuncionalidadeBO;


public class FuncionalidadeConverter implements Converter {

	private FuncionalidadeBO funcionalidadeBO = (FuncionalidadeBO) Util.getSpringBean("funcionalidadeBO");

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2)
			throws ConverterException {
		
		Funcionalidade func = funcionalidadeBO.findByDescricao(arg2);
		
		return func;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ConverterException {
		Funcionalidade func = (Funcionalidade)arg2;
		return func.getDescricao();
	}

}
