package eva.luca.soccorso_Web.models;

public class Missione {
	
	private String obiettivo;
	private String posizione;
	private String status;
	private int richiestaRif;
	private int squadraRif;
	private int missioneId;


	public Missione(String obiettivo, String posizione, int richiestaRif, int squadraRif) {
		this.obiettivo = obiettivo;
		this.posizione = posizione;
		this.richiestaRif = richiestaRif;
		this.squadraRif = squadraRif;
	}
	
	public Missione() {
		
	}
	
	public String getObiettivo() {
		return obiettivo;
	}
	
	public void setObiettivo(String obiettivo) {
		this.obiettivo = obiettivo;
	}
	
	public String getPosizione() {
		return posizione;
	}
	
	public void setPosizione(String posizione) {
		this.posizione = posizione;
	}
	
	public int getRichiestaRif() {
		return richiestaRif;
	}
	
	public void setRichiestaRif(int richiestaRif) {
		this.richiestaRif = richiestaRif;
	}
	
	public int getSquadraRif() {
		return squadraRif;
	}
	
	public void setSquadraRif(int squadraRif) {
		this.squadraRif = squadraRif;
	}
	
	public int getMissioneId() {
		return missioneId;
	}
	
	public void setMissioneId(int missioneId) {
		this.missioneId = missioneId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
