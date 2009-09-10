package br.com.archeion.persistencia.empresa;

import java.util.List;

import br.com.archeion.modelo.empresa.Empresa;
import br.com.archeion.persistencia.GenericDAO;

public interface EmpresaDAO extends GenericDAO<Empresa, Long> {
	
	public List<Empresa> findRoots() ;
	public Empresa findByName(String name);
	public List<Empresa> findAllInvisivel();

}
