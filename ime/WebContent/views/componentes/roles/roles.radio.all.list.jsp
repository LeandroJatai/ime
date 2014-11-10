<%@page import="br.edu.ifg.ime.controllers.Lc"%>
<%@page import="java.util.ArrayList"%>
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
            %>
    <fieldset>
    <legend><%=Lc.getTexto(session, "at.cp.role.titulo.leg")%></legend>
    
    <fieldset>
    <legend><%=Lc.getTexto(session, "at.cp.role.learner.leg")%></legend>
    <%
    	boolean flagFirst = true;
        	
        	for (ArrayList<Learner> gLearners : lLearners) {

        		for (Learner role : gLearners) {
        				    	
                    	Boolean checked = false;
                    	
                    	checked = ImeWorkspace.getImeWorkspace(request).existeVinculo(role.getIdentifier(), identifier);
    %>
    
    		<label class="list<%=flagFirst || checked?"":" inherit"%>"><input type="radio" name="papelSelecionado" value="<%=role.getIdentifier()%>" <%=checked?"checked":""%>/><%=role.getTitle()%></label>								
		<%
											}
																				flagFirst = false;
																			}
										%>
    
    </fieldset>
    
    <fieldset>
    <legend><%=Lc.getTexto(session, "at.cp.role.staff.leg")%></legend>
    <%
    	flagFirst = true;
           	for (ArrayList<Staff> gStaffs : lStaffs) {

           		for (Staff role : gStaffs) {
           	   	
                    	Boolean checked = false;
                    	
                    	checked = ImeWorkspace.getImeWorkspace(request).existeVinculo(role.getIdentifier(), identifier);
    %>
    
    		<label class="list<%=flagFirst || checked?"":" inherit" %>"><input type="radio" name="papelSelecionado" value="<%=role.getIdentifier()%>" <%=checked?"checked":"" %>/><%=role.getTitle() %></label>								
		<% } 
		flagFirst = false;
		}%>
    
    </fieldset>
    
    
    </fieldset>
