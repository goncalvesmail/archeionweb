package br.com.archeion.persistencia;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

public interface GenericDAO<T, ID extends Serializable> {

	List<T> findAll();

	T findById(ID id);

	T merge(T entity);

	T persist(T entity);

	void refresh(T entity);

	void remove(T entity);

	void removeById(ID id);
	
	Connection getConnection();
}