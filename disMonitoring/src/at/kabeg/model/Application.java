package at.kabeg.model;

import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;

import at.kabeg.utilities.StringReplacer;

public class Application {

	private int id;
	private int supervisor_id;
	private int company_id;
	private String name;
	private String beschreibung;
	private String bereitschaft;
	private String datum;
	private boolean error;
	private String ampel;
	private ArrayList<Server> servers = new ArrayList<Server>();
	private ArrayList<MonitoringError> errors = new ArrayList<MonitoringError>();

	public Application() {
	}

	public Application(int id, int supervisor_id, int company_id, String name, String beschreibung, String bereitschaft) {
		this.setId(id);
		this.setSupervisor_id(supervisor_id);
		this.setCompany_id(company_id);
		this.setName(name);
		this.setBeschreibung(beschreibung);
		this.setBereitschaft(bereitschaft);
	}
	
	public Application(int supervisor_id, int company_id, String name, String beschreibung, String bereitschaft) {
		this.setSupervisor_id(supervisor_id);
		this.setCompany_id(company_id);
		this.setName(name);
		this.setBeschreibung(beschreibung);
		this.setBereitschaft(bereitschaft);
	}
	
	public Application(int id, String name, boolean error, Server s){
		this.id=id;
		this.name=name;
		this.setError(error);
		this.addServer(s);
	}
	
	public Application(int id, String name, boolean error){
		this.id=id;
		this.name=name;
		this.setError(error);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSupervisor_id() {
		return supervisor_id;
	}

	public void setSupervisor_id(int supervisor_id) {
		this.supervisor_id = supervisor_id;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getBereitschaft() {
		return bereitschaft;
	}

	public void setBereitschaft(String bereitschaft) {
		this.bereitschaft = bereitschaft;
	}

	public static Application setApplicationFromJSON(JSONObject jo)
			throws JSONException {
		return new Application(Integer.parseInt(jo.getString("supervisor")),
				Integer.parseInt(jo.getString("company")),
				jo.getString("name"), jo.getString("beschreibung"),
				jo.getString("bereitschaft"));
	}

	public String getHTMLOption() {
		return "<option value=\"" + this.id + "\">" + this.name + "</option>";
	}
	
	public String toDivHTMLTag(){
		StringReplacer sr = new StringReplacer();
		Properties p = new Properties();
		String output = "";
		p.put("$id", String.valueOf(this.id));
		p.put("$name", this.name);
		p.put("$ampel", this.ampel);		
		sr.setReplacements(p);
		output += sr.replaceInFile(this.getClass().getResource("../../../../../html/app_entry_head_template.html").getPath());
		for(Server s : servers){
			output += s.toDivHTMLTag();
		}
		output += "</div>";
		return output;
	}
	
	@Override
	public String toString(){
		return this.getName()+" "+this.getId()+" "+this.ampel;
	}

	public ArrayList<Server> getServers() {
		return servers;
	}

	public void setServers(ArrayList<Server> servers) {
		this.servers = servers;
	}
	
	public void addServer(Server s){
		boolean found = false;
		if(servers.isEmpty()){
			found = false;
		} else {
			for (Server s2 : servers){
				if(s2.getName().equals(s.getName())){
					found = true;
					break;
				}
				
			}
		}
		if (!found) this.servers.add(s);
	}
	
	public void addError(MonitoringError me){
		this.errors.add(me);
	}
	
	public void setError(boolean error){
		this.error=error;
		if(error){
			this.ampel="red";
		} else {
			this.ampel="green";
		}
	}
	
	public boolean hasError(){
		return this.error;
	}

	public ArrayList<MonitoringError> getErrors() {
		return errors;
	}

	public void setErrors(ArrayList<MonitoringError> errors) {
		this.errors = errors;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}
}
