package br.edu.ifg.ime.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imsglobal.jaxb.ld.Act;
import org.imsglobal.jaxb.ld.Learner;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.controllers.ActsController;
import br.edu.ifg.ime.controllers.EnvironmentsController;
import br.edu.ifg.ime.controllers.Lc;
import br.edu.ifg.ime.controllers.PlaysController;
import br.edu.ifg.ime.controllers.RolePartController;
import br.edu.ifg.ime.controllers.UsuarioController;
import br.edu.ifg.ime.suport.LearningDesignUtils;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.suport.Urls;

/**
 * Servlet implementation class PlaysServlet
 */
public class ActsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ActsServlet() {
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
			action = "act.list";

		if (!UsuarioController.validateConnection(request, response, action))
			return;

		if (action.equals("act.list")) {

			Urls.forward(request, response, null, "views/methods/acts/acts.list.jsp", null); 
			return;
		}


		else if (action.startsWith("act.edit")) {

			Urls.forward(request, response, null, "views/methods/acts/acts.edit.jsp", null); 
			return;
		}


		else if (action.equals("act.save")) {
			w.oldState(ldep);

			String identifier = ActsController.requestUpDateAct(request);	

			Object ob = ImeWorkspace.getImeWorkspace(request).getObject(identifier);

			if (ob instanceof Act)	
				Urls.forward(request, response, null, "views/methods/acts/acts.edit.jsp?identifier="+identifier);
			else {

				if (request.getParameter("comp-mi") != null && UsuarioController.checkPermissao(request, "ldep.comp.mi.roles.config"))
					Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"),  "views/ldep/ldep.comp.mi.roles.config.jsp?ldepAgr="+identifier);
				else
					Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"),  "views/ldep/ldep.edit.jsp");
				 
			}
			return;
		}

		else if (action.equals("act.remove")) {
			w.oldState(ldep);

			ActsController.requestRemoveAct(request);		
			//Urls.forward(request, response, null, "views/usuarios/usuarios_login.jsp");
			
			String playRef = Suport.r(request, "play-ref");
			Urls.forward(request, response, null, "views/methods/plays/play.edit.jsp?ldep"+ldep+"&identifier="+playRef); 
			return;
		}

		else if (action.equals("act.remove.agregado")) {
			w.oldState(ldep);
			ActsController.requestRemoveLdepOfAct(request);		
			//Urls.forward(request, response, null, "views/usuarios/usuarios_login.jsp");

			String playRef = Suport.r(request, "play-ref");
			Urls.forward(request, response, null, "views/methods/plays/play.edit.jsp?ldep"+ldep+"&identifier="+playRef); 
			return;
		}


		else if (action.equals("act.new.for.mi")) {

			Urls.forward(request, response, null, "views/methods/acts/acts.new.for.mi.jsp", null); 
			return;
		}

		else if (action.equals("act.switch.pos.rp")) {

			String [] params = Suport.r(request, "item").split("-");

			ldep = params[7];
			w.oldStateById(ldep);			
			ActsController.switchPosRps(w, params, Suport.r(request, "start"), Suport.r(request, "stop"));

			response.getOutputStream().print("OK");
		}


	}

}
