package at.kabeg.model;

public class LogDaten {

	private int logdaten_id;
	private String search_string;
	private String logdir;
	private String logfile;
	
	public LogDaten(){}
	
	public LogDaten(String search_string, String logdir, String logfile){
		this.setSearch_string(search_string);
		this.setLogdir(logdir);
		this.setLogfile(logfile);
	}

	public int getLogdaten_id() {
		return logdaten_id;
	}

	public void setLogdaten_id(int logdaten_id) {
		this.logdaten_id = logdaten_id;
	}

	public String getSearch_string() {
		return search_string;
	}

	public void setSearch_string(String search_string) {
		this.search_string = search_string;
	}

	public String getLogdir() {
		return logdir;
	}

	public void setLogdir(String logdir) {
		this.logdir = logdir;
	}

	public String getLogfile() {
		return logfile;
	}

	public void setLogfile(String logfile) {
		this.logfile = logfile;
	}
	
	
	
	
}
