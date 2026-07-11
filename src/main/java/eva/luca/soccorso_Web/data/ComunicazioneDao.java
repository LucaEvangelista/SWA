package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Comunicazione;

public class ComunicazioneDao implements IDaoWrite<Comunicazione>, IDaoRead<Comunicazione> {

	@Override
	public ArrayList<Comunicazione> findAll() {
		String query = "SELECT * FROM comunicazioni";
		
		ArrayList<Comunicazione> list = new ArrayList<Comunicazione>();
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Comunicazione cm = new Comunicazione();
				cm.setId(rs.getInt("comunicazioneID"));
				cm.setMissioneRif(rs.getInt("missioneRIF"));
				cm.setContenuto(rs.getString("contenuto"));
				cm.setRuolo(rs.getString("ruolo"));
				cm.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
				
				list.add(cm);
			}
			
		} catch (Exception e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}

	@Override
	public Comunicazione findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<Comunicazione> findByMissionId(int missionId) {
		String query = "SELECT * " +
                    "FROM comunicazioni " +
                    "WHERE missioneRIF = ? " +
                    "ORDER BY created_at ASC";
		
		ArrayList<Comunicazione> list = new ArrayList<Comunicazione>();
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, missionId);
			ResultSet rs = ps.executeQuery();
			
			
			
			while(rs.next()) {
				Comunicazione cm = new Comunicazione();
				
				cm.setId(rs.getInt("comunicazioneID"));
				cm.setMissioneRif(rs.getInt("missioneRIF"));
				cm.setContenuto(rs.getString("contenuto"));
				cm.setRuolo(rs.getString("ruolo"));
				cm.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
				
				list.add(cm);
			}
			
			return list;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}

	@Override
	public boolean insert(Comunicazione c) {
		String query = "INSERT INTO comunicazioni(contenuto, missioneRIF, ruolo) VALUES (?, ?, ?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, c.getContenuto());
			ps.setInt(2, c.getMissioneRif());
			ps.setString(3, c.getRuolo());
			
			int affRows = ps.executeUpdate();
			
			if(affRows > 0) {
				return true;
			}
		} catch (Exception e) {
			throw new RuntimeException("JDBC error", e);
		}
		
		return false;
	}

	@Override
	public boolean delete(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Comunicazione t) {
		// TODO Auto-generated method stub
		return false;
	}

}
