package at.kabeg.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import at.kabeg.model.Application;
import at.kabeg.model.MonitoringError;
import at.kabeg.model.Server;
import at.kabeg.utilities.DBConnectionHandler;
import at.kabeg.utilities.DBQueryHandler;
import at.kabeg.utilities.Queries;
import at.kabeg.utilities.StringReplacer;

public class MonitoringErrorController {

	public void insertMonitoringError(MonitoringError me, String[] serverIDList) throws SQLException{
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		
		p.put("$appid", String.valueOf(me.getApplication_id()));
		p.put("$description", me.getDescription());
		p.put("$status", me.getStatus());
		p.put("$kommentar", me.getKommentar());
		p.put("$prio", me.getPrio());

		sr.setReplacements(p);
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.insertMonitoringError.toString())));
		
		for (String s : serverIDList){
			if( s!=null && !s.equals("")) {
				p.put("$id", s);
				sr.setReplacements(p);
				stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateServer.toString())));
			}
		}
		
		DBConnectionHandler.closeConnection(null, stmt);
	}
	
	public ArrayList<MonitoringError> getMonitoringJobs() throws SQLException{
		ArrayList<MonitoringError> errors = new ArrayList<MonitoringError>();
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		ResultSet rs = stmt.executeQuery(DBQueryHandler.getQuery(Queries.getMonitoringJobs.toString()));
		while(rs.next()){
			errors.add(new MonitoringError(Integer.parseInt(rs.getString("monitoring_id")), Integer.parseInt(rs.getString("application_id")), rs.getString("description"), rs.getString("name")));
		}
		DBConnectionHandler.closeConnection(rs, stmt);
		return errors;
	}
	
	public ArrayList<MonitoringError> getMonitoringErrorByApplicationID(String appID) throws SQLException{
		ArrayList<MonitoringError> errors = new ArrayList<MonitoringError>();
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		p.put("$application_id", appID);
		sr.setReplacements(p);
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		ResultSet rs = stmt.executeQuery(sr.replaceInString(DBQueryHandler.getQuery(Queries.getOverviewByApplicationID.toString())));
		//TODO: rs.getString("description")+"</td><td>"+rs.getString("server")+"</td><td>"+rs.getString("vorname")+" "+rs.getString("nachname")+"</td><td>"+rs.getString("bereitschaft")
		MonitoringError me = null;
		while(rs.next()){
			me = new MonitoringError();
			me.setDescription(rs.getString("description"));
			errors.add(me);
		}
		
		return errors;
	}
	
	public ArrayList<Application> getMonitoringOverview() throws SQLException{
		ArrayList<Application> apps = new ArrayList<Application>();
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		ResultSet rs = stmt.executeQuery(DBQueryHandler.getQuery(Queries.getMonitoringOverview.toString()));
		Application a=null;
		MonitoringError me=null;
		Server s=null;
		int serverId, appId, monitoringId, error;
		boolean errorBool = false;
		String appName="",serverName,meDescription,datum;
		while(rs.next()){
			serverId=Integer.parseInt(rs.getString("server_id"));
			appId=Integer.parseInt(rs.getString("application_id"));
			monitoringId=Integer.parseInt(rs.getString("monitoring_id"));
			error=Integer.parseInt(rs.getString("error"));
			if(error==1)errorBool=true;
			else errorBool=false;
			serverName=rs.getString("server");
			meDescription=rs.getString("description");
			datum=rs.getString("datum");
			s = new Server(serverId, appId, serverName,error);
			me = new MonitoringError(monitoringId, meDescription, errorBool, datum);
			if(!appName.equals(rs.getString("application")) || a==null){
				if(a!=null) apps.add(a);
				a=new Application(appId, appName, errorBool);
			} 
			a.addServer(s);
			a.addError(me);
		}
		DBConnectionHandler.closeConnection(rs, stmt);
		return apps;		
	}
	
	public void updateMonitoringErrorSetAck(String monitoringId, String server_id, String app_id, String error) throws SQLException{		
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		p.put("$monitoring_id", monitoringId);
		p.put("$server_id", server_id);
		p.put("$application_id", app_id);
		p.put("$error", error);
		sr.setReplacements(p);
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateMonitoringErrorSetAck.toString())));
		stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateHistorySetEndDate.toString())));
		stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateServerSetError.toString())));
		ResultSet rs = stmt.executeQuery(sr.replaceInString(DBQueryHandler.getQuery(Queries.getErrorServerByAppId.toString())));
		
		if(rs.isBeforeFirst()){
			error="1";
			p.put("$error", error);
			while(rs.next()){
				p.put("$monitoring_id", String.valueOf(rs.getInt("monitoring_id")));
				p.put("$server_id", String.valueOf(rs.getInt("server_id")));
				sr.setReplacements(p);
				stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateErrorDate.toString())));
				stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateServerSetError.toString())));
			}
		} 
		DBConnectionHandler.closeConnection(rs, stmt);
		
	}
	
	
}
