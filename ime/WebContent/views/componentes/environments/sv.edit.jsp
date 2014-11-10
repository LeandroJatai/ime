<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="br.edu.ifg.ime.controllers.Lc"%>
<%@page import="org.imsglobal.jaxb.ld.Service"%>
<%@page import="org.imsglobal.jaxb.ld.Environment"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="java.util.List"%>
<%@page import="org.imsglobal.jaxb.ld.LearningObject"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="org.imsglobal.jaxb.ld.Item"%>
<%@page import="br.edu.ifg.ime.controllers.ItemModelController"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="org.imsglobal.jaxb.ld.ItemModel"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	Usuario u = UsuarioController.getUsuarioConectado(session);
	boolean readonlyItens = !u.hasPermissoes("im.item.save");
	String title = "";
	ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);

	String ldep = Suport.r(request, "ldep");
	String idRef = Suport.r(request, "id-ref");
	String idEnv = Suport.r(request, "id-env");
	String type = Suport.r(request, "type");
	String identifier = Suport.r(request, "identifier");

	if (type == null)
		type = "";

	if (identifier == null)
		identifier = "";

	Environment env = EnvironmentsController.getEnviromentByIdentifier(
			request, idEnv);

	List<Service> lSvs = EnvironmentsController.getServices(env);

	Item itemSelected = null;

	String action = Suport.r(request, "action");

	Service svSelected = (Service) w.getObject(identifier);

	if (svSelected != null)
		type = svSelected.getType();
%>
<form action="<%=Urls.urlAppBase%>environment.do" method="post">
	<fieldset>
		<%
			if (type.equals(Service.TYPE_SENDMAIL)) {
		%>
		<legend>
			__Serviço de E-mail do Ambiente <b><%=env.getTitle()%></b>
		</legend>
		<%
			} else if (type.equals(Service.TYPE_CONFERENCE)) {
		%>
		<legend>
			__Serviço de Conferência do Ambiente <b><%=env.getTitle()%></b>
		</legend>
		<%
			} else if (type.equals(Service.TYPE_INDEXSEARCH)) {
		%>
		<legend>
			__Serviço de Busca do Ambiente <b><%=env.getTitle()%></b>
		</legend>
		<%
			}
		%>



		<img style="float: left; padding: 7px 7px 0px 0px;"
			src="<%=Urls.url_av_Servlet%>/imgs/ime/sv.png" />

		<fieldset>
			<legend><%=u.getTexto("at.itemmodel.titulo.leg")%></legend>
			<label style="float: right; margin-right: 10px;"><input
				style="float: left;" name="sv.isvisible" type="checkbox"
				<%=svSelected != null && svSelected.isIsvisible() ? "checked"
					: ""%>><%=Lc.getTexto(session, "at.cp.env.sv.visivel.leg")%></label>



			<label style="display: block; margin-right: 192px;"> <input
				type="text" name="sv.title" tabindex="5"
				value="<%=svSelected != null ? StringEscapeUtils
					.escapeHtml4(svSelected.getTitle()) : ""%>"
				style="width: 100%;" /></label>

		</fieldset>

		<fieldset class="right">
			<legend><%=u.getTexto("at.cp.env.sv.classe.leg")%></legend>
			<label style="display: block; margin-right: 10px;"><input
				type="text" name="sv.clazz" tabindex="10"
				value="<%=svSelected != null
					&& svSelected.getClazzForEditionView().length() > 0 ? StringEscapeUtils
					.escapeHtml4(svSelected.getClazzForEditionView()) : ""%>"
				style="width: 100%;" /></label>
		</fieldset>



		<fieldset>
			<legend><%=u.getTexto("at.cp.env.sv.param.leg")%></legend>
			<label style="display: block; margin-right: 10px;"><input
				type="text" name="sv.parameters" tabindex="10"
				value="<%=svSelected != null && svSelected.getParameters() != null ? StringEscapeUtils
					.escapeHtml4(svSelected.getParameters()) : ""%>"
				style="width: 100%;" /></label>
		</fieldset>


	</fieldset>

	<fieldset class="controles">
		<%
			if (!readonlyItens) {
		%>
		<label><input type="submit"
			value="<%=u.getTexto("at.itemmodel.btn.guardar")%>"></label>
		<%
			}
		%>

		<%
			if (u.hasPermissoes("env.sv.edit.new")) {
		%>
		<a
			href="environment.do?type=send.mail&action=env.sv.edit.new&id-env=<%=env.getIdentifier()%>&ldep=<%=ldep%>"><%=u.getTexto("at.cp.env.sv.btn.novo.email")%></a>
		<a
			href="environment.do?type=conference&action=env.sv.edit.new&id-env=<%=env.getIdentifier()%>&ldep=<%=ldep%>"><%=u.getTexto("at.cp.env.sv.btn.novo.conf")%></a>
		<a
			href="environment.do?type=index.search&action=env.sv.edit.new&id-env=<%=env.getIdentifier()%>&ldep=<%=ldep%>"><%=u.getTexto("at.cp.env.sv.btn.novo.index")%></a>
		<%
			}
		%>

	</fieldset>

	<%
		if (identifier.length() >= 0) {
	%>
	<%
		if (type.equals(Service.TYPE_SENDMAIL)) {
	%>

	<fieldset>
		<legend><%=u.getTexto("at.cp.env.sv.email.tipo")%></legend>
		<label><input type="radio" name="sv.select"
			value="all-persons-in-role" tabindex="20"
			<%=svSelected != null
							&& svSelected.getSendMail() != null
							&& svSelected.getSendMail().getSelect() != null
							&& svSelected.getSendMail().getSelect()
									.equals("all-persons-in-role") ? "checked"
							: (svSelected == null ? "checked" : "")%> /><%=u.getTexto("at.cp.env.sv.email.tipo.all")%></label>&nbsp;&nbsp;&nbsp;&nbsp;
		<label><input type="radio" name="sv.select"
			value="persons-in-role" tabindex="20"
			<%=(svSelected != null
							&& svSelected.getSendMail() != null
							&& svSelected.getSendMail().getSelect() != null && svSelected
							.getSendMail().getSelect()
							.equals("persons-in-role")) ? "checked" : ""%> /><%=u.getTexto("at.cp.env.sv.email.tipo.ind")%></label>&nbsp;&nbsp;&nbsp;&nbsp;
	</fieldset>

	<jsp:include page="roles.checkbox.emaildata.jsp"></jsp:include>


	<%
		} else if (type.equals(Service.TYPE_CONFERENCE)) {
	%>

	<fieldset>
		<legend><%=u.getTexto("at.cp.env.sv.conf.tipo")%></legend>
		<label><input type="radio" name="sv.type.conference"
			value="synchronous" tabindex="20"
			<%=svSelected != null
							&& svSelected.getConference() != null
							&& svSelected.getConference().getConferenceType() != null
							&& svSelected.getConference().getConferenceType()
									.equals("synchronous") ? "checked"
							: (svSelected == null ? "checked" : "")%> /><%=u.getTexto("at.cp.env.sv.conf.tipo.sin")%></label>&nbsp;&nbsp;&nbsp;&nbsp;
		<label><input type="radio" name="sv.type.conference"
			value="asynchronous" tabindex="20"
			<%=(svSelected != null
							&& svSelected.getConference() != null
							&& svSelected.getConference().getConferenceType() != null && svSelected
							.getConference().getConferenceType()
							.equals("asynchronous")) ? "checked" : ""%> /><%=u.getTexto("at.cp.env.sv.conf.tipo.assin")%></label>&nbsp;&nbsp;&nbsp;&nbsp;
		<label><input type="radio" name="sv.type.conference"
			value="announcement" tabindex="20"
			<%=(svSelected != null
							&& svSelected.getConference() != null
							&& svSelected.getConference().getConferenceType() != null && svSelected
							.getConference().getConferenceType()
							.equals("announcement")) ? "checked" : ""%> /><%=u.getTexto("at.cp.env.sv.conf.tipo.anun")%></label>&nbsp;&nbsp;&nbsp;&nbsp;
	</fieldset>

	<jsp:include page="roles.checkbox.manager.jsp"></jsp:include>
	<jsp:include page="roles.checkbox.moderator.jsp"></jsp:include>

	<jsp:include page="roles.checkbox.observer.jsp"></jsp:include>
	<jsp:include page="roles.checkbox.participante.jsp"></jsp:include>


	<%
		} else if (type.equals(Service.TYPE_INDEXSEARCH)) {
	%>

	<%
		}
	%>
	<%
		}
	%>


	<input type="hidden" name="type" value="<%=type%>" /> <input
		type="hidden" name="ldep"
		value="<%=StringEscapeUtils.escapeHtml4(ldep)%>" /> <input
		type="hidden" name="id-env"
		value="<%=StringEscapeUtils.escapeHtml4(idEnv)%>" /> <input
		type="hidden" name="identifier"
		value="<%=StringEscapeUtils.escapeHtml4(identifier)%>" /> <input
		type="hidden" name="action" value="env.sv.save" />

</form>



<%
	if (type.equals(Service.TYPE_CONFERENCE)) {
%>

<jsp:include page="env.conference.item.edit.jsp"></jsp:include>

<%
	}
%>

<%
	ImeObject imeO = (ImeObject) svSelected;
%>
<%@include file="../../../admin/msgValidateImsLd.jsp"%>