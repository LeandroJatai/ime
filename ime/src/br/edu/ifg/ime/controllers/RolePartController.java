package br.edu.ifg.ime.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;

import org.imsglobal.jaxb.ld.Activities;
import org.imsglobal.jaxb.ld.ActivityStructure;
import org.imsglobal.jaxb.ld.ActivityStructureRef;
import org.imsglobal.jaxb.ld.Environment;
import org.imsglobal.jaxb.ld.EnvironmentRef;
import org.imsglobal.jaxb.ld.LearningActivity;
import org.imsglobal.jaxb.ld.LearningActivityRef;
import org.imsglobal.jaxb.ld.Method;
import org.imsglobal.jaxb.ld.Act;
import org.imsglobal.jaxb.ld.Play;
import org.imsglobal.jaxb.ld.RolePart;
import org.imsglobal.jaxb.ld.RoleRef;
import org.imsglobal.jaxb.ld.SupportActivity;
import org.imsglobal.jaxb.ld.SupportActivityRef;
import org.imsglobal.jaxb.ld.UnitOfLearningHref;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.ld.LdProject;
import br.edu.ifg.ime.suport.LearningDesignUtils;
import br.edu.ifg.ime.suport.Suport;

public class RolePartController {

	public static void requestRemoveRolePart(HttpServletRequest request) {

		List<Play> lPlays = PlaysController.getPlays(request);
		String identifier = Suport.r(request, "identifier");

		for (Play play : lPlays) {					
			for (Act act : play.getActList()) {				
				for (RolePart rp : act.getRolePartList()) {
					if (rp.getIdentifier().equals(identifier)) {
						act.getRolePartList().remove(rp);
						ImeWorkspace.getImeWorkspace(request).excluirOfWorkspace(rp);
						return;
					}
				}
			}
		}			
	}

	public static RolePart getRolePartByIdentifier(HttpServletRequest request, String identifier) {

		List<Play> lPlays = PlaysController.getPlays(request);				
		for (Play play : lPlays) {					
			for (Act act : play.getActList()) {		

				for(RolePart rp : act.getRolePartList()) {

					if (rp.getIdentifier().equals(identifier))
						return rp;
				}
			}
		}			
		return null;
	}

	public static String requestUpDateRolePart(HttpServletRequest request) {

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);

		//Method m = MethodController.getMethodOfJob(request);

		String titulo =  Suport.r(request, "titulo");
		String identifier = Suport.r(request, "identifier");
		String actRef = Suport.r(request, "act-ref");

		String parteSelecionada = Suport.r(request, "parteSelecionada");
		String papelSelecionado = Suport.r(request, "papelSelecionado");
		RolePart rp = null;

		if (identifier != null && identifier.length() == 0) {

			rp = new RolePart();
			rp.setTitle(titulo);
			rp.setIdentifier(w.newIdentifier(rp, "rp"));

			Act act = ActsController.getActByIdentifier(request, actRef);
			act.getRolePartList().add(rp);
			identifier = rp.getIdentifier();
		}
		else {
			rp = getRolePartByIdentifier(request, identifier);
			rp.setTitle(titulo);
		} 

		if (UsuarioController.checkPermissao(request, "rp.view.radiobox.roles"))  {


			if (rp.getRoleRef() != null && rp.getRoleRef().getRef() != null && !LearningDesignUtils.getIdentifier(rp.getRoleRef().getRef()).equals(papelSelecionado)) {
				w.desvincularReferencia(
						LearningDesignUtils.getIdentifier(rp.getRoleRef().getRef()),
						LearningDesignUtils.getIdentifier(rp));
			}
			
			if (rp.getRoleRef() != null && rp.getRoleRef().getRef() != null && LearningDesignUtils.getIdentifier(rp.getRoleRef().getRef()).equals(papelSelecionado)) {
			}
			else if (papelSelecionado != null) {

				if (rp.getRoleRef() == null)
					rp.setRoleRef(new RoleRef());
				
				Object ob = w.getObject(papelSelecionado);
				rp.getRoleRef().setRef(ob);
				w.referenciar(LearningDesignUtils.getIdentifier(ob), rp);
			}
		}

		if (UsuarioController.checkPermissao(request, "rp.view.radiobox.environment") || UsuarioController.checkPermissao(request, "rp.view.radiobox.activities") ) 			
		
				setPart(w, identifier, parteSelecionada, rp);

		return identifier;

	}

	private static void setPart(ImeWorkspace w, String identifier,	String parteSelecionada, RolePart rp) {

		if (rp.getActivityStructureRef() != null) {			
			w.desvincularReferencia(
					LearningDesignUtils.getIdentifier(rp.getActivityStructureRef().getRef()),
					LearningDesignUtils.getIdentifier(rp));
			rp.setActivityStructureRef(null);

		}

		if (rp.getLearningActivityRef() != null) {			
			w.desvincularReferencia(
					LearningDesignUtils.getIdentifier(rp.getLearningActivityRef().getRef()),
					LearningDesignUtils.getIdentifier(rp));
			rp.setLearningActivityRef(null);

		}

		if (rp.getSupportActivityRef() != null) {			
			w.desvincularReferencia(
					LearningDesignUtils.getIdentifier(rp.getSupportActivityRef().getRef()),
					LearningDesignUtils.getIdentifier(rp));
			rp.setSupportActivityRef(null);

		}

		if (rp.getUnitOfLearningHref() != null) {			
			w.desvincularReferencia(
					LearningDesignUtils.getIdentifier(rp.getUnitOfLearningHref().getRef()),
					LearningDesignUtils.getIdentifier(rp));
			rp.setUnitOfLearningHref(null);

		}

		if (rp.getEnvironmentRef() != null) {			
			w.desvincularReferencia(
					LearningDesignUtils.getIdentifier(rp.getEnvironmentRef().getRef()),
					LearningDesignUtils.getIdentifier(rp));
			rp.setEnvironmentRef(null);

		}
		if (parteSelecionada != null) {
		Object ob = w.getObject(parteSelecionada);

		if (ob instanceof LearningActivity) {

			LearningActivityRef laRef = new LearningActivityRef();
			laRef.setRef(ob);
			w.referenciar(LearningDesignUtils.getIdentifier(ob), rp);

			rp.setLearningActivityRef(laRef);
		}
		else if (ob instanceof SupportActivity) {

			SupportActivityRef saRef = new SupportActivityRef();
			saRef.setRef(ob);
			w.referenciar(LearningDesignUtils.getIdentifier(ob), rp);

			rp.setSupportActivityRef(saRef);
		}

		else if (ob instanceof ActivityStructure) {

			ActivityStructureRef asRef = new ActivityStructureRef();
			asRef.setRef(ob);
			w.referenciar(LearningDesignUtils.getIdentifier(ob), rp);

			rp.setActivityStructureRef(asRef);
		}

		else if (ob instanceof Environment) {

			EnvironmentRef envRef = new EnvironmentRef();
			envRef.setRef(ob);
			w.referenciar(LearningDesignUtils.getIdentifier(ob), rp);

			rp.setEnvironmentRef(envRef);
		}

		else if (ob instanceof LdProject) {

			UnitOfLearningHref uolRef = new UnitOfLearningHref();
			uolRef.setRef(ob);
			w.referenciar(LearningDesignUtils.getIdentifier(ob), rp);

			rp.setUnitOfLearningHref(uolRef);
		}
		}
	}

}
