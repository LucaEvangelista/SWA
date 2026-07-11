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

import eva.luca.soccorso_Web.data.AppartenenzaDao;
import eva.luca.soccorso_Web.data.SquadraDao;
import eva.luca.soccorso_Web.models.Appartenenza;
import eva.luca.soccorso_Web.models.Squadra;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@WebServlet("/sqd")
public class SquadraController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final SquadraDao serviceS = new SquadraDao();
	private final AppartenenzaDao serviceAp = new AppartenenzaDao();


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		
		if (action == null) {
			action = "list";
		}
		
		switch (action) {
		case "list" -> squadList(request, response);
		case "insert" -> newSquad(request, response);
		case "details" -> squadDetails(request, response);
		default -> newSquad(request, response);
		
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
	
	protected void squadDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		String idParam = request.getParameter("id");
		
		Map<String, Object> dataMap = new HashMap<>();
		
		if (idParam == null || idParam.isBlank()) {
			response.sendRedirect(request.getContextPath() + "/sqd?action=list");
			return;
		}
		
		int id = Integer.parseInt(idParam);
		Squadra sq = serviceS.findById(id);

		if (sq == null) {
			response.sendRedirect(request.getContextPath() + "/sqd?action=list");
			return;
		}
		
		dataMap.put("sqds", sq);
		dataMap.put("leadApp", serviceAp.finCapoBySquadID(id));
		dataMap.put("opApp", serviceAp.finOperatorBySquadID(id));
		dataMap.put("opNS", serviceAp.findOperatoriSenzaSquadra());
		dataMap.put("contextPath", request.getContextPath());
		
		renderTemplate(request, response, "SquadDetails.ftl", dataMap);
		
	}

	protected void squadList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		dataMap.put("contextPath", request.getContextPath());
		dataMap.put("sqds", serviceS.findAll());
		
		renderTemplate(request, response, "SquadList.ftl", dataMap);

	}
	
	protected void newSquad(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("contextPath", request.getContextPath());
		
		renderTemplate(request, response, "NewSquad.ftl", dataMap);
		

	}
	
	protected void removeOperatorFromSquad (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int operatoreId = Integer.parseInt(request.getParameter("operatorId"));
		int squadraId = Integer.parseInt(request.getParameter("squadId"));
		
		serviceAp.deleteByIDCombination(operatoreId, squadraId);
		
		response.sendRedirect(request.getContextPath() + "/sqd?action=details&id=" + squadraId);
	}
	
	protected void removeLeaderFromSquad (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int leaderId = Integer.parseInt(request.getParameter("leaderId"));
		int squadraId = Integer.parseInt(request.getParameter("squadId"));
		
		serviceAp.deleteByIDCombination(leaderId, squadraId);
		
		response.sendRedirect(request.getContextPath() + "/sqd?action=details&id=" + squadraId);
	}
	
	protected void addOperatorToSquad (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int squadraId = Integer.parseInt(request.getParameter("squadId"));
		String[] operatoriId = request.getParameterValues("operatore");
		
		if(operatoriId != null) {
			for(String opID : operatoriId) {
				int operatoreId = Integer.parseInt(opID);
				
				Appartenenza app = new Appartenenza(operatoreId, squadraId, false);
		
				serviceAp.insert(app);
			}
		}
		
		response.sendRedirect(request.getContextPath() + "/sqd?action=details&id=" + squadraId);
	}
	
	protected void addLeaderToSquad (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int squadraId = Integer.parseInt(request.getParameter("squadId"));
		String[] operatoriId = request.getParameterValues("operatore");
		
		if(operatoriId != null) {
			for(String opID : operatoriId) {
				int operatoreId = Integer.parseInt(opID);
				
				Appartenenza app = new Appartenenza(operatoreId, squadraId, true);
		
				serviceAp.insert(app);
			}
		}
		
		response.sendRedirect(request.getContextPath() + "/sqd?action=details&id=" + squadraId);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name = request.getParameter("squad-name");
		
	    String action = request.getParameter("action");


	    if ("removeOperator".equals(action)) {
	        removeOperatorFromSquad(request, response);
	        return;
	    }
	    
	    if ("removeLeader".equals(action)) {
	        removeLeaderFromSquad(request, response);
	        return;
	    }
	    
	    if ("addMembri".equals(action)) {
	    	addOperatorToSquad(request, response);
	    	return;
	    }
	    
	    if ("addLeader".equals(action)) {
	    	addLeaderToSquad(request, response);
	    	return;
	    }
	    
		
		Squadra squad = new Squadra(name);
		
		if (serviceS.insert(squad)) {
			response.sendRedirect(request.getContextPath() + "/sqd?action=list");
		} else {
			response.sendRedirect("Error.html");
		}
	}

}
