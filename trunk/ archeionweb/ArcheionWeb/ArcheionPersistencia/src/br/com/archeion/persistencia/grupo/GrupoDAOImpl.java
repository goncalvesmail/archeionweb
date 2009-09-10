package br.com.archeion.persistencia.grupo;

import java.util.List;

import br.com.archeion.modelo.grupo.Grupo;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class GrupoDAOImpl extends JpaGenericDAO<Grupo, Long> implements GrupoDAO {
	
	@SuppressWarnings("unchecked")
	public Grupo findByName(String name) {
		Grupo retorno = null;
		List<Grupo> lista = getJpaTemplate()
			.find("select OBJECT(g) from Grupo g where g.nome = '"+name+"'");
		
		if ( lista!=null && lista.size()>0 )
			retorno = lista.get(0);
		return retorno;
	}

	@SuppressWarnings("unchecked")
	public List<Grupo> findById(List<Grupo> listaId) {

		List<Grupo> retorno = null;
		
		if( listaId.size() > 0 ) {
		
				StringBuffer consulta = new StringBuffer(
						"select OBJECT(g) from Grupo g where g.id in (");
		
				for (Grupo grupos : listaId) {
					consulta.append(grupos.getId().toString());
					consulta.append(",");
				}
		
				consulta.replace(consulta.length() - 1, consulta.length(), ")");
		
				retorno = getJpaTemplate().find(consulta.toString());

		}		
					
		return retorno;

	}
	
}
