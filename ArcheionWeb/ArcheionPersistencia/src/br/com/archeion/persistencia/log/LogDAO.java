package br.com.archeion.persistencia.log;

import java.util.Date;
import java.util.List;

import br.com.archeion.modelo.log.Log;
import br.com.archeion.persistencia.GenericDAO;

public interface LogDAO extends GenericDAO<Log, Long> {
	public List<Log> findAll(int userId, Date iniDate, Date fimDate);
}
