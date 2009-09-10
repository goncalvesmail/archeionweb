package br.com.archeion.persistencia.funcionalidade;

import br.com.archeion.modelo.funcionalidade.Funcionalidade;
import br.com.archeion.persistencia.GenericDAO;

public interface FuncionalidadeDAO extends GenericDAO<Funcionalidade, Long> {
	
	Funcionalidade findByName(String nome);
	Funcionalidade findByDescricao(String descricao);
	
}
