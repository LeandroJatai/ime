<%@page import="org.imsglobal.jaxb.ld.ConferenceManager"%>
<%@page import="org.imsglobal.jaxb.ld.Participant"%>
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
	
	ConferenceManager cm = null;
	
	if (sv != null)
	if (sv.getConference() != null)
		cm = sv.getConference().getConferenceManager();
	
	boolean checked = false;
     %>
    <fieldset class="right">
    <legend><%=Lc.getTexto(session, "at.cp.env.sv.conf.manager.leg")%></legend>
    
    <fieldset>
    <legend><%=Lc.getTexto(session, "at.cp.role.learner.leg") %></legend>
    
    
		<%
			
		
    boolean flagFirst = true;
		for (ArrayList<Learner> gLearner : lLearners) {

			for (Learner role : gLearner) {
		

		%>

		<label class="list<%=flagFirst?"":" inherit" %>">
		
		 <input type="radio" name="conf.manager" value="<%=role.getIdentifier() %>" <%=cm != null && cm.getRoleRef() == role?"checked":""%>/><%=role.getTitle()%></label>

		<%
			
	} 
		flagFirst = false;	
		}
		%>
    
    
    </fieldset>
    
    <fieldset>
    <legend><%=Lc.getTexto(session, "at.cp.role.staff.leg") %></legend>   
    
		<%
		flagFirst = true;
	for (ArrayList<Staff> gStaffs : lStaffs) {

		for (Staff role : gStaffs) {
	

		%>

		<label class="list<%=flagFirst?"":" inherit" %>">
 
		 <input type="radio" name="conf.manager" value="<%=role.getIdentifier() %>" <%=cm != null && cm.getRoleRef() == role?"checked":""%>/><%=role.getTitle()%></label>
	
		<%
			
			}

		flagFirst = false;	
	}
		%>
    
    
    
    </fieldset>
    
    
    </fieldset>
