package eva.luca.soccorso_Web.resources;

import java.util.HashMap;
import java.util.Map;

import eva.luca.soccorso_Web.data.RequestDao;
import eva.luca.soccorso_Web.models.ErrorResponse;
import eva.luca.soccorso_Web.models.Request;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("convalida")
public class ConvalidaRes {
	private final RequestDao serviceR = new RequestDao();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response convalidaRichiesta(@QueryParam("uuid") String uuid) {
		
		if(uuid == null || uuid.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("UUID mancante"))
                    .build();
		}
		
		Request rq = serviceR.findByUuid(uuid);
		
		if(rq == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Richiesta non trovata"))
                    .build();
		}
		
		if(!"pendente".equals(rq.getFase())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse("Richiesta già attivata"))
                    .build();
		}
		
		boolean aggiornato = serviceR.updateToAttiva(uuid);
		
		if(!aggiornato) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(new ErrorResponse("error in updating process"))
					.build();
		}
		
        Request richiestaAggiornata = serviceR.findByUuid(uuid);

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Richiesta attivata");
        result.put("richiesta", richiestaAggiornata);

        return Response.ok(result).build();
	}

}
