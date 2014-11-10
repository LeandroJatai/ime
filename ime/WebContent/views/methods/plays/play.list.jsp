<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
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
    List<Play> lPlays = PlaysController.getPlays(request);

	String ldep = Suport.r(request, "ldep");
%>
    <fieldset>
    <legend><img src="<%=Urls.url_av_Servlet%>/imgs/ime/play16.png" /><%=u.getTexto("at.mt.play.ac.tit.list.leg")%> <b><%=ImeWorkspace.getImeWorkspace(request).getLdProject(request).getLd().getTitle()%></b></legend>
    <%  for (Play play : lPlays)  { %>
		    <label class="list">
      		<% 	if (u.hasPermissoes("play.remove")) { %>
		    <a class="controls-objs right" href="play.do?action=play.remove&identifier=<%=play.getIdentifier() %>&ldep=<%=ldep %>" title="<%=u.getTexto("at.mt.play.btn.excluir.titulo")%>"><%=u.getTexto("at.mt.play.btn.excluir.ico")%></a>
		    <%} %>
		    
		    <a href="play.do?action=play.edit&identifier=<%=play.getIdentifier() %>&ldep=<%=ldep %>"><%=play.getTitle() %>
		    
<%
	ImeObject imeO = (ImeObject) play;
%>
<%@include file="../../../admin/iconsValidateImsLd.jsp"%>
</a>
		    </label>								
		<% } %>
		
		
						<%  if (u.hasPermissoes("play.edit.new")) { %>
    <br>
    <fieldset>
		<a href="<%=Urls.urlAppBase%>play.do?action=play.edit.new&ldep=<%=ldep %>"><%=u.getTexto("at.mt.play.ac.novo.leg")%></a>    
    </fieldset>
    <% } %>
    </fieldset>
 
<%	ImeObject imeO = (ImeObject) ImeWorkspace.getImeWorkspace(request).getLdProject(request).getLd().getMethod(); %>
<%@include file="../../../admin/msgValidateImsLd.jsp"%>