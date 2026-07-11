package eva.luca.soccorso_Web.models;

public class MaterialiMissione{

	private int materialeRif;
	private int missioneRif;
	
	
	
	public MaterialiMissione(int materialeRif, int missioneRif) {
		this.materialeRif = materialeRif;
		this.missioneRif = missioneRif;
	}
	
	public MaterialiMissione() {
		
	}
	

	public int getMaterialeRif() {
		return materialeRif;
	}

	public void setMaterialeRif(int materialeRif) {
		this.materialeRif = materialeRif;
	}

	public int getMissioneRif() {
		return missioneRif;
	}

	public void setMissioneRif(int missioneRif) {
		this.missioneRif = missioneRif;
	}
	
	
}
