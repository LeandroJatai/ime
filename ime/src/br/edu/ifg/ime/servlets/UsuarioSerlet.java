package br.edu.ifg.ime.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.controllers.Lc;
import br.edu.ifg.ime.controllers.LinguagemController;
import br.edu.ifg.ime.controllers.UsuarioController;
import br.edu.ifg.ime.dto.Linguagem;
import br.edu.ifg.ime.dto.Permissoes;
import br.edu.ifg.ime.dto.Perspectiva;
import br.edu.ifg.ime.dto.Usuario;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.suport.Urls;

/**
 * Servlet implementation class UsuarioSerlet
 */
public class UsuarioSerlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UsuarioSerlet() {
		super();
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		RequestDispatcher rd;
		String action = Suport.r(request, "action");

		//UsuarioController.inserirManualmente();

		if (action == null) 		 	{
			Urls.forward(request, response, null, "views/usuarios/usuarios_login.jsp");
			return;
		}
		else if (action.equals("usuario.json.permissoes")) {

			String strIdUser = request.getParameter("idTipo");
			Usuario tipoUsuario = UsuarioController.getUsuario(strIdUser);
			UsuarioController.refreshPermissoesForUsuario(tipoUsuario);

			String json = "[";

			boolean init = true;
			for (Permissoes p: tipoUsuario.getL_permissoes()) {
				json += init?"{":", {";

				if (init)
					init = false;


				json += "\"id\": \""+p.getId()+"\"";
				json += ", \"servico\": \""+p.getServico().getServico()+"\"";

				json += "}";
			}

			json += "]";
			ServletOutputStream out = response.getOutputStream();

			out.write(json.getBytes());
			out.flush();
			out.close();
			return;

		}
		else if (action.equals("usuario_logout")) {

			ImeWorkspace.resetLdPlayerWorkspace(request);

			UsuarioController.setUsuarioConectado(request.getSession(), null);
			Usuario u = UsuarioController.getUsuarioConectado(request.getSession());

			Urls.forward(request, response, u.getTexto("at.acesso.fim"), "views/usuarios/usuarios_login.jsp");
			return;
		}
		else if (action.equals("usuario_login")) {

			UsuarioController.setUsuarioConectado(request.getSession(), null);

			//response.getOutputStream().println(Suport.r(request, "msg"));
			Urls.forwardAjax(request, response, null, "views/usuarios/usuarios_login.jsp");
			return;
		}
		else if (action.equals(Lc.getTexto(request, "at.acesso.btn.acesso"))) {

			String url = Suport.r(request, "url");

			String login = Suport.r(request, "login");
			String senha = Suport.r(request, "senha");

			Usuario usuario = UsuarioController.validateUsuario(login, senha);

			if (usuario != null) {
				UsuarioController.refreshPermissoesForUsuario(usuario);
				UsuarioController.refreshPerspectivasForUsuario(usuario);
			}

			//	""
			if (usuario != null && !usuario.hasPermissoes("user.login")) {
				usuario = null;
				UsuarioController.setUsuarioConectado(
						request.getSession(), usuario);
				Usuario u = UsuarioController.getUsuarioConectado(request.getSession());
				Urls.forward(request, response, u.getTexto("at.acesso.suspenso"), "views/usuarios/usuarios_login.jsp");
				return;
			} 
			//""
			UsuarioController.setUsuarioConectado(
					request.getSession(), usuario);

			if (usuario == null) {				
				Usuario u = UsuarioController.getUsuarioConectado(request.getSession());
				Urls.forward(request, response, u.getTexto("at.acesso.nome.invalido"), "views/usuarios/usuarios_login.jsp");
				return;
			}

			if (url.length() == 0)
				response.sendRedirect(Urls.urlAppBase);
			else 
				response.sendRedirect(url);
			return;
		}
		else if (action.equals(Lc.getTexto(request, "at.acesso.btn.trocar.senha"))) {
			Urls.forward(request, response, null, "views/usuarios/usuarios_trocasenha.jsp");
			return;
		}
		else if (action.equals("Trocar")) {

			String login = Suport.r(request, "login");
			String senhaOld = Suport.r(request, "senhaOld");
			String senhaNew = Suport.r(request, "senhaNew");
			String senhaRepeat = Suport.r(request, "senhaRepeat");

			Usuario usuario = UsuarioController.validateUsuario(login, senhaOld);
			if (usuario == null) {	
				Usuario u = UsuarioController.getUsuarioConectado(request.getSession());

				Urls.forward(request, response,  u.getTexto("at.acesso.nome.atual.invalido"), "views/usuarios/usuarios_trocasenha.jsp");
				return;
			}
			
			if (!usuario.hasPermissoes("user.trocar.senha")) {

				Urls.forward(request, response,  usuario.getTexto("user.troca.senha.nao.permitido"), "views/usuarios/usuarios_login.jsp");
				return;
				
				
			}
			
			

			if (!senhaNew.equals(senhaRepeat)) {
				Usuario u = UsuarioController.getUsuarioConectado(request.getSession());

				Urls.forward(request, response,  u.getTexto("at.acesso.senha.nova.invalida"), "views/usuarios/usuarios_trocasenha.jsp");
				return;
			}
			else if (senhaNew.length() == 0) {
				Usuario u = UsuarioController.getUsuarioConectado(request.getSession());

				Urls.forward(request, response,  u.getTexto("at.acesso.senha.branca.invalido"), "views/usuarios/usuarios_trocasenha.jsp");
				return;
			}

			usuario.setSenha(UsuarioController.encryptPassword(senhaNew));
			UsuarioController.updateUsuario(usuario);		
			Usuario u = UsuarioController.getUsuarioConectado(request.getSession());
			Urls.forward(request, response,  u.getTexto("at.acesso.nome.alterada"), "views/usuarios/usuarios_login.jsp");
			return;
		}

