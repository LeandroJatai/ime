package br.edu.ifg.ime.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.controllers.CompletesController;
import br.edu.ifg.ime.controllers.ItemModelController;
import br.edu.ifg.ime.controllers.Lc;
import br.edu.ifg.ime.controllers.ProjetoController;
import br.edu.ifg.ime.controllers.UsuarioController;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.suport.Urls;

/**
 * Servlet implementation class CompleteServlet
 */
public class CompleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CompleteServlet() {
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
			throw new ServletException();

		if (!UsuarioController.validateConnection(request, response, action))
			return;

		if (action.equals("complete.activity.save")) {		
			
			w.oldState(ldep);
			
			CompletesController.requestUpDateCompleteActivities(request);			
			Urls.forward(request, response,  Lc.getTexto(request, "at.ldep.msg.save"), ProjetoController.getViewsReturn(request), null); 
		}
		else if (action.equals("complete.act.save")) {		
			
			w.oldState(ldep);
			
			CompletesController.requestUpDateCompleteActs(request);			
			Urls.forward(request, response,  Lc.getTexto(request, "at.ldep.msg.save"), ProjetoController.getViewsReturn(request), null); 
		}
		else if (action.equals("complete.play.save")) {		
			
			w.oldState(ldep);
			
			CompletesController.requestUpDateCompletePlays(request);			
			Urls.forward(request, response,  Lc.getTexto(request, "at.ldep.msg.save"), ProjetoController.getViewsReturn(request), null); 
		}		
		else if (action.equals("complete.ldep.save")) {		
			
			w.oldState(ldep);
			
			CompletesController.requestUpDateCompleteUols(request);			
			Urls.forward(request, response,  Lc.getTexto(request, "at.ldep.msg.save"), ProjetoController.getViewsReturn(request), null); 
		}
	}

}
