package br.edu.ifg.ime.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationEvent;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.imsglobal.jaxb.ld.Learner;
import org.imsglobal.jaxb.ld.LearningDesign;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.controllers.Lc;
import br.edu.ifg.ime.controllers.ProjetoController;
import br.edu.ifg.ime.controllers.RolesController;
import br.edu.ifg.ime.controllers.UsuarioController;
import br.edu.ifg.ime.ld.LdProject;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.suport.Urls;

/**
 * Servlet implementation class AppBaseServlet
 */
public class AppBaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AppBaseServlet() {
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
				Urls.forward(request, response, null, "views/ldew/ldew.list.jsp", null);
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


				for (DiskFileItem item : items) {


					if (item.isFormField()) {
						// Process regular form field (input type="text|radio|checkbox|etc", select, etc).
						//String fieldname = item.getFieldName();
						//String fieldvalue = item.getString();
						// ... (do your job here)

					} else {


						// Process form file field (input type="file").
						String fieldname = item.getFieldName();
						String filename = FilenameUtils.getName(item.getName());

						if (!UsuarioController.validateConnection(request, response, action))
							return;

						if (action != null && action.equals("ldep.import.imscp") && fieldname.equals("fileData")) {

							InputStream filecontent = item.getInputStream();

							String cType = item.getContentType();

							if (cType.equals("text/xml")) {

								try {
									w.importXMLRequest(filecontent, request, response);
								} catch (Exception e) {

									e.printStackTrace();
								}
							}
							else if  (cType.equals("application/zip")) {

								try {
									w.importZIPRequest(filecontent, request, response);
								} catch (Exception e) {

									e.printStackTrace();
								}


							}

						}
					}
				}
			} catch (FileUploadException e) {

				e.printStackTrace();
			}


			//Urls.forward(request, response, null, "views/usuarios/usuarios_login.jsp");
			//Urls.forward(request, response, null, "views/ldew/ldew.list.jsp");
			return;
		}
		
		if (action.equals("ldep.vsplitbar")) { 
			
			String pos = request.getParameter("pos");
			HttpSession session = request.getSession();
			session.setAttribute("ldep.vsplitbar", pos);
			return;
			
		}

		

		if (!UsuarioController.validateConnection(request, response, action))
			return;

		if (action.equals("ldep.new")) {

			w.newLdProject();			

			Urls.forward(request, response, null, "views/ldew/ldew.list.jsp", null); 
			return;
		}else if (action.equals("ldep.new.copy")) {

			ldep = w.criarCopiaLdep(ldep);			

			Urls.redirectAction(request, response, "app.do?action=ldep.edit&ldep="+ldep); 
			return;
		}
		else if (action.equals("ldep.remove")) {
			w.removeLdProject(ldep);			

			Urls.forward(request, response, null, "views/ldew/ldew.list.jsp"); 
			return;
		}

		else if (action.equals("ldep.reset")) {

			ImeWorkspace.resetLdPlayerWorkspace(request);			

			Urls.forward(request, response, null, "views/ldew/ldew.list.jsp");
			return;
		}

		else if (action.equals("ldep_select")) {
			Urls.forward(request, response, null, "views/ldew/ldew.list.jsp"); 
			return;
		}
		else if (action.equals("ldep.undo")) {

			w.getLdProject(request).undo();

			Urls.forward(request, response, null, "views/ldew/ldew.list.jsp"); 
			return;
		}		
		else if (action.equals("ldep.redo")) {

			w.getLdProject(request).redo();

			Urls.forward(request, response, null, "views/ldew/ldew.list.jsp"); 
			return;
		}
		else if (action.equals("ldew.wizard")) {
			Urls.forward(request, response, null, "views/ldew/ldew.wizard.jsp"); 
			return;
		}

		else if (action.equals("ldep.edit")) {

			List<LdProject> lldeps = w.getMasterListLdProject();

			LdProject ldepSelect = w.getLdProjectByIdentifier(ldep);
			if (!ldepSelect.isAgregado())			
				for (LdProject obLdep : lldeps) {
					if (obLdep == null)
						continue;
					obLdep.flagTreeView = obLdep.getIdentifier().equals(ldep);
				}
			else {
				ldepSelect.flagTreeView = true;
			}




			Urls.forward(request, response, null, "views/ldep/ldep.edit.jsp");
			return;
		}
		else if (action.equals("ldep.edit.code.imscp")) {
			Urls.forward(request, response, null, "views/ldep/ldep.edit.code.imscp.jsp"); 
			return;
		}
		else if (action.equals("ldep.save")) {

			w.getLdProject(request).oldState();

			w.getLdProject(request).requestUpDateLdProject(request);
			

			Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "views/ldep/ldep.edit.jsp");
			return;
		}
		else if (action.equals("ldep.save.code.imscp")) {

			w.oldState(ldep);

			w.updateByCodeIMSCP(ldep, Suport.r(request, "codigo"));

			Urls.forward(request, response,  Lc.getTexto(request, "at.ldep.msg.save"), "views/ldep/ldep.edit.jsp"); 
			return;
		}
		else if (action.equals("ldep.comp.mi.roles.config")) {

			w.oldState(ldep);

			RolesController.vincularRoles(request);

			Urls.forward(request, response, null,  "views/ldep/ldep.comp.mi.roles.config.jsp");

		}		
		else if (action.equals("ldep.ftv")) {

			String identifier = Suport.r(request, "identifier");

			if (identifier != null)			
				w.toogleFlagTreeView(identifier);

			response.getOutputStream().print("ok");
			//Urls.forward(request, response, null, "views/ldew/ldew.list.jsp");
			return;
		}

		else if (action.equals("ldep.switch.pos.ldep")) {

			String [] params = Suport.r(request, "item").split("-");

			ldep = params[1];		
			w.switchPosLdeps(params, Suport.r(request, "start"), Suport.r(request, "stop"));

			response.getOutputStream().print("OK");
		}

		else if (action.equals("ldep.zip.file.imscp")) {

			try {


				LdProject ldProject = w.getLdProjectByIdentifier(ldep);
				ldProject.validateImsLd();
				
				if (Suport.existErrorIn(ldProject)) {
					Urls.forward(request, response, "Verifique os erros abaixo!", "views/ldep/ldep.edit.jsp");		
					return;
				}
				 
				ByteArrayOutputStream bOut = new ByteArrayOutputStream();				
				List<ValidationEvent> result = w.zipImscpWithResumeMi(bOut, ldep);

				if (result == null || result.size() == 0) {
					response.setHeader("contentType", "application/zip");
					response.setHeader("Content-Disposition", "attachment; filename="+ldep+".zip");
					ServletOutputStream out = response.getOutputStream();

					out.write(bOut.toByteArray());
					
					return;
				}



				request.setAttribute("errosExport", result);

				//w.criarCopiaNoPadraoLd(ldep);
				Urls.forward(request, response, null, "views/ldep/ldep.edit.jsp");			
				//Urls.forward(request, response, null, "views/ldep/ldep.erros.export.jsp");			
			
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}

		else if (action.equals("ldep.zip.file.lmi")) {

			try {
				
				
				
				
				ByteArrayOutputStream bOut = new ByteArrayOutputStream();				
				List<ValidationEvent> errors = w.zipImscpWithoutResumeMi(bOut, ldep);

				if (errors == null || errors.size() == 0) {
					response.setHeader("contentType", "application/zip");
					response.setHeader("Content-Disposition", "attachment; filename="+ldep+".zip");
					ServletOutputStream out = response.getOutputStream();

					out.write(bOut.toByteArray());
					
					return;
				}
				
				request.setAttribute("errosExport", errors);
				Urls.forward(request, response, null, "views/ldep/ldep.erros.export.jsp");			

			} catch (Exception e) {
				e.printStackTrace();
			}

		}	

		else if (action.equals("ldep.xml.backup.project")) {
			try {


				w.xmlBackupProject(response.getOutputStream(), ldep);

			} catch (Exception e) {

				e.printStackTrace();
			}
		}	
		else if (action.equals("ldep.xml.backup.workspace")) {
			try {

				w.xmlBackupWorkspace(response.getOutputStream());

			} catch (Exception e) {

				e.printStackTrace();
			}
		}	
		else if (action.equals("ldep.database.update")) {			
			ProjetoController.persistir(request);
			Urls.forward(request, response, null, "views/ldew/ldew.list.jsp");			
		}
		else if (action.equals("ldep.database.publicar")) {		
			try {
				ProjetoController.publicar(request);
			} catch (Exception e) {
				Urls.forward(request, response, e.getMessage(), "views/ldew/ldew.database.list.owner.jsp");
				return;
			}
			Urls.forward(request, response, null,"views/ldew/ldew.database.list.owner.jsp");
		}
		else if (action.equals("ldep.database.open")) {			
			try {
				ProjetoController.abrirProjeto(request);
			} catch (Exception e) {
				Urls.forward(request, response, e.getMessage(), "views/ldew/ldew.list.jsp");	
				return;
			}
			Urls.forward(request, response, null, "views/ldew/ldew.list.jsp");			
		}
		else if (action.equals("ldep.database.remove")) {

			try {
				ProjetoController.remover(request);
			} catch (Exception e) {
				Urls.forward(request, response, e.getMessage(), "views/ldew/ldew.database.list.owner.jsp");
				return;
			}

			Urls.forward(request, response, null, "views/ldew/ldew.database.list.owner.jsp"); 					
		}				
		else if (action.equals("ldep.database.list.owner")) {			
			Urls.forward(request, response, null, "views/ldew/ldew.database.list.owner.jsp"); 			
		}	
		else if (action.equals("ldep.database.list.publicos")) {			
			Urls.forward(request, response, null, "views/ldew/ldew.database.list.publicos.jsp"); 			
		}
	}
}
