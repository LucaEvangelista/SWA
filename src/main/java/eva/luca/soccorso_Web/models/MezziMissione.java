package eva.luca.soccorso_Web.models;

public class MezziMissione {
	
	private int mezzoRif;
	private int missioneRif;
	
	
	
	public MezziMissione(int mezzoRif, int missioneRif) {
		this.mezzoRif = mezzoRif;
		this.missioneRif = missioneRif;
	}
	
	public MezziMissione() {
		
	}
	
	
	public int getMezzoRif() {
		return mezzoRif;
	}
	
	public void setMezzoRif(int mezzoRif) {
		this.mezzoRif = mezzoRif;
	}
	
	public int getMissioneRif() {
		return missioneRif;
	}
	
	public void setMissioneRif(int missioneRif) {
		this.missioneRif = missioneRif;
	}
}
