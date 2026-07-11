package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Squadra;

public class SquadraDao implements IDaoRead<Squadra>, IDaoWrite<Squadra>{

	@Override
	public boolean insert(Squadra sqd) {
		String query = "INSERT INTO squadre(nome) VALUES (?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, sqd.getNome());
			
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
	public boolean delete(int id) {
		String query = "DELETE FROM squadre WHERE squadraID = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, id);
			
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
	public boolean update(Squadra s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Squadra> findAll() {
		String query = "SELECT * FROM squadre";
		
		ArrayList<Squadra> list = new ArrayList<Squadra>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Squadra sq = new Squadra();
				sq.setSquadraId(rs.getInt("squadraID"));
				sq.setNome(rs.getString("nome"));
				
				list.add(sq);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}

	@Override
	public Squadra findById(int id) {
		String query = "SELECT * FROM squadre WHERE squadraID = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			Squadra sq = new Squadra();
			
			while(rs.next()) {
				
				sq.setSquadraId(rs.getInt("squadraID"));
				sq.setNome(rs.getString("nome"));
				
			}
			
			return sq;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public Squadra findByMissioneId(int missioneId) {
		String query = "SELECT s.* " +
                    "FROM squadre s " +
                    "JOIN missioni m " +
                    "ON s.squadraID = m.squadraRIF " +
                    "WHERE m.missioneID = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, missioneId);
			ResultSet rs = ps.executeQuery();
			
			Squadra sq = new Squadra();
			
			while(rs.next()) {
				
				sq.setSquadraId(rs.getInt("squadraID"));
				sq.setNome(rs.getString("nome"));
				
			}
			
			return sq;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}

	
	public ArrayList<Squadra> findSquadreNonOperative(){
		String query = "SELECT s.* " +
		            "FROM squadre s " +
		            "WHERE EXISTS ( " +
		            "    SELECT 1 " +
		            "    FROM appartenenza a " +
		            "    WHERE a.squadraRIF = s.squadraID " +
		            ") " +
		            "AND NOT EXISTS ( " +
		            "    SELECT 1 " +
		            "    FROM appartenenza a " +
		            "    JOIN operatori o ON o.operatoreID = a.operatoreRIF " +
		            "    WHERE a.squadraRIF = s.squadraID " +
		            "    AND o.stato <> 'libero' " +
		            ")";
		
		ArrayList<Squadra> list = new ArrayList<Squadra>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Squadra sq = new Squadra();
				
				sq.setSquadraId(rs.getInt("squadraID"));
				sq.setNome(rs.getString("nome"));
				
				list.add(sq);
			}
			
			return list;
			
	    } catch (SQLException e) {
	        throw new RuntimeException("JDBC error", e);
	    }
	}

}
