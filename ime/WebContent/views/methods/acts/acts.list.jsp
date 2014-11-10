<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="org.imsglobal.jaxb.ld.Act"%>
<%@page import="br.edu.ifg.ime.controllers.PlaysController"%>
<%@page import="br.edu.ifg.ime.controllers.MethodController"%>
<%@page import="org.imsglobal.jaxb.ld.Method"%>
<%@page import="org.imsglobal.jaxb.ld.Play"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="org.imsglobal.jaxb.ld.Environment"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
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
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>

<%
	Usuario u = UsuarioController.getUsuarioConectado(session);

	String ldep = Suport.r(request, "ldep");
    String playRef = Suport.r(request, "play-ref");
	Play play = PlaysController.getPlayByIdentifier(request, playRef);
    List<Act> lActs = play.getActList();
%>
    <fieldset>
    <legend><%=u.getTexto("at.mt.act.list.leg")%>: <strong><%=play.getTitle()%></strong> <%=u.getTexto("at.mt.act.list.of.leg")%> <b><%=ImeWorkspace.getImeWorkspace(request).getLdProject(request).getLd().getTitle()%></b></legend>



    <%  if ((u.hasPermissoes("act.edit.new") || u.hasPermissoes("act.new.for.mi")) && play != null) { %>
    <fieldset>
	
 					<% if (u.hasPermissoes("act.new.for.mi") && play != null) { %>
						<span style="float: right; padding-right: 5px;"> <a class="controls"
							  href="<%=Urls.urlAppBase%>act.do?action=act.new.for.mi&play-ref=<%=play.getIdentifier()%>&ldep=<%=ldep%>"
							     title="<%=u.getTexto("at.mt.act.mi.btn.novo.titulo") %>"><%=u.getTexto("at.mt.act.mi.btn.novo.ico") %></a></span>	
		 	
						<%} if (u.hasPermissoes("act.edit.new") && play != null) { %>
							<span style="float: right; padding-right: 5px;"> <a class="controls"
								  href="<%=Urls.urlAppBase%>act.do?action=act.edit.new&play-ref=<%=play.getIdentifier()%>&ldep=<%=ldep%>"
									           title="<%=u.getTexto("at.mt.act.btn.novo.titulo") %>"><%=u.getTexto("at.mt.act.btn.novo.ico") %></a></span>
						<%} %>
    </fieldset>
    <% } %>






    <%  for (Act act : lActs)  { 
    
    	if (act.getLearningDesignRef() != null) {
    	    
    	    
    	    %>
    			    <label class="list">
    		
 		<%
			if (u.hasPermissoes("act.remove.agregado")) {
		%> <a
			class="controls-obj right"
			href="act.do?action=act.remove.agregado&act-ref=<%=act.getIdentifier()%>&ldep=<%=ldep%>&play-ref=<%=playRef%>"
			title="Remover composição de MI">[-]</a><%	} %>
			
    		
    			    <a href="act.do?action=act.edit&play-ref=<%=play.getIdentifier() %>&identifier=<%=act.getIdentifier() %>&ldep=<%=ldep %>"><%=((LearningDesign)act.getLearningDesignRef().getRef()).getTitle() %></a>
    			    </label>								
    			<% }
    	else {
    		    
    		    
    		    %>
    				    <label class="list">
    				    
    				    	
 		<%
			if (u.hasPermissoes("act.remove.agregado")) {
		%>
    				    <a class="controls-objs right" href="act.do?action=act.remove&play-ref=<%=play.getIdentifier() %>&identifier=<%=act.getIdentifier() %>&ldep=<%=ldep %>" title="Excluir">[-]</a>
    		<%} %>		    
    				    
    				    <a href="act.do?action=act.edit&play-ref=<%=play.getIdentifier() %>&identifier=<%=act.getIdentifier() %>&ldep=<%=ldep %>"><%=act.getTitle() %></a>
    				    </label>								
    				<% }
    		    	
    }%>
    <br>
    

 
    </fieldset>
