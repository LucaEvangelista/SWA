package eva.luca.soccorso_Web.models;

public class Competenza {
	
	private String tipologia;
	private int id;
	
	
	

	public Competenza(String tipologia) {
		
		this.tipologia = tipologia;
	}
	
	public Competenza() {
		
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
		return "Competenze [tipologia=" + tipologia + ", id=" + id + "]";
	}
	
	
}


