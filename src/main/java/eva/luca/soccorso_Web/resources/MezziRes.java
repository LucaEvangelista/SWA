package eva.luca.soccorso_Web.resources;

import java.util.List;
import java.util.Set;

import eva.luca.soccorso_Web.data.MezzoDao;
import eva.luca.soccorso_Web.models.ErrorResponse;
import eva.luca.soccorso_Web.models.Mezzo;
import eva.luca.soccorso_Web.models.PaginatedResponse;
import eva.luca.soccorso_Web.utility.Logged;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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
	public Response listaMezzi(
			@DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("10") @QueryParam("size") int size,
	        @Parameter(description = "Filtra i mezzi in base allo stato",
            schema = @Schema(allowableValues = {
                    "libero",
                    "occupato"}))
			@QueryParam("stato")String stato,
			
			@Context SecurityContext securityContext) {

    	
		if (!securityContext.isUserInRole("admin")) {
		    return Response.status(Response.Status.FORBIDDEN)
		            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
		            .build();
		}
	    if (page < 1) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity(new ErrorResponse("Il numero della pagina deve essere maggiore o uguale a 1"))
	                .build();
	    }
	    
	    if (size < 1 || size > 100) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity(new ErrorResponse("La dimensione della pagina deve essere compresa tra 1 e 100"))
	                .build();
	    }
	    
	    //controllo su stato, se non presente rimane vuoto
	    //se contiene spazi vengono tolti
	    if (stato != null) {
	        stato = stato.trim().toLowerCase();

	        if (stato.isBlank()) {
	            stato = null;
	        }
	    }
	    
	    Set<String> statiPermessi = Set.of("libero", "occupato");
	    
	    if (stato != null && !statiPermessi.contains(stato)) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity(new ErrorResponse("Stato non valido. Valori consentiti; libero e occupato."))
	                .build();
	    }
	    
	    int offset = (page - 1) * size;
	    
		List<Mezzo> mezzi;
		
		long totaleMezzi;
		
	    if (stato == null) {
	    	// Nessun filtro: restituisce tutti gli operatori
	    	mezzi = serviceMz.findAllPaginated(offset, size);
	    	totaleMezzi = serviceMz.countAllMezzi();

	    } else {
	    	// Filtro per stato
	    	mezzi = serviceMz.findByStatoPaginated(stato, offset, size);
	    	totaleMezzi = serviceMz.countByStato(stato);
	    }
	    
	    int totalePagine = (int) Math.ceil((double) totaleMezzi / size);
	    
	    PaginatedResponse<Mezzo> risultato = new PaginatedResponse<>(
	    				mezzi,
	                    page,
	                    size,
	                    totaleMezzi,
	                    totalePagine);

	    return Response.ok(risultato).build();
	}
	
	@GET
	@Path("{id:[0-9]+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMezzoById(@PathParam("id") int id, @Context SecurityContext securityContext) {
	    	
		if (!securityContext.isUserInRole("admin")) {
		    return Response.status(Response.Status.FORBIDDEN)
		            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
		            .build();
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
	    	
		if (!securityContext.isUserInRole("admin")) {
		    return Response.status(Response.Status.FORBIDDEN)
		            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
		            .build();
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
