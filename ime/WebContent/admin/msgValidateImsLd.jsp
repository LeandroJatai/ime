<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<% 
if (imeO != null) imeO.validateImsLd();
if (Suport.existErrorOrWarningIn(imeO)) {%>
	<fieldset>
	<legend>*Erros e Notificações na Validação IMS-LD</legend> 
	<ul>
		<% 
		if (imeO != null) for (String msg: imeO.getERRORs()) { %>
			<li class="list"><a><img src="<%=Urls.urlAppBase %>imgs/iconError16.png"> <%= msg%></a></li>
		<% } %>
		<% if (imeO != null) for (String msg: imeO.getWARNINGs()) { %>
			<li class="list"><a><img src="<%=Urls.urlAppBase %>imgs/iconWarning16.png"> <%= msg%></a></li>
		<% } %> 
	</ul>
	</fieldset>
<% } %> 