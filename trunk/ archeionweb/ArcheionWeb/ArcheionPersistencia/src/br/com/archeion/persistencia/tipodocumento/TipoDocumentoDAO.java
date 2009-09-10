package br.com.archeion.persistencia.tipodocumento;

import br.com.archeion.modelo.tipodocumento.TipoDocumento;
import br.com.archeion.persistencia.GenericDAO;

public interface TipoDocumentoDAO extends GenericDAO<TipoDocumento, Long> {
	public TipoDocumento findByName(String name);
}
