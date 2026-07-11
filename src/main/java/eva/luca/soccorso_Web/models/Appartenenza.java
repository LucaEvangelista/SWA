package eva.luca.soccorso_Web.models;

public class Appartenenza {
	
	private int appartenenzaId;
	private int operatoreRif;
	private int squadraRif;
	private boolean caposquadra;
	
	
	
	public Appartenenza(int operatoreRif, int squadraRif, boolean caposquadra) {
		this.operatoreRif = operatoreRif;
		this.squadraRif = squadraRif;
		this.caposquadra = caposquadra;
	}
	
	public Appartenenza() {
		
	}
	
	
	public int getAppartenenzaId() {
		return appartenenzaId;
	}
	
	public void setAppartenenzaId(int appartenenzaId) {
		this.appartenenzaId = appartenenzaId;
	}
	
	public int getOperatoreRif() {
		return operatoreRif;
	}
	
	public void setOperatoreRif(int operatoreRif) {
		this.operatoreRif = operatoreRif;
	}
	
	public int getSquadraRif() {
		return squadraRif;
	}
	
	public void setSquadraRif(int squadraRif) {
		this.squadraRif = squadraRif;
	}
	
	public boolean isCaposquadra() {
		return caposquadra;
	}
	
	public void setCaposquadra(boolean caposquadra) {
		this.caposquadra = caposquadra;
	}

}
