package at.kabeg.overview;

import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import at.kabeg.utilities.LoginData;
import at.kabeg.utilities.StringReplacer;


@Path("/login")
public class Login {
	
	@GET
	@Path("/getForm")
	@Produces({ MediaType.TEXT_HTML})
	public String irgendwas() {
		StringReplacer sr = new StringReplacer();	
		return sr.replaceInFile(Thread.currentThread().getContextClassLoader().getResource("login.html").getPath());	
	}
	 
	@POST
	@Path("/doLogin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_JSON})
	public String checkLogin(String input){
		String result = "";
		try {
			JSONObject login = new JSONObject(input);
			if(LoginData.getInstance().checkLogin(login.getString("username"), login.getString("password"))){
				String admin = "no";
				if(login.getString("username").equals("admin")){
					admin="yes";
				}
				result = "{\"access\" : \"success\", \"session\" : \"6a438e3f3a3015785b24fc9a922517c9\", \"admin\" : \""+admin+"\"}";
			} else {
				result = "{\"access\" : \"denied\"}";
			}
		} catch (JSONException e) {
			result = "{\"access\" : \"error\"}";
			e.printStackTrace();
		}
		return result;
	}
	
}
