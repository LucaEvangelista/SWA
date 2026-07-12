package eva.luca.soccorso_Web.resources;

import java.net.URI;
import java.util.List;

import eva.luca.soccorso_Web.data.MaterialeDao;
import eva.luca.soccorso_Web.data.MaterialiMissioneDao;
import eva.luca.soccorso_Web.data.MezziMissioneDao;
import eva.luca.soccorso_Web.data.MezzoDao;
import eva.luca.soccorso_Web.data.MissioneDao;
import eva.luca.soccorso_Web.data.OperatorDao;
import eva.luca.soccorso_Web.data.RequestDao;
import eva.luca.soccorso_Web.data.SquadraDao;
import eva.luca.soccorso_Web.models.ErrorResponse;
import eva.luca.soccorso_Web.models.Materiale;
import eva.luca.soccorso_Web.models.MaterialiMissione;
import eva.luca.soccorso_Web.models.MezziMissione;
import eva.luca.soccorso_Web.models.Mezzo;
import eva.luca.soccorso_Web.models.Missione;
import eva.luca.soccorso_Web.models.Squadra;
import eva.luca.soccorso_Web.utility.Logged;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("missioni")
@Logged
public class MissioniRes {
	private final MissioneDao serviceMs = new MissioneDao();
	private final SquadraDao serviceSq = new SquadraDao();
	private final MezziMissioneDao serviceMzm = new MezziMissioneDao();
	private final MaterialiMissioneDao serviceMtm = new MaterialiMissioneDao();
	private final MezzoDao serviceMz = new MezzoDao();
	private final MaterialeDao serviceMa = new MaterialeDao();
	private final OperatorDao serviceO = new OperatorDao();
	private final RequestDao serviceR = new RequestDao();
	
	public static class MissioneCreateRequest {
	    public Integer richiestaId;
	    public String obiettivo;
	    public String posizione;
	    public Integer squadraId;
	    public List<Integer> mezziIds;
	    public List<Integer> materialiIds;
	}
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listaMissioni(@Context SecurityContext securityContext) {
		
	    	
			if (!securityContext.isUserInRole("admin")) {
			    return Response.status(Response.Status.FORBIDDEN)
			            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
			            .build();
			}

		
		List<Missione> missioni = serviceMs.findAll();
		
		return Response.ok(missioni).build();
	}
	
	@GET
	@Path("{id:[0-9]+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMissioneById(@PathParam("id") int id, @Context SecurityContext securityContext) {
		

			if (!securityContext.isUserInRole("admin")) {
			    return Response.status(Response.Status.FORBIDDEN)
			            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
			            .build();
			}
			
		
		Missione ms = serviceMs.findById(id);
		
		if (ms == null)  {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Missione non trovata"))
                    .build();
        }
		
		return Response.ok(ms).build();
	}
	
	@GET
	@Path("{id:[0-9]+}/squadra")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSquadraByMissionId(@PathParam("id") int id, @Context SecurityContext securityContext) {

			if (!securityContext.isUserInRole("admin")) {
			    return Response.status(Response.Status.FORBIDDEN)
			            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
			            .build();
			}

		
		Squadra sq = serviceSq.findByMissioneId(id);
		
		if (sq == null)  {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(new ErrorResponse("Squadra non trovata"))
					.build();
		}
		
		return Response.ok(sq).build();
	}
	
	@GET
	@Path("{id:[0-9]+}/mezzi")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMezziByMissionId(@PathParam("id") int id, @Context SecurityContext securityContext) {
	    	
			if (!securityContext.isUserInRole("admin")) {
			    return Response.status(Response.Status.FORBIDDEN)
			            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
			            .build();
			}

		
		Missione ms = serviceMs.findById(id);
		
		List<Mezzo> mezzi = serviceMzm.mezziOfTheMission(id);
		
		if (ms == null)  {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(new ErrorResponse("Missione non trovata"))
					.build();
		}
		
		return Response.ok(mezzi).build();
	}
	
	@GET
	@Path("{id:[0-9]+}/materiali")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMaterialiByMissionId(@PathParam("id") int id, @Context SecurityContext securityContext) {

			if (!securityContext.isUserInRole("admin")) {
			    return Response.status(Response.Status.FORBIDDEN)
			            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
			            .build();
			}

		
		List<Materiale> materiali = serviceMtm.materialiOfTheMission(id);
		
		Missione ms = serviceMs.findById(id);
		
		if (ms == null)  {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(new ErrorResponse("Missione non trovata"))
					.build();
		}
		
		return Response.ok(materiali).build();
	}
	
