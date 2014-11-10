<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="br.edu.ifg.ime.controllers.ArquivoController"%>
<%@page import="br.edu.ifg.ime.dto.Perspectiva"%>
<%@page import="org.apache.catalina.startup.UserConfig"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=Urls.urlAppBase%>js/jquery.min.js"></script>
<script type="text/javascript" src="<%=Urls.urlAppBase%>js/jquery.caret.1.02.js"></script>
<script type="text/javascript" src="<%=Urls.urlAppBase%>js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="<%=Urls.urlAppBase%>js/ckeditor/ckeditor.js"></script>


<%
	String ldep = Suport.r(request, "ldep");
LdProject obLdep = null;
ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
if ((obLdep = w.getLdProjectByIdentifier(ldep)) == null) {
	
	if (w.getMasterListLdProject().size() == 0) 
	   out.write(new String(ArquivoController.getBytesArquivo("/_skins/ime.skin")));
	else {
		obLdep = w.getMasterListLdProject().get(0);
		if (obLdep.skin == null || obLdep.skin.length() == 0)
		    out.write(new String(ArquivoController.getBytesArquivo("/_skins/ime.skin")));
		else 
	out.write(new String(ArquivoController.getBytesArquivo(obLdep.skin)));
	}
}
else {
	while (obLdep.parent != null)
		obLdep = obLdep.parent;
	
	if (obLdep.skin == null || obLdep.skin.length() == 0)
	    out.write(new String(ArquivoController.getBytesArquivo("/_skins/ime.skin")));
	else 	
     	out.write(new String(ArquivoController.getBytesArquivo(obLdep.skin)));
}

	Usuario usuario = UsuarioController.getUsuarioConectado(session);
	String action = request.getParameter("action");
	String pos = (String)session.getAttribute("ldep.vsplitbar");
%>

<title><%=usuario.getTexto("ime.titulo") %></title>

</head>
<body>


	      <jsp:include page="main_topo.jsp"></jsp:include>
	
<div id="panelWait">Carregando Informações e construíndo interface...</div>
<div id="globalWorks">

	 <jsp:include page="main_rodape.jsp"></jsp:include>
	 

<% if (usuario != null) { %>
	
	<div id="RightPane" <%=pos!=null?"style=\"left: "+(Integer.parseInt(pos)-3)+"px;\"":"" %>>
	
		   <%String jspInclude = (String)request.getAttribute("jspInclude"); %>
		   <jsp:include page="<%=jspInclude %>"></jsp:include>
		</div>

<% } %>
</div>
<div id="LeftPane" <%=pos!=null?"style=\"width: "+(Integer.parseInt(pos)-3)+"px;\"":"" %>>

	      <jsp:include page="arvore/arvoreMIConstructions.jsp"></jsp:include>
	
	</div>
	
   <div id="vsplitbar" <%=pos!=null?"style=\"left: "+(Integer.parseInt(pos)-3)+"px;\"":"" %>>
    </div>	
 
 
</body>
</html>