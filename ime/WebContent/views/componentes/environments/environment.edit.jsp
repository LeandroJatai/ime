<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="br.edu.ifg.ime.controllers.Lc"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="org.imsglobal.jaxb.ld.Environment"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="org.imsglobal.jaxb.ld.SupportActivity"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.controllers.ActivityController"%>
<%@page import="org.imsglobal.jaxb.ld.Activities"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%=Suport.getMessageWarning(request)%>

<%	Usuario u = UsuarioController.getUsuarioConectado(session);
boolean readonly = !u.hasPermissoes("environment.save");
	


	String identifier = Suport.r(request, "identifier");
	String idRef = Suport.r(request, "id-ref");
	
	if (idRef != null) {
		identifier = idRef;
	}

	String ldep = Suport.r(request, "ldep");
	String aba = Suport.r(request, "aba");






	if (aba == null)
		aba = "geral";

	String titulo = "";

	Environment env = EnvironmentsController.getEnviromentByIdentifier(request, identifier);
	
	if (env != null) {
		titulo = StringEscapeUtils.escapeHtml4(env.getTitle());
	}
	
	
	
%>

<fieldset>
	<legend>
				
           		<img style ="float: left;" src = "<%=Urls.url_av_Servlet %>/imgs/ime/environment16.png"/>
	<%=u.getTexto("at.cp.env.tit.leg")%></legend>

	<div class="TabControl" aba="<%=aba%>">
		<div id="header">
			<ul class="abas">
				<li style="padding-left: 7px;">
					<div class="aba geral">
						<span><%=u.getTexto("at.cp.env.aba.geral.leg")%></span>
					</div>
				</li>

			</ul>
		</div>
		<div id="content">
			<div class="conteudo geral">


		 <% if (!readonly) { %>		 <form action="<%=Urls.urlAppBase %>environment.do" method="post"> <%} %>

           		<img style ="float: left;" src = "<%=Urls.url_av_Servlet %>/imgs/ime/environment.png"/>
           		

					<fieldset>
						<legend><%=u.getTexto("at.cp.env.cad.tit.leg")%></legend> 
						<label style="display: block; margin-right: 10px;"><input style="width:100%;" type="text"
							name="titulo" tabindex="10" value="<%=titulo%>" size="30" /></label>  
					</fieldset>


					<fieldset>
						<legend><%=u.getTexto("at.cp.env.cad.vinc.amb.leg")%></legend> 
						<jsp:include page="../environments/environment.checkbox.list.jsp"></jsp:include>
					</fieldset>
						

						<% if (!readonly)  { %>
							<fieldset class="controles">
								<label><input type="submit" value="<%=u.getTexto("at.cp.env.cad.btn.guardar")%>"></label>
								
						    <% if (u.hasPermissoes("environment.edit.new")) { %>
								<a href="<%=Urls.urlAppBase%>environment.do?action=environment.edit.new&ldep=<%=ldep%>"><%=u.getTexto("at.cp.env.cad.btn.novo")%></a>  
								<% } %>   
							</fieldset>
							<input type="hidden" name="action" value="environment.save"/>
					    	<input type="hidden" name="ldep" value="<%=ldep%>" />
							<input type="hidden" name="identifier" value="<%=identifier%>"/>
						<%} %>


			 <% if (!readonly) { %></form> <%} %>
	
				<%
					if (identifier != null && identifier.length() > 0) {
				%>
				
						<jsp:include page="../environments/lo.list.jsp"></jsp:include>
	
						<jsp:include page="../environments/sv.list.jsp"></jsp:include>
	     <%	} %>
	
			</div>
			
		</div>
	</div>


</fieldset>



<% ImeObject imeO = (ImeObject) env;  %>
<%@include file="../../../admin/msgValidateImsLd.jsp"%>
    
    
    