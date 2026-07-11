package eva.luca.soccorso_Web.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SecurityLayer {
    //--------- SESSION SECURITY ------------
    //questa funzione esegue una serie di controlli di sicurezza
    //sulla sessione corrente. Se la sessione non è valida, la cancella
    //e ritorna null, altrimenti la aggiorna e la restituisce
	
	public static HttpSession checkSession(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
        //per prima cosa vediamo se la sessione è attiva
		if(session == null) {
			return null;
		}
		
        //check sulla validità  della sessione
		if(session.getAttribute("userid") == null 
				|| session.getAttribute("username") == null
				|| session.getAttribute("email") == null
				|| session.getAttribute("ruolo") == null) {
			
			session.invalidate();
			return null;
		}
		
		return session;
	}
	
	public static HttpSession createSession(HttpServletRequest request, String username, int userid, String email, String ruolo){
		
		HttpSession session = request.getSession(true);
		
		session.setAttribute("userid", userid);
		session.setAttribute("username", username);
		session.setAttribute("email", email);
		session.setAttribute("ruolo", ruolo);
		
		return session;
	}
	
	public static void disposeSession(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			session.invalidate();
		}
	}
	
	public static boolean isAdmin(HttpServletRequest request) {
		
		HttpSession session = checkSession(request);
		
		if(session == null) {
			return false;
		}
		
		return "admin".equals(session.getAttribute("ruolo"));
	}
	
	public static boolean isOperator(HttpServletRequest request) {
		
		HttpSession session = checkSession(request);
		
		if(session == null) {
			return false;
		}
		
		return "operator".equals(session.getAttribute("ruolo"));
	}
}
