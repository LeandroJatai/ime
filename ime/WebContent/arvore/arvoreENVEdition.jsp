<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
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
	
			<ul>
				<%
																				for (EnvironmentRef envRef: lEnvRef) {
																					Environment env = (Environment)envRef.getRef();
										%>
				    
					<li  class="environment" onmouseover="liHover();" onmouseout="liOut();">
					   
					     <a class="environment-link<%=env.getIdentifier().equals(identifier) ? " selectedEdit" : ""%>"
						href="environment.do?action=environment.edit&ldep=<%=strLdep%>&identifier=<%=env.getIdentifier()%>"><%=env.getTitle()%></a>
						         <% imeO = (ImeObject) env;  %>
								<%@include file="../../../admin/iconsValidateImsLd.jsp"%>	
						 <span class="environment right"> </span>
						 
						 
						 
						 
						 
						 
						 
								
								
								
								
								<ul>
								    <%
								    	for (LearningObject lo : EnvironmentsController.getLearningObjects(env)) {
								    				    				    							
		    							
								    %>
									<li  onmouseover="liHover();" onmouseout="liOut(event);"><a
										class="learningObjects <%=lo.getIdentifier().equals(identifier) ? "selectedEdit"
									: ""%>"
										href="environment.do?action=env.lo.edit&ldep=<%=strLdep%>&identifier=<%=lo.getIdentifier()%>&id-env=<%=env.getIdentifier()%>"><%=lo.getTitle()%></a>
										 <% imeO = (ImeObject) lo;  %>
								<%@include file="../../../admin/iconsValidateImsLd.jsp"%>	
									</li>
									<%
										}
									%>
								</ul>
								
								
								
								
								<ul>
								    <%
								    
								    	for (Service sv : EnvironmentsController.getServices(env)) {
								    		    				    						 
								    %>
									<li  onmouseover="liHover();" onmouseout="liOut(event);"><a
										class="services <%=sv.getIdentifier().equals(identifier) ? "selectedEdit"
									: ""%>"
										href="environment.do?action=env.sv.edit&ldep=<%=strLdep%>&identifier=<%=sv.getIdentifier()%>&id-env=<%=env.getIdentifier()%>"><%=sv.getTitle()%></a>
										
												 <% imeO = (ImeObject) sv;  %>
												<%@include file="../../../admin/iconsValidateImsLd.jsp"%>	
										</li>
									<%
										}
									%>
								</ul>
								
								
								
								
								
								
						 
						 
						 
						 
						 
						 
						 
						 
						 
						 
						 
						 
						 
					</li>
				<%
					}
				%>
			</ul>