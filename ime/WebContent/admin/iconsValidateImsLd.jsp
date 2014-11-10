<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%if (imeO != null) imeO.validateImsLd();
if (imeO.getERRORs().size() > 0) { %>
   	<img src="<%=Urls.urlAppBase %>imgs/iconError14.png">
<%} 			
if (imeO.getWARNINGs().size() > 0) { %>
   	<img src="<%=Urls.urlAppBase %>imgs/iconWarning14.png">
   <% } %>