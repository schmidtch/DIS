package at.kabeg.admin;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import at.kabeg.controller.MonitoringErrorController;
import at.kabeg.model.MonitoringError;

@Path("/manageApplication")
public class ManageApplication {
	
	@POST
	@Produces({ MediaType.TEXT_HTML })
	@Consumes(MediaType.APPLICATION_JSON)
	public String showData(String input) {

		MonitoringErrorController mec = new MonitoringErrorController();
		JSONObject response = new JSONObject();
		String state = "", data = "";
		
		try {
			JSONObject jo = new JSONObject(input);
			ArrayList<MonitoringError> errors = mec.getMonitoringErrorByApplicationID(jo.getString("id"));
			data = "<table>";
			data +="<thead><tr><th style=\"border-left: 1px solid #D0D0D0;\">Monitoring Error</th><th>Server</th><th>Supervisor</th><th>Attendance</th></tr>";
			data += "</thead><tbody>";
			for (MonitoringError me : errors){
				data += "<tr><td style=\"border-left: 1px solid #D0D0D0;\">"+me.getDescription()+"</td></tr>";//<td>"+rs.getString("server")+"</td><td>"+rs.getString("vorname")+" "+rs.getString("nachname")+"</td><td>"+rs.getString("bereitschaft")+"</td></tr>";
			}
			data += "</tbody>";
			data += "</table>";
			state = "success";
		} catch (JSONException e) {
			state = "error_json";
			data = e.getMessage();
		} catch (SQLException e) {
			state = "error_sql";
			data = e.getMessage();
		} 
		
		try {
			response.put("state", state);
			response.put("data", data);
		} catch (JSONException e) {}
		
		return response.toString();
		
	}
	
}
