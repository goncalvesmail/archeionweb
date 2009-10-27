package br.com.archeion.negocio.relatoriotxt;

import java.io.OutputStream;

public interface RelatorioTxtBO {
	public void geraRelatorioTxt(String sql, OutputStream stream);

}
