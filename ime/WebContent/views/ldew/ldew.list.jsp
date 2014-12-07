<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="org.imsglobal.jaxb.ld.Environment"%>
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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
		
			<%=Suport.getMessageWarning(request)%>
			<%
				Usuario u = UsuarioController.getUsuarioConectado(session);
			%>
    <fieldset>
    <legend><img src="<%=Urls.url_av_Servlet%>/imgs/ime/uol16.png"/><%=u.getTexto("ime.mi.titulo")%></legend>
    <fieldset>	
		<%
				if (u.hasPermissoes("ldep.new")) {
			%><a class="menu" href="app.do?action=ldep.new" title="<%=u.getTexto("at.mi.novo.titulo")%>"><%=u.getTexto("at.mi.novo")%></a> <%
 	}
 %>  
		<%
  			if (u.hasPermissoes("ldep.reset")) {
  		%><a class="menu" href="app.do?action=ldep.reset" title="<%=u.getTexto("at.mi.limpar.titulo")%>"><%=u.getTexto("at.mi.limpar")%></a> <%
 	}
 %>	 	
		<%
	 				if (u.hasPermissoes("ldep.xml.backup.workspace")) {
	 			%><a class="menu"  style="float:right;"  target="_blanck" href="app.do?action=ldep.xml.backup.workspace" title="<%=u.getTexto("at.copia.titulo")%>"><%=u.getTexto("at.copia")%></a>  	<%
  		}
  	%>
    </fieldset>
    
<ul class="arvoreRaiz">
		<%
			ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
				    			for (LdProject ldep : w.getMasterListLdProject()) { 
				    		    		        LearningDesign ld = ldep.getLd();
		%>    	
   		    <li> <a class="arvoreMais" href="app.do?action=ldep.ftv"> </a>
   		         <a  class="<%=(ldep == w.getLdProject(request))?"selected":""%>" title="<%=u.getTexto("ime.mi.editar.tit") %>" href="app.do?action=ldep.edit&ldep=<%=ldep.getIdentifier() %>" ><%=ld.getTitle() %><b style="font-size: 120%;" title="<%=u.getTexto("at.ldep.nao.salvo") %>"><%=ldep.flagAlterado?"* ":""%></b></a>
	   		    <ul class="ulFechado">
	                <% if (u.hasPermissoes("ldep.new.copy")) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.new.copy&ldep=<%=ldep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.copia.titulo") %>"><b><%=u.getTexto("at.ldep.copia") %></b> - <%=u.getTexto("at.ldep.copia.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.remove")) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.remove&ldep=<%=ldep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.remover.titulo") %>"><b><%=u.getTexto("at.ldep.remover") %></b> - <%=u.getTexto("at.ldep.remover.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.undo")) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.undo&ldep=<%=ldep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.undo.titulo") %> - <%=ldep.sizeUndo()%>"><b><%=u.getTexto("at.ldep.undo") %></b> - <%=u.getTexto("at.ldep.undo.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.redo")) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.redo&ldep=<%=ldep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.redo.titulo") %> - <%=ldep.sizeRedo()%>"><b><%=u.getTexto("at.ldep.redo") %></b> - <%=u.getTexto("at.ldep.redo.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.database.update")) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.database.update&ldep=<%=ldep.getIdentifier()%>" title="<%=u.getTexto("at.db.ldep.persistir.titulo") %>"><b><%=u.getTexto("at.db.ldep.persistir") %></b> - <%=u.getTexto("at.db.ldep.persistir.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.edit.code.imscp")) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.edit.code.imscp&ldep=<%=ldep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.codigo.edit.titulo") %>"><b><%=u.getTexto("at.ldep.codigo.edit") %></b> - <%=u.getTexto("at.ldep.codigo.edit.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.xml.backup.project")) { %><li class="list"><a class="controls-objs" target="_blanck" href="app.do?action=ldep.xml.backup.project&ldep=<%=ldep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.copia.xml.titulo") %>"><b><%=u.getTexto("at.ldep.copia.xml") %></b> - <%=u.getTexto("at.ldep.copia.xml.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.zip.file.lmi")) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.zip.file.lmi&ldep=<%=ldep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.zip.lmi.titulo") %>"><b><%=u.getTexto("at.ldep.zip.lmi") %></b> - <%=u.getTexto("at.ldep.zip.lmi.msg") %></a></li><%} %>
		
		
		   		    <%
		   		 ImeObject imeO = (ImeObject) ldep; 
		   		    
		   		    if (u.hasPermissoes("ldep.zip.file.imscp") && !ldep.isAgregado() ) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.zip.file.imscp&ldep=<%=ldep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.zip.imscp.titulo") %>"><b><%=u.getTexto("at.ldep.zip.imscp") %></b> - <%=u.getTexto("at.ldep.zip.imscp.msg") %>
		   	
						<%@include file="../../../admin/iconsValidateImsLd.jsp"%></a></li><%} %>
		
		   		</ul>
    	<% 
    	} 
    	%>		
</ul>
    </fieldset>
    
    <fieldset style="color: #777;"><%=u.getTexto("at.bemvindo") %>
    <% if (u.getId() == Usuario.wmx_convidado) { %><br><br>
   <%=u.getTexto("at.bemvindo.convidado") %>
   
   <br><br>
   <iframe width="560" height="315" src="//www.youtube.com/embed/O1w0e1trCcQ" frameborder="0" allowfullscreen></iframe>
   
   
    <% } %>
    
    </fieldset>
  		
    <% if (u.hasPermissoes("ldep.import.imscp"))  {%>
    <br><br><br><br><br>
    
<form style="position: fixed; bottom: 20px; width: 100%'" action="<%=Urls.urlAppBase %>app.do" method="post" enctype="multipart/form-data" name="importXml" id="importXml">
    <fieldset>
    	<label><%=u.getTexto("at.importar") %><br>
				<input type="submit" value="<%=u.getTexto("at.importar.btn") %>">	
				<input type="hidden" value="ldep.import.imscp" name="action" >	
		    	<input type="file" name="fileData" size="50" accept=".zip,text/xml"/><br>		
		</label>  	
	</fieldset>
</form>
<%} %>