package eva.luca.soccorso_Web.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import eva.luca.soccorso_Web.data.RequestDao;
import eva.luca.soccorso_Web.models.Request;
import eva.luca.soccorso_Web.utility.UtilityMethods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@WebServlet(urlPatterns = {"/index.html", "/prqs"})
public class PublicRqs extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final RequestDao service = new RequestDao();
       


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		if(action == null) {
			action = "insert";
		}
		
		switch (action) {
		case "insert" -> requestInsert(request, response);
		default -> requestInsert(request, response);
		
		}
		
	}
	
	private void renderTemplate(HttpServletRequest request, HttpServletResponse response, String templateName,
			Map<String, Object> dataModel) throws IOException, ServletException {

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);

		cfg.setServletContextForTemplateLoading(getServletContext(), "/WEB-INF/views");

		cfg.setDefaultEncoding("UTF-8");
		dataModel.put("contextPath", request.getContextPath());

		response.setContentType("text/html;charset=UTF-8");

		try {
			Template template = cfg.getTemplate(templateName);
			template.process(dataModel, response.getWriter());
		} catch (TemplateException e) {
			throw new ServletException("Errore rendering FreeMarker", e);
		}
	}
	
	private void requestInsert(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{	
		
		Map<String, Object> dataMap = new HashMap<>();
		
		dataMap.put("contextPath", request.getContextPath());
		
		renderTemplate(request, response, "NewRequest_home.ftl", dataMap);
		 
	}
	
	private void saveRequest(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{	
		
		String nam = request.getParameter("request-name");
		String mail = request.getParameter("request-email");
		String adr = request.getParameter("request-address");
		String dex = request.getParameter("request-description");
		String uuid = UUID.randomUUID().toString();
		
		if(service.findExistRecentRequestByEmail(mail)) {
	        Map<String, Object> dataMap = new HashMap<>();

	        dataMap.put("contextPath", request.getContextPath());
	        dataMap.put("error", "Hai già inviato una richiesta con questa email negli ultimi 2 minuti.");

	        renderTemplate(request, response, "NewRequest_home.ftl", dataMap);
	        return;
	    }
		
		
		Request req = new Request(nam, mail, adr, dex, uuid);
		
		
		if(service.insert(req)) {
	        Map<String, Object> dataMap = new HashMap<>();

	        dataMap.put("contextPath", request.getContextPath());
	        dataMap.put("success", "richiesta inserita correttamente");
	        
	        UtilityMethods.sendEmailWithCodes(req);

	        renderTemplate(request, response, "NewRequest_home.ftl", dataMap);
			} else {
				response.sendRedirect("Error.html");
			}
		
	}
		


	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		if("insert".equals(action)) {
			saveRequest(request, response);
		}

	}

}
