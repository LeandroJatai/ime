<%@page import="org.imsglobal.jaxb.ld.Environment"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="org.imsglobal.jaxb.ld.Service"%>
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

	String ldep = Suport.r(request, "ldep");
	String idEnv = Suport.r(request, "id-env");

	String identifier = Suport.r(request, "identifier");

	String typeItem = Suport.r(request, "type-item");

	String type = Suport.r(request, "type");

	if (identifier == null)
		identifier = "";

	String titleItem = "";

	Item item = null;

	ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
	Environment env = EnvironmentsController.getEnviromentByIdentifier(
			request, idEnv);

	Service sv = (Service) w.getObject(identifier);

	if (sv != null)
		if (sv.getConference() != null)
			item = sv.getConference().getItem();

	if (typeItem == null && item != null)
		typeItem = item.getType();

	if (sv != null) {
%>


<form action="<%=Urls.urlAppBase%>environment.do" method="post"
	enctype="multipart/form-data">



	<fieldset>
		<legend><%=u.getTexto("at.itemmodel.item.leg")%></legend>

		<%
			if (typeItem != null) {
		%>


		<%
			if (u.hasPermissoes("env.sv.save.item")) {
		%>
		<label><input type="submit"
			value="<%=u.getTexto("at.cp.env.sv.conf.guardar.item")%>"></label>
		<%
			}
		%>

		<%
			}
		%>

		<%
			if (u.hasPermissoes("env.sv.save.item")) {
		%>
		<a class="controls-objs "
			href="environment.do?action=env.sv.edit&id-env=<%=env.getIdentifier()%>&ldep=<%=ldep%>&identifier=<%=sv != null ? sv.getIdentifier() : ""%>&type-item=text"
			title="<%=u.getTexto("at.itemmodel.btn.texto.titulo")%>"><%=u.getTexto("at.itemmodel.btn.texto.add")%></a>
		<a class="controls-objs "
			href="environment.do?action=env.sv.edit&id-env=<%=env.getIdentifier()%>&ldep=<%=ldep%>&identifier=<%=sv != null ? sv.getIdentifier() : ""%>&type-item=link"
			title="<%=u.getTexto("at.itemmodel.btn.link.titulo")%>"><%=u.getTexto("at.itemmodel.btn.link.add")%></a>
		<a class="controls-objs "
			href="environment.do?action=env.sv.edit&id-env=<%=env.getIdentifier()%>&ldep=<%=ldep%>&identifier=<%=sv != null ? sv.getIdentifier() : ""%>&type-item=file"
			title="<%=u.getTexto("at.itemmodel.btn.file.titulo")%>"><%=u.getTexto("at.itemmodel.btn.file.add")%></a>
		<br> <br>
		<%
			}
		%>

		<%
			if (typeItem != null) {

					if (item == null) {
						titleItem = "";

					} else {
						titleItem = item.getTitle();
					}
		%>
		<input type="hidden" name="type" value="<%=typeItem%>" /> <input
			type="hidden" name="ldep"
			value="<%=StringEscapeUtils.escapeHtml4(ldep)%>" /> <input
			type="hidden" name="id-env"
			value="<%=StringEscapeUtils.escapeHtml4(idEnv)%>" /> <input
			type="hidden" name="identifier"
			value="<%=StringEscapeUtils.escapeHtml4(identifier)%>" /> <input
			type="hidden" name="action" value="env.sv.save.item" />

		<fieldset>
			<legend><%=u.getTexto("at.itemmodel.titulo.leg")%></legend>

			<label style="float: right; margin-right: 10px;"> <input
				style="float: left;" name="isvisible" type="checkbox"
				<%=item != null && item.isIsvisible() ? "checked"
							: ""%>><%=u.getTexto("at.itemmodel.item.visivel")%>
			</label> <label style="display: block; margin-right: 102px;"><input
				type="text" name="title" tabindex="10"
				value="<%=StringEscapeUtils.escapeHtml4(titleItem)%>"
				style="width: 100%;" /></label>

		</fieldset>

		<%
			if (UsuarioController.checkPermissao(request,
							"im.item.view.parametros")) {
		%>
		<fieldset>
			<legend><%=u.getTexto("at.itemmodel.item.par.leg")%></legend>
			<label style="display: block; margin-right: 10px;"><input
				type="text" name="parameters" tabindex="10"
				value="<%=item != null && item.getParameters() != null ? StringEscapeUtils
								.escapeHtml4(item.getParameters()) : ""%>"
				style="width: 100%;" /></label>
		</fieldset>
		<%
			}
					if (typeItem.equals("link")) {
		%>
		<fieldset>
			<legend><%=u.getTexto("at.itemmodel.item.link.leg")%></legend>
			<label style="display: block; margin-right: 10px;"><input
				type="text" name="dadosItem" tabindex="10"
				value="<%=item != null && item.getLink() != null ? StringEscapeUtils
								.escapeHtml4(item.getLink()) : ""%>"
				style="width: 100%;" /></label>
		</fieldset>

		<%
			}
		%>

		<%
			if (typeItem.equals("text")) {

						String idDadosItem = ImeWorkspace.getImeWorkspace(
								request).newIdentifier(null,
								"ckEditorTextoItem");
		%>

		<fieldset>
			<legend><%=u.getTexto("at.itemmodel.item.texto.leg")%></legend>

			<label style="display: block; margin-right: 2px;"> <%
 	if (u.hasPermissoes("env.sv.save.item")) {
 %> <textarea id="<%=idDadosItem%>" name="dadosItem" tabindex="10"
					style="width: 100%;" rows="6"><%=item != null && item.getText() != null ? StringEscapeUtils
									.escapeHtml4(item.getText()) : " "%></textarea></label>
			<script>
				
											// Replace the <textarea id="editor"> with an CKEditor
											// instance, using default configurations.
											CKEDITOR.replace( '<%=idDadosItem%>', {
					uiColor : '#eeddaa',
					toolbar : [
							[ 'Bold', 'Italic', '-', 'NumberedList',
									'BulletedList', '-', 'Link', 'Unlink' ],
							[ 'FontSize', 'TextColor', 'BGColor' ] ] });
			</script>
			<%
				} else {
			%>
			<%=item != null && item.getText() != null ? item
									.getText() : " "%>
			<%
				}
			%>
		</fieldset>

		<%
			}
		%>


		<%
			if (typeItem.equals("file")) {
		%>
		<fieldset>
			<legend><%=u.getTexto("at.itemmodel.item.file.leg")%></legend>

			<%
				if (item != null && item.getFile() != null) {
			%>
			<%=u
									.getTexto("at.itemmodel.item.file.atual")%>: <a
				class="<%=item.getIdentifier().equals(identifier) ? "selectedItem"
									: ""%>"
				href="im.do?action=im.item.edit.download.file<%=item.getType() == null ? ""
									: "&type-item=" + item.getType()%>&ldep=<%=ldep%>&identifier=<%=item.getIdentifier()%>"
				target="_blanck"><%=StringEscapeUtils.escapeHtml4(item
									.getNameFile())%></a>

			<%
				}
			%>


			<%
				if (u.hasPermissoes("env.sv.save.item")) {
			%>
			<label><input type="file" name="dadosItem" size="50" /><br>
			</label>
			<%
				}
			%>
		</fieldset>

		<%
			}
		%>

	<%
		}
	%>
	</fieldset>
</form>
<%
	}
%>

