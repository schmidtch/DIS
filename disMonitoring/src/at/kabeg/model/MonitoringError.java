package at.kabeg.model;

import java.util.Properties;


import org.json.JSONException;
import org.json.JSONObject;

import at.kabeg.utilities.StringReplacer;

public class MonitoringError {

	private int id;
	private String Date;
	private int application_id;
	private String description;
	private String status;
	private String event;
	private String kommentar;
	private String log;
	private String ack;
	private int duplicate_count;
	private String last_date;
	private String prio;
	private String ampel;
	private String appName;
	private boolean error;
		
	public MonitoringError(){}
	
	public MonitoringError(int id, String description, boolean error, String date){
		this.id=id;
		this.description=description;
		this.Date=date;
		this.setError(error);
	}
	
	public MonitoringError(int id, int appId, String description, String appName){
		this.setId(id);
		this.setApplication_id(appId);
		this.setDescription(description);
		this.setAppName(appName);
	}
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public int getApplication_id() {
		return application_id;
	}
	public void setApplication_id(int application_id) {
		this.application_id = application_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getKommentar() {
		return kommentar;
	}
	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getAck() {
		return ack;
	}
	public void setAck(String ack) {
		this.ack = ack;
	}
	public int getDuplicate_count() {
		return duplicate_count;
	}
	public void setDuplicate_count(int duplicate_count) {
		this.duplicate_count = duplicate_count;
	}
	public String getLast_date() {
		return last_date;
	}
	public void setLast_date(String last_date) {
		this.last_date = last_date;
	}
	public String getPrio() {
		return prio;
	}
	public void setPrio(String prio) {
		this.prio = prio;
	}
	
	public MonitoringError createError(JSONObject jo) throws JSONException{
		
		this.application_id=Integer.parseInt(jo.getString("application_id"));
		this.description=jo.getString("description");
		this.status=jo.getString("status");
		this.kommentar=jo.getString("kommentar");
		this.prio=jo.getString("prio");
		
		return this;
	}
	
	public String getHTMLOption() {
		
		return "<option value=\""+String.valueOf(this.getId())+","+String.valueOf(this.getApplication_id())+"\">"+ this.getAppName() + " - " +this.getDescription()+"</option>";
		
	}
	
	public String toDivHTMLTag(String server, String app, String serverName){
		String output = "", disp="none";
		if(ampel.equals("red")) disp="inline";
		Properties p = new Properties();
		StringReplacer sr = new StringReplacer();
		p.put("$monitoring_id", String.valueOf(this.id));
		p.put("$description", this.description);
		p.put("$server_id", server);
		p.put("$besDisp", disp);
		p.put("$application_id", app);
		p.put("$ampel", this.ampel);
		p.put("$serverName", serverName);
		sr.setReplacements(p);
		output += sr.replaceInFile(this.getClass().getResource("../../../../../html/error_entry_head_template.html").getPath());
		return output;
	}

	public boolean hasError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
		if(error)ampel="red";else ampel="green";
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
}
