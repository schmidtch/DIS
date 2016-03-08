package at.kabeg.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import at.kabeg.model.Application;
import at.kabeg.utilities.DBConnectionHandler;
import at.kabeg.utilities.DBQueryHandler;
import at.kabeg.utilities.Queries;
import at.kabeg.utilities.StringReplacer;

public class ApplicationController {
	
	public void insertApplication(Application app) throws SQLException{
		Properties p = new Properties();
		p.put("$supervisor", String.valueOf(app.getSupervisor_id()));
		p.put("$company", String.valueOf(app.getCompany_id()));
		p.put("$name", app.getName());
		p.put("$bereitschaft", app.getBereitschaft());
		p.put("$beschreibung", app.getBeschreibung());

		StringReplacer sr = new StringReplacer();
		sr.setReplacements(p);
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.insertApplication.toString())));
		DBConnectionHandler.closeConnection(null, stmt);		
	}
	
	public ArrayList<Application> getApplications() throws SQLException{
		ArrayList<Application> applications = new ArrayList<Application>();
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		ResultSet rs = stmt.executeQuery(DBQueryHandler.getQuery(Queries.getApplications.toString()));
		while(rs.next()){
			applications.add(new Application(Integer.parseInt(rs.getString("application_id")), Integer.parseInt(rs.getString("supervisor_id")), Integer.parseInt(rs.getString("company_id")), rs.getString("name"), rs.getString("beschreibung"), rs.getString("bereitschaft")));
		}
		DBConnectionHandler.closeConnection(rs, stmt);
		return applications;
	}
	

}
