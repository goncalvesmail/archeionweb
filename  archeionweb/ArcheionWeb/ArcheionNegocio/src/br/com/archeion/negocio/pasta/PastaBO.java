package br.com.archeion.negocio.pasta;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import util.Relatorio;

import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.SituacaoExpurgo;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.util.Log;

public interface PastaBO {
	
	@Secured({ "ROLE_BUSCAR_PASTA" })
	List<Pasta> consultaPermanenteRecolhimentoIntervalo(Empresa empresa, Local local, Date inicio, Date fim);
	
	@Secured({ "ROLE_BUSCAR_PASTA" })
	List<Pasta> consultaTemporarioRecolhimentoIntervalo(Empresa empresa, Local local, Date inicio, Date fim);

	@Secured({ "ROLE_BUSCAR_PASTA" })
	List<Pasta> findByEmpresaLocalExpurgo(int emp, int local);
	
	@Secured({ "ROLE_BUSCAR_PASTA" })
	List<Pasta> consultaTemporarioRecolhimento(Local local);
	
	@Secured({ "ROLE_BUSCAR_PASTA" })
	List<Pasta> consultaPermanenteRecolhimento(Local local);
	
	@Secured({ "ROLE_BUSCAR_PASTA" })
	List<Pasta> findAll();
	
	@Secured({ "ROLE_BUSCAR_PASTA" })
	Pasta findById(Long id);
	
	@Secured({ "ROLE_BUSCAR_PASTA" })
	Pasta findByTitulo(String titulo);
	
	@Secured({ "ROLE_BUSCAR_PASTA" })
	Pasta findByTituloLocalEmpresa(String titulo,String nomeLocal, String nomeEmpresa);
	
	@Secured({ "ROLE_BUSCAR_PASTA" })
	List<Pasta> consultaEtiquetaPasta(String where);
	
	@Secured({ "ROLE_BUSCAR_PASTA" })
	List<Pasta> findByEmpresaLocal(int emp, int local);

	@Secured({ "ROLE_BUSCAR_PASTA" })
	List<Pasta> findByEmpresaLocalSituacao(int emp, int local, SituacaoExpurgo situacao);
	
	@Secured({ "ROLE_ATUALIZAR_PASTA" })
	@Transactional
	@Log(descricao="Alteração")
	Pasta merge(Pasta pasta) throws BusinessException, CadastroDuplicadoException;
	
	@Secured({ "ROLE_REMOVER_PASTA" })
	@Transactional
	@Log(descricao="Exclusão")
	void remove(Pasta pasta) throws BusinessException;
	
	@Secured({ "ROLE_INCLUIR_PASTA" })
	@Transactional
	@Log(descricao="Inclusão")
	Pasta persist(Pasta pasta) throws CadastroDuplicadoException, BusinessException;
	
	@Secured({ "ROLE_IMPRIMIR_PASTA" })
	@Transactional
	@Log(descricao="Impressão")
	Relatorio getRelatorio(HashMap<String, Object> parameters,
			String localRelatorio);
	
	List<Pasta> findPastaAtivasEmprestimo();
	List<Pasta> findByEmpresaLocalEmprestimo(Long emp, Long local);
}
