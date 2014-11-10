package br.edu.ifg.ime.suport;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifg.ime.dao.DAOConnection;

public class Urls {


	
    //public static final String urlAppBase = DAOConnection.dev?"/ime/":"/";
  	//public static final String urlAppForms = DAOConnection.dev?"/ime/forms/":"/forms/";

	
    public static final String urlAppBase = "/ime/";
  	public static final String urlAppForms = "/ime/forms/";

  	public static final String urlProjectAppBase = "/";

  	public static final String url_av_Servlet = urlAppBase+"av";
	
  	
  	//public static final String urlProjectAppForms = "/forms/";	


  	// Redireciona para uma View inserindo no main_template
	public static void forward(HttpServletRequest request,
			HttpServletResponse response, String msg, String view) throws ServletException, IOException {
				forward(request, response, msg, view, null);
			}

	// Redireciona para uma View inserindo no template
	public static void forward(HttpServletRequest request,
			HttpServletResponse response, String msg, String view, String template) throws ServletException, IOException {
		RequestDispatcher rd;

		if (template == null)
			template = "main_template";
		
		rd = request.getRequestDispatcher(Urls.urlProjectAppBase+template+".jsp");

		if (msg != null)
			request.setAttribute("msg", msg);

		request.setAttribute("jspInclude", Urls.urlProjectAppBase+view);

		rd.forward(request, response);
	}

	// Redireciona para uma View inserindo no main_tamplate_ajax
	public static void forwardAjax(HttpServletRequest request,
			HttpServletResponse response, String msg, String view) throws ServletException, IOException {
		RequestDispatcher rd;

		rd = request.getRequestDispatcher(Urls.urlProjectAppBase+"main_template_ajax.jsp");

		if (msg != null)
			request.setAttribute("msg", msg);

		request.setAttribute("jspInclude", Urls.urlProjectAppBase+view);

		rd.forward(request, response);
	}

	//Redireciona internamente para um servlet ou view
	public static void forwardAction(HttpServletRequest request,
			HttpServletResponse response, String msg, String view) throws ServletException, IOException {
		RequestDispatcher rd;

		if (msg != null)
			request.setAttribute("msg", msg);

		rd = request.getRequestDispatcher(Urls.urlProjectAppBase+view); 
		rd.forward(request, response);

		//response.sendRedirect(Urls.urlCifrasBase+view);
	}	
	
	//Redireciona externamente para um servlet ou jsp - sempre converter parametros de utf para iso
	public static void redirectAction(HttpServletRequest request,
			HttpServletResponse response, String view) throws ServletException, IOException {
		/*RequestDispatcher rd;

		if (msg != null)
			request.setAttribute("msg", msg);

		rd = request.getRequestDispatcher(Urls.urlProjectCifrasBase+view); 
		rd.forward(request, response);
*/
		response.sendRedirect(Urls.urlAppBase+view);
	}	
}
