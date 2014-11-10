package br.edu.ifg.ime.controllers;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.imsglobal.jaxb.ld.Act;
import org.imsglobal.jaxb.ld.Activities;
import org.imsglobal.jaxb.ld.Components;
import org.imsglobal.jaxb.ld.LearningDesign;
import org.imsglobal.jaxb.ld.Learner;
import org.imsglobal.jaxb.ld.Method;
import org.imsglobal.jaxb.ld.Play;
import org.imsglobal.jaxb.ld.Roles;
import org.imsglobal.jaxb.ld.Staff;

import com.sun.org.apache.xml.internal.security.encryption.AgreementMethod;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.dao.DAOConnection;
import br.edu.ifg.ime.dto.Usuario;
import br.edu.ifg.ime.ld.LdProject;
import br.edu.ifg.ime.ld.LearningDesignRef;
import br.edu.ifg.ime.suport.LearningDesignUtils;
import br.edu.ifg.ime.suport.Suport;

public class PlaysController {

	public static Play getPlayByIdentifier(HttpServletRequest request, String identifier) {

		List<Play> lPlays = getPlays(request);

		for (Play play : lPlays) {		
			if (play.getIdentifier().equals(identifier))
				return play;
		}		

		return null;
	}
	public static Play getPlayOfAct(HttpServletRequest request, Act act) {

		List<Play> lPlays = getPlays(request);

		for (Play play : lPlays) {		
			for (Act a : play.getActList())

				if (a.getIdentifier().equals(act.getIdentifier()))
					return play;
		}		

		return null;
	}

	public static Play newPlay(HttpServletRequest request) {

		List<Play> lPlays= getPlays(request);

		Play play = new Play();
		play.setIdentifier(ImeWorkspace.getImeWorkspace(request).newIdentifier(play, "play"));			
		lPlays.add(play);

		play.setTitle(play.getIdentifier());

		return play;
	}

	public static String requestUpDatePlay(HttpServletRequest request) {

		//Method m = MethodController.getMethodOfJob(request);

		List<Play> lPlays= getPlays(request);

		String titulo =  Suport.r(request, "titulo");
		String identifier = Suport.r(request, "identifier");
		String isvisible = Suport.r(request, "isvisible");

		String ldRef[] = request.getParameterValues("ld-ref");

		if (identifier != null && identifier.length() == 0) {

			Play play = new Play();
			play.setTitle(titulo);
			play.setIsvisible(isvisible==null?new Boolean(false):null);
			play.setIdentifier(ImeWorkspace.getImeWorkspace(request).newIdentifier(play, "play"));			
			lPlays.add(play);


			//updateLearningDesignRef(request, ldRef, play.getLearningDesignRef(), play);


			identifier = play.getIdentifier();
		}
		else {

			Play play = getPlayByIdentifier(request, identifier);
			play.setTitle(titulo);
			play.setIsvisible(isvisible==null?new Boolean(false):null);

			//updateLearningDesignRef(request, ldRef, play.getLearningDesignRef(), play);

			//play.getActList().clear();
		}
		return identifier;
	}
 
	public static void requestRemovePlay(HttpServletRequest request) {


		List<Play> lPlays= getPlays(request);

		String identifier = Suport.r(request, "identifier");

		for (Play play : lPlays) {

			if (play.getIdentifier().equals(identifier)) {
				lPlays.remove(play);

				//LdEditorWorkspace.getLdPlayerWorkspace(request).notifyRemoveObject(identifier);
				return;
			}
		}
	}

	public static List<Play> getPlays(HttpServletRequest request) {

		Method m =  MethodController.getMethod(ImeWorkspace.getImeWorkspace(request).getLdProject(request));		
		return m.getPlayList();		
	}
	public static List<Play> getPlays(LdProject ldep) {

		Method m =  MethodController.getMethod(ldep);		
		return m.getPlayList();		
	}

	public static void switchPosActs(ImeWorkspace w, String[] params, String start, String stop) {

		LdProject ldep = w.getLdProjectById(params[5]);
		
		for (Play play : ldep.getLd().getMethod().getPlayList()) {		


			for (Act a : play.getActList()) {

				if (a.getId() != Long.parseLong(params[1]))
					continue; 

				int stopPos = Integer.parseInt(stop);
				int startPos = Integer.parseInt(start);
				
				play.getActList().remove(startPos);
				play.getActList().add(stopPos, a);
				

				return;
			}

		}		

	}
	public static void switchPosPlays(ImeWorkspace w, String[] params, String start, String stop) {

		LdProject ldep = w.getLdProjectById(params[3]);
		
		for (Play play : ldep.getLd().getMethod().getPlayList()) {		

			if (play.getId() != Long.parseLong(params[1]))
					continue; 

				int stopPos = Integer.parseInt(stop);
				int startPos = Integer.parseInt(start);
				
				ldep.getLd().getMethod().getPlayList().remove(startPos);
				ldep.getLd().getMethod().getPlayList().add(stopPos, play);
				

				return;		

		}		

	}
}
