<%@page import="org.imsglobal.jaxb.ld.CompleteAct"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="org.imsglobal.jaxb.ld.CompleteActivity"%>
<%@page import="org.imsglobal.jaxb.ld.LearningActivity"%>
<%@page import="br.edu.ifg.ime.controllers.ActivityController"%>
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
	// ItemModel im = ItemModelController.getItemModel(request);

Usuario u = UsuarioController.getUsuarioConectado(session);
boolean readonly = !u.hasPermissoes("complete.act.save");

    String aba = Suport.r(request, "aba");	
    String ldep = Suport.r(request, "ldep");
    String idRef = Suport.r(request, "id-ref");
    String field = Suport.r(request, "field");    
    

	Object ob = ImeWorkspace.getImeWorkspace(request).getObject(idRef);

	if (ob == null) {

		Urls.forwardAction(request, response, null,
		"act.do?action=act.list");
		return;
	}
	
	CompleteAct ca = (CompleteAct) ob.getClass().getMethod("getCompleteAct").invoke(ob);
%>
 <% if (!readonly) { %> <form action="<%=Urls.urlAppBase %>complete.do" method="post"> <%} %>
    <fieldset>	
   	       <legend><%=u.getTexto("at.act.conc.act.leg") %></legend>
   	       
   	       <label style="display: block;  margin-right: 10px;">
   	       <input type="radio" name="complete" tabindex="10" value="undefined" <%=ca==null?"checked":"" %>/><%=u.getTexto("at.act.conc.act.indf") %></label>
   	       
   	       <label style="display: block;  margin-right: 10px;">
   	       <input type="radio" name="complete" tabindex="10" value="time-limit" <%=ca!=null&&ca.getTimeLimit()!= null?"checked": "" %>/><%=u.getTexto("at.act.conc.act.tempo") %> 
   	
   	       </label>
   	       <script type="text/javascript">
			   	    $(document).ready(function() {
			
			   	    	$("input[name='complete']").click(function(event) {
			   	    		if (this.defaultValue == 'undefined') {
			   	    			$('#tempoCompleteAct').hide();
			   	    			$('#rpCompleteAct').hide();
			   	    		}
			   	    		else if (this.defaultValue == 'time-limit') {
			   	    			$('#tempoCompleteAct').show();
			   	    			$('#rpCompleteAct').hide();
			   	    		}
			   	    		else if (this.defaultValue == 'role-part') {
			   	    			$('#tempoCompleteAct').hide();
			   	    			$('#rpCompleteAct').show();
			   	    		}
			   	    	});
			   	    });
   	       </script>
   	       <label style="display: <%=ca!=null&&ca.getTimeLimit()!= null?"block": "none" %>;  margin-right: 10px;" id="tempoCompleteAct">
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=ca!=null && ca.getTimeLimit()!= null && ca.getTimeLimit().getValue().getYears() != 0 ? ca.getTimeLimit().getValue().getYears(): "" %>"/><%=u.getTexto("at.act.conc.act.y") %> 
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=ca!=null && ca.getTimeLimit()!= null && ca.getTimeLimit().getValue().getMonths() != 0 ? ca.getTimeLimit().getValue().getMonths(): "" %>"/><%=u.getTexto("at.act.conc.act.m") %>
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=ca!=null && ca.getTimeLimit()!= null && ca.getTimeLimit().getValue().getDays() != 0 ? ca.getTimeLimit().getValue().getDays(): "" %>"/><%=u.getTexto("at.act.conc.act.d") %>
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=ca!=null && ca.getTimeLimit()!= null && ca.getTimeLimit().getValue().getHours() != 0 ? ca.getTimeLimit().getValue().getHours(): "" %>"/><%=u.getTexto("at.act.conc.act.h") %>
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=ca!=null && ca.getTimeLimit()!= null && ca.getTimeLimit().getValue().getMinutes() != 0 ? ca.getTimeLimit().getValue().getMinutes(): "" %>"/><%=u.getTexto("at.act.conc.act.mm") %>
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=ca!=null && ca.getTimeLimit()!= null && ca.getTimeLimit().getValue().getSeconds() != 0 ? ca.getTimeLimit().getValue().getSeconds(): "" %>"/><%=u.getTexto("at.act.conc.act.s") %>  <br> <br></label>
   	     
   	       <label style="display: block;  margin-right: 10px;"><input type="radio" name="complete" tabindex="10" value="role-part" <%=ca!=null&&ca.getWhenRolePartCompletedList().size()>0?"checked": "" %>/><%=u.getTexto("at.act.conc.act.rp") %></label>
   	       
   	       <fieldset style="display: <%=ca!=null&&ca.getWhenRolePartCompletedList().size()>0?"block": "none" %>; margin-right: 10px;" id="rpCompleteAct">
   	       
			<jsp:include page="../methods/rp/rp.checkbox.all.list.for.complete.act.jsp"></jsp:include>


   	       </fieldset>
   	       <% if (!readonly) { %><br><br>
		   <label><input type="submit" value="Guardar"></label>
    	   <input type="hidden" name="ldep" value="<%=ldep%>" />
    	   <input type="hidden" name="id-ref" value="<%=idRef%>" />
    	   <input type="hidden" name="field" value="<%=field%>" />
    	   <input type="hidden" name="aba" value="<%=aba%>" />
    	   <input type="hidden" name="action" value="complete.act.save" />
    	   <%} %>
	</fieldset>	
   	
 <% if (!readonly) { %></form><%} %>	  
	
   	