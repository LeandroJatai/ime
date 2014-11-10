<%@page import="javax.xml.bind.ValidationEventLocator"%>
<%@page import="javax.xml.bind.ValidationEvent"%>
<%@page import="java.util.List"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="org.imsglobal.jaxb.ld.Learner"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="org.imsglobal.jaxb.ld.LearningActivity"%>
<%@page import="br.edu.ifg.ime.controllers.ActivityController"%>
<%@page import="org.imsglobal.jaxb.ld.Activities"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

			<%=Suport.getMessageWarning(request)%>

<%

Usuario u = UsuarioController.getUsuarioConectado(session);

Object ob = request.getAttribute("errosExport");
request.removeAttribute("errosExport");
List<ValidationEvent> errors = null;

if (ob != null && ob instanceof List) {
	errors = (List<ValidationEvent>)ob;	
}
	

%>

<fieldset>
	<legend>Pendências para exportação</legend>
									
					     	

		   		    <ul class="arvoreRaiz">
		   		    
		   		    <%
		   		    if (errors != null)
		   		    for (ValidationEvent ve: errors) {
		   		    	
		   		    	ValidationEventLocator vel = ve.getLocator();
		   		    	
		   		    	ob = vel.getObject();
		   		    	
		   		    	
		   		    	%>
		   		    
							<li class="list"><a href="#" title="teste">Elementos em: <%=vel.getObject().getClass().getCanonicalName() %></a></li>

					<%} %>
		 		</ul> 
				
</fieldset>			&nbsp; &nbsp; &nbsp; (Rotina em desenvolvimento)
				