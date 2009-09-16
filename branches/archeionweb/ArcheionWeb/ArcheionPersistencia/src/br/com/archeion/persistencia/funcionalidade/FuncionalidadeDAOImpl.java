package br.com.archeion.persistencia.funcionalidade;

import java.util.List;

import br.com.archeion.modelo.funcionalidade.Funcionalidade;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class FuncionalidadeDAOImpl extends JpaGenericDAO<Funcionalidade, Long> implements FuncionalidadeDAO {

	public Funcionalidade findByName(String nome) {
		Funcionalidade retorno = null;
		List<Funcionalidade> lista = getJpaTemplate()
			.find("select OBJECT(g) from Funcionalidade g where g.nome = '"+nome+"'");
		
		if ( lista!=null && lista.size()>0 )
			retorno = lista.get(0);
		return retorno;
	}

	public Funcionalidade findByDescricao(String descricao) {
		Funcionalidade retorno = null;
		List<Funcionalidade> lista = getJpaTemplate()
			.find("select OBJECT(g) from Funcionalidade g where g.descricao = '"+descricao+"'");
		
		if ( lista!=null && lista.size()>0 )
			retorno = lista.get(0);
		return retorno;
	}
	
}
