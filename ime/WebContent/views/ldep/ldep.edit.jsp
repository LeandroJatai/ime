<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="br.edu.ifg.ime.controllers.ArquivoController"%>
<%@page import="java.util.List"%>
<%@page import="br.edu.ifg.ime.dto.Arquivo"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="org.imsglobal.jaxb.ld.Learner"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="org.imsglobal.jaxb.ld.LearningActivity"%>
<%@page import="br.edu.ifg.ime.controllers.ActivityController"%>
<%@page import="org.imsglobal.jaxb.ld.Activities"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

			<%=Suport.getMessageWarning(request)%>

<%
	Usuario u = UsuarioController.getUsuarioConectado(session);
boolean readonly = !u.hasPermissoes("ldep.save");




	String ldep = Suport.r(request, "ldep");
	String aba = Suport.r(request, "aba");

	if (aba == null)
		aba = "geral";

	LdProject obLdep = null;

	if ((obLdep = ImeWorkspace.getImeWorkspace(request)
	.getLdProjectByIdentifier(ldep)) == null) {

		Urls.forwardAction(request, response, null,
		"app.do?action=ldew.list");
		return;
	}
%>

<fieldset>
	<legend><img src="<%=Urls.url_av_Servlet %>/imgs/ime/uol16.png"/><%=u.getTexto("ime.mi.titulo") %></legend>
									
					     	
