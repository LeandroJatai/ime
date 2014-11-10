package br.edu.ifg.ime.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.controllers.Lc;
import br.edu.ifg.ime.controllers.LinguagemController;
import br.edu.ifg.ime.controllers.UsuarioController;
import br.edu.ifg.ime.dto.Chave_textual;
import br.edu.ifg.ime.dto.Linguagem;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.suport.Urls;

/**
 * Servlet implementation class LinguagemServlet
 */
public class LinguagemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LinguagemServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



		String action = Suport.r(request, "action");
		//String ldep = Suport.r(request, "ldep");
		//LdEditorWorkspace w = LdEditorWorkspace.getLdEditorWorkspace(request);
			
		if (action == null)
			action = "manage.language";
		
		if (!UsuarioController.validateConnection(request, response, action))
			return;

		if (action.equals("manage.language")) {
			
			Urls.forward(request, response, null, "views/linguagem/linguagem.edit.jsp", "main_template_clean"); 
			return;
		}
		else if (action.equals("manage.schemas")) {
			
			Urls.forward(request, response, null, "views/schemas/schemas.edit.jsp", "main_template_clean"); 
			return;
		}		
		else if (action.equals("manage.schemas.save")) {

			String idSchema = request.getParameter("idSchema");
			String texto = Suport.r(request, "schema.texto");
			String chave = Suport.r(request, "schema.chave");
			String tipo = Suport.r(request, "schema.tipo");
			

			Chave_textual ct = new Chave_textual();
			ct.setId(Integer.parseInt(idSchema));
			ct.setChave(chave);
			ct.setTexto(texto);
			ct.setTipo(Integer.parseInt(tipo));
			
			LinguagemController.updateSchema(ct);
			
			
			Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "views/schemas/schemas.edit.jsp", "main_template_clean"); 
			return;
		}
		else if (action.equals("manage.language.save")) {

			String idLang = request.getParameter("idLang");
			String langTitulo = Suport.r(request, "lang.titulo");
			String texto[] = request.getParameterValues("lang.texto");
			String chave[] = request.getParameterValues("lang.chave");
			
			Linguagem linguagem = new Linguagem();
			linguagem.setId(Integer.parseInt(idLang));
			linguagem.setTitulo(langTitulo);
			
			
			LinguagemController.update(linguagem, chave, texto);
			
			
			Urls.forward(request, response, Lc.getTexto(request, "at.ldep.msg.save"), "views/linguagem/linguagem.edit.jsp", "main_template_clean"); 
			return;
		}	
		else if (action.equals("manage.language.remove")) {

			String idLang = request.getParameter("idLang");

			LinguagemController.delete(idLang);
			
			
			Urls.forward(request, response, null, "views/linguagem/linguagem.edit.jsp", "main_template_clean"); 
			return;
		}
	}

}
