package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;

public class TokenBlackListDao {
	
	public boolean insert(String token, Instant expireAt) {
        String query = "INSERT IGNORE INTO token_blacklist "
                + "(token, expires_at) VALUES (?, ?)";
        
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, token);
			ps.setTimestamp(2, Timestamp.from(expireAt));
			
			int affRows = ps.executeUpdate();
			
			if(affRows > 0) {
				return true;
			}
		} catch (Exception e) {
			throw new RuntimeException("JDBC error", e);
		}
		return false;
	}
	
	public boolean isBlackListed(String token) {
        
		String query = "SELECT 1 "
                + "FROM token_blacklist "
                + "WHERE token = ? "
                + "AND expires_at > CURRENT_TIMESTAMP "
                + "LIMIT 1";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, token);
			
			ResultSet rs = ps.executeQuery();
			
			return rs.next();
			
		} catch (Exception e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	

}
