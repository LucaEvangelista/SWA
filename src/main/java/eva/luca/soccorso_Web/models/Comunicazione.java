package eva.luca.soccorso_Web.models;

import java.time.LocalDateTime;

public class Comunicazione {
	
	private int id;
	private String contenuto;
	private String ruolo;
	private LocalDateTime createdAt;
	private int missioneRif;
	
	
	
	public Comunicazione() {
		
	}
	
	public Comunicazione(String contenuto, LocalDateTime createdAt, int missioneRif) {
		
		this.contenuto = contenuto;
		this.createdAt = createdAt;
		this.missioneRif = missioneRif;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContenuto() {
		return contenuto;
	}

	public void setContenuto(String contenuto) {
		this.contenuto = contenuto;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public int getMissioneRif() {
		return missioneRif;
	}

	public void setMissioneRif(int missioneRif) {
		this.missioneRif = missioneRif;
	}
	
	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	
	

}
