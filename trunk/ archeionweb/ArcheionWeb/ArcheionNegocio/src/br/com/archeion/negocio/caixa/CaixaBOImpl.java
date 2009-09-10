package br.com.archeion.negocio.caixa;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.SituacaoExpurgo;
import br.com.archeion.modelo.TipoArquivo;
import br.com.archeion.modelo.caixa.Caixa;
import br.com.archeion.modelo.caixa.EmprestimoCaixa;
import br.com.archeion.modelo.pasta.Pasta;
import br.com.archeion.persistencia.caixa.CaixaDAO;
import br.com.archeion.persistencia.caixa.EmprestimoCaixaDAO;

public class CaixaBOImpl implements CaixaBO {

	private CaixaDAO caixaDAO;
	private EmprestimoCaixaDAO emprestimoCaixaDAO;
	
	public List<Caixa> findVaoOcupados(String nomeVao) {
		return caixaDAO.findVaoOcupados(nomeVao);
	}
	
	public Caixa persist(Caixa caixa) throws CadastroDuplicadoException, BusinessException {
		validaCaixa(caixa);
		
		caixa = caixaDAO.persist(caixa);

		return caixa;
	}
	
	public List<Caixa> findCaixaAtivasEmprestimo() {
		return caixaDAO.findCaixaAtivasEmprestimo();
	}
	
	public List<Caixa> findByEmpresaLocal(int emp, int local) {
		return caixaDAO.findByEmpresaLocal(emp, local);
	}
	
	public List<Caixa> findByEmpresaLocalSituacao(int emp, int local, SituacaoExpurgo situacao) {
		return caixaDAO.findByEmpresaLocalSituacao(emp, local, situacao);
	}
	
	public Caixa findByVaoNumero(String vao, int numero) {
		return caixaDAO.findByVaoNumero(vao,numero);
	}
	
	public Caixa findByVaoNumeroAtiva(String vao, int numero) {
		return caixaDAO.findByVaoNumeroAtiva(vao, numero);
	}
	
	public List<Caixa> findByEndereco(Long id) {
		return caixaDAO.findByEndereco(id);
	}

	public List<Caixa> findAll() {
		return caixaDAO.findAll();
	}
	
	public List<Caixa> consultaEtiquetaCaixa(int caixaId, Date date, TipoArquivo tipo) {
		return caixaDAO.consultaEtiquetaCaixa(caixaId, date, tipo);
	}
	
	public List<Caixa> findCaixaAtivasExpurgo() {
		return caixaDAO.findCaixaAtivasExpurgo();
	}
	
	public void setCaixaDAO(CaixaDAO caixaDAO) {
		this.caixaDAO = caixaDAO;
	}

	public Caixa findById(Long id) {
		return caixaDAO.findById(id);
	}
	
	public Caixa merge(Caixa caixa) throws BusinessException, CadastroDuplicadoException {
		validaCaixa(caixa);
		return caixaDAO.merge(caixa);
	}

	public void remove(Caixa caixa) throws BusinessException {		
		
		//Verificar Pastas
		List<Pasta> pastas = caixa.getPastas();
		if ( pastas!=null && pastas.size()>0 ) {
			throw new BusinessException("caixa.erro.pasta");
		}
		
		//Verifica Emprestimo
		List<EmprestimoCaixa> emprestimos = emprestimoCaixaDAO.findByCaixa(caixa);
		if ( emprestimos!=null && emprestimos.size()>0 ) {
			throw new BusinessException("caixa.erro.emprestimo");
		}
		
		caixaDAO.remove(caixa);		
	}

	public Relatorio getRelatorio(HashMap<String, Object> parameters,
			String localRelatorio) {
		Connection conn = caixaDAO.getConnection();
		try {
			Relatorio relatorio = new Relatorio(conn,parameters,localRelatorio);
			return relatorio;
		} catch (JRException e) {
			e.printStackTrace();
		}finally {
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}	
	
	private void validaCaixa(Caixa caixa) throws CadastroDuplicadoException, BusinessException{
		
		
	}

	public void setEmprestimoCaixaDAO(EmprestimoCaixaDAO emprestimoCaixaDAO) {
		this.emprestimoCaixaDAO = emprestimoCaixaDAO;
	}
	
}
