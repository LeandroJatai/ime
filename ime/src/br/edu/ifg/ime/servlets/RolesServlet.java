package br.edu.ifg.ime.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.controllers.Lc;
import br.edu.ifg.ime.controllers.ProjetoController;
import br.edu.ifg.ime.controllers.RolesController;
import br.edu.ifg.ime.controllers.UsuarioController;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.suport.Urls;

/**
 * Servlet implementation class RolesServlet
 */
public class RolesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RolesServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		String action = Suport.r(request, "action");
		String ldep = Suport.r(request, "ldep");
		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
		
		
		if (action == null)
			action = "roles.list";
		
		if (!UsuarioController.validateConnection(request, response, action))
			return;
		
		if (action.equals("roles.list")) {
			
			Urls.forward(request, response, null, "views/componentes/roles/roles.list.jsp", null); 
			return;
		}
		else if (action.equals("roles.remove")) {
			
			w.oldState(ldep);
			
			RolesController.requestRemoveRole(request);
			
			Urls.forward(request, response, null, "views/componentes/roles/roles.list.jsp", null); //RolesServlet
			return;
		}
		else if (action.startsWith("roles.edit")) {
			
			Urls.forward(request, response, null, "views/componentes/roles/roles.edit.jsp", null); //RolesServlet
			return;
		}
		else if (action.equals("roles.save")) {
			w.oldState(ldep);
			
			String identifier = RolesController.requestUpDateRole(request);			
			
			Urls.forward(request, response,  Lc.getTexto(request, "at.ldep.msg.save"), "views/componentes/roles/roles.edit.jsp?identifier="+identifier, null); //RolesServlet
			return;
		}
		
		
	}

}
