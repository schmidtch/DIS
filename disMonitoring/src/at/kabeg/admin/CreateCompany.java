package at.kabeg.admin;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import at.kabeg.controller.CompanyController;
import at.kabeg.model.Company;
import at.kabeg.utilities.StringReplacer;

@Path("/createCompany")
public class CreateCompany {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public String create(String input) {

		JSONObject response = new JSONObject();
		CompanyController cc = new CompanyController();
		String state="", msg="";
		
		try {
			JSONObject in = new JSONObject(input);
			cc.insertCompany(new Company(0, in.getString("name"), in.getString("telefon"), in.getString("email")));
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
		
		response.put("state", state);
		response.put("msg", msg);
		
		return response.toString();
	}

	@GET
	@Produces({ MediaType.TEXT_HTML })
	public String form() throws SQLException {

		StringReplacer sr = new StringReplacer();
		
		return sr.replaceInFile(this.getClass().getResource("../../../../../html/company_form.html").getPath());
	}



}
