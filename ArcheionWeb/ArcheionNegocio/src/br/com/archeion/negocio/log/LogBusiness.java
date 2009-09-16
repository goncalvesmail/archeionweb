package br.com.archeion.negocio.log;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import util.Relatorio;
import br.com.archeion.exception.CadastroDuplicadoException;
import br.com.archeion.modelo.log.Log;

public interface LogBusiness {	
	
	List<Log> findAll();
	
	List<Log> findAll(int userId, Date iniDate, Date fimDate);
	
	Log findById(Long id);
	
	@Transactional
	Log persist(Log log) throws CadastroDuplicadoException;

	@Transactional
	Relatorio getRelatorio(HashMap<String, String> parameters, String localRelatorio);
}
