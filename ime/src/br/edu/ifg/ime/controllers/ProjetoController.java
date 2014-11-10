package br.edu.ifg.ime.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.imsglobal.jaxb.ld.Act;
import org.imsglobal.jaxb.ld.ActivityStructure;
import org.imsglobal.jaxb.ld.Components;
import org.imsglobal.jaxb.ld.Environment;
import org.imsglobal.jaxb.ld.Learner;
import org.imsglobal.jaxb.ld.LearningActivity;
import org.imsglobal.jaxb.ld.LearningDesign;
import org.imsglobal.jaxb.ld.LearningObject;
import org.imsglobal.jaxb.ld.Play;
import org.imsglobal.jaxb.ld.Roles;
import org.imsglobal.jaxb.ld.Staff;
import org.imsglobal.jaxb.ld.SupportActivity;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.dao.DAOConnection;
import br.edu.ifg.ime.dao.PermissoesDAO;
import br.edu.ifg.ime.dao.PerspectivaDAO;
import br.edu.ifg.ime.dao.ProjetoDAO;
import br.edu.ifg.ime.dao.ServicoDAO;
import br.edu.ifg.ime.dao.UsuarioDAO;
import br.edu.ifg.ime.dto.Projeto;
import br.edu.ifg.ime.dto.Usuario;
import br.edu.ifg.ime.ld.LdProject;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.test.UnitTest;

public class ProjetoController {

	private static ProjetoDAO projetoDao = null;	

	public static ProjetoDAO getProjetoDAO() {

		if (projetoDao == null)
			sync();

		return projetoDao;    	
	}	

	private synchronized static void sync() {

		if (projetoDao == null)
			projetoDao = new ProjetoDAO();	

	}	




	public static TreeMap<String, String> views_return = new TreeMap<String, String>();

	public synchronized static TreeMap<String, String> getViewsReturn() {

		if (views_return.size() > 0)
			return views_return;

		views_return.put(LearningDesign.class.getCanonicalName(), "views/ldep/ldep.edit.jsp");
		views_return.put(Learner.class.getCanonicalName(), "views/componentes/roles/roles.edit.jsp");
		views_return.put(Staff.class.getCanonicalName(),  "views/componentes/roles/roles.edit.jsp");

		views_return.put(LearningActivity.class.getCanonicalName(), "views/componentes/activities/la.edit.jsp");
		views_return.put(SupportActivity.class.getCanonicalName(), "views/componentes/activities/sa.edit.jsp");
		views_return.put(ActivityStructure.class.getCanonicalName(), "views/componentes/activities/as.edit.jsp");

		views_return.put(Environment.class.getCanonicalName(), "views/componentes/environments/environment.edit.jsp");
		views_return.put(LearningObject.class.getCanonicalName(), "views/componentes/environments/lo.edit.jsp");

		views_return.put(Act.class.getCanonicalName(), "views/methods/acts/acts.edit.jsp");
		views_return.put(Play.class.getCanonicalName(), "views/methods/plays/play.edit.jsp");

		views_return.put(LearningDesign.class.getCanonicalName(), "views/ldep/ldep.edit.jsp");

		return views_return;

	} 

	public synchronized static String getViewsReturn(HttpServletRequest request) {

		String idRef = Suport.r(request, "id-ref");
		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
		Object objParent = w.getObject(idRef);

		String result = "";

		result = ProjetoController.getViewsReturn().get(objParent.getClass().getCanonicalName());

		return result == null ? "": result;

	}


	public synchronized static String getViewsReturn(TreeMap<String, Object> lParams, HttpServletRequest request) {

		String idRef = (String)lParams.get("id-ref");

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
		Object objParent = w.getObject(idRef);

		String result = "";

		result = getViewsReturn().get(objParent.getClass().getCanonicalName());

		return result == null ? "": result;

	}

	public static List<Projeto> getProjetos(Usuario u) {

		return getProjetoDAO().selectAll(u);

	}

