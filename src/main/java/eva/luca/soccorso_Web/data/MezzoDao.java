package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Mezzo;

public class MezzoDao implements IDaoRead<Mezzo>, IDaoWrite<Mezzo> {

	@Override
	public ArrayList<Mezzo> findAll() {
		String query = "SELECT * FROM mezzi";
		
		ArrayList<Mezzo> list = new ArrayList<Mezzo>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Mezzo mz = new Mezzo();
				mz.setTipologia(rs.getString("tipo"));
				mz.setSeriale(rs.getString("seriale"));
				mz.setId(rs.getInt("mezzoID"));
				mz.setStatus(rs.getString("stato"));
				
				list.add(mz);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}
	
	public ArrayList<Mezzo> findAllLiberi() {
		String query = "SELECT * FROM mezzi WHERE stato = ?";
		
		ArrayList<Mezzo> list = new ArrayList<Mezzo>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, "libero");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Mezzo mz = new Mezzo();
				mz.setTipologia(rs.getString("tipo"));
				mz.setSeriale(rs.getString("seriale"));
				mz.setId(rs.getInt("mezzoID"));
				mz.setStatus(rs.getString("stato"));
				
				list.add(mz);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}

	@Override
	public Mezzo findById(int id) {
		String query = "SELECT * FROM mezzi WHERE mezzoID = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				Mezzo mz = new Mezzo();
				
				mz.setId(rs.getInt("mezzoID"));
				mz.setTipologia(rs.getString("tipo"));
				mz.setSeriale(rs.getString("seriale"));
				mz.setStatus(rs.getString("stato"));
				
				return mz;
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		
	}
	
	public Mezzo findByType(String type) {
		String query = "SELECT * FROM mezzi WHERE tipo = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, type);
			ResultSet rs = ps.executeQuery();
			
			Mezzo mz = new Mezzo();
			
			while(rs.next()) {
				mz.setId(rs.getInt("mezzoID"));
				mz.setSeriale(rs.getString("seriale"));
				mz.setTipologia(rs.getString("tipo"));
				mz.setStatus(rs.getString("stato"));
			}
			
			return mz;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		
	}
	
	public Mezzo findBySeriale(String seriale) {
		String query = "SELECT * FROM mezzi WHERE seriale = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, seriale);
			ResultSet rs = ps.executeQuery();
			
			Mezzo mz = new Mezzo();
			
			while(rs.next()) {
				mz.setId(rs.getInt("mezzoID"));
				mz.setSeriale(rs.getString("seriale"));
				mz.setTipologia(rs.getString("tipo"));
				mz.setStatus(rs.getString("stato"));
			}
			
			return mz;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		
	}

	@Override
	public boolean insert(Mezzo mz) {
		String query = "INSERT INTO mezzi(tipo,seriale) VALUES (?, ?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, mz.getTipologia());
			ps.setString(2, mz.getSeriale());
			
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
		String query = "DELETE FROM mezzi WHERE mezzoID = ?";
		
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
	public boolean update(Mezzo mz) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean updateStatusByMissioneId(int missioneId, String stato) {
		String query = "UPDATE mezzi mz " +
	                       "JOIN mezzi_missioni mm " +
	                       "ON mz.mezzoID = mm.mezzoRIF " +
	                       "SET mz.stato = ? " +
	                       "WHERE mm.missioneRIF = ?";

	    try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){

	        ps.setString(1, stato);
	        ps.setInt(2, missioneId);
	        
			int affRows = ps.executeUpdate();
			
			if(affRows > 0) {
				return true;
			}
		} catch (Exception e) {
			throw new RuntimeException("JDBC error", e);
		}
		return false;
	}

}
