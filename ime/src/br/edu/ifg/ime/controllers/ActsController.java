package br.edu.ifg.ime.controllers;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.imsglobal.jaxb.ld.LearningDesign;
import org.imsglobal.jaxb.ld.Method;
import org.imsglobal.jaxb.ld.Act;
import org.imsglobal.jaxb.ld.Play;
import org.imsglobal.jaxb.ld.RolePart;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.ld.LdProject;
import br.edu.ifg.ime.ld.LearningDesignRef;
import br.edu.ifg.ime.suport.LearningDesignUtils;
import br.edu.ifg.ime.suport.Suport;

public class ActsController {

	public static void requestRemoveLdepOfAct(HttpServletRequest request) {

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);

		String actRef = Suport.r(request,"act-ref");
	//	String ldep = Suport.r(request,"ldep-ref");
		String ldepRef = Suport.r(request,"ldep");


		Act act = (Act) w.getObject(actRef);
		LearningDesignRef ldRefOld = act.getLearningDesignRef();

		if (LearningDesignUtils.getRef(ldRefOld) == null) {
			act.setLearningDesignRef(null);
		}
		else {
			String key = LearningDesignUtils.getIdentifier(LearningDesignUtils.getRef(ldRefOld));

			w.desvincularReferencia(
					key,
					LearningDesignUtils.getIdentifier(act));

			LdProject elRemove = w.getLdProjectByIdentifier(ldepRef).removerLdAgregadoByIdLD(key);
			w.removeLdProject(elRemove.getIdentifier());
		}

		w.excluirOfWorkspace(act);





	}

	public static void requestRemoveAct(HttpServletRequest request) {

		List<Play> lPlays = PlaysController.getPlays(request);
		String identifier = Suport.r(request,"identifier");

		for (Play play : lPlays) {					
			for (Act act : play.getActList()) {		
				if (act.getIdentifier().equals(identifier)) {
					play.getActList().remove(act);

					if (act.getLearningDesignRef() != null ) {
						String key = LearningDesignUtils.getIdentifier(LearningDesignUtils.getRef(act.getLearningDesignRef()));

						ImeWorkspace.getImeWorkspace(request).desvincularReferencia(
								key,
								LearningDesignUtils.getIdentifier(act));

						LdProject elRemove = ImeWorkspace.getImeWorkspace(request).getLdProjectByIdentifier(Suport.r(request, "ldep")).removerLdAgregadoByIdLD(key);
					}
					act.setLearningDesignRef(null);

					ImeWorkspace.getImeWorkspace(request).excluirOfWorkspace(act);
					return;
				}
			}
		}			
	}

	public static Act getActByIdentifier(HttpServletRequest request, String identifier) {

		//Method m = MethodController.getMethodOfJob(request);

		List<Play> lPlays = PlaysController.getPlays(request);				
		for (Play play : lPlays) {					
			for (Act act : play.getActList()) {				
				if (act.getIdentifier().equals(identifier))
					return act;
			}
		}
 
		return null;
	}

	public static String requestUpDateAct(HttpServletRequest request) {

		//Method m = MethodController.getMethodOfJob(request);
		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
		String titulo =  Suport.r(request, "titulo");

		if (titulo == null) titulo = "no title";

		String identifier = Suport.r(request, "identifier");
		String playRef = Suport.r(request, "play-ref");

		String ldRef[] = request.getParameterValues("ld-ref");

		if (identifier != null && identifier.length() == 0) {

			Act act = new Act();
			act.setTitle(titulo);
			act.setIdentifier(w.newIdentifier(act, "act"));
			updateLearningDesignRef(request, ldRef, act.getLearningDesignRef(), act);
			identifier = act.getIdentifier();

			Play play = (Play)w.getObject(playRef);
			//Play play = PlaysController.getPlayByIdentifier(request, playRef);
			play.getActList().add(act);

			w.referenciar(act.getIdentifier(), play);

		}
		else {

			Act act = getActByIdentifier(request, identifier);
			act.setTitle(titulo);

			updateLearningDesignRef(request, ldRef, act.getLearningDesignRef(), act);
		}

		if (ldRef != null && ldRef.length > 0)
			return ldRef[0];

		return identifier;
	}



	private static void updateLearningDesignRef(HttpServletRequest request,
			String[] ldNovosReferenciados, LearningDesignRef ldRefOld, Object actAgregante) {

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);


		Act act = (Act) actAgregante;

		if (LearningDesignUtils.getRef(ldRefOld) == null) {
			act.setLearningDesignRef(null);
		}
		else {
			
			String key = LearningDesignUtils.getIdentifier(LearningDesignUtils.getRef(ldRefOld));

			w.desvincularReferencia(
					key,
					LearningDesignUtils.getIdentifier(actAgregante));

			LdProject elRemove = w.getLdProjectByIdentifier(Suport.r(request, "ldep")).removerLdAgregadoByIdLD(key);
			act.setLearningDesignRef(null);
		}

		if (ldNovosReferenciados != null)
			for (String strLdRef : ldNovosReferenciados) {


				String newLdep = w.criarCopiaLdep(w.getLdProjectByIdentifier(strLdRef));
				ldNovosReferenciados[0] = newLdep;

				LdProject ldep = w.getLdProjectByIdentifier(newLdep);

				LearningDesign ld = ldep.getLd();
				ld.setInheritRoles(true);

				LearningDesignRef ldRef = new LearningDesignRef();
				ldRef.setRef(ld);
				
				act.setLearningDesignRef(ldRef);

				w.getLdProjectByIdentifier(Suport.r(request, "ldep")).agregarLd(ldep);
				
				w.referenciar(ldep.getIdentifier(), actAgregante);
				break;
			}
	}


	public static void switchPosRps(ImeWorkspace w, String[] params, String start, String stop) {

		LdProject ldep = w.getLdProjectById(params[7]);

		for (Play play : ldep.getLd().getMethod().getPlayList()) {		


			for (Act a : play.getActList()) {

				for (RolePart rp : a.getRolePartList()) {

					if (rp.getId() != Long.parseLong(params[1]))
						continue; 

					int stopPos = Integer.parseInt(stop);
					int startPos = Integer.parseInt(start);

					a.getRolePartList().remove(startPos);
					a.getRolePartList().add(stopPos, rp);


					return;
				}
			}

		}		

	}



}
