package br.edu.ifg.ime.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.TreeMap;

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
import br.edu.ifg.ime.controllers.EnvironmentsController;
import br.edu.ifg.ime.controllers.ItemModelController;
import br.edu.ifg.ime.controllers.Lc;
import br.edu.ifg.ime.controllers.ProjetoController;
import br.edu.ifg.ime.controllers.RolesController;
import br.edu.ifg.ime.controllers.UsuarioController;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.suport.Urls;

/**
 * Servlet implementation class RolesServlet
 */
public class EnvironmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EnvironmentServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String action = Suport.r(request, "action");
		String ldep = Suport.r(request, "ldep");
		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);

		if (action == null) {

			boolean isMultipart = ServletFileUpload.isMultipartContent(request);

			if (!isMultipart) {
				Urls.forward(request, response, null, "views/componentes/environments/environment.list.jsp", null); 
				return;
			}

			List<DiskFileItem> items;
			try {

				items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

				for (DiskFileItem item : items) {
					if (item.isFormField()) {
						if (item.getFieldName().equals("action")) {
							action = item.getString();
						}
					}
				}

				if (!UsuarioController.validateConnection(request, response, action))
					return;

				TreeMap<String, Object> lParams = new TreeMap<String, Object>();

				InputStream filecontent = null;
				for (DiskFileItem item : items) {
					if (item.isFormField()) {

						// Process regular form field (input type="text|radio|checkbox|etc", select, etc).
						String fieldname = item.getFieldName();
						String fieldvalue = Suport.r(item.getString());

						lParams.put(fieldname, fieldvalue);

					} else {
						// Process form file field (input type="file").
						String fieldname = item.getFieldName();
						String filename = FilenameUtils.getName(item.getName());
						if (fieldname.equals("dadosItem")) {

							filecontent = item.getInputStream();

							TreeMap<String, Object> file = (TreeMap<String, Object>) lParams.get("dadosItem");

							if (file == null) {
								file = new TreeMap<String, Object>();
								lParams.put("dadosItem", file);
							}

							file.put("name", filename);
							file.put("data", Suport.toByteVector(filecontent));

						}
					}
				}
				if (action != null && action.equals("env.sv.save.item")) {

					w.oldState((String)lParams.get("ldep"));

					request.setAttribute("lParams", lParams);

					try {

						String identifier = EnvironmentsController.requestUpDateServiceItemConference(request,lParams);	
						Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "views/componentes/environments/sv.edit.jsp", null); 
					} catch (Exception e) {
						Urls.forward(request, response, e.getMessage(), "views/componentes/environments/sv.edit.jsp?identifier=", null);
					}

					return;


					//ItemModelController.requestUpDateItem(request, lParams);					
					//	Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"),ProjetoController.getViewsReturn(lParams, request), null);

				}

			} catch (FileUploadException e) {

				e.printStackTrace();
			}

			//Urls.forward(request, response, null, "views/usuarios/usuarios_login.jsp");
			//Urls.forward(request, response, null, "views/ldew/ldew.list.jsp");
			return;
		}	

		if (!UsuarioController.validateConnection(request, response, action))
			return;

		if (action.equals("environment.list")) {
			Urls.forward(request, response, null, "views/componentes/environments/environment.list.jsp", null); 
			return;
		}
		else if (action.equals("environment.remove")) {
			w.oldState(ldep);

			EnvironmentsController.requestRemoveEnvironment(request);

			Urls.forward(request, response, null, "views/componentes/environments/environment.list.jsp", null); 
			return;
		}		
		else if (action.startsWith("environment.edit")) {

			Urls.forward(request, response, null, "views/componentes/environments/environment.edit.jsp", null); 
			return;
		}
		else if (action.equals("environment.save")) {
			w.oldState(ldep);

			String identifier = EnvironmentsController.requestUpDateEnvironment(request);			

			Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "views/componentes/environments/environment.edit.jsp?identifier="+identifier, null); 
			return;
		}
		else if (action.equals("env.lo.remove")) {
			w.oldState(ldep);

			EnvironmentsController.requestRemoveLoServices(request);
			String idEnv = Suport.r(request, "id-env");
			Urls.forward(request, response, null, "views/componentes/environments/environment.edit.jsp?identifier="+idEnv, null); 
			return;
		}
		else if (action.equals("env.sv.remove")) {
			w.oldState(ldep);

			EnvironmentsController.requestRemoveLoServices(request);
			String idEnv = Suport.r(request, "id-env");
			Urls.forward(request, response, null, "views/componentes/environments/environment.edit.jsp?identifier="+idEnv, null); 
			return;
		}
		else if (action.startsWith("env.lo.edit")) {

			Urls.forward(request, response, null, "views/componentes/environments/lo.edit.jsp", null); 
			return;
		}		
		else if (action.startsWith("env.sv.edit")) {

			Urls.forward(request, response, null, "views/componentes/environments/sv.edit.jsp", null); 
			return;
		}		
		else if (action.startsWith("env.sv.save")) {
			w.oldState(ldep);
			try {

				String identifier = EnvironmentsController.requestUpDateService(request);	
				Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "views/componentes/environments/sv.edit.jsp?identifier="+identifier, null); 
			} catch (Exception e) {
				Urls.forward(request, response, e.getMessage(), "views/componentes/environments/sv.edit.jsp?identifier=", null);
			}

			return;
		}

	}

}
