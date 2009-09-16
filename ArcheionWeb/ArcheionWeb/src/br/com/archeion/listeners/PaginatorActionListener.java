package br.com.archeion.listeners;

import java.util.Collection;
import java.util.Map;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;

public class PaginatorActionListener implements ActionListener {

	public void processAction(ActionEvent arg0) throws AbortProcessingException {
		Map<String, Object> attributes = arg0.getComponent().getAttributes();
		Collection<Object> values = attributes.values();
		System.out.println("TESTE --- ");
		for (Object object : values) {
			System.out.println("TESTE --- " + object);
		}
	}

	public PhaseId getPhaseId() {
		System.out.println("getPhaseId called");
		return PhaseId.APPLY_REQUEST_VALUES;
	}

}
