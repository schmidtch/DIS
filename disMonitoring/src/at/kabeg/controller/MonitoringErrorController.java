package at.kabeg.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import at.kabeg.model.Application;
import at.kabeg.model.MonitoringError;
import at.kabeg.model.Server;
import at.kabeg.utilities.DBConnectionHandler;
import at.kabeg.utilities.DBQueryHandler;
import at.kabeg.utilities.Queries;
import at.kabeg.utilities.StringReplacer;

public class MonitoringErrorController {

	public void insertMonitoringError(MonitoringError me, String[] serverIDList) throws SQLException {
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();

		p.put("$appid", String.valueOf(me.getApplication_id()));
		p.put("$description", me.getDescription());
		p.put("$status", me.getStatus());
		p.put("$kommentar", me.getKommentar());
		p.put("$prio", me.getPrio());

		sr.setReplacements(p);
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.insertMonitoringError.toString())));

		for (String s : serverIDList) {
			if (s != null && !s.equals("")) {
				p.put("$id", s);
				sr.setReplacements(p);
				stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateServer.toString())));
			}
		}

		DBConnectionHandler.closeConnection(null, stmt);
	}

	public ArrayList<MonitoringError> getMonitoringJobs() throws SQLException {
		ArrayList<MonitoringError> errors = new ArrayList<MonitoringError>();
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		ResultSet rs = stmt.executeQuery(DBQueryHandler.getQuery(Queries.getMonitoringJobs.toString()));
		while (rs.next()) {
			errors.add(new MonitoringError(Integer.parseInt(rs.getString("monitoring_id")),
					Integer.parseInt(rs.getString("application_id")), rs.getString("description"),
					rs.getString("name")));
		}
		DBConnectionHandler.closeConnection(rs, stmt);
		return errors;
	}

	public String getMonitoringErrorByApplicationID(String appID) throws SQLException {
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		p.put("$application_id", appID);
		sr.setReplacements(p);
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		ResultSet rs = stmt.executeQuery(
				sr.replaceInString(DBQueryHandler.getQuery(Queries.getOverviewByApplicationID.toString())));
		String retour = "";
		while (rs.next()) {
			retour += "<tr><td style=\"border-left: 1px solid #D0D0D0;\">"+rs.getString("description")+"</td><td>"+rs.getString("server")+"</td><td>"+rs.getString("vorname")+rs.getString("nachname")+"</td><td>"+rs.getString("bereitschaft")+"</td></tr>";
		}

		return retour;
	}

	public String getMonitoringOverview() throws SQLException {
		ArrayList<Application> apps = new ArrayList<Application>();
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();

		ResultSet rsApps = stmt.executeQuery(DBQueryHandler.getQuery(Queries.getApplications.toString()));
		while (rsApps.next()) {
			apps.add(new Application(rsApps.getInt("application_id"), rsApps.getString("name"), false));
		}

		ResultSet rsServer = stmt.executeQuery(DBQueryHandler.getQuery(Queries.getServer.toString()));
		int appId = 0;
		while (rsServer.next()) {
			appId = rsServer.getInt("application_id");
			for (Application a : apps) {
				if (a.getId() == appId) {
					a.addServer(new Server(rsServer.getInt("server_id"), appId, rsServer.getString("name"),0));
				}
			}
		}

		ResultSet rsError = stmt.executeQuery(DBQueryHandler.getQuery(Queries.getMonitoringOverview.toString()));
		ResultSet rsCheck = null;
		int serverId = 0;
		int locError = 0;
		boolean error = false;
		while (rsError.next()) {
			appId = rsError.getInt("application_id");
			serverId = rsError.getInt("server_id");
			locError = rsError.getInt("error");
			if (locError == 0) {
				error = false;
			} else {
				error = true;
			}
			for (Application a : apps) {
				if (a.getId() == appId) {
					for (Server s : a.getServers()) {
						if (s.getId() == serverId) {
							s.addError(new MonitoringError(rsError.getInt("monitoring_id"),	rsError.getString("description"), error, rsError.getString("datum")));
						}
					}
				}
			}
		}

		if (!apps.isEmpty()) {
			StringReplacer sr = new StringReplacer();
			Properties p = new Properties();
			for (Application a : apps) {
				for (Server s : a.getServers()) {
					for (MonitoringError me : s.getErrors()) {
						p.put("$server_id", String.valueOf(s.getId()));
						p.put("$monitoring_id", String.valueOf(me.getId()));
						sr.setReplacements(p);
						rsCheck = stmt.executeQuery(sr.replaceInString(DBQueryHandler.getQuery(Queries.getErrorAnzahlForServer.toString())));
						if (rsCheck.next()) {
							if (rsCheck.getInt("anzahl") > 0) {
								me.setError(true);
							} else {
								me.setError(false);
							}
						}

					}
				}
			}
		}
		
		String output = "";

		if (!apps.isEmpty()) {

			for (Application a : apps) {
				for (Server s : a.getServers()) {
					for (MonitoringError m : s.getErrors()) {
						if (m.hasError()) {
							s.setError(true);
						}
					}
					if (s.getError() == 1) {
						a.setError(true);
					}
				}
				output += a.toDivHTMLTag();
			}
		}
		rsApps.close();
		rsServer.close();
		rsError.close();
		if (rsCheck != null)
			rsCheck.close();

		DBConnectionHandler.closeConnection(null, stmt);
		return output;
	}

	public void updateMonitoringErrorSetAck(String monitoringId, String server_id, String app_id, String error)
			throws SQLException {
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		p.put("$monitoring_id", monitoringId);
		p.put("$server_id", server_id);
		p.put("$application_id", app_id);
		p.put("$error", error);
		sr.setReplacements(p);
		Statement stmt = DBConnectionHandler.getConnection(false).createStatement();
		stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateMonitoringErrorSetAck.toString())));
		stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateHistorySetEndDate.toString())));
		stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateServerSetError.toString())));
		ResultSet rs = stmt
				.executeQuery(sr.replaceInString(DBQueryHandler.getQuery(Queries.getErrorServerByAppId.toString())));

		if (rs.isBeforeFirst()) {
			error = "1";
			p.put("$error", error);
			while (rs.next()) {
				p.put("$monitoring_id", String.valueOf(rs.getInt("monitoring_id")));
				p.put("$server_id", String.valueOf(rs.getInt("server_id")));
				sr.setReplacements(p);
				stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateErrorDate.toString())));
				stmt.execute(sr.replaceInString(DBQueryHandler.getQuery(Queries.updateServerSetError.toString())));
			}
		}
		DBConnectionHandler.closeConnection(rs, stmt);

	}

}
