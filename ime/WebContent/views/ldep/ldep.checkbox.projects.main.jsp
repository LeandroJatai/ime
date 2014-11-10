<%@page import="br.edu.ifg.ime.ld.LearningDesignRef"%>
<%@page import="org.imsglobal.jaxb.ld.LearningActivityRef"%>
<%@page import="br.edu.ifg.ime.controllers.ActsController"%>
<%@page import="org.imsglobal.jaxb.ld.Act"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
   <%
       	String identifier = Suport.r(request, "identifier");
              	String playRef = Suport.r(request, "play-ref");
              	String ldep = Suport.r(request, "ldep");
              	String titulo = "";
              	
              	Act act = ActsController.getActByIdentifier(request, identifier);
       %>
    	<%
    		for (LdProject obLdep : ImeWorkspace.getImeWorkspace(request).getLdProject(request).getLdAgregados()) {
    	    	    	    			
    	    	    	    			if (act.getLearningDesignRef().getRef() != obLdep.getLd()) {			
    	    	    	    				continue;
    	%>    	
   		<label class="list"><input type="checkbox" name="ld-ref" value="<%=obLdep.getIdentifier()%>" checked/><%=obLdep.getLd().getTitle()%></label>								
		<%
											} }
										%>			<%
				for (LdProject obLdep : ImeWorkspace.getImeWorkspace(request).getListLdProject()) { 
										          		
										        		  if (obLdep == ImeWorkspace.getImeWorkspace(request).getLdProject(request) || obLdep.isAgregado())
										        			  continue;
										        		       
										        		  //boolean checked = false;
										        		  //checked = LdEditorWorkspace.getLdPlayerWorkspace(request).existeVinculo(obLdep.getIdentifier(), identifier);
			%>    	
   		<label class="list"><input type="checkbox" name="ld-ref" value="<%=obLdep.getIdentifier()%>" /><%=obLdep.getLd().getTitle()%></label>								
		<%} %>		
   