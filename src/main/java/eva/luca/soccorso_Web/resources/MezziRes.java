package eva.luca.soccorso_Web.resources;

import java.util.List;

import eva.luca.soccorso_Web.data.MezzoDao;
import eva.luca.soccorso_Web.models.ErrorResponse;
import eva.luca.soccorso_Web.models.Mezzo;
import eva.luca.soccorso_Web.utility.Logged;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("mezzi")
@Logged
public class MezziRes {
	private final MezzoDao serviceMz = new MezzoDao();
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listaMezzi(@Context SecurityContext securityContext) {
		
	    try {
	    	
			if (!securityContext.isUserInRole("admin")) {
			    return Response.status(Response.Status.FORBIDDEN)
			            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
			            .build();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		List<Mezzo> mezzi = serviceMz.findAll();
		
		/*
		 * impostiamo il codice 200 (ok)
		 * alleghiamo i dati da trasmettere e generiamo
		 * la response da restituire
		 */
		
		return Response.ok(mezzi).build();
	}
	
	@GET
	@Path("{id:[0-9]+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMezzoById(@PathParam("id") int id, @Context SecurityContext securityContext) {
		
	    try {
	    	
			if (!securityContext.isUserInRole("admin")) {
			    return Response.status(Response.Status.FORBIDDEN)
			            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
			            .build();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		Mezzo mz = serviceMz.findById(id);
		
		if(mz == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Mezzo non trovato"))
                    .build();
        }
		
		return Response.ok(mz).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createMezzo(Mezzo mz, @Context SecurityContext securityContext) {
		
	    try {
	    	
			if (!securityContext.isUserInRole("admin")) {
			    return Response.status(Response.Status.FORBIDDEN)
			            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
			            .build();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		
		if(mz.getTipologia() == null || mz.getTipologia().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("you cannot leave type blank"))
                    .build();
        }
		
		if(mz.getSeriale() == null || mz.getSeriale().isBlank()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("you cannot leave serial number blank"))
					.build();
		}
		
		boolean inserted = serviceMz.insert(mz);
		
		if(!inserted) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new ErrorResponse("error in saving process"))
					.build();
		}
		
		return Response.status(Response.Status.CREATED).entity(serviceMz.findBySeriale(mz.getSeriale())).build();
	}
}
