package at.kabeg.utilities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import at.kabeg.model.*;

public class MonitorViewParser {
	private ResultSet rs, rs2;
	private ArrayList<Application> applications = new ArrayList<Application>();

	public MonitorViewParser(ResultSet rs) throws SQLException {
		this.rs = rs;
		this.setApplicationsFromResultSet();
		this.addErrorsToApplications();
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	public void addApplication(Application a) {
		applications.add(a);
	}

	private void setApplicationsFromResultSet() throws SQLException {
		boolean error = false;
		if (rs != null) {
			while (rs.next()) {
				error = false;
				if (applications.isEmpty()) {
					addApplication(new Application(rs.getInt("application_id"), rs.getString("Application"), error,
							new Server(rs.getString("Server"), rs.getInt("server_id"), rs.getInt("application_id"),
									error)));
				} else {
					boolean found = false;
					for (Application a : applications) {
						if (rs.getString("Application").equalsIgnoreCase(a.getName())) {
							found = true;
							break;
						}
					}
					if (!found) {
						addApplication(new Application(rs.getInt("application_id"), rs.getString("Application"), error,
								new Server(rs.getString("Server"), rs.getInt("server_id"), rs.getInt("application_id"),
										error)));
						found = false;
					} else {
						for (Application a : applications) {
							if (a.getName().equals(rs.getString("Application"))) {
								a.addServer(new Server(rs.getString("Server"), rs.getInt("server_id"),
										rs.getInt("application_id"), error));
							}
						}
					}
				}
			}
		}
	}

	private void addErrorsToApplications() throws SQLException {
		boolean error = false;
		Properties p = new Properties();
		StringReplacer sr = new StringReplacer();
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		if (rs != null) {
			rs.beforeFirst();
			if (!applications.isEmpty()) {
				while (rs.next()) {
					for (Application a : this.applications) {
						if (a.getName().equalsIgnoreCase(rs.getString("Application"))) {
							for (Server s : a.getServers()) {
								error = false;
								if (s.getName().equalsIgnoreCase(rs.getString("Server"))) {
									if (rs.getString("datum") != null && rs.getString("ERROR").equals("1")) {
										p.put("$server_id", String.valueOf(rs.getInt("server_id")));
										p.put("$monitoring_id", String.valueOf(rs.getInt("monitoring_id")));
										sr.setReplacements(p);

										rs2 = stmt.executeQuery(sr.replaceInString(
												DBQueryHandler.getQuery(Queries.getErrorAnzahlForServer.toString())));

										if (rs2.next()) {
											if (rs2.getInt("anzahl") > 0) {
												error = true;
											}
										}

									}
									s.addError(new MonitoringError(rs.getInt("monitoring_id"),
											rs.getString("description"), error, rs.getString("datum")));
								}
							}
						}
					}
				}
			}
		}
	}

	public String getApplicationsInHTML() {

		String output = "";

		if (!applications.isEmpty()) {

			for (Application a : applications) {
				for (Server s : a.getServers()) {
					for (MonitoringError m : s.getErrors()) {
						if (m.hasError() && s.getError() == 0) {
							s.setError(true);
						}
					}
					if (s.getError() == 1 && !a.hasError()) {
						a.setError(true);
					}
				}
				output += a.toDivHTMLTag();
			}
		}

		return output;

	}

	/*
	 * public void closeResultSet() throws SQLException{ rs.close(); }
	 */

}
