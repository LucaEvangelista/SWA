package eva.luca.soccorso_Web.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import eva.luca.soccorso_Web.data.RequestDao;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@WebServlet("/con")
public class ConvalidaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final RequestDao serviceR = new RequestDao();


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("contextPath", request.getContextPath());
		
		String uuid = request.getParameter("uuid");
		if(serviceR.findByUuid(uuid) == null) {
	        

	        dataMap.put("error", "richiesta non trovata");

	        renderTemplate(request, response, "NewRequest_home.ftl", dataMap);
	        return;
		}
		
		if(!serviceR.updateToAttiva(uuid)) {
	        dataMap.put("error", "richiesta già evasa");

	        renderTemplate(request, response, "NewRequest_home.ftl", dataMap);
	        return;
		}
        
        dataMap.put("success", "richiesta attivata");

        renderTemplate(request, response, "NewRequest_home.ftl", dataMap);
	}
	
	private void renderTemplate(HttpServletRequest request, HttpServletResponse response, String templateName,
			Map<String, Object> dataModel) throws IOException, ServletException {

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);

		cfg.setServletContextForTemplateLoading(getServletContext(), "/WEB-INF/views");

		cfg.setDefaultEncoding("UTF-8");

		response.setContentType("text/html;charset=UTF-8");

		try {
			Template template = cfg.getTemplate(templateName);
			template.process(dataModel, response.getWriter());
		} catch (TemplateException e) {
			throw new ServletException("Errore rendering FreeMarker", e);
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
