package br.edu.ifg.ime.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.controllers.ActsController;
import br.edu.ifg.ime.controllers.EnvironmentsController;
import br.edu.ifg.ime.controllers.Lc;
import br.edu.ifg.ime.controllers.PlaysController;
import br.edu.ifg.ime.controllers.RolePartController;
import br.edu.ifg.ime.controllers.RolesController;
import br.edu.ifg.ime.controllers.UsuarioController;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.suport.Urls;

/**
 * Servlet implementation class PlaysServlet
 */
public class RolePartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RolePartServlet() {
        super();
        
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		

		String action = Suport.r(request, "action");
		String ldep = Suport.r(request, "ldep");
		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
		
		if (action == null)
			action = "rp.list";

		if (!UsuarioController.validateConnection(request, response, action))
			return;

		if (action.equals("rp.list")) {
			Urls.forward(request, response, null, "views/methods/rp/rp.list.jsp", null); 
			return;
		}
		else if (action.startsWith("rp.edit")) {
			Urls.forward(request, response, null, "views/methods/rp/rp.edit.jsp", null); 
			return;
		}
		else if (action.equals("rp.save")) {
			w.oldState(ldep);
			String identifier = RolePartController.requestUpDateRolePart(request);			
			
			
			
			
			Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "views/methods/rp/rp.edit.jsp?identifier="+identifier, null); 
			return;
		}
		else if (action.equals("rp.remove")) {
			w.oldState(ldep);

			RolePartController.requestRemoveRolePart(request);		

			String identifier = Suport.r(request, "act-ref");
			String playRef = Suport.r(request, "play-ref");
			
			//Urls.forward(request, response, null, "views/usuarios/usuarios_login.jsp");
			Urls.forward(request, response, null, "views/methods/acts/acts.edit.jsp?ldep="+ldep+"&play-ref="+playRef+"&identifier="+identifier, null); 
				return;
		}
		
		
	}

}
