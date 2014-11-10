package br.edu.ifg.ime.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

import br.edu.ifg.ime.dao.PermissoesDAO;
import br.edu.ifg.ime.dao.PerspectivaDAO;
import br.edu.ifg.ime.dao.ServicoDAO;
import br.edu.ifg.ime.dao.UsuarioDAO;
import br.edu.ifg.ime.dto.Permissoes;
import br.edu.ifg.ime.dto.Perspectiva;
import br.edu.ifg.ime.dto.Servico;
import br.edu.ifg.ime.dto.Usuario;
import br.edu.ifg.ime.suport.Urls;


public class UsuarioController {

	private static UsuarioDAO usuarioDao = null;	
	private static ServicoDAO servicoDao = null;
	private static PermissoesDAO permissoesDao = null;
	private static PerspectivaDAO perspectivaDao = null;

	private synchronized static void sync() {

		if (usuarioDao == null)
			usuarioDao = new UsuarioDAO();	

		if (servicoDao == null)
			servicoDao = new ServicoDAO();		

		if (permissoesDao == null)
			permissoesDao = new PermissoesDAO();

		if (perspectivaDao == null)
			perspectivaDao = new PerspectivaDAO();		
	}	

	private static UsuarioDAO getUsuarioDAO() {

		if (usuarioDao == null)
			sync();

		return usuarioDao;    	
	}	
	private static ServicoDAO getServicoDAO() {

		if (servicoDao == null)
			sync();

		return servicoDao;    	
	}	
	private static PermissoesDAO getPermissoesDAO() {

		if (permissoesDao == null)
			sync();

		return permissoesDao;    	
	}	
	private static PerspectivaDAO getPerspectivaDAO() {

		if (perspectivaDao == null)
			sync();

		return perspectivaDao;    	
	}	

	public static void inserirUsuario(Usuario usuario) {

		resetSenha(usuario);
		updateUsuario(usuario);
	}

	private static void resetSenha(Usuario usuario) {
		usuario.setSenha("123456");
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		String encryptedPassword = passwordEncryptor.encryptPassword(usuario.getSenha());
		usuario.setSenha(encryptedPassword);
	}

	public static String encryptPassword(String senha) {

		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		String encryptedPassword = passwordEncryptor.encryptPassword(senha);

		return encryptedPassword;

	}

	public static void updateUsuario(Usuario usuario) {

		if (usuario.getId() == 0) {
			resetSenha(usuario);
			getUsuarioDAO().insert(usuario);
		}
		else {
			if (usuario.getSenha() == null || usuario.getSenha().length() == 0)
				resetSenha(usuario);
			getUsuarioDAO().update(usuario);
		}

	}
	public static Usuario getUsuario(String id) {
		return getUsuarioDAO().selectUsuarioById(id);

	}

	public static Usuario validateUsuario(String login, String senha) {

		Usuario usuario = getUsuarioDAO().selectUsuarioByLogin(login);

		if (usuario == null || usuario.getId() > Usuario.wmx_root && usuario.getId() <= Usuario.wmx_convidado)
			return null;

		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		//String encryptedPassword = passwordEncryptor.encryptPassword(senha);

		if (passwordEncryptor.checkPassword(senha, usuario.getSenha()))
			return usuario;

		return null;
	}

	public static Usuario getUsuarioConectado(HttpSession session) {		
		Usuario usuario = (Usuario)session.getAttribute("userConnected");	

		if (usuario == null) {
			usuario = UsuarioController.getUsuario(String.valueOf(Usuario.wmx_convidado));
			refreshPermissoesForUsuario(usuario);
			setUsuarioConectado(session, usuario);
		}


		return usuario;				
	}

	public static void setUsuarioConectado(HttpSession session, Usuario usuario) {		
		session.setAttribute("userConnected", usuario);		
	}

	public static boolean checkPermissao(HttpServletRequest request, String action)  {

		if (action == null)
			return false;

		HttpSession session = request.getSession();

		Usuario user = null;
		if ((user = (Usuario)session.getAttribute("userConnected")) == null)
			user = getUsuarioConectado(session);

		if (user.cacheValido() && user.hasPermissoes(action))
			return true;

		Servico servico = new Servico();
		servico.setServico(action);

		servico = getServicoDAO().select(servico);

		if (servico == null)
			return false;

		if (!servico.getAutenticar())
			return true;


		if (!getPermissoesDAO().existePermissao(user, servico, user.wmx_perspectiva))
			return false;

		return true;
	}

