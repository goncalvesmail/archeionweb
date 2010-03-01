package br.com.archeion.negocio.relatoriotxt;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import util.RelatorioTxt;
import br.com.archeion.mbean.pasta.EtiquetaPastaBean;
import br.com.archeion.persistencia.relatorio.RelatorioDAO;

public class RelatorioConsultaBOImpl implements RelatorioConsultaBO {
	private RelatorioDAO relatorioDAO;
	
	public void geraRelatorio(String sql,OutputStream stream) {
		Connection conn = relatorioDAO.getConnection();
		try {
			Statement stm =  conn.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			RelatorioTxt.gerar(rs,stream);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public List<EtiquetaPastaBean> geraEtiquetaPastaPorCaixa(String ids) {
		Connection conn = relatorioDAO.getConnection();
		List<EtiquetaPastaBean> result = new ArrayList<EtiquetaPastaBean>();
		
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select e.nm_empresa, l.nm_local, it.nm_item_documental, p.nm_titulo, ");
			sql.append("p.dt_previsao_recolhimento, p.dt_previsao_expurgo, p.nm_caixeta, ");
			sql.append("cx.nu_vao_endereco_caixa, ecx.vao_endereco_caixa ");
			sql.append(" from tb_pasta p join tb_local l on (p.id_local = l.id_local) ");
			sql.append(" join tb_empresa e on (l.id_empresa = e.id_empresa) "); 
			sql.append(" join tb_item_documental it on (p.id_item_documental = it.id_item_documental) "); 
			sql.append(" left outer join tb_caixa cx on(p.id_caixa = cx.id_caixa) ");
			sql.append(" left outer join tb_endereco_caixa ecx on (cx.id_endereco_caixa = ecx.id_endereco_caixa) ");
			sql.append(" where p.id_pasta in ( ");
			sql.append(ids);
			sql.append(" ) order by  ecx.vao_endereco_caixa, cx.nu_vao_endereco_caixa ");
			
			Statement stm =  conn.createStatement();
			ResultSet rs = stm.executeQuery(sql.toString());
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			while(rs.next()){
				EtiquetaPastaBean etiqueta = new EtiquetaPastaBean();
				etiqueta.setEmpresa(rs.getString(1));
				etiqueta.setLocal(rs.getString(2));
				etiqueta.setItemDocumental(rs.getString(3));
				etiqueta.setTitulo(rs.getString(4));
				etiqueta.setPrevisaoRecolhimento( (rs.getDate(5) != null)?df.format(rs.getDate(5)):"" );
				etiqueta.setPrevisaoExpurgo((rs.getDate(6) != null)?df.format(rs.getDate(6)):"Permanente");
				etiqueta.setCaixaOuCaixeta((rs.getString(9) != null)?("Caixa"+rs.getString(9)+rs.getString(8)):("Caixeta: "+rs.getString(7)));
				
				result.add(etiqueta);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public RelatorioDAO getRelatorioDAO() {
		return relatorioDAO;
	}

	public void setRelatorioDAO(RelatorioDAO relatorioDAO) {
		this.relatorioDAO = relatorioDAO;
	}
}
