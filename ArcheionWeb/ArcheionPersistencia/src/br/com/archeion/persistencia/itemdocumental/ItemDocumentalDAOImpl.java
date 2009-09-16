package br.com.archeion.persistencia.itemdocumental;

import java.util.HashMap;
import java.util.List;

import br.com.archeion.modelo.itemdocumental.ItemDocumental;
import br.com.archeion.persistencia.impl.JpaGenericDAO;

public class ItemDocumentalDAOImpl extends JpaGenericDAO<ItemDocumental, Long> implements ItemDocumentalDAO {
	@SuppressWarnings("unchecked")
	public ItemDocumental findByName(String nome){
		HashMap<String, Object> parametros = new HashMap<String, Object>();

		parametros.put("nome", nome);

		StringBuilder sql = new StringBuilder(
			"SELECT u FROM ItemDocumental u WHERE u.nome = :nome");

		List<ItemDocumental> list = getJpaTemplate().findByNamedParams(sql.toString(),
				parametros);

		ItemDocumental itd = null;
		if (list != null && list.size() > 0) {
			itd = list.get(0);
		}

		return itd;
	}

}
