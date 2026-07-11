package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Missione;

public class MissioneDao implements IDaoRead<Missione>, IDaoWrite<Missione>{

	@Override
	public boolean insert(Missione m) {
		
		String query = "INSERT INTO missioni(obiettivo, posizione, richiestaRIF, squadraRif) VALUES (?, ?, ?, ?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, m.getObiettivo());
			ps.setString(2, m.getPosizione());
			ps.setInt(3, m.getRichiestaRif());
			ps.setInt(4, m.getSquadraRif());
			
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
		
		String query = "DELETE FROM missioni WHERE missioneID = ?";
		
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
	public boolean update(Missione m) {
		//TO DO
		return false;
	}
	
	public boolean updateToTerminata(int missioneId) {
		
		String query = "UPDATE missioni\n"
					+ "SET fase = 'terminata'\n"
					+ "WHERE missioneID = ?;";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, missioneId);
			
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
	public ArrayList<Missione> findAll() {
		ArrayList<Missione> list = new ArrayList<Missione>();
		
		String query = "SELECT * FROM missioni";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Missione m = new Missione();
				m.setObiettivo(rs.getString("obiettivo"));
				m.setPosizione(rs.getString("posizione"));
				m.setRichiestaRif(rs.getInt("richiestaRIF"));
				m.setSquadraRif(rs.getInt("squadraRIF"));
				m.setMissioneId(rs.getInt("missioneID"));
				m.setStatus(rs.getString("fase"));
				
				list.add(m);
			}
			return list;
			
 		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public ArrayList<Missione> findAttive() {
		
		String query = "SELECT * FROM missioni WHERE fase = 'attiva'";
		
		ArrayList<Missione> list = new ArrayList<Missione>();
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Missione m = new Missione();
				m.setObiettivo(rs.getString("obiettivo"));
				m.setPosizione(rs.getString("posizione"));
				m.setRichiestaRif(rs.getInt("richiestaRIF"));
				m.setSquadraRif(rs.getInt("squadraRIF"));
				m.setMissioneId(rs.getInt("missioneID"));
				m.setStatus(rs.getString("fase"));
				
				list.add(m);
			}
			return list;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}

