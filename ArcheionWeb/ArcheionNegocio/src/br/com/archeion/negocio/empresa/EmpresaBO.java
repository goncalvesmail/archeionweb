package br.com.archeion.negocio.empresa;

import java.util.HashMap;
import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.util.Log;

public interface EmpresaBO {	
	
	@Secured({ "ROLE_BUSCAR_EMPRESA" })
	List<Empresa> findAll();
	
	@Secured({ "ROLE_BUSCAR_EMPRESA" })
	public List<Empresa> findAllInvisivel();
	
	@Secured({ "ROLE_BUSCAR_EMPRESA" })
	List<Empresa> findRoots();
	
	@Secured({ "ROLE_BUSCAR_EMPRESA" })
	Empresa findById(Long id);
	
	@Secured({ "ROLE_BUSCAR_EMPRESA" })
	Empresa findByName(String nome);
	
	@Secured({ "ROLE_ATUALIZAR_EMPRESA" })
	@Transactional
	@Log(descricao="Alteração")
	Empresa merge(Empresa empresa) throws BusinessException, CadastroDuplicadoException;
	
	@Secured({ "ROLE_REMOVER_EMPRESA" })
	@Transactional
	@Log(descricao="Exclusão")
	void remove(Empresa empresa) throws BusinessException;
	
	@Secured({ "ROLE_INCLUIR_EMPRESA" })
	@Transactional
	@Log(descricao="Inclusão")
	Empresa persist(Empresa empresa) throws CadastroDuplicadoException;
	
	@Secured({ "ROLE_IMPRIMIR_EMPRESA" })
	@Transactional
	Relatorio getRelatorio(HashMap<String, Object> parameters, String localRelatorio);
}
