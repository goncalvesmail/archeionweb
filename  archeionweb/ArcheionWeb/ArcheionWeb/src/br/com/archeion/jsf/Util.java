package br.com.archeion.jsf;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class Util {


	/**
	 * LOG utilizado na classe.
	 */
	private static final Logger LOG = Logger.getLogger(Util.class);
	
	
	public static String getParameter(String param) {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return req.getParameter(param);
	}

	/**
	 * Backing Bean do faces.
	 * 
	 * @param ctx
	 * @param beanName
	 * @return
	 */
	public static Object getManagedBean(FacesContext ctx, String beanName) {
		LOG.info("Recuperando bean: " + beanName);
		Object bean = ctx.getApplication().getELResolver().getValue(ctx.getELContext(), null, beanName);
		LOG.info("Bean recuperado: " + bean);
		return bean;
	}

	/**
	 * 
	 * @param beanName
	 * @return
	 */
	public static Object getManagedBean(String beanName) {
		LOG.info("Recuperando bean: " + beanName);
		return getManagedBean(FacesContext.getCurrentInstance(), beanName);
	}

	/**
	 * Criar um método que tem no bean.
	 * @param action
	 * @param returnType
	 * @param attributes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static MethodExpression createMethodExpression(String action, Class returnType, Class[] attributes) {
		ExpressionFactory ef = FacesContext.getCurrentInstance().getApplication().getExpressionFactory();
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		MethodExpression me = ef.createMethodExpression(elContext, action, returnType, attributes);
		return me;
	}

	/**
	 * Invoca o método acima.
	 * @param mex
	 * @param args
	 * @return
	 */
	public static Object invoke(MethodExpression mex, Object[] args) {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		return mex.invoke(elContext, args);
	}

	public static Object getSpringBean(String beanName) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		ServletContext svCtx = (ServletContext) (externalContext.getContext());
		return WebApplicationContextUtils.getWebApplicationContext(svCtx).getBean(beanName);

	}

	/**
	 * Até agora, é o método que é chamado no filter, servlet.
	 * 
	 * @param servletContext
	 * @param request
	 * @param response
	 * @return
	 */
	public static FacesContext getFacesContext(ServletContext servletContext, ServletRequest request,
			ServletResponse response) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null)
			return facesContext;

		FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder
				.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
		LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder
				.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
		Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
		facesContext = contextFactory.getFacesContext(servletContext, request, response, lifecycle);
		return facesContext;
	}
}
