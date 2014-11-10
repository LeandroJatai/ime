<%@page import="br.edu.ifg.ime.controllers.LinguagemController"%>
<%@page import="br.edu.ifg.ime.dto.Linguagem"%>
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
	
<fieldset id="usuariosList">
	<legend><%=u.getTexto("ime.users.cads.leg") %></legend>
	
	<% 
	
	String strIdUser = request.getParameter("idUser");
	
	List<Usuario> listaUsuarios = UsuarioController.getUsuarios();
	Usuario usuario = null;
	for (Usuario auxUser: listaUsuarios) {
		
		if (auxUser.getId() == Integer.parseInt(strIdUser==null?"0":strIdUser))
			usuario = auxUser;
	
	%>
	
	<label class="list">
	 		<a class="controls-objs right" href="usuario.do?action=manage.users.remove&idUser=<%=auxUser.getId() %>" title="<%=u.getTexto("ime.users.excluir.tit") %>"><%=u.getTexto("ime.users.excluir.btn")%> </a>
		 
	<a class="<%=usuario != null && usuario.getId() == auxUser.getId()?"selected":"" %>"  href="<%=Urls.urlAppBase%>usuario.do?action=manage.users&idUser=<%=auxUser.getId()%>">
	<%= auxUser.getNome() %></a>
	</label>
	
	<% } %>
	
	</fieldset>
	
</div>

<%
if (usuario == null && strIdUser != null && Integer.parseInt(strIdUser) <= Usuario.wmx_convidado) 
	usuario = UsuarioController.getUsuario(strIdUser);
 
%>



<div id="RightPane">

<%=Suport.getMessageWarning(request)%>

        <form action="<%=Urls.urlAppBase %>usuario.do" method="post"> 
  <fieldset id="usuarioEdit">
	<legend><%=u.getTexto("ime.users.edit.leg") %></legend>
	
	<fieldset  class="right">
		<legend><%=u.getTexto("ime.users.login.leg") %></legend>
	<label >
	  	<input type="text" name="usuario.login" value="<%=usuario!=null?usuario.getLogin():""%>"/>
	  </label>	 
	  <label>
		<input type="checkbox" name="usuario.senha.reset" value="usr"/><%=u.getTexto("ime.users.reset.passwd") %>
	 	</label>
	</fieldset>
	
	
	<fieldset>
		<legend><%=u.getTexto("ime.users.nome.leg") %></legend>
		<label style="display: block; padding-right: 10px;">
	 	<input  style="width: 100%;" type="text" name="usuario.nome" value="<%=usuario!=null?usuario.getNome():""%>"/> 
	 	</label>
	</fieldset>
	<%
	

	List<Usuario> listaTiposUsuarios = UsuarioController.getTipoDeUsuarios();
	List<Linguagem> lLinguagens = LinguagemController.getLinguagens();
	
	%>
	
	<fieldset class="right">
		<legend><%=u.getTexto("ime.users.lang.leg") %></legend>
	<label >
		<select id="userIdLang" name="userIdLang">
			<option value="0"> </option>
			<% for (Linguagem lang: lLinguagens)  { 
			    boolean checkLang = false;
				if (usuario != null && usuario.getLinguagem() != null && usuario.getLinguagem().getId() == lang.getId())
					checkLang = true;
			
			%>
			<option  value="<%=lang.getId()%>" <%=checkLang?"selected=\"selected\"":"" %>><%=lang.getTitulo() %></option>
		<%} %>
		
		</select>
	  </label>
	</fieldset>
	
	<fieldset>
		<legend><%=u.getTexto("ime.users.perfil.leg") %></legend>
	<label >
		<select id="userIdPerfil" name="userIdPerfil">
			<option value="<%=usuario!=null?usuario.getId():"0"%>"> </option>
			<% for (Usuario tipoUsuario: listaTiposUsuarios)  { 
			    boolean checkPerfil = false;
				if (usuario != null && usuario.getPerfil() != null && usuario.getPerfil().getId() == tipoUsuario.getId())
					checkPerfil = true;
			
			%>
			<option  value="<%=tipoUsuario.getId()%>" <%=checkPerfil?"selected=\"selected\"":"" %>><%=tipoUsuario.getNome() %></option>
		<%} %>
		
		</select>
	  </label>
	  <label><input type="radio" name="userTipoPerfil" value="perfil.livre" <%=usuario==null?"checked":usuario.getTipo_perfil().equals("perfil.livre")?"checked":"" %> title="<%=u.getTexto("ime.users.tp.livre.tit") %>"/><%=u.getTexto("ime.users.tp.livre.leg") %></label>
	  <label><input type="radio" name="userTipoPerfil" value="perfil.vinc"  <%=usuario!=null && usuario.getTipo_perfil().equals("perfil.vinc")?"checked":"" %> title="<%=u.getTexto("ime.users.tp.vinc.tit") %>"/><%=u.getTexto("ime.users.tp.vinc.leg") %></label>
	  <label><input type="radio" name="userTipoPerfil" value="perfil.fixo"  <%=usuario!=null && usuario.getTipo_perfil().equals("perfil.fixo")?"checked":"" %> title="<%=u.getTexto("ime.users.tp.fixo.tit") %>" /><%=u.getTexto("ime.users.tp.fixo.leg") %></label>
	</fieldset>
	
	
	
	
					<fieldset class="controles">
						<label><input type="submit" value="<%=usuario!=null?u.getTexto("ime.users.btn.guardar"):u.getTexto("ime.users.btn.guardar.novo")%>"> &nbsp; &nbsp;<a class="menu" style="float: right;  font-size: 80% " href="<%=Urls.urlAppBase%>usuario.do?action=manage.users"><%=u.getTexto("ime.users.btn.novo") %></a></label>
					</fieldset>
	
<fieldset id="servicosList">
	<legend><%=u.getTexto("ime.users.servicos.leg") %></legend>
	<%
			Grp_servicos grupo = null;
			
			List<Servico>  listaServicos = UsuarioController.getServicosAutenticaveis();
			
			
			for (Servico serv: listaServicos) {

				
				if (grupo == null || (grupo != null && serv.getGrupo().getId() != grupo.getId())) {

			if (grupo != null)
				out.println("</fieldset>");
			
			grupo = serv.getGrupo();
			out.println("<fieldset><legend>"+u.getTexto(grupo.getTitulo(), LinguagemController.chaveDb)+"</legend>");
				}
		%>

	<label class="list"  title="<%=serv.getServico() %>" >
	 	<input type="checkbox" name="servico.item" value="<%=serv.getServico() %>" <%=UsuarioController.checkPermissao(usuario, serv, null)?"checked":"" %>/><%=u.getTexto(serv.getDescricao(), LinguagemController.chaveDb)%>
	 	</label>

<% }
	if (listaServicos != null && listaServicos.size() > 0)
		out.println("</fieldset>");
	
	%>
</fieldset>

					<input type="hidden" name="idUser" value="<%=usuario!=null?usuario.getId():0%>" />
					<input type="hidden" name="action" value="manage.users" />
</fieldset>

</form>
</div>

<div id="vsplitbar"></div>
