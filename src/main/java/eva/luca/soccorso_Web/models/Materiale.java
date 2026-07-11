package eva.luca.soccorso_Web.models;

public class Materiale {
	
	
	private String tipologia;
	private String seriale;
	private String status;
	private int id;
	
	
	public Materiale(String tipologia, String seriale) {
		
		this.tipologia = tipologia;
		this.seriale = seriale;
	}
	
	public Materiale() {
		
	}
	
	
	public String getSeriale() {
		return seriale;
	}

	public void setSeriale(String seriale) {
		this.seriale = seriale;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
