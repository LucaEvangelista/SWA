package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.MezziMissione;
import eva.luca.soccorso_Web.models.Mezzo;

public class MezziMissioneDao implements IDaoRead<MezziMissione>, IDaoWrite<MezziMissione>{

	@Override
	public boolean insert(MezziMissione mzM) {
		String query = "INSERT INTO mezzi_missioni(mezzoRIF, missioneRIF) VALUES (?, ?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, mzM.getMezzoRif());
			ps.setInt(2, mzM.getMissioneRif());
			
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
	
	public boolean deleteByIDCombination(int mezzoId, int missioneId) {
		String query = "DELETE FROM mezzi_missioni WHERE mezzoRIF = ? AND missioneRIF = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, mezzoId);
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
	public boolean update(MezziMissione t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<MezziMissione> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MezziMissione findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ArrayList<Mezzo> mezziOfTheMission(int missioneRif){
		String query ="SELECT mz.*\n"
					+ "FROM mezzi mz\n"
					+ "JOIN mezzi_missioni mzm\n"
					+ "ON mz.mezzoID = mzm.mezzoRIF\n"
					+ "WHERE mzm.missioneRIF = ?";
		
		ArrayList<Mezzo> list = new ArrayList<Mezzo>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, missioneRif);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Mezzo mz = new Mezzo();
				
				mz.setId(rs.getInt("mezzoID"));
				mz.setTipologia(rs.getString("tipo"));
				
				list.add(mz);
			}
			
			return list;
			
 		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}

}
