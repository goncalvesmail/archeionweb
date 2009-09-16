package br.com.archeion.modelo;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;


@MappedSuperclass
public abstract class AbstractTO implements TransferObject {

	private static final long serialVersionUID = 899855174388357250L;
	
	/**
	 * Version ID
	 */
	@Version
	@Column(name = "VERSION_ID")
	private Integer versionId;

	/**
	 * Retornar o tipo da classe.
	 * 
	 * @return Class
	 */
	@SuppressWarnings("unchecked")
	@Transient
	public Class getClassType() {
		return this.getClass();
	}

	/**
	 * @return O campo versionId
	 */
	public Integer getVersionId() {
		return versionId;
	}

	/**
	 * @param versionId
	 *            seta o campo versionId
	 */
	public void setVersionId(final Integer versionId) {
		this.versionId = versionId;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * @return int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		if (versionId == null) {
			result += 0;
		} else {
			result += versionId.hashCode();
		}
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj
	 *            Object.
	 * @return boolean
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass() != obj.getClass()) {
			return false;
		}
		final AbstractTO other = (AbstractTO) obj;
		if (versionId == null) {
			if (other.versionId != null) {
				return false;
			}
		} else if (!versionId.equals(other.versionId)) {
			return false;
		}
		return true;
	}
}
