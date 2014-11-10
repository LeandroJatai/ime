package br.edu.ifg.ime.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.imsglobal.jaxb.content.File;
import org.imsglobal.jaxb.content.Resource;
import org.imsglobal.jaxb.ld.Act;
import org.imsglobal.jaxb.ld.CompleteAct;
import org.imsglobal.jaxb.ld.CompleteActivity;
import org.imsglobal.jaxb.ld.CompletePlay;
import org.imsglobal.jaxb.ld.CompleteUnitOfLearning;
import org.imsglobal.jaxb.ld.Item;
import org.imsglobal.jaxb.ld.ItemModel;
import org.imsglobal.jaxb.ld.Learner;
import org.imsglobal.jaxb.ld.LearningActivity;
import org.imsglobal.jaxb.ld.LearningDesign;
import org.imsglobal.jaxb.ld.Method;
import org.imsglobal.jaxb.ld.Play;
import org.imsglobal.jaxb.ld.RolePart;
import org.imsglobal.jaxb.ld.Staff;
import org.imsglobal.jaxb.ld.TimeLimit;
import org.imsglobal.jaxb.ld.UserChoice;
import org.imsglobal.jaxb.ld.WhenLastActCompleted;
import org.imsglobal.jaxb.ld.WhenPlayCompleted;
import org.imsglobal.jaxb.ld.WhenRolePartCompleted;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.ld.LdProject;
import br.edu.ifg.ime.suport.Suport;

public class CompletesController {

	public static void requestUpDateCompleteActivities(HttpServletRequest request) {

		String idRef = Suport.r(request, "id-ref");
		String field = Suport.r(request, "field");

		String complete = Suport.r(request, "complete");

		String tempo[] = request.getParameterValues("tempo");

		Object ob = ImeWorkspace.getImeWorkspace(request).getObject(idRef);

		try {
			CompleteActivity ca = (CompleteActivity) ob.getClass().getMethod("getCompleteActivity").invoke(ob);

			//TODO: Implementar WhenPropertyValueIsSetList neste ponto de comentario 

			if (ca == null && complete.equals("undefined")) {	

			}
			else if (complete.equals("undefined")) {
				ob.getClass().getMethod("setCompleteActivity", Class.forName(CompleteActivity.class.getCanonicalName())).invoke(ob, new Object[] { null });					
			}
			else if (complete.equals("user-choice")) {

				if (ca == null) {
					ca = new CompleteActivity();
				}
				ca.setTimeLimit(null);
				ca.setUserChoice(new UserChoice());
				ob.getClass().getMethod("setCompleteActivity", Class.forName(CompleteActivity.class.getCanonicalName())).invoke(ob, new Object[] { ca });			

				return;
			}
			else if (complete.equals("time-limit")) {

				if (ca == null) {
					ca = new CompleteActivity();
				}
				ca.setUserChoice(null);
				TimeLimit tl = new TimeLimit();
				ca.setTimeLimit(tl);

				String strDuration = "P";
				strDuration += tempo[0].length() > 0 ? tempo[0] + "Y": "";
				strDuration += tempo[1].length() > 0 ? tempo[1] + "M": "";
				strDuration += tempo[2].length() > 0 ? tempo[2] + "D": "";
				strDuration += tempo[3].length() > 0 || tempo[4].length() > 0 || tempo[5].length() > 0 ? "T":"";
				strDuration += tempo[3].length() > 0 ? tempo[3] + "H": "";
				strDuration += tempo[4].length() > 0 ? tempo[4] + "M": "";
				strDuration += tempo[5].length() > 0 ? tempo[5] + "S": "";


				Duration duration = DatatypeFactory.newInstance().newDuration(strDuration);
				tl.setValue(duration);

				ob.getClass().getMethod("setCompleteActivity", Class.forName(CompleteActivity.class.getCanonicalName())).invoke(ob, new Object[] { ca });			

				return;
			}

		} catch (Exception e) {
			return;
		} 
	}

	public static void requestUpDateCompleteActs(HttpServletRequest request) {

		String idRef = Suport.r(request, "id-ref");
		String field = Suport.r(request, "field");

		String complete = Suport.r(request, "complete");

		String tempo[] = request.getParameterValues("tempo");
		String rpRef[] = request.getParameterValues("rp-ref");

		//Object ob = LdEditorWorkspace.getLdEditorWorkspace(request).getObject(idRef);

		Act act = ActsController.getActByIdentifier(request, idRef);

		try {
			CompleteAct ca = act.getCompleteAct();

			//TODO: Implementar WhenPropertyValueIsSetList neste ponto de comentario 

			if (ca == null && complete.equals("undefined")) {	

			}
			else if (complete.equals("undefined")) {
				act.setCompleteAct(null);					
			}

			else if (complete.equals("role-part")) {

				if (ca == null) {
					ca = new CompleteAct();
				}
				act.setCompleteAct(ca);		
				ca.getWhenRolePartCompletedList().clear();
				ca.setTimeLimit(null);

				if (rpRef.length == 0)
					act.setCompleteAct(null);

				for (String idRP: rpRef) {

					RolePart rp = (RolePart) ImeWorkspace.getImeWorkspace(request).getObject(idRP);

					WhenRolePartCompleted wrpc = new WhenRolePartCompleted();
					wrpc.setRef(rp);

					ca.getWhenRolePartCompletedList().add(wrpc);


				}




			}

			else if (complete.equals("time-limit")) {

				if (ca == null) {
					ca = new CompleteAct();
				}
				TimeLimit tl = new TimeLimit();
				ca.setTimeLimit(tl);
				ca.getWhenRolePartCompletedList().clear();

				String strDuration = "P";
				strDuration += tempo[0].length() > 0 ? tempo[0] + "Y": "";
				strDuration += tempo[1].length() > 0 ? tempo[1] + "M": "";
				strDuration += tempo[2].length() > 0 ? tempo[2] + "D": "";
				strDuration += tempo[3].length() > 0 || tempo[4].length() > 0 || tempo[5].length() > 0 ? "T":"";
				strDuration += tempo[3].length() > 0 ? tempo[3] + "H": "";
				strDuration += tempo[4].length() > 0 ? tempo[4] + "M": "";
				strDuration += tempo[5].length() > 0 ? tempo[5] + "S": "";


				if (!strDuration.equals("P")) {
					Duration duration = DatatypeFactory.newInstance().newDuration(strDuration);
					tl.setValue(duration);
					act.setCompleteAct(ca);			
				} 
				else {
					act.setCompleteAct(null);
				}


				return;
			}

		} catch (Exception e) {
			return;
		}




	}
	public static void requestUpDateCompletePlays(HttpServletRequest request) {

		String idRef = Suport.r(request, "id-ref");
		String field = Suport.r(request, "field");

		String complete = Suport.r(request, "complete");

		String tempo[] = request.getParameterValues("tempo");

		//Object ob = LdEditorWorkspace.getLdEditorWorkspace(request).getObject(idRef);

		Play play = PlaysController.getPlayByIdentifier(request, idRef);

		try {
			CompletePlay cp = play.getCompletePlay();


			//TODO: Implementar WhenPropertyValueIsSetList neste ponto de comentario 

			if (cp == null && complete.equals("undefined")) {	

			}
			else if (complete.equals("undefined")) {
				play.setCompletePlay(null);					
			}

			else if (complete.equals("when-act-last-completed")) {

				if (cp == null) {
					cp = new CompletePlay();
				}
				play.setCompletePlay(cp);		
				cp.setWhenLastActCompleted(new WhenLastActCompleted());
				cp.setTimeLimit(null);

			}

			else if (complete.equals("time-limit")) {

				if (cp == null) {
					cp = new CompletePlay();
				}
				TimeLimit tl = new TimeLimit();
				cp.setTimeLimit(tl);
				cp.setWhenLastActCompleted(null);

				String strDuration = "P";
				strDuration += tempo[0].length() > 0 ? tempo[0] + "Y": "";
				strDuration += tempo[1].length() > 0 ? tempo[1] + "M": "";
				strDuration += tempo[2].length() > 0 ? tempo[2] + "D": "";
				strDuration += tempo[3].length() > 0 || tempo[4].length() > 0 || tempo[5].length() > 0 ? "T":"";
				strDuration += tempo[3].length() > 0 ? tempo[3] + "H": "";
				strDuration += tempo[4].length() > 0 ? tempo[4] + "M": "";
				strDuration += tempo[5].length() > 0 ? tempo[5] + "S": "";

				if (!strDuration.equals("P")) {
					Duration duration = DatatypeFactory.newInstance().newDuration(strDuration);
					tl.setValue(duration);
					play.setCompletePlay(cp);		
				}
				else
					play.setCompletePlay(null);

				return;
			}

		} catch (Exception e) {
			return;
		}




	}

