package at.kabeg.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import at.kabeg.controller.LogDatenController;
import at.kabeg.controller.MonitoringErrorController;
import at.kabeg.model.LogDaten;
import at.kabeg.model.MonitoringError;
import at.kabeg.utilities.StringReplacer;

@Path("/logdata")
public class CreateLogDaten {
	
	@GET
	@Path("/getForm")
	@Produces({ MediaType.TEXT_HTML })
	public String form() throws SQLException {

		String result = "", monitoringOption = "<option value=\"\">==Choose==</option>"; 
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		MonitoringErrorController mec = new MonitoringErrorController();
		ArrayList<MonitoringError> errors = mec.getMonitoringJobs();
		if(!errors.isEmpty()){
			for(MonitoringError me : errors){
				monitoringOption = monitoringOption + me.getHTMLOption();
			}
		}
		p.put("$monitoringOption", monitoringOption);
		sr.setReplacements(p);
		result = sr.replaceInFile(Thread.currentThread().getContextClassLoader().getResource("html/logdata_form.html").getPath());
		
		return result;
	
	}
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public String create(String input) {

		JSONObject response = new JSONObject();
		LogDatenController ldc = new LogDatenController();
		String state="", msg="";
		
		try {		
			JSONObject request = new JSONObject(input);
			ldc.insertLogDaten(new LogDaten(request.getString("search_string"), request.getString("logdir"), request.getString("logfile")), request.getString("monitoring_id"), request.getString("server_id"));
			state="success";			
		} catch (JSONException e) {
			state="error";
			msg=e.getMessage();
			e.printStackTrace();
		} catch (SQLException e) {
			state="error";
			msg=e.getMessage();
			e.printStackTrace();
		}
		
		try {
			response.put("state", state);
			response.put("msg", msg);
		} catch (JSONException e) {}
		
		return response.toString();
	}
}
