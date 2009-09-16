package br.com.archeion.persistencia.caixa;

import java.util.Date;
import java.util.List;

import br.com.archeion.modelo.SituacaoExpurgo;
import br.com.archeion.modelo.TipoArquivo;
import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.persistencia.GenericDAO;

public interface CaixaDAO extends GenericDAO<Caixa, Long> {
	
	List<Caixa> findVaoOcupados(String nomeVao);
	List<Caixa> findByEmpresaLocal(int emp, int local);
	List<Caixa> findByEndereco(Long id);
	List<Caixa> consultaEtiquetaCaixa(int caixaId, Date date, TipoArquivo tipo);
	Caixa findByVaoNumero(String vao, int numero);
	Caixa findByVaoNumeroAtiva(String vao, int numero);
	List<Caixa> findCaixaAtivasExpurgo();
	List<Caixa> findCaixaAtivasEmprestimo();
	List<Caixa> findByEmpresaLocalSituacao(int emp, int local, SituacaoExpurgo situacao);
	
}
