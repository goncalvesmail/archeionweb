package br.com.archeion.negocio.caixa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.SituacaoExpurgo;
import br.com.archeion.modelo.TipoArquivo;
import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.util.Log;

public interface CaixaBO {	

	@Secured({ "ROLE_BUSCAR_CAIXA" })
	List<Caixa> findCaixaAtivasEmprestimo();
	
	@Secured({ "ROLE_BUSCAR_CAIXA" })
	List<Caixa> findCaixaAtivasExpurgo();
	
	@Secured({ "ROLE_BUSCAR_CAIXA" })
	List<Caixa> findVaoOcupados(String nomeVao);
	
	@Secured({ "ROLE_BUSCAR_CAIXA" })
	List<Caixa> findByEndereco(Long id);
	
	@Secured({ "ROLE_BUSCAR_CAIXA" })
	List<Caixa> findAll();
	
	@Secured({ "ROLE_BUSCAR_CAIXA" })
	List<Caixa> findByEmpresaLocal(int emp, int local);

	@Secured({ "ROLE_BUSCAR_CAIXA" })
	List<Caixa> findByEmpresaLocalSituacao(int emp, int local, SituacaoExpurgo situacao); 
	
	@Secured({ "ROLE_BUSCAR_CAIXA" })
	List<Caixa> consultaEtiquetaCaixa(int caixaId, Date date, TipoArquivo tipo);
	
	@Secured({ "ROLE_BUSCAR_CAIXA" })
	Caixa findById(Long id);

	@Secured({ "ROLE_BUSCAR_CAIXA" })
	Caixa findByVaoNumero(String vao, int numero);

	@Secured({ "ROLE_BUSCAR_CAIXA" })
	Caixa findByVaoNumeroAtiva(String vao, int numero);
	
	@Secured({ "ROLE_ATUALIZAR_CAIXA" })
	@Transactional
	@Log(descricao="Alteração")
	Caixa merge(Caixa caixa) throws BusinessException, CadastroDuplicadoException;
	
	@Secured({ "ROLE_REMOVER_CAIXA" })
	@Transactional
	@Log(descricao="Exclusão")
	void remove(Caixa caixa) throws BusinessException;
	
	@Secured({ "ROLE_INCLUIR_CAIXA" })
	@Transactional
	@Log(descricao="Inclusão")
	Caixa persist(Caixa caixa) throws CadastroDuplicadoException, BusinessException;
	
	@Secured({ "ROLE_IMPRIMIR_CAIXA" })
	@Transactional
	Relatorio getRelatorio(HashMap<String, Object> parameters, String localRelatorio);
}
