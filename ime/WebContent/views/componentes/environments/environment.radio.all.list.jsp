<%@page import="br.edu.ifg.ime.controllers.Lc"%>
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
<%@page import="br.edu.ifg.ime.controllers.ProjetoController"%>
<%@page import="org.imsglobal.jaxb.ld.LearningDesign"%>
<%@page import="org.imsglobal.jaxb.ld.Learner"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    
    
    <%
            	List<Environment> lEnvs = EnvironmentsController.getEnvironments(request).getEnvironmentList();

            	String identifier = Suport.r(request, "identifier"); // Objeto que fará referencia a ambientes
            %>
    <fieldset>
    <legend><img src="<%=Urls.url_av_Servlet%>/imgs/ime/environment16.png"/><%=Lc.getTexto(session, "at.cp.env.tit.leg")%></legend>
    
    
    <%
            	for (Environment env : lEnvs)  {
                                                    	
                                                    	Boolean checked = false;
                                                    	
                                                    	checked = ImeWorkspace.getImeWorkspace(request).existeVinculo(env.getIdentifier(), identifier);
            %>
    
    		<label class="list"><input type="radio" name="parteSelecionada" value="<%=env.getIdentifier()%>" <%=checked?"checked":"" %>/><%=env.getTitle() %></label>								
		<% } %>
    
    </fieldset>
