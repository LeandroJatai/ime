<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
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
	String title = "";

	String ldep = Suport.r(request, "ldep");
	String type = Suport.r(request, "type");
	String identifier = Suport.r(request, "identifier");

	if (type == null)
		type = "";

	if (identifier == null)
		identifier = "";

	Environment env = EnvironmentsController.getEnviromentByIdentifier(
			request, identifier);

	List<Service> lSvs = EnvironmentsController.getServices(env);

	Item itemSelected = null;

	String action = Suport.r(request, "action");
%>
<fieldset>
	<legend><%=u.getTexto("at.cp.env.sv.lista.leg")%></legend>
	<%
		for (Service sv : lSvs) {
	%>
	<label class="list"> <%
 	if (u.hasPermissoes("env.lo.remove")) {
 %> <a class="controls-objs right"
		href="environment.do?action=env.sv.remove&identifier=<%=sv.getIdentifier()%>&id-env=<%=env.getIdentifier()%>&ldep=<%=ldep%>"
		title="<%=u.getTexto("at.cp.env.sv.btn.remove.tit")%>"><%=u.getTexto("at.cp.env.sv.btn.remove.leg")%></a>
		<%
			}
		%> <a
		class="<%=sv.getIdentifier().equals(identifier) ? "selected"
						: ""%>"
		href="environment.do?action=env.sv.edit&id-env=<%=env.getIdentifier()%>&ldep=<%=ldep%>&identifier=<%=sv.getIdentifier()%>"><%=sv.getTitle()%>
			<%
				ImeObject imeO = (ImeObject) sv;
			%> <%@include file="../../../admin/iconsValidateImsLd.jsp"%></a>

	</label>
	<%
		}
	%>
	<br>
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