	@Override
	public Missione findById(int id) {
		
		String query = "SELECT * FROM missioni WHERE missioneID = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			
			
			if(rs.next()) {
				Missione m = new Missione();
				
				m.setObiettivo(rs.getString("obiettivo"));
				m.setPosizione(rs.getString("posizione"));
				m.setRichiestaRif(rs.getInt("richiestaRIF"));
				m.setSquadraRif(rs.getInt("squadraRIF"));
				m.setMissioneId(rs.getInt("missioneID"));
				m.setStatus(rs.getString("fase"));
				
				return m;
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public Missione findByRequestID(int richiestaID) {
		
		String query = "SELECT * FROM missioni WHERE richiestaRIF = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, richiestaID);
			ResultSet rs = ps.executeQuery();
			
            if (rs.next()) {
                Missione m = new Missione();

                m.setObiettivo(rs.getString("obiettivo"));
                m.setPosizione(rs.getString("posizione"));
                m.setRichiestaRif(rs.getInt("richiestaRIF"));
                m.setSquadraRif(rs.getInt("squadraRIF"));
                m.setMissioneId(rs.getInt("missioneID"));
                m.setStatus(rs.getString("fase"));

                return m;
            }

            return null;
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		
	}
	
	public int insertAndReturnID(Missione miss) {
		
		String query = "INSERT INTO missioni(obiettivo, posizione, richiestaRIF, squadraRif) VALUES (?, ?, ?, ?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
			
			ps.setString(1, miss.getObiettivo());
			ps.setString(2, miss.getPosizione());
			ps.setInt(3, miss.getRichiestaRif());
			ps.setInt(4, miss.getSquadraRif());
			
			int affRows = ps.executeUpdate();
			
			if(affRows == 0) {
				return -1;
			}
			
			ResultSet generatedKey = ps.getGeneratedKeys();
			
	        if (generatedKey.next()) {
	            return generatedKey.getInt(1);
	        }

	        return -1;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public Missione findByOperatoreId(int operatoreId) {
		
		String query = "SELECT m.* " +
                    "FROM missioni m " +
                    "JOIN appartenenza a " +
                    "ON m.squadraRIF = a.squadraRIF " +
                    "WHERE a.operatoreRIF = ? " +
                    "AND m.fase = 'attiva'";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, operatoreId);
			
			ResultSet rs = ps.executeQuery();
			
			Missione m = null;
			
			if(rs.next()) {
				m = new Missione();
				
				m.setObiettivo(rs.getString("obiettivo"));
				m.setPosizione(rs.getString("posizione"));
				m.setRichiestaRif(rs.getInt("richiestaRIF"));
				m.setSquadraRif(rs.getInt("squadraRIF"));
				m.setMissioneId(rs.getInt("missioneID"));
				m.setStatus(rs.getString("fase"));
			}
			
			return m;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public Missione findByMaterialeId(int materialeId) {
		
		String query =  "SELECT m.* " +
					"FROM missioni m " +
					"JOIN materiali_missioni ma " +
					"ON m.missioneID = ma.missioneRIF " +
					"WHERE ma.materialeRIF = ? " +
					"AND m.fase = 'attiva'";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, materialeId);
			
			ResultSet rs = ps.executeQuery();
			
			Missione m = null;
			
			if(rs.next()) {
				m = new Missione();
				
				m.setObiettivo(rs.getString("obiettivo"));
				m.setPosizione(rs.getString("posizione"));
				m.setRichiestaRif(rs.getInt("richiestaRIF"));
				m.setSquadraRif(rs.getInt("squadraRIF"));
				m.setMissioneId(rs.getInt("missioneID"));
				m.setStatus(rs.getString("fase"));
			}
			
			return m;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public Missione findByMezzoId(int mezzoId) {
		
		String query = "SELECT m.* " +
					"FROM missioni m " +
					"JOIN mezzi_missioni mz " +
					"ON m.missioneID = mz.missioneRIF " +
					"WHERE mz.mezzoRIF = ? " +
					"AND m.fase = 'attiva'";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, mezzoId);
			
			ResultSet rs = ps.executeQuery();
			
			Missione m = null;
			
			if(rs.next()) {
				m = new Missione();
				
				m.setObiettivo(rs.getString("obiettivo"));
				m.setPosizione(rs.getString("posizione"));
				m.setRichiestaRif(rs.getInt("richiestaRIF"));
				m.setSquadraRif(rs.getInt("squadraRIF"));
				m.setMissioneId(rs.getInt("missioneID"));
				m.setStatus(rs.getString("fase"));
			}
			
			return m;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public ArrayList<Missione> storicoByOperatoreId(int operatoreId) {
		
		String query = "SELECT m.* " +
					"FROM missioni m " +
					"JOIN appartenenza a " +
					"ON m.squadraRIF = a.squadraRIF " +
					"WHERE a.operatoreRIF = ? ";
		
		ArrayList<Missione> list = new ArrayList<Missione>();
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, operatoreId);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Missione m = new Missione();
				
				m.setObiettivo(rs.getString("obiettivo"));
				m.setPosizione(rs.getString("posizione"));
				m.setRichiestaRif(rs.getInt("richiestaRIF"));
				m.setSquadraRif(rs.getInt("squadraRIF"));
				m.setMissioneId(rs.getInt("missioneID"));
				m.setStatus(rs.getString("fase"));
				
				list.add(m);
			}
			
			return list;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public ArrayList<Missione> storicoByMaterialeId(int materialeId) {
		String query = "SELECT m.* " +
					"FROM missioni m " +
					"JOIN materiali_missioni ma " +
					"ON m.missioneID = ma.missioneRIF " +
					"WHERE ma.materialeRIF = ? ";
		
		ArrayList<Missione> list = new ArrayList<Missione>();
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, materialeId);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Missione m = new Missione();
				
				m.setObiettivo(rs.getString("obiettivo"));
				m.setPosizione(rs.getString("posizione"));
				m.setRichiestaRif(rs.getInt("richiestaRIF"));
				m.setSquadraRif(rs.getInt("squadraRIF"));
				m.setMissioneId(rs.getInt("missioneID"));
				m.setStatus(rs.getString("fase"));
				
				list.add(m);
			}
			
			return list;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public ArrayList<Missione> storicoByMezzoId(int mezzoId) {
		String query = "SELECT m.* " +
					"FROM missioni m " +
					"JOIN mezzi_missioni mz " +
					"ON m.missioneID = mz.missioneRIF " +
					"WHERE mz.mezzoRIF = ? ";
		
		ArrayList<Missione> list = new ArrayList<Missione>();
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, mezzoId);
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Missione m = new Missione();
				
				m.setObiettivo(rs.getString("obiettivo"));
				m.setPosizione(rs.getString("posizione"));
				m.setRichiestaRif(rs.getInt("richiestaRIF"));
				m.setSquadraRif(rs.getInt("squadraRIF"));
				m.setMissioneId(rs.getInt("missioneID"));
				m.setStatus(rs.getString("fase"));
				
				list.add(m);
			}
			
			return list;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	
	

}
