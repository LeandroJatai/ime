<%@page import="br.edu.ifg.ime.controllers.Lc"%>
<%@page import="br.edu.ifg.ime.controllers.LinguagemController"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 
<script type="text/javascript">

roleText = '<%=Lc.getTexto(request, "rodape.role.text")%>';
envText = '<%=Lc.getTexto(request, "rodape.env.text")%>';
activityText = '<%=Lc.getTexto(request, "rodape.activity.text")%>';
rpText = 	'<%=Lc.getTexto(request, "rodape.rp.text")%>';
</script>   	
<div id="globalBottom">
	<div id="cTree"></div>
		
</div>	