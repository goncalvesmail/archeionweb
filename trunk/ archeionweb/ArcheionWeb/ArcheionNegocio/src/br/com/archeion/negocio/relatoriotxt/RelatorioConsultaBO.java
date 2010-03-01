package br.com.archeion.negocio.relatoriotxt;

import java.io.OutputStream;
import java.util.List;

import br.com.archeion.mbean.pasta.EtiquetaPastaBean;

public interface RelatorioConsultaBO {
	public void geraRelatorio(String sql, OutputStream stream);
	public List<EtiquetaPastaBean> geraEtiquetaPastaPorCaixa(String ids);
}
