<%@page import="br.edu.ifg.ime.controllers.Lc"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.controllers.ActsController"%>
<%@page import="org.imsglobal.jaxb.ld.Act"%>
<%@page import="org.imsglobal.jaxb.ld.Play"%>
<%@page import="br.edu.ifg.ime.controllers.PlaysController"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="org.imsglobal.jaxb.ld.Environment"%>
<%@page import="br.edu.ifg.ime.controllers.RolesController"%>
<%@page import="org.imsglobal.jaxb.ld.Staff"%>
<%@page import="org.imsglobal.jaxb.ld.Learner"%>
<%@page import="br.edu.ifg.ime.controllers.ProjetoController"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

			<%=Suport.getMessageWarning(request)%>
			
<%
	String identifier = Suport.r(request, "identifier");
	String playRef = Suport.r(request, "play-ref");
	String ldep = Suport.r(request, "ldep");
	String titulo = "";
	
	Act act = ActsController.getActByIdentifier(request, identifier);
	
	if (act != null) {
		titulo = StringEscapeUtils.escapeHtml4(act.getTitle());
	}
	
	if (identifier == null) identifier = "";
	
%>
			
			
    <form action="<%=Urls.urlAppBase%>act.do" method="post">

			<input type="hidden" name="action" value="act.save"/>
			<input type="hidden" name="play-ref" value="<%=playRef%>"/>
			<input type="hidden" name="ldep" value="<%=ldep%>"/>
			<input type="hidden" name="identifier" value="<%=identifier%>"/>
			<input type="hidden" name="comp-mi" value="true"/>
			
	    <fieldset>
	    <legend><img src="<%=Urls.url_av_Servlet %>/imgs/ime/uol16.png"/><%=Lc.getTexto(session, "at.play.mi.comp.leg") %></legend>
		     <jsp:include page="/views/ldep/ldep.radio.projects.main.jsp"></jsp:include>
	    </fieldset>
	    
	    
			<fieldset class="controles">
				<label><input type="submit" value="<%=Lc.getTexto(session, "at.play.mi.comp.btn.guardar") %>"></label>
			</fieldset>
			
    </form>