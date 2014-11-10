package br.edu.ifg.ime.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import org.imsglobal.jaxb.content.Resource;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.controllers.ItemModelController;
import br.edu.ifg.ime.controllers.Lc;
import br.edu.ifg.ime.controllers.ProjetoController;
import br.edu.ifg.ime.controllers.UsuarioController;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.suport.Urls;

/**
 * Servlet implementation class ItemModelServlet
 */
public class ItemModelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ItemModelServlet() {
		super();

	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		RequestDispatcher rd;
		String action = Suport.r(request, "action");
		
		String ldep = Suport.r(request, "ldep");
		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);


		if (action == null) {

			boolean isMultipart = ServletFileUpload.isMultipartContent(request);

			if (!isMultipart) {
				Urls.forward(request, response, null, "/", null); 
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


				if (action != null && action.equals("im.item.save")) {

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

					w.oldState((String)lParams.get("ldep"));
					ItemModelController.requestUpDateItem(request, lParams);
					
					request.setAttribute("lParams", lParams);
					Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"),ProjetoController.getViewsReturn(lParams, request), null);

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
		/*
		if (action.equals("im.save")) {			
			
			w.oldState(ldep);
			
			ItemModelController.requestUpDateItemModel(request);			
			
			Urls.forward(request, response, "Alterações guardadas!", ProjetoController.getViewsReturn(request), null); 
		}*/
		if (action.equals("im.item.edit")) {			
			//ItemModelController.requestUpDateItemModel(request);			
			Urls.forward(request, response, null, ProjetoController.getViewsReturn(request), null);
		}
		else if (action.equals("im.item.edit.download.file")) {		

			if (!ItemModelController.dowloadFileOfItem(request, response))
				Urls.forward(request, response, null, ProjetoController.getViewsReturn(request), null);
		}
		else if (action.equals("im.item.remove")) {		
			w.oldState(ldep);
			if (ItemModelController.requestRemoveItem(request, response))
				Urls.forward(request, response, null, ProjetoController.getViewsReturn(request), null);
		}


	}
}
