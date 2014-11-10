<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.imsglobal.jaxb.ld.Service"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="org.imsglobal.jaxb.ld.LearningObject"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="java.io.Serializable"%>
<%@page import="br.edu.ifg.ime.suport.LearningDesignUtils"%>
<%@page import="org.imsglobal.jaxb.ld.RoleRef"%>
<%@page import="org.imsglobal.jaxb.ld.SupportActivity"%>
<%@page import="org.imsglobal.jaxb.ld.EnvironmentRef"%>
<%@page import="java.util.List"%>
<%@page import="org.imsglobal.jaxb.ld.LearningActivity"%>
<%@page import="org.imsglobal.jaxb.ld.Environment"%>
<%@page import="org.imsglobal.jaxb.ld.ActivityStructure"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
    	if (ImeWorkspace.getImeWorkspace(request).constructViewTree >= 32)  {
        	return;
        }

	ImeObject imeO;


        ImeWorkspace.getImeWorkspace(request).constructViewTree++;

        String identifier = Suport.r(request, "identifier");
        String strLdep = Suport.r(request, "ldep");

        Object objRef = request.getAttribute("objRef");

        request.removeAttribute("objRef");

        if (objRef instanceof Environment) { 
        	Environment env0 = (Environment)objRef;
        	
    %>
    
    
				<%	List<EnvironmentRef> lEnvRef = new ArrayList<EnvironmentRef>();
				    EnvironmentRef envvRef = new EnvironmentRef();
				    envvRef.setRef(env0);
				    lEnvRef.add(envvRef);
				    
				%>						
			<%@include file="arvoreENVEdition.jsp" %>
	

<%
 	} else if (objRef instanceof LearningActivity) { 
      LearningActivity la = (LearningActivity) objRef;
 %>
	<li class="activities"  onmouseover="liHover();" onmouseout="liOut();">
	   <a class="<%=la.getIdentifier().equals(identifier) ? "selectedEdit" : ""%> learning-activity"
		href="activities.do?action=activities.edit.la&ldep=<%=strLdep%>&identifier=<%=la.getIdentifier()%>"><%=la.getTitle()%></a>
			
								<% imeO = (ImeObject) la;  %>
								<%@include file="../../../admin/iconsValidateImsLd.jsp"%>								
	
				<%	List<EnvironmentRef> lEnvRef = la.getEnvironmentRefList();%>						
						<%@include file="arvoreENVEdition.jsp" %>
	</li> 


<%
 	} else if (objRef instanceof SupportActivity) { 
  	SupportActivity sa = (SupportActivity) objRef;
 %>
	<li class="activities"  onmouseover="liHover();" onmouseout="liOut();">
	
	   <a class="support-activity <%=sa.getIdentifier().equals(identifier) ? "selectedEdit" : ""%>"
		href="activities.do?action=activities.edit.sa&ldep=<%=strLdep%>&identifier=<%=sa.getIdentifier()%>"><%=sa.getTitle()%></a>
								<% imeO = (ImeObject) sa;  %>
								<%@include file="../../../admin/iconsValidateImsLd.jsp"%>				
			<ul>
			    <%
			    	List<RoleRef> lRoleRef = sa.getRoleRefList();
			    	    		for (RoleRef roleRef: lRoleRef) {
			    	    			Object role = roleRef.getRef();
			    %>
				    
					<li class="learner staff" onmouseover="liHover();" onmouseout="liOut();">
					   <a class="<%=role.getClass().getSimpleName().toLowerCase()%> <%=LearningDesignUtils.getIdentifier(role).equals(identifier) ? "selectedEdit" : ""%>"
						href="roles.do?action=roles.edit&ldep=<%=strLdep%>&identifier=<%=LearningDesignUtils.getIdentifier(role)%>">
						<%=role.getClass().getMethod("getTitle").invoke(role)%></a>
						<% imeO = (ImeObject) role;  %>
								<%@include file="../../../admin/iconsValidateImsLd.jsp"%>	
						<span class="<%=role.getClass().getSimpleName().toLowerCase()%> right"> </span>
					</li>
				<%
					}
				%>
			</ul>
			
				<%	List<EnvironmentRef> lEnvRef = sa.getEnvironmentRefList();%>						
						<%@include file="arvoreENVEdition.jsp" %>
						
			
	</li> 
	
<%
 		} else if (objRef instanceof ActivityStructure) { 
 	 		ActivityStructure as = (ActivityStructure) objRef;
 	%>
	<li class="activities" onmouseover="liHover();" onmouseout="liOut();">
	   <a class="activity-structure <%=as.getIdentifier().equals(identifier) ? "selectedEdit" : ""%>"
		href="activities.do?action=activities.edit.as&ldep=<%=strLdep%>&identifier=<%=as.getIdentifier()%>"><%=as.getTitle()%></a>
			
												<% imeO = (ImeObject) as;  %>
								<%@include file="../../../admin/iconsValidateImsLd.jsp"%>	
			
			
				<%	List<EnvironmentRef> lEnvRef = as.getEnvironmentRef();%>						
						<%@include file="arvoreENVEdition.jsp" %>
			
			
			<ul>
				
				<%
									List<Serializable> lActivitiesRef = as.getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref();
																for (Serializable childObRef: lActivitiesRef) {
																	
																	if (LearningDesignUtils.getIdentifier(objRef).equals(  LearningDesignUtils.getIdentifier(LearningDesignUtils.getRef(childObRef))  ))
																		continue;
																	

																	request.setAttribute("objRef", LearningDesignUtils.getRef(childObRef));
																	String iddRef =  Suport.utfToIso(identifier);
																	String link = "arvoreASEdition.jsp?ldep="+strLdep+"&identifier="+iddRef;
								%>								
						<jsp:include page="<%=link%>"></jsp:include>
				
				<%
									}
								%>
				
				
				
			</ul>
	</li> 


<%
 	}

  ImeWorkspace.getImeWorkspace(request).constructViewTree--;
 %>
								