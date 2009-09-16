package br.com.archeion.persistencia;

import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;

import br.com.archeion.util.UnidadePersistencia;

/**
 * Classe utilizada no Application Context para informar ao Entity Manager
 * Factory qual Unidade de Persistência a aplicação está utilizando.
 * 
 */
public class ArcheionEntityManagerFactoryBean extends LocalEntityManagerFactoryBean {

	
	public ArcheionEntityManagerFactoryBean() {
		super();
		this.setPersistenceUnitName(UnidadePersistencia.unidadePersistencia.getDescricao());
	}

}