<div class="TabControl" aba="<%=aba%>">
    <div id="header">
        <ul class="abas">
            <li style="padding-left: 7px;">
                <div class="aba geral">
                    <span><%=u.getTexto("at.ldep.aba.geral") %></span>
                </div>
            </li>
            
            <% if (u.hasPermissoes("ldep.itemmodel.prerrequisitos")) {%>
            <li>
                <div class="aba prerequisites">
                    <span><%=u.getTexto("at.ldep.aba.pr") %></span>
                </div>
            </li>
            <% } %>
            
            <% if (u.hasPermissoes("ldep.itemmodel.objetivos.aprendizagem")) {%>
            <li>
                <div class="aba learningObjectives">
                    <span><%=u.getTexto("at.ldep.aba.obj.aprnd") %></span>
                </div>
            </li>		
            	<% }
					if (u.hasPermissoes("ldep.aba.conclusao")) { %>
				<li>
					<div class="aba completeUnitOfLearning">
						<span><%=u.getTexto("at.ldep.aba.con.un") %></span>
					</div>
				</li>
				<% }
				if (u.hasPermissoes("ldep.aba.oncompletion")) { %>
				<li>
					<div class="aba method-onCompletion-feedbackDescription">
						<span><%=u.getTexto("at.ldep.aba.ao.conc") %></span>
					</div>
				</li>
            <% } %>
            
        </ul>
    </div>
    <div id="content">
        <div class="conteudo geral">					        		
					 <% if (!readonly) { %>    <form action="<%=Urls.urlAppBase %>app.do" method="post"> <%} %>
						      	<fieldset style="float: right;  margin-left: 0px;">
					           	<legend><%=u.getTexto("at.ldep.aba.geral.sq.leg") %></legend>	          
					           		<label><input type="radio" name="sequenceUsed" value="true" tabindex="50" <%=obLdep.getLd().isSequenceUsed()?"checked":"" %> /><%=u.getTexto("at.ldep.aba.geral.sq.sim") %></label>&nbsp;&nbsp;&nbsp;&nbsp; 
					           		<label><input type="radio" name="sequenceUsed" value="false" tabindex="60" <%=!obLdep.getLd().isSequenceUsed()?"checked":"" %> /><%=u.getTexto("at.ldep.aba.geral.sq.nao") %></label> 
					           	</fieldset>
					           	
								<fieldset style="float: right;">
					           	<legend><%=u.getTexto("at.ldep.aba.geral.nivel.leg") %></legend>  
					           		<label><input type="radio" name="level" value="A" tabindex="20" <%=obLdep.getLd().getLevel().equals("A")?"checked":"" %> /><%=u.getTexto("at.ldep.aba.geral.nivel.lev.a") %></label>&nbsp;&nbsp;&nbsp;&nbsp; 
					           		<label><input type="radio" name="level" value="B" tabindex="30" <%=obLdep.getLd().getLevel().equals("B")?"checked":"" %> /><%=u.getTexto("at.ldep.aba.geral.nivel.lev.b") %></label>&nbsp;&nbsp;&nbsp;&nbsp; 
					           		<label><input type="radio" name="level" value="C" tabindex="40" <%=obLdep.getLd().getLevel().equals("C")?"checked":"" %> /><%=u.getTexto("at.ldep.aba.geral.nivel.lev.c") %></label>
					           </fieldset>	           
					           
								<fieldset>
					           	<legend><%=u.getTexto("at.ldep.aba.geral.titulo.leg") %></legend>
					           		<label style="display:block; padding-right: 10px;" ><input style="width: 100%;" type="text" name="title" tabindex="10" value="<%=obLdep.getLd().getTitle()!=null?StringEscapeUtils.escapeHtml4(obLdep.getLd().getTitle()):"" %>" size="30"/></label>
					           	</fieldset>	
					           	
					           	<%if (!obLdep.isAgregado() && u.hasPermissoes("ldep.skin.alter")) { %>
				
					           	<fieldset class="right">
									<legend><%=u.getTexto("ldep.skin.leg") %></legend>
								<label >
									<select id="ldepSkin" name="ldepSkin">
									<option value=""> </option>
										<% 
										List<Arquivo> lArqs = ArquivoController.getSkins();
										for (Arquivo arq: lArqs)  { 
										    boolean checkArq = false;
											if (arq.getNome().equals(obLdep.skin))
												checkArq = true;
										
										%>
										<option  value="<%=arq.getNome()%>" <%=checkArq?"selected=\"selected\"":"" %>><%=arq.getTitulo() %></option>
									<%} %>
									
									</select>
								  </label>
								</fieldset>
					           		<%} %>
								
					           	
					           	<% if (obLdep.isAgregado()) { %>
								<fieldset style="float: right;">
					           	<legend><%=u.getTexto("at.ldep.aba.geral.hr.leg") %></legend>
					           		<label><input type="checkbox" name="inherit-roles" tabindex="90" value="inherit-roles" <%=obLdep.getLd().getInheritRoles()!=null?"checked":"" %>/><%=u.getTexto("at.ldep.aba.geral.hr") %></label>
					           	</fieldset>	
					           	<% }
					           	%>
								<fieldset style="float: right;">
					           	<legend><%=u.getTexto("at.ldep.aba.geral.versao.leg") %></legend>
					           		<label style="display:block; padding-right: 10px;" ><input style="width: 100%;" type="text" name="version" tabindex="80" value="<%=obLdep.getLd().getVersion()!=null?StringEscapeUtils.escapeHtml4(obLdep.getLd().getVersion()):"" %>" size="30"/></label>
					           	</fieldset>	
					           	
								<fieldset>
					           	<legend><%=u.getTexto("at.ldep.aba.geral.uri.leg") %></legend>
					           		<label style="display:block; padding-right: 10px;" ><input style="width: 100%;" type="text" name="uri" tabindex="70" value="<%=obLdep.getLd().getUri()!=null?StringEscapeUtils.escapeHtml4(obLdep.getLd().getUri()):"" %>" size="30"/></label>
					           	</fieldset>		
								
								<% if (!readonly) { %>
								<fieldset class="controles">
						    	   <input type="hidden" name="ldep" value="<%=ldep%>" />
						    	   <input type="hidden" name="aba" value="geral" />
								   <input type="hidden" name="action" value="ldep.save"/>
								   <label><input type="submit" value="<%=u.getTexto("at.ldep.aba.geral.btn.guardar") %>"></label>
								</fieldset>
								<%} %>
					   <% if (!readonly) { %>  </form> <%} %>
        </div>
       
        <% if (u.hasPermissoes("ldep.itemmodel.prerrequisitos")) { %>
        <div class="conteudo prerequisites">
           <% String link = "/views/itens/itemmodel.edit.jsp?field=prerequisites&aba=prerequisites&abaAtiva="+aba+"&id-ref="+Suport.utfToIso(obLdep.getLd().getIdentifier());  %>
            <jsp:include page="<%=link %>"></jsp:include>
        </div> 
            <% } %>
            
            <% if (u.hasPermissoes("ldep.itemmodel.objetivos.aprendizagem")) {%>
        <div class="conteudo learningObjectives">
        	<%  String link = "/views/itens/itemmodel.edit.jsp?field=learningObjectives&aba=learningObjectives&abaAtiva="+aba+"&id-ref="+Suport.utfToIso(obLdep.getLd().getIdentifier());  %>
            <jsp:include page="<%=link %>"></jsp:include>
        </div>
        
        
        
            <% }
				if (u.hasPermissoes("ldep.aba.conclusao")) { %>
			<div class="conteudo completeUnitOfLearning">
				<%
				String link = "/views/complete/complete.ldep.edit.jsp?field=completeUnitOfLearning&abaAtiva="+aba+"&aba=completeUnitOfLearning&id-ref="
								+ Suport.utfToIso(obLdep.getLd().getIdentifier());
				%>
				<jsp:include page="<%=link%>"></jsp:include>
			</div>
			
			
				<% }
				if (u.hasPermissoes("ldep.aba.oncompletion")) { %>
			<div class="conteudo method-onCompletion-feedbackDescription">
				<%
				String link = "/views/itens/itemmodel.edit.jsp?field=method-onCompletion-feedbackDescription&abaAtiva="+aba+"&aba=method-onCompletion-feedbackDescription&id-ref="
								+ Suport.utfToIso(obLdep.getLd().getIdentifier());
				%>
				<jsp:include page="<%=link%>"></jsp:include>
			</div>
            <% } %>
    </div>
