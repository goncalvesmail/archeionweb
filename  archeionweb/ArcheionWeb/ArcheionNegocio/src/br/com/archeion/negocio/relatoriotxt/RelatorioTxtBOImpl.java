package br.com.archeion.negocio.relatoriotxt;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.RelatorioTxt;
import br.com.archeion.persistencia.relatorioTxt.RelatorioTxtDAO;

public class RelatorioTxtBOImpl implements RelatorioTxtBO {
	private RelatorioTxtDAO relatorioDAO;
	
	public void geraRelatorioTxt(String sql,OutputStream stream) {
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

	public RelatorioTxtDAO getRelatorioDAO() {
		return relatorioDAO;
	}

	public void setRelatorioDAO(RelatorioTxtDAO relatorioDAO) {
		this.relatorioDAO = relatorioDAO;
	}
}
