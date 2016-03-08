package at.kabeg.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import at.kabeg.model.Server;
import at.kabeg.utilities.DBConnectionHandler;
import at.kabeg.utilities.DBQueryHandler;
import at.kabeg.utilities.Queries;
import at.kabeg.utilities.StringReplacer;

public class ServerController {

	public ArrayList<Server> getServers() throws SQLException{
		ArrayList<Server> servers = new ArrayList<Server>();
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		ResultSet rs = stmt.executeQuery(DBQueryHandler.getQuery(Queries.getServer.toString()));
		while(rs.next()){
			servers.add(new Server(Integer.parseInt(rs.getString("server_id")), Integer.parseInt(rs.getString("application_id")), rs.getString("name"), Integer.parseInt(rs.getString("error"))));
		}
		DBConnectionHandler.closeConnection(rs, stmt);
		return servers;
	}
	
	public void insertServer(Server s) throws SQLException {
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		p.put("$name", s.getName());
		sr.setReplacements(p);
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.insertServer.toString())));
		DBConnectionHandler.closeConnection(null, stmt);
	}
	
	public ArrayList<Server> getServerByMonitoringId(String id) throws SQLException{
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		p.put("$monitoring_id", id);
		ArrayList<Server> servers = new ArrayList<Server>();
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		ResultSet rs = stmt.executeQuery(sr.replaceInString(DBQueryHandler.getQuery(Queries.getServerByMonitoringId.toString())));
		while(rs.next()){
			servers.add(new Server(Integer.parseInt(rs.getString("server_id")), Integer.parseInt(rs.getString("application_id")), rs.getString("name"), Integer.parseInt(rs.getString("error"))));
		}
		DBConnectionHandler.closeConnection(rs, stmt);
		return servers;
	}
	
	
}
