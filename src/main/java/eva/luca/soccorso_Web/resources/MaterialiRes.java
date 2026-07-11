package eva.luca.soccorso_Web.resources;

import java.util.List;

import eva.luca.soccorso_Web.data.MaterialeDao;
import eva.luca.soccorso_Web.models.ErrorResponse;
import eva.luca.soccorso_Web.models.Materiale;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("materiali")
public class MaterialiRes {
	private final MaterialeDao serviceMt = new MaterialeDao();
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listaMateriali() {
		List<Materiale> materiali = serviceMt.findAll();
		
		return Response.ok(materiali).build();
	}
	
	@GET
	@Path("{id:[0-9]+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMezzoById(@PathParam("id") int id) {
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
	public Response createMateriale(Materiale mt) {
		
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
