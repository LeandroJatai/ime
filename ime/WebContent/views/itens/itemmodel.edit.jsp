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
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	ItemModel im = ItemModelController.getItemModel(request);

Usuario u = UsuarioController.getUsuarioConectado(session);
boolean readonlyItens = !u.hasPermissoes("im.item.save");

    String title = "";
    
    if (im != null)
    	title = im.getTitle() != null?im.getTitle():"";

    String aba = Suport.r(request, "aba");	
    String abaAtiva = Suport.r(request, "abaAtiva"); 
    String ldep = Suport.r(request, "ldep");
    String idRef = Suport.r(request, "id-ref");
    String field = Suport.r(request, "field");    
    String type = Suport.r(request, "type");   
    String identifier = Suport.r(request, "identifier");   
    
    Item itemSelected = null;
    
    String action = Suport.r(request, "action");
%>

 <%
 	if (!readonlyItens) {
 %><form action="<%=Urls.urlAppBase%>im.do" method="post" enctype="multipart/form-data"> <%
 	}
 %>
    <fieldset>	
   	       <legend><%=u.getTexto("at.itemmodel.titulo.leg")%></legend>
   	       
   	       <%
   	          	       	if (!readonlyItens) {
   	          	       %>
		   <label style="float: right;"><input type="submit" value="<%=u.getTexto("at.itemmodel.btn.guardar")%>"></label>
   	     <%
   	     	}
   	     %>
   	      <label style="display: block;  margin-right: 90px;"><input type="text" name="title.model" tabindex="10" value="<%=title%>" style="width:100%;"/></label>
	</fieldset> 
	
	<%
 			if (im != null) {
 		%>
	
   	       <fieldset style="float: left; width: 280px;">   	       
   	       		<legend><%=u.getTexto("at.itemmodel.itens.leg")%></legend>
		   		   
		   		   
		   		   
   	       <%
		   		   		   		   		   		      	       	if (!readonlyItens) {
		   		   		   		   		   		      	       %>
	   	       		<label>
			   		    <a class="controls-objs left" href="<%=Urls.urlAppBase%>im.do?action=im.item.edit&type=text&field=<%=field%>&aba=<%=field%>&ldep=<%=ldep%>&id-ref=<%=idRef%>" title="<%=u.getTexto("at.itemmodel.btn.texto.titulo")%>"><%=u.getTexto("at.itemmodel.btn.texto.add")%></a>
			   		    <a class="controls-objs left" href="<%=Urls.urlAppBase%>im.do?action=im.item.edit&type=link&field=<%=field%>&aba=<%=field%>&ldep=<%=ldep%>&id-ref=<%=idRef%>" title="<%=u.getTexto("at.itemmodel.btn.link.titulo")%>"><%=u.getTexto("at.itemmodel.btn.link.add")%></a>
			   		    <a class="controls-objs left " href="<%=Urls.urlAppBase%>im.do?action=im.item.edit&type=file&field=<%=field%>&aba=<%=field%>&ldep=<%=ldep%>&id-ref=<%=idRef%>" title="<%=u.getTexto("at.itemmodel.btn.file.titulo")%>"><%=u.getTexto("at.itemmodel.btn.file.add")%></a>
			   	    
	   	       		</label>
		   	    <%
		   	    	}
		   	    %>
		   	    
		   	    <%
		   	    		   	    	for (Item item: im.getItemList()) {
		   	    		   	    		   	    	
		   	    		   	    		   	    	if (item.getIdentifier().equals(identifier)) {
		   	    		   	    		   	    		itemSelected = item;
		   	    		   	    		   	    	}
		   	    		   	    		   	    	
		   	    		   	    		   	    	
		   	    		   	    		   	    	title = "";    
		   	    		   	    		    if (item != null)
		   	    		   	    		    	title = item.getTitle() != null?item.getTitle():"";
		   	    		   	    %>
				    	
	   	       		<label class="list mini">
	   	       		
	   	       		<%
	   	       			   	       			if (u.hasPermissoes("im.item.remove")) {
	   	       			   	       		%>
			   		    <a class="controls-objs right" href="im.do?action=im.item.remove&field=<%=field%>&aba=<%=field%>&ldep=<%=ldep%>&id-ref=<%=idRef%>&identifier=<%=item.getIdentifier()%>"title="<%=u.getTexto("at.itemmodel.item.excluir.titulo")%>"><%=u.getTexto("at.itemmodel.item.excluir")%></a>
			   		<%
			   			}
			   		%>
			   		    <a class="<%=item.getIdentifier().equals(identifier)?"selectedItem":""%>" href="im.do?action=im.item.edit<%=item.getType()==null?"":"&type="+item.getType()%>&field=<%=field%>&aba=<%=field%>&ldep=<%=ldep%>&id-ref=<%=idRef%>&identifier=<%=item.getIdentifier()%>"><%=StringEscapeUtils.escapeHtml4(item.getTitle())%></a>
			   	    </label>
			    
		   	    <%
   		   	    	}
   		   	    		   	    if (identifier != null && itemSelected == null && im.getItemList().size() > 0) {
   		   	    		   	    	//itemSelected = im.getItemList().get(0);
   		   	    		   	    	//type = itemSelected.getType();
   		   	    		   	    	type = null;
   		   	    		   	    }
			    		   	    %>
		   	    
   	       </fieldset>
   	       
			<%
   	       				if (type != null && aba.equals(abaAtiva)) {
   	       				
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
							   	   <%=u.getTexto("at.itemmodel.item.file.atual") %>: <a class="<%=itemSelected.getIdentifier().equals(identifier)?"selectedItem":"" %>" href="im.do?action=im.item.edit.download.file<%=itemSelected.getType()==null?"":"&type="+itemSelected.getType() %>&field=<%=field %>&aba=<%=field %>&ldep=<%=ldep %>&id-ref=<%=idRef %>&identifier=<%=itemSelected.getIdentifier()%>" target="_blanck"><%= StringEscapeUtils.escapeHtml4(itemSelected.getNameFile()) %></a>
			   			   
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
			    	   <input type="hidden" name="id-ref" value="<%=StringEscapeUtils.escapeHtml4(idRef)%>" />
			    	   <input type="hidden" name="field" value="<%=field%>" />
			    	   <input type="hidden" name="aba" value="<%=field%>"/>
			    	  
			
			    	   <input type="hidden" name="action" value="im.item.save" />
			    	   <% } %>		
			
			<br style="clear:both;">
			   	     
 <% if (!readonlyItens) { %></form>   	<%} %>	

   	
   	
   	