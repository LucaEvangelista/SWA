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

import eva.luca.soccorso_Web.data.ComunicazioneDao;
import eva.luca.soccorso_Web.data.MaterialeDao;
import eva.luca.soccorso_Web.data.MaterialiMissioneDao;
import eva.luca.soccorso_Web.data.MezziMissioneDao;
import eva.luca.soccorso_Web.data.MezzoDao;
import eva.luca.soccorso_Web.data.MissioneDao;
import eva.luca.soccorso_Web.data.OperatorDao;
import eva.luca.soccorso_Web.data.RequestDao;
import eva.luca.soccorso_Web.data.SquadraDao;
import eva.luca.soccorso_Web.models.Comunicazione;
import eva.luca.soccorso_Web.models.MaterialiMissione;
import eva.luca.soccorso_Web.models.MezziMissione;
import eva.luca.soccorso_Web.models.Missione;
import eva.luca.soccorso_Web.models.Request;
import eva.luca.soccorso_Web.models.Squadra;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@WebServlet("/mss")
public class MissionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final RequestDao serviceR = new RequestDao();
	private final SquadraDao serviceS = new SquadraDao();
	private final MezzoDao serviceMZ = new MezzoDao();
	private final MaterialeDao serviceMA = new MaterialeDao();
	private final MissioneDao serviceMS = new MissioneDao();
	private final MezziMissioneDao serviceMZM = new MezziMissioneDao();
	private final MaterialiMissioneDao serviceMTM = new MaterialiMissioneDao();
	private final ComunicazioneDao serviceC = new ComunicazioneDao();
	private final OperatorDao serviceO = new OperatorDao();



	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String  action = request.getParameter("action");
		
		if (action.equals(null)) {
			action = "list";
		}
		
		switch (action) {
		case "list" -> missionList(request, response);
		case "details" -> missionDetails(request, response);
		default -> missionList(request, response);

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
	

	protected void missionList(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		dataMap.put("contextPath", request.getContextPath());
		dataMap.put("mis", serviceMS.findAll());
		
		renderTemplate(request, response, "MissionsList.ftl", dataMap);
		
		
		
	}
	
	protected void missionDetails(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		String idParam = request.getParameter("id");
		
		if(idParam == null || idParam.isBlank()){
			response.sendRedirect(request.getContextPath() + "/rqs?action=list");
			return;
		}
		
		int id = Integer.parseInt(idParam);
		
		Missione msn = serviceMS.findById(id);
		
		Squadra sqd = serviceS.findByMissioneId(id);
		
		if(msn == null){
			response.sendRedirect(request.getContextPath() + "/rqs?action=list");
			return;
		}
		
		
		dataMap.put("contextPath", request.getContextPath());
		dataMap.put("mission", msn);
		dataMap.put("squad", sqd);
		dataMap.put("comunicazioni", serviceC.findByMissionId(id));
		
		renderTemplate(request, response, "MissionDetails.ftl", dataMap);
		
	}
	
	protected void missionCreation(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		String idParam = request.getParameter("richiestaId");
		
		if (idParam == null || idParam.isBlank()) {
			response.sendRedirect(request.getContextPath() + "/mss?action=list");
			return;
		}
		
		int id = Integer.parseInt(idParam);
		Request rq = serviceR.findById(id);
		
		if (rq == null) {
			response.sendRedirect(request.getContextPath() + "/mss?action=list");
			return;
		}
		
		dataMap.put("contextPath", request.getContextPath());
		dataMap.put("rqst", rq);
		dataMap.put("squadre", serviceS.findSquadreNonOperative());
		dataMap.put("mezzi", serviceMZ.findAllLiberi());
		dataMap.put("materiali", serviceMA.findAllLiberi());
		
		renderTemplate(request, response, "NewMission.ftl", dataMap);
		
	}
	
	protected void saveMission(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		int richiestaId = Integer.parseInt(request.getParameter("richiestaId"));
		String obiettivo = request.getParameter("obiettivo");
		String posizione = request.getParameter("posizione");
		
		int squadraId = Integer.parseInt(request.getParameter("squadraId"));
		
		String[] mezziId = request.getParameterValues("mezzi");
		String[] materialiId = request.getParameterValues("materiali");
		
		Missione mis = new Missione();
		
		mis.setRichiestaRif(richiestaId);
		mis.setObiettivo(obiettivo);
		mis.setPosizione(posizione);
		
		mis.setSquadraRif(squadraId);
		
		int missionId = serviceMS.insertAndReturnID(mis);
		
	    if (missionId <= 0) {
	        response.sendRedirect(request.getContextPath() + "/mss?action=list");
	        return;
	    }
	    
	    if (mezziId != null) {
	        for (String mezzoId : mezziId) {
	            MezziMissione mzM = new MezziMissione();

	            mzM.setMezzoRif(Integer.parseInt(mezzoId));
	            mzM.setMissioneRif(missionId);

	            serviceMZM.insert(mzM);
	        }
	    }
	    
	    if (materialiId != null) {
	        for (String materialeId : materialiId) {
	            MaterialiMissione mtM = new MaterialiMissione();

	            mtM.setMaterialeRif(Integer.parseInt(materialeId));
	            mtM.setMissioneRif(missionId);

	            serviceMTM.insert(mtM);
	        }
	    }
	    
	    serviceMA.updateStatusByMissioneId(missionId, "occupato");
	    serviceMZ.updateStatusByMissioneId(missionId, "occupato");
	    serviceO.updateStatusBySquadraId(squadraId, "occupato");
	    serviceR.updateToEsecuzione(richiestaId);
	    
	    response.sendRedirect(request.getContextPath() + "/mss?action=list");
	}
	
	protected void addComunicazioni(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		int missionId = Integer.parseInt(request.getParameter("missionId"));
		
		HttpSession session = request.getSession(false);
		String ruolo = (String) session.getAttribute("ruolo");
		
		String contenuto = request.getParameter("com-contenuto");
		
		Comunicazione com = new Comunicazione();
		
	    com.setMissioneRif(missionId);
	    com.setContenuto(contenuto);
	    com.setRuolo(ruolo);
	    
	    serviceC.insert(com);
	    
	    response.sendRedirect(request.getContextPath() + "/mss?action=details&id=" + missionId);
	}
	
	private void terminaMissione(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		int missionId = Integer.parseInt(request.getParameter("missionId"));
		int squadraId = Integer.parseInt(request.getParameter("squadraId"));
		int richiestaId = Integer.parseInt(request.getParameter("richiestaId"));
		
	    serviceMA.updateStatusByMissioneId(missionId, "libero");
	    serviceMZ.updateStatusByMissioneId(missionId, "libero");
	    serviceO.updateStatusBySquadraId(squadraId, "libero");
	    serviceR.updateToterminata(richiestaId);
	    serviceMS.updateToTerminata(missionId);
	    
	    response.sendRedirect(request.getContextPath() + "/mss?action=list");
		
	}
	
	private void dettagliMissione(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		

		int richiestaId = Integer.parseInt(request.getParameter("richiestaId"));
		
		Missione mis = serviceMS.findByRequestID(richiestaId);

		
		response.sendRedirect(request.getContextPath() + "/mss?action=details&id=" + mis.getMissioneId());
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		
		if("creazioneMissione".equals(action)) {
			missionCreation(request, response);
			return;
		}
		
		if("saveMission".equals(action)) {
			saveMission(request, response);
			return;
		}
		
		if("addComunicazione".equals(action)) {
			addComunicazioni(request, response);
			return;
		}
		
		if("terminaMissione".equals(action)) {
			terminaMissione(request, response);
			return;
		}
		
		if("dettagliMissione".equals(action)) {
			dettagliMissione(request, response);
			return;
		}
		
	}

} 
