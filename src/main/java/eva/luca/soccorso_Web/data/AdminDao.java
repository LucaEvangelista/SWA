package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Admin;

public class AdminDao implements IDaoRead<Admin>, IDaoWrite<Admin>{

	@Override
	public boolean insert(Admin a) {
		String query = "INSERT INTO amministratori(nome, email, passkey) VALUES (?, ?, ?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
//			Connection con = ConnectionFactory.getConnection();
//			String query = "INSERT INTO amministratori(nome, email, passkey) VALUES (?, ?, ?)";
//			
//			PreparedStatement ps = con.prepareStatement(query);
			
			ps.setString(1, a.getName());
			ps.setString(2, a.getEmail());
			ps.setString(3, a.getPasskey());
			
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Admin a) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Admin> findAll() {
		
		ArrayList<Admin> list = new ArrayList<Admin>();
		
		String query = "SELECT amministratoreID, nome, email FROM amministratori";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Admin ad = new Admin();
				ad.setName(rs.getString("nome"));
				ad.setEmail(rs.getString("email"));
				ad.setId(rs.getInt("amministratoreID"));
				
				list.add(ad);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}

	@Override
	public Admin findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
