package eva.luca.soccorso_Web.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import eva.luca.soccorso_Web.data.AdminDao;
import eva.luca.soccorso_Web.data.CompetenzaDao;
import eva.luca.soccorso_Web.data.CompetenzeOperatoreDao;
import eva.luca.soccorso_Web.data.MissioneDao;
import eva.luca.soccorso_Web.data.OperatorDao;
import eva.luca.soccorso_Web.data.PatenteDao;
import eva.luca.soccorso_Web.data.PatenteOperatoreDao;
import eva.luca.soccorso_Web.models.Admin;
import eva.luca.soccorso_Web.models.CompetenzeOperatore;
import eva.luca.soccorso_Web.models.Operator;
import eva.luca.soccorso_Web.models.PatenteOperatore;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@WebServlet("/opt")
public class OperatorController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final OperatorDao service = new OperatorDao();
	private final PatenteOperatoreDao servicePO = new PatenteOperatoreDao();
	private final PatenteDao serviceP = new PatenteDao();
	private final CompetenzaDao serviceC = new CompetenzaDao();
	private final CompetenzeOperatoreDao serviceCO = new CompetenzeOperatoreDao();
	private final AdminDao serviceA = new AdminDao();
	private final MissioneDao serviceM = new MissioneDao();

	// http://localhost:8080/soccorso_Web/opt?action=insert
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action == null) {
			action = "list";
		}

		switch (action) {
		case "list" -> operatorsList(request, response);
		case "listAd" -> adminList(request, response);
		case "details" -> operatorDetails(request, response);
		case "insert" -> insertOperator(request, response);
		case "insertAd" -> insertAdmin(request, response);
		default -> operatorsList(request, response);

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
	
	private void operatorsList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			
			Map<String, Object> dataMap = new HashMap<String, Object>();
			
			dataMap.put("contextPath", request.getContextPath());
			dataMap.put("opt", service.findAll());
			
			renderTemplate(request, response, "OperatorsList.html.ftl", dataMap);

	}
	
	private void adminList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		dataMap.put("contextPath", request.getContextPath());
		dataMap.put("adm", serviceA.findAll());
		
		renderTemplate(request, response, "AdminList.html.ftl", dataMap);

	}

	private void operatorDetails(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idParam = request.getParameter("id");
		
		Map<String, Object> dataMap = new HashMap<>();

		if (idParam == null || idParam.isBlank()) {
			response.sendRedirect(request.getContextPath() + "/opt?action=list");
			return;
		}

		int id = Integer.parseInt(idParam);
		Operator op = service.findById(id);

		if (op == null) {
			response.sendRedirect(request.getContextPath() + "/opt?action=list");
			return;
		}

		dataMap.put("opt", op);
		dataMap.put("patOp", servicePO.findByOperatorID(id));
		dataMap.put("pat", serviceP.findAll());
		dataMap.put("comOp", serviceCO.findByOperatorID(id));
		dataMap.put("com", serviceC.findAll());
		dataMap.put("att", serviceM.findByOperatoreId(id));
		dataMap.put("sto", serviceM.storicoByOperatoreId(id));
		dataMap.put("contextPath", request.getContextPath());
		
		renderTemplate(request, response, "OperatorDetails.html.ftl", dataMap);
		
	}

	private void insertOperator(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		Map<String, Object> dataMap = new HashMap<>();
		
		dataMap.put("contextPath", request.getContextPath());
		
		renderTemplate(request, response, "NewOperator.html.ftl", dataMap);
		

	}
	
	private void insertAdmin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		Map<String, Object> dataMap = new HashMap<>();
		
		dataMap.put("contextPath", request.getContextPath());
		
		renderTemplate(request, response, "NewAdmin.html.ftl", dataMap);

	}

	private void addPatentiToOperator(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int operatorId = Integer.parseInt(request.getParameter("operatorId"));

		String[] patentiIds = request.getParameterValues("patenti");

		if (patentiIds != null) {
			for (String patenteIdStr : patentiIds) {

				int patenteId = Integer.parseInt(patenteIdStr);

				PatenteOperatore po = new PatenteOperatore();
				po.setOperatoreRif(operatorId);
				po.setPatenteRif(patenteId);

				servicePO.insert(po);
			}
		}

		response.sendRedirect(request.getContextPath() + "/opt?action=details&id=" + operatorId);
	}
	
	private void addCompetenzeToOperator(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int operatorId = Integer.parseInt(request.getParameter("operatorId"));
		
		String[] competenzeIds = request.getParameterValues("competenze");
		if (competenzeIds != null) {
			for (String patenteIcompetenzeIdStr : competenzeIds) {

				int competenzaId = Integer.parseInt(patenteIcompetenzeIdStr);

				CompetenzeOperatore co = new CompetenzeOperatore();
				co.setOperatoreRif(operatorId);
				co.setAbilitaRif(competenzaId);

				serviceCO.insert(co);
			}
		}

		response.sendRedirect(request.getContextPath() + "/opt?action=details&id=" + operatorId);
	}

	private void removePatenteFromOperator(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int operatorId = Integer.parseInt(request.getParameter("operatorId"));
		int patenteId = Integer.parseInt(request.getParameter("patenteId"));

		servicePO.deleteByIDCombination(operatorId, patenteId);

		response.sendRedirect(request.getContextPath() + "/opt?action=details&id=" + operatorId);
	}
	
	private void removeCompetenzaFromOperator(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int operatorId = Integer.parseInt(request.getParameter("operatorId"));
		int competenzaId = Integer.parseInt(request.getParameter("competenzaId"));
		
		serviceCO.deleteByIDCombination(operatorId, competenzaId);

		response.sendRedirect(request.getContextPath() + "/opt?action=details&id=" + operatorId);
	}
	
	private void addOperatore(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nam = request.getParameter("operator-name");
		String sur = request.getParameter("operator-surname");
		String mail = request.getParameter("operator-email");
		String birthst = request.getParameter("operator-birthday");
		String passkey = request.getParameter("operator-passkey");
		LocalDate birth = LocalDate.parse(birthst);

		Operator optr = new Operator(nam, sur, mail, birth, passkey);

		if (service.insert(optr)) {
			response.sendRedirect(request.getContextPath() + "/opt?action=list");
		} else {
			response.sendRedirect("Error.html");
		}
		
	}
	
	private void addAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nam = request.getParameter("admin-name");
		String mail = request.getParameter("admin-email");
		String passkey = request.getParameter("admin-passkey");
		
		Admin adm = new Admin(nam, mail, passkey);
		
		if (serviceA.insert(adm)) {
			response.sendRedirect(request.getContextPath() + "/opt?action=listAd");
		} else {
			response.sendRedirect("Error.html");
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if ("addPatenti".equals(action)) {
			addPatentiToOperator(request, response);
			return;
		}

		if ("removePatente".equals(action)) {
			removePatenteFromOperator(request, response);
			return;
		}
		
		if ("addCompetenze".equals(action)) {
		    addCompetenzeToOperator(request, response);
		    return;
		}

		if ("removeCompetenza".equals(action)) {
		    removeCompetenzaFromOperator(request, response);
		    return;
		}
		
		if ("insert".equals(action)) {
			addOperatore(request, response);
			return;
		}
		
		if ("insertA".equals(action)) {
			addAdmin(request, response);
			return;
		}

	}

}
