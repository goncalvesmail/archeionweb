package br.com.archeion.negocio.enderecocaixa;

import java.util.HashMap;
import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.enderecocaixa.EnderecoCaixa;
import br.com.archeion.util.Log;

public interface EnderecoCaixaBO {	
	
	@Secured({ "ROLE_BUSCAR_ENDERECO_CAIXA" })
	List<EnderecoCaixa> findAll();
	
	@Secured({ "ROLE_BUSCAR_ENDERECO_CAIXA" })
	EnderecoCaixa findById(Long id);
	
	@Secured({ "ROLE_ATUALIZAR_ENDERECO_CAIXA" })
	@Transactional
	@Log(descricao="Alteração")
	EnderecoCaixa merge(EnderecoCaixa enderecoCaixa) throws BusinessException, CadastroDuplicadoException;
	
	@Secured({ "ROLE_REMOVER_ENDERECO_CAIXA" })
	@Transactional
	@Log(descricao="Exclusão")
	void remove(EnderecoCaixa enderecoCaixa) throws BusinessException;
	
	@Secured({ "ROLE_INCLUIR_ENDERECO_CAIXA" })
	@Transactional
	@Log(descricao="Inclusão")
	EnderecoCaixa persist(EnderecoCaixa enderecoCaixa) throws CadastroDuplicadoException, BusinessException ;
	
	@Secured({ "ROLE_IMPRIMIR_ENDERECO_CAIXA" })
	@Transactional
	Relatorio getRelatorio(HashMap<String, Object> parameters, String localRelatorio);
}
