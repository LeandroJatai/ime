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
	String ldep = Suport.r(request, "ldep");

    ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
%>

<fieldset>
	<legend><img src="imgs/uol16.png"/>Editor Textual. Projeto: <b><%=w.getLdProjectByIdentifier(ldep).getLd().getTitle()%></b></legend>
									
     	
    <form action="<%=Urls.urlAppBase %>app.do" method="post">
			<fieldset>
           	<legend>Código</legend>
           		<label style="width: 100%;" ><textarea style="display: block; width: 100%;" name="codigo" rows="15"><%= w.getCodeIMSCPOfLdepProject(ldep) %></textarea></label>
           	</fieldset>		
			
			<fieldset class="controles">
	    	   <input type="hidden" name="ldep" value="<%=ldep%>" />
			   <input type="hidden" name="action" value="ldep.save.code.imscp"/>
			   <label><input type="submit" value="Guardar"></label>
			</fieldset>
				<fieldset>
           	<legend>Orientações</legend>
           ﻿<label>1. Esta é uma edição estrutural e contempla todas as possibilidades da especificação IMS <i>Learning Design</i> (IMSLD), do padrão IMS <i>Content Packaging</i> (IMSCP) sem suas extensões, e da Linguagem de Modelos Instrucionais (LMI). </label><br><br>
<label style=" text-indent: -30px; padding-left: 30px;" >2. A LMI permite se fazer composições por meio da inclusão e referência entre manifestos (pai e filho). As possibilidades apresentadas pela LMI nesta versão são:<br>
                 1. Em qualquer elemento <i>&lt;act&gt;</i> de um manifesto (pai), poderá ser incluída a <i>tag &lt;learning-design-ref&gt;</i>, cujo atributo "ref", irá referenciar o identificador (ID) de outro manifesto (filho).<br>
                 2. O número de composições é ilimitado.<br>
                 3. Podem existir vários submanifestos (filhos) referenciados em diferentes atos de um mesmo manifesto (pai). A escrita de manifestos e submanifestos segue rigorosamente a especificação IMSCP.<br>
                 </label><br><br>
<label>3. Ao guardar as alterações, o projeto será substituído integralmente! </label><br>
<label>4. Os prefixos foram abreviados para melhor legibilidade. </label>

           	</fieldset>		
			
    </form>
		</fieldset>