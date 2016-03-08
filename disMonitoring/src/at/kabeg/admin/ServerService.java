package at.kabeg.admin;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import at.kabeg.controller.ServerController;
import at.kabeg.model.Server;

@Path("/server")
public class ServerService {

	@GET
	@Path("/{id}")
	@Produces({ MediaType.TEXT_HTML })
	public String getServer(@PathParam("id") String id) {

		String monitoringOption = "<option value=\"\">==Choose==</option>";
		ServerController sc = new ServerController();
		
		ArrayList<Server> servers;
		try {
			servers = sc.getServerByMonitoringId(id);
			if(servers.isEmpty()){
				for(Server s : servers) {
					monitoringOption = monitoringOption + s.getHTMLOption();
				}	
			}
		} catch (SQLException e) { e.printStackTrace(); }

		return monitoringOption;
	}
	
}
