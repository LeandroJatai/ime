package br.edu.ifg.ime.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.controllers.ActivityController;
import br.edu.ifg.ime.controllers.EnvironmentsController;
import br.edu.ifg.ime.controllers.Lc;
import br.edu.ifg.ime.controllers.PlaysController;
import br.edu.ifg.ime.controllers.UsuarioController;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.suport.Urls;

/**
 * Servlet implementation class PlaysServlet
 */
public class PlaysServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PlaysServlet() {
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
			action = "play.list";
		
		if (!UsuarioController.validateConnection(request, response, action))
			return;

		if (action.equals("play.list")) {

			Urls.forward(request, response, null, "views/methods/plays/play.list.jsp"); 
			return;
		}


		else if (action.startsWith("play.edit")) {

			Urls.forward(request, response, null, "views/methods/plays/play.edit.jsp"); 
			return;
		}


		else if (action.equals("play.switch.pos.play")) {

			String [] params = Suport.r(request, "item").split("-");

			ldep = params[3];
			w.oldStateById(ldep);			
			PlaysController.switchPosPlays(w, params, Suport.r(request, "start"), Suport.r(request, "stop"));

			response.getOutputStream().print("OK");
		}

		else if (action.equals("play.switch.pos.act")) {

			String [] params = Suport.r(request, "item").split("-");

			ldep = params[5];
			w.oldStateById(ldep);			
			PlaysController.switchPosActs(w, params, Suport.r(request, "start"), Suport.r(request, "stop"));

			response.getOutputStream().print("OK");
		}

		else if (action.startsWith("play.remove")) {
			w.oldState(ldep);			
			PlaysController.requestRemovePlay(request);

			Urls.forward(request, response, null, "views/ldep/ldep.edit.jsp"); //
		}

		else if (action.equals("play.save")) {
			w.oldState(ldep);

			String identifier = PlaysController.requestUpDatePlay(request);			

			Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "views/methods/plays/play.edit.jsp?identifier="+identifier); 
			return;
		}



	}

}
