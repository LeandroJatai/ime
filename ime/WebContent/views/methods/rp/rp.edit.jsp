<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.controllers.RolePartController"%>
<%@page import="org.imsglobal.jaxb.ld.RolePart"%>
<%@page import="br.edu.ifg.ime.controllers.ActsController"%>
<%@page import="org.imsglobal.jaxb.ld.Act"%>
<%@page import="org.imsglobal.jaxb.ld.Play"%>
<%@page import="br.edu.ifg.ime.controllers.PlaysController"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="org.imsglobal.jaxb.ld.Environment"%>
<%@page import="br.edu.ifg.ime.controllers.RolesController"%>
<%@page import="org.imsglobal.jaxb.ld.Staff"%>
<%@page import="org.imsglobal.jaxb.ld.Learner"%>
<%@page import="br.edu.ifg.ime.controllers.ProjetoController"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

			<%=Suport.getMessageWarning(request)%>

		<%
			Usuario u = UsuarioController.getUsuarioConectado(session);

		boolean readonly = !u.hasPermissoes("rp.save");

				String identifier = Suport.r(request, "identifier");
				String actRef = Suport.r(request, "act-ref");
				String titulo = "";
				
				String ldep = Suport.r(request, "ldep");
				
				RolePart rp = RolePartController.getRolePartByIdentifier(request, identifier);
				
				if (rp != null) {
			titulo = StringEscapeUtils.escapeHtml4(rp.getTitle());
				}
				
				
				if (identifier == null) identifier = "";
		%>

			
    <%
			    	if (!readonly) {
			    %> <form action="<%=Urls.urlAppBase%>rp.do" method="post"> <%
 	}
 %>
		<fieldset>
		<legend><img src="<%=Urls.url_av_Servlet%>/imgs/ime/rp16.png" /><%=u.getTexto("at.mt.rp.tit.edit.leg")%> <strong>[<%=ActsController.getActByIdentifier(request, actRef).getTitle()%>]</strong> <%=u.getTexto("at.mt.rp.list.of.play.leg")%> <b>[<%=PlaysController.getPlayOfAct(request, ActsController.getActByIdentifier(request, actRef)).getTitle()%>]</b> <%=u.getTexto("at.mt.act.list.of.leg")%> <b>[<%=ImeWorkspace.getImeWorkspace(request).getLdProject(request).getLd().getTitle()%>]</b></legend>
    	                                                                                                      	
			
           		<img style ="float: left;" src = "<%=Urls.url_av_Servlet %>/imgs/ime/rp.png"/>
           		
			<fieldset>
           	<legend><%=u.getTexto("at.mt.rp.title")%></legend>	          
           		<label ><input type="text" name="titulo" tabindex="10" value="<%=titulo%>" size="30"/></label>
           	</fieldset>
			
			
			
          <% if (!readonly) { %>
			<fieldset class="controles">
				<label><input type="submit" value="<%=u.getTexto("at.mt.rp.btn.guardar")%>"></label>
				<%
					if (u.hasPermissoes("rp.edit.new"))  {
				%>
				<a href="<%=Urls.urlAppBase%>rp.do?action=rp.edit.new&act-ref=<%=actRef%>&ldep=<%=ldep%>"  title="<%=u.getTexto("at.mt.rp.btn.novo.titulo") %>"><%=u.getTexto("at.mt.rp.btn.novo.ico") %></a></span>	
				<%
 					}
 				%>      
			</fieldset>
			
			<input type="hidden" name="action" value="rp.save"/>
			<input type="hidden" name="act-ref" value="<%=actRef%>"/>
	    	<input type="hidden" name="ldep" value="<%=ldep%>" />
			<input type="hidden" name="identifier" value="<%=identifier%>"/>
			<%} %>
			
		</fieldset>    
    
    
	<% if (u.hasPermissoes("rp.view.radiobox.activities") || u.hasPermissoes("rp.view.radiobox.roles") || u.hasPermissoes("rp.view.radiobox.environment") )  { %>
    <fieldset>
    <legend><%=u.getTexto("at.mt.rp.ref.unica.roles.leg")%></legend>
    <% if (u.hasPermissoes("rp.view.radiobox.roles"))  { %>
	       <jsp:include page="../../componentes/roles/roles.radio.all.list.jsp"></jsp:include>
    <% } %>
    </fieldset>
    
    <fieldset>
    <legend><%=u.getTexto("at.mt.rp.ref.unica.atvs.leg")%></legend>
    
    <% if (u.hasPermissoes("rp.view.radiobox.environment"))  {%>      
		<jsp:include page="../../componentes/environments/environment.radio.all.list.jsp"></jsp:include>
    <% } if (u.hasPermissoes("rp.view.radiobox.activities"))  {%>  
	    <jsp:include page="../../componentes/activities/activities.radio.all.list.jsp"></jsp:include>
	<% } %>      
	</fieldset>  
	
    <% } %>      
				
    
    
    
	 <% if (!readonly) { %></form> <%}%>
	 
	 
<% ImeObject imeO = (ImeObject) rp;  %>
<%@include file="../../../admin/msgValidateImsLd.jsp"%>