package eva.luca.soccorso_Web.resources;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eva.luca.soccorso_Web.data.CompetenzeOperatoreDao;
import eva.luca.soccorso_Web.data.MissioneDao;
import eva.luca.soccorso_Web.data.OperatorDao;
import eva.luca.soccorso_Web.data.PatenteOperatoreDao;
import eva.luca.soccorso_Web.models.CompetenzeOperatore;
import eva.luca.soccorso_Web.models.ErrorResponse;
import eva.luca.soccorso_Web.models.Operator;
import eva.luca.soccorso_Web.models.PaginatedResponse;
import eva.luca.soccorso_Web.models.PatenteOperatore;
import eva.luca.soccorso_Web.models.SuccessResponse;
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

@Path("operatori")
@Logged
public class OperatoriRes {
	private final OperatorDao serviceO = new OperatorDao();
	private final PatenteOperatoreDao servicePo = new PatenteOperatoreDao();
	private final CompetenzeOperatoreDao serviceCo = new CompetenzeOperatoreDao();
	private final MissioneDao serviceM = new MissioneDao();
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listOperatori(
			@DefaultValue("1") @QueryParam("page") int page,
			@DefaultValue("10") @QueryParam("size") int size,
	        @Parameter(description = "Filtra gli operatori in base allo stato",
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
		
	    List<Operator> operatori;
	    
	    long totaleOperatori;
	    
	    if (stato == null) {
	    	// Nessun filtro: restituisce tutti gli operatori
	        operatori = serviceO.findAllPaginated(offset, size);
	        totaleOperatori = serviceO.countAllOperatori();

	    } else {
	    	// Filtro per stato
	        operatori = serviceO.findByStatoPaginated(stato, offset, size);
	        totaleOperatori = serviceO.countByStato(stato);
	    }
	    
	    int totalePagine = (int) Math.ceil((double) totaleOperatori / size);
	    
	    PaginatedResponse<Operator> risultato = new PaginatedResponse<>(
	                    operatori,
	                    page,
	                    size,
	                    totaleOperatori,
	                    totalePagine);

	    return Response.ok(risultato).build();
	}
	
	@GET
	@Path("{id:[0-9]+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOperatoreById(@PathParam("id") int id) {
		
		Operator op = serviceO.findById(id);
		
		if(op == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Operatore non trovato"))
                    .build();
        }
		
		Map<String, Object> dataMap = new LinkedHashMap<>();
		
		dataMap.put("Operatore", op);
//		dataMap.put("PatentiOperatore", servicePo.findByOperatorID(id));
//		dataMap.put("AbilitaOperatore", serviceCo.findByOperatorID(id)); 
		dataMap.put("MissioneAttiva", serviceM.findByOperatoreId(id));
//		dataMap.put("StoricoMissioni", serviceM.storicoByOperatoreId(id));
		
		return Response.ok(dataMap).build();
	}
	
	@GET
	@Path("{id:[0-9]+}/patenti")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPatenteByOpId(@PathParam("id") int id) {
		
		Operator op = serviceO.findById(id);
		
		if(op == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(new ErrorResponse("Operatore non trovato"))
					.build();
		}
		
		return Response.ok(servicePo.findByOperatorID(id)).build();
	}
	
	@GET
	@Path("{id:[0-9]+}/abilita")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAbilitaByOpId(@PathParam("id") int id) {
		
		Operator op = serviceO.findById(id);
		
		if(op == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(new ErrorResponse("Operatore non trovato"))
					.build();
		}
		
		return Response.ok(serviceCo.findByOperatorID(id)).build();
	}
	
	@GET
	@Path("{id:[0-9]+}/storico")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getStoricoByOpId(@PathParam("id") int id) {

		Operator op = serviceO.findById(id);
		
		if(op == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(new ErrorResponse("Operatore non trovato"))
					.build();
		}
		
		return Response.ok(serviceM.storicoByOperatoreId(id)).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createOperator(Operator op, @Context SecurityContext securityContext) {

		if (!securityContext.isUserInRole("admin")) {
		    return Response.status(Response.Status.FORBIDDEN)
		            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
		            .build();
		}
		
		if(op.getName() == null || op.getName().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("you cannot leave name blank"))
                    .build();
        }
		
		if(op.getSurname() == null || op.getSurname().isBlank()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("you cannot leave surname blank"))
					.build();
		}
		
		if(op.getAge() == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("you cannot leave date of birth blank"))
					.build();
		}
		
		if(op.getEmail() == null || op.getEmail().isBlank()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("you cannot leave email blank"))
					.build();
		}
		
		if(op.getPasskey() == null || op.getPasskey().isBlank()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("you cannot leave temporary passkey blank"))
					.build();
		}
		
		boolean inserted = serviceO.insert(op);
		
		if(!inserted) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new ErrorResponse("error in saving process"))
					.build();
		}
		
		return Response.status(Response.Status.CREATED).entity(serviceO.findByMail(op.getEmail())).build();
	}
	
	@POST
	@Path("patenti")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPatenteToOperatore(PatenteOperatore po) {
		
	    boolean inserito = servicePo.insert(po);

	    if (!inserito) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity(new ErrorResponse("Patente non aggiunta all'operatore"))
	                .build();
	    }

	    return Response.ok()
	            .entity(new SuccessResponse("Patente aggiunta all'operatore"))
	            .build();
	}
	
	@POST
	@Path("abilita")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addAbilitaToOperatore(CompetenzeOperatore co) {
		
		boolean inserito = serviceCo.insert(co);
		
		if (!inserito) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new ErrorResponse("Abilità non aggiunta all'operatore"))
					.build();
		}
		
		return Response.ok()
				.entity(new SuccessResponse("Abilità aggiunta all'operatore"))
				.build();
	}
	
}
