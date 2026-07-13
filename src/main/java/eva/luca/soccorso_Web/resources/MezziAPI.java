package eva.luca.soccorso_Web.resources;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import eva.luca.soccorso_Web.data.MezzoDao;
import eva.luca.soccorso_Web.models.ErrorResponse;
import eva.luca.soccorso_Web.models.Mezzo;

//iniziale svolgimento tramite servlet esplicita

@WebServlet("/api/mezzi/*")
public class MezziAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final MezzoDao serviceM = new MezzoDao();
	private final ObjectMapper mapper = new ObjectMapper();
       



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer varId = extractIdFromPathre(request);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		if(varId != null) {
			getMezzoById(response, varId);
		} else {
			listMezzi(response);
		}
		
	}
	
	private void listMezzi(HttpServletResponse res) throws JacksonException, IOException {
		List<Mezzo> mezzi = serviceM.findAll();
		
		res.setStatus(HttpServletResponse.SC_OK);
		mapper.writeValue(res.getWriter(), mezzi);
	}
	
	private void getMezzoById(HttpServletResponse res, Integer id) throws JacksonException, IOException {
		Mezzo mz = serviceM.findById(id);
		
		if(mz == null) {
			showError(res, HttpServletResponse.SC_NOT_FOUND, "not found");
			return;
		}
			
		res.setStatus(HttpServletResponse.SC_OK);
		mapper.writeValue(res.getWriter(), mz);
	}
	


	private Integer extractIdFromPathre(HttpServletRequest request) {
		String pathInfo = request.getPathInfo(); //prende tutto quello dopo lo / (praticamente tutto quello dopo il context path)
		
		if (pathInfo == null || pathInfo.equals("/")) {
			return null;
		}
		try {
			String idParam = pathInfo.substring(1); //estrae l'ultimo elemento della stringa path
			return Integer.parseInt(idParam);
		} catch (Exception e) {
			return null;		
			}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		Mezzo mz = mapper.readValue(request.getInputStream(), Mezzo.class);
		
		if(mz.getTipologia() == null || mz.getTipologia().isBlank()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			ErrorResponse error = new ErrorResponse("you cannot leave type blank");
			
			mapper.writeValue(response.getWriter(), error);
			return;
		}
		
		if(mz.getSeriale() == null || mz.getSeriale().isBlank()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			ErrorResponse error = new ErrorResponse("you cannot leave serial number blank");
			
			mapper.writeValue(response.getWriter(), error);
			return;
		}
		
		boolean inserted = serviceM.insert(mz);
		
		if(inserted) {
			response.setStatus(HttpServletResponse.SC_CREATED);
			mapper.writeValue(response.getWriter(), mz);
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			ErrorResponse error = new ErrorResponse("error in saving procedure");
			mapper.writeValue(response.getWriter(), error);
		}
		
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPut(req, resp);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doDelete(req, resp);
	}
	
	
	private void showError(HttpServletResponse resp, int type, String motivation) throws JacksonException, IOException {
		resp.setStatus(type);
		ErrorResponse error = new ErrorResponse(motivation);
		
		mapper.writeValue(resp.getWriter(), error);
	}

}
