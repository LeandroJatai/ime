<%@page import="java.util.TreeMap"%>
<%@page import="br.edu.ifg.ime.dto.Chave_textual"%>
<%@page import="br.edu.ifg.ime.controllers.LinguagemController"%>
<%@page import="br.edu.ifg.ime.dto.Linguagem"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="java.util.List"%>
<%@page import="br.edu.ifg.ime.dto.Grp_servicos"%>
<%@page import="br.edu.ifg.ime.dto.Servico"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


	<% 

Usuario u = UsuarioController.getUsuarioConectado(session);
	%>
<div id="LeftPane">


<fieldset id="usuariosList">
	<legend><%=u.getTexto("ime.schemas.leg") %></legend>
	
<%
String strIdLang = request.getParameter("idSchema");
String tipo = request.getParameter("schema.tipo");

if (tipo == null || tipo.length() == 0)
	tipo = "4";
	
	List<Chave_textual> listaCts= LinguagemController.getChavesTextuais();
	Chave_textual ct = null;
	for (Chave_textual auxCt: listaCts) {
		
		if (auxCt.getTipo() != Integer.parseInt(tipo))
			continue;
		
		if (auxCt.getId() == Integer.parseInt(strIdLang==null?"0":strIdLang))
			ct = auxCt;
	
	%>
	
	<label class="list">
	 		<a class="controls-objs right" href="lang.do?action=manage.schemas.remove&idLang=<%=auxCt.getId() %>" title="<%=u.getTexto("ime.schemas.excluir.tit") %>"><%=u.getTexto("ime.schemas.excluir.btn") %></a>
		 
	<a class="<%=ct != null && ct.getId() == auxCt.getId()?"selected":"" %>"  href="<%=Urls.urlAppBase%>lang.do?action=manage.schemas&idSchema=<%=auxCt.getId()%>&schema.tipo=<%=tipo %>">
	<%= auxCt.getChave() %></a>
	</label>
	
	<% } %>
	
	</fieldset>
	
</div>

<div id="RightPane">

<%=Suport.getMessageWarning(request)%>

        <form action="<%=Urls.urlAppBase %>lang.do" method="post"> 
    <fieldset>
		<legend><%=u.getTexto("ime.schemas.tit.leg") %></legend>
			<label class="right"><input type="submit" value="<%=ct!=null?u.getTexto("ime.schemas.guardar"):u.getTexto("ime.schemas.guardar.novo")%>"> &nbsp; &nbsp;<a class="menu" style="float: right;  font-size: 80% " href="<%=Urls.urlAppBase%>lang.do?action=manage.schemas&schema.tipo=<%=tipo %>"><%=u.getTexto("ime.schema.novo") %></a></label>
				
		<label style="display: block; padding-right: 50%;">
	 	<input  style="width: 100%;" type="text" name="schema.chave" value="<%=ct!=null?ct.getChave():""%>"/> 
	 	</label>
	</fieldset>

	
	<fieldset>
		<legend><%=u.getTexto("ime.schemas.texto.leg")%></legend>
		<label style="display: block;">
	 	<textarea style="width: 100%;" name="schema.texto" rows=20><%=ct!=null?(ct.getTexto()==null?"":ct.getTexto()):""%></textarea>
	 	</label>
	</fieldset>
	
	
					<input type="hidden" name="idSchema" value="<%=ct!=null?ct.getId():0%>" />
					<input type="hidden" name="action" value="manage.schemas.save" />
					<input type="hidden" name="schema.tipo" value="<%=tipo %>" />



</form>
</div>

<div id="vsplitbar"></div>