	@PUT
	@Path("{id:[0-9]+}/terminazione")
	@Produces(MediaType.APPLICATION_JSON)
	public Response termineMissione(@PathParam("id") int id, @Context SecurityContext securityContext) {
		
			if (!securityContext.isUserInRole("admin")) {
			    return Response.status(Response.Status.FORBIDDEN)
			            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
			            .build();
			}

		
		Missione ms = serviceMs.findById(id);

		if (ms == null)  {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(new ErrorResponse("Missione non trovata"))
					.build();
		}
		
		Squadra sq = serviceSq.findByMissioneId(id);
		
		if (sq == null)  {
			return Response.status(Response.Status.CONFLICT)
					.entity(new ErrorResponse("Nessuna squadra associata alla missione"))
					.build();
		}
		
		serviceMa.updateStatusByMissioneId(id, "libero");
		serviceMz.updateStatusByMissioneId(id, "libero");
		serviceO.updateStatusBySquadraId(sq.getSquadraId(), "libero");
		serviceR.updateToterminata(ms.getRichiestaRif());
		serviceMs.updateToTerminata(id);
		
		return Response.ok(serviceMs.findById(id)).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createMission(MissioneCreateRequest rqst, @Context SecurityContext securityContext) {
	    	
			if (!securityContext.isUserInRole("admin")) {
			    return Response.status(Response.Status.FORBIDDEN)
			            .entity(new ErrorResponse("Non hai i permessi per visualizzare la richiesta selezionata"))
			            .build();
			}


	    if (rqst == null) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity(new ErrorResponse("Body JSON mancante"))
	                .build();
	    }

	    if (rqst.richiestaId == null) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity(new ErrorResponse("richiestaId obbligatorio"))
	                .build();
	    }

	    if (rqst.squadraId == null) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity(new ErrorResponse("squadraId obbligatorio"))
	                .build();
	    }

	    if (rqst.obiettivo == null || rqst.obiettivo.isBlank()) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity(new ErrorResponse("obiettivo obbligatorio"))
	                .build();
	    }

	    if (rqst.posizione == null || rqst.posizione.isBlank()) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                .entity(new ErrorResponse("posizione obbligatoria"))
	                .build();
	    }

	    if (serviceR.findById(rqst.richiestaId) == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                .entity(new ErrorResponse("Richiesta non trovata"))
	                .build();
	    }
	    
	    Missione giaEsistente = serviceMs.findByRequestID(rqst.richiestaId);
	    
	    if (giaEsistente != null) {
	        return Response.status(Response.Status.CONFLICT)
	                .entity(new ErrorResponse("Esiste già una missione per questa richiesta"))
	                .build();
	    }
	    
	    Missione missione = new Missione();

	    missione.setRichiestaRif(rqst.richiestaId);
	    missione.setObiettivo(rqst.obiettivo);
	    missione.setPosizione(rqst.posizione);
	    missione.setSquadraRif(rqst.squadraId);

	    int missionId = serviceMs.insertAndReturnID(missione);

	    if (missionId <= 0) {
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                .entity(new ErrorResponse("Errore durante la creazione della missione"))
	                .build();
	    }
	    
	    if (rqst.mezziIds != null) {
	        for (Integer mezzoId : rqst.mezziIds) {
	            if (mezzoId == null) {
	                continue;
	            }

	            MezziMissione mzM = new MezziMissione();

	            mzM.setMissioneRif(missionId);
	            mzM.setMezzoRif(mezzoId);

	            serviceMzm.insert(mzM);
	        }
	    }

	    if (rqst.materialiIds != null) {
	        for (Integer materialeId : rqst.materialiIds) {
	            if (materialeId == null) {
	                continue;
	            }

	            MaterialiMissione mtM = new MaterialiMissione();

	            mtM.setMissioneRif(missionId);
	            mtM.setMaterialeRif(materialeId);

	            serviceMtm.insert(mtM);
	        }
	    }

	    serviceMa.updateStatusByMissioneId(missionId, "occupato");
	    serviceMz.updateStatusByMissioneId(missionId, "occupato");
	    serviceO.updateStatusBySquadraId(rqst.squadraId, "occupato");
	    serviceR.updateToEsecuzione(rqst.richiestaId);

	    Missione creata = serviceMs.findById(missionId);

	    return Response.created(URI.create("/missioni/" + missionId))
	            .entity(creata)
	            .build();
	    
	}
	    
	    
}



