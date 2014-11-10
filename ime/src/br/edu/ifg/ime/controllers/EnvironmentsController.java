package br.edu.ifg.ime.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.imsglobal.jaxb.ld.Components;
import org.imsglobal.jaxb.ld.Conference;
import org.imsglobal.jaxb.ld.ConferenceManager;
import org.imsglobal.jaxb.ld.EmailData;
import org.imsglobal.jaxb.ld.Environment;
import org.imsglobal.jaxb.ld.EnvironmentRef;
import org.imsglobal.jaxb.ld.Environments;
import org.imsglobal.jaxb.ld.Item;
import org.imsglobal.jaxb.ld.LearningObject;
import org.imsglobal.jaxb.ld.Moderator;
import org.imsglobal.jaxb.ld.Observer;
import org.imsglobal.jaxb.ld.Participant;
import org.imsglobal.jaxb.ld.RoleRef;
import org.imsglobal.jaxb.ld.SendMail;
import org.imsglobal.jaxb.ld.Service;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.ld.LdProject;
import br.edu.ifg.ime.ld.interfaces.Role;
import br.edu.ifg.ime.suport.LearningDesignUtils;
import br.edu.ifg.ime.suport.Suport;

public class EnvironmentsController {

	public static void requestRemoveEnvironment(HttpServletRequest request) {

		Environments envs = getEnvironments(request);
		String identifier = Suport.r(request, "identifier");
		String identifierLo = Suport.r(request, "identifier-lo");


		Environment env = getEnviromentByIdentifier(request, identifier);

		if (env != null && identifierLo == null) {
			envs.getEnvironmentList().remove(env);
			if (envs.getEnvironmentList().size() == 0)
				envs.environmentList = null;
			ImeWorkspace.getImeWorkspace(request).excluirOfWorkspace(env);
		}
		else {
			Object ob = ImeWorkspace.getImeWorkspace(request).getObject(identifierLo);
			env.getLearningObjectOrServiceOrEnvironmentRef().remove(ob);
			ImeWorkspace.getImeWorkspace(request).excluirOfWorkspace((Serializable)ob);
		}

	}

	public static String requestUpDateService(HttpServletRequest request) throws IOException {

		String type = Suport.r(request, "type");
		String ldep = Suport.r(request, "ldep");
		String idEnv = Suport.r(request, "id-env");
		String identifier = Suport.r(request, "identifier");	

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);

		Environment env = EnvironmentsController.getEnviromentByIdentifier(request, idEnv);
		Service sv = null;

		if (identifier == null || identifier.length() == 0) {
			sv = new Service();
			sv.setIdentifier(w.newIdentifier(sv, "sv"));
			env.getLearningObjectOrServiceOrEnvironmentRef().add(sv);
		}
		else {
			sv = (Service) w.getObject(identifier);	    	
		}

		sv.setParameters(Suport.r(request, "sv.parameters"));
		sv.setIsvisible(Suport.r(request, "sv.isvisible") == null?false:null);


		String clazz = (String)Suport.r(request, "sv.clazz");
		sv.restartClazz();
		if (clazz != null && clazz.length() > 0) {
			String clazzs[] = clazz.trim().split(" ");
			sv.restartClazz();
			for (String c : clazzs)
				sv.getClazz().add(c);
		}

		if (type.equals(Service.TYPE_SENDMAIL)) {
			sv.setConference(null);
			sv.setGameService(null);
			sv.setIndexSearch(null);
			sv.setMonitor(null);

			SendMail sm = null;
			if (sv.getSendMail() == null) {
				sm = new SendMail();
				sv.setSendMail(sm);
			}
			else
				sm = sv.getSendMail();

			String title = Suport.r(request, "sv.title");
			String select = Suport.r(request, "sv.select");

			sm.setTitle(title);
			sm.setSelect(select);

			String papelSelecionado[] = Suport.rs(request, "papelSelecionado");

			//Remover emailData para papeis desmarcados
			for (Iterator<EmailData> itEd = sm.getEmailDataList().iterator(); itEd.hasNext();) {

				EmailData ed = itEd.next();

				if (ed.getRoleRef() == null) {
					itEd.remove();
					continue;
				}
				RoleRef rr = ed.getRoleRef();

				if (rr.getRef() == null) {
					itEd.remove();
					continue;
				}

				String idRole = LearningDesignUtils.getIdentifier(rr.getRef());
				boolean flagRoleEncontrada = false; 
				if (papelSelecionado != null)
				for (int i = 0; i < papelSelecionado.length; i++) {

					if (papelSelecionado[i] == null)
						continue;

					if (papelSelecionado[i].equals(idRole)) {
						papelSelecionado[i] = null;
						flagRoleEncontrada = true;
						break;
					}
				}

				if (!flagRoleEncontrada) {
					itEd.remove();

					w.desvincularReferencia(idRole, identifier);

					continue;					
				}

			}
			if (papelSelecionado != null)
			for (int i = 0; i < papelSelecionado.length; i++) {

				if (papelSelecionado[i] == null)
					continue;


				EmailData ed = new EmailData();
				sm.getEmailDataList().add(ed);
				ed.setRoleRef(new RoleRef());

				Object ob = w.getObject(papelSelecionado[i]);

				ed.getRoleRef().setRef(ob);		


				w.referenciar(papelSelecionado[i], sv);
			}
		}
		else if (type.equals(Service.TYPE_CONFERENCE)) {
			sv.setGameService(null);
			sv.setIndexSearch(null);
			sv.setMonitor(null);
			sv.setSendMail(null);


			Conference conf = null;

			if (sv.getConference() == null) {
				conf = new Conference();
				sv.setConference(conf);
			}
			else
				conf = sv.getConference();

			String title = Suport.r(request, "sv.title");
			String svConfType = Suport.r(request, "sv.type.conference");

			conf.setTitle(title);
			conf.setConferenceType(svConfType);





			String participantes[] = Suport.rs(request, "participantes");			
			//Remover participantes para papeis desmarcados
			for (Iterator<Participant> itPart = conf.getParticipantList().iterator(); itPart.hasNext();) {

				Participant participante = itPart.next();

				if (participante.getRoleRef() == null) {
					itPart.remove();
					continue;
				}
				Role rr = (Role)participante.getRoleRef();

				if (rr  == null) {
					itPart.remove();
					continue;
				}

				String idRole = rr.getIdentifier();
				boolean flagRoleEncontrada = false; 

				if (participantes != null)
					for (int i = 0; i < participantes.length; i++) {

						if (participantes[i] == null)
							continue;

						if (participantes[i].equals(idRole)) {
							participantes[i] = null;
							flagRoleEncontrada = true;
							break;
						}
					}

				if (!flagRoleEncontrada) {
					itPart.remove();

					w.desvincularReferencia(idRole, identifier);

					continue;					
				}								
			}

			if (participantes != null)
				for (int i = 0; i < participantes.length; i++) {

					if (participantes[i] == null)
						continue;


					Participant participante = new Participant();
					conf.getParticipantList().add(participante);

					participante.setRoleRef(w.getObject(participantes[i]));

					w.referenciar(participantes[i], sv);
				}


			String observers[] = Suport.rs(request, "observers");			
			//Remover observers para papeis desmarcados
			for (Iterator<Observer> itObs = conf.getObserverList().iterator(); itObs.hasNext();) {

				Observer obs = itObs.next();

				if (obs.getRoleRef() == null) {
					itObs.remove();
					continue;
				}
				Role rr = (Role)obs.getRoleRef();

				if (rr  == null) {
					itObs.remove();
					continue;
				}

				String idRole = rr.getIdentifier();
				boolean flagRoleEncontrada = false; 

				if (observers != null)
					for (int i = 0; i < observers.length; i++) {

						if (observers[i] == null)
							continue;

						if (observers[i].equals(idRole)) {
							observers[i] = null;
							flagRoleEncontrada = true;
							break;
						}
					}

				if (!flagRoleEncontrada) {
					itObs.remove();					
					w.desvincularReferencia(idRole, identifier);					
					continue;					
				}

			}

			if (observers != null)
				for (int i = 0; i < observers.length; i++) {

					if (observers[i] == null)
						continue;				

					Observer obs = new Observer();
					conf.getObserverList().add(obs);

					obs.setRoleRef(w.getObject(observers[i]));

					w.referenciar(observers[i], sv);
				}



			//Gerente da Conferência
			String confManager = Suport.r(request, "conf.manager");
			if (confManager == null) {
				if (conf.getConferenceManager() != null && conf.getConferenceManager().getRoleRef() != null)
					w.desvincularReferencia(LearningDesignUtils.getIdentifier(conf.getConferenceManager().getRoleRef()), identifier);

				conf.setConferenceManager(null);

			}
			else {
				ConferenceManager cm = conf.getConferenceManager();
				
				if (cm == null) {
					cm = new ConferenceManager();
					conf.setConferenceManager(cm);
				}
					
				if (cm.getRoleRef() != null) {
					if (!LearningDesignUtils.getIdentifier(cm.getRoleRef()).equals(confManager)) {
						w.desvincularReferencia(LearningDesignUtils.getIdentifier(cm.getRoleRef()), identifier);
					}
				}
				
				Role rr = (Role)w.getObject(confManager);
				cm.setRoleRef(rr);;
				
				w.referenciar(confManager, sv);				
			}
			
			//Moderador
			String moderator = Suport.r(request, "moderator");
			if (moderator == null) {
				if (conf.getModerator() != null && conf.getModerator().getRoleRef() != null)
					w.desvincularReferencia(LearningDesignUtils.getIdentifier(conf.getModerator().getRoleRef()), identifier);

				conf.setModerator(null);

			}
			else {
				Moderator mod = conf.getModerator();
				
				if (mod == null) {
					mod = new Moderator();
					conf.setModerator(mod);
				}
					
				if (mod.getRoleRef() != null) {
					if (!LearningDesignUtils.getIdentifier(mod.getRoleRef()).equals(moderator)) {
						w.desvincularReferencia(LearningDesignUtils.getIdentifier(mod.getRoleRef()), identifier);
					}
				}
				
				Role rr = (Role)w.getObject(moderator);
				mod.setRoleRef(rr);;
				
				w.referenciar(moderator, sv);				
			}




		}
		return sv.getIdentifier();

	}

	public static String requestUpDateEnvironment(HttpServletRequest request) {

		Environments envs = getEnvironments(request);

		String titulo =  Suport.r(request, "titulo");
		String tipo = Suport.r(request, "tipo");
		String identifier = Suport.r(request, "identifier");
		String envRef[] = Suport.rs(request, "environment-ref");

		if (identifier == null || identifier.length() == 0) {

			Environment env = new Environment();
			env.setTitle(titulo);
			env.setIdentifier(ImeWorkspace.getImeWorkspace(request).newIdentifier(env, "env"));			
			envs.getEnvironmentList().add(env);

			identifier = env.getIdentifier();
		}
		else {

			Environment env = getEnviromentByIdentifier(request, identifier);

			if (titulo != null)
				env.setTitle(titulo);

			if (envRef != null) {
				for (Iterator<Object> it = env.getLearningObjectOrServiceOrEnvironmentRef().iterator(); it.hasNext();) {
					Object ob = it.next();
					if (ob instanceof EnvironmentRef) {
						it.remove();
					}
				}
				for (String sEnv: envRef) {
					EnvironmentRef eRef = new EnvironmentRef();
					eRef.setRef(getEnviromentByIdentifier(request, sEnv));
					env.getLearningObjectOrServiceOrEnvironmentRef().add(eRef);
				}
			}
		}
		return identifier;
	}




	public static Environment getEnviromentByIdentifier(HttpServletRequest request, String identifier) {

		Environments envs = getEnvironments(request);

		List<Environment> lEnvs = envs.getEnvironmentList();				
		for (Environment env : lEnvs) {		
			if (env.getIdentifier().equals(identifier))
				return env;
		}			
		return null;
	}

	public synchronized static Environments getEnvironments(HttpServletRequest request) {

		Components componentes =  ImeWorkspace.getImeWorkspace(request).getLdProject(request).getLd().getComponents();

		if (componentes.getEnvironments() == null) {
			componentes.setEnvironments(new Environments());
		}

		return componentes.getEnvironments();		
	}

	public synchronized static Environments getEnvironments(LdProject ldep) {

		Components componentes = ComponentsController.getComponents(ldep);

		if (componentes.getEnvironments() == null) {
			componentes.setEnvironments(new Environments());
		}

		return componentes.getEnvironments();		
	}
	public synchronized static Environments prepareEnvironmentsForExportLD(LdProject ldep) {

		Components componentes = ComponentsController.getComponents(ldep);

		if (componentes.getEnvironments() != null && componentes.getEnvironments().getEnvironmentList().size() == 0) {
			componentes.setEnvironments(null);
		}

		return componentes.getEnvironments();		
	}

	public synchronized static List<Service> getServices(Environment ob) {


		Environment env = (Environment) ob;

		List<Service> lResult = new ArrayList<Service>();

		if (env == null)
			return lResult;

		for (Object obItem: env.getLearningObjectOrServiceOrEnvironmentRef()) {

			if (obItem instanceof Service)
				lResult.add((Service) obItem);				
		}
		return lResult;

	}
	public synchronized static List<LearningObject> getLearningObjects(Environment ob) {


		Environment env = (Environment) ob;

		List<LearningObject> lResult = new ArrayList<LearningObject>();

		if (env == null)
			return lResult;

		for (Object obItem: env.getLearningObjectOrServiceOrEnvironmentRef()) {

			if (obItem instanceof LearningObject)
				lResult.add((LearningObject) obItem);				
		}
		return lResult;

	}
	public synchronized static List<EnvironmentRef> getEnviromentsRef(Object ob) {


		if (ob instanceof Environment) {
			Environment env = (Environment) ob;

			List<EnvironmentRef> lResult = new ArrayList<EnvironmentRef>();

			for (Object obItem: env.getLearningObjectOrServiceOrEnvironmentRef()) {

				if (obItem instanceof EnvironmentRef)
					lResult.add((EnvironmentRef) obItem);				
			}
			return lResult;
		}
		else {

			if (ob == null)
				return new ArrayList<EnvironmentRef>();

			Field[] lFields = ob.getClass().getDeclaredFields();   

			for (Field f : lFields) {

				//	System.out.format("Type: %s%n", f.getType());
				//System.out.println(f.getGenericType().toString());

				if (f.getGenericType().toString().equals("java.util.List<org.imsglobal.jaxb.ld.EnvironmentRef>")) {

					try {
						return 	(List<EnvironmentRef>) ob.getClass().getMethod(  Suport.mGetOfField(f) ).invoke(ob);
					} catch (Exception e) {
						return null;
					}


				}
			}
		}
		return null;
	}

	public static void requestRemoveLoServices(HttpServletRequest request) {

		String identifier = Suport.r(request, "identifier");
		String idEnv = Suport.r(request, "id-env");


		Environment env = getEnviromentByIdentifier(request, idEnv);

		if (env != null && identifier != null) {
			Object ob = ImeWorkspace.getImeWorkspace(request).getObject(identifier);
			env.getLearningObjectOrServiceOrEnvironmentRef().remove(ob);
			ImeWorkspace.getImeWorkspace(request).excluirOfWorkspace((Serializable)ob);
		}

	}

	public static String requestUpDateServiceItemConference(
			HttpServletRequest request, TreeMap<String, Object> lParams) {
		

		String identifier = Suport.r(request, "identifier");
		String idEnv = Suport.r(request, "id-env");

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
		Environment env = getEnviromentByIdentifier(request, idEnv);
		
		Service sv = null;

		if (identifier == null || identifier.length() == 0) {
			return "";
		}
		else {
			sv = (Service) w.getObject(identifier);	    	
		}
		
		if (sv.getConference() == null)
			return "";
		
		Conference conf = sv.getConference();
		Item item = conf.getItem();
		
		if (item == null) {
			item = new Item();
			item.setIdentifier(w.newIdentifier(item, "item"));
			conf.setItem(item);
			w.referenciar(item.getIdentifier(), sv);
		}

		ItemModelController.fillItem(item, w, lParams);
		
		return null;
	}

}
