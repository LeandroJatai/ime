<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
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

	Usuario u = UsuarioController.getUsuarioConectado(session);
    
    List<Serializable> lActivities = ActivityController.getActivities(request).getLearningActivityOrSupportActivityOrActivityStructure();
    String ldep = Suport.r(request, "ldep");
    
    %>
    <fieldset>
    <legend> <%=u.getTexto("at.cp.at.tit.leg")%></legend>
    
    <fieldset>
    <legend><img src="<%=Urls.url_av_Servlet %>/imgs/ime/learning_activity16.png"/> <%=u.getTexto("at.cp.la.tit.leg")%></legend>
		<%for (Serializable activity: lActivities) { 
          		
        	  if (LearningDesignUtils.isLearningActivity(activity)) {
        		  LearningActivity la = (LearningActivity) activity;
          %>    	
          
          
          <label class="list">
          <% if (u.hasPermissoes("activities.remove")) { %>
          		<a class="controls-objs right" href="activities.do?action=activities.remove&identifier=<%=la.getIdentifier() %>&ldep=<%=ldep%>" title="<%=u.getTexto("at.cp.at.btn.excluir.titulo") %>"><%=u.getTexto("at.cp.at.btn.excluir.ico") %></a>
		  <%} %>
		  <a href="activities.do?action=activities.edit.la&identifier=<%=la.getIdentifier() %>&ldep=<%=ldep%>"><%=la.getTitle() %>
   											             <% ImeObject imeO = (ImeObject) la;  %>
           											 <%@include file="../../../admin/iconsValidateImsLd.jsp"%></a>
		    </label>
		<%} }%>		
    </fieldset>
    
    <fieldset>
    <legend><img src="<%=Urls.url_av_Servlet %>/imgs/ime/support_activity16.png"/> <%=u.getTexto("at.cp.sa.tit.leg")%></legend>
		<%for (Serializable activity: lActivities) { 
          		
        	  if (LearningDesignUtils.isSupportActivity(activity)) {
        		  SupportActivity sa = (SupportActivity) activity;
          %>    	
          <label class="list">
          <% if (u.hasPermissoes("activities.remove")) { %>
               <a class="controls-objs right" href="activities.do?action=activities.remove&identifier=<%=sa.getIdentifier() %>&ldep=<%=ldep%>" title="<%=u.getTexto("at.cp.at.btn.excluir.titulo") %>"><%=u.getTexto("at.cp.at.btn.excluir.ico") %></a>
		  <%} %>
		  <a href="activities.do?action=activities.edit.sa&identifier=<%=sa.getIdentifier() %>&ldep=<%=ldep%>"><%=sa.getTitle() %>  
		   											             <% ImeObject imeO = (ImeObject) sa;  %>
           											 <%@include file="../../../admin/iconsValidateImsLd.jsp"%></a>
		    </label>
		<%} }%>		
    </fieldset>
    
    <fieldset>
    <legend><img src="<%=Urls.url_av_Servlet %>/imgs/ime/activity_structure16.png"/> <%=u.getTexto("at.cp.as.tit.leg")%></legend>
		<%for (Serializable activity: lActivities) { 
          		
        	  if (LearningDesignUtils.isActivityStructure(activity)) {
        		  ActivityStructure as = (ActivityStructure) activity;
          %>    	
          <label class="list">
          <% if (u.hasPermissoes("activities.remove")) { %>
              <a class="controls-objs right" href="activities.do?action=activities.remove&identifier=<%=as.getIdentifier() %>&ldep=<%=ldep%>" title="<%=u.getTexto("at.cp.at.btn.excluir.titulo") %>"><%=u.getTexto("at.cp.at.btn.excluir.ico") %></a>
		  <%} %>
		   <a href="activities.do?action=activities.edit.as&identifier=<%=as.getIdentifier() %>&ldep=<%=ldep%>"><%=as.getTitle() %>
		   
   	
   											             <% ImeObject imeO = (ImeObject) as;  %>
           											 <%@include file="../../../admin/iconsValidateImsLd.jsp"%></a>
		    </label>
		<%} }%>		
    </fieldset>
    
     <% if (u.hasPermissoes("activities.edit")) { %>
	    <fieldset>    
			<a href="<%=Urls.urlAppBase%>activities.do?action=activities.edit&ldep=<%=ldep%>"> <%=u.getTexto("at.cp.at.cad.novo")%></a>    
	    </fieldset>
    <%} %>
    </fieldset>
