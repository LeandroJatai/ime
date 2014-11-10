package br.edu.ifg.ime.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
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

import br.edu.ifg.ime.controllers.ArquivoController;
import br.edu.ifg.ime.controllers.Lc;
import br.edu.ifg.ime.controllers.UsuarioController;
import br.edu.ifg.ime.suport.ImeException;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.suport.Urls;

/**
 * Servlet implementation class ArquivoServlet
 */
public class ArquivoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ArquivoServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = Suport.r(request, "action");


		if (action == null) {

			boolean isMultipart = ServletFileUpload.isMultipartContent(request);

			if (!isMultipart) {
				
				String url = request.getPathInfo();
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("contentType", "text/css");
				//response.setHeader("Content-Disposition", "inline; filename="+item.getNameFile());				
				try {
					byte[] result = ArquivoController.getArquivoDAO().getBytesArquivo(url);
					if (result != null && result.length > 0)
						response.getOutputStream().write(result);
					else 
						System.out.println("Arq. N. Encontrado: "+url);
				} catch (SQLException e) {

				}

				//TODO: Buscar e encaminhar arquivo

				//Urls.forward(request, response, null, "views/arquivo/arquivo.edit.jsp", null);
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


				if (action != null && action.equals("manage.arquivo.upload")) {

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
							if (fieldname.equals("fileData")) {

								filecontent = item.getInputStream();

								TreeMap<String, Object> file = (TreeMap<String, Object>) lParams.get("fileData");

								if (file == null) {
									file = new TreeMap<String, Object>();
									lParams.put("fileData", file);
								}

								file.put("name", filename);
								file.put("data", Suport.toByteVector(filecontent));
								file.put("content_type", item.getContentType());

							}
						}
					}

					try {

						ArquivoController.updateArquivo(lParams);
						request.setAttribute("lParams", lParams);
					} catch (ImeException e) {
						Urls.forward(request, response, e.getMessage(), "views/arquivo/arquivo.edit.jsp", "main_template_clean");
						return;
					}

					Urls.forward(request, response, null, "views/arquivo/arquivo.edit.jsp", "main_template_clean");
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}



		if (!UsuarioController.validateConnection(request, response, action))
			return;


		if (action.equals("manage.arquivo")) {
			Urls.forward(request, response, null, "views/arquivo/arquivo.edit.jsp", "main_template_clean");
		}


	}


}
