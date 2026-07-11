package eva.luca.soccorso_Web.data.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {
	
	private ConnectionFactory(){
		
	}
	
	static {
		try {
			Class.forName(DBConfig.DRIVER);
			System.out.println("Drivare JDBC succesfully loaded");
		} catch (Exception e) {
			throw new RuntimeException("Driver JDBC not found", e);
		}
	}
	
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
	}

}
