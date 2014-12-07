package br.edu.ifg.ime.controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.imsglobal.jaxb.ld.Components;
import org.imsglobal.jaxb.ld.Learner;
import org.imsglobal.jaxb.ld.LearningDesign;
import org.imsglobal.jaxb.ld.Roles;
import org.imsglobal.jaxb.ld.Staff;

import br.edu.ifg.ime.dao.DAOConnection;
import br.edu.ifg.ime.dto.Usuario;
import br.edu.ifg.ime.ld.LdProject;

public class ComponentsController {

/*
	public synchronized static  Components getComponentsOfJob(HttpServletRequest request) {

		LearningDesign ld = ProjetoController.getLdOfJob(request);

		if (ld.getComponents() == null) {
			ld.setComponents(new Components());
		}
		return ld.getComponents();		
	}*/
	
	public synchronized static  Components getComponents(LdProject ldep) {

		LearningDesign ld = ldep.getLd();

		if (ld.getComponents() == null) {
			ld.setComponents(new Components());
		}
		ld.getComponents().parent = ld;
		return ld.getComponents();		
	}

}
