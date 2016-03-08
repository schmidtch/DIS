package at.kabeg.service;

import java.sql.ResultSet;
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

@Path("/checkTask")
public class CheckTask {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON})
	public String start(String input) {
		
		JSONObject response = new JSONObject();
		JSONObject responseLine = null;
		int count = 1;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			JSONObject request = new JSONObject(input);
			String server = request.getString("server");
			
			System.out.println("\nRECEIVED A Call from "+server);
			
			
			StringReplacer sr = new StringReplacer();
			Properties p = new Properties();
			StringReplacer sr2 = new StringReplacer();
			Properties p2 = new Properties();
			p.put("$servername", server);
			sr.setReplacements(p);
			int server_id=0, monitoring_id=0;
			String latestError="no";
			stmt = DBConnectionHandler.getConnection(false).createStatement();
			rs = stmt.executeQuery(sr.replaceInString(DBQueryHandler.getQuery(Queries.getTaskByServerName.toString())));
			ResultSet rsHistory = null;
			
			while (rs.next()){
				responseLine = new JSONObject();
				server_id = rs.getInt("server_id");
				monitoring_id = rs.getInt("monitoring_id");
				responseLine.put("appid", rs.getInt("application_id"));
				responseLine.put("monitoring", monitoring_id);
				responseLine.put("applikation", rs.getString("name"));
				responseLine.put("serverid", server_id);
				responseLine.put("description", rs.getString("description"));
				//response.put("datum", rs.getString("datum"));
				responseLine.put("logdaten_id",rs.getInt("logdaten_id"));
				responseLine.put("logdir",rs.getString("logdir"));
				responseLine.put("logfile",rs.getString("logfile"));
				responseLine.put("search_string",rs.getString("search_string"));
				
				System.out.println("MONITORING ID: "+monitoring_id + " SERVER ID: "+server_id);
				
				p2.put("$monitoring_id", String.valueOf(monitoring_id));
				p2.put("$server_id", String.valueOf(server_id));
				sr2.setReplacements(p2);
				rsHistory = stmt.executeQuery(sr2.replaceInString(DBQueryHandler.getQuery(Queries.getMaxHistoryForTask.toString())));
				while(rsHistory.next()){
					latestError=rsHistory.getString("error_string");
				}
				if(rsHistory!=null)rsHistory.close();
				responseLine.put("latest_error", latestError);
				
				response.put(String.valueOf(count), responseLine);
				count++;
			}
			response.put("state", "success");
			response.put("count", count-1);
			
		} catch (JSONException e) {
			System.err.println("PROBLEM WITH PARSING JSON MESSAGE \n"+e.getMessage());
		} catch (SQLException e) {
			System.err.println("PROBLEM WITH DB QUERY OR CONNECTION "+e.getMessage());
			try {
				response.put("state", "error");
				response.put("error", e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				DBConnectionHandler.closeConnection(rs, stmt);
			} catch (SQLException e) {}
		}
		
		return response.toString();
	}
	
}
