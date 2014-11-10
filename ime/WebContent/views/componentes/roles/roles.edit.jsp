<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
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

boolean readonly = !u.hasPermissoes("roles.save");

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
	String matchPersons = "";
	String minPersons = "";
	String maxPersons = "";
	String createNew = "";

	Learner learner = RolesController.getLearnerByIdentifier(request,
			identifier);
	Staff staff = RolesController.getStaffByIdentifier(request,
			identifier);

	if (learner != null) {
		titulo = StringEscapeUtils.escapeHtml4(learner.getTitle());
		matchPersons = learner.getMatchPersons();
		minPersons = learner.getMinPersons() == null ? "" : learner
				.getMinPersons().toString();
		maxPersons = learner.getMaxPersons() == null ? "" : learner
				.getMaxPersons().toString();
		createNew = learner.getCreateNew();
	//	learner.validateImsLd();
	}

	if (staff != null) {
		titulo = StringEscapeUtils.escapeHtml4(staff.getTitle());
		matchPersons = staff.getMatchPersons();
		minPersons = staff.getMinPersons() == null ? "" : staff
				.getMinPersons().toString();
		maxPersons = staff.getMaxPersons() == null ? "" : staff
				.getMaxPersons().toString();
		createNew = staff.getCreateNew();
	//	staff.validateImsLd();
	}

	if (identifier == null)
		identifier = "";
	if (matchPersons == null)
		matchPersons = "";
	if (minPersons == null)
		minPersons = "";
	if (maxPersons == null)
		maxPersons = "";
	if (createNew == null)
		createNew = "";
