package eva.luca.soccorso_Web.models;

public class Admin {
	
	private String name;
	private String email;
	private String passkey;
	private int id;
	
	
	public Admin(String name, String email, String passkey) {

		this.name = name;
		this.email = email;
		this.passkey = passkey;
	}
	
	public Admin() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasskey() {
		return passkey;
	}

	public void setPasskey(String passkey) {
		this.passkey = passkey;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
