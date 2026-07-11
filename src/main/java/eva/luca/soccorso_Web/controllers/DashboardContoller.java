package eva.luca.soccorso_Web.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import eva.luca.soccorso_Web.data.MissioneDao;
import eva.luca.soccorso_Web.data.RequestDao;
import eva.luca.soccorso_Web.models.Missione;
import eva.luca.soccorso_Web.models.Request;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@WebServlet("/dshb")
public class DashboardContoller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final RequestDao serviceR = new RequestDao();
	private static final MissioneDao serviceM = new MissioneDao();


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String  action = request.getParameter("action");
		
		if (action.equals(null)) {
			action = "home";
		}
		
		switch (action) {
		case "home" -> home(request, response);
		default -> home(request, response);

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
	
	private void home(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		ArrayList<Request> pendenti = serviceR.findAllPending();
		ArrayList<Request> tutteR = serviceR.findAll();
		ArrayList<Missione> tutteM = serviceM.findAll();
		ArrayList<Missione> attive = serviceM.findAttive();
		ArrayList<Request> inEsecuzione = serviceR.findAllInExecution();
		
		int missioniAttive = attive.size();
		int missioniTotali = tutteM.size();
		
		int terminate = missioniTotali - missioniAttive;
		int percentualeTerminate = 0;
		
		if (missioniTotali > 0) {
			percentualeTerminate = (terminate * 100)/missioniTotali;
		}
		
		
		dataMap.put("all", tutteR);
		dataMap.put("pending", pendenti);
		dataMap.put("pending", pendenti);
		dataMap.put("attive", attive);
		dataMap.put("percentualeTerminate", percentualeTerminate);
		dataMap.put("inEsecuzione", inEsecuzione);
		
		renderTemplate(request, response, "Dashboard.html.ftl", dataMap);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
	}

}