%>
<fieldset>
	<legend><%=u.getTexto("at.cp.role.titulo2.leg") %></legend>

	<div class="TabControl" aba="<%=aba%>">
		<div id="header">
			<ul class="abas">
				<li style="padding-left: 7px;">
					<div class="aba geral">
						<span><%=u.getTexto("at.cp.role.aba.geral") %></span>
					</div>
				</li>

				<% if (identifier != null && identifier.length() > 0) { %>
	            	<% if (u.hasPermissoes("roles.itemmodel.informacoes")) {%>
					<li>
						<div class="aba information">
							<span><%=u.getTexto("at.cp.role.aba.inf") %></span>
						</div>
					</li>
					<%} %>
				<% } %>
			</ul>
		</div>
		<div id="content">
			<div class="conteudo geral">

				 <% if (!readonly) { %><form action="<%=Urls.urlAppBase%>roles.do" method="post"> <%} %>

					<fieldset style="float: right;">
						<legend><%=u.getTexto("at.cp.role.aba.geral.cn.leg") %></legend>

						<label><input type="radio" name="create-new" tabindex="40"
							value="allowed"
							<%=createNew != null && createNew.equals("allowed") ? "checked"
					: createNew.length() == 0 ? "checked" : ""%> />
							<%=u.getTexto("at.cp.role.aba.geral.cn.p") %></label>&nbsp;&nbsp;&nbsp;&nbsp; <label><input
							type="radio" name="create-new" tabindex="50" value="not-allowed"
							<%=createNew != null && createNew.equals("not-allowed") ? "checked"
					: ""%> />
							<%=u.getTexto("at.cp.role.aba.geral.cn.np") %></label>
					</fieldset>


					<%
						if (identifier.length() == 0) {
					%>	
					<fieldset style="float: right;">
						<legend><%=u.getTexto("at.cp.role.aba.geral.tipo") %></legend>
						<label><input type="radio" name="tipo" value="learner"
							tabindex="20" checked /> <img src="<%=Urls.url_av_Servlet %>/imgs/ime/learner16.png"><%=u.getTexto("at.cp.role.learner.leg") %></label>&nbsp;&nbsp;&nbsp;&nbsp;
						<label><input type="radio" name="tipo" value="staff"
							tabindex="30" /> <img src="<%=Urls.url_av_Servlet %>/imgs/ime/staff16.png"><%=u.getTexto("at.cp.role.staff.leg") %></label>
					</fieldset>
					<%
						} else {
					%>

					<input type="hidden" name="tipo"
						value="<%=learner != null ? "learner" : "staff"%>" />

					<%
						}
						if (identifier.length() > 0) {
					%>
					<img style="float: left; padding: 7px;"
						src="<%=Urls.url_av_Servlet %>/imgs/ime/<%=learner != null ? "learner" : "staff"%>.png" />

					<%
						}
					%>
					<fieldset>
						<legend><%=u.getTexto("at.cp.role.aba.geral.nome") %></legend>
						<label style="text-align: center; width: 100%;"><input
							type="text" name="titulo" tabindex="10" value="<%=titulo%>"
							size="30" /></label>
					</fieldset>


					<fieldset style="float: right; text-align: center;">
						<legend><%=u.getTexto("at.cp.role.aba.geral.maxp") %></legend>
						<label><input type="text" name="max-persons" tabindex="90"
							value="<%=maxPersons != null && maxPersons.length() > 0 ? maxPersons
					: ""%>"
							size="6" /></label>
					</fieldset>

					<fieldset style="float: right; text-align: center;">
						<legend><%=u.getTexto("at.cp.role.aba.geral.minp") %></legend>
						<label><input type="text" name="min-persons" tabindex="80"
							value="<%=minPersons != null && minPersons.length() > 0 ? minPersons
					: ""%>"
							size="6" /></label>
					</fieldset>



					<fieldset style="float: right;">
						<legend><%=u.getTexto("at.cp.role.aba.geral.igp") %></legend>
						<label><input type="radio" name="match-persons"
							tabindex="60" value="exclusively-in-roles"
							<%=matchPersons != null
					&& matchPersons.equals("exclusively-in-roles") ? "checked"
					: matchPersons.length() == 0 ? "checked" : ""%> />
							<%=u.getTexto("at.cp.role.aba.geral.igp.ep") %></label>&nbsp;&nbsp;&nbsp;&nbsp; <label><input
							type="radio" name="match-persons" tabindex="70"
							value="not-exclusively"
							<%=matchPersons != null
					&& matchPersons.equals("not-exclusively") ? "checked" : ""%> />
							<%=u.getTexto("at.cp.role.aba.geral.igp.ne") %></label>
					</fieldset>

				<% if (!readonly) { %>
					<fieldset class="controles">
					
						<label><input type="submit" value="<%=u.getTexto("at.cp.role.btn.guardar") %>"></label>

						<% if (u.hasPermissoes("roles.edit.new")) { %>
						   <a href="<%=Urls.urlAppBase%>roles.do?action=roles.edit.new&ldep=<%=ldep%>"><%=u.getTexto("at.cp.role.btn.novo") %></a>
						<% } %>
					</fieldset>
<%} %>

				

				<% if (!readonly) { %>
					<input type="hidden" name="action" value="roles.save" /> 
					<input type="hidden" name="ldep" value="<%=ldep%>" /> 
					<input type="hidden" name="aba" value="geral" /> 
					<input type="hidden" name="identifier" value="<%=identifier%>" />
					
					</form>
				<% } %>
			</div>

			<% if (identifier != null && identifier.length() > 0) { %>
            	<% if (u.hasPermissoes("roles.itemmodel.informacoes")) {%>
			
					<div class="conteudo information">
						<%
							idRef = Suport.utfToIso(learner != null ? learner.getIdentifier() : staff.getIdentifier());
								String link = "/views/itens/itemmodel.edit.jsp?field=information&abaAtiva="+aba+"&aba=information&id-ref="
										+ idRef;
						%>
						<jsp:include page="<%=link%>"></jsp:include>
					</div>
				<% } %>
			<% } %>
		</div>
	</div>


</fieldset>

	    <% ImeObject imeO = (ImeObject) (learner != null ? learner : staff);  %>
        <%@include file="../../../admin/msgValidateImsLd.jsp"%>