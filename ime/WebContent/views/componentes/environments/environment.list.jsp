<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
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

	Usuario userConnect = UsuarioController.getUsuarioConectado(session);
    
    
    
    List<Environment> lEnvs = EnvironmentsController.getEnvironments(request).getEnvironmentList();

    String ldep = Suport.r(request, "ldep");
    %>
    <fieldset>
    <legend><%=userConnect.getTexto("at.cp.env.tit.leg")%></legend>
    <%  for (Environment env : lEnvs)  { %>
		    <label class="list">
		    <% if (userConnect.hasPermissoes("environment.remove")) { %>
		    <a class="controls-objs right" href="environment.do?action=environment.remove&identifier=<%=env.getIdentifier() %>&ldep=<%=ldep%>" title="Excluir">[-]</a>
		    <%} %>
		    <a href="environment.do?action=environment.edit&identifier=<%=env.getIdentifier() %>&ldep=<%=ldep%>"><%=env.getTitle() %>
		    
   	
   											             <% ImeObject imeO = (ImeObject) env;  %>
           											 <%@include file="../../../admin/iconsValidateImsLd.jsp"%></a>
		    </label>								
		<% } %>
    <br>
		    <% if (userConnect.hasPermissoes("environment.edit.new")) { %>
    <fieldset>
		<a href="<%=Urls.urlAppBase%>environment.do?action=environment.edit.new&ldep=<%=ldep%>">[Novo Ambiente]</a>    
    </fieldset>
    <% } %>
    </fieldset>
