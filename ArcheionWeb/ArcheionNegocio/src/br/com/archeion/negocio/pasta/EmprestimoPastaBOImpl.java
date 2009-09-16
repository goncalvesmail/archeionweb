package br.com.archeion.negocio.pasta;

import java.util.Date;
import java.util.List;

import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.modelo.pasta.EmprestimoPasta;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.persistencia.pasta.EmprestimoPastaDAO;


public class EmprestimoPastaBOImpl implements EmprestimoPastaBO {

	private EmprestimoPastaDAO emprestimoPastaDAO;

	public EmprestimoPasta persist(EmprestimoPasta emprestimoCaixa) throws CadastroDuplicadoException, BusinessException {
		validar(emprestimoCaixa);
		return emprestimoPastaDAO.persist(emprestimoCaixa);
	}

	public List<EmprestimoPasta> findAll() {
		return emprestimoPastaDAO.findAll();
	}

	public List<EmprestimoPasta> findDevolvidas(Empresa empresa, Local local, Usuario usuario, Date data) {
		return emprestimoPastaDAO.findDevolvidas(empresa, local, usuario, data);
	}

	public List<EmprestimoPasta> findEmprestadas(Empresa empresa, Local local, Usuario usuario, Date data) {
		return emprestimoPastaDAO.findEmprestadas(empresa, local, usuario, data);
	}
	
	public void setEmprestimoPastaDAO(EmprestimoPastaDAO emprestimoCaixaDAO) {
		this.emprestimoPastaDAO = emprestimoCaixaDAO;
	}

	public EmprestimoPasta findById(Long id) {
		return emprestimoPastaDAO.findById(id);
	}

	public EmprestimoPasta merge(EmprestimoPasta emprestimoCaixa) throws BusinessException, CadastroDuplicadoException {
		validar(emprestimoCaixa);
		return emprestimoPastaDAO.merge(emprestimoCaixa);
	}

	public void remove(EmprestimoPasta emprestimoCaixa) throws BusinessException {
		emprestimoPastaDAO.remove(emprestimoCaixa);		
	}	
	
	public void setEmprestimoDAO(EmprestimoPastaDAO emprestimoDAO) {
		this.emprestimoPastaDAO = emprestimoDAO;
	}
	
	public void validar(EmprestimoPasta emprestimo) throws BusinessException  {
		if ( emprestimo.getDataEmprestimo().after(new Date()) ) {
			throw new BusinessException("emprestimo.pasta.erro.dataSaida.invalida");
		}
		if ( emprestimo.getDataEmprestimo().after(emprestimo.getPrevisaoDevolucao()) ) {
			throw new BusinessException("emprestimo.pasta.erro.previsao.invalida");
			
		}
	}
	
}
