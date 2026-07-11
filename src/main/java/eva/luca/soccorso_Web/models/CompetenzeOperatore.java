package eva.luca.soccorso_Web.models;

public class CompetenzeOperatore {
	
	private int operatoreRif;
	private int abilitaRif;
	
	
	
	public CompetenzeOperatore(int operatoreRif, int abilitaRif) {
		this.operatoreRif = operatoreRif;
		this.abilitaRif = abilitaRif;
	}
	
	public CompetenzeOperatore() {

	}

	
	
	public int getOperatoreRif() {
		return operatoreRif;
	}
	
	public void setOperatoreRif(int operatoreRif) {
		this.operatoreRif = operatoreRif;
	}
	
	public int getAbilitaRif() {
		return abilitaRif;
	}
	
	public void setAbilitaRif(int abilitaRif) {
		this.abilitaRif = abilitaRif;
	}
	
		@Override
	public String toString() {
		return "CompetenzeOperatore [operatoreRif=" + operatoreRif + ", abilitaRif=" + abilitaRif + "]";
	}
}
