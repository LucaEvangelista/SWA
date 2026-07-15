package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Materiale;

public class MaterialeDao implements IDaoRead<Materiale>, IDaoWrite<Materiale>{

	@Override
	public ArrayList<Materiale> findAll() {
		String query = "SELECT * FROM materiali";
		
		ArrayList<Materiale> list = new ArrayList<Materiale>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Materiale m = new Materiale();
				m.setTipologia(rs.getString("tipo"));
				m.setSeriale(rs.getString("seriale"));
				m.setId(rs.getInt("materialeID"));
				m.setStatus(rs.getString("stato"));
				
				list.add(m);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}
	
	public ArrayList<Materiale> findAllLiberi() {
		String query = "SELECT * FROM materiali WHERE stato = ?";
		
		ArrayList<Materiale> list = new ArrayList<Materiale>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, "libero");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Materiale m = new Materiale();
				m.setTipologia(rs.getString("tipo"));
				m.setSeriale(rs.getString("seriale"));
				m.setId(rs.getInt("materialeID"));
				m.setStatus(rs.getString("stato"));
				
				list.add(m);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}
	
	public ArrayList<Materiale> findAllPaginated(int offset, int size) {
		
		String query = "SELECT * FROM materiali " +
						"ORDER BY materialeID " + 
						"LIMIT ? OFFSET ?;";
		
		ArrayList<Materiale> list = new ArrayList<Materiale>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
	        ps.setInt(1, size);
	        ps.setInt(2, offset);

	        ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Materiale m = new Materiale();
				m.setTipologia(rs.getString("tipo"));
				m.setSeriale(rs.getString("seriale"));
				m.setId(rs.getInt("materialeID"));
				m.setStatus(rs.getString("stato"));
				
				list.add(m);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}
	
	public ArrayList<Materiale> findByStatoPaginated(String stato, int offset, int size) {
		
		String query = "SELECT * FROM materiali " +
				"WHERE stato = ? " +
				"ORDER BY materialeID " + 
				"LIMIT ? OFFSET ?;";
		
		ArrayList<Materiale> list = new ArrayList<Materiale>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
	        ps.setString(1, stato);
	        ps.setInt(2, size);
	        ps.setInt(3, offset);

	        ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Materiale m = new Materiale();
				m.setTipologia(rs.getString("tipo"));
				m.setSeriale(rs.getString("seriale"));
				m.setId(rs.getInt("materialeID"));
				m.setStatus(rs.getString("stato"));
				
				list.add(m);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}
	
	public long countAllMateriali() {

	    String query ="SELECT COUNT(*) AS totale FROM materiali";

	    try (Connection con = ConnectionFactory.getConnection();
	    		PreparedStatement ps = con.prepareStatement(query);
	    		ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            return rs.getLong("totale");
	        }

	        return 0;

	    } catch (SQLException e) {
	        throw new RuntimeException("Errore durante il conteggio dei materiali", e);
	    }
	}
	
	public long countByStato(String stato) {
		
	    String query ="SELECT COUNT(*) AS totale " +
	            "FROM materiali " +
	            "WHERE stato = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			
			if (rs.next()) {
				return rs.getLong("totale");
			}
			
			return 0;
			
		} catch (SQLException e) {
			throw new RuntimeException("Errore durante il conteggio dei materiali per stato", e);
		}
	}

	@Override
	public Materiale findById(int id) {
		String query = "SELECT * FROM materiali WHERE materialeID = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			Materiale m = new Materiale();
			
			while(rs.next()) {
				m.setId(rs.getInt("materialeID"));
				m.setSeriale(rs.getString("seriale"));
				m.setTipologia(rs.getString("tipo"));
				m.setStatus(rs.getString("stato"));
			}
			
			return m;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public Materiale findByType(String type) {
		String query = "SELECT * FROM materiali WHERE tipo = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, type);
			ResultSet rs = ps.executeQuery();
			
			Materiale m = new Materiale();
			
			while(rs.next()) {
				m.setId(rs.getInt("materialeID"));
				m.setSeriale(rs.getString("seriale"));
				m.setTipologia(rs.getString("tipo"));
				m.setStatus(rs.getString("stato"));
			}
			
			return m;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		
	}
	
	public Materiale findBySeriale(String seriale) {
		String query = "SELECT * FROM materiali WHERE seriale = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, seriale);
			ResultSet rs = ps.executeQuery();
			
			Materiale m = new Materiale();
			
			while(rs.next()) {
				m.setId(rs.getInt("materialeID"));
				m.setSeriale(rs.getString("seriale"));
				m.setTipologia(rs.getString("tipo"));
				m.setStatus(rs.getString("stato"));
			}
			
			return m;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		
	}

	@Override
	public boolean insert(Materiale m) {
		String query = "INSERT INTO materiali(tipo,seriale) VALUES (?, ?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, m.getTipologia());
			ps.setString(2, m.getSeriale());
			
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
		String query = "DELETE FROM materiali WHERE materialeID = ?";
		
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
	public boolean update(Materiale m) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean updateStatusByMissioneId(int missioneId, String stato) {
		String query = "UPDATE materiali m " +
	                       "JOIN materiali_missioni mtm " +
	                       "ON m.materialeID = mtm.materialeRIF " +
	                       "SET m.stato = ? " +
	                       "WHERE mtm.missioneRIF = ?";

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
