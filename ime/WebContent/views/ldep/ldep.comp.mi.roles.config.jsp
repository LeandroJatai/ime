<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="br.edu.ifg.ime.controllers.RolesController"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@page import="org.imsglobal.jaxb.ld.Staff"%>
<%@page import="org.imsglobal.jaxb.ld.Learner"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%=Suport.getMessageWarning(request)%>
    
    <%
        	Usuario userConnect = UsuarioController.getUsuarioConectado(session);
                                    
                                	
                                	ImeWorkspace w = ImeWorkspace
                                	.getImeWorkspace(request);
                                	
                                	String strLdep = Suport.r(request, "ldep");
                                    String strLdepAgr = Suport.r(request, "ldepAgr");


                                    LdProject ldep = w.getLdProjectByIdentifier(strLdep);
                                    LdProject ldepAgr = w.getLdProjectByIdentifier(strLdepAgr);
                                    
                                	List<ArrayList<Learner>> lLearners = RolesController.getAllLearners(ldepAgr);
                                	List<ArrayList<Staff>> lStaffs = RolesController.getAllStaffs(ldepAgr);
        %>
    
    <form action="<%=Urls.urlAppBase%>app.do" method="post">
    
    <fieldset>
	    <legend>Integração de Papeis de <strong title="<%= ldepAgr.getIdentifier() %>">[<%=ldepAgr.getLd().getTitle() %>]</strong> para <strong  title="<%= ldep.getIdentifier() %>">[<%=ldep.getLd().getTitle() %>]</strong></legend>
	    
	    
	    <fieldset style="float: right; width: 60%;">
	    	<legend>Integrar a:</legend>
	    	
		    <fieldset>
	    	    <legend>Aprendiz</legend>
				    <%  
					
				    boolean flagFirst = true;
					
					for (ArrayList<Learner> gLearners : lLearners) {

						if (flagFirst) {
							flagFirst = false;
							continue;
						}
						
						for (Learner l : gLearners) { %>
				    	<label class="list"><input type="radio" name="role-agregante" value="<%=l.getIdentifier()%>" /><%=l.getTitle()%></label>								
					<%}
						}
					%>
		    </fieldset>		   
		    <fieldset>
	    	    <legend>Equipe de Apoio</legend>
				    <% 
				    flagFirst = true;
				   	for (ArrayList<Staff> gStaffs : lStaffs) {

						if (flagFirst) {
							flagFirst = false;
							continue;
						}
				   		for (Staff s : gStaffs) { %>
				    	<label class="list"><input type="radio" name="role-agregante" value="<%=s.getIdentifier()%>" /><%=s.getTitle()%></label>								
					<%}
				   		
					}%>
		    </fieldset>
    
	    </fieldset>
    
    
	    
	    <fieldset>
	    	<legend>Papeis a integrar</legend>
	    	
	    	<label class="list"><input type="checkbox" name="incluir-itens" value="incluir-itens" />Incluir os itens dos papeis na integração</label>
	    	
		    <fieldset>
	    	    <legend>Aprendiz</legend>
				    <% for (Learner l : ldepAgr.getLd().getComponents().getRoles().getLearnerList()) { %>
				    	<label class="list"><input type="checkbox" name="role-agregada" value="<%=l.getIdentifier()%>" /><%=l.getTitle()%></label>								
					<%}%>
		    </fieldset>		   
		    <fieldset>
	    	    <legend>Equipe de Apoio</legend>
				    <% for (Staff s : ldepAgr.getLd().getComponents().getRoles().getStaffList()) { %>
				    	<label class="list"><input type="checkbox" name="role-agregada" value="<%=s.getIdentifier()%>" /><%=s.getTitle()%></label>								
					<%}%>
		    </fieldset>
    
	    </fieldset>
	    
          
		<fieldset>
			<label><input type="submit" value="Integrar"></label>
		</fieldset>
		
		
		<input type="hidden" name="action" value="ldep.comp.mi.roles.config"/>
    	<input type="hidden" name="ldep" value="<%=ldep.getIdentifier()%>" />
		<input type="hidden" name="ldepAgr" value="<%=strLdepAgr%>"/>
   	    
    </fieldset>
    
    </form>
    