package eva.luca.soccorso_Web.resources;

import eva.luca.soccorso_Web.models.ErrorResponse;
import eva.luca.soccorso_Web.models.LoggedUser;
import eva.luca.soccorso_Web.utility.AuthHelpers;
import eva.luca.soccorso_Web.utility.Logged;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

@Path("auth")
public class AuthRes {
	
	@POST
	@Path("login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response login(@Context UriInfo uriinfo, 
			@FormParam("email") String email,
            @FormParam("password") String password) {
		
		try {
			LoggedUser user = AuthHelpers.getInstance().authenticateUser(email, password);
			if(user != null) {
                String authToken = AuthHelpers.getInstance().issueToken(uriinfo, user.getEmail(), user.getRuolo());
                return Response.ok(authToken)
                        .cookie(new NewCookie.Builder("token").value(authToken).path("/").build())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken).build();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Status.UNAUTHORIZED)
				.entity(new ErrorResponse("Non sei autorizzato"))
				.build();
	}
	
	@DELETE
	@Path("logout")
	@Logged
	public Response logout(@Context ContainerRequestContext req) {
        //proprietà estratta dall'authorization header 
        //e iniettata nella request dal filtro di autenticazione
        String token = (String) req.getProperty("token");
        AuthHelpers.getInstance().revokeToken(token);
        return Response.noContent()
                //eliminaimo anche il cookie con il token
                .cookie(new NewCookie.Builder("token").value("").path("/").maxAge(0).build())
                .header(HttpHeaders.AUTHORIZATION, "").build();
	}
	
	@GET
	@Path("refresh")
	@Logged
	public Response refresh(@Context ContainerRequestContext req, @Context UriInfo uriinfo) {
        //proprietà iniettata nella request dal filtro di autenticazione
        String mail = (String) req.getProperty("user");
        String ruolo = (String) req.getProperty("ruolo");
        String newtoken = AuthHelpers.getInstance().issueToken(uriinfo, mail, ruolo);
        return Response.ok(newtoken)
                .cookie(new NewCookie.Builder("token").value(newtoken).path("/").build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + newtoken).build();
	}
}
