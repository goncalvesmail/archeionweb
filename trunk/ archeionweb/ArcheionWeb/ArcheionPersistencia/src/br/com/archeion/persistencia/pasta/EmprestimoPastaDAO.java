package br.com.archeion.persistencia.pasta;

import java.util.Date;
import java.util.List;

import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.EmprestimoPasta;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.persistencia.GenericDAO;

public interface EmprestimoPastaDAO extends GenericDAO<EmprestimoPasta, Long> {
	
	List<EmprestimoPasta> findEmprestadas(Empresa empresa, Local local, Usuario usuario, Date data);
	List<EmprestimoPasta> findDevolvidas(Empresa empresa, Local local, Usuario usuario, Date data);
	List<EmprestimoPasta> findByPasta(Pasta pasta);
	
}