	public static boolean checkPermissao(Usuario usuario, String action)  {

		if (action == null)
			return false;

		if (usuario.cacheValido() && usuario.hasPermissoes(action))
			return true;

		Servico servico = new Servico();
		servico.setServico(action);

		servico = getServicoDAO().select(servico);

		if (!servico.getAutenticar())
			return true;

		return checkPermissao(usuario, servico);

	}

	public static boolean checkPermissao(Usuario usuario, Servico servico)  {
		return checkPermissao(usuario, servico, usuario!=null?usuario.wmx_perspectiva:null);
	}

	public static boolean checkPermissao(Usuario usuario, Servico servico, Perspectiva perspectiva)  {

		if (servico == null)
			return false;

		if (!servico.getAtivo())
			return false;

		if (!servico.getAutenticar())
			return true;

		if (usuario == null)
			return false;

		if (perspectiva != null) {
			if (usuario.wmx_perspectiva != null && perspectiva != null && usuario.wmx_perspectiva.getId() == perspectiva.getId() && usuario.cacheValido() && usuario.hasPermissoes(servico.getServico()))
				return true;
		}
		else {
			if (usuario.cacheValido() && usuario.hasPermissoes(servico.getServico()))
				return true;
		}

		if (!getPermissoesDAO().existePermissao(usuario, servico, perspectiva))
			return false;

		return true;
	}

	public static boolean validateConnection(HttpServletRequest request,
			HttpServletResponse response, String action) throws ServletException, IOException {

		System.out.println(action);

		if (action == null)
			return true;

		HttpSession session = request.getSession();

		Usuario user = null;
		if ((user = (Usuario)session.getAttribute("userConnected")) == null)
			user = getUsuarioConectado(session);


		if (user.cacheValido()) {

			if (user.hasPermissoes(action))
				return true;

		}

		Servico servico = new Servico();
		servico.setServico(action);
		servico = getServicoDAO().select(servico);

		if (servico == null) {
			return errorConnectUser(request, response, action, user.getTexto("ime.msg.serv.inexistente"));
		}

		if (!servico.getAtivo())
			return errorConnectUser(request, response, action, user.getTexto("ime.msg.serv.desativado"));

		if (!servico.getAutenticar())
			return true;		

		if (!getPermissoesDAO().existePermissao(user, servico, user.wmx_perspectiva))
			return errorConnectUser(request, response, action, null);

		return true;
	}

	private static boolean errorConnectUser(HttpServletRequest request,
			HttpServletResponse response, String action, String msg) throws IOException, ServletException {

		request.setAttribute("url", request.getRequestURI());

		if (msg == null) {
			msg = Lc.getTexto(request, "ime.msg.serv.restrito")+"<br>("+action+")";
		}

		if (action.endsWith("_ajax"))
			Urls.forwardAjax(request, response, msg, "views/usuarios/usuarios_login.jsp");
		else 
			Urls.forward(request, response, msg, "views/usuarios/usuarios_login.jsp");


		//response.sendRedirect(Urls.urlPortalSislegisBase+"/usuarios/");
		return false;
	}

	public static List<Servico> getServicosAutenticaveis() {

		return getServicoDAO().selectAllAutenticaveis();
	}

	public static void refreshPermissoesForUsuario(Usuario usuario) {		
		getPermissoesDAO().getPermissoes(usuario, usuario.wmx_perspectiva);
	}

	public static List<Permissoes> getPermissoesForUsuario(Usuario usuario) {	
		Usuario u = new Usuario();
		u.setId(usuario.getId());
		getPermissoesDAO().getPermissoes(u, null);
		return u.getL_permissoes();
	}

	public static void refreshPerspectivasForUsuario(Usuario usuario) {		

		getPerspectivaDAO().getPerspectivas(usuario);
	}

