package br.com.archeion.persistencia.enderecocaixa;

import br.com.archeion.modelo.enderecocaixa.EnderecoCaixa;
import br.com.archeion.persistencia.GenericDAO;

public interface EnderecoCaixaDAO extends GenericDAO<EnderecoCaixa, Long> {

	public EnderecoCaixa findByName(String name);
	public EnderecoCaixa findIntervalo(EnderecoCaixa endereco);

}
