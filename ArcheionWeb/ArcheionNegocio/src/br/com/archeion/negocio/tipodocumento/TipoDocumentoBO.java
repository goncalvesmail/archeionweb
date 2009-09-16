package br.com.archeion.negocio.tipodocumento;

import java.util.HashMap;
import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.tipodocumento.TipoDocumento;
import br.com.archeion.util.Log;

public interface TipoDocumentoBO {
	
	@Secured({ "ROLE_BUSCAR_TIPO_DOCUMENTO" })
	List<TipoDocumento> findAll();
	
	@Secured({ "ROLE_BUSCAR_TIPO_DOCUMENTO" })
	TipoDocumento findById(Long id);
	
	@Secured({ "ROLE_ATUALIZAR_TIPO_DOCUMENTO" })
	@Transactional
	@Log(descricao="Alteração")
	TipoDocumento merge(TipoDocumento item) throws BusinessException, CadastroDuplicadoException;
	
	@Secured({ "ROLE_REMOVER_TIPO_DOCUMENTO" })
	@Transactional
	@Log(descricao="Exclusão")
	void remove(TipoDocumento item) throws BusinessException;
	
	@Secured({ "ROLE_INCLUIR_TIPO_DOCUMENTO" })
	@Transactional
	@Log(descricao="Inclusão")
	TipoDocumento persist(TipoDocumento item) throws CadastroDuplicadoException;
	
	@Secured({ "ROLE_IMPRIMIR_TIPO_DOCUMENTO" })
	Relatorio getRelatorio(HashMap<String, Object> parameters, String localRelatorio);
}