	public synchronized static boolean persistir(HttpServletRequest request) {

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
		LdProject ldep = w.getLdProject(request);

		Projeto p;

		if (ldep.getPersistencia() == null) { 
			p = new Projeto();
		}
		else
			p = ldep.getPersistencia();


		p.setTitulo(ldep.getLd().getTitle());
		p.setSkin(ldep.skin);
		p.setUsuario(UsuarioController.getUsuarioConectado(request.getSession()));


		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();			
			w.zipImscpWithoutResumeMi(out, ldep.getIdentifier());
			p.setManifesto(out.toByteArray());

			if (p.getId() == 0)
				getProjetoDAO().insert(p);
			else
				getProjetoDAO().update(p);

			getProjetoDAO().updateManifesto(p);


		} catch (Exception e) {
			return false;
		}

		getProjetoDAO().selectProjeto(p);
		ldep.setPersistencia(p);
		ldep.flagAlterado = false;
		p.setManifesto(null);


		return true;
	}


	public synchronized static boolean remover(HttpServletRequest request) throws Exception {

		String idProjeto = Suport.r(request, "id");

		Usuario userConnect = UsuarioController.getUsuarioConectado(request.getSession());

		Projeto p = new Projeto();
		p.setId(Integer.parseInt(idProjeto));

		p = getProjetoDAO().selectProjeto(p);

		if (p.getUsuario().getId() != userConnect.getId())
			throw new Exception(userConnect.getTexto("ime.db.remove.project"));

		getProjetoDAO().delete(p);


		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);

		List<LdProject> lLdeps = w.getListLdProject();

		for (LdProject ldep : lLdeps) {

			if (ldep.getPersistencia() == null || ldep.getPersistencia().getId() != p.getId())
				continue;

			ldep.setPersistencia(null);
			ldep.flagAlterado = true;
		}
		return true;
	}

	public static void abrirProjeto(HttpServletRequest request)  throws Exception {

		String idProjeto = Suport.r(request, "id");

		Usuario userConnect = UsuarioController.getUsuarioConectado(request.getSession());

		Projeto p = new Projeto();
		p.setId(Integer.parseInt(idProjeto));

		p = getProjetoDAO().selectProjeto(p);

		if (p.getUsuario().getId() != userConnect.getId() && !p.getPublico())
			throw new Exception(userConnect.getTexto("ime.db.open.project.privado"));

		p = getProjetoDAO().refreshManifesto(p);


		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);

		ByteArrayInputStream bIn = new ByteArrayInputStream(p.getManifesto());

		LdProject ldep = w.importZIPStream(bIn);

		ldep.flagAlterado = false;
		if (p.getUsuario().getId() != userConnect.getId()) { 
			ldep.setPersistencia(null);
			ldep.flagAlterado = true;
		}
		else 
			ldep.setPersistencia(p);
		ldep.skin = p.getSkin();
		p.setManifesto(null);

/*
		String play = request.getParameter("p");
		String mi = request.getParameter("m");
		String nivel = request.getParameter("n");

		if (play == null || mi == null || nivel == null)
			return;

		UnitTest t = new UnitTest(Integer.parseInt(nivel), Integer.parseInt(mi), Integer.parseInt(play));
		w.t = t;
		t.start(ldep);*/


	}

	public static void publicar(HttpServletRequest request)  throws Exception {

		String idProjeto = Suport.r(request, "id");
		String flagPub = Suport.r(request, "value");

		Usuario userConnect = UsuarioController.getUsuarioConectado(request.getSession());

		Projeto p = new Projeto();
		p.setId(Integer.parseInt(idProjeto));

		p = getProjetoDAO().selectProjeto(p);

		if (p.getUsuario().getId() != userConnect.getId() && !p.getPublico())
			throw new Exception(userConnect.getTexto("ime.db.project.publicar"));

		p.setPublico(flagPub == null || flagPub.equals("true"));

		getProjetoDAO().update(p);





		//
		//ldep.flagAlterado = false;
		//ldep.setPersistencia(p);
		//p.setManifesto(null);

	}


}