		if (!UsuarioController.validateConnection(request, response, action))
			return;

		if (action.equals("manage.users")) {

			String strIdUser = request.getParameter("idUser");
			String login = Suport.r(request, "usuario.login");

			String strIdLang = request.getParameter("userIdLang");
			String strIdPerfil = request.getParameter("userIdPerfil");
			String tipoPerfil = request.getParameter("userTipoPerfil");
			String [] servicosPermitidos = request.getParameterValues("servico.item");			


			if (login != null && login.length() > 0) {
				Usuario usuario;

				if (strIdUser == null || strIdUser.equals("0")) {
					usuario = new Usuario();
				}
				else { 
					//					usuario.setId(Integer.parseInt(strIdUser));
					usuario = UsuarioController.getUsuario(strIdUser);
				}

				usuario.setNome(Suport.r(request, "usuario.nome"));
				usuario.setLogin(Suport.r(request, "usuario.login"));
				usuario.setAtivo(true);


				usuario.setTipo_perfil(tipoPerfil);

				if (strIdPerfil != null && !strIdPerfil.equals("0") && !strIdPerfil.equals(strIdUser) && tipoPerfil != null && !tipoPerfil.equals("perfil.livre") )
					usuario.setPerfil(UsuarioController.getUsuario(strIdPerfil));	
				else 
					usuario.setPerfil(null);

				if (strIdLang != null && !strIdLang.equals("0") ) {
					usuario.setLinguagem(LinguagemController.getLinguagem(strIdLang));					
				}
				else usuario.setLinguagem(null);

				String reset = request.getParameter("usuario.senha.reset");
				if (reset != null)
					usuario.setSenha(null);

				UsuarioController.updateUsuario(usuario);
				strIdUser = String.valueOf(usuario.getId());
			}

			if (tipoPerfil != null) {				

				if (strIdUser == null || strIdUser.length() == 0 || strIdUser.equals("0")) {
					Urls.forward(request, response, Lc.getTexto(request, "at.acesso.nome.nao.informado"), "views/usuarios/usuarios.edit.jsp","main_template_clean");
					return;
				}

				UsuarioController.updatePermissoes(strIdUser, servicosPermitidos);

				Usuario uc = UsuarioController.getUsuarioConectado(request.getSession());

				if (uc.getId() == Integer.parseInt(strIdUser)) {
					uc = UsuarioController.getUsuario(strIdUser);
					UsuarioController.setUsuarioConectado(request.getSession(), uc);
				}

				UsuarioController.refreshPerspectivasForUsuario(uc);
				UsuarioController.refreshPermissoesForUsuario(uc);			

				uc.reloadPerfilTextual();

				Urls.forward(request, response, Lc.getTexto(request, "at.users.update.msg"), "views/usuarios/usuarios.edit.jsp","main_template_clean");
				return;
			}

			Urls.forward(request, response, null, "views/usuarios/usuarios.edit.jsp","main_template_clean");

		}
		else if (action.equals("manage.users.remove")) {

			String strIdUser = request.getParameter("idUser");

			UsuarioController.removerUsuario(strIdUser);

			Urls.forward(request, response, Lc.getTexto(request, "at.users.remove.msg"), "views/usuarios/usuarios.edit.jsp","main_template_clean");		
		}	
		else if (action.equals("manage.users.perspectiva.remove")) {

			String strIdPerspectiva = request.getParameter("idPerspectiva");

			UsuarioController.removerPerspectiva(strIdPerspectiva);
			UsuarioController.refreshPerspectivasForUsuario(UsuarioController.getUsuarioConectado(request.getSession()));

			Urls.forward(request, response, Lc.getTexto(request, "at.users.persp.remove.msg"), "views/usuarios/usuarios.perfil.jsp","main_template_clean");		
		}
		else if (action.equals("manage.users.perspectiva.open")) {		

			String strIdPerspectiva = request.getParameter("idPerspectiva");
			Perspectiva pp = null;
			Usuario u = UsuarioController.getUsuarioConectado(request.getSession());			
			if (strIdPerspectiva != null && strIdPerspectiva.length() > 0) {
				pp= UsuarioController.getPerspectiva(strIdPerspectiva);

				if (pp != null && u != null && pp.getUsuario().getId() == u.getId()) {
					u.wmx_perspectiva = pp;
					UsuarioController.refreshPermissoesForUsuario(u);
					u.reloadPerfilTextual();

				}
			}
			else {
				u.wmx_perspectiva = null;
				UsuarioController.refreshPermissoesForUsuario(u);
				u.reloadPerfilTextual();
			}

			Urls.forward(request, response, null, "views/ldew/ldew.list.jsp", null);
		}
		else if (action.equals("manage.users.perspectiva")) {

			String strIdPerspectiva = request.getParameter("idPerspectiva");
			String strIdUser = request.getParameter("idUser");
			String userIdLang = request.getParameter("userIdLang");

			String titulo = Suport.r(request, "titulo");
			Perspectiva pp = null;

			if (titulo != null && titulo.length() > 0) {

				Usuario usuario;


				if (strIdPerspectiva == null || strIdPerspectiva.equals("0")) {
					pp = new Perspectiva();
				}
				else { 
					//					usuario.setId(Integer.parseInt(strIdUser));
					pp = UsuarioController.getPerspectiva(strIdPerspectiva);
				}

				pp.setTitulo(titulo);
				if (strIdUser != null)
					pp.setUsuario(UsuarioController.getUsuario(strIdUser));
				else 
					pp.setUsuario(UsuarioController.getUsuarioConectado(request.getSession()));

				if (userIdLang != null && !userIdLang.equals("0")) {
					Linguagem l = new Linguagem();
					l.setId(Integer.parseInt(userIdLang));
					pp.setLinguagem(l);
				}
				else pp.setLinguagem(null);


				UsuarioController.updatePerspectiva(pp);
				strIdPerspectiva = String.valueOf(pp.getId());
				String [] servicosPermitidos = request.getParameterValues("servico.item");			

				if (servicosPermitidos != null) {
					UsuarioController.updatePerspectivas(strIdPerspectiva, servicosPermitidos);	
					UsuarioController.refreshPerspectivasForUsuario(UsuarioController.getUsuarioConectado(request.getSession()));	
					UsuarioController.getUsuarioConectado(request.getSession()).reloadPerfilTextual();
				}	

			}
			Urls.forward(request, response, null, "views/usuarios/usuarios.perfil.jsp","main_template_clean");

		}





	}

}
