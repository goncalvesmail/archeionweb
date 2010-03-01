package br.com.archeion.negocio.relatorio;

import util.TipoRelatorio;

public interface RelatorioBO {
	
	public void gerar(TipoRelatorio tipoRelatorio, String ids);
}