	public static List<Usuario> getUsuarios() {

		return getUsuarioDAO().selectAll();
	}
	public static List<Usuario> getTipoDeUsuarios() {

		return getUsuarioDAO().selectTiposDeUsuarios();
	}

	public static void updatePermissoes(String strIdUser, String[] servicosPermitidos) {

		Usuario usuario = getUsuarioDAO().selectUsuarioById(strIdUser);
		Usuario perfil = usuario.getPerfil();

		refreshPermissoesForUsuario(usuario);

		for (Permissoes p: usuario.getL_permissoes()) {
			boolean flagExcluir = true;
			if (servicosPermitidos != null)
				for (int i = 0; i< servicosPermitidos.length; i++) {

					if (p.getServico().getServico().equals(servicosPermitidos[i])) {
						flagExcluir = false;		
						servicosPermitidos[i] = null;
						break;
					}
				}

			//Excluir todo serviço que está na base de dados mas não está na nova seleção
			if (flagExcluir)
				getPermissoesDAO().deletePermissoesPerspectivaPadrao(p);
			// apagar da perspectiva padrão significa apagar de todas as perspectivas deste usuario

		}

		if (servicosPermitidos != null)
			for (String strServico: servicosPermitidos) {
				if (strServico == null)
					continue;

				Servico servico = new Servico();
				servico.setServico(strServico);
				servico = getServicoDAO().select(servico);

				Permissoes p = new Permissoes();
				p.setLiberado(true);
				p.setServico(servico);
				p.setUsuario(usuario);

				try {
					getPermissoesDAO().insert(p);				

				} catch (SQLException e) {

					e.printStackTrace();
				}



			}

		if (perfil != null && usuario.getTipo_perfil().equals("perfil.fixo")) {

			refreshPermissoesForUsuario(perfil);


			List<Usuario> lUserPerfil = selectUsuariosComPerfil(perfil);

			for (Permissoes pUser: perfil.getL_permissoes()) {

				for (Usuario user: lUserPerfil) {

					if (checkPermissao(usuario, pUser.getServico()))
						continue;

					pUser.setUsuario(user);
					try {
						getPermissoesDAO().insert(pUser);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}



				}

			}

			for (Usuario user: lUserPerfil) {

				refreshPermissoesForUsuario(user);

				for (Permissoes pUser: user.getL_permissoes()) {

					if (!perfil.hasPermissoes(pUser.getServico().getServico()))
						getPermissoesDAO().deletePermissoesPerspectivaPadrao(pUser);


				}

			}
		}





	}

	public static void updatePerspectivas(String strIdPerspectiva, String[] servicosPermitidos) {

		Perspectiva pp = getPerspectivaDAO().getPerspectiva(strIdPerspectiva);
		getPermissoesDAO().deleteTodasPermissoes(pp);

		for (String strServico: servicosPermitidos) {

			if (strServico == null)
				continue;

			Servico servico = new Servico();
			servico.setServico(strServico);
			servico = getServicoDAO().select(servico);

			Permissoes p = new Permissoes();
			p.setLiberado(true);
			p.setServico(servico);
			p.setUsuario(pp.getUsuario());
			p.setPerspectiva(pp);

			try {
				getPermissoesDAO().insert(p);				

			} catch (SQLException e) {

				e.printStackTrace();
			}
		}	
	}

	public static void removerUsuario(String idUser) {

		Usuario usuario = new Usuario(Integer.parseInt(idUser));

		getUsuarioDAO().delete(usuario);



	}

	public static Perspectiva getPerspectiva(String strIdPerspectiva) {

		return getPerspectivaDAO().getPerspectiva(strIdPerspectiva);

	}

	public static void updatePerspectiva(Perspectiva pp) {
		try {
			getPerspectivaDAO().upDate(pp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void removerPerspectiva(String strIdPerspectiva) {
		Perspectiva pp = new Perspectiva(Integer.parseInt(strIdPerspectiva));

		getPerspectivaDAO().delete(pp);

	}

	public static  List<Usuario> selectUsuariosComPerfil(Usuario perfil) {

		return getUsuarioDAO().selectUsuariosComPerfil(perfil);
	}

    public static void updateServico(Servico s) {
    	getServicoDAO().upDate(s);
    }

}
