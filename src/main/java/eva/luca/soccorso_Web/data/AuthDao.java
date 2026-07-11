package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.LoggedUser;

public class AuthDao {
	
	private LoggedUser findAdmin(String mail, String pass) {
		String query = "SELECT amministratoreID, nome, email "
					+ "FROM amministratori "
					+ "WHERE email = ? AND passkey = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, mail);
			ps.setString(2, pass);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				LoggedUser lu = new LoggedUser();
				
				lu.setId(rs.getInt("amministratoreID"));
				lu.setNome(rs.getString("nome"));
				lu.setEmail(rs.getString("email"));
				lu.setRuolo("admin");
				
				return lu;
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	private LoggedUser findOperatore(String mail, String pass) {
		String query = "SELECT operatoreID, nome, email "
					+ "FROM operatori "
					+ "WHERE email = ? AND passkey = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, mail);
			ps.setString(2, pass);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				LoggedUser lu = new LoggedUser();
				
				lu.setId(rs.getInt("operatoreID"));
				lu.setNome(rs.getString("nome"));
				lu.setEmail(rs.getString("email"));
				lu.setRuolo("operator");
				
				return lu;
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public LoggedUser login(String mail, String pass) {
		
		LoggedUser admin = findAdmin(mail, pass);
		
		if(admin != null) {
			return admin;
		}
		
		return findOperatore(mail, pass);
	}
}
