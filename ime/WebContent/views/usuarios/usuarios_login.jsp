
<%@page import="br.edu.ifg.ime.controllers.Lc"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

         <%			          
     		String msg = (String)request.getAttribute("msg");
     		String url = (String)request.getAttribute("url");         

     		request.removeAttribute("msg");
     		request.removeAttribute("url");
          %> 
        <form action="<%=Urls.urlAppBase %>usuario.do" method="post">
            	<fieldset>
	            	<legend><%=Lc.getTexto(session, "at.acesso.titulo") %></legend>
	            	<input type="hidden" name="url" value="<%=url==null?"":url %>" />   
	            	
	            	<% if (msg != null  && msg.length() > 0)  { %>         		
	                	<div class="message_warning_static"><%=msg%></div>
	            	<% } %>
	            	
	            	<fieldset>
	            	<legend><%=Lc.getTexto(session, "at.acesso.usuario") %></legend>	          
	            		<label><input type="text" name="login" value="" size="15"/></label>
	            	</fieldset>
	            	<fieldset>
	            	<legend><%=Lc.getTexto(session, "at.acesso.senha") %></legend>
					<label><input type="password" name="senha" value="" size="15"/></label>
		    	   	</fieldset>      		
		    	   		<label><input type="submit" name="action" value="<%=Lc.getTexto(session, "at.acesso.btn.acesso") %>"></label>&nbsp;&nbsp;&nbsp;		    	   		
		    	   		<label><input type="submit" name="action" value="<%=Lc.getTexto(session, "at.acesso.btn.trocar.senha") %>"></label>
	            	
           		</fieldset>        
        	</form>
           