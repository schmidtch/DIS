package at.kabeg.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import at.kabeg.controller.ApplicationController;
import at.kabeg.model.Application;
import at.kabeg.utilities.StringReplacer;

@Path("/verwalten")
public class Verwalten {

	@POST
	@Produces({ MediaType.TEXT_HTML })
	public String start(String input) {
		String result = "", applicationOption = "<option value=\"\">==Choose==</option>";
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		ApplicationController ac = new ApplicationController();
		
		try {
			ArrayList<Application> applications = ac.getApplications();
			if(!applications.isEmpty()){
				for(Application a : applications){
					applicationOption = applicationOption + a.getHTMLOption();
				}
			}
		} catch (SQLException e) {	e.printStackTrace(); } 
		
		sr.setReplacements(p);
		p.put("$applicationOption", applicationOption);
		result = sr.replaceInFile(this.getClass().getResource("../../../../../html/verwalten_template.html").getPath());
		
		return result;
	}
	
	
}
