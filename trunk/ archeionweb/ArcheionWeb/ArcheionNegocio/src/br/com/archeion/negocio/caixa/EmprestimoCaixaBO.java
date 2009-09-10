package br.com.archeion.negocio.caixa;

import java.util.Date;
import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.modelo.caixa.EmprestimoCaixa;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.util.Log;


public interface EmprestimoCaixaBO {	

	@Secured({ "ROLE_BUSCAR_EMPRESTIMO_CAIXA" })
	List<EmprestimoCaixa> findEmprestadas(Caixa caixa, Usuario usuario, Date data);
	
	@Secured({ "ROLE_BUSCAR_EMPRESTIMO_CAIXA" })
	List<EmprestimoCaixa> findDevolvidas(Caixa caixa, Usuario usuario, Date data);
	
	@Secured({ "ROLE_BUSCAR_EMPRESTIMO_CAIXA" })
	List<EmprestimoCaixa> findAll();
	
	@Secured({ "ROLE_BUSCAR_EMPRESTIMO_CAIXA" })
	EmprestimoCaixa findById(Long id);
	
	@Secured({ "ROLE_ATUALIZAR_EMPRESTIMO_CAIXA" })
	@Transactional
	@Log(descricao="Alteração")
	EmprestimoCaixa merge(EmprestimoCaixa emprestimoCaixa) throws BusinessException, CadastroDuplicadoException;
	
	@Secured({ "ROLE_REMOVER_EMPRESTIMO_CAIXA" })
	@Transactional
	@Log(descricao="Exclusão")
	void remove(EmprestimoCaixa emprestimoCaixa) throws BusinessException;
	
	@Secured({ "ROLE_INCLUIR_EMPRESTIMO_CAIXA" })
	@Transactional
	@Log(descricao="Inclusão")
	EmprestimoCaixa persist(EmprestimoCaixa emprestimoCaixa) throws CadastroDuplicadoException, BusinessException;
	
}
