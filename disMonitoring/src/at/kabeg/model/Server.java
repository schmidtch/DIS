package at.kabeg.model;

import java.util.ArrayList;
import java.util.Properties;

import at.kabeg.utilities.StringReplacer;

public class Server {

	private int id;
	private int application_id;
	private String name;
	private int error;
	private String ampel;
	private ArrayList<LogDaten> ld = new ArrayList<LogDaten>();
	private ArrayList<MonitoringError> errors = new ArrayList<MonitoringError>();
	
	public Server(){}
	
	public Server(String name){
		this.setName(name);
	}
	
	public Server(int id, int application_id, String name, int error){
		this.name=name;
		this.id=id;
		this.application_id=application_id;
		this.setError(error);
	}
	
	public Server(String name, int id, int application_id, boolean error){
		this.name=name;
		this.id=id;
		this.application_id=application_id;
		this.setError(error);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getApplication_id() {
		return application_id;
	}

	public void setApplication_id(int application_id) {
		this.application_id = application_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		if(error==1) ampel="red";
		else ampel="green";
		this.error=error;
	}
	
	public void setError(boolean error){
		if(error) this.setError(1);
		else this.setError(0);
	}

	public ArrayList<LogDaten> getLogDaten() {
		return this.ld;
	}

	public void addLogDaten(LogDaten ld){
		this.ld.add(ld);
	}

	public String getHTMLOption() {
		return "<option value=\"" + this.id + "\">" + this.name + "</option>";
	}
	
	public String toDivHTMLTag(){
		String output = "";
		Properties p = new Properties();
		StringReplacer sr = new StringReplacer();
		p.put("$id", String.valueOf(this.id));
		p.put("$application_id", String.valueOf(this.application_id));
		p.put("$name", this.name);
		p.put("$ampel", this.ampel);
		sr.setReplacements(p);
		output += sr.replaceInFile(this.getClass().getResource("../../../../../html/server_entry_head_template.html").getPath());
		
		if(errors.isEmpty()){
			output +="<div>No Error for this Server</div>";
		} else {
			for(MonitoringError e : this.errors){
				output += e.toDivHTMLTag(String.valueOf(this.id), String.valueOf(this.application_id), this.name);
			}
		}
		output +="</div><br style=\"clear:both;\" />";
		return output;
	}
	public ArrayList<MonitoringError> getErrors() {
		return errors;
	}
	public void setErrors(ArrayList<MonitoringError> errors) {
		this.errors = errors;
	}
	public void addError(MonitoringError e){
		this.errors.add(e);
	}
}
