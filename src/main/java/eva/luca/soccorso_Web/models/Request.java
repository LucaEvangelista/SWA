package eva.luca.soccorso_Web.models;

import java.time.LocalDateTime;

public class Request {
	
	private String nomePersona;
	private String mailPersona;
	private String descrizione;
	private String indirizzo;
	private int id;
	
	private LocalDateTime createdAt;
	private LocalDateTime workingAt;
	private LocalDateTime closedAt;
	
	private String fase;
	
	private String uuid;
	


	public Request(String nomePersona, String mailPersona, String indirizzo, String descrizione, String uuid) {
		
		this.nomePersona = nomePersona;
		this.mailPersona = mailPersona;
		this.indirizzo = indirizzo;
		this.descrizione = descrizione;
		this.uuid = uuid;
		
	}

	public Request(){
		
	}
	
	public String getNomePersona() {
		return nomePersona;
	}

	public void setNomePersona(String nomePersona) {
		this.nomePersona = nomePersona;
	}

	public String getMailPersona() {
		return mailPersona;
	}

	public void setMailPersona(String mailPersona) {
		this.mailPersona = mailPersona;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getWorkingAt() {
		return workingAt;
	}

	public void setWorkingAt(LocalDateTime workingAt) {
		this.workingAt = workingAt;
	}

	public LocalDateTime getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(LocalDateTime closedAt) {
		this.closedAt = closedAt;
	}

	public String getFase() {
		return fase;
	}

	public void setFase(String fase) {
		this.fase = fase;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	

}
