<%@page import="org.imsglobal.jaxb.ld.Service"%>
<%@page import="org.imsglobal.jaxb.ld.EmailData"%>
<%@page import="br.edu.ifg.ime.controllers.Lc"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.imsglobal.jaxb.ld.RoleRef"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="org.imsglobal.jaxb.ld.Environment"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@page import="org.imsglobal.jaxb.ld.ActivityStructure"%>
<%@page import="org.imsglobal.jaxb.ld.SupportActivity"%>
<%@page import="org.imsglobal.jaxb.ld.LearningActivity"%>
<%@page import="br.edu.ifg.ime.suport.LearningDesignUtils"%>
<%@page import="java.io.Serializable"%>
<%@page import="br.edu.ifg.ime.controllers.ActivityController"%>
<%@page import="org.imsglobal.jaxb.ld.Staff"%>
<%@page import="br.edu.ifg.ime.controllers.RolesController"%>
<%@page import="br.edu.ifg.ime.controllers.ProjetoController"%>
<%@page import="org.imsglobal.jaxb.ld.LearningDesign"%>
<%@page import="org.imsglobal.jaxb.ld.Learner"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
    <%
	List<ArrayList<Learner>> lLearners = RolesController.getLearners(request);
	List<ArrayList<Staff>> lStaffs = RolesController.getStaffs(request);
	
	String identifier = Suport.r(request, "identifier");
	String idRef = Suport.r(request, "id-ref");
	
	if (idRef != null) {
		identifier = idRef;
	}
    if (identifier == null)
    	identifier = "";
    
	ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
	
	Service sv = (Service) w.getObject(identifier);
	
	
	List<EmailData> lEds = null;
	
	if (sv != null)
		lEds = sv.getSendMail().getEmailDataList();
	else lEds = new ArrayList<EmailData>();
	
	boolean checked = false;
     %>
    <fieldset>
    <legend><%=Lc.getTexto(session, "at.cp.role.titulo.leg") %></legend>
    
    <fieldset>
    <legend><%=Lc.getTexto(session, "at.cp.role.learner.leg") %></legend>
    
    
	<%
	
	for (EmailData ed : lEds) {
	
		if (!(ed.getRoleRef().getRef() instanceof Learner))
		   continue;
		
		Learner role = (Learner) ed.getRoleRef().getRef();
	
	%>

		<label class="list">
 
		
			
		<input type="checkbox" name="papelSelecionado" value="<%=role.getIdentifier()%>" checked /><%=role.getTitle()%></label>

		<%
			
			}
		
    boolean flagFirst = true;
	
	for (ArrayList<Learner> gLearners : lLearners) {

		for (Learner role : gLearners) {
						
					checked = false;
					

					for (EmailData ed : lEds) {
						
						if (!(ed.getRoleRef().getRef() instanceof Learner))
						   continue;
						
							Learner roleSelect =  (Learner) ed.getRoleRef().getRef();
							
							if (roleSelect == role) {
								checked = true;
								break;
							}
						
					}
					if (checked)
						continue;

		%>

		<label class="list<%=flagFirst?"":" inherit" %>">
		
		 <input type="checkbox"
			name="papelSelecionado" value="<%=role.getIdentifier()%>" /><%=role.getTitle()%></label>

		<%
			
			}
		flagFirst = false;
	}
		%>
    
    
    </fieldset>
    
    <fieldset>
    <legend><%=Lc.getTexto(session, "at.cp.role.staff.leg") %></legend>   
    
    <%

	for (EmailData ed : lEds) {
	
		if (!(ed.getRoleRef().getRef() instanceof Staff))
		   continue;
		
					Staff role = (Staff) ed.getRoleRef().getRef();
 
		%>

		<label class="list">
		
			
		<input type="checkbox" name="papelSelecionado" value="<%=role.getIdentifier()%>" checked /><%=role.getTitle()%></label>

		<%
			
			}
    flagFirst = true;
	for (ArrayList<Staff> gStaffs : lStaffs) {

		for (Staff role : gStaffs) {
	
					checked = false;
					

					for (EmailData ed : lEds) {
					
						if (!(ed.getRoleRef().getRef() instanceof Staff))
						   continue;
						
									Staff roleSelect = (Staff) ed.getRoleRef().getRef();
				 
							
							if (roleSelect == role) {
								checked = true;
								break;
							}
						
					}
					if (checked)
						continue;

		%>

		<label class="list<%=flagFirst?"":" inherit" %>">
 
		 <input type="checkbox"
			name="papelSelecionado" value="<%=role.getIdentifier()%>" /><%=role.getTitle()%></label>

		<%
			
			}
		flagFirst = false;
	}
		%>
    
    
    
    </fieldset>
    
    
    </fieldset>
