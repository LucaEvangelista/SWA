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

import eva.luca.soccorso_Web.data.MaterialeDao;
import eva.luca.soccorso_Web.data.MezzoDao;
import eva.luca.soccorso_Web.data.MissioneDao;
import eva.luca.soccorso_Web.models.Materiale;
import eva.luca.soccorso_Web.models.Mezzo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@WebServlet("/ogm")
public class OggettiMissioniControlle extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final MezzoDao serviceMZ = new MezzoDao();
	private final MaterialeDao serviceMA = new MaterialeDao();
	private final MissioneDao serviceMS = new MissioneDao();
       


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");

		if (action == null) {
			action = "list";
		}

		switch (action) {
		case "list" -> mezziList(request, response);
		case "insertMz" -> newMezzo(request, response);
		case "insertMa" -> newMateriale(request, response);
		case "detailsMa" -> detailsMateriale(request, response);
		case "detailsMz" -> detailsMezzo(request, response);
		default -> mezziList(request, response);

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
	
	protected void mezziList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		dataMap.put("contextPath", request.getContextPath());
		dataMap.put("mez", serviceMZ.findAll());
		dataMap.put("mat", serviceMA.findAll());
		
		renderTemplate(request, response, "ListaOggetti.ftl", dataMap);
	}
	
	
	protected void newMezzo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("contextPath", request.getContextPath());
		
		renderTemplate(request, response, "NewMezzo.ftl", dataMap);
	}
	
	protected void newMateriale(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("contextPath", request.getContextPath());
		
		renderTemplate(request, response, "NewMateriale.ftl", dataMap);
	}
	
	
	protected void insertMateriale(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	    String nome = request.getParameter("nome-materiale");
	    String seriale = request.getParameter("seriale-materiale");

	    Materiale materiale = new Materiale();

	    materiale.setTipologia(nome);
	    materiale.setSeriale(seriale);

	    if (serviceMA.insert(materiale)) {
	        response.sendRedirect(request.getContextPath() + "/ogm?action=list");
	    } else {
	        response.sendRedirect("Error.html");
	    }
	}
	
	protected void insertMezzo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	    String nome = request.getParameter("nome-mezzo");
	    String seriale = request.getParameter("seriale-mezzo");

	    Mezzo mezzo = new Mezzo();

	    mezzo.setTipologia(nome);
	    mezzo.setSeriale(seriale);

	    if (serviceMZ.insert(mezzo)) {
	        response.sendRedirect(request.getContextPath() + "/ogm?action=list");
	    } else {
	        response.sendRedirect("Error.html");
	    }
	}
	
	private void detailsMezzo(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Map<String, Object> dataMap = new HashMap<>();
		
		String idParam = request.getParameter("id");
		int id = Integer.parseInt(idParam);
		
		Mezzo mz = serviceMZ.findById(id);
		
		dataMap.put("mez", mz);
		dataMap.put("attivaMZ", serviceMS.findByMezzoId(id));
		dataMap.put("storicoMZ", serviceMS.storicoByMezzoId(id));
		dataMap.put("contextPath", request.getContextPath());
		
		renderTemplate(request, response, "MezzoDetails.html.ftl", dataMap);
	}
	
	private void detailsMateriale(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Map<String, Object> dataMap = new HashMap<>();
		
		String idParam = request.getParameter("id");
		int id = Integer.parseInt(idParam);
		
		Materiale ma = serviceMA.findById(id);
		
		dataMap.put("mat", ma);
		dataMap.put("attivaMA", serviceMS.findByMaterialeId(id));
		dataMap.put("storicoMA", serviceMS.storicoByMaterialeId(id));
		dataMap.put("contextPath", request.getContextPath());
		
		renderTemplate(request, response, "MaterialeDetails.html.ftl", dataMap);
	}
	


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		if ("instMz".equals(action)) {
			insertMezzo(request, response);
			return;
		}
		
		if ("instMa".equals(action)) {
			insertMateriale(request, response);
			return;
		}

		
	}

}
