package eva.luca.soccorso_Web.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import eva.luca.soccorso_Web.data.AuthDao;
import eva.luca.soccorso_Web.models.LoggedUser;
import eva.luca.soccorso_Web.security.SecurityLayer;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@WebServlet("/auth")
public class AuthController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final AuthDao serviceA = new AuthDao();
       


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request. getParameter("action");
		
		if (action.equals(null)) {
			action = "login";
		}
		
		switch (action) {
		case "login" -> loginPage(request, response);
		case "logout" -> logout(request, response);
		default -> loginPage(request, response);

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
	
	protected void loginPage(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		if(SecurityLayer.checkSession(request) != null) {
			response.sendRedirect(request.getContextPath() + "/rqs?action=list");
			return;
		}
		
		dataMap.put("contextPath", request.getContextPath());
		
		if(request.getParameter("error") != null) {
			dataMap.put("error", "Email o password non corretti");
		}
		
		renderTemplate(request, response, "Login.ftl", dataMap);
		
	}
	
	protected void logout(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		SecurityLayer.disposeSession(request);
		
		response.sendRedirect(request.getContextPath() + "/rqs?action=insert");
	}
	
	protected void login(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String passkey = request.getParameter("passkey");
		
		if(email == null || email.isBlank()
				|| passkey == null || passkey.isBlank()) {
			response.sendRedirect(request.getContextPath() + "/auth?action=login&error=1");
			return;
		}
		
		LoggedUser user = serviceA.login(email, passkey);
		
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/auth?action=login&error=1");
			return;
		}
		
		SecurityLayer.createSession(request, user.getNome(), user.getId(),
				user.getEmail(), user.getRuolo());
		
		if("admin".equals(user.getRuolo())) {
			response.sendRedirect(request.getContextPath() + "/dshb?action=home");

		} else {
			response.sendRedirect(request.getContextPath() + "/opt?action=details&id=" + user.getId());
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		String action = request.getParameter("action");
		
		if("login".equals(action)) {
			login(request, response);
			return;
		}
		
		response.sendRedirect(request.getContextPath() + "/auth?action=login");
	}

}
