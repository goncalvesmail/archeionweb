/**
 * 
 */
package br.com.archeion.persistencia;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TemporalType;


public class GenericQuery<T> implements Query {
	
	public static <T> GenericQuery<T> adaptQuery(final Class<T> klass, final Query query) {
		return new GenericQuery<T>(klass, query);
	}

	private Query query;

	private Class<T> klass;

	public GenericQuery(final Class<T> klass, final Query query) {
		this.klass = klass;
		this.query = query;
	}

	/**
	 * @see javax.persistence.Query#executeUpdate()
	 * 
	 * @return s
	 */
	public int executeUpdate() {
		return query.executeUpdate();
	}

	/**
	 * Isto promete que esta lista conterá apenas elementos do tipo "T".
	 * @see javax.persistence.Query#getResultList()
	 * 
	 * @return s
	 */
	@SuppressWarnings("unchecked")
	public List<T> getResultList() {
		// 
		return Collections.checkedList(query.getResultList(), klass);
	}

	/**
	 * @see javax.persistence.Query#getSingleResult()
	 * 
	 * @return s
	 */
	public T getSingleResult() {
		// isto força o "class cast exception" a ocorrer aqui dentro
		return klass.cast(query.getSingleResult());
	}

	/**
	 * @see javax.persistence.Query#setFirstResult(int)
	 * 
	 * @param startPosition s
	 * @return s
	 */
	public GenericQuery<T> setFirstResult(final int startPosition) {
		return new GenericQuery<T>(klass, query.setFirstResult(startPosition));
	}

	/**
	 * 
	 * @see javax.persistence.Query#setFlushMode(javax.persistence.FlushModeType)
	 * 
	 * @param flushMode s
	 * @return s
	 */
	public GenericQuery<T> setFlushMode(final FlushModeType flushMode) {
		return new GenericQuery<T>(klass, query.setFlushMode(flushMode));
	}

	/**
	 * @see javax.persistence.Query#setHint(java.lang.String, java.lang.Object)
	 * 
	 * @param hintName
	 * @param value
	 * @return
	 */
	public GenericQuery<T> setHint(final String hintName, final Object value) {
		return new GenericQuery<T>(klass, query.setHint(hintName, value));
	}

	public GenericQuery<T> setMaxResults(final int maxResult) {
		return new GenericQuery<T>(klass, query.setMaxResults(maxResult));
	}

	public GenericQuery<T> setParameter(final int position, final Calendar value, final TemporalType temporalType) {
		return new GenericQuery<T>(klass, query.setParameter(position, value, temporalType));
	}

	public GenericQuery<T> setParameter(final int position, final Date value, final TemporalType temporalType) {
		return new GenericQuery<T>(klass, query.setParameter(position, value, temporalType));
	}

	public GenericQuery<T> setParameter(final int position, final Object value) {
		return new GenericQuery<T>(klass, query.setParameter(position, value));
	}

	public GenericQuery<T> setParameter(final String name, final Calendar value, final TemporalType temporalType) {
		return new GenericQuery<T>(klass, query.setParameter(name, value, temporalType));
	}

	public GenericQuery<T> setParameter(final String name, final Date value, final TemporalType temporalType) {
		return new GenericQuery<T>(klass, query.setParameter(name, value, temporalType));
	}

	public GenericQuery<T> setParameter(final String name, final Object value) {
		return new GenericQuery<T>(klass, query.setParameter(name, value));
	}
}