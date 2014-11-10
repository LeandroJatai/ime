package br.edu.ifg.ime.controllers;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.imsglobal.jaxb.ld.Activities;
import org.imsglobal.jaxb.ld.ActivityStructure;
import org.imsglobal.jaxb.ld.ActivityStructureRef;
import org.imsglobal.jaxb.ld.Components;
import org.imsglobal.jaxb.ld.Environment;
import org.imsglobal.jaxb.ld.EnvironmentRef;
import org.imsglobal.jaxb.ld.LearningActivity;
import org.imsglobal.jaxb.ld.LearningActivityRef;
import org.imsglobal.jaxb.ld.RoleRef;
import org.imsglobal.jaxb.ld.SupportActivity;
import org.imsglobal.jaxb.ld.SupportActivityRef;
import org.imsglobal.jaxb.ld.UnitOfLearningHref;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.ld.LdProject;
import br.edu.ifg.ime.ld.LearningDesignRef;
import br.edu.ifg.ime.suport.LearningDesignUtils;
import br.edu.ifg.ime.suport.Suport;

public class ActivityController {

	public static void requestRemoveActivities(HttpServletRequest request) {

		Activities activities = getActivities(request);
		String identifier = Suport.r(request, "identifier");

		List<Serializable> lSers = activities.getLearningActivityOrSupportActivityOrActivityStructure();

		Serializable ser = null;

		for (Serializable serializable : lSers) {

			if (LearningDesignUtils.getIdentifier(serializable).equals(identifier)) {

				lSers.remove(serializable);

				ImeWorkspace.getImeWorkspace(request).excluirOfWorkspace(serializable);




				//excluir vinculos

				//LdEditorWorkspace.getLdPlayerWorkspace(request).notifyRemoveObject(identifier);
				return;
			}
		}
	}


	public static String requestUpDateSupportActivity(HttpServletRequest request) {

		Activities activities = getActivities(request);

		String identifier = Suport.r(request, "identifier");
		String titulo =  Suport.r(request, "titulo");
		String isvisible =  Suport.r(request, "isvisible");
		String parameters =  Suport.r(request, "parameters");

		String lEnvRef[] = request.getParameterValues("environment-ref"); 
		String lrolesRef[] = request.getParameterValues("papelSelecionado");

		if (identifier == null || identifier.length() == 0)
			return "Erro no identificador";

		SupportActivity sa = getSupportActivityByIdentifier(request, identifier);
		sa.setTitle(titulo);

		if (isvisible != null)
			sa.setIsvisible(null);
		else 
			sa.setIsvisible(false);

		if (parameters != null && parameters.length() > 0)
			sa.setParameters(parameters);
		else 
			sa.setParameters(null);



		if (UsuarioController.checkPermissao(request, "activities.view.sa.checkbox.environment")) {
			updateEnvironmentRef(request, lEnvRef, sa.getEnvironmentRefList(), sa);
		}

		if (UsuarioController.checkPermissao(request, "activities.view.sa.checkbox.roles")) {
			updateRolesRef(request, lrolesRef, sa.getRoleRefList(), sa);
		}

		return "";

	}

	public static String requestUpDateActivityStructure(HttpServletRequest request) {

		Activities activities = getActivities(request);

		String identifier = Suport.r(request, "identifier");
		String titulo = Suport.r(request, "titulo");
		String structureType = Suport.r(request, "structure-type");
		String sort = Suport.r(request, "sort");
		String numberToSelect = Suport.r(request, "number-to-select");


		String lEnvRef[] = request.getParameterValues("environment-ref"); 

		String lactivityRef[] = request.getParameterValues("activity-ref"); 

		if (identifier == null || identifier.length() == 0)
			return "Erro no identificador";

		ActivityStructure as = getActivityStructureByIdentifier(request, identifier);
		as.setTitle(titulo);
		as.setStructureType(structureType);
		as.setNumberToSelect(numberToSelect!=null&&numberToSelect.length()>0?new BigInteger(numberToSelect):null);


		if (UsuarioController.checkPermissao(request, "activities.view.as.checkbox.environment")) 
			updateEnvironmentRef(request, lEnvRef, as.getEnvironmentRef(), as);			

		if (UsuarioController.checkPermissao(request, "activities.view.as.checkbox.activities")) 
			updateActivityRef(request, lactivityRef, as.getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref(), as);			


		/*updateLaRef(request, lLaRef, as.getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref(), as);			
		updateSaRef(request, lSaRef, as.getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref(), as);			
		updateUolRef(request, lUolRef, as.getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref(), as);
		 */
		return "";
	}

	private static void updateActivityRef(HttpServletRequest request,
			String[] lActivityRef, List<Serializable> ldRefOlds, Object activity) {
		Iterator<Serializable> it = ldRefOlds.iterator();

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);



		for (Iterator iterator = ldRefOlds.iterator(); iterator.hasNext();) {
			Serializable activityRef = (Serializable) iterator.next();
			if (LearningDesignUtils.getRef(activityRef) == null) {
				iterator.remove();
			}
		}

		for (Object activityRef : ldRefOlds ) {
			String key = LearningDesignUtils.getIdentifier(LearningDesignUtils.getRef(activityRef));

			w.desvincularReferencia(
					key,
					LearningDesignUtils.getIdentifier(activity));
		}
		ldRefOlds.clear();


		if (lActivityRef != null)
			for (String strActivity : lActivityRef) {


				Object activityRef = w.getObject(strActivity);

				if (LearningDesignUtils.isLearningActivity(activityRef)) {
					LearningActivityRef ref = new LearningActivityRef();
					ref.setRef(activityRef);
					ldRefOlds.add(ref);
				} 
				else if (LearningDesignUtils.isSupportActivity(activityRef)) {
					SupportActivityRef ref = new SupportActivityRef();
					ref.setRef(activityRef);
					ldRefOlds.add(ref);
				} 
				else if (LearningDesignUtils.isActivityStructure(activityRef)) {
					ActivityStructureRef ref = new ActivityStructureRef();
					ref.setRef(activityRef);
					ldRefOlds.add(ref);
				} 			

				w.referenciar(strActivity, activity);				
			}
	}
	public static String requestUpDateLearningActivities(HttpServletRequest request) {

		Activities activities = getActivities(request);

		String identifier = Suport.r(request, "identifier");
		String titulo =  Suport.r(request, "titulo");
		String isvisible =  Suport.r(request, "isvisible");
		String parameters =  Suport.r(request, "parameters");

		String lEnvRef[] = request.getParameterValues("environment-ref"); 

		if (identifier == null || identifier.length() == 0)
			return "Erro no identificador";

		LearningActivity la = getLearningActivityByIdentifier(request, identifier);
		la.setTitle(titulo);

		if (isvisible != null)
			la.setIsvisible(null);
		else 
			la.setIsvisible(false);

		if (parameters != null && parameters.length() > 0)
			la.setParameters(parameters);
		else 
			la.setParameters(null);


		if (UsuarioController.checkPermissao(request, "activities.view.la.checkbox.environment"))
			updateEnvironmentRef(request, lEnvRef, la.getEnvironmentRefList(), la);
		return "";

	}


	private static void updateEnvironmentRef(HttpServletRequest request,
			String[] ldNovosReferenciados, List<EnvironmentRef> ldRefOlds, Object activity) {

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);


		for (Iterator iterator = ldRefOlds.iterator(); iterator.hasNext();) {
			EnvironmentRef environmentRef = (EnvironmentRef) iterator.next();
			if (LearningDesignUtils.getRef(environmentRef) == null) {
				iterator.remove();
			}
		}

		for (Object lEnvRef : ldRefOlds ) {
			String key = LearningDesignUtils.getIdentifier(LearningDesignUtils.getRef(lEnvRef));

			w.desvincularReferencia(
					key,
					LearningDesignUtils.getIdentifier(activity));
		}
		ldRefOlds.clear();

		if (ldNovosReferenciados != null)
			for (String strEnvRef : ldNovosReferenciados) {

				Environment env = EnvironmentsController.getEnviromentByIdentifier(request, strEnvRef);

				EnvironmentRef envRef = new EnvironmentRef();
				envRef.setRef(env);

				ldRefOlds.add(envRef);


				w.referenciar(strEnvRef, activity);				
			}
	}

	private static void updateRolesRef(HttpServletRequest request,
			String[] lRoleRef, List<RoleRef> lRefs, Object activity) {

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);



		for (Iterator iterator = lRefs.iterator(); iterator.hasNext();) {
			RoleRef roleRef = (RoleRef) iterator.next();
			if (LearningDesignUtils.getRef(roleRef) == null) {
				iterator.remove();
			}
		}
		Iterator<RoleRef> it = lRefs.iterator();

		while (it.hasNext()) {
			RoleRef roleRef = null;
			roleRef = it.next();
			if (!(roleRef instanceof RoleRef))
				continue;

			w.desvincularReferencia(
					LearningDesignUtils.getIdentifier(LearningDesignUtils.getRef(roleRef)),
					LearningDesignUtils.getIdentifier(activity));

			it.remove();
		}

		if (lRoleRef != null)
			for (String strRoleRef : lRoleRef) {

				Object ob = w.getObject(strRoleRef);

				RoleRef roleRef = new RoleRef();
				roleRef.setRef(ob);

				lRefs.add(roleRef);


				w.referenciar(strRoleRef, activity);				
			}
	}

	public static String requestIncludeActivities(HttpServletRequest request) {

		Activities activities = getActivities(request);

		String titulo =  Suport.r(request, "titulo");
		String tipo = Suport.r(request, "tipo");

		if (tipo.equals("activity-structure")){

			ActivityStructure as = new ActivityStructure();
			as.setIdentifier(ImeWorkspace.getImeWorkspace(request).newIdentifier(as, "activity-as"));
			as.setTitle(titulo);
		
			activities.getLearningActivityOrSupportActivityOrActivityStructure().add(as);
			return as.getIdentifier();
		}
		else if (tipo.equals("learning-activity")) {

			LearningActivity la = new LearningActivity();
			la.setIdentifier(ImeWorkspace.getImeWorkspace(request).newIdentifier(la, "activity-la"));
			la.setTitle(titulo);
			la.setIsvisible(true);
			activities.getLearningActivityOrSupportActivityOrActivityStructure().add(la);
			return la.getIdentifier();
		}
		else if (tipo.equals("support-activity")){			
			SupportActivity sa = new SupportActivity();
			sa.setIdentifier(ImeWorkspace.getImeWorkspace(request).newIdentifier(sa, "activity-sa"));
			sa.setTitle(titulo);
			sa.setIsvisible(true);
			activities.getLearningActivityOrSupportActivityOrActivityStructure().add(sa);
			return sa.getIdentifier();
		}
		return "";
	}


	public static SupportActivity getSupportActivityByIdentifier(HttpServletRequest request, String identifier) {

		Activities activities = getActivities(request);

		List<Serializable> lActivities = activities.getLearningActivityOrSupportActivityOrActivityStructure();				
		for (Serializable ser : lActivities) {

			if (!LearningDesignUtils.isSupportActivity(ser))
				continue;

			SupportActivity sa = (SupportActivity) ser;

			if (sa.getIdentifier().equals(identifier))
				return sa;
		}			
		return null;
	}

	public static ActivityStructure getActivityStructureByIdentifier(HttpServletRequest request, String identifier) {

		Activities activities = getActivities(request);

		List<Serializable> lActivities = activities.getLearningActivityOrSupportActivityOrActivityStructure();				
		for (Serializable ser : lActivities) {

			if (!LearningDesignUtils.isActivityStructure(ser))
				continue;

			ActivityStructure as = (ActivityStructure) ser;

			if (as.getIdentifier().equals(identifier))
				return as;
		}			
		return null;
	}


	public static LearningActivity getLearningActivityByIdentifier(HttpServletRequest request, String identifier) {

		Activities activities = getActivities(request);

		List<Serializable> lActivities = activities.getLearningActivityOrSupportActivityOrActivityStructure();				
		for (Serializable ser : lActivities) {

			if (!LearningDesignUtils.isLearningActivity(ser))
				continue;

			LearningActivity la = (LearningActivity) ser;

			if (la.getIdentifier().equals(identifier))
				return la;
		}			
		return null;
	}

	public static Activities getActivities(HttpServletRequest request) {

		Components componentes =  ImeWorkspace.getImeWorkspace(request).getLdProject(request).getLd().getComponents();

		if (componentes.getActivities() == null) {
			componentes.setActivities(new Activities());
		}

		return componentes.getActivities();		
	}
	/*
	public static Activities getActivitiesOfJob(HttpServletRequest request) {

		Components componentes = ComponentsController.getComponentsOfJob(request);

		if (componentes.getActivities() == null) {
			componentes.setActivities(new Activities());
		}

		return componentes.getActivities();		
	}
	 */

	public static Activities getActivities(LdProject ldep) {

		Components componentes = ComponentsController.getComponents(ldep);

		if (componentes.getActivities() == null) {
			componentes.setActivities(new Activities());
		}

		return componentes.getActivities();		
	}

}
