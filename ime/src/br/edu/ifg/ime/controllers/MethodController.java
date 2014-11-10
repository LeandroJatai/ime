package br.edu.ifg.ime.controllers;

import javax.servlet.http.HttpServletRequest;

import org.imsglobal.jaxb.ld.LearningDesign;
import org.imsglobal.jaxb.ld.Method;

import br.edu.ifg.ime.ld.LdProject;

public class MethodController {


/*	public synchronized static Method getMethodOfJob(HttpServletRequest request) {

		LearningDesign ld = ProjetoController.getLdOfJob(request);

		if (ld.getMethod() == null) {
			ld.setMethod(new Method());
		}
		
		
		return ld.getMethod();		
	}*/
	
	public synchronized static Method getMethod(LdProject ldep) {

		LearningDesign ld = ldep.getLd();

		if (ld.getMethod() == null) {
			ld.setMethod(new Method());
		}
		return ld.getMethod();			
	}

}
