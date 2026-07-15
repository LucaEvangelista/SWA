package eva.luca.soccorso_Web.resources;

import java.util.List;
import java.util.Set;

import eva.luca.soccorso_Web.data.MaterialeDao;
import eva.luca.soccorso_Web.models.ErrorResponse;
import eva.luca.soccorso_Web.models.Materiale;
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

@Path("materiali")
@Logged
public class MaterialiRes {
	private final MaterialeDao serviceMt = new MaterialeDao();
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listaMateriali(
			@DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("10") @QueryParam("size") int size,
	        @Parameter(description = "Filtra i materiali in base allo stato",
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
	    
		List<Materiale> materiali;
		
		long totaleMateriali;
		
	    if (stato == null) {
	    	// Nessun filtro: restituisce tutti gli operatori
	    	materiali = serviceMt.findAllPaginated(offset, size);
	    	totaleMateriali = serviceMt.countAllMateriali();

	    } else {
	    	// Filtro per stato
	    	materiali = serviceMt.findByStatoPaginated(stato, offset, size);
	    	totaleMateriali = serviceMt.countByStato(stato);
	    }
	    
	    int totalePagine = (int) Math.ceil((double) totaleMateriali / size);
	    
	    PaginatedResponse<Materiale> risultato = new PaginatedResponse<>(
	    				materiali,
	                    page,
	                    size,
	                    totaleMateriali,
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
	    
		Materiale mt = serviceMt.findById(id);
		
		if(mt == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Materiale non trovato"))
                    .build();
		}
		
		return Response.ok(mt).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createMateriale(Materiale mt, @Context SecurityContext securityContext) {
		
		if (!securityContext.isUserInRole("admin")) {
		    return Response.status(Response.Status.FORBIDDEN)
		            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
		            .build();
		}
	    
		if(mt.getTipologia() == null || mt.getTipologia().isBlank()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("you cannot leave type blank"))
					.build();
		}
		
		if(mt.getSeriale() == null || mt.getSeriale().isBlank()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("you cannot leave serial number blank"))
					.build();
		}
		
		boolean inserted = serviceMt.insert(mt);
		
		if(!inserted) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new ErrorResponse("error in saving process"))
					.build();
		}
		
		return Response.status(Response.Status.CREATED).entity(serviceMt.findBySeriale(mt.getSeriale())).build();
	}
}
