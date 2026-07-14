package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Operator;

public class OperatorDao implements IDaoRead<Operator>, IDaoWrite<Operator>{

	@Override
	public boolean insert(Operator o) {
		String query = "INSERT INTO operatori(nome, cognome, eta, email, passkey) VALUES (?, ?, ?, ?, ?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, o.getName());
			ps.setString(2, o.getSurname());
			ps.setDate(3, java.sql.Date.valueOf(o.getAge())); //setDate non accetta un LocalDate, ma un java.sql.Date
			ps.setString(4, o.getEmail());
			ps.setString(5, o.getPasskey());
			
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

	@Override
	public boolean update(Operator t) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean updateStatusBySquadraId(int squadraId, String stato) {
		String query = "UPDATE operatori o " +
                    "JOIN appartenenza a " +
                    "ON o.operatoreID = a.operatoreRIF " +
                    "SET o.stato = ? " +
                    "WHERE a.squadraRIF = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, stato);
			ps.setInt(2, squadraId);
			
			int affRows = ps.executeUpdate();
			
			if(affRows > 0) {
				return true;
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return false;
	}

	@Override
	public ArrayList<Operator> findAll() {
		String query = "SELECT operatoreID, nome, cognome, eta, email, stato FROM operatori";
			
		ArrayList<Operator> list = new ArrayList<Operator>();
			
			try (Connection con = ConnectionFactory.getConnection();
					PreparedStatement ps = con.prepareStatement(query)){
				
				ResultSet rs = ps.executeQuery(); //comunque un tipo di lista
				
				while(rs.next()) {
					Operator op = new Operator();
					op.setName(rs.getString("nome"));
					op.setSurname(rs.getString("cognome"));
					op.setAge(rs.getDate("eta").toLocalDate());
					op.setEmail(rs.getString("email"));
					op.setId(rs.getInt("operatoreID"));
					op.setStatus(rs.getString("stato"));
					
					list.add(op);
				}
				
			} catch (SQLException e) {
				throw new RuntimeException("JDBC error", e);
			}
			return list;
		}
	
	public ArrayList<Operator> findAllLiberi() {
		String query = "SELECT operatoreID, nome, cognome, eta, email, stato FROM operatori WHERE stato = 'libero'";
		
		ArrayList<Operator> list = new ArrayList<Operator>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ResultSet rs = ps.executeQuery(); //comunque un tipo di lista
			
			while(rs.next()) {
				Operator op = new Operator();
				op.setName(rs.getString("nome"));
				op.setSurname(rs.getString("cognome"));
				op.setAge(rs.getDate("eta").toLocalDate());
				op.setEmail(rs.getString("email"));
				op.setId(rs.getInt("operatoreID"));
				op.setStatus(rs.getString("stato"));
				
				list.add(op);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}
	
	public ArrayList<Operator> findAllPaginated(int offset, int size) {

	    String query = "SELECT operatoreID, nome, cognome, eta, email, stato " +
	            "FROM operatori " +
	            "ORDER BY operatoreID " +
	            "LIMIT ? OFFSET ?";

	    ArrayList<Operator> operatori = new ArrayList<Operator>();

	    try (Connection con = ConnectionFactory.getConnection();
	    		PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setInt(1, size);
	        ps.setInt(2, offset);

	        ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
					Operator op = new Operator();
					op.setName(rs.getString("nome"));
					op.setSurname(rs.getString("cognome"));
					op.setAge(rs.getDate("eta").toLocalDate());
					op.setEmail(rs.getString("email"));
					op.setId(rs.getInt("operatoreID"));
					op.setStatus(rs.getString("stato"));
					
					operatori.add(op);
	            }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Errore durante il caricamento paginato degli operatori", e);
	        
	    }
	    return operatori;
	}
	
	public ArrayList<Operator> findByStatoPaginated(String stato, int offset, int size) {

	    String query = "SELECT operatoreID, nome, cognome, eta, email, stato " +
	            "FROM operatori " +
	            "WHERE stato = ? " +
	            "ORDER BY operatoreID " +
	            "LIMIT ? OFFSET ?";

	    ArrayList<Operator> operatori = new ArrayList<Operator>();

	    try (Connection con = ConnectionFactory.getConnection();
	    		PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, stato);
	        ps.setInt(2, size);
	        ps.setInt(3, offset);

	        ResultSet rs = ps.executeQuery();

	            while (rs.next()) {
					Operator op = new Operator();
					op.setName(rs.getString("nome"));
					op.setSurname(rs.getString("cognome"));
					op.setAge(rs.getDate("eta").toLocalDate());
					op.setEmail(rs.getString("email"));
					op.setId(rs.getInt("operatoreID"));
					op.setStatus(rs.getString("stato"));
					
					operatori.add(op);
	            }

	    } catch (SQLException e) {
	        throw new RuntimeException("Errore durante il caricamento degli operatori filtrati", e);
	    }
	    return operatori;
	}
	
	public long countAllOperatori() {

	    String query ="SELECT COUNT(*) AS totale FROM operatori";

	    try (Connection con = ConnectionFactory.getConnection();
	    		PreparedStatement ps = con.prepareStatement(query);
	    		ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            return rs.getLong("totale");
	        }

	        return 0;

	    } catch (SQLException e) {
	        throw new RuntimeException("Errore durante il conteggio degli operatori", e);
	    }
	}
	
	public long countByStato(String stato) {

	    String query ="SELECT COUNT(*) AS totale " +
	            "FROM operatori " +
	            "WHERE stato = ?";

	    try (Connection con = ConnectionFactory.getConnection();
	    		PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, stato);

	        ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                return rs.getLong("totale");
	            }

	            return 0;

	    } catch (SQLException e) {
	        throw new RuntimeException("Errore durante il conteggio degli operatori per stato", e);
	    }
	}

	@Override
	public Operator findById(int id) {
		String query = "SELECT operatoreID, nome, cognome, eta, email, stato FROM operatori WHERE operatoreID = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				Operator op = new Operator();
				
				op.setName(rs.getString("nome"));
				op.setSurname(rs.getString("cognome"));
				op.setAge(rs.getDate("eta").toLocalDate());
				op.setEmail(rs.getString("email"));
				op.setId(rs.getInt("operatoreID"));
				op.setStatus(rs.getString("stato"));
				
				
				return op;		
			}
			
			return null;
			
			} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	
	public Operator findByMail(String mail) {
		String query = "SELECT operatoreID, nome, cognome, eta, email, stato FROM operatori WHERE email = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, mail);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				Operator op = new Operator();
				
				op.setName(rs.getString("nome"));
				op.setSurname(rs.getString("cognome"));
				op.setAge(rs.getDate("eta").toLocalDate());
				op.setEmail(rs.getString("email"));
				op.setId(rs.getInt("operatoreID"));
				op.setStatus(rs.getString("stato"));
				
				
				return op;		
			}
			
			return null;
			
			} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}

}
