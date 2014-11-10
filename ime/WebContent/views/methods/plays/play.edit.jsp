<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="org.imsglobal.jaxb.ld.LearningDesign"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="org.imsglobal.jaxb.ld.Play"%>
<%@page import="br.edu.ifg.ime.controllers.PlaysController"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="org.imsglobal.jaxb.ld.Environment"%>
<%@page import="br.edu.ifg.ime.controllers.RolesController"%>
<%@page import="org.imsglobal.jaxb.ld.Staff"%>
<%@page import="org.imsglobal.jaxb.ld.Learner"%>
<%@page
	import="br.edu.ifg.ime.controllers.ProjetoController"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%=Suport.getMessageWarning(request)%>

<%
	Usuario u = UsuarioController.getUsuarioConectado(session);
    boolean readonly = !u.hasPermissoes("play.save");
	String identifier = Suport.r(request, "identifier");
	String playRef =  Suport.r(request, "play-ref");
	String ldep = Suport.r(request, "ldep");
	String idRef = Suport.r(request, "id-ref"); 
	String aba = Suport.r(request, "aba");
	String titulo = "";

	if (idRef != null) 
		identifier = idRef; 
	
	Play play = PlaysController.getPlayByIdentifier(request, identifier);	
	
	if (identifier == null) 
		identifier = ""; 

	if (play != null) 
		titulo = StringEscapeUtils.escapeHtml4(play.getTitle()); 


	if (aba == null)
		aba = "geral";
%>
<fieldset>
		<legend><img src="<%=Urls.url_av_Servlet%>/imgs/ime/play16.png" /><%=u.getTexto("at.mt.play.cad.tit.edit.leg")%> <b><%=ImeWorkspace.getImeWorkspace(request).getLdProject(request).getLd().getTitle()%></b></legend>

	<div class="TabControl" aba="<%=aba%>">
		<div id="header">
			<ul class="abas">
				<li style="padding-left: 7px;">
					<div class="aba geral">
						<span><%=u.getTexto("at.mt.play.aba.geral")%></span>
					</div>
				</li>		
			<%
				if (identifier != null && identifier.length() > 0) {
					if (u.hasPermissoes("play.aba.conclusao")) { %>
				<li>
					<div class="aba completePlay">
						<span><%=u.getTexto("at.mt.play.aba.conc.py")%></span>
					</div>
				</li>
				<% }
				if (u.hasPermissoes("play.aba.oncompletion")) { %>
				<li>
					<div class="aba onCompletion-feedbackDescription">
						<span><%=u.getTexto("at.mt.play.aba.ao.conc.py")%></span>
					</div>
				</li>
				<%
					} 
				}
				%>
			</ul>
		</div>
		<div id="content">
			<div class="conteudo geral"> 
								

 <% if (!readonly) { %><form action="<%=Urls.urlAppBase%>play.do" method="post"><%} %>
			<img style="float: left;" src="<%=Urls.url_av_Servlet %>/imgs/ime/play.png" />

		<fieldset>
			<legend><%=u.getTexto("at.mt.play.aba.geral.title")%></legend>    
						   <label style="float: right; margin-right: 10px;">
					   		    <input style="float: left;" name="isvisible" type="checkbox" <%=play != null && play.isIsvisible()?"checked":""%>><%=u.getTexto("at.mt.play.aba.geral.visivel")%>
					   	    </label>
				   	       <label style="display: block;  margin-right: 112px;">
				   	       <input style="width: 100%;" type="text" name="titulo" tabindex="10" 	value="<%=titulo%>" size="30" /></label>
		</fieldset>



		<% if (!readonly) { %>
		<fieldset class="controles">
			<label><input type="submit" value="<%=u.getTexto("at.mt.play.btn.guardar")%>"></label>
			
						<% if (u.hasPermissoes("play.remove") && play != null) { %>
						<span style="float: right; padding-right: 5px;">
						   <a class="controls"
							  href="play.do?action=play.remove&identifier=<%=play.getIdentifier()%>&ldep=<%=ldep%>"
								 title="<%=u.getTexto("at.mt.play.btn.excluir.titulo") %>"><%=u.getTexto("at.mt.play.btn.excluir.ico") %></a></span>
						<% } if (u.hasPermissoes("play.edit.new")) { %>
						<span style="float: right; padding-right: 5px;">
						   <a class="controls"
							  href="play.do?action=play.edit.new&ldep=<%=ldep%>"
							  title="<%=u.getTexto("at.mt.play.btn.novo.titulo") %>"><%=u.getTexto("at.mt.play.btn.novo.ico") %></a></span>
						<% } %>
						
		

		<input type="hidden" name="action" value="play.save" /> 
		<input type="hidden" name="ldep" value="<%=ldep%>" />
		<input type="hidden" name="identifier" value="<%=identifier%>" />
		
		</fieldset>
		
		
		<% } %>
 	<br>
    <%  if (play != null) {    	
    	String link  = "/views/methods/acts/acts.list.jsp?play-ref="+play.getIdentifier()+"&ldep="+ldep; %>
    
    <jsp:include page="<%=link %>"></jsp:include>
    
    <% } %>

	
 <% if (!readonly) { %></form><%} %>
    
    </div>


	    <% 
		   if (identifier != null && identifier.length() > 0) {
			
				if (u.hasPermissoes("play.aba.conclusao")) { %>
			<div class="conteudo completePlay">
				<%
				String link = "/views/complete/complete.play.edit.jsp?field=completePlay&abaAtiva="+aba+"&aba=completePlay&id-ref="
								+ Suport.utfToIso(play.getIdentifier());
				%>
				<jsp:include page="<%=link%>"></jsp:include>
			</div>
			
			
				<% }
				if (u.hasPermissoes("play.aba.oncompletion")) { %>
			<div class="conteudo onCompletion-feedbackDescription">
				<%
				String link = "/views/itens/itemmodel.edit.jsp?field=onCompletion-feedbackDescription&abaAtiva="+aba+"&aba=onCompletion-feedbackDescription&play-ref="+playRef+"&id-ref="
								+ Suport.utfToIso(play.getIdentifier());
				%>
				<jsp:include page="<%=link%>"></jsp:include>
			</div> 
				<% } }
			%>
		</div>
	</div> 
</fieldset>
<% ImeObject imeO = (ImeObject) play;  %>
<%@include file="../../../admin/msgValidateImsLd.jsp"%>