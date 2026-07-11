package eva.luca.soccorso_Web.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//pubblica solo la creazione della richiesta di soccorso
@WebFilter(urlPatterns = {/* "/opt", "/sqd", "/mss", "/ogm", "/rqs", "/dshb" */})
public class AuthFilter extends HttpFilter implements Filter {
       

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		
		String servletPath = httpRequest.getServletPath();
		String action = httpRequest.getParameter("action");
		
		if(action == null) {
			action = "";
		}
		
		
		//controllo sessione esistente
		HttpSession session = SecurityLayer.checkSession(httpRequest);
		
		if(session ==null) {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth?action=login");
			return;
		}
		
		String ruolo = (String)session.getAttribute("ruolo");
		int id = (Integer)session.getAttribute("userid");
		
		//admin può accedere liberamente
		if("admin".equals(ruolo)) {
			chain.doFilter(httpRequest, httpResponse);
			return;
		}
		
		//operatore ha accessi limitati
		if("operator".equals(ruolo)) {
			if(isOperatorAllowed(servletPath, action)) {
				chain.doFilter(httpRequest, httpResponse);
				return;
			}
			
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/opt?action=details&id=" + id);
			return;
		}
		
		//ruolo non riconosciuto
		httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth?action=login");
	}
	
	protected boolean isOperatorAllowed(String servletPath, String action) {
		
		if("/opt".equals(servletPath)) {
			return "details".equals(action)
		            || "addPatenti".equals(action)
		            || "removePatente".equals(action)
		            || "addCompetenze".equals(action)
		            || "removeCompetenza".equals(action);
		}
		
		if("/mss".equals(servletPath)) {
			return "details".equals(action) 
					|| "addComunicazione".equals(action);
		}
		
		return false;
	}


}
