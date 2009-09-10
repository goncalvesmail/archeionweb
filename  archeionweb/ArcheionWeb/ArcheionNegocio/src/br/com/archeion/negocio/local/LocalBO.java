package br.com.archeion.negocio.local;

import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.util.Log;

public interface LocalBO {	
	
	@Secured({ "ROLE_BUSCAR_LOCAL" })
	List<Local> findAll();
	
	@Secured({ "ROLE_BUSCAR_LOCAL" })
	List<Local> findByEmpresa(Empresa empresa);
	
	@Secured({ "ROLE_BUSCAR_LOCAL" })
	Local findById(Long id);
	
	@Secured({ "ROLE_ATUALIZAR_LOCAL" })
	@Transactional
	@Log(descricao="Alteração")
	Local merge(Local local) throws BusinessException, CadastroDuplicadoException;
	
	@Secured({ "ROLE_REMOVER_LOCAL" })
	@Transactional
	@Log(descricao="Exclusão")
	void remove(Local local) throws BusinessException;
	
	@Secured({ "ROLE_INCLUIR_LOCAL" })
	@Transactional
	@Log(descricao="Inclusão")
	Local persist(Local local) throws CadastroDuplicadoException;
}
