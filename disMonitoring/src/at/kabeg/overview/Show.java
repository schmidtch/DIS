package at.kabeg.overview;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import at.kabeg.utilities.StringReplacer;

@Path("/show")
public class Show {
	
	@POST
	@Path("/index")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({ MediaType.TEXT_HTML})
	public String start(String input) {
		String result = "";
		StringReplacer sr = new StringReplacer();
		try {
			JSONObject jo = new JSONObject(input);
			if(jo.getString("session").equals("6a438e3f3a3015785b24fc9a922517c9")){
				result = sr.replaceInFile(Thread.currentThread().getContextClassLoader().getResource("html/index.html").getPath());
			} else {
				result = "<h2>Permission denied!</h2>";
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
