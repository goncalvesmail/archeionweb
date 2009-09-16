package br.com.archeion.negocio.caixa;

import java.util.Date;
import java.util.List;

import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.modelo.caixa.EmprestimoCaixa;
import br.com.archeion.modelo.usuario.Usuario;
import br.com.archeion.persistencia.caixa.EmprestimoCaixaDAO;


public class EmprestimoCaixaBOImpl implements EmprestimoCaixaBO {

	private EmprestimoCaixaDAO emprestimoCaixaDAO;

	public EmprestimoCaixa persist(EmprestimoCaixa emprestimoCaixa) throws CadastroDuplicadoException, BusinessException {
		validar(emprestimoCaixa);
		return emprestimoCaixaDAO.persist(emprestimoCaixa);
	}

	public List<EmprestimoCaixa> findAll() {
		return emprestimoCaixaDAO.findAll();
	}

	public List<EmprestimoCaixa> findDevolvidas(Caixa caixa, Usuario usuario, Date data) {
		return emprestimoCaixaDAO.findDevolvidas(caixa, usuario, data);
	}

	public List<EmprestimoCaixa> findEmprestadas(Caixa caixa, Usuario usuario, Date data) {
		return emprestimoCaixaDAO.findEmprestadas(caixa, usuario, data);
	}
	
	public void setEmprestimoCaixaDAO(EmprestimoCaixaDAO emprestimoCaixaDAO) {
		this.emprestimoCaixaDAO = emprestimoCaixaDAO;
	}

	public EmprestimoCaixa findById(Long id) {
		return emprestimoCaixaDAO.findById(id);
	}

	public EmprestimoCaixa merge(EmprestimoCaixa emprestimoCaixa) throws BusinessException, CadastroDuplicadoException {
		validar(emprestimoCaixa);
		return emprestimoCaixaDAO.merge(emprestimoCaixa);
	}

	public void remove(EmprestimoCaixa emprestimoCaixa) throws BusinessException {
		emprestimoCaixaDAO.remove(emprestimoCaixa);		
	}	
	
	public void setEmprestimoDAO(EmprestimoCaixaDAO emprestimoDAO) {
		this.emprestimoCaixaDAO = emprestimoDAO;
	}
	
	public void validar(EmprestimoCaixa emprestimo) throws BusinessException  {
		if ( emprestimo.getDataEmprestimo().after(new Date()) ) {
			throw new BusinessException("emprestimo.caixa.erro.dataSaida.invalida");
		}
		if ( emprestimo.getDataEmprestimo().after(emprestimo.getPrevisaoDevolucao()) ) {
			throw new BusinessException("emprestimo.caixa.erro.previsao.invalida");
			
		}
	}
	
}
