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

import at.kabeg.controller.ServerController;
import at.kabeg.model.Server;
import at.kabeg.utilities.StringReplacer;

@Path("/createServer")
public class CreateServer {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON })
	public String create(String input) {
		
		JSONObject response = new JSONObject();
		ServerController sc = new ServerController();
		String state="", msg="";
		try {
			JSONObject request = new JSONObject(input);
			sc.insertServer(new Server(request.getString("name")));
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

	@GET
	@Produces({ MediaType.TEXT_HTML })
	public String form() throws SQLException {

		StringReplacer sr = new StringReplacer();
		
		return sr.replaceInFile(this.getClass().getResource("../../../../../html/server_form.html").getPath());
	}
	
}
