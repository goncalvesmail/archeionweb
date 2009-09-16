package br.com.archeion.persistencia.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import br.com.archeion.persistencia.GenericDAO;
import br.com.archeion.util.UnidadePersistencia;

public class JpaGenericDAO<T, ID extends Serializable> extends JpaDaoSupport
		implements GenericDAO<T, ID> {

	private Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public JpaGenericDAO() {
		setJpaTemplate(new JpaTemplate(Persistence
				.createEntityManagerFactory(UnidadePersistencia.unidadePersistencia.getDescricao())));
		getJpaTemplate().afterPropertiesSet();
		Class clazz = getClass();
		while (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
			clazz = clazz.getSuperclass();
		}
		this.persistentClass = (Class<T>) ((ParameterizedType) clazz
				.getGenericSuperclass()).getActualTypeArguments()[0];
		try {
			initDao();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		final String sql = "select o from "
				+ this.persistentClass.getSimpleName() + " o";
		return getJpaTemplate().find(sql);
	}

	public T findById(final ID id) {
		T o = getJpaTemplate().find(getPersistentClass(), id);
		if ( o!=null) refresh(o);
		return o;
	}

	protected Class<T> getPersistentClass() {
		return persistentClass;
	}

	@Transactional
	public T merge(final T entity) {
		T merge = getJpaTemplate().merge(entity);
		return merge;
	}

	@Transactional
	public T persist(final T entity) {
		getJpaTemplate().persist(entity);
		return entity;
	}

	@Transactional
	public void refresh(final T entity) {
		getJpaTemplate().refresh(entity);
	}

	@Transactional
	public void remove(final T entity) {
		getJpaTemplate().remove(entity);
	}

	@Transactional
	public void removeById(final ID id) {
		final T entity = findById(id);
		remove(entity);
	}
	
	public Connection getConnection(){
		EntityManager em = getJpaTemplate().getEntityManagerFactory().createEntityManager();
		
		return ((oracle.toplink.essentials.ejb.cmp3.EntityManager) em)
		.getServerSession().getDefaultConnectionPool().
		acquireConnection().getConnection();
	}
}