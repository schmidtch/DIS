package at.kabeg.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import at.kabeg.model.Supervisor;
import at.kabeg.utilities.DBConnectionHandler;
import at.kabeg.utilities.DBQueryHandler;
import at.kabeg.utilities.Queries;

public class SupervisorController {

	public ArrayList<Supervisor> getSupervisors() throws SQLException{
		ArrayList<Supervisor> supervisors = new ArrayList<Supervisor>();
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		ResultSet rs = stmt.executeQuery(DBQueryHandler.getQuery(Queries.getSupervisors.toString()));
		while(rs.next()){
			supervisors.add(new Supervisor(Integer.parseInt(rs.getString("supervosor_id")), rs.getString("nachname"), rs.getString("vorname"), rs.getString("email"), rs.getString("telefon")));
		}		
		DBConnectionHandler.closeConnection(rs, stmt);
		return supervisors;
	}
	
	
}
