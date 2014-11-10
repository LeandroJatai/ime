<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="org.imsglobal.jaxb.ld.RolePart"%>
<%@page import="br.edu.ifg.ime.controllers.ActsController"%>
<%@page import="org.imsglobal.jaxb.ld.Act"%>
<%@page import="br.edu.ifg.ime.controllers.PlaysController"%>
<%@page import="br.edu.ifg.ime.controllers.MethodController"%>
<%@page import="org.imsglobal.jaxb.ld.Method"%>
<%@page import="org.imsglobal.jaxb.ld.Play"%>
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
	Usuario u = UsuarioController.getUsuarioConectado(session);

String playRef = Suport.r(request, "play-ref");
    String actRef = Suport.r(request, "act-ref");
	Act act = ActsController.getActByIdentifier(request, actRef);
    List<RolePart> lRp = act.getRolePartList();

	String ldep = Suport.r(request, "ldep");
%>
    <fieldset>
    <legend><%=u.getTexto("at.mt.rp.list.leg")%> <strong><%=act.getTitle()%></strong> <%=u.getTexto("at.mt.rp.list.of.play.leg")%> <b><%=PlaysController.getPlayOfAct(request, act).getTitle()%></b> <%=u.getTexto("at.mt.act.list.of.leg")%> <b><%=ImeWorkspace.getImeWorkspace(request).getLdProject(request).getLd().getTitle()%></b></legend>
    
    
      
    <% if (u.hasPermissoes("rp.edit.new") && act != null) { %>
    <fieldset>
		<span style="float: right; padding-right: 5px;">
													    <a class="controls"
														   href="<%=Urls.urlAppBase%>rp.do?action=rp.edit.new&act-ref=<%=act.getIdentifier() %>&ldep=<%=ldep%>"
														  title="<%=u.getTexto("at.mt.rp.btn.novo.titulo") %>"><%=u.getTexto("at.mt.rp.btn.novo.ico") %></a></span>	
											
    </fieldset>
<% } %>
    
    
    <%  for (RolePart rp : lRp)  { %>
		    <label class="list">
			<% if (u.hasPermissoes("rp.remove") && act != null) { %>
	   			 <a class="controls-objs right" href="rp.do?action=rp.remove&act-ref=<%=act.getIdentifier() %>&identifier=<%=rp.getIdentifier() %>&ldep=<%=ldep %>&play-ref=<%=playRef%>"  title="<%=u.getTexto("at.mt.rp.excluir.titulo") %>"><%=u.getTexto("at.mt.rp.btn.excluir.ico") %></a>
		    <% } %>  
    	    <a href="rp.do?action=rp.edit&act-ref=<%=act.getIdentifier() %>&identifier=<%=rp.getIdentifier() %>&ldep=<%=ldep %>"><%=rp.getTitle() %>
    	    
<% ImeObject imeO = (ImeObject) rp;  %>
<%@include file="../../../admin/iconsValidateImsLd.jsp"%></a>
		  
		    </label>								
		<% } %>
    <br>
 

    </fieldset>
