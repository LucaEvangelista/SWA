package eva.luca.soccorso_Web.models;

public class Squadra {

	private String nome;
	private int squadraId;
	
	
	public Squadra(String nome) {
		this.nome = nome;
	}
	
	public Squadra() {
		
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public int getSquadraId() {
		return squadraId;
	}
	
	public void setSquadraId(int squadraId) {
		this.squadraId = squadraId;
	}
}
