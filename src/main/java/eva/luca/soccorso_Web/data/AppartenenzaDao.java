package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Appartenenza;
import eva.luca.soccorso_Web.models.Operator;

public class AppartenenzaDao implements IDaoRead<Appartenenza>, IDaoWrite<Appartenenza>{

	@Override
	public boolean insert(Appartenenza a) {
		String query = "INSERT INTO appartenenza(operatoreRIF, squadraRIF, caposquadra) VALUES (?, ?, ?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, a.getOperatoreRif());
			ps.setInt(2, a.getSquadraRif());
			ps.setBoolean(3, a.isCaposquadra());
			
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
	
	public boolean deleteByIDCombination(int operatorId, int squadraId) {
		String query = "DELETE FROM appartenenza WHERE operatoreRIF = ? AND squadraRIF = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, operatorId);
			ps.setInt(2, squadraId);
			
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
	public boolean update(Appartenenza t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Appartenenza> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appartenenza findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<Operator> finCapoBySquadID(int squadraId) {
		String query = "SELECT o.*\n"
					+ "FROM operatori o\n"
					+ "JOIN appartenenza ap\n"
					+ "ON o.operatoreID = ap.operatoreRIF\n"
					+ "WHERE ap.squadraRIF = ? AND ap.caposquadra = TRUE";
		
		ArrayList<Operator> list = new ArrayList<Operator>();
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, squadraId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Operator op = new Operator();
				
				op.setName(rs.getString("nome"));
				op.setSurname(rs.getString("cognome"));
				op.setAge(rs.getDate("eta").toLocalDate());
				op.setEmail(rs.getString("email"));
				op.setId(rs.getInt("operatoreID"));
				
				list.add(op);
			}
			
			return list;
 		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public ArrayList<Operator> finOperatorBySquadID(int squadraId) {
		String query = "SELECT o.*\n"
					+ "FROM operatori o\n"
					+ "JOIN appartenenza ap\n"
					+ "ON o.operatoreID = ap.operatoreRIF\n"
					+ "WHERE ap.squadraRIF = ? AND ap.caposquadra = FALSE";
		
		ArrayList<Operator> list = new ArrayList<Operator>();
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, squadraId);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Operator op = new Operator();
				
				op.setName(rs.getString("nome"));
				op.setSurname(rs.getString("cognome"));
				op.setAge(rs.getDate("eta").toLocalDate());
				op.setEmail(rs.getString("email"));
				op.setId(rs.getInt("operatoreID"));
				
				list.add(op);
			}
			
			return list;
 		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public ArrayList<Operator> findOperatoriSenzaSquadra() {
		String query = "SELECT o.* " +
	                       "FROM operatori o " +
	                       "WHERE o.operatoreID NOT IN ( " +
	                       "    SELECT ap.operatoreRIF " +
	                       "    FROM appartenenza ap " +
	                       ")";
		
	    ArrayList<Operator> list = new ArrayList<Operator>();

	    try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Operator op = new Operator();

	            op.setId(rs.getInt("operatoreID"));
	            op.setName(rs.getString("nome"));
	            op.setSurname(rs.getString("cognome"));
	            op.setAge(rs.getDate("eta").toLocalDate());
	            op.setEmail(rs.getString("email"));

	            list.add(op);
	        }

	        return list;

	    } catch (SQLException e) {
	        throw new RuntimeException("JDBC error", e);
	    }
	}

}
