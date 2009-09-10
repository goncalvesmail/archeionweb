package br.com.archeion.negocio.ttd;

import java.util.HashMap;
import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.ttd.TTD;
import br.com.archeion.util.Log;

public interface TTDBO {
	
	@Secured({ "ROLE_BUSCAR_TTD" })
	List<TTD> findAll();
	
	@Secured({ "ROLE_BUSCAR_TTD" })
	TTD findById(Long id);
	
	@Secured({ "ROLE_BUSCAR_TTD" })
	List<TTD> findByEmpresaLocalItemDocumental(int emp, int local, int item);
	
	@Secured({ "ROLE_BUSCAR_TTD" })
	List<TTD> findByEvento(Long idEvento);
	
	@Secured({ "ROLE_ATUALIZAR_TTD" })
	@Transactional
	@Log(descricao="Alteração")
	TTD merge(TTD ttd) throws BusinessException, CadastroDuplicadoException;
	
	@Secured({ "ROLE_REMOVER_TTD" })
	@Transactional
	@Log(descricao="Exclusão")
	void remove(TTD ttd) throws BusinessException;
	
	@Secured({ "ROLE_INCLUIR_TTD" })
	@Transactional
	@Log(descricao="Inclusão")
	TTD persist(TTD ttd) throws CadastroDuplicadoException, BusinessException;
	
	@Secured({ "ROLE_IMPRIMIR_TTD" })
	@Transactional
	Relatorio getRelatorio(HashMap<String, Object> parameters, String localRelatorio);

}
