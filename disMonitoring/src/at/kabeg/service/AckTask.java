package at.kabeg.service;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import at.kabeg.controller.MonitoringErrorController;



@Path("/ackTask")
public class AckTask {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON})
	public String start(String input) {
		
		JSONObject response = new JSONObject();
		MonitoringErrorController mec = new MonitoringErrorController();
		String state = "", msg = "";
		
		try {
			JSONObject request = new JSONObject(input);
			String monitoringId = request.getString("monitoring_id");
			String server_id = request.getString("server_id");
			String app_id = request.getString("app_id");
			String error = "0";
			mec.updateMonitoringErrorSetAck(monitoringId, server_id, app_id, error);
			state="success";
			
		} catch (JSONException e) {
			state="error";
			msg=e.getMessage();
		} catch (SQLException e) {
			state="error";
			msg=e.getMessage();
		}

		try {
			response.put("state", state);
			response.put("msg", msg);
		} catch (JSONException e) {}
		
		
		return response.toString();
	}		
}
