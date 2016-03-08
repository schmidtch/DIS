package at.kabeg.service;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import at.kabeg.utilities.DBConnectionHandler;
import at.kabeg.utilities.DBQueryHandler;
import at.kabeg.utilities.Queries;
import at.kabeg.utilities.StringReplacer;

@Path("foundError")
public class FoundError {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON})
	public String start(String input) {
		
		JSONObject response = new JSONObject();
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		Statement stmt = null;
		
		try {
			JSONObject request = new JSONObject(input);
			
			String monitoring_id = request.getString("monitoring");
			String app_id = request.getString("appid");
			String description = request.getString("error_string");
			String server_id = request.getString("server_id");
			int error = request.getInt("error");
			
			System.out.println("\nRECEIVED A Call from app: "+request.getString("appid")+ "found Error: "+request.getString("monitoring"));			
			
			p.setProperty("$monitoring_id", monitoring_id);
			p.setProperty("$application_id", app_id);
			p.setProperty("$server_id", server_id);
			p.setProperty("$application_id", app_id);	
			p.setProperty("$error", String.valueOf(error));
			sr.setReplacements(p);
			stmt = DBConnectionHandler.getConnection(false).createStatement();			
			stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateMonitoringErrorById.toString())));
			stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateServerSetError.toString())));
			
			String[] errors = description.split(";",-1);
			String insertHistoryQuery = DBQueryHandler.getQuery(Queries.insertHistory.toString());
			
			for(int i=1; i<errors.length; i++){
				p.setProperty("$description", errors[i]);
				sr.setReplacements(p);
				stmt.execute(sr.replaceInString(insertHistoryQuery));
			}
			
						
			response.put("state", "success");
			
		} catch (JSONException e) {
			try {response.put("state", "error"); response.put("msg", "PROBLEM WITH PARSING JSON MESSAGE "+e.getMessage());} catch (JSONException e1) {}
			System.err.println("PROBLEM WITH PARSING JSON MESSAGE \n"+e.getMessage());
		} catch (SQLException e) {
			try { response.put("state", "error"); response.put("msg", "PROBLEM BY EXECUTING THE QUERY OR WITH TH DB-CONNECTION "+e.getMessage());	} catch (JSONException e1) {}
			System.err.println("PROBLEM BY EXECUTING THE QUERY OR WITH TH DB-CONNECTION "+e.getMessage());
		} finally {
			try {
				DBConnectionHandler.closeConnection(null, stmt);
			} catch (SQLException e) {
				System.err.println("PROBLEM BY CLOSING THE CONNECTION "+e.getMessage());
			}
		}
		
				
		return response.toString();
	}
}
