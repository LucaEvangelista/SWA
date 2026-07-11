package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Patente;

public class PatenteDao implements IDaoRead<Patente>{

	@Override
	public ArrayList<Patente> findAll() {
		String query = "SELECT patenteID, tipo FROM patenti";
		
		ArrayList<Patente> list = new ArrayList<Patente>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Patente p = new Patente();
				p.setTipologia(rs.getString("tipo"));
				p.setId(rs.getInt("patenteID"));
				
				list.add(p);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}

	@Override
	public Patente findById(int id) {
		String query = "SELECT patenteID, tipo FROM patenti WHERE patenteID = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			Patente p = new Patente();
			
			while(rs.next()) {
				p.setId(rs.getInt("patenteID"));
				p.setTipologia(rs.getString("tipo"));
			}
			
			return p;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		
	}
	
	public Patente findByType(String type) {
		String query = "SELECT patenteID, tipo FROM patenti WHERE tipo = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, type);
			ResultSet rs = ps.executeQuery();
			
			Patente p = new Patente();
			
			while(rs.next()) {
				p.setId(rs.getInt("patenteID"));
				p.setTipologia(rs.getString("tipo"));
			}
			
			return p;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		
	}

}
