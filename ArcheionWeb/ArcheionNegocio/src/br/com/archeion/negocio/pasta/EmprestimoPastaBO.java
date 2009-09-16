package br.com.archeion.negocio.pasta;

import java.util.Date;
import java.util.List;

import org.acegisecurity.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.EmprestimoPasta;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.util.Log;


public interface EmprestimoPastaBO {	

	@Secured({ "ROLE_BUSCAR_EMPRESTIMO_PASTA" })
	List<EmprestimoPasta> findEmprestadas(Empresa empresa, Local local, Usuario usuario, Date data);
	
	@Secured({ "ROLE_BUSCAR_EMPRESTIMO_PASTA" })
	List<EmprestimoPasta> findDevolvidas(Empresa empresa, Local local, Usuario usuario, Date data);
	
	@Secured({ "ROLE_BUSCAR_EMPRESTIMO_PASTA" })
	List<EmprestimoPasta> findAll();
	
	@Secured({ "ROLE_BUSCAR_EMPRESTIMO_PASTA" })
	EmprestimoPasta findById(Long id);
	
	@Secured({ "ROLE_ATUALIZAR_EMPRESTIMO_PASTA" })
	@Transactional
	@Log(descricao="Alteração")
	EmprestimoPasta merge(EmprestimoPasta emprestimoCaixa) throws BusinessException, CadastroDuplicadoException;
	
	@Secured({ "ROLE_REMOVER_EMPRESTIMO_PASTA" })
	@Transactional
	@Log(descricao="Exclusão")
	void remove(EmprestimoPasta emprestimoCaixa) throws BusinessException;
	
	@Secured({ "ROLE_INCLUIR_EMPRESTIMO_PASTA" })
	@Transactional
	@Log(descricao="Inclusão")
	EmprestimoPasta persist(EmprestimoPasta emprestimoCaixa) throws CadastroDuplicadoException, BusinessException;
	
}
