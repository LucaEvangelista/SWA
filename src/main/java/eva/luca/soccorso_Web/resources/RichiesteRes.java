package eva.luca.soccorso_Web.resources;

import java.util.List;
import java.util.UUID;

import eva.luca.soccorso_Web.data.RequestDao;
import eva.luca.soccorso_Web.models.ErrorResponse;
import eva.luca.soccorso_Web.models.PaginatedResponse;
import eva.luca.soccorso_Web.models.Request;
import eva.luca.soccorso_Web.utility.Logged;
import eva.luca.soccorso_Web.utility.UtilityMethods;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("richieste")
public class RichiesteRes {
	private final RequestDao serviceR = new RequestDao();
	
	
	@GET
	@Path("list")
	@Logged
	@Produces(MediaType.APPLICATION_JSON)
	public Response listaRichieste(
	        @DefaultValue("1") @QueryParam("page") int page,
	        @DefaultValue("10") @QueryParam("size") int size,
	        @Context SecurityContext securityContext) {

	    if (!securityContext.isUserInRole("admin")) {
	        return Response.status(Response.Status.FORBIDDEN)
	                .entity(new ErrorResponse("Non hai i permessi per visualizzare la lista delle richieste"))
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

	    int offset = (page - 1) * size;

	    List<Request> richieste = serviceR.findAllNotPendingPaginated(offset, size);

	    long totaleRichieste = serviceR.countAllNotPending();

	    int totalePagine = (int) Math.ceil((double) totaleRichieste / size);

	    PaginatedResponse<Request> risultato = new PaginatedResponse<>(richieste,
	                    page,
	                    size,
	                    totaleRichieste,
	                    totalePagine);

	    return Response.ok(risultato).build();
	}
	
	@GET
	@Path("{id:[0-9]+}")
	@Logged
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRichiestaById(@PathParam("id") int id, @Context SecurityContext securityContext) {

			if (!securityContext.isUserInRole("admin")) {
			    return Response.status(Response.Status.FORBIDDEN)
			            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
			            .build();
			}
	    
		Request rq = serviceR.findById(id);
		
		if (rq == null)  {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Richiesta non trovata"))
                    .build();
        }
		
		return Response.ok(rq).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createRequest(Request rq) {
		
		if(rq.getNomePersona() == null || rq.getNomePersona().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("you cannot leave name blank"))
                    .build();
		}
		
		if(rq.getMailPersona() == null || rq.getMailPersona().isBlank()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("you cannot leave mail blank"))
					.build();
		}
		
		if(rq.getDescrizione() == null || rq.getDescrizione().isBlank()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("you cannot leave description blank"))
					.build();
		}
		
		if(rq.getIndirizzo() == null || rq.getIndirizzo().isBlank()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("you cannot leave address blank"))
					.build();
		}
		
		if(serviceR.findExistRecentRequestByEmail(rq.getMailPersona())) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("you sent a request less than two minute ago"))
					.build();
		}
		
		String uuid = UUID.randomUUID().toString();
		
		rq.setUuid(uuid);
		
		boolean inserted = serviceR.insert(rq);
		
		if(!inserted) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new ErrorResponse("error in saving process"))
					.build();
		}
		
		UtilityMethods.sendEmailWithCodes(rq);		
		return Response.status(Response.Status.CREATED).entity(serviceR.findByUuid(uuid)).build();
		
	}
	
	@PUT
	@Path("{id:[0-9]+}/rifiutata")
	@Logged
	@Produces(MediaType.APPLICATION_JSON)
	public Response rifiutaRichiesta(@PathParam("id") int id, @Context SecurityContext securityContext) {

			if (!securityContext.isUserInRole("admin")) {
			    return Response.status(Response.Status.FORBIDDEN)
			            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
			            .build();
			}
		
		Request rq = serviceR.findById(id);
		
		if (rq == null)  {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Richiesta non trovata"))
                    .build();
        }
		
		if(!"attiva".equals(rq.getFase())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse("Non puoi rifiutare una richiesta non attiva"))
                    .build();
		}
		
		boolean updated = serviceR.updateToRifiutata(id);
		
		if(!updated) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new ErrorResponse("Richiesta non rifiutata"))
					.build();
		}
		
		return Response.ok(serviceR.findById(id)).build();
	}
	
	@PUT
	@Path("{id:[0-9]+}/lavorazione")
	@Logged
	@Produces(MediaType.APPLICATION_JSON)
	public Response richiestaInLavorazione(@PathParam("id") int id, @Context SecurityContext securityContext) {

			if (!securityContext.isUserInRole("admin")) {
			    return Response.status(Response.Status.FORBIDDEN)
			            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
			            .build();
			}
		
		Request rq = serviceR.findById(id);
		
		if (rq == null)  {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(new ErrorResponse("Richiesta non trovata"))
					.build();
		}
		
		if(!"attiva".equals(rq.getFase())) {
			return Response.status(Response.Status.CONFLICT)
					.entity(new ErrorResponse("Non puoi portare in lavorazione una richiesta non attiva"))
					.build();
		}
		
		boolean updated = serviceR.updateToEsecuzione(id);
		
		if(!updated) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new ErrorResponse("Richiesta non messa in lavorazione"))
					.build();
		}
		
		return Response.ok(serviceR.findById(id)).build();
	}
	
	@PUT
	@Path("{id:[0-9]+}/terminata")
	@Logged
	@Produces(MediaType.APPLICATION_JSON)
	public Response richiestaTerminata(@PathParam("id") int id) {
		
		Request rq = serviceR.findById(id);
		
		if (rq == null)  {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(new ErrorResponse("Richiesta non trovata"))
					.build();
		}
		
		if(!"in esecuzione".equals(rq.getFase())) {
			return Response.status(Response.Status.CONFLICT)
					.entity(new ErrorResponse("Non puoi terminare una richiesta non in lavorazione"))
					.build();
		}
		
		boolean updated = serviceR.updateToterminata(id);
		
		if(!updated) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new ErrorResponse("Richiesta non terminata"))
					.build();
		}
		
		return Response.ok(serviceR.findById(id)).build();
	}

}
