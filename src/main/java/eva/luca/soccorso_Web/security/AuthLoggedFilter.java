package eva.luca.soccorso_Web.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

import eva.luca.soccorso_Web.models.ErrorResponse;
import eva.luca.soccorso_Web.models.TokenData;
import eva.luca.soccorso_Web.utility.AuthHelpers;
import eva.luca.soccorso_Web.utility.Logged;


@Provider
@Logged
@Priority(Priorities.AUTHENTICATION)
public class AuthLoggedFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String token = null;
        final String path = requestContext.getUriInfo().getAbsolutePath().toString();

        //come esempio, proviamo a cercare il token in vari punti, in ordine di priorità
        //in un'applicazione reale, potremmo scegliere una sola modalità
        //ricerca del token nell'header
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring("Bearer".length()).trim();
            //ricerca del cookie token
        } else if (requestContext.getCookies().containsKey("token")) {
            token = requestContext.getCookies().get("token").getValue();
            //ricerca del token come parametro della query string
        } else if (requestContext.getUriInfo().getQueryParameters().containsKey("token")) {
            token = requestContext.getUriInfo().getQueryParameters().getFirst("token");
        }
        if (token != null && !token.isEmpty()) {
            try {
                //validiamo il token
                final TokenData tokenData = AuthHelpers.getInstance().validateToken(token);
                if (tokenData!= null) {
                    String email = tokenData.getEmail();
                    String ruolo = tokenData.getRuolo();
                    //inseriamo nel contesto i risultati dell'autenticazione
                    //per farli usare dai nostri metodi restful
                    //iniettando @Context ContainerRequestContext
                    requestContext.setProperty("token", token);
                    requestContext.setProperty("user", email);
                    requestContext.setProperty("ruolo", ruolo);
                    //OPPURE
                    // https://dzone.com/articles/custom-security-context-injax-rs
                    //mettiamo i dati anche nel securitycontext standard di JAXRS...
                    //che può essere iniettato con @Context SecurityContext nei metodi
                    //final SecurityContext originalSecurityContext = requestContext.getSecurityContext();
                    requestContext.setSecurityContext(new SecurityContext() {
                        @Override
                        public Principal getUserPrincipal() {
                            return new Principal() {
                                @Override
                                public String getName() {
                                    return email;
                                }
                            };
                        }

                        @Override
                        public boolean isUserInRole(String role) {
                            return ruolo != null
                                    && ruolo.equalsIgnoreCase(role);
                        }

                        @Override
                        public boolean isSecure() {
                            return path.startsWith("https");
                        }

                        @Override
                        public String getAuthenticationScheme() {
                            return "Token-Based-Auth-Scheme";
                        }
                    });

                } else {
                    //se non va bene... 
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    		.entity(new ErrorResponse("Non sei autorizzato"))
                    		.build());
                }
            } catch (Exception e) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                		.entity(new ErrorResponse("Non sei autorizzato"))
                		.build());
            }
        } else {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
            		.entity(new ErrorResponse("Non sei autorizzato"))
            		.build());
        }
    }
}
