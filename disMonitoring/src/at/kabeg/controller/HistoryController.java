package at.kabeg.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import at.kabeg.utilities.DBConnectionHandler;
import at.kabeg.utilities.DBQueryHandler;
import at.kabeg.utilities.Queries;
import at.kabeg.utilities.StringReplacer;

public class HistoryController {

	public String getHistoryByMIDAndSID(String monitoringID, String serverID) throws SQLException{
		String tbody = "";
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		p.put("$monitoring_id", monitoringID);
		p.put("$server_id", serverID);
		sr.setReplacements(p);
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		ResultSet rs = stmt.executeQuery(sr.replaceInString(DBQueryHandler.getQuery(Queries.getHistoryByMIDAndSID.toString())));
		
		while(rs.next()){
			tbody += "<tr>";
			
			tbody += "<td style=\"border-left:1px solid #D0D0D0;\">" + rs.getString("beginn_date") + "</td><td>" + rs.getString("error_string") + "</td>";
			
			tbody += "</tr>";
		}		
		
		DBConnectionHandler.closeConnection(rs, stmt);
				
		return tbody;
	}
	
}
