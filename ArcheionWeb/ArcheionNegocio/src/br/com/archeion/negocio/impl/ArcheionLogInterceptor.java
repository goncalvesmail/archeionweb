package br.com.archeion.negocio.impl;

import java.lang.annotation.Annotation;
import java.util.Date;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.annotation.Secured;
import org.acegisecurity.context.SecurityContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import br.com.archeion.jsf.Util;
import br.com.archeion.modelo.log.Log;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.negocio.log.LogBusiness;

public class ArcheionLogInterceptor implements MethodInterceptor {
	private Logger log = Logger.getLogger(ArcheionLogInterceptor.class);
	private LogBusiness logBO;

	public Object invoke(MethodInvocation method) throws Throwable {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if( auth != null ) {
			
			boolean isLoged = false;
			String descricao = "";
			Annotation[] annotations = method.getMethod().getDeclaredAnnotations();
			for ( Annotation a:annotations ) {
				System.out.println(a.annotationType());
				if ( a.annotationType().equals(br.com.archeion.util.Log.class) ) {
					isLoged = true;
					descricao = ((br.com.archeion.util.Log)a).descricao();
					break;
				}
			}
			
			if ( isLoged ) {
				logBO =  (LogBusiness) Util.getSpringBean("logBusiness");
				Log log = new Log();
				Usuario user = (Usuario)auth.getPrincipal();				
				log.setUsuario(user);
				log.setData(new Date());
				
				
				String className = method.getMethod().toString();
				className = className.substring(0, className.indexOf(method.getMethod().getName())-1);
				className = className.substring(className.lastIndexOf('.')+1);
				className = className.substring(0,className.lastIndexOf("BO"));
				
				StringBuffer acao = new StringBuffer("["+className+"] ");
				acao.append(descricao);
				acao.append(":");				
				Object args[] = method.getArguments();
				acao.append(" (");
				for(Object a:args) {
					acao.append(a);
					acao.append(",");
				}
				if ( !acao.toString().endsWith("(") )
					acao.deleteCharAt(acao.length()-1);
				acao.append(")");
				log.setAcao(acao.toString());
				
				logBO.persist(log);			
			}
		}		

		
		Secured secured = method.getMethod().getAnnotation(Secured.class);
		StringBuffer str = new StringBuffer();

		if( secured!=null ) {
			String[] funcionalidades = secured.value();
			for (String string : funcionalidades) {
				str.append(string+";");
			}
		}
		try {
			log.info("Usuário acessando funcionalidade: " + str);
			log.info("Permissoes do usuário: " + permissoesDoUsuario());
			return method.proceed();
		}
		catch(Throwable ex) {
			log.info("Erro ao acessar a funcionalidade: \n" + str, ex);
			throw ex;
		}
	}

	private String permissoesDoUsuario()
	{
		String perm = "[";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if( auth!=null )
		{
			GrantedAuthority[] autorizacoes = auth.getAuthorities();
			for (GrantedAuthority autorizacao : autorizacoes) {
				perm += autorizacao.getAuthority()+","; 
			}
			return perm.substring(0, perm.length()-1)+"]";
		}
		return "N/A";
	}

	public void setLogBO(LogBusiness logBO) {
		this.logBO = logBO;
	}
	
}
