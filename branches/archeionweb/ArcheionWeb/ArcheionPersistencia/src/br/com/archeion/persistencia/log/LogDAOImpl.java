package br.com.archeion.persistencia.log;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.log.Log;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class LogDAOImpl extends JpaGenericDAO<Log, Long> implements LogDAO {

	@SuppressWarnings("unchecked")
	public List<Log> findAll(int userId, Date iniDate, Date fimDate) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder("SELECT u FROM Log u");
		
		boolean where = false;
		if ( userId!=-1 ) {
			parametros.put("userId", userId);
			sql.append(" WHERE u.usuario.id = :userId ");
			where = true;
		}
		if ( iniDate!=null ) {
			parametros.put("iniDate", iniDate);
			if ( where ) {
				sql.append(" and u.data > :iniDate ");
			}
			else {
				sql.append(" WHERE u.data > :iniDate ");
				where = true;
			}
		}
		if ( fimDate!=null ) {
			parametros.put("fimDate", fimDate);
			if ( where ) {
				sql.append(" and u.data < :fimDate ");
			}
			else {
				sql.append(" WHERE u.data < :fimDate ");
				where = true;
			}
		}

		List<Log> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);

		return list;
	}
}
