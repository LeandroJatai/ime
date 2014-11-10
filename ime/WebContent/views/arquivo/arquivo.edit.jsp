<%@page import="com.sun.xml.internal.bind.v2.model.core.ID"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Stack"%>
<%@page import="java.util.Queue"%>
<%@page import="br.edu.ifg.ime.controllers.ArquivoController"%>
<%@page import="br.edu.ifg.ime.dto.Arquivo"%>
<%@page import="java.util.TreeMap"%>
<%@page import="br.edu.ifg.ime.dto.Chave_textual"%>
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
	%>
<div id="LeftPane">

<span style="font-size: 90%; color: #776;">Lista as notas no final da lista de arquivos.</span>
	
<fieldset id="usuariosList">
	<legend><%=u.getTexto("ime.arquivo.leg") %></legend>
<ul class="arvoreRaiz">
<%
String strIdArq = Suport.r(request, "id");

TreeMap<String, Object> listaArquivos = ArquivoController.getArquivos();
TreeMap<String, Object> al = listaArquivos;
int nivel = 0;
String pathAtual = "";

Arquivo aq = null;
Arquivo aqIt = null;
Stack<TreeMap<String, Object>> pilha = new Stack<TreeMap<String, Object>>();
boolean contemPasta = false;
boolean contemArquivo = false;

pilha.add(listaArquivos);

	while (!pilha.isEmpty()) {
		
		al = pilha.peek();
		
		
		contemPasta = false;
		contemArquivo = true;
		while (contemArquivo) {
			
			contemArquivo = false;
			String arq = null;
			for (String auxArq:  al.keySet()) {				
				if (al.get(auxArq) instanceof Arquivo) {
					contemArquivo = true;
					arq = auxArq;
					break;
				}
				else contemPasta = true;
			}
			if (contemArquivo) {
				aqIt = (Arquivo)al.remove(arq);
				
				if (strIdArq != null && aqIt.getId() == Integer.parseInt(strIdArq))
					aq = aqIt;
					out.println("<li><a "+(aq!=null && aq == aqIt?"class=\"selected\"":"")+" href=\""+Urls.urlAppBase+"av?action=manage.arquivo&id="+aqIt.getId()+"\">"+arq+"</a></li>");
		    	
			}

		}
		if (!contemPasta) {
			out.println("</ul></li>");
			pilha.pop();
			continue;
		}
		Entry<String, Object> entry = al.pollFirstEntry();
		out.println("<li><a class=\"arvoreMenos\"> </a><a>"+entry.getKey()+"</a><ul>");
		
		
		pilha.push((TreeMap<String, Object>)entry.getValue());
		
		}
		
		
		%>
	
	
</ul>
	</fieldset>
	
	
	<ul style="margin-left: 20px; margin-right: 20px; font-size: 90%; color: #776;">
	<li style="list-style: square; border-top: 1px solid #aaa; margin-top: 5px;">Todas as pastas podem ter [n] subpastas.</li>
	<li style="list-style: square; border-top: 1px solid #aaa; margin-top: 5px;">Pastas são apenas uma definição lógica, não crie um item com a intenção de usá-lo como pasta, será tratado como arquivo, se assim o proceder. Exemplo: para criar o arquivo [ime.pdf] dentro de uma pasta [teste], que não existe, defina apenas então: /teste/ime.pdf</li>
	<li style="list-style: square; border-top: 1px solid #aaa; margin-top: 5px;">A pasta <b>/_xsd</b> é uma pasta de sistema. O validador busca nesta pasta os arquivos necessários para validação nos seguintes formatos: 
		<ul style="font-size: 90%;">
		<li>IMS_LD_Level_[Level]_LMI_Import.xsd e seus includes</li>
		<li>IMS_LD_Level_[Level].xsd e seus includes</li>
		<li>imscp_v1p2.xsd e seus includes</li>
		</ul>
	<li>
	<li style="list-style: square; border-top: 1px solid #aaa; margin-top: 5px;">A pasta <b>/_skins</b> é uma pasta de sistema. Serão disponibilizados todos os arquivos que estiverem nesta pasta, para vínculo com Modelos instrucionais.
		<ul>
		<li style="list-style: square; margin-top: 2px;">O <b>skin</b> da Área de Trabalho é sempre o <b>skin</b> do primeiro Modelo Instrucional da árvore.</li>
	<li style="list-style: square; margin-top: 2px;">Ás áreas administrativas (Arquivos, Linguagem, Usuários, Perspectivas) não usam <b>skins</b>.</li>
		<li style="list-style: square; margin-top: 2px;">Todos Mi's internos de uma composição podem estar vinculados a diferentes <b>skins</b>, porém, isto ainda (Versão Futura) não é permitido, podendo pois ser vinculado apenas os Mi's principais com reflexo único para todas as composições.</li>
		<li style="list-style: square; margin-top: 2px;">TODO: <b>Skins</b> podem estar associadas a usuários e suas perspectivas, já sendo atribuidas ao acessar o sistema, bem como aplicá-la a qualquer projeto que nao tenha uma <b>skin</b> individual.</li>
		<li style="list-style: square; margin-top: 2px;">TODO: Existem poucos detalhes na associação de tags de html com classes e/ou ID de css que deverão ser organizados para que se tenha liberdade total na criação de <b>skins</b></li>
		</ul>
	</li>
	<li style="list-style: square; border-top: 1px solid #aaa; margin-top: 5px;">Nas Chamadas a arquivos virtuais dentro de arquivos [css] deve-se usar o prefixo [/ime/av], ou com instruções de retorno de pasta [../] para quantos níveis forem necessários. Exemplo:
	<pre style="font-size: 80%;">
  #RightPane {  
     background-image: url('/ime/av/imgs/logoIFGt.png'); /*forma 1*/
     background-image: url('../imgs/logoIFGt.png');      /*forma 2*/
  } 
	</pre></li>
	<li style="list-style: square; border-top: 1px solid #aaa; margin-top: 5px;">Dentro dos skins não vale o item anterior, devendo sempre ser usado o prefíxo [/ime/av]</li>
	<li style="list-style: square; border-top: 1px solid #aaa; margin-top: 5px;">O envio de arquivos tem precedência sobre texto, portanto, caso o usuário anexe um arquivo e coloque um texto, o texto será desconsiderado.</li>

	</ul>
