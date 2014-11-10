<%@page import="org.imsglobal.jaxb.ld.Method"%>
<%@page import="br.edu.ifg.ime.controllers.MethodController"%>
<%@page import="org.imsglobal.jaxb.ld.CompleteUnitOfLearning"%>
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
<%@page import="org.imsglobal.jaxb.ld.ItemModel"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	// ItemModel im = ItemModelController.getItemModel(request);

Usuario u = UsuarioController.getUsuarioConectado(session);
boolean readonly = !u.hasPermissoes("complete.ldep.save");

    String aba = Suport.r(request, "aba");	
    String ldep = Suport.r(request, "ldep");
    String idRef = Suport.r(request, "id-ref");
    String field = Suport.r(request, "field");    
    
    
	Method met =  MethodController.getMethod(ImeWorkspace.getImeWorkspace(request).getLdProjectByIdentifier(ldep));
	CompleteUnitOfLearning cUol = met.getCompleteUnitOfLearning();
	
	if (cUol != null && cUol.getTimeLimit() != null && cUol.getTimeLimit().getValue() == null)
		cUol = null;
%>
 <% if (!readonly) { %> <form action="<%=Urls.urlAppBase %>complete.do" method="post"> <%} %>
    <fieldset>	
   	       <legend><%=u.getTexto("at.ldep.conc.un.leg") %></legend>
   	       
   	       <label style="display: block;  margin-right: 10px;">
   	       <input type="radio" name="complete" tabindex="10" value="undefined" <%=cUol==null?"checked":"" %>/><%=u.getTexto("at.ldep.conc.un.indf") %></label>
   	       
   	       <label style="display: block;  margin-right: 10px;">
   	       <input type="radio" name="complete" tabindex="10" value="time-limit" <%=cUol!=null&&cUol.getTimeLimit()!= null?"checked": "" %>/><%=u.getTexto("at.ldep.conc.un.tempo") %> 
   	
   	       </label>
   	       <script type="text/javascript">
			   	    $(document).ready(function() {
			
			   	    	$("input[name='complete']").click(function(event) {
			   	    		if (this.defaultValue == 'undefined') {
			   	    			$('#tempoCompleteUol').hide();
			   	    			$('#playCompleteUol').hide();
			   	    		}
			   	    		else if (this.defaultValue == 'time-limit') {
			   	    			$('#tempoCompleteUol').show();
			   	    			$('#playCompleteUol').hide();
			   	    		}
			   	    		else if (this.defaultValue == 'play') {
			   	    			$('#tempoCompleteUol').hide();
			   	    			$('#playCompleteUol').show();
			   	    		}
			   	    	});
			   	    });
   	       </script>
   	       <label style="display: <%=cUol!=null&&cUol.getTimeLimit()!= null?"block": "none" %>;  margin-right: 10px;" id="tempoCompleteUol">
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=cUol!=null && cUol.getTimeLimit()!= null && cUol.getTimeLimit().getValue().getYears() != 0 ? cUol.getTimeLimit().getValue().getYears(): "" %>"/><%=u.getTexto("at.ldep.conc.un.tempo.y") %> 
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=cUol!=null && cUol.getTimeLimit()!= null && cUol.getTimeLimit().getValue().getMonths() != 0 ? cUol.getTimeLimit().getValue().getMonths(): "" %>"/><%=u.getTexto("at.ldep.conc.un.tempo.m") %>
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=cUol!=null && cUol.getTimeLimit()!= null && cUol.getTimeLimit().getValue().getDays() != 0 ? cUol.getTimeLimit().getValue().getDays(): "" %>"/><%=u.getTexto("at.ldep.conc.un.tempo.d") %>
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=cUol!=null && cUol.getTimeLimit()!= null && cUol.getTimeLimit().getValue().getHours() != 0 ? cUol.getTimeLimit().getValue().getHours(): "" %>"/><%=u.getTexto("at.ldep.conc.un.tempo.h") %>
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=cUol!=null && cUol.getTimeLimit()!= null && cUol.getTimeLimit().getValue().getMinutes() != 0 ? cUol.getTimeLimit().getValue().getMinutes(): "" %>"/><%=u.getTexto("at.ldep.conc.un.tempo.mm") %>
   	       <input style="text-align: center;" type="text" name="tempo" size="3" maxlength="2" value="<%=cUol!=null && cUol.getTimeLimit()!= null && cUol.getTimeLimit().getValue().getSeconds() != 0 ? cUol.getTimeLimit().getValue().getSeconds(): "" %>"/><%=u.getTexto("at.ldep.conc.un.tempo.s") %>  <br> <br></label>
   	     
   	       <label style="display: block;  margin-right: 10px;"><input type="radio" name="complete" tabindex="10" value="play" <%=cUol!=null&&cUol.getWhenPlayCompletedList().size()>0?"checked": "" %>/><%=u.getTexto("at.ldep.conc.un.play") %></label>
   	       
   	       <fieldset style="display: <%=cUol!=null&&cUol.getWhenPlayCompletedList().size()>0?"block": "none" %>; margin-right: 10px;" id="playCompleteUol">
   	       
			<jsp:include page="../methods/plays/play.checkbox.all.list.for.complete.uol.jsp"></jsp:include>


   	       </fieldset>
   	       <% if (!readonly) { %><br><br>
		   <label><input type="submit" value="<%=u.getTexto("at.ldep.conc.un.guardar") %>"></label>
    	   <input type="hidden" name="ldep" value="<%=ldep%>" />
    	   <input type="hidden" name="id-ref" value="<%=idRef%>" />
    	   <input type="hidden" name="field" value="<%=field%>" />
    	   <input type="hidden" name="aba" value="<%=aba%>" />
    	   <input type="hidden" name="action" value="complete.ldep.save" />
    	   <%} %>
	</fieldset>	
   	
 <% if (!readonly) { %></form><%} %>	  
	
   	