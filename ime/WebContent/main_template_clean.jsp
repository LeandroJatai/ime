
<%@page import="br.edu.ifg.ime.dto.Perspectiva"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="<%=Urls.urlAppBase%>js/jquery.min.js"></script>
<script type="text/javascript" src="<%=Urls.urlAppBase%>js/jquery.caret.1.02.js"></script>
<script type="text/javascript" src="<%=Urls.urlAppBase%>js/jquery-ui-1.10.3.custom.min.js"></script>

<script type="text/javascript" src="<%=Urls.urlAppBase%>js/lib.js"></script>
<link rel="stylesheet" href="<%=Urls.urlAppBase%>css/lib.css">

<link rel="shortcut icon" href="<%=Urls.urlAppBase%>image/1.jpg" >

<%
	Usuario usuario = UsuarioController.getUsuarioConectado(session);
%>
<title><%=usuario.getTexto("ime.titulo") %></title>

</head>
<body>


	 <jsp:include page="main_topo.jsp"></jsp:include>


<div id="globalWorks">

		   <%String jspInclude = (String)request.getAttribute("jspInclude"); %>
		   <jsp:include page="<%=jspInclude %>"></jsp:include>
		   
</div>

	 <jsp:include page="main_rodape.jsp"></jsp:include>

</body>
</html>