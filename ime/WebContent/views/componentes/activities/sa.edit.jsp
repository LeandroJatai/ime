<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
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

<%
Usuario u = UsuarioController.getUsuarioConectado(session);

boolean readonly = !u.hasPermissoes("activities.save.sa");
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

	SupportActivity sa = null;

	if (identifier == null
			|| identifier == ""
			|| (sa = ActivityController
					.getSupportActivityByIdentifier(request,
							identifier)) == null) {

		Urls.forwardAction(request, response, null,
				"activities.do?action=activities.list");
		return;
	}

	titulo = StringEscapeUtils.escapeHtml4(sa.getTitle());
%>

<fieldset>
	<legend>
					<img style="float: left;" src="<%=Urls.url_av_Servlet %>/imgs/ime/support_activity16.png" />
	<%=u.getTexto("at.cp.sa.cad.tit.leg")%></legend>

	<div class="TabControl" aba="<%=aba%>">
		<div id="header">
			<ul class="abas">
				<li style="padding-left: 7px;">
					<div class="aba geral">
						<span><%=u.getTexto("at.cp.sa.aba.geral.leg")%></span>
					</div>
				</li>

				<%
					if (identifier != null && identifier.length() > 0) {
				 if (u.hasPermissoes("activities.itemmodel.sa.descricao")) { %>
				<li>
					<div class="aba activityDescription">
						<span><%=u.getTexto("at.cp.sa.aba.descr.leg")%></span>
					</div>
				</li>
				<% }
				if (u.hasPermissoes("activities.itemmodel.sa.conclusao")) { %>
				<li>
					<div class="aba completeActivity">
						<span><%=u.getTexto("at.cp.sa.aba.conc.actv.leg")%></span>
					</div>
				</li>
				<% }
				if (u.hasPermissoes("activities.itemmodel.sa.oncompletion")) { %>
				<li>
					<div class="aba onCompletion-feedbackDescription">
						<span><%=u.getTexto("at.cp.sa.aba.ao.conc.leg")%></span>
					</div>
				</li>
				<%
					} }
				%>
			</ul>
		</div>
		<div id="content">
			<div class="conteudo geral">


		 <% if (!readonly) { %>		<form action="<%=Urls.urlAppBase%>activities.do" method="post"> <%} %>

					<img style="float: left; padding: 7px;" src="<%=Urls.url_av_Servlet %>/imgs/ime/support_activity.png" />

					<fieldset>
						<legend><%=u.getTexto("at.cp.sa.aba.geral.titulo")%></legend> 
						<label style="float: right; margin-right: 10px;">
					   		    <input style="float: left;" name="isvisible" type="checkbox" <%=sa != null && sa.isIsvisible()?"checked":""%>><%=u.getTexto("at.cp.sa.aba.geral.visivel")%>
					   	    </label>
						<label style="display: block; margin-right: 102px;"><input style="width:100%;" type="text"
							name="titulo" tabindex="10" value="<%=titulo%>" size="30" /></label>  
							
				   	  
					</fieldset>
   
					<% if (u.hasPermissoes("activities.view.sa.parametros")) { %>
			   	       <fieldset>	
					        <legend><%=u.getTexto("at.cp.sa.aba.geral.param")%></legend>
					        <label style="display: block;  margin-right: 10px;"><input type="text" name="parameters" tabindex="10" value="<%=sa != null && sa.getParameters() != null?StringEscapeUtils.escapeHtml4(sa.getParameters()):""%>" style="width:100%;"/></label>
				   	   </fieldset>	
			   	       <%} %>


<% if (!readonly)  { %>
					<fieldset class="controles">
						<label><input type="submit" value="<%=u.getTexto("at.cp.sa.aba.geral.guardar")%>"></label>						
						
						     <% if (u.hasPermissoes("activities.edit")) { %>
							  		<a href="<%=Urls.urlAppBase%>activities.do?action=activities.edit&ldep=<%=ldep%>"><%=u.getTexto("at.cp.sa.aba.geral.btn.novo")%></a>    
							   
						    <%} %>

					</fieldset>


					<input type="hidden" name="identifier" value="<%=identifier%>" />
					<input type="hidden" name="ldep" value="<%=ldep%>" /> <input
						type="hidden" name="action" value="activities.save.sa" />
<%} %>


						<%	if (u.hasPermissoes("activities.view.sa.checkbox.roles")) {	%>							
	             		   <jsp:include page="../roles/roles.checkbox.all.list.jsp"></jsp:include>
	                    <%	} %>
	                	<%	if (u.hasPermissoes("activities.view.sa.checkbox.environment")) {	%>
								<jsp:include page="../environments/environment.checkbox.list.jsp"></jsp:include>
                        <%	} %>

			 <% if (!readonly) { %></form> <%} %>
			</div>
			
				<%
					if (identifier != null && identifier.length() > 0) {
				 if (u.hasPermissoes("activities.itemmodel.sa.descricao")) { %>
			<div class="conteudo activityDescription">
				<%
					idRef = Suport.utfToIso(sa != null ? sa.getIdentifier() : "");
						String link = "/views/itens/itemmodel.edit.jsp?field=activityDescription&abaAtiva="+aba+"&aba=activityDescription&id-ref="
								+ idRef;
				%>
				<jsp:include page="<%=link%>"></jsp:include>
			</div>
				<% }
				if (u.hasPermissoes("activities.itemmodel.sa.conclusao")) { %>
			<div class="conteudo completeActivity">
				<%
				String link = "/views/complete/complete.activity.edit.jsp?field=completeActivity&abaAtiva="+aba+"&aba=completeActivity&id-ref="
								+ Suport.utfToIso(sa.getIdentifier());
				%>
				<jsp:include page="<%=link%>"></jsp:include>
			</div>
			
				<% }
				if (u.hasPermissoes("activities.itemmodel.sa.oncompletion")) { %>
			<div class="conteudo onCompletion-feedbackDescription">
				<%
				String link = "/views/itens/itemmodel.edit.jsp?field=onCompletion-feedbackDescription&abaAtiva="+aba+"&aba=onCompletion-feedbackDescription&id-ref="
								+ Suport.utfToIso(sa.getIdentifier());
				%>
				<jsp:include page="<%=link%>"></jsp:include>
			</div>
			
			<%
				} }
			%>
		</div>
	</div>


</fieldset>

              <% ImeObject imeO = (ImeObject) sa;  %>
              <%@include file="../../../admin/msgValidateImsLd.jsp"%>