package br.com.archeion.negocio.empresa;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import util.Relatorio;
import br.com.archeion.exception.BusinessException;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.persistencia.empresa.EmpresaDAO;
import br.com.archeion.persistencia.local.LocalDAO;

public class EmpresaBOImpl implements EmpresaBO {

	private EmpresaDAO empresaDAO;
	private LocalDAO localDAO;

	public Empresa persist(Empresa empresa) throws CadastroDuplicadoException {
		validaEmpresa(empresa);
		return empresaDAO.persist(empresa);
	}

	public List<Empresa> findAll() {
		return empresaDAO.findAll();
	}
	
	public List<Empresa> findAllInvisivel(){
		return empresaDAO.findAllInvisivel();		
	}

	public void setEmpresaDAO(EmpresaDAO empresaDAO) {
		this.empresaDAO = empresaDAO;
	}

	public Empresa findById(Long id) {
		return empresaDAO.findById(id);
	}

	public Empresa findByName(String nome) {
		return empresaDAO.findByName(nome);
	}
	
	public Empresa merge(Empresa empresa) throws BusinessException, CadastroDuplicadoException {
		validaEmpresa(empresa);
		return empresaDAO.merge(empresa);
	}

	public void remove(Empresa empresa) throws BusinessException {
		
		List<Local> locais = localDAO.findByEmpresa(empresa);
		if ( locais!=null && locais.size()>0 ) {
			throw new BusinessException("empresa.erro.local");
		}
		
		empresaDAO.remove(empresa);		
	}

	public Relatorio getRelatorio(HashMap<String, Object> parameters,
			String localRelatorio) {
		Connection conn = empresaDAO.getConnection();
		try {
			Relatorio relatorio = new Relatorio(conn,parameters,localRelatorio);
			return relatorio;
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public List<Empresa> findRoots() {
		return empresaDAO.findRoots();
	}
	
	private void validaEmpresa(Empresa emp) throws CadastroDuplicadoException{
		Empresa e = empresaDAO.findByName(emp.getNome());
		
		if(e != null) {
			if ( emp.equals(e)) {
				return;
			}
			
			throw new CadastroDuplicadoException();
		}
	}

	public LocalDAO getLocalDAO() {
		return localDAO;
	}

	public void setLocalDAO(LocalDAO localDAO) {
		this.localDAO = localDAO;
	}

	public EmpresaDAO getEmpresaDAO() {
		return empresaDAO;
	}
	
}
