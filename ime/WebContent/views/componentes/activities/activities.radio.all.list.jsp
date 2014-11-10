<%@page import="br.edu.ifg.ime.controllers.Lc"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
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


	String identifier = Suport.r(request, "identifier"); // Objeto que fará referencia a ambientes
	

    List<Serializable> lActivities = ActivityController.getActivities(request).getLearningActivityOrSupportActivityOrActivityStructure();

	Boolean checked = false;	
	//checked = LdPlayerWorkspace.getLdPlayerWorkspace(request).existeVinculo(env.getIdentifier(), identifier);
	
    
    %>
    <fieldset>
    <legend><img src="<%=Urls.url_av_Servlet %>/imgs/ime/learning_activity16.png"/> <%=Lc.getTexto(session, "at.cp.la.tit.leg")%></legend>
		<%
			for (Serializable activity: lActivities) { 
				          		
				        	  if (LearningDesignUtils.isLearningActivity(activity)) {
				        		  LearningActivity la = (LearningActivity) activity;
				        		  
				        		  checked = ImeWorkspace.getImeWorkspace(request).existeVinculo(la.getIdentifier(), identifier);
		%>    	
          
    		<label class="list"><input type="radio" name="parteSelecionada" value="<%=la.getIdentifier()%>" <%=checked?"checked":""%>/><%=la.getTitle()%></label>								
	
		<%
												} }
											%>		
    </fieldset>
    
    <fieldset>
    <legend><img src="<%=Urls.url_av_Servlet%>/imgs/ime/support_activity16.png"/> <%=Lc.getTexto(session, "at.cp.sa.tit.leg")%></legend>
		<%
			for (Serializable activity: lActivities) { 
				          		
				        	  if (LearningDesignUtils.isSupportActivity(activity)) {
				        		  SupportActivity sa = (SupportActivity) activity;
				        		  
				        		  checked = ImeWorkspace.getImeWorkspace(request).existeVinculo(sa.getIdentifier(), identifier);
		%>    	
   		<label class="list"><input type="radio" name="parteSelecionada" value="<%=sa.getIdentifier()%>" <%=checked?"checked":""%>/><%=sa.getTitle()%></label>								
			<%
												} }
											%>		
    </fieldset>
    
    <fieldset>
    <legend><img src="<%=Urls.url_av_Servlet%>/imgs/ime/activity_structure16.png"/> <%=Lc.getTexto(session, "at.cp.as.tit.leg")%></legend>
		<%
			for (Serializable activity: lActivities) { 
				          		
				        	  if (LearningDesignUtils.isActivityStructure(activity)) {
				        		  ActivityStructure as = (ActivityStructure) activity;
				        		  
				        		  if (as.getIdentifier().equals(identifier))
				        			  continue;
				        		  
				        		  checked = ImeWorkspace.getImeWorkspace(request).existeVinculo(as.getIdentifier(), identifier);
		%>    	
   		<label class="list"><input type="radio" name="parteSelecionada" value="<%=as.getIdentifier()%>" <%=checked?"checked":""%>/><%=as.getTitle()%></label>								
			<%
												} }
											%>		
    </fieldset>
   