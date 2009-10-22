package br.com.archeion.persistencia.pasta;

import java.util.Date;
import java.util.List;

import br.com.archeion.modelo.SituacaoExpurgo;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.persistencia.GenericDAO;

public interface PastaDAO extends GenericDAO<Pasta, Long> {
	
	List<Pasta> findByEmpresaLocal(int emp, int local);
	Pasta findByTitulo(String titulo);
	Pasta findByTituloLocalEmpresa(String titulo,String nomeLocal, String nomeEmpresa);
	List<Pasta> consultaEtiquetaPasta(String where);
	List<Pasta> consultaPermanenteRecolhimento(Local local);
	List<Pasta> consultaTemporarioRecolhimento(Local local);
	List<Pasta> findByEmpresaLocalExpurgo(int emp, int local);
	List<Pasta> findPastaAtivasEmprestimo();
	List<Pasta> findByEmpresaLocalEmprestimo(Long emp, Long local);
	List<Pasta> findByEmpresaLocalSituacao(int emp, int local, SituacaoExpurgo situacao);
	List<Pasta> consultaTemporarioRecolhimentoIntervalo(Empresa empresa, Local local, Date inicio, Date fim);
	List<Pasta> consultaPermanenteRecolhimentoIntervalo(Empresa empresa, Local local, Date inicio, Date fim);
	List<Pasta> findByLocalItemDocumental(long item, long local);
	
}
