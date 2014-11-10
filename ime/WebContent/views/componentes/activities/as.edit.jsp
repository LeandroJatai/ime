<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="org.imsglobal.jaxb.ld.ActivityStructure"%>
<%@page import="org.imsglobal.jaxb.ld.LearningActivity"%>
<%@page import="br.edu.ifg.ime.controllers.ActivityController"%>
<%@page import="org.imsglobal.jaxb.ld.Activities"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%=Suport.getMessageWarning(request)%>

<%
Usuario u = UsuarioController.getUsuarioConectado(session);

boolean readonly = !u.hasPermissoes("activities.save.as");

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
			String structureType = Suport.r(request, "structure-type");
			String sort = Suport.r(request, "sort");
			String numberToSelect = Suport.r(request, "number-to-select");

			ActivityStructure as = null;
			
			if (identifier == null || identifier == "" || (as = ActivityController.getActivityStructureByIdentifier(request, identifier)) == null) {
				Urls.forwardAction(request, response, null, "activities.do?action=activities.list");
				return;
			}
			
			titulo = StringEscapeUtils.escapeHtml4(as.getTitle());	
			structureType = as.getStructureType();
			sort = as.getSort();
			if (as.getNumberToSelect() != null)
			numberToSelect = as.getNumberToSelect().toString();
	
			if ( structureType == null)  structureType = "sequence";
			if ( sort == null) sort = "as-is";
			if ( numberToSelect == null) numberToSelect = "";
			%>



<fieldset>
	<legend>
					<img src="<%=Urls.url_av_Servlet %>/imgs/ime/activity_structure16.png" />
	<%=u.getTexto("at.cp.as.cad.tit.leg") %></legend>

	<div class="TabControl" aba="<%=aba%>">
		<div id="header">
			<ul class="abas">
				<li style="padding-left: 7px;">
					<div class="aba geral">
						<span><%=u.getTexto("at.cp.as.aba.geral.leg") %></span>
					</div>
				</li>

				<%
					if (identifier != null && identifier.length() > 0) {
				 if (u.hasPermissoes("activities.itemmodel.as.informacoes")) { %>
				<li>
					<div class="aba information">
						<span><%=u.getTexto("at.cp.as.aba.info.leg") %></span>
					</div>
				</li>
				
				<%
					} }
				%>
			</ul>
		</div>
		<div id="content">
			<div class="conteudo geral">




 <% if (!readonly) { %><form action="<%=Urls.urlAppBase %>activities.do" method="post"> <%} %>
	
		<img style="float: left; padding: 7px;"
			src="<%=Urls.url_av_Servlet %>/imgs/ime/activity_structure.png" />



		<fieldset>
			<legend><%=u.getTexto("at.cp.as.aba.geral.titulo") %></legend>
			<label style="display: block; margin-right: 10px;"><input
				style="width: 100%;" type="text" name="titulo" tabindex="10"
				value="<%=titulo %>" /></label>
		</fieldset>

		<fieldset style="float: right;">
			<legend><%=u.getTexto("at.cp.as.aba.geral.tipo.as") %></legend>
			<label><input type="radio" name="structure-type"
				tabindex="20" value="sequence"
				<%=structureType != null && structureType.equals("sequence")?"checked": structureType.length() == 0? "checked": ""%> />
				<%=u.getTexto("at.cp.as.aba.geral.tipo.as.seq") %></label>&nbsp;&nbsp; <label><input type="radio"
				name="structure-type" tabindex="20" value="selection"
				<%=structureType != null && structureType.equals("selection")?"checked": ""%> />
				<%=u.getTexto("at.cp.as.aba.geral.tipo.as.sel") %></label>
		</fieldset>

		<fieldset style="float: right; margin-left: 14px;">
			<legend><%=u.getTexto("at.cp.as.aba.geral.num.sel") %></legend>
			<label style="width: 100%; text-align: center;"><input
				size="5" type="text" name="number-to-select" tabindex="30"
				value="<%=numberToSelect %>" /></label>
		</fieldset>

		<fieldset>
			<legend><%=u.getTexto("at.cp.as.aba.geral.ordem") %></legend>
			<label><input type="radio" name="sort" tabindex="20"
				value="as-is"
				<%=sort != null && sort.equals("as-is")?"checked": structureType.length() == 0? "checked": ""%> /><%=u.getTexto("at.cp.as.aba.geral.ordem.ce") %></label>&nbsp;&nbsp; <label><input type="radio" name="sort"
				tabindex="20" value="visibility-order"
				<%=sort != null && sort.equals("visibility-order")?"checked": ""%> /><%=u.getTexto("at.cp.as.aba.geral.ordem.ov") %></label>
		</fieldset>

<% if (!readonly) { %>
		<fieldset class="controles">
			<label><input type="submit" value="<%=u.getTexto("at.cp.as.aba.geral.btn.guardar") %>"></label>

						     <% if (u.hasPermissoes("activities.edit")) { %>
							  		<a href="<%=Urls.urlAppBase%>activities.do?action=activities.edit&ldep=<%=ldep%>"><%=u.getTexto("at.cp.as.aba.geral.btn.novo") %></a>    
							   
						    <%} %>
		</fieldset>



		<input type="hidden" name="identifier" value="<%=identifier%>" /> <input
			type="hidden" name="ldep" value="<%=ldep%>" /> <input type="hidden"
			name="action" value="activities.save.as" />
<% } %>

	
	
	<%	if (u.hasPermissoes("activities.view.as.checkbox.environment")) {	%>
		<jsp:include page="../environments/environment.checkbox.list.jsp"></jsp:include>
	<%	} %>
	
	<%	if (u.hasPermissoes("activities.view.as.checkbox.activities")) {	%>
	<jsp:include page="activities.checkbox.by.type.list.for.as.jsp"></jsp:include>
	<%	} %>
	
 <% if (!readonly) { %></form> <%} %>




			</div>
				<%
					if (identifier != null && identifier.length() > 0) {
				 if (u.hasPermissoes("activities.itemmodel.as.informacoes")) { %>
			<div class="conteudo information">
				<%
					idRef = Suport.utfToIso(as != null ? as.getIdentifier() : "");
						String link = "/views/itens/itemmodel.edit.jsp?field=information&abaAtiva="+aba+"&aba=information&id-ref="
								+ idRef;
				%>
				<jsp:include page="<%=link%>"></jsp:include>
			</div>
			<%
				} }
			%>
		</div>
	</div>


</fieldset>


              <% ImeObject imeO = (ImeObject) as;  %>
              <%@include file="../../../admin/msgValidateImsLd.jsp"%>