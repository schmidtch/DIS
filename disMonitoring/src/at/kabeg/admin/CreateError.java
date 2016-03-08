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

import at.kabeg.controller.ApplicationController;
import at.kabeg.controller.MonitoringErrorController;
import at.kabeg.controller.ServerController;
import at.kabeg.model.Application;
import at.kabeg.model.MonitoringError;
import at.kabeg.model.Server;
import at.kabeg.utilities.StringReplacer;

@Path("/createError")
public class CreateError {

	@GET
	@Produces({ MediaType.TEXT_HTML })
	public String form() {

		String result = "", applOption = "<option value=\"\">==Choose==</option>", serverOption = "<option value=\"\">==Choose==</option>";
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		ApplicationController ac = new ApplicationController();
		ServerController sc = new ServerController();
		
		try {
			ArrayList<Application> applications = ac.getApplications();
			ArrayList<Server> servers = sc.getServers();
			if(!applications.isEmpty()){
				for(Application a : applications){
					applOption = applOption + a.getHTMLOption();
				}
			}
			if(!servers.isEmpty()){
				for(Server s : servers){
					serverOption = serverOption + s.getHTMLOption();
				}
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
			
		p.put("$applOption", applOption);
		p.put("$serverOption", serverOption);
		sr.setReplacements(p);
		result = sr.replaceInFile(this.getClass()
				.getResource("../../../../../html/error_form.html")
				.getPath());		
		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public String create(String input) {

		//TODO: create
		JSONObject response = new JSONObject();
		String state="", msg="", name="";
		MonitoringErrorController mec = new MonitoringErrorController();
		
		try {		
			JSONObject request = new JSONObject(input);
			String ss = request.getString("server");
			String[] serverIDList = ss.split(";",-1);
			mec.insertMonitoringError(new MonitoringError().createError(request), serverIDList);
			name=request.getString("description");
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
			response.put("name", name);
			response.put("msg", msg);
		} catch (JSONException e) {}
		
		return response.toString();
	}
}
