package br.com.archeion.persistencia.documento;

import java.util.List;

import br.com.archeion.modelo.documento.Documento;
import br.com.archeion.persistencia.GenericDAO;

public interface DocumentoDAO extends GenericDAO<Documento, Long> {
	
	Documento findByDocumento(Documento d);

	List<Documento> findByLocal(Long id);
	List<Documento> findByTipo(Long id);
	List<Documento> findByPasta(Long id);
	List<Documento> consultarDocumento(String where);
}
