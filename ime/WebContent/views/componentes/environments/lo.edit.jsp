<%@page import="br.edu.ifg.ime.ld.ImeObject"%>
<%@page import="org.imsglobal.jaxb.ld.Environment"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="java.util.List"%>
<%@page import="org.imsglobal.jaxb.ld.LearningObject"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="org.imsglobal.jaxb.ld.Item"%>
<%@page import="br.edu.ifg.ime.controllers.ItemModelController"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="org.imsglobal.jaxb.ld.ItemModel"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>

<%
	Usuario u = UsuarioController.getUsuarioConectado(session);
    boolean readonlyItens = !u.hasPermissoes("im.item.save");

    String title = "";
     
    String ldep = Suport.r(request, "ldep");
    String idRef = Suport.r(request, "id-ref");
    String idEnv = Suport.r(request, "id-env");
    String type = Suport.r(request, "type");
    String identifier = Suport.r(request, "identifier");

	if (idRef == null && identifier != null) 
		idRef = identifier;

    Environment env = EnvironmentsController.getEnviromentByIdentifier(request, idEnv);

    List<LearningObject> lLos = EnvironmentsController.getLearningObjects(env);
    
    Item itemSelected = null;
    
    String action = Suport.r(request, "action");
    
    LearningObject loSelected = null;
