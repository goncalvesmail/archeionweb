package br.com.archeion.bd;

/**
 * 
 * Informa qual a Unidade de Persistência que está sendo utilizada.
 * 
 * 
 */
public class UnidadePersistencia {
	
	/**
	 * Unidade de Persistência utilizada.
	 */
	public static ListaUnidadesPersistencia unidadePersistencia;

	/**
	 * @see java.lang.Object#toString()
	 * @return String
	 */
	@Override
	public String toString() {
		return unidadePersistencia.getDescricao();
	}
}
