package eva.luca.soccorso_Web.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import eva.luca.soccorso_Web.data.db.ConnectionFactory;
import eva.luca.soccorso_Web.models.Request;

public class RequestDao implements IDaoRead<Request>, IDaoWrite<Request>{

	@Override
	public boolean insert(Request t) {
		String query = "INSERT INTO richieste(nomePERS, mailPERS, descrizione, indirizzo, codice_u) VALUES (?, ?, ?, ?, ?)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, t.getNomePersona());
			ps.setString(2, t.getMailPersona());
			ps.setString(3, t.getDescrizione());
			ps.setString(4, t.getIndirizzo());
			ps.setString(5, t.getUuid());
			
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
	public boolean update(Request r) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean updateToterminata(int richiestaId) {
		String query = "UPDATE richieste\n"
					+ "SET fase = 'terminata', closed_at = CURRENT_TIMESTAMP\n"
					+ "WHERE richiestaID = ?;";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, richiestaId);
			
			int affRows = ps.executeUpdate();
			
			if(affRows > 0) {
				return true;
			}
			
		} catch (Exception e) {
			throw new RuntimeException("JDBC error", e);
		}
		return false;
	}
	
	public boolean updateToEsecuzione(int richiestaId) {
		String query = "UPDATE richieste\n"
					+ "SET fase = 'in esecuzione', working_at = CURRENT_TIMESTAMP\n"
					+ "WHERE richiestaID = ?;";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, richiestaId);
			
			int affRows = ps.executeUpdate();
			
			if(affRows > 0) {
				return true;
			}
			
		} catch (Exception e) {
			throw new RuntimeException("JDBC error", e);
		}
		return false;
	}
	
	public boolean updateToRifiutata(int richiestaId) {
		String query = "UPDATE richieste\n"
					+ "SET fase = 'rifiutata', closed_at = CURRENT_TIMESTAMP\n"
					+ "WHERE richiestaID = ?  AND fase = ?;";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, richiestaId);
			ps.setString(2, "attiva");
			
			int affRows = ps.executeUpdate();
			
			if(affRows > 0) {
				return true;
			}
			
		} catch (Exception e) {
			throw new RuntimeException("JDBC error", e);
		}
		return false;
	}
	
	public boolean updateToAttiva(String uuid) {
		String query = "UPDATE richieste\n"
					+ "SET fase = 'attiva' WHERE codice_u = ? AND fase = 'pendente';";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, uuid);
			
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
	public ArrayList<Request> findAll() {
		String query = "SELECT * FROM richieste";
		
		ArrayList<Request> list = new ArrayList<Request>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Request rq = new Request();
				rq.setNomePersona(rs.getString("nomePERS"));
				rq.setMailPersona(rs.getString("mailPERS"));
				rq.setDescrizione(rs.getString("descrizione"));
				rq.setIndirizzo(rs.getString("indirizzo"));
				rq.setId(rs.getInt("richiestaID"));
				
				rq.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
				rq.setWorkingAt(getLocalDateTime(rs, "working_at"));
				rq.setClosedAt(getLocalDateTime(rs, "closed_at"));
				
				rq.setFase(rs.getString("fase"));
				rq.setUuid(rs.getString("codice_u"));
				
				list.add(rq);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}
	
	public ArrayList<Request> findAllInExecution() {
		String query = "SELECT * FROM richieste WHERE fase = ?";
		
		ArrayList<Request> list = new ArrayList<Request>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, "in esecuzione");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Request rq = new Request();
				rq.setNomePersona(rs.getString("nomePERS"));
				rq.setMailPersona(rs.getString("mailPERS"));
				rq.setDescrizione(rs.getString("descrizione"));
				rq.setIndirizzo(rs.getString("indirizzo"));
				rq.setId(rs.getInt("richiestaID"));
				
				rq.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
				rq.setWorkingAt(getLocalDateTime(rs, "working_at"));
				rq.setClosedAt(getLocalDateTime(rs, "closed_at"));
				
				rq.setFase(rs.getString("fase"));
				rq.setUuid(rs.getString("codice_u"));
				
				list.add(rq);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}
	
	public ArrayList<Request> findAllNotPending() {
		String query = "SELECT * FROM richieste WHERE fase <>?";
		
		ArrayList<Request> list = new ArrayList<Request>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, "pendente");
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Request rq = new Request();
				rq.setNomePersona(rs.getString("nomePERS"));
				rq.setMailPersona(rs.getString("mailPERS"));
				rq.setDescrizione(rs.getString("descrizione"));
				rq.setIndirizzo(rs.getString("indirizzo"));
				rq.setId(rs.getInt("richiestaID"));
				
				rq.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
				rq.setWorkingAt(getLocalDateTime(rs, "working_at"));
				rq.setClosedAt(getLocalDateTime(rs, "closed_at"));
				
				rq.setFase(rs.getString("fase"));
				rq.setUuid(rs.getString("codice_u"));
				
				list.add(rq);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}
	
	public ArrayList<Request> findAllPending() {
		String query = "SELECT * FROM richieste WHERE fase = 'pendente'";
		
		ArrayList<Request> list = new ArrayList<Request>();
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Request rq = new Request();
				rq.setNomePersona(rs.getString("nomePERS"));
				rq.setMailPersona(rs.getString("mailPERS"));
				rq.setDescrizione(rs.getString("descrizione"));
				rq.setIndirizzo(rs.getString("indirizzo"));
				rq.setId(rs.getInt("richiestaID"));
				
				rq.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
				rq.setWorkingAt(getLocalDateTime(rs, "working_at"));
				rq.setClosedAt(getLocalDateTime(rs, "closed_at"));
				
				rq.setFase(rs.getString("fase"));
				rq.setUuid(rs.getString("codice_u"));
				
				list.add(rq);
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		return list;
	}
	
	//query per la paginazione di tutte le richieste non pendenti
	public ArrayList<Request> findAllNotPendingPaginated(int offset, int limit) {

	    String query = "SELECT * FROM richieste " +
	            "WHERE fase <> ? " +
	            "ORDER BY created_at DESC " +
	            "LIMIT ? OFFSET ?";

	    ArrayList<Request> list = new ArrayList<Request>();

	    try (Connection con = ConnectionFactory.getConnection();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, "pendente");
	        ps.setInt(2, limit);
	        ps.setInt(3, offset);

	        try (ResultSet rs = ps.executeQuery()) {

	            while (rs.next()) {

	                Request rq = new Request();

	                rq.setNomePersona(rs.getString("nomePERS"));
	                rq.setMailPersona(rs.getString("mailPERS"));
	                rq.setDescrizione(rs.getString("descrizione"));
	                rq.setIndirizzo(rs.getString("indirizzo"));
	                rq.setId(rs.getInt("richiestaID"));

	                rq.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
	                rq.setWorkingAt(getLocalDateTime(rs, "working_at"));
	                rq.setClosedAt(getLocalDateTime(rs, "closed_at"));

	                rq.setFase(rs.getString("fase"));
	                rq.setUuid(rs.getString("codice_u"));

	                list.add(rq);
	            }
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException(
	                "Errore JDBC durante la paginazione delle richieste", e);
	    }

	    return list;
	}
	
	public long countAllNotPending() {

	    String query = "SELECT COUNT(*) AS totale " +
	            "FROM richieste " +
	            "WHERE fase <> ?";

	    try (Connection con = ConnectionFactory.getConnection();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, "pendente");

	        try (ResultSet rs = ps.executeQuery()) {

	            if (rs.next()) {
	                return rs.getLong("totale");
	            }
	        }

	    } catch (SQLException e) {
	        throw new RuntimeException("Errore JDBC durante il conteggio delle richieste", e);
	    }

	    return 0;
	}
	
	public List<Request> findByStatoPaginated(String stato, int offset, int size) {

	    List<Request> richieste = new ArrayList<>();

	    String query = """
	        SELECT *
	        FROM richieste
	        WHERE fase = ?
	        ORDER BY created_at DESC
	        LIMIT ? OFFSET ?
	        """;

	    try (Connection con = ConnectionFactory.getConnection();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, stato);
	        ps.setInt(2, size);
	        ps.setInt(3, offset);

	        try (ResultSet rs = ps.executeQuery()) {

	            while (rs.next()) {

	                Request rq = new Request();

	                rq.setNomePersona(rs.getString("nomePERS"));
	                rq.setMailPersona(rs.getString("mailPERS"));
	                rq.setDescrizione(rs.getString("descrizione"));
	                rq.setIndirizzo(rs.getString("indirizzo"));
	                rq.setId(rs.getInt("richiestaID"));

	                rq.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
	                rq.setWorkingAt(getLocalDateTime(rs, "working_at"));
	                rq.setClosedAt(getLocalDateTime(rs, "closed_at"));

	                rq.setFase(rs.getString("fase"));
	                rq.setUuid(rs.getString("codice_u"));

	                richieste.add(rq);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Errore durante il caricamento delle richieste filtrate", e);
	    }

	    return richieste;
	}
	
	public long countByStato(String stato) {

	    String query = """
	        SELECT COUNT(*)
	        FROM richieste
	        WHERE fase = ?
	        """;

	    try (Connection con = ConnectionFactory.getConnection();
	         PreparedStatement ps = con.prepareStatement(query)) {

	        ps.setString(1, stato);

	        try (ResultSet rs = ps.executeQuery()) {

	            if (rs.next()) {
	                return rs.getLong(1);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException(
	                "Errore durante il conteggio delle richieste filtrate", e);
	    }

	    return 0;
	}
	
	public boolean findExistRecentRequestByEmail(String requestEmail) {
		String query = "SELECT COUNT(*) AS totale " +
			        "FROM richieste " +
			        "WHERE mailPERS = ? " +
			        "AND created_at >= DATE_SUB(NOW(), INTERVAL 2 MINUTE)";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, requestEmail);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("totale") > 0;
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
		
		return false;
	}

	@Override
	public Request findById(int id) {
		String query = "SELECT * FROM richieste WHERE richiestaID = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				Request rq = new Request();
				
				rq.setNomePersona(rs.getString("nomePERS"));
				rq.setMailPersona(rs.getString("mailPERS"));
				rq.setDescrizione(rs.getString("descrizione"));
				rq.setIndirizzo(rs.getString("indirizzo"));
				rq.setId(rs.getInt("richiestaID"));
				
				rq.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
				rq.setWorkingAt(getLocalDateTime(rs, "working_at"));
				rq.setClosedAt(getLocalDateTime(rs, "closed_at"));
				
				rq.setFase(rs.getString("fase"));
				rq.setUuid(rs.getString("codice_u"));
				
				return rq;
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	public Request findByUuid(String uuid) {
		String query = "SELECT * FROM richieste WHERE codice_u = ?";
		
		try (Connection con = ConnectionFactory.getConnection();
				PreparedStatement ps = con.prepareStatement(query)){
			
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			
			
			if(rs.next()) {
				Request rq = new Request();
				rq.setNomePersona(rs.getString("nomePERS"));
				rq.setMailPersona(rs.getString("mailPERS"));
				rq.setDescrizione(rs.getString("descrizione"));
				rq.setIndirizzo(rs.getString("indirizzo"));
				rq.setId(rs.getInt("richiestaID"));
				
				rq.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
				rq.setWorkingAt(getLocalDateTime(rs, "working_at"));
				rq.setClosedAt(getLocalDateTime(rs, "closed_at"));
				
				rq.setFase(rs.getString("fase"));
				rq.setUuid(rs.getString("codice_u"));
				
				return rq;
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new RuntimeException("JDBC error", e);
		}
	}
	
	private LocalDateTime getLocalDateTime(ResultSet rs, String columnName) throws SQLException {
	    Timestamp timestamp = rs.getTimestamp(columnName);

	    if (timestamp != null) {
	        return timestamp.toLocalDateTime();
	    }

	    return null;
	}


}
