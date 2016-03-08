package at.kabeg.admin;

import java.sql.SQLException;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import at.kabeg.controller.HistoryController;
import at.kabeg.utilities.StringReplacer;

@Path("/history")
public class History {

	@GET
	@Path("/{monitoringID}/{serverID}/{monitoringName}/{serverName}")
	@Produces({ MediaType.TEXT_HTML })
	public String getServer(@PathParam("monitoringID") String monitoringID, @PathParam("serverID") String serverID, @PathParam("monitoringName") String monitoringName, @PathParam("serverName") String serverName) {
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		HistoryController hc = new HistoryController();
		try {
			p.put("$tbody", hc.getHistoryByMIDAndSID(monitoringID, serverID));
		} catch (SQLException e) {
			p.put("$tbody", "<p>Error with SQL Statement or Connection</p>");
		} 
		p.put("$monitoringName", monitoringName);
		p.put("$serverName", serverName);
		
		return sr.replaceInFile(this.getClass().getResource("../../../../../html/history_for_error_template.html").getPath());
	}
	
	
}
