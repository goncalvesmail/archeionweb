package br.com.archeion.persistencia.local;

import java.util.List;

import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.modelo.local.Local;
import br.com.archeion.persistencia.GenericDAO;

public interface LocalDAO extends GenericDAO<Local, Long> {
	Local findByLocal(Local l);
	List<Local> findByEmpresa(Empresa empresa);
}
