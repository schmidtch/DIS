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
	@Produces({ MediaType.TEXT_HTML })
	public String start(String input) {
		
		String result = "", entry="";
		
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		ArrayList<Application> apps = new ArrayList<Application>();
		MonitoringErrorController mec = new MonitoringErrorController();
		try {
			apps = mec.getMonitoringOverview();
			if(!apps.isEmpty()){
				
				for(Application a : apps){
					for(Server s : a.getServers()){
						for(MonitoringError m : a.getErrors()){
							if(m.hasError() && s.getError()==0){
								s.setError(true);
							}
						}
						if(s.getError()==1 && !a.hasError()){
							a.setError(true);
						}
					}
					entry+=a.toDivHTMLTag();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{}
		
		p=new Properties();
		p.put("$monitorContent", entry);
		sr.setReplacements(p);
		result = sr.replaceInFile(this.getClass().getResource("../../../../../html/monitor_template.html").getPath());

		return result;
	}
	
}
