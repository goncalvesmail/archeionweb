package br.com.archeion.persistencia.itemdocumental;

import br.com.archeion.modelo.itemdocumental.ItemDocumental;
import br.com.archeion.persistencia.GenericDAO;

public interface ItemDocumentalDAO extends GenericDAO<ItemDocumental, Long> {
	public ItemDocumental findByName(String nome);
}
