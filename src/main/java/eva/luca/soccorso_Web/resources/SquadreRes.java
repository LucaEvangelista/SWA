package eva.luca.soccorso_Web.resources;

import java.util.List;

import eva.luca.soccorso_Web.data.SquadraDao;
import eva.luca.soccorso_Web.models.Squadra;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("squadre")
public class SquadreRes {
	private final SquadraDao serviceS = new SquadraDao();
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listaSquadre() {
		List<Squadra> squadre = serviceS.findSquadreNonOperative();
		
		return Response.ok(squadre).build();
	}
	

}
