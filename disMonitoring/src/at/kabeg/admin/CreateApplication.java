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


import at.kabeg.controller.ApplicationController;
import at.kabeg.controller.CompanyController;
import at.kabeg.controller.SupervisorController;
import at.kabeg.model.Application;
import at.kabeg.model.Company;
import at.kabeg.model.Supervisor;
import at.kabeg.utilities.StringReplacer;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/application")
public class CreateApplication {

	@GET
	@Path("/getForm")
	@Produces({ MediaType.TEXT_HTML })
	public String form() {

		String result = "", supervisorOption = "<option value=\"\">==Choose==</option>", companyOption="";
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		SupervisorController sc = new SupervisorController();
		CompanyController cc = new CompanyController();
		
		try {
			ArrayList<Supervisor> supervisors = sc.getSupervisors();
			ArrayList<Company> companies = cc.getCompanies();
			if(!supervisors.isEmpty()){
				for(Supervisor s : supervisors){
					supervisorOption = supervisorOption + s.getHTMLOption();
				}
			}
			if(!companies.isEmpty()){
				for(Company c : companies){
					companyOption = companyOption + c.getHTMLOption();
				}
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		
		p.put("$companyOption", companyOption);
		p.put("$supervisorOption", supervisorOption);
		sr.setReplacements(p);
		result = sr.replaceInFile(Thread.currentThread().getContextClassLoader().getResource("html/application_form.html").getPath());
		return result;
	}
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public String create(String input) {

		JSONObject response = new JSONObject();
		String state = "", msg="";
		ApplicationController ac = new ApplicationController();
		
		try {
			Application app = Application.setApplicationFromJSON(new JSONObject(input));
			ac.insertApplication(app);
			state="success";
		} catch (JSONException e) {
			state="error";
			msg=e.getMessage();
		} catch (SQLException e) {
			state="error";
			msg=e.getMessage();
			e.printStackTrace();
		} 
		
		try {
			response.put("state", state);
			response.put("msg", msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response.toString();
	}
	
}
