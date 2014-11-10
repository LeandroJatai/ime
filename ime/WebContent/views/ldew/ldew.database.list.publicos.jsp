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

			if (userConnect == null)
				return;
		%>		
    <fieldset>
    <legend><img src="<%=Urls.url_av_Servlet%>/imgs/ime/uol16.png"/><%=userConnect.getTexto("ime.projetos.publicos")%></legend>

    
<ul class="arvoreRaiz">
	<%
		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);

		List<Usuario> lUsers = UsuarioController.getUsuarios();		
		
		for (Usuario user: lUsers) {
			
			if (!UsuarioController.checkPermissao(user, "ldep.database.publicar"))
		continue;	
			
		List<Projeto> lProjetos = ProjetoController.getProjetos(user);
		
		int countPublicos = 0;
		for (Projeto p : lProjetos)
			if (p.getPublico())
		countPublicos++;
	%>
		
	    <li> <a class="arvoreMais" href="#"> </a>
		    
	         <a style="font-size: 110%; font-style: italic;" title="TODO: Acessar Perfil público do usuário."><%=user.getNome()%> <b style="font-size: 70%; color:#800;">(<%=countPublicos%>)</b></a>
	   
		    <ul class="ulFechado">
  		
		
		
    	<%
  						    		for (Projeto p : lProjetos) { 

  						    		if (!p.getPublico())
  						    			continue;
  						    	%>    	
   		
   		    <li class="list">
				
   		    	<a title="<%=userConnect.getTexto("ime.projetos.abrir.titulo") %>" href="app.do?action=ldep.database.open&id=<%=p.getId() %>" ><%=p.getTitulo() %></a>
		    </li>
   		
    	<% 
    	} 
    	%>
    	</ul>
    	</li>
		<%
		}
    	%>		
</ul>
    </fieldset>
  