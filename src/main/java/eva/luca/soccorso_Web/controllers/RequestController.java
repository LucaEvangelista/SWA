package eva.luca.soccorso_Web.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import eva.luca.soccorso_Web.data.RequestDao;
import eva.luca.soccorso_Web.models.Request;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@WebServlet("/rqs")
public class RequestController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private final RequestDao service = new RequestDao();

    //http://localhost:8080/soccorso_Web/rqs?action=insert
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		if(action == null) {
			action = "list";
		}
		
		switch (action) {
		case "list" -> requestList(request, response);
		case "details" -> requestDetails(request, response);
		default -> requestList(request, response);
		
		}
		
	}
	
	
	private void renderTemplate(HttpServletRequest request, HttpServletResponse response, String templateName,
			Map<String, Object> dataModel) throws IOException, ServletException {

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);

		cfg.setServletContextForTemplateLoading(getServletContext(), "/WEB-INF/views");

		cfg.setDefaultEncoding("UTF-8");
		dataModel.put("contextPath", request.getContextPath());

		response.setContentType("text/html;charset=UTF-8");
		
	    HttpSession session = request.getSession(false);

	    if (session != null) {
	        dataModel.put("userid", session.getAttribute("userid"));
	        dataModel.put("username", session.getAttribute("username"));
	        dataModel.put("email", session.getAttribute("email"));
	        dataModel.put("ruolo", session.getAttribute("ruolo"));
	    }

		try {
			Template template = cfg.getTemplate(templateName);
			template.process(dataModel, response.getWriter());
		} catch (TemplateException e) {
			throw new ServletException("Errore rendering FreeMarker", e);
		}
	}
	
	private void requestList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{//capace di inoltrare la request alla jsp
		
		Map<String, Object> dataMap = new HashMap<>();
		
		dataMap.put("contextPath", request.getContextPath());
		dataMap.put("rqs", service.findAllNotPending());
		
		renderTemplate(request, response, "RequestList.html.ftl", dataMap); 
	}
	
	private void requestDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String idParam = request.getParameter("id");
		String flag;
		
		Map<String, Object>dataMap = new HashMap<>();
		
		if(idParam == null || idParam.isBlank()){
			response.sendRedirect(request.getContextPath() + "/rqs?action=list");
			return;
		}
		
		int id = Integer.parseInt(idParam);
		
		Request rq = service.findById(id);
		
		if(rq == null){
			response.sendRedirect(request.getContextPath() + "/rqs?action=list");
			return;
		}
		
		if(rq.getFase().equals("attiva")) {
			flag = "entrambi";
			dataMap.put("flg", flag);
		}
		
		if(rq.getFase().equals("rifiutata")) {
			flag = "nessuno";
			dataMap.put("flg", flag);
		}
		
		if(rq.getFase().equals("in esecuzione") || rq.getFase().equals("terminata")){
			flag = "singolo";
			dataMap.put("flg", flag);
		}
		
		dataMap.put("rqst", rq);
		dataMap.put("contextPath", request.getContextPath());
		renderTemplate(request, response, "RequestDetails.html.ftl", dataMap);
	
	}

	
	protected void rifiutaRichiesta(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		int richiestaID = Integer.parseInt(request.getParameter("richiestaId"));
		
		service.updateToRifiutata(richiestaID);
		
		response.sendRedirect(request.getContextPath() + "/rqs?action=list");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		if ("rifiutaRichiesta".equals(action)) {
			rifiutaRichiesta(request, response);
			return;
		}

	}
	

}
