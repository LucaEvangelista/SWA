package eva.luca.soccorso_Web.resources;

import java.util.List;

import eva.luca.soccorso_Web.data.PatenteDao;
import eva.luca.soccorso_Web.models.Patente;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("patenti")
public class PatentiRes {
	private final PatenteDao serviceP = new PatenteDao();
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listPatenti() {
		
		List<Patente> patenti = serviceP.findAll();
		
		return Response.ok(patenti).build();
	}

}
