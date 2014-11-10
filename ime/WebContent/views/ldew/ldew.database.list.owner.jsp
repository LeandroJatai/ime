<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.dto.Projeto"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="org.imsglobal.jaxb.ld.Environment"%>
<%@page import="org.imsglobal.jaxb.ld.ActivityStructure"%>
<%@page import="org.imsglobal.jaxb.ld.SupportActivity"%>
<%@page import="org.imsglobal.jaxb.ld.LearningActivity"%>
<%@page import="br.edu.ifg.ime.suport.LearningDesignUtils"%>
<%@page import="java.io.Serializable"%>
<%@page import="br.edu.ifg.ime.controllers.ActivityController"%>
<%@page import="org.imsglobal.jaxb.ld.Staff"%>
<%@page import="br.edu.ifg.ime.controllers.RolesController"%>
<%@page import="br.edu.ifg.ime.controllers.ProjetoController"%>
<%@page import="org.imsglobal.jaxb.ld.LearningDesign"%>
<%@page import="org.imsglobal.jaxb.ld.Learner"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

			<%=Suport.getMessageWarning(request)%>
		<%
			Usuario userConnect = UsuarioController.getUsuarioConectado(session);
		%>
			
    <fieldset>
    <legend><img src="<%=Urls.url_av_Servlet%>/imgs/ime/uol16.png"/><%=userConnect.getTexto("ime.projetos.meus")%></legend>

    
<ul class="arvoreRaiz">
		
		<%
					List<Projeto> lProjetos = ProjetoController.getProjetos(userConnect);
								
								ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
								
						    	for (Projeto p : lProjetos) {
				%>    	
   		
   		    <li class="list">
   		    
   		    	<a class="controls-objs right" href="app.do?action=ldep.database.remove&id=<%=p.getId() %>" title="<%=userConnect.getTexto("at.db.ldep.btn.excluir.titulo") %>"><%=userConnect.getTexto("at.db.ldep.btn.excluir") %></a>


				<% if (userConnect.hasPermissoes("ldep.database.publicar")) {%> 
					<% if (p.getPublico()) {%> 
	   		    	<a class="controls-objs right" href="app.do?action=ldep.database.publicar&value=false&id=<%=p.getId() %>"><%=userConnect.getTexto("at.db.ldep.btn.privado") %></a>
	                <%} else {%> 
	   		    	<a class="controls-objs right" href="app.do?action=ldep.database.publicar&id=<%=p.getId() %>"><%=userConnect.getTexto("at.db.ldep.btn.publicar") %></a>
	                <%} %>
                <%} %>

   		   		<a title="<%=userConnect.getTexto("ime.projetos.abrir.titulo") %>" href="app.do?action=ldep.database.open&id=<%=p.getId() %>" ><%=p.getTitulo() %> - <%=p.getPublico()?"<span class=\"itemPub\">"+userConnect.getTexto("at.db.ldep.proj.pub")+"</span>":"<span class=\"itemPriv\">"+userConnect.getTexto("at.db.ldep.proj.priv")+"</span>" %></a>

		    </li>
   		
    	<% 
    	} 
    	%>		
</ul>
    </fieldset>
  