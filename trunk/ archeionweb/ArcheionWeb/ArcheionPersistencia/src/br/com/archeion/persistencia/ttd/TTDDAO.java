package br.com.archeion.persistencia.ttd;

import java.util.List;

import br.com.archeion.modelo.ttd.TTD;
import br.com.archeion.persistencia.GenericDAO;

public interface TTDDAO extends GenericDAO<TTD, Long> {
	TTD findByTTD(TTD ttd);
	List<TTD> findByEmpresaLocalItemDocumental(int emp, int local, int item);
	List<TTD> findByEvento(Long idEvento);
	void atualizarPastasTTD(TTD ttd);
}
