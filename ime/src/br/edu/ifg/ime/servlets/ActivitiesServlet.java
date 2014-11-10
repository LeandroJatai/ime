package br.edu.ifg.ime.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imsglobal.jaxb.ld.ActivityStructure;
import org.imsglobal.jaxb.ld.LearningActivity;
import org.imsglobal.jaxb.ld.SupportActivity;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.controllers.ActivityController;
import br.edu.ifg.ime.controllers.Lc;
import br.edu.ifg.ime.controllers.ProjetoController;
import br.edu.ifg.ime.controllers.RolesController;
import br.edu.ifg.ime.controllers.UsuarioController;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.suport.Urls;

/**
 * Servlet implementation class ActivitiesServlet
 */
public class ActivitiesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ActivitiesServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String action = Suport.r(request, "action");
		String ldep = Suport.r(request, "ldep");
		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);


		if (action == null)
			action = "activities.list";

		if (!UsuarioController.validateConnection(request, response, action))
			return;

		if (action.startsWith("activities.list")) {

			Urls.forward(request, response, null, "views/componentes/activities/activities.list.jsp", null); //
		}

		else if (action.startsWith("activities.remove")) {
			w.oldState(ldep);

			ActivityController.requestRemoveActivities(request);

			Urls.forward(request, response, null, "views/componentes/activities/activities.list.jsp", null); //
		}

		else if (action.startsWith("activities.edit")) {

			if (action.equals("activities.edit")) {
				Urls.forward(request, response, null, "views/componentes/activities/activities.edit.jsp", null); //
			}

			else if (action.equals("activities.edit.la")) {
				Urls.forward(request, response, null, "views/componentes/activities/la.edit.jsp", null); //
			}

			else if (action.equals("activities.edit.sa")) {
				Urls.forward(request, response, null, "views/componentes/activities/sa.edit.jsp", null); //
			}

			else if (action.equals("activities.edit.as")) {
				Urls.forward(request, response, null, "views/componentes/activities/as.edit.jsp", null); //
			}


			return;
		}
		else if (action.startsWith("activities.save")) {
			w.oldState(ldep);
			if (action.equals("activities.save")) {
				String identifier = ActivityController.requestIncludeActivities(request);

				String tipo = Suport.r(request, "tipo");

				if (tipo.equals("activity-structure"))
					Urls.forwardAction(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "activities.do?action=activities.edit.as&ldep="+ldep+"&identifier="+identifier); 				
				else if (tipo.equals("learning-activity")) 
					Urls.forwardAction(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "activities.do?action=activities.edit.la&ldep="+ldep+"&identifier="+identifier); 			
				else if (tipo.equals("support-activity"))		
					Urls.forwardAction(request, response, Lc.getTexto(request, "at.ldep.msg.save"),  "activities.do?action=activities.edit.sa&ldep="+ldep+"&identifier="+identifier); 		
				else
					Urls.forwardAction(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "views/componentes/activities/activities.edit.jsp");

			}
			else if (action.equals("activities.save.la")) {
				ActivityController.requestUpDateLearningActivities(request);
				Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "views/componentes/activities/la.edit.jsp", null); //
			}
			else if (action.equals("activities.save.sa")) {
				ActivityController.requestUpDateSupportActivity(request);
				Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "views/componentes/activities/sa.edit.jsp", null); //
			}

			else if (action.equals("activities.save.as")) {
				ActivityController.requestUpDateActivityStructure(request);
				Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "views/componentes/activities/as.edit.jsp", null); //
			}

			return;
		}

	}

}
