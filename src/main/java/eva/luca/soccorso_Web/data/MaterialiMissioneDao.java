package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Materiale;
import eva.luca.soccorso_Web.models.MaterialiMissione;

public class MaterialiMissioneDao implements IDaoRead<MaterialiMissione>, IDaoWrite<MaterialiMissione>{

	@Override
	public boolean insert(MaterialiMissione mtM) {
		String query = "INSERT INTO materiali_missioni(materialeRIF, missioneRIF) VALUES (?, ?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, mtM.getMaterialeRif());
			ps.setInt(2, mtM.getMissioneRif());
			
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
	
	public boolean deleteByIDCombination(int materialeId, int missioneId) {
		String query = "DELETE FROM materiali_missioni WHERE materialeRIF = ? AND missioneRIF = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, materialeId);
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

	@Override
	public boolean update(MaterialiMissione t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<MaterialiMissione> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MaterialiMissione findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<Materiale> materialiOfTheMission(int missioneRif){
		String query ="SELECT m.*\n"
					+ "FROM materiali m\n"
					+ "JOIN materiali_missioni mtm\n"
					+ "ON m.materialeID = mtm.materialeRIF\n"
					+ "WHERE mtm.missioneRIF = ?";
		
		ArrayList<Materiale> list = new ArrayList<Materiale>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, missioneRif);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Materiale m = new Materiale();
				
				m.setId(rs.getInt("materialeID"));
				m.setTipologia(rs.getString("tipo"));
				
				list.add(m);
			}
			
			return list;
			
 		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}

}
