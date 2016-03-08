package at.kabeg.model;

public class Supervisor {
	
	private int id;
	private String vorname;
	private String nachname;
	private String email;
	private String telefon;
	
	public Supervisor(){}
	
	public Supervisor(int id, String vorname, String nachname, String email, String telefon){
		this.setId(id);
		this.setVorname(vorname);
		this.setNachname(nachname);
		this.setEmail(email);
		this.setTelefon(telefon);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	
	public String getHTMLOption(){
			
		return "<option value=\""+String.valueOf(this.getId())+"\">"+this.getNachname()+"</option>";
		
	}
	

}
