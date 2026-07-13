package eva.luca.soccorso_Web.resources;

import java.util.List;

import eva.luca.soccorso_Web.data.AdminDao;
import eva.luca.soccorso_Web.models.Admin;
import eva.luca.soccorso_Web.models.ErrorResponse;
import eva.luca.soccorso_Web.utility.Logged;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("admin")
@Logged
public class AdminRes {
	private final AdminDao serviceA = new AdminDao();
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listAdmin(@Context SecurityContext securityContext) {

		if (!securityContext.isUserInRole("admin")) {
		    return Response.status(Response.Status.FORBIDDEN)
		            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
		            .build();
		}
		
	    List<Admin> amministratori = serviceA.findAll();

	    return Response.ok(amministratori).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAdmin(Admin ad, @Context SecurityContext securityContext) {
		
		if (!securityContext.isUserInRole("admin")) {
		    return Response.status(Response.Status.FORBIDDEN)
		            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
		            .build();
		}
		
		if(ad.getName() == null || ad.getName().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("you cannot leave name blank"))
                    .build();
        }
		
		if(ad.getEmail() == null || ad.getEmail().isBlank()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("you cannot leave email blank"))
					.build();
		}
		
		if(ad.getPasskey() == null || ad.getPasskey().isBlank()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("you cannot leave temporary passkey blank"))
					.build();
		}
		
		boolean inserted = serviceA.insert(ad);
		
		if(!inserted) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new ErrorResponse("error in saving process"))
					.build();
		}
		
		return Response.status(Response.Status.CREATED).entity(serviceA.findByMail(ad.getEmail())).build();
	}
	
}