</div>
							</fieldset>

							<fieldset>


		   		    <ul class="arvoreRaiz">
		   		    	    <% if (u.hasPermissoes("ldep.new.copy")) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.new.copy&ldep=<%=obLdep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.copia.titulo") %>"><b><%=u.getTexto("at.ldep.copia") %></b> - <%=u.getTexto("at.ldep.copia.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.remove") && !obLdep.isAgregado()) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.remove&ldep=<%=obLdep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.remover.titulo") %>"><b><%=u.getTexto("at.ldep.remover") %></b> - <%=u.getTexto("at.ldep.remover.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.undo") && !obLdep.isAgregado()) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.undo&ldep=<%=obLdep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.undo.titulo") %> - <%=obLdep.sizeUndo()%>"><b><%=u.getTexto("at.ldep.undo") %></b> - <%=u.getTexto("at.ldep.undo.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.redo") && !obLdep.isAgregado()) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.redo&ldep=<%=obLdep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.redo.titulo") %> - <%=obLdep.sizeRedo()%>"><b><%=u.getTexto("at.ldep.redo") %></b> - <%=u.getTexto("at.ldep.redo.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.database.update") && !obLdep.isAgregado()) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.database.update&ldep=<%=obLdep.getIdentifier()%>" title="<%=u.getTexto("at.db.ldep.persistir.titulo") %>"><b><%=u.getTexto("at.db.ldep.persistir") %></b> - <%=u.getTexto("at.db.ldep.persistir.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.edit.code.imscp") && !obLdep.isAgregado()) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.edit.code.imscp&ldep=<%=obLdep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.codigo.edit.titulo") %>"><b><%=u.getTexto("at.ldep.codigo.edit") %></b> - <%=u.getTexto("at.ldep.codigo.edit.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.xml.backup.project")) { %><li class="list"><a class="controls-objs" target="_blanck" href="app.do?action=ldep.xml.backup.project&ldep=<%=obLdep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.copia.xml.titulo") %>"><b><%=u.getTexto("at.ldep.copia.xml") %></b> - <%=u.getTexto("at.ldep.copia.xml.msg") %></a></li><%} %>
		   		    <% if (u.hasPermissoes("ldep.zip.file.lmi") && !obLdep.isAgregado()) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.zip.file.lmi&ldep=<%=obLdep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.zip.lmi.titulo") %>"><b><%=u.getTexto("at.ldep.zip.lmi") %></b> - <%=u.getTexto("at.ldep.zip.lmi.msg") %></a></li><%} %>

		   		    <%
		   		 ImeObject imeO = (ImeObject) obLdep; 
		   		    
		   		    if (u.hasPermissoes("ldep.zip.file.imscp") && !obLdep.isAgregado() ) { %><li class="list"><a class="controls-objs" href="app.do?action=ldep.zip.file.imscp&ldep=<%=obLdep.getIdentifier()%>" title="<%=u.getTexto("at.ldep.zip.imscp.titulo") %>"><b><%=u.getTexto("at.ldep.zip.imscp") %></b> - <%=u.getTexto("at.ldep.zip.imscp.msg") %>
		   	
						<%@include file="../../../admin/iconsValidateImsLd.jsp"%></a></li><%} %>
		   		    
		 		</ul>
							</fieldset>
							
              <%  %>
              <%@include file="../../../admin/msgValidateImsLd.jsp"%>
              			