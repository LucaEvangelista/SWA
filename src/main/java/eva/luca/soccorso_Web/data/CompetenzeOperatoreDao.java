package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Competenza;
import eva.luca.soccorso_Web.models.CompetenzeOperatore;

public class CompetenzeOperatoreDao implements IDaoRead<CompetenzeOperatore>, IDaoWrite<CompetenzeOperatore> {

	@Override
	public boolean insert(CompetenzeOperatore co) {
		String query = "INSERT INTO operatori_abilita(operatoreRIF, abilitaRIF) VALUES (?, ?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, co.getOperatoreRif());
			ps.setInt(2, co.getAbilitaRif());
			
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
	
	public boolean deleteByIDCombination(int operatoreId, int abilitaId) {
		String query = "DELETE FROM operatori_abilita WHERE operatoreRIF = ? AND abilitaRIF = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, operatoreId);
			ps.setInt(2, abilitaId);
			
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
	public boolean update(CompetenzeOperatore t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<CompetenzeOperatore> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompetenzeOperatore findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<Competenza> findByOperatorID(int operatorRif){
		String query = "SELECT a.*\n"
					+ "FROM abilita a\n"
					+ "JOIN operatori_abilita oa\n"
					+ "ON a.abilitaID = oa.abilitaRIF\n"
					+ "WHERE oa.operatoreRIF = ?";
		
		ArrayList<Competenza> list = new ArrayList<Competenza>();
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, operatorRif);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Competenza c = new Competenza();
				
				c.setId(rs.getInt("abilitaID"));
				c.setTipologia(rs.getString("tipo"));
				
				list.add(c);
				
			}
			
			return list;
			
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}

}
