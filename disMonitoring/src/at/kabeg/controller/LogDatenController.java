package at.kabeg.controller;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import at.kabeg.model.LogDaten;
import at.kabeg.utilities.DBConnectionHandler;
import at.kabeg.utilities.DBQueryHandler;
import at.kabeg.utilities.Queries;
import at.kabeg.utilities.StringReplacer;

public class LogDatenController {

	public void insertLogDaten(LogDaten ld, String monitoringId, String serverId) throws SQLException{

		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		p.put("$monitoring_id", monitoringId);
		p.put("$server_id", serverId);
		p.put("$logfile", ld.getLogfile());
		p.put("$logdir", ld.getLogdir());
		p.put("$search_string", ld.getSearch_string());
		sr.setReplacements(p);
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.insertLogdataForServer.toString())));
		DBConnectionHandler.closeConnection(null, stmt);
		
	}
	
}
