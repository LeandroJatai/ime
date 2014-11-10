<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%=Suport.getMessageWarning(request)%>

<form action="usuario.do" method="post">
	<fieldset>
		<legend>Redefinição de Senha</legend>
		<fieldset style="display: inline;">
			<legend>Login de Usuário</legend>
			<label><input type="text" name="login" value="" size="25" /></label>
		</fieldset>
		<fieldset style="display: inline;">
			<legend>Senha</legend>
			<label><input type="password" name="senhaOld" value=""
				size="15" /></label>
		</fieldset>
		<fieldset style="display: inline;">
			<legend>Nova Senha</legend>
			<label><input type="password" name="senhaNew" value=""
				size="15" /></label>
		</fieldset>
		<fieldset style="display: inline;">
			<legend>Repita a nova Senha</legend>
			<label><input type="password" name="senhaRepeat" value=""
				size="15" /></label>
		</fieldset>

		<fieldset>
			<input type="submit" name="action" value="Trocar"></label>
		</fieldset>
	</fieldset>

</form>
