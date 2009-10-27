package util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class RelatorioTxt {

	public static void gerar(ResultSet rs, OutputStream stream){
		try {
			BufferedWriter  bf = new BufferedWriter(new OutputStreamWriter(stream));
			ResultSetMetaData rsmd = rs.getMetaData();
			int colNumber = rsmd.getColumnCount();
			
			for(int i = 1;i<=colNumber;i++){
				bf.append(rsmd.getColumnLabel(i));
				bf.append("\t");
			}			
			
			while(rs.next()){
				bf.append("\n");
				for(int i = 1;i<=colNumber;i++){
					bf.append((rs.getString(i)!=null)?rs.getString(i).replaceAll("\\r", " ").replaceAll("\\n", " "):"");
					bf.append("\t");
				}
			}
			bf.flush();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
