package eva.luca.soccorso_Web.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.ws.rs.core.UriInfo;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import eva.luca.soccorso_Web.data.TokenBlackListDao;
import eva.luca.soccorso_Web.models.TokenData;



public class JWTHelpers {
	
	 private static JWTHelpers instance = null;
	    private SecretKey jwtKey = null;
	    private final TokenBlackListDao serviceT;

	    private JWTHelpers() {
	    	serviceT = new TokenBlackListDao();
	        KeyGenerator keyGenerator;
	        try {
	            keyGenerator = KeyGenerator.getInstance("HmacSha256");
	            jwtKey = keyGenerator.generateKey();
	        } catch (NoSuchAlgorithmException ex) {
	            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
	        }
	    }

	    public SecretKey getJwtKey() {
	        return jwtKey;
	    }

	    public TokenData validateToken(String token) {

	        Jws<Claims> jwsc = Jwts.parser()
	                .verifyWith(getJwtKey())
	                .build()
	                .parseSignedClaims(token);
	        
	        if (serviceT.isBlackListed(token)) {
				return null;
			}

	        Claims claims = jwsc.getPayload();

	        String email = claims.getSubject();
	        String ruolo = claims.get("ruolo", String.class);

	        if (email == null || email.isBlank()
	                || ruolo == null || ruolo.isBlank()) {
	            return null;
	        }

	        return new TokenData(email, ruolo);
	    }

	    public String issueToken(UriInfo context, String email, String ruolo) {
	        String token = Jwts.builder()
	                .subject(email)
	                .claim("ruolo", ruolo)
	                .issuer(context.getAbsolutePath().toString())
	                .issuedAt(new Date())
	                .expiration(Date.from(LocalDateTime.now().plusMinutes(15L).atZone(ZoneId.systemDefault()).toInstant()))
	                .signWith(getJwtKey())
	                .compact();
	        return token;
	    }

	    public boolean revokeToken(String token) {
	    	
	    	//controllo esistenza token
	    	if (token == null || token.isBlank()) {
	            return false;
	        }

	        try {
	            //verifica del token
	            Jws<Claims> jwsc = Jwts.parser()
	                    .verifyWith(getJwtKey())
	                    .build()
	                    .parseSignedClaims(token);

	            Claims claims = jwsc.getPayload();

	            Date expiration = claims.getExpiration();

	            if (expiration == null) {
	                return false;
	            }

	            //se già scaduto non viene inserito direttamente
	            if (!expiration.after(new Date())) {
	                return false;
	            }
	            
	            return serviceT.insert(token, expiration.toInstant());

	        } catch (JwtException | IllegalArgumentException e) {

	            //Token malformato, scaduto o con firma errata
	            return false;

	        } catch (RuntimeException e) {
	        	return false;
	        }
	    }

	    public static JWTHelpers getInstance() {
	        if (instance == null) {
	            instance = new JWTHelpers();
	        }
	        return instance;
	    }
}
