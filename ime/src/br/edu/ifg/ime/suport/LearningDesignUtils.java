package br.edu.ifg.ime.suport;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.imsglobal.jaxb.ld.ActivityStructure;
import org.imsglobal.jaxb.ld.Environment;
import org.imsglobal.jaxb.ld.Learner;
import org.imsglobal.jaxb.ld.LearningActivity;
import org.imsglobal.jaxb.ld.Staff;
import org.imsglobal.jaxb.ld.SupportActivity;

public class LearningDesignUtils {

	public static boolean isActivity(Object object) {
		return object instanceof LearningActivity || 
			object instanceof SupportActivity || 
			object instanceof ActivityStructure;
	}

	public static boolean isActivityStructure(Object object) {
		return object instanceof ActivityStructure;
	}

	public static boolean isLearningActivity(Object object) {
		return object instanceof LearningActivity;
	}

	public static boolean isSupportActivity(Object object) {
			return object instanceof SupportActivity;
	}

	public static boolean isRole(Object object) {
		return object instanceof Learner || object instanceof Staff;
	}
	
	public static boolean isEnviromnent(Object object) {
		return object instanceof Environment;
	}
	
	public static boolean isComponent(Object object){
		return isRole(object) || isEnviromnent(object) || isActivity(object);
	}

	public static String getIdentifier(Object object){
		try {
			return (String) object.getClass().getMethod("getIdentifier").invoke(object);
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		} 
		
	}
	
	public static String getIdentifierForImport(Object object) throws Exception {
		
			return (String) object.getClass().getMethod("getIdentifier").invoke(object);
		
	}
	
	public static Object getRef(Object object){
		try {
			return (Object) object.getClass().getMethod("getRef").invoke(object);
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		} 
	}
	
}
