
<%@page import="br.edu.ifg.ime.dto.Perspectiva"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	Usuario usuario = UsuarioController.getUsuarioConectado(session);

String action = request.getParameter("action");
%>

<div id="globalCab">

<span style="font-size:9pt; "><a href="#" class="titulo" title="<%=usuario.getTexto("ime.titulo")%>"><%=usuario.getTexto("ime.titulo")%></a></span>
	<%
		if (usuario.getId() == Usuario.wmx_convidado) {
	%>
        <a class="titulo" style="font-size:9pt; float:right;" href="usuario.do" title="<%=usuario.getTexto("ime.acesso.titulo")%>"><%=usuario.getTexto("ime.acesso")%></a><br>
    <%
    	} else {
    %> 
         <a class="titulo" style="font-size:9pt; float:right;" href="<%=Urls.urlAppBase%>usuario.do?action=usuario_logout"><%=usuario.getTexto("ime.sair")%></a>
         
          <span class="titulo" style="font-size: 8pt;color:#ff0; float: right; padding-top: 2px;"><a  style="font-size: 8pt;color:#ff0;" href="<%=Urls.urlAppBase%>usuario.do?action=manage.users.perspectiva
         "><%=usuario.getNome()%></a></span> 

         
    <%
    	}
    %>


</div>
<div id="globalTop">
		<a class="menu <%=action==null?"selected":"" %>" href="<%=Urls.urlAppBase%>">&nbsp;<%=usuario.getTexto("at")%></a>
		
	<% if (UsuarioController.checkPermissao(request, "ldep.database.list.owner")) { %>
	<a class="menu <%=action!=null && action.equals("ldep.database.list.owner")?"selected":"" %>" href="<%=Urls.urlAppBase%>app.do?action=ldep.database.list.owner"> &nbsp;<%=usuario.getTexto("ime.projetos.meus")%></a>
	
    <% } if (UsuarioController.checkPermissao(request, "ldep.database.list.publicos")) { %>
	<a class="menu <%=action!=null && action.equals("ldep.database.list.publicos")?"selected":"" %>" href="<%=Urls.urlAppBase%>app.do?action=ldep.database.list.publicos"> &nbsp;<%=usuario.getTexto("ime.projetos.publicos")%></a>
	
    <% } if (UsuarioController.checkPermissao(request, "manage.users")) { %>
	<a class="menu" style="float: right;  font-size: 80% " href="<%=Urls.urlAppBase%>usuario.do?action=manage.users">[<%=usuario.getTexto("ime.usuarios")%>] &nbsp;</a>
	
    <% } if (UsuarioController.checkPermissao(request, "manage.language")) { %>
	<a class="menu" style="float: right;  font-size: 80% " href="<%=Urls.urlAppBase%>lang.do?action=manage.language">[<%=usuario.getTexto("ime.linguagem")%>] &nbsp;</a>
	 
    <% } if (UsuarioController.checkPermissao(request, "manage.arquivo")) { %>
	<a class="menu" style="float: right;  font-size: 80% " href="<%=Urls.urlAppBase%>av?action=manage.arquivo">[<%=usuario.getTexto("ime.arquivo")%>] &nbsp;</a>
	
    <% 	} if (usuario != null && usuario.getL_perspectivas().size() > 0) { %>
    
	<a class="menu <%=usuario.wmx_perspectiva==null?"selected":"" %> right"  style="font-size: 80%;" href="<%=Urls.urlAppBase%>usuario.do?action=manage.users.perspectiva.open" title="<%=usuario.getTexto("ime.perspectiva.padrao.titulo")%>" ><%=usuario.getTexto("ime.perspectiva.padrao")%> &nbsp;</a>
	
	
	<span class="right" style="font-size: 80%;">
	<%
	for (Perspectiva pp: usuario.getL_perspectivas()) {
	%>	
			<a class="menu <%=usuario.wmx_perspectiva!=null && usuario.wmx_perspectiva.getId() == pp.getId()?"selected":"" %>" href="<%=Urls.urlAppBase%>usuario.do?action=manage.users.perspectiva.open&idPerspectiva=<%=pp.getId()%>" title="<%=usuario.getTexto("ime.perspectiva.usuario.titulo")%>" >[<%=pp.getTitulo() %>] &nbsp;</a>
	
		<%
	}
	}
    %>	
    </span>
</div>
