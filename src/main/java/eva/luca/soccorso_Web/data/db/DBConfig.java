package eva.luca.soccorso_Web.data.db;



public class DBConfig {
	
	private DBConfig() {
		
	}
	
	public static final String URL = "jdbc:mysql://localhost:3306/soccorso_web?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
	public static final String USER = "root";
	public static final String PASSWORD = "toor";
	
	public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	

}