%>

 <%
 	if (!readonlyItens) {
 %><form action="<%=Urls.urlAppBase%>im.do" method="post" enctype="multipart/form-data"> <%
 	}
 %>
      <fieldset>
   <legend>	<img src="<%=Urls.url_av_Servlet %>/imgs/ime/lo16.png" /><%=u.getTexto("at.cp.env.lo.cad.leg")%> <b><%=env.getTitle() %></b></legend>
 		   <%
 		   	for (LearningObject lo : lLos)  {
 		       	 
 		       	 if (lo.getIdentifier().equals(idRef))
 		       		 loSelected = lo;
 		
			}
									
     	    	if (loSelected != null)
     	                  title = loSelected.getTitle() != null?loSelected.getTitle():"";
     	    %>
		   <fieldset class="right">
              <legend><%=u.getTexto("at.cp.env.lo.tipo.ob")%> </legend>
    		    <label><input type="radio" name="lo.tipo" value="" tabindex="20" <%=loSelected == null || loSelected != null && loSelected.getType() == null ? "checked":""%> /><%=u.getTexto("at.cp.env.lo.tipo.ob.indef")%></label>&nbsp;&nbsp;&nbsp;&nbsp; 
    	    	 <br>    <label><input type="radio" name="lo.tipo" value="knowledge-object" tabindex="20" <%=loSelected != null && loSelected.getType() != null && loSelected.getType().equals("knowledge-object") ? "checked":""%> /><%=u.getTexto("at.cp.env.lo.tipo.ob.conh")%></label>&nbsp;&nbsp;&nbsp;&nbsp; 
                 <br> 	 <label><input type="radio" name="lo.tipo" value="tool-object" tabindex="30" <%=loSelected != null && loSelected.getType() != null && loSelected.getType().equals("tool-object") ? "checked":""%> /><%=u.getTexto("at.cp.env.lo.tipo.ob.ferr")%></label>&nbsp;&nbsp;&nbsp;&nbsp; 
                 <br> 	 <label><input type="radio" name="lo.tipo" value="test-object" tabindex="40" <%=loSelected != null && loSelected.getType() != null && loSelected.getType().equals("test-object") ? "checked":""%> /><%=u.getTexto("at.cp.env.lo.tipo.ob.test")%></label>
         </fieldset> 
		<img  style="float: left; padding: 7px 7px 0px 0px;" src="<%=Urls.url_av_Servlet %>/imgs/ime/lo.png" />
    <fieldset>	
   	       <legend><%=u.getTexto("at.itemmodel.titulo.leg")%></legend>
   	      
   	     <label style="float: right; margin-right: 10px;">
					   		    <input style="float: left;" name="lo.isvisible" type="checkbox" <%=loSelected != null && loSelected.isIsvisible()?"checked":""%>>__Visível
					   	    </label>
				   	       <label style="display: block;  margin-right: 192px;">
   	      <input type="text" name="title.model" tabindex="5" value="<%=title%>" style="width:100%;"/></label>
   	      
	</fieldset> 
	  
	  <fieldset class="right">
        <legend ><%=u.getTexto("at.cp.env.lo.classe.leg")%></legend>
   	     <label style="display: block;  margin-right: 10px;"><input type="text" name="lo.clazz" tabindex="10" value="<%=loSelected != null && loSelected.getClazzForEditionView().length() > 0  ? StringEscapeUtils.escapeHtml4(loSelected.getClazzForEditionView()):""%>" style="width:100%;"/></label>
	</fieldset>
		
	  <fieldset>
        <legend><%=u.getTexto("at.lo.parameters.leg")%></legend>
   	     <label style="display: block;  margin-right: 10px;"><input type="text" name="lo.parameters" tabindex="10" value="<%=loSelected != null && loSelected.getParameters() != null ? StringEscapeUtils.escapeHtml4(loSelected.getParameters()):""%>" style="width:100%;"/></label>
	</fieldset> 
	
	   	       <% if (!readonlyItens) {  %>
		   <label ><input type="submit" value="<%=u.getTexto("at.itemmodel.btn.guardar")%>"></label>
		   		<% if (u.hasPermissoes("env.lo.edit.new")) { %>
                 <a href="environment.do?action=env.lo.edit.new&id-env=<%=env.getIdentifier()%>&ldep=<%=ldep%>"><%=u.getTexto("at.cp.env.lo.btn.novo")%></a></a>
   	             <% 	}   %>  
   	       <% 	}   %>
   	       
	
 <br style="clear:right;">
	 
	<% 		if (loSelected != null) { 	%>
	    <fieldset style="float: left; width: 280px;">   	       
   	       		<legend><%=u.getTexto("at.itemmodel.itens.leg")%></legend>
		    <%   	if (!readonlyItens) {     %>
	   	       		<label>
			   		    <a class="controls-objs left" href="<%=Urls.urlAppBase%>im.do?action=im.item.edit&type=text&ldep=<%=ldep%>&id-ref=<%=idRef%>&id-env=<%=idEnv%>" title="<%=u.getTexto("at.itemmodel.btn.texto.titulo")%>"><%=u.getTexto("at.itemmodel.btn.texto.add")%></a>
			   		    <a class="controls-objs left" href="<%=Urls.urlAppBase%>im.do?action=im.item.edit&type=link&ldep=<%=ldep%>&id-ref=<%=idRef%>&id-env=<%=idEnv%>" title="<%=u.getTexto("at.itemmodel.btn.link.titulo")%>"><%=u.getTexto("at.itemmodel.btn.link.add")%></a>
			   		    <a class="controls-objs left " href="<%=Urls.urlAppBase%>im.do?action=im.item.edit&type=file&ldep=<%=ldep%>&id-ref=<%=idRef%>&id-env=<%=idEnv%>" title="<%=u.getTexto("at.itemmodel.btn.file.titulo")%>"><%=u.getTexto("at.itemmodel.btn.file.add")%></a>
			   	    </label>
		   	    <% } 
		    
	    		   	    	for (Item item: loSelected.getItemList()) {
	    		   	    		   	    	
	    		   	    		   	    	if (item.getIdentifier().equals(identifier))
	    		   	    		   	    		itemSelected = item; 
	    		   	    		   	    	
	    		   	    		   	    	title = "";    
	    		   	    		    if (item != null)
	    		   	    		    	title = item.getTitle() != null?item.getTitle():"";
 	    		   	    %>
				    	
	   	       		<label class="list mini">
	   	       		
	   	       		<%
	   	       			   	       			if (u.hasPermissoes("im.item.remove")) {
	   	       			   	       		%>
			   		    <a class="controls-objs right" href="im.do?action=im.item.remove&ldep=<%=ldep%>&id-ref=<%=idRef%>&id-env=<%=idEnv%>&identifier=<%=item.getIdentifier()%>" title="<%=u.getTexto("at.itemmodel.item.excluir.titulo")%>"><%=u.getTexto("at.itemmodel.item.excluir")%></a>
			   		<%
			   			}
			   		%>
			   		    <a class="<%=item.getIdentifier().equals(identifier)?"selectedItem":""%>" href="im.do?action=im.item.edit<%=item.getType()==null?"":"&type="+item.getType()%>&ldep=<%=ldep%>&id-ref=<%=idRef%>&id-env=<%=idEnv%>&identifier=<%=item.getIdentifier()%>"><%=StringEscapeUtils.escapeHtml4(item.getTitle())%></a>
			   	    </label>
			    
		   	    <%
			    		   	    	}
			    		   	    		   	    if (identifier != null && itemSelected == null && loSelected.getItemList().size() > 0) {
			    		   	    		   	    	//itemSelected = im.getItemList().get(0);
			    		   	    		   	    	//type = itemSelected.getType();
			    		   	    		   	    	type = null;
			    		   	    		   	    }
			    		   	    %>
		   	    
   	       </fieldset>
   	       
			<%
   	       				if (type != null) {
   	       				
   	       					if (itemSelected == null) {
   	       						title = "";
   	       						identifier = "";					
   	       					}
   	       					else {
   	       						title = itemSelected.getTitle();
   	       						identifier = itemSelected.getIdentifier();
   	       					}
   	       			%>
   	       		 <fieldset>	
			   	       <legend><%=u.getTexto("at.itemmodel.item.leg")%></legend>
					    <fieldset>	
				   	       <legend><%=u.getTexto("at.itemmodel.titulo.leg")%></legend>
				   	       
						   <label style="float: right; margin-right: 10px;">
					   		    <input style="float: left;" name="isvisible" type="checkbox" <%=itemSelected != null && itemSelected.isIsvisible()?"checked":""%>><%=u.getTexto("at.itemmodel.item.visivel")%>
					   	    </label>
				   	       <label style="display: block;  margin-right: 102px;"><input type="text" name="title" tabindex="10" value="<%=StringEscapeUtils.escapeHtml4(title)%>" style="width:100%;"/></label>
			   	       
			   	       </fieldset>	
			   	       
			   	       <%
				   	       			   	       	if (UsuarioController.checkPermissao(request, "im.item.view.parametros")) {
				   	       			   	       %>
			   	       <fieldset>	
					        <legend><%=u.getTexto("at.itemmodel.item.par.leg")%></legend>
					        <label style="display: block;  margin-right: 10px;"><input type="text" name="parameters" tabindex="10" value="<%=itemSelected != null && itemSelected.getParameters() != null?StringEscapeUtils.escapeHtml4(itemSelected.getParameters()):""%>" style="width:100%;"/></label>
				   	   </fieldset>	
				   	   <%
					   	   	}
					   	   %>
			   	       
			   	       <%
			   	       			   	       	if (type.equals("link")) {
			   	       			   	       %>
			   	    	    <fieldset>	
						   	       <legend><%=u.getTexto("at.itemmodel.item.link.leg")%></legend>
						   	       <label style="display: block; margin-right: 10px;"><input type="text" name="dadosItem" tabindex="10" value="<%=itemSelected != null && itemSelected.getLink() != null?StringEscapeUtils.escapeHtml4(itemSelected.getLink()):""%>" style="width:100%;"/></label>
			   	            </fieldset>	
			   	    	   
			   	       <%
				   	    	   			   	       	}
				   	    	   			   	       %>
			   	       
			   	       <%
			   	       			   	       	if (type.equals("text")) {
			   	       			   	       	   	       			   	       	   	       
			   	       			   	       	   	       			   	       	   	       		String idDadosItem = ImeWorkspace.getImeWorkspace(request).newIdentifier(null, "ckEditorTextoItem");
			   	       			   	       %>
			   	    	    <fieldset>	
			   	       			<legend><%=u.getTexto("at.itemmodel.item.texto.leg") %></legend>
			   	    			<label style="display: block; margin-right: 2px;">
			   	    			     <textarea id="<%=idDadosItem %>" name="dadosItem" tabindex="10"  style="width:100%;" rows="6"><%=itemSelected != null && itemSelected.getText() != null?StringEscapeUtils.escapeHtml4(itemSelected.getText()):" "%></textarea></label>
							   	        <script>
				
											// Replace the <textarea id="editor"> with an CKEditor
											// instance, using default configurations.
											CKEDITOR.replace( '<%=idDadosItem%>', {
												uiColor: '#eeddaa',
												toolbar: [
													[ 'Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink' ],
													[ 'FontSize', 'TextColor', 'BGColor' ]
												]
											});
										</script>
			   	            </fieldset>	
			   	    	   
			   	       <% } %>
			   	       
			   	       
			   	       <% if (type.equals("file") ) {
			   	    	  %>
			   	    	    <fieldset>	
			   	       			<legend><%=u.getTexto("at.itemmodel.item.file.leg") %></legend>
			   	       			
			   	       			<% 
					   	    	if (itemSelected != null && itemSelected.getFile() != null) {
							   	%>
							   	   <%=u.getTexto("at.itemmodel.item.file.atual") %>: <a class="<%=itemSelected.getIdentifier().equals(identifier)?"selectedItem":"" %>" href="im.do?action=im.item.edit.download.file<%=itemSelected.getType()==null?"":"&type="+itemSelected.getType() %>&identifier=<%=itemSelected.getIdentifier()%>" target="_blanck"><%= StringEscapeUtils.escapeHtml4(itemSelected.getNameFile()) %></a>
			   			   
							   		   <% } %>
							   		   
			   	       			
   	       <% if (!readonlyItens) { %>
			   	    			<label><input type="file" name="dadosItem" size="50"/><br>	</label>
			   	    			<%} %>
			   	            </fieldset>	
			   	    	   
			   	       <% } %>

				</fieldset> 
				
		    	  <% if (!readonlyItens) { %>
   	     				<input type="hidden" name="type" value="<%=type%>" />
			    	   <input type="hidden" name="identifier" value="<%=StringEscapeUtils.escapeHtml4(identifier)%>" />
		       	  <% } %>	
			   	     
			<%} %>  		
			
			
   	<% } %>			
				   	       
   	       <% if (!readonlyItens) { %>
	   	           	   <input type="hidden" name="ldep" value="<%=StringEscapeUtils.escapeHtml4(ldep)%>" />
			    	   
			    	   <% if (idRef != null) { %>
			    	      <input type="hidden" name="id-ref" value="<%=StringEscapeUtils.escapeHtml4(idRef)%>" />
			    	<%}
			    	   
			    	   if (idEnv != null) { %>			    	
			    	      <input type="hidden" name="id-env" value="<%=StringEscapeUtils.escapeHtml4(idEnv)%>" />
			    	  <% } %>
			    	  
			
			    	   <input type="hidden" name="action" value="im.item.save" />
			    	   <% } %>		
			
			<br style="clear:both;">
			   	 <br>
			   	 
			   	  

   	</fieldset>
          
 <% if (!readonlyItens) { %></form>   	<%} %>	

   	
   	
<% ImeObject imeO = (ImeObject) loSelected;  %>
<%@include file="../../../admin/msgValidateImsLd.jsp"%>