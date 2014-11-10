<%@page import="org.imsglobal.jaxb.ld.CompletePlay"%>
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
boolean readonly = !u.hasPermissoes("complete.play.save");

    String aba = Suport.r(request, "aba");	
    String ldep = Suport.r(request, "ldep");
    String idRef = Suport.r(request, "id-ref");
    String field = Suport.r(request, "field");    
    

	Object ob = ImeWorkspace.getImeWorkspace(request).getObject(idRef);

	if (ob == null) {

		Urls.forwardAction(request, response, null,
		"play.do?action=play.list");
		return;
	}
	
	CompletePlay cp = (CompletePlay) ob.getClass().getMethod("getCompletePlay").invoke(ob);
	
	if (cp != null && cp.getTimeLimit() != null && cp.getTimeLimit().getValue() == null)
		cp = null;
%>
 <% if (!readonly) { %> <form action="<%=Urls.urlAppBase %>complete.do" method="post"> <%} %>
    <fieldset>	
   	       <legend><%=u.getTexto("at.play.conc.py.leg") %></legend>
   	       
   	       <label style="display: block;  margin-right: 10px;">
   	       <input type="radio" name="complete" tabindex="10" value="undefined" <%=cp==null?"checked":"" %>/><%=u.getTexto("at.play.conc.py.indf") %></label>
   	       
   	       <label style="display: block;  margin-right: 10px;">
   	       <input type="radio" name="complete" tabindex="10" value="time-limit" <%=cp!=null&&cp.getTimeLimit()!= null?"checked": "" %>/><%=u.getTexto("at.play.conc.py.tempo") %>
   	
   	       </label>
   	       <script type="text/javascript">
			   	    $(document).ready(function() {
			
			   	    	$("input[name='complete']").click(function(event) {
			   	    		if (this.defaultValue == 'undefined') {
			   	    			$('#tempoCompletePlay').hide();			   	    			
			   	    		}
			   	    		else if (this.defaultValue == 'time-limit') {
			   	    			$('#tempoCompletePlay').show();
			   	    		}
			   	    		else if (this.defaultValue == 'when-act-last-completed') {
			   	    			$('#tempoCompletePlay').hide();			   	    			
			   	    		}
			   	    	});
			   	    });
   	       </script>
   	       <label style="display: <%=cp!=null&&cp.getTimeLimit()!= null?"block": "none" %>;  margin-right: 10px;" id="tempoCompletePlay">
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=cp!=null && cp.getTimeLimit()!= null && cp.getTimeLimit().getValue().getYears() != 0 ? cp.getTimeLimit().getValue().getYears(): "" %>"/><%=u.getTexto("at.play.conc.py.y") %> 
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=cp!=null && cp.getTimeLimit()!= null && cp.getTimeLimit().getValue().getMonths() != 0 ? cp.getTimeLimit().getValue().getMonths(): "" %>"/><%=u.getTexto("at.play.conc.py.m") %>
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=cp!=null && cp.getTimeLimit()!= null && cp.getTimeLimit().getValue().getDays() != 0 ? cp.getTimeLimit().getValue().getDays(): "" %>"/><%=u.getTexto("at.play.conc.py.d") %>
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=cp!=null && cp.getTimeLimit()!= null && cp.getTimeLimit().getValue().getHours() != 0 ? cp.getTimeLimit().getValue().getHours(): "" %>"/><%=u.getTexto("at.play.conc.py.h") %>
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=cp!=null && cp.getTimeLimit()!= null && cp.getTimeLimit().getValue().getMinutes() != 0 ? cp.getTimeLimit().getValue().getMinutes(): "" %>"/><%=u.getTexto("at.play.conc.py.mm") %>
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=cp!=null && cp.getTimeLimit()!= null && cp.getTimeLimit().getValue().getSeconds() != 0 ? cp.getTimeLimit().getValue().getSeconds(): "" %>"/><%=u.getTexto("at.play.conc.py.s") %>  <br> <br></label>
   	     
   	       <label style="display: block;  margin-right: 10px;"><input type="radio" name="complete" tabindex="10" value="when-act-last-completed" <%=cp!=null&&cp.getWhenLastActCompleted() != null?"checked": "" %>/><%=u.getTexto("at.play.conc.py.act") %></label>
   	       
   	       <% if (!readonly) { %><br><br>
		   <label><input type="submit" value="<%=u.getTexto("at.play.conc.py.guardar") %>"></label>
    	   <input type="hidden" name="ldep" value="<%=ldep%>" />
    	   <input type="hidden" name="id-ref" value="<%=idRef%>" />
    	   <input type="hidden" name="field" value="<%=field%>" />
    	   <input type="hidden" name="aba" value="<%=aba%>" />
    	   <input type="hidden" name="action" value="complete.play.save" />
    	   <%} %>
	</fieldset>	
   	
 <% if (!readonly) { %></form><%} %>	  
	
   	