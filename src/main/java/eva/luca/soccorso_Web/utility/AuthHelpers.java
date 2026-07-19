package eva.luca.soccorso_Web.utility;

import eva.luca.soccorso_Web.data.AuthDao;
import eva.luca.soccorso_Web.models.LoggedUser;
import eva.luca.soccorso_Web.models.TokenData;
import jakarta.ws.rs.core.UriInfo;

public class AuthHelpers {
	
	/* è una classe intermedia che collega:
		la risorsa AuthRes;
		il DAO dell'autenticazione;
		la classe che gestisce i JWT.*/
	
    private static AuthHelpers instance = null;
    private final JWTHelpers jwt;
    private final AuthDao serviceA = new AuthDao();
    
    private AuthHelpers() {
        jwt = JWTHelpers.getInstance();
    }
    
    public LoggedUser authenticateUser(String email, String password) {
        return serviceA.login(email, password);
    }
    
    public String issueToken(UriInfo context, String email, String ruolo) {
        return jwt.issueToken(context, email, ruolo);
    }
    
    public void revokeToken(String token) {
        jwt.revokeToken(token);
    }
    
    public TokenData validateToken(String token) {
        return jwt.validateToken(token);
    }
    
    public static AuthHelpers getInstance() {
        if (instance == null) {
            instance = new AuthHelpers();
        }
        return instance;
    }

}
