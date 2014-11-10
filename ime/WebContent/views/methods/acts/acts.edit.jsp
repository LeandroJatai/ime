<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
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
			boolean readonly = !u.hasPermissoes("act.save");
				String identifier = Suport.r(request, "identifier");
				String playRef = Suport.r(request, "play-ref");
				String ldep = Suport.r(request, "ldep");
				String titulo = "";
				String idRef = Suport.r(request, "id-ref");

				if (idRef != null) {
					identifier = idRef;
				}
				
				Act act = ActsController.getActByIdentifier(request, identifier);
				
				Play play = playRef==null?PlaysController.getPlayOfAct(request, act):PlaysController.getPlayByIdentifier(request, playRef);
				
				if (act != null) {
					titulo = StringEscapeUtils.escapeHtml4(act.getTitle());
				}
				
				if (identifier == null) identifier = "";

				String aba = Suport.r(request, "aba");

				if (aba == null)
					aba = "geral";
			%>
			
	
<fieldset>
				<legend>
					<img src="<%=Urls.url_av_Servlet%>/imgs/ime/act16.png" />
					<%=u.getTexto("at.mt.act.cad.tit.edit.leg")%> <strong><%=play.getTitle()%></strong> <%=u.getTexto("at.mt.act.list.of.leg")%> <b><%=ImeWorkspace.getImeWorkspace(request).getLdProject(request).getLd().getTitle()%></b>
	</legend>

	<div class="TabControl" aba="<%=aba%>">
		<div id="header">
			<ul class="abas">
				<li style="padding-left: 7px;">
					<div class="aba geral">
						<span><%=u.getTexto("at.mt.act.aba.geral")%></span>
					</div>
				</li>		
			<%
				if (identifier != null && identifier.length() > 0) {
					if (u.hasPermissoes("act.aba.conclusao")) { %>
				<li>
					<div class="aba completeAct">
						<span><%=u.getTexto("at.mt.act.aba.conc")%></span>
					</div>
				</li>
				<% }
				if (u.hasPermissoes("act.aba.oncompletion")) { %>
				<li>
					<div class="aba onCompletion-feedbackDescription">
						<span><%=u.getTexto("at.mt.act.aba.ao.conc")%></span>
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
								
					  <% if (!readonly) { %>   <form action="<%=Urls.urlAppBase%>act.do" method="post"><%} %>
							
					           		<img style ="float: left;" src = "<%=Urls.url_av_Servlet %>/imgs/ime/act.png"/>
					           		
								<fieldset>
					           	<legend><%=u.getTexto("at.mt.act.aba.geral.title")%></legend>	          
					           		<label ><input type="text" name="titulo" tabindex="10" value="<%=titulo%>" size="30"/></label>
					           	</fieldset>
					           	
								
								<% if (!readonly) { %>
								<fieldset class="controles">
									<label><input type="submit" value="<%=u.getTexto("at.mt.act.btn.guardar")%>"></label>
									
													<% if (u.hasPermissoes("act.remove") && act != null) { %>
													<span style="float: right; padding-right: 5px;">
													    <a class="controls" href="act.do?action=act.remove&identifier=<%=act.getIdentifier()%>&play-ref=<%=playRef%>&ldep=<%=ldep%>" 		 title="<%=u.getTexto("at.mt.act.btn.excluir.titulo") %>"><%=u.getTexto("at.mt.act.btn.excluir.ico") %></a>
						
												    </span>
												    <%} %>
												    
											<%  if (u.hasPermissoes("act.edit.new")) { %>
												<span style="float: right; padding-right: 5px;"> <a class="controls"
													  href="<%=Urls.urlAppBase%>act.do?action=act.edit.new&play-ref=<%=playRef%>&ldep=<%=ldep%>"
												           title="<%=u.getTexto("at.mt.act.btn.novo.titulo") %>"><%=u.getTexto("at.mt.act.btn.novo.ico") %></a>
												</span> 
											<%} %>
													     
								</fieldset>
								
								<input type="hidden" name="action" value="act.save"/>
								<input type="hidden" name="play-ref" value="<%=playRef%>"/>
								<input type="hidden" name="ldep" value="<%=ldep%>"/>
								<input type="hidden" name="identifier" value="<%=identifier%>"/>
										 
							<% } %>
							<%
										if (u.hasPermissoes("acts.view.radiobox.projetos"))  {
									%>
						    <fieldset>
						    <legend><img src="<%=Urls.url_av_Servlet %>/imgs/ime/uol16.png"/><%=u.getTexto( "at.play.mi.comp.leg") %></legend>
							     <jsp:include page="/views/ldep/ldep.radio.projects.main.jsp"></jsp:include>
						    </fieldset>
						    <% 	} %> 
	
  			    <% if (!readonly) { %> </form>   <%  }  %>
  
	
    <% 
    if (act != null) {
    String link  = "/views/methods/rp/rp.list.jsp?act-ref="+act.getIdentifier()+"&ldep="+ldep+"&play-ref="+playRef; %>
      <br>
    <jsp:include page="<%=link %>"></jsp:include>
    
    <%} %>
    
	    </div>
	    <% 
		   if (identifier != null && identifier.length() > 0) {
			
				if (u.hasPermissoes("act.aba.conclusao")) { %>
			<div class="conteudo completeAct">
				<%
				String link = "/views/complete/complete.act.edit.jsp?field=completeAct&abaAtiva="+aba+"&aba=completeAct&id-ref="
								+ Suport.utfToIso(act.getIdentifier());
				%>
				<jsp:include page="<%=link%>"></jsp:include>
			</div>
			
			
				<% }
				if (u.hasPermissoes("act.aba.oncompletion")) { %>
			<div class="conteudo onCompletion-feedbackDescription">
				<%
				String link = "/views/itens/itemmodel.edit.jsp?field=onCompletion-feedbackDescription&abaAtiva="+aba+"&aba=onCompletion-feedbackDescription&play-ref="+playRef+"&id-ref="
								+ Suport.utfToIso(act.getIdentifier());
				%>
				<jsp:include page="<%=link%>"></jsp:include>
			</div>
			
			
			
			
				<% } }
			%>
		</div>
	</div>
	    
</fieldset>
    
    
<% ImeObject imeO = (ImeObject) act;  %>
<%@include file="../../../admin/msgValidateImsLd.jsp"%>