package at.kabeg.overview;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import at.kabeg.controller.MonitoringErrorController;
import at.kabeg.model.Application;
import at.kabeg.model.MonitoringError;
import at.kabeg.model.Server;
import at.kabeg.utilities.StringReplacer;

@Path("/monitor")
public class Monitor {

	@POST
	@Path("/show")
	@Produces({ MediaType.TEXT_HTML })
	public String start(String input) {
		
		String result = "", entry="";
		
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		MonitoringErrorController mec = new MonitoringErrorController();
		try {
			entry = mec.getMonitoringOverview();
		} catch (SQLException e) {
			entry = e.getMessage();
			e.printStackTrace();
		}
		
		p=new Properties();
		p.put("$monitorContent", entry);
		sr.setReplacements(p);
		result = sr.replaceInFile(Thread.currentThread().getContextClassLoader().getResource("html/monitor_template.html").getPath());

		return result;
	}
	
}
