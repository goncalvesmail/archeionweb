package br.com.archeion.mbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.Persistence;

import br.com.archeion.util.UnidadePersistencia;

public class ApplicationUtilBean {
	public ApplicationUtilBean() {
		Persistence.createEntityManagerFactory(UnidadePersistencia.unidadePersistencia.getDescricao()).createEntityManager();
	}

	public Date getDataAtual() {
		return new Date();
	}

	public List<SelectItem> getListaOrgao() {
		// TODO: Implementar pegando a lista correta de Orgãos
		List<SelectItem> itens = new ArrayList<SelectItem>();

		itens.add(new SelectItem(new Integer(1), "Orgao Teste 1"));
		itens.add(new SelectItem(new Integer(2), "Orgao Teste 2"));
		itens.add(new SelectItem(new Integer(3), "Orgao Teste 3"));
		itens.add(new SelectItem(new Integer(4), "Orgao Teste 4"));
		itens.add(new SelectItem(new Integer(5), "Orgao Teste 5"));
		itens.add(new SelectItem(new Integer(6), "Orgao Teste 6"));
		itens.add(new SelectItem(new Integer(7), "Orgao Teste 7"));

		return itens;
	}
	public List<SelectItem> getListaBoolean() {
		// TODO: Implementar pegando a lista correta de Orgãos
		List<SelectItem> itens = new ArrayList<SelectItem>();

		itens.add(new SelectItem(new Integer(0), "NÃO"));
		itens.add(new SelectItem(new Integer(1), "SIM"));

		return itens;
	}
}
