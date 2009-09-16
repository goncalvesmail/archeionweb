package br.com.archeion.jsf;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.apache.myfaces.application.NavigationHandlerImpl;

public class ArcheionNavigationHandler extends NavigationHandlerImpl
{
	private static final Logger logger = Logger.getLogger(ArcheionNavigationHandler.class);
	
	@Override
	public void handleNavigation(FacesContext fcx, String metodo, String retorno) {
		try
		{
			logger.info("View-ID -> " + fcx.getViewRoot().getViewId());
			logger.info("Metodo -> " + metodo);
			logger.info("Retorno -> " + retorno);
		}
		catch(Exception ex)
		{}

		super.handleNavigation(fcx, metodo, retorno);
	}

}
