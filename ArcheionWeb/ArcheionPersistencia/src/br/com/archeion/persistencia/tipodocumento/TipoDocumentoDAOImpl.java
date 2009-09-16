package br.com.archeion.persistencia.tipodocumento;

import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.tipodocumento.TipoDocumento;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class TipoDocumentoDAOImpl extends JpaGenericDAO<TipoDocumento, Long> implements TipoDocumentoDAO {

	@SuppressWarnings("unchecked")
	public TipoDocumento findByName(String name) {
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		parametros.put("nome", name);

		StringBuilder sql = new StringBuilder(
			"SELECT u FROM TipoDocumento u WHERE u.nome = :nome");

		List<TipoDocumento> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);

		TipoDocumento tipoDocumento = null;
		if (list != null && list.size() > 0) {
			tipoDocumento = list.get(0);
		}

		return tipoDocumento;
	}
}