	public static void requestUpDateCompleteUols(HttpServletRequest request) {

		String idRef = Suport.r(request, "ldep");
		String field = Suport.r(request, "field");

		String complete = Suport.r(request, "complete");

		String tempo[] = request.getParameterValues("tempo");
		String playRef[] = request.getParameterValues("play-ref");

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);


		LdProject ldep = w.getLdProjectByIdentifier(idRef);
		Method m = MethodController.getMethod(ldep);


		try {
			CompleteUnitOfLearning cUol = m.getCompleteUnitOfLearning();

			//TODO: Implementar WhenPropertyValueIsSetList neste ponto de comentario 

			if (cUol == null && complete.equals("undefined")) {	

			}
			else if (complete.equals("undefined")) {
				m.setCompleteUnitOfLearning(null);					
			}

			else if (complete.equals("play")) {

				if (cUol == null) {
					cUol = new CompleteUnitOfLearning();
				}
				m.setCompleteUnitOfLearning(cUol);		
				cUol.getWhenPlayCompletedList().clear();
				cUol.setTimeLimit(null);

				if (playRef.length == 0)
					m.setCompleteUnitOfLearning(null);;
				
				for (String idPlay: playRef) {

					Play p = (Play) ImeWorkspace.getImeWorkspace(request).getObject(idPlay);

					WhenPlayCompleted wpc = new WhenPlayCompleted();
					wpc.setRef(p);

					cUol.getWhenPlayCompletedList().add(wpc);


				}




			}

			else if (complete.equals("time-limit")) {

				if (cUol == null) {
					cUol = new CompleteUnitOfLearning();
				}
				TimeLimit tl = new TimeLimit();
				cUol.setTimeLimit(tl);
				cUol.getWhenPlayCompletedList().clear();

				String strDuration = "P";
				strDuration += tempo[0].length() > 0 ? tempo[0] + "Y": "";
				strDuration += tempo[1].length() > 0 ? tempo[1] + "M": "";
				strDuration += tempo[2].length() > 0 ? tempo[2] + "D": "";
				strDuration += tempo[3].length() > 0 || tempo[4].length() > 0 || tempo[5].length() > 0 ? "T":"";
				strDuration += tempo[3].length() > 0 ? tempo[3] + "H": "";
				strDuration += tempo[4].length() > 0 ? tempo[4] + "M": "";
				strDuration += tempo[5].length() > 0 ? tempo[5] + "S": "";


				if (!strDuration.equals("P")) {
					Duration duration = DatatypeFactory.newInstance().newDuration(strDuration);
					tl.setValue(duration);
					m.setCompleteUnitOfLearning(cUol);
				}
				else {
					m.setCompleteUnitOfLearning(null);
				}

				return;
			}

		} catch (Exception e) {
			return;
		}




	}
}
