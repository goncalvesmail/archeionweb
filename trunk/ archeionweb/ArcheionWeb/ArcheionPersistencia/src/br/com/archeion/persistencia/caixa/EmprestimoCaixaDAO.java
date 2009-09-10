package br.com.archeion.persistencia.caixa;

import java.util.Date;
import java.util.List;

import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.modelo.caixa.EmprestimoCaixa;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.persistencia.GenericDAO;

public interface EmprestimoCaixaDAO extends GenericDAO<EmprestimoCaixa, Long> {
	
	List<EmprestimoCaixa> findEmprestadas(Caixa caixa, Usuario usuario, Date data);
	List<EmprestimoCaixa> findDevolvidas(Caixa caixa, Usuario usuario, Date data);
	List<EmprestimoCaixa> findByCaixa(Caixa caixa);
	
}
