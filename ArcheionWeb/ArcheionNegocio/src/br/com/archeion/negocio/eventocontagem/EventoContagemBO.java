package br.com.archeion.negocio.eventocontagem;

import java.util.HashMap;
import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.eventocontagem.EventoContagem;
import br.com.archeion.util.Log;

public interface EventoContagemBO {
	
	@Secured({ "ROLE_BUSCAR_EVENTO_CONTAGEM" })
	List<EventoContagem> findAll();
	
	@Secured({ "ROLE_BUSCAR_EVENTO_CONTAGEM" })
	EventoContagem findById(Long id);
	
	@Transactional
	@Secured({ "ROLE_ATUALIZAR_EVENTO_CONTAGEM" })
	@Log(descricao="Alteração")
	EventoContagem merge(EventoContagem item) throws BusinessException, CadastroDuplicadoException;
	
	@Transactional
	@Secured({ "ROLE_REMOVER_EVENTO_CONTAGEM" })
	@Log(descricao="Exclusão")
	void remove(EventoContagem item) throws BusinessException;
	
	@Transactional
	@Secured({ "ROLE_INCLUIR_EVENTO_CONTAGEM" })
	@Log(descricao="Inclusão")
	EventoContagem persist(EventoContagem item) throws CadastroDuplicadoException;
	
	@Transactional
	@Secured({ "ROLE_IMPRIMIR_EVENTO_CONTAGEM" })
	Relatorio getRelatorio(HashMap<String, Object> parameters, String localRelatorio);
}
