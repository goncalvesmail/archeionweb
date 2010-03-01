package br.com.archeion.negocio.relatorio;

import java.util.HashMap;

import util.JasperReportUtil;
import util.TipoRelatorio;
import br.com.archeion.negocio.relatoriotxt.RelatorioConsultaBO;

public class RelatorioBOImpl implements RelatorioBO {
	private RelatorioConsultaBO relatorioConsultaBO;// = (RelatorioConsultaBO) Util.getSpringBean("relatorioConsultaBO");
	
	@Override
	public void gerar(TipoRelatorio tipoRelatorio, String ids) {
		switch (tipoRelatorio) {
		case ETIQUETAPASTACAIXA: 
			JasperReportUtil.gerarPDF("/WEB-INF/relatorios/ArcheionEtiquetaPastaPorCaixaBean.jasper", 
					new HashMap<String, Object>(), relatorioConsultaBO.geraEtiquetaPastaPorCaixa(ids), "etiquetaPasta");
			break;
		case ETIQUETAPASTACAIXETA:
			break;
		default:
			break;
		}
		
	}

	public RelatorioConsultaBO getRelatorioConsultaBO() {
		return relatorioConsultaBO;
	}

	public void setRelatorioConsultaBO(RelatorioConsultaBO relatorioConsultaBO) {
		this.relatorioConsultaBO = relatorioConsultaBO;
	}



}
