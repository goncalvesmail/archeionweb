package br.com.archeion.jsf;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class ArcheionPhaseListener implements PhaseListener {
	private static final String PHASE_PARAM = "br.com.archeion.jsf.helper.phaseTracker.cphase";
	private static final Logger logger = Logger.getLogger(ArcheionPhaseListener.class);
	private static String cphase = null;

	public void setPhase(String newValue) {
		cphase = newValue;
	}

	public PhaseId getPhaseId() {
		PhaseId phaseId = PhaseId.ANY_PHASE;
		if (cphase == null)
		{
			FacesContext context = FacesContext.getCurrentInstance();
			if (context == null)
				return phaseId;
			cphase = (String) context.getExternalContext().getInitParameter(PHASE_PARAM);
		}

		if (cphase != null) {
			if ("RESTORE_VIEW".equals(cphase))
				phaseId = PhaseId.RESTORE_VIEW;
			else if ("APPLY_REQUEST_VALUES".equals(cphase))
				phaseId = PhaseId.APPLY_REQUEST_VALUES;
			else if ("PROCESS_VALIDATIONS".equals(cphase))
				phaseId = PhaseId.PROCESS_VALIDATIONS;
			else if ("UPDATE_MODEL_VALUES".equals(cphase))
				phaseId = PhaseId.UPDATE_MODEL_VALUES;
			else if ("INVOKE_APPLICATION".equals(cphase))
				phaseId = PhaseId.INVOKE_APPLICATION;
			else if ("RENDER_RESPONSE".equals(cphase))
				phaseId = PhaseId.RENDER_RESPONSE;
			else if ("ANY_PHASE".equals(cphase))
				phaseId = PhaseId.ANY_PHASE;
		}
		return phaseId;
	}

	public void beforePhase(PhaseEvent e) {
		logger.info("BEFORE " + e.getPhaseId());
	}

	public void afterPhase(PhaseEvent e) {
		logger.info("AFTER " + e.getPhaseId());
	}
}
