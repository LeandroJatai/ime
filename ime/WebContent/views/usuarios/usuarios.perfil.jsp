<%@page import="br.edu.ifg.ime.dto.Linguagem"%>
<%@page import="br.edu.ifg.ime.controllers.LinguagemController"%>
<%@page import="br.edu.ifg.ime.dto.Permissoes"%>
<%@page import="br.edu.ifg.ime.dto.Perspectiva"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="java.util.List"%>
<%@page import="br.edu.ifg.ime.dto.Grp_servicos"%>
<%@page import="br.edu.ifg.ime.dto.Servico"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%
	Usuario u = UsuarioController.getUsuarioConectado(session);
//LinguagemController.transferirTextoServicosParaChavesTextuais();
%>

<div id="LeftPane">


<fieldset id="viewsList">
	<legend><%=u.getTexto("ime.users.perps.leg") %></legend>
	

	<% 
	
	String strIdPerspectiva = request.getParameter("idPerspectiva");

	UsuarioController.refreshPerspectivasForUsuario(u);
	UsuarioController.refreshPermissoesForUsuario(u);
	List<Perspectiva> listaPerspectivas = u.getL_perspectivas();
	%>
	
	<label class="list">
	 	<a class="<%=strIdPerspectiva == null ? "selected" : ""  %>" href="<%=Urls.urlAppBase%>usuario.do?action=manage.users.perspectiva"><%=u.getTexto("ime.perspectiva.padrao") %></a>
	</label>
	<hr style="border: 0px solid #000; border-top-width: 1px;">
	<%
	Perspectiva p = null;
	for (Perspectiva ap: listaPerspectivas) {
		
		if (ap.getId() == Integer.parseInt(strIdPerspectiva==null?"0":strIdPerspectiva))
			p = ap;
	
	%>
	
	<label class="list">
	 		<a class="controls-objs right" href="usuario.do?action=manage.users.perspectiva.remove&idPerspectiva=<%=ap.getId() %>" title="<%=u.getTexto("ime.users.perps.excluir.tit") %>"><%=u.getTexto("ime.users.perps.excluir.btn") %></a>
		 
	<a class="<%=p != null && p.getId() == ap.getId()?"selected":"" %>"  href="<%=Urls.urlAppBase%>usuario.do?action=manage.users.perspectiva&idPerspectiva=<%=ap.getId()%>">
	<%= ap.getTitulo() %></a>
	</label>
	
	<% } %>
	
	</fieldset>
	
</div>



<div id="RightPane">


        <form action="<%=Urls.urlAppBase %>usuario.do" method="post"> 
  <fieldset id="usuarioEdit">
	<legend><%=u.getTexto("ime.users.perps.edit.leg") %></legend>
	
		<%	List<Usuario> listaTiposUsuarios = UsuarioController.getTipoDeUsuarios();
		List<Linguagem> lLinguagens = LinguagemController.getLinguagens();
	%>
	
	
	
					<fieldset class="controles" >
	
	
	<fieldset class="right">
		<legend><%=u.getTexto("ime.users.lang.leg") %></legend>
	<label >
		<select id="userIdLang" name="userIdLang">
			<option value="0"> </option>
			<% for (Linguagem lang: lLinguagens)  { 
			    boolean checkLang = false;
				if (p != null && p.getLinguagem() != null && p.getLinguagem().getId() == lang.getId())
					checkLang = true;
			
			%>
			<option  value="<%=lang.getId()%>" <%=checkLang?"selected=\"selected\"":"" %>><%=lang.getTitulo() %></option>
		<%} %>
		
		</select>
	  </label>
	</fieldset>
										<fieldset class="right">
		<legend><%=u.getTexto("ime.users.perfil.leg") %></legend>
	<label >
		<select id="userIdPerfil" name="userIdPerfil">
			<option value="<%=u!=null?u.getId():"0"%>"> </option>
			<% for (Usuario tipoUsuario: listaTiposUsuarios)  { %>
			<option  value="<%=tipoUsuario.getId()%>"><%=tipoUsuario.getNome() %></option>
		<%} %>
		
		</select>
	  </label>	 
		</fieldset>
						<label >
	  	<input type="text" name="titulo" value="<%=p!=null?p.getTitulo():""%>"/>
	  </label>	 <label><input type="submit" value="<%=p!=null?u.getTexto("ime.users.persp.guardar"):u.getTexto("ime.users.persp.guardar.novo")%>"></label>
					</fieldset>
	
<fieldset id="servicosList">
	<legend><%=u.getTexto("ime.users.servicos.leg") %></legend>
		<%
			Grp_servicos grupo = null;
			
			
			for (Permissoes perm: UsuarioController.getPermissoesForUsuario(u)) {

				
				if (grupo == null || (grupo != null && perm.getServico().getGrupo().getId() != grupo.getId())) {

					if (grupo != null)
						out.println("</fieldset>");
					
					grupo = perm.getServico().getGrupo();
					out.println("<fieldset><legend>"+u.getTexto(grupo.getTitulo(),LinguagemController.chaveDb)+"</legend>");
				}
		     %>

				<label class="list"  title="<%=perm.getServico().getServico() %>" >
				 	<input type="checkbox" name="servico.item" value="<%=perm.getServico().getServico() %>" <%=UsuarioController.checkPermissao(u, perm.getServico(),p)?"checked":"" %>/><%=u.getTexto(perm.getServico().getDescricao(), LinguagemController.chaveDb)%>
				 	</label>
			
		<% }
			if (u.getL_permissoes() != null && u.getL_permissoes().size() > 0)
				out.println("</fieldset>");
			
			%>
</fieldset>

					<input type="hidden" name="idPerspectiva" value="<%=p!=null?p.getId():0%>" />
					<input type="hidden" name="idUser" value="<%=u!=null?u.getId():0%>" />
					<input type="hidden" name="action" value="manage.users.perspectiva" />
</fieldset>

</form>
</div>

<div id="vsplitbar"></div>
