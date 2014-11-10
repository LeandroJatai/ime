<%@page import="br.edu.ifg.ime.controllers.Lc"%>
<%@page import="org.imsglobal.jaxb.ld.EnvironmentRef"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
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
<%@page
	import="br.edu.ifg.ime.controllers.ProjetoController"%>
<%@page import="org.imsglobal.jaxb.ld.LearningDesign"%>
<%@page import="org.imsglobal.jaxb.ld.Learner"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>



<%
	List<Environment> lEnvs = EnvironmentsController.getEnvironments(request).getEnvironmentList();
    
    String identifier = Suport.r(request, "identifier");
	String idRef = Suport.r(request, "id-ref");

	if (idRef != null) {
		identifier = idRef;
	}
	if (identifier == null)
		identifier = "";
	
	List<EnvironmentRef> lEnvRef = EnvironmentsController.getEnviromentsRef(
	ImeWorkspace.getImeWorkspace(request).getObject(identifier) );
%>
<fieldset id="envList">
	<legend><%=Lc.getTexto(session, "at.cp.env.s.leg")%></legend>
	<%
				
			for (EnvironmentRef envRef : lEnvRef) {

					Environment env = (Environment) LearningDesignUtils.getRef(envRef);
					
					if (env == null)
						continue;
 
		%>

		<label class="list">
		 <span class="left" style="padding-right: 5px;"> 
		    <a class="controls" title="<%=Lc.getTexto(session, "at.msg.move.for.seq")%>">&#8661;</a>
		 </span>
		
			
		<input type="checkbox" name="environment-ref" value="<%=env.getIdentifier()%>" checked /><%=env.getTitle()%></label>

		<%
			
			}
		%>
		<% boolean checked = false;
			for (Environment env : lEnvs) {
				
					checked = false;
					

					for (EnvironmentRef envRef : lEnvRef) {

						Environment envSelect = (Environment) LearningDesignUtils.getRef(envRef);
							
							if (envSelect == env) {
								checked = true;
								break;
							}
						
					}
					if (checked)
						continue;

		%>

		<label class="list">
		<span class="left" style="padding-right: 5px;"> 
		    <a class="controls" title="<%=Lc.getTexto(session, "at.msg.move.for.seq")%>">&#8661;</a>
		 </span>
		 <input type="checkbox"
			name="environment-ref" value="<%=env.getIdentifier()%>" /><%=env.getTitle()%></label>

		<%
			
			}
		%>
	

</fieldset>
