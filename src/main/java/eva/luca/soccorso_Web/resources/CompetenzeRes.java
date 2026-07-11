package eva.luca.soccorso_Web.resources;

import java.util.List;

import eva.luca.soccorso_Web.data.CompetenzaDao;
import eva.luca.soccorso_Web.models.Competenza;
import eva.luca.soccorso_Web.utility.Logged;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("abilita")
@Logged
public class CompetenzeRes {
	private final CompetenzaDao serviceC = new CompetenzaDao();
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listCompetenze() {
		
		List<Competenza> competenze = serviceC.findAll();
		
		return Response.ok(competenze).build();
	}

}
