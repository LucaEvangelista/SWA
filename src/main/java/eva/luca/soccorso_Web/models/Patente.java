package eva.luca.soccorso_Web.models;

public class Patente {
	
	private String tipologia;
	private int id;
	
	
	

	public Patente(String tipologia) {
		
		this.tipologia = tipologia;
	}
	
	public Patente() {
		
	}
	
	
	public String getTipologia() {
		return tipologia;
	}
	
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Patente [tipologia=" + tipologia + ", id=" + id + "]";
	}
	
	
}
