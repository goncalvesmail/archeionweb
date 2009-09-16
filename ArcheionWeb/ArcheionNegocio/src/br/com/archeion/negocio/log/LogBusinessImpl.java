package br.com.archeion.negocio.log;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import util.Relatorio;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.log.Log;
import br.com.archeion.persistencia.log.LogDAO;

public class LogBusinessImpl implements LogBusiness {

	private LogDAO logDAO;
	
	public List<Log> findAll() {		
		return logDAO.findAll();
	}
	
	public List<Log> findAll(int userId, Date iniDate, Date fimDate) {
		return logDAO.findAll(userId, iniDate, fimDate);
	}

	public Log findById(Long id) {
		return logDAO.findById(id);
	}

	public Relatorio getRelatorio(HashMap<String, String> parameters,
			String localRelatorio) {
		return null;
	}

	public Log persist(Log log) throws CadastroDuplicadoException {
		return logDAO.persist(log);
	}

	public void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}	
}
