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
	
	List<RoleRef> lRoleRef = RolesController.getListRoleRef(
			ImeWorkspace.getImeWorkspace(request).getObject(identifier) );
	
	boolean checked = false;
     %>
    <fieldset>
    <legend><%=Lc.getTexto(session, "at.cp.role.titulo.leg") %></legend>
    
    <fieldset id="learnerList">
    <legend><%=Lc.getTexto(session, "at.cp.role.learner.leg") %></legend>
    
    
	<%
		
	for (RoleRef roleRef : lRoleRef) {
	
		if (!(LearningDesignUtils.getRef(roleRef) instanceof Learner))
		   continue;
		
		Learner role = (Learner) LearningDesignUtils.getRef(roleRef);
	
	%>

		<label class="list">
		 <span class="left" style="padding-right: 5px;"> 
		    <a class="controls" title="<%=Lc.getTexto(session, "at.msg.move.for.seq")%>">&#8661;</a>
		 </span>
		
			
		<input type="checkbox" name="papelSelecionado" value="<%=role.getIdentifier()%>" checked /><%=role.getTitle()%></label>

		<%
			
			}
		
    boolean flagFirst = true;
	
	for (ArrayList<Learner> gLearners : lLearners) {

		for (Learner role : gLearners) {
						
					checked = false;
					

					for (RoleRef roleRef : lRoleRef) {

						if (!(LearningDesignUtils.getRef(roleRef) instanceof Learner))
							   continue;
							
							Learner roleSelect = (Learner) LearningDesignUtils.getRef(roleRef);
							
							if (roleSelect == role) {
								checked = true;
								break;
							}
						
					}
					if (checked)
						continue;

		%>

		<label class="list<%=flagFirst?"":" inherit" %>">
		<span class="left" style="padding-right: 5px;"> 
		    <a class="controls" title="<%=Lc.getTexto(session, "at.msg.move.for.seq")%>">&#8661;</a>
		 </span>
		 <input type="checkbox"
			name="papelSelecionado" value="<%=role.getIdentifier()%>" /><%=role.getTitle()%></label>

		<%
			
			}
		flagFirst = false;
	}
		%>
    
    
    </fieldset>
    
    <fieldset id="staffList">
    <legend><%=Lc.getTexto(session, "at.cp.role.staff.leg") %></legend>
    
    
    <%
				
			for (RoleRef roleRef : lRoleRef) {

					if (!(LearningDesignUtils.getRef(roleRef) instanceof Staff))
					   continue;
					
					Staff role = (Staff) LearningDesignUtils.getRef(roleRef);
 
		%>

		<label class="list">
		 <span class="left" style="padding-right: 5px;"> 
		    <a class="controls" title="<%=Lc.getTexto(session, "at.msg.move.for.seq")%>">&#8661;</a>
		 </span>
		
			
		<input type="checkbox" name="papelSelecionado" value="<%=role.getIdentifier()%>" checked /><%=role.getTitle()%></label>

		<%
			
			}
    flagFirst = true;
	for (ArrayList<Staff> gStaffs : lStaffs) {

		for (Staff role : gStaffs) {
	
					checked = false;
					

					for (RoleRef roleRef : lRoleRef) {

						if (!(LearningDesignUtils.getRef(roleRef) instanceof Staff))
							   continue;
							
						Staff roleSelect = (Staff) LearningDesignUtils.getRef(roleRef);
							
							if (roleSelect == role) {
								checked = true;
								break;
							}
						
					}
					if (checked)
						continue;

		%>

		<label class="list<%=flagFirst?"":" inherit" %>">
		<span class="left" style="padding-right: 5px;"> 
		    <a class="controls" title="<%=Lc.getTexto(session, "at.msg.move.for.seq")%>">&#8661;</a>
		 </span>
		 <input type="checkbox"
			name="papelSelecionado" value="<%=role.getIdentifier()%>" /><%=role.getTitle()%></label>

		<%
			
			}
		flagFirst = false;
	}
		%>
    
    
    
    </fieldset>
    
    
    </fieldset>
