<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
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
    String identifier = Suport.r(request, "identifier");     

    
    if (identifier == null)
    	identifier = "";
    
    Environment env = EnvironmentsController.getEnviromentByIdentifier(request, identifier);

     List<LearningObject> lLos = EnvironmentsController.getLearningObjects(env);
    
    Item itemSelected = null;
    
    String action = Suport.r(request, "action");
     
%>
      <fieldset>
   <legend><%=u.getTexto("at.cp.env.lo.lista.leg")%></legend>
 		   <%
 		   	for (LearningObject lo : lLos)  {
 		       	  
 		   %>
		    <label class="list">
		    <%
		    	if (u.hasPermissoes("env.lo.remove")) {
		    %>
		        <a class="controls-objs right" href="environment.do?action=env.lo.remove&identifier=<%=lo.getIdentifier()%>&id-env=<%=env.getIdentifier()%>&ldep=<%=ldep%>" title="<%=u.getTexto("at.cp.env.lo.btn.remove.tit")%>"><%=u.getTexto("at.cp.env.lo.btn.remove.leg")%></a>
		    <%
		    	}
		    %>
	
		    		        <a  href="environment.do?action=env.lo.edit&id-env=<%=env.getIdentifier()%>&ldep=<%=ldep%>&id-ref=<%=lo.getIdentifier()%>"><%=lo.getTitle()%>
   											             <% ImeObject imeO = (ImeObject) lo;  %>
           											 <%@include file="../../../admin/iconsValidateImsLd.jsp"%>
		    		        </a>

		       </label>								
		<%
											}
										%> <br>
 	    <%
		    	if (u.hasPermissoes("env.lo.edit.new")) {
		    %>
		      <a href="environment.do?action=env.lo.edit.new&id-env=<%=env.getIdentifier()%>&ldep=<%=ldep%>"><%=u.getTexto("at.cp.env.lo.btn.novo")%></a>
		 	<%
		    	}
		    %>
		       	</fieldset>
     
   	
   	
   	