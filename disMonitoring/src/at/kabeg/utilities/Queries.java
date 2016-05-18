package at.kabeg.utilities;

public enum Queries {
	getSupervisors("getSupervisors"),
	getCompanies("getCompanies"),
	getApplications("getApplications"),
	getServer("getServer"),
	getServerByMonitoringId("getServerByMonitoringId"),
	insertApplication("insertApplication"),
	insertMonitoringError("insertMonitoringError"),
	getMonitoringOverview("getMonitoringOverview"),
	getMonitoringJobs("getMonitoringJobs"),
	getTaskByServerName("getTaskByServerName"),
	getMaxHistoryForTask("getMaxHistoryForTask"),
	getErrorServerByAppId("getErrorServerByAppId"),
	getErrorAnzahlForServer("getErrorAnzahlForServer"),
	getHistoryByMIDAndSID("getHistoryByMIDAndSID"),
	getOverviewByApplicationID("getOverviewByApplicationID"),
	getMonitoringError("getMonitoringError"),
	insertHistory("insertHistory"),
	insertCompany("insertCompany"),
	insertServer("insertServer"),
	insertLogdataForServer("insertLogdataForServer"),
	updateMonitoringErrorById("updateMonitoringErrorById"),
	updateMonitoringErrorSetAck("updateMonitoringErrorSetAck"),
	updateHistorySetEndDate("updateHistorySetEndDate"),
	updateServerSetError("updateServerSetError"),
	updateServer("updateServer"),
	updateErrorDate("updateErrorDate");
	
	private String query;
	private Queries(String query){
		this.query = query;
	}
	
	@Override
	public String toString(){
		return this.query;
	}
}
