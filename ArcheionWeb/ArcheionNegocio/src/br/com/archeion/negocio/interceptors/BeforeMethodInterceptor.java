/**
 * 
 */
package br.com.archeion.negocio.interceptors;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

/**
 * Interceptor chamado antes de executar o método.
 * 
 */
public class BeforeMethodInterceptor implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {

	private static Logger log = Logger.getLogger(BeforeMethodInterceptor.class);

	public void afterReturning(final Object value, final Method method, final Object[] params, final Object target)
			throws Throwable {
		log.info("Class: " + target.getClass().getSimpleName() + " - Finalizando método: " + method.getName());
	}

	public void afterThrowing(final Method method, final Object[] params, final Object target, final Throwable ex) {
		log.info("Class: " + target.getClass() + " - Ocorreu exceção no método: " + method.getName()
				+ "\nA exceção é: " + ex.getMessage());
	}

	public void before(final Method method, final Object[] params, final Object target) throws Throwable {
		log.info("Class: " + target.getClass() + " - Iniciando método: " + method.getName());
	}
}
