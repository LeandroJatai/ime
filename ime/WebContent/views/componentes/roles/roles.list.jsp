<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="org.imsglobal.jaxb.ld.Roles"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

    <%

Usuario u = UsuarioController.getUsuarioConectado(session);
    
	List<ArrayList<Learner>> lLearners = RolesController.getLearners(request);
	List<ArrayList<Staff>> lStaffs = RolesController.getStaffs(request);
    String ldep = Suport.r(request, "ldep");
    
    Roles roles = RolesController.getRoles(ImeWorkspace.getImeWorkspace(request).getLdProject(request));
    %>

    <fieldset>
    <legend><%=u.getTexto("at.cp.role.titulo.leg") %></legend>
    
    <fieldset>
    <legend><img src="<%=Urls.url_av_Servlet %>/imgs/ime/learners16.png"/><%=u.getTexto("at.cp.role.learner.leg") %></legend>
    
		<% 
	    boolean flagFirst = true;
		
		for (ArrayList<Learner> gLearners : lLearners) {

			for (Learner learner : gLearners) { %>
		    	<label class="list<%=flagFirst?"":" inherit" %>"> <%if (u.hasPermissoes("roles.remove")) { %><a class="controls-objs right" href="roles.do?action=roles.remove&identifier=<%=learner.getIdentifier() %>&ldep=<%=ldep%>" title="<%=u.getTexto("at.cp.role.btn.excluir.titulo") %>"><%=u.getTexto("at.cp.role.btn.excluir") %></a><%} %>
		  	    <a  href="roles.do?action=roles.edit&identifier=<%=learner.getIdentifier() %>&ldep=<%=ldep %>"><%=learner.getTitle() %>
	  		    <% ImeObject imeO = (ImeObject) learner;  %>
     		    <%@include file="../../../admin/iconsValidateImsLd.jsp"%></a>
		    </label>								
		<% } 
		flagFirst = false;
		}%>
    </fieldset>

    <fieldset>
    <legend><img src="<%=Urls.url_av_Servlet %>/imgs/ime/staffs16.png"/><%=u.getTexto("at.cp.role.staff.leg") %></legend>
    <%   flagFirst = true;
   	for (ArrayList<Staff> gStaffs : lStaffs) {

   		for (Staff staff : gStaffs) { %>
   		   <label class="list<%=flagFirst?"":" inherit" %>"> <%if (u.hasPermissoes("roles.remove")) { %><a class="controls-objs right" href="roles.do?action=roles.remove&identifier=<%=staff.getIdentifier() %>&ldep=<%=ldep%>" title="<%=u.getTexto("at.cp.role.btn.excluir.titulo") %>"><%=u.getTexto("at.cp.role.btn.excluir") %></a><%} %>
	       <a  href="roles.do?action=roles.edit&identifier=<%=staff.getIdentifier() %>&ldep=<%=ldep %>"><%=staff.getTitle() %>
	       <% ImeObject imeO = (ImeObject) staff;  %>
           <%@include file="../../../admin/iconsValidateImsLd.jsp"%></a>
	   </label>									
		<% } 
		flagFirst = false;
		}%>
    </fieldset>
    
    <%if (u.hasPermissoes("roles.edit.new")) { %>
    <fieldset>
		<a href="<%=Urls.urlAppBase%>roles.do?action=roles.edit.new&ldep=<%=ldep%>"><%=u.getTexto("at.cp.role.btn.novo") %></a>    
    </fieldset>
    <%} %>
    </fieldset>

    <% ImeObject imeO = (ImeObject) roles;  %>
    <%@include file="../../../admin/msgValidateImsLd.jsp"%>