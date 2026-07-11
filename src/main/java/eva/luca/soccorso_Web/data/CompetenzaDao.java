package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Competenza;
public class CompetenzaDao implements IDaoRead<Competenza> {

	@Override
	public ArrayList<Competenza> findAll() {
		String query = "SELECT abilitaID, tipo FROM abilita";
		
		ArrayList<Competenza> list = new ArrayList<Competenza>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Competenza c = new Competenza();
				c.setTipologia(rs.getString("tipo"));
				c.setId(rs.getInt("abilitaID"));
				
				list.add(c);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}

	@Override
	public Competenza findById(int id) {
		String query = "SELECT patenteID, tipo FROM patenti WHERE patenteID = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			Competenza c = new Competenza();
			
			while(rs.next()) {
				c.setId(rs.getInt("patenteID"));
				c.setTipologia(rs.getString("tipo"));
			}
			
			return c;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		
	}
	
	public Competenza findByType(String type) {
		String query = "SELECT patenteID, tipo FROM patenti WHERE tipo = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			ps.setString(1, type);
			ResultSet rs = ps.executeQuery();
			
			Competenza c = new Competenza();
			
			while(rs.next()) {
				c.setId(rs.getInt("patenteID"));
				c.setTipologia(rs.getString("tipo"));
			}
			
			return c;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		
	}

}
