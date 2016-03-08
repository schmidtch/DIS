package at.kabeg.model;

public class Company {

	private int id;
	private String name;
	private String telefon;
	private String email;
	
	public Company(){}
	
	public Company(int id, String name, String telefon, String email){
		this.setId(id);
		this.setName(name);
		this.setTelefon(telefon);
		this.setEmail(email);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHTMLOption(){
			
		return "<option value=\""+String.valueOf(this.getId())+"\">"+this.getName()+"</option>";
		
	}
	
}
