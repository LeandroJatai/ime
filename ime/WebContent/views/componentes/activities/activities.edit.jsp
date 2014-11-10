<%@page import="br.edu.ifg.ime.controllers.Lc"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.controllers.ActivityController"%>
<%@page import="org.imsglobal.jaxb.ld.Activities"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

			<%=Suport.getMessageWarning(request)%>
	<%	
       String ldep = Suport.r(request, "ldep");
    %>
    <form action="<%=Urls.urlAppBase %>activities.do" method="post">
		<fieldset>
			<legend><%=Lc.getTexto(session,	"at.cp.at.cad.tit.leg") %></legend>
			
			<fieldset>
           	<legend><%=Lc.getTexto(session,	"at.cp.at.cad.titulo") %></legend>	          
           		<label style="width: 100%;" ><input type="text" name="titulo" tabindex="10" value="" size="30"/></label>
           	</fieldset>
			
			<fieldset>
           	<legend><%=Lc.getTexto(session,	"at.cp.at.cad.tipo") %></legend>	          
           		<label><input type="radio" name="tipo" value="learning-activity" tabindex="20" checked /> <img src="<%=Urls.url_av_Servlet %>/imgs/ime/learning_activity16.png"><%=Lc.getTexto(session, "at.cp.la.cad.tit.leg")%></label>&nbsp;&nbsp;&nbsp;&nbsp; 
           		<label><input type="radio" name="tipo" value="support-activity" tabindex="30" /> <img src="<%=Urls.url_av_Servlet %>/imgs/ime/support_activity16.png"><%=Lc.getTexto(session, "at.cp.sa.cad.tit.leg")%></label>&nbsp;&nbsp;&nbsp;&nbsp; 
           		<label><input type="radio" name="tipo" value="activity-structure" tabindex="40" /> <img src="<%=Urls.url_av_Servlet %>/imgs/ime/activity_structure16.png"><%=Lc.getTexto(session, "at.cp.as.cad.tit.leg")%></label>
           </fieldset>
          
				
			<fieldset class="controles">
				<label><input type="submit" value="<%=Lc.getTexto(session,	"at.cp.at.cad.guardar") %>"></label>
			</fieldset>
			
	    	<input type="hidden" name="ldep" value="<%=ldep%>" />
			<input type="hidden" name="action" value="activities.save"/>
			
		</fieldset>    
    
    
    
    </form>