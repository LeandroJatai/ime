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
	<legend><%=u.getTexto("ime.users.lang.leg") %></legend>
	
<%
	String strIdLang = request.getParameter("idLang");
	
	List<Linguagem> listaLinguagens= LinguagemController.getLinguagens();
	Linguagem lang = null;
	for (Linguagem auxLang: listaLinguagens) {
		if (auxLang.getId() == Integer.parseInt(strIdLang==null?"0":strIdLang))
			lang = auxLang;
	
	%>
	
	<label class="list">
	 		<a class="controls-objs right" href="lang.do?action=manage.language.remove&idLang=<%=auxLang.getId() %>" title="<%=u.getTexto("ime.users.lang.excluir.tit") %>"><%=u.getTexto("ime.users.lang.excluir.btn") %></a>
		 
	<a class="<%=lang != null && lang.getId() == auxLang.getId()?"selected":"" %>"  href="<%=Urls.urlAppBase%>lang.do?action=manage.language&idLang=<%=auxLang.getId()%>">
	<%= auxLang.getTitulo() %></a>
	</label>
	
	<% } %>
	
	</fieldset>
	
</div>

<% 

TreeMap<String, String> textosLang = null;

textosLang = LinguagemController.getTextos(lang);


%>


<div id="RightPane">

<%=Suport.getMessageWarning(request)%>

        <form action="<%=Urls.urlAppBase %>lang.do" method="post"> 
        
        
	
	
	<fieldset>
		<legend><%=u.getTexto("ime.users.lang.tit.leg") %></legend>
		<label style="display: block; padding-right: 10px;">
	 	<input  style="width: 100%;" type="text" name="lang.titulo" value="<%=lang!=null?lang.getTitulo():""%>"/> 
	 	</label>
	</fieldset>
					<fieldset class="controles">
						<label><input type="submit" value="<%=lang!=null?u.getTexto("ime.users.lang.guardar"):u.getTexto("ime.users.lang.guardar.novo")%>"> &nbsp; &nbsp;<a class="menu" style="float: right;  font-size: 80% " href="<%=Urls.urlAppBase%>lang.do?action=manage.language"><%=u.getTexto("ime.users.lang.nova") %></a></label>
					</fieldset>
	

					<input type="hidden" name="idLang" value="<%=lang!=null?lang.getId():0%>" />
					<input type="hidden" name="action" value="manage.language.save" />


<fieldset>

<style type="text/css">

tr td, tr th {
    border: 1px solid #aaa;
    padding: 5px; 
}
tr th {
text-align: center;
}

</style>

<table style="width: 100%; border: 1px solid #aaa; font-size: 10pt;">
<thead>
	<tr>
	<th><%=u.getTexto("ime.users.lang.tit.texto") %></th>
	<th><%=u.getTexto("ime.users.lang.tit.atual") %></th>
	<th><%=u.getTexto("ime.users.lang.tit.padrao") %></th>
	</tr>
</thead>
<tbody>

<% for (Chave_textual ct: LinguagemController.getChavesTextuais()) { 
	
	
	if (ct.getTipo() != 1 && ct.getTipo() != 2)
		continue;

	String textUserConnect = u.getTexto(ct.getChave());
	String textCtLang = null;
	
	if (textosLang != null)
		textCtLang = textosLang.get(ct.getChave()); 
%>

	<tr>
	<td style="width: 50%; ">

<label style="display:block; padding-right: 10px;">
	<input type="hidden" name="lang.chave" value="<%=ct.getChave() %>" />
	<input style="width: 100%;" type="text" name="lang.texto" value="<%= textCtLang != null ? textCtLang: ""%>"/>
</label>
	</td>
	<td style="width: 25%;"><%=textUserConnect.equals(ct.getTexto())?"":textUserConnect %></td>
	<td style="width: 25%;"><%=ct.getTexto() %></td>
	</tr>

<%} %>
</tbody>


</table>
</fieldset>



</form>
</div>

<div id="vsplitbar"></div>
