package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Patente;
import eva.luca.soccorso_Web.models.PatenteOperatore;

public class PatenteOperatoreDao implements IDaoRead<PatenteOperatore>, IDaoWrite<PatenteOperatore> {

	@Override
	public boolean insert(PatenteOperatore po) {
		String query = "INSERT INTO operatori_patenti(patenteRIF, operatoreRIF) VALUES (?, ?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, po.getPatenteRif());
			ps.setInt(2, po.getOperatoreRif());
			
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
	
	public boolean deleteByIDCombination(int operatorId, int patenteId) {
		String query = "DELETE FROM operatori_patenti WHERE operatoreRIF = ? AND patenteRIF = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, operatorId);
			ps.setInt(2, patenteId);
			
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
	public boolean update(PatenteOperatore t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<PatenteOperatore> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PatenteOperatore findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<Patente>  findByOperatorID(int operatorRif) {
		String query = "SELECT p.*\n"
					+ "FROM patenti p\n"
					+ "JOIN operatori_patenti op\n"
					+ "ON p.patenteID = op.patenteRIF\n"
					+ "WHERE op.operatoreRIF = ?";
		
		ArrayList<Patente> list = new ArrayList<Patente>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, operatorRif);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Patente p = new Patente();
				
				p.setId(rs.getInt("patenteID"));
				p.setTipologia(rs.getString("tipo"));
				
				list.add(p);
			}
			
			return list;
 		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}

}
