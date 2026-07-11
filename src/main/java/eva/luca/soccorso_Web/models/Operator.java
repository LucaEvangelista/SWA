package eva.luca.soccorso_Web.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Operator {
	
	private String name;
	private String surname;
	private String email;
	private LocalDate age;
	private int id;
	private String passkey;
	private String status;
	
	
	public Operator(String name, String surname, String email, LocalDate age, String passkey) {
		
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.age = age;
		this.passkey = passkey;
	}
	



	public Operator(){
		
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public LocalDate getAge() {
		return age;
	}
	
	public void setAge(LocalDate age) {
		this.age = age;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)	
	public String getPasskey() {
		return passkey;
	}


	public void setPasskey(String passkey) {
		this.passkey = passkey;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Operator [name=" + name + ", surname=" + surname + ", email=" + email + ", age=" + age + "]";
	}
	
	

}
