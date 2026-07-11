package eva.luca.soccorso_Web.models;

public class PatenteOperatore {
	
	private int operatoreRif;
	private int patenteRif;
	
	
	
	public PatenteOperatore(int operatoreRif, int patenteRif) {
		this.operatoreRif = operatoreRif;
		this.patenteRif = patenteRif;
	}
	
	public PatenteOperatore() {
		
	}
	
	
	
	public int getOperatoreRif() {
		return operatoreRif;
	}
	
	public void setOperatoreRif(int operatoreRif) {
		this.operatoreRif = operatoreRif;
	}
	
	public int getPatenteRif() {
		return patenteRif;
	}
	
	public void setPatenteRif(int patenteRif) {
		this.patenteRif = patenteRif;
	}
	
	@Override
	public String toString() {
		return "PatenteOperatore [operatoreRif=" + operatoreRif + ", patenteRif=" + patenteRif + "]";
	}
}