</div>

<div id="RightPane">


<%

boolean flagString = false;
if (aq != null) {
	
	aq = ArquivoController.refreshBytes(aq);
	
	if (!aq.getNome().endsWith("png") &&
			!aq.getNome().endsWith("zip") &&
			!aq.getNome().endsWith("tar.gz") &&
			!aq.getNome().endsWith("jpg") &&
			!aq.getNome().endsWith("jpeg") &&
			!aq.getNome().endsWith("gif")&&
			!aq.getNome().endsWith("bmp"))
	flagString = Suport.isByteArrayIsFileText(aq.getArquivo());
	
	
	
}

%>


<%=Suport.getMessageWarning(request)%>

        <form action="<%=Urls.urlAppBase %>av" method="post" enctype="multipart/form-data" > 

	
    <fieldset>
    
    	<fieldset style="float: right; width: 60%;" >
	<legend>Título/Descrição do Arquivo</legend>
		<label style="display: block;  padding-right: 10px;">
	 	<input  style="width: 100%;" type="text" name="arquivo.titulo" tabindex="20" value="<%=aq!=null?aq.getTitulo():""%>"/> 
	 	</label>
	</fieldset>
	
    <fieldset>
		<legend><%=u.getTexto("ime.arquivo.tit.leg") %></legend>
			
		<label style="display: block; padding-right: 10px;">
	 	<input  style="width: 100%;" type="text" name="arquivo.nome" tabindex="10"  value="<%=aq!=null?aq.getNome():""%>"/> 
	 	</label>
	</fieldset>

    <legend><%=u.getTexto("ime.arquivo.upload.leg") %></legend>
    	<label class="left">			<a class="menu" style="float: right;  font-size: 80% " href="<%=Urls.urlAppBase%>av?action=manage.arquivo"><%=u.getTexto("ime.arquivo.novo") %></a></label>
		<label>
				 &nbsp;&nbsp;
		    	<input type="submit" value="<%=aq!=null?u.getTexto("ime.arquivo.guardar"):u.getTexto("ime.arquivo.guardar.novo")%>">	
	            <input type="hidden" name="id" value="<%=aq!=null?aq.getId():0%>" />
	            <input type="hidden" name="arquivo.unidade" value="<%=aq!=null?aq.getUnidade():1%>" />
				<input type="hidden" value="manage.arquivo.upload" name="action" >	
		    	<input type="file" name="fileData" size="50"/><br>		
		</label>  	
	</fieldset>
	
	<fieldset style="<%= flagString || aq == null ?"":"display: none;" %>">
		<legend><%=u.getTexto("ime.arquivo.texto.leg")%></legend>
		<label style="display: block;">
	 	<textarea style="width: 100%;" name="arquivo.texto" rows=20><%=aq!=null?(flagString?new String(aq.getArquivo()):""):""%></textarea>
	 	</label>
	</fieldset>

	<%
	if (aq!=null && aq.getContent_type().startsWith("image")) {
		%>
		<img src="<%=Urls.url_av_Servlet+aq.getNome()%>"/>
	<%
	}
	%>

</form>
</div>

<div id="vsplitbar"></div>
