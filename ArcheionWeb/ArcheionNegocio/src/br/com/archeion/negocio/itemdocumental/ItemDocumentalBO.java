package br.com.archeion.negocio.itemdocumental;

import java.util.HashMap;
import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.itemdocumental.ItemDocumental;
import br.com.archeion.util.Log;

public interface ItemDocumentalBO {
	
	@Secured({ "ROLE_BUSCAR_ITEM_DOCUMENTAL" })
	List<ItemDocumental> findAll();
	
	@Secured({ "ROLE_BUSCAR_ITEM_DOCUMENTAL" })
	ItemDocumental findById(Long id);
	
	@Secured({ "ROLE_ATUALIZAR_ITEM_DOCUMENTAL" })
	@Transactional
	@Log(descricao="Alteração")
	ItemDocumental merge(ItemDocumental item) throws BusinessException, CadastroDuplicadoException;
	
	@Secured({ "ROLE_REMOVER_ITEM_DOCUMENTAL" })
	@Transactional
	@Log(descricao="Exclusão")
	void remove(ItemDocumental item) throws BusinessException;
	
	@Secured({ "ROLE_INCLUIR_ITEM_DOCUMENTAL" })
	@Transactional
	@Log(descricao="Inclusão")
	ItemDocumental persist(ItemDocumental item) throws CadastroDuplicadoException;
	
	@Secured({ "ROLE_IMPRIMIR_ITEM_DOCUMENTAL" })
	Relatorio getRelatorio(HashMap<String, Object> parameters, String localRelatorio);
}
