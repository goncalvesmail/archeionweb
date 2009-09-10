package br.com.archeion.negocio.documento;

import java.util.HashMap;
import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import util.Relatorio;

import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.exception.CompareDateException;
import br.com.archeion.modelo.documento.Documento;
import br.com.archeion.util.Log;

public interface DocumentoBO {	

	@Secured({ "ROLE_BUSCAR_DOCUMENTO" })
	List<Documento> findByPasta(Long id);
	
	@Secured({ "ROLE_BUSCAR_DOCUMENTO" })
	List<Documento> findByTipo(Long id);
	
	@Secured({ "ROLE_BUSCAR_DOCUMENTO" })
	List<Documento> findByLocal(Long id);
	
	@Secured({ "ROLE_BUSCAR_DOCUMENTO" })
	List<Documento> consultarDocumento(String where);
	
	@Secured({ "ROLE_BUSCAR_DOCUMENTO" })
	List<Documento> findAll();
	
	@Secured({ "ROLE_BUSCAR_DOCUMENTO" })
	Documento findById(Long id);
	
	@Secured({ "ROLE_ATUALIZAR_DOCUMENTO" })
	@Transactional
	@Log(descricao="Alteração")
	Documento merge(Documento documento) throws BusinessException, CompareDateException, CadastroDuplicadoException;
	
	@Secured({ "ROLE_REMOVER_DOCUMENTO" })
	@Transactional
	@Log(descricao="Exclusão")
	void remove(Documento documento) throws BusinessException;
	
	@Secured({ "ROLE_INCLUIR_DOCUMENTO" })
	@Transactional
	@Log(descricao="Inclusão")
	Documento persist(Documento documento)throws BusinessException, CadastroDuplicadoException;
	
	@Secured({ "ROLE_IMPRIMIR_DOCUMENTO" })
	@Transactional
	Relatorio getRelatorio(HashMap<String, Object> parameters, String localRelatorio);
}
