<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.edu.ifg.ime.ld.LearningDesignRef"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@page import="org.imsglobal.jaxb.ld.RolePart"%>
<%@page import="org.imsglobal.jaxb.ld.Act"%>
<%@page import="br.edu.ifg.ime.controllers.MethodController"%>
<%@page import="org.imsglobal.jaxb.ld.Play"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="org.imsglobal.jaxb.ld.Environment"%>
<%@page import="org.imsglobal.jaxb.ld.ActivityStructure"%>
<%@page import="org.imsglobal.jaxb.ld.SupportActivity"%>
<%@page import="org.imsglobal.jaxb.ld.LearningActivity"%>
<%@page import="br.edu.ifg.ime.suport.LearningDesignUtils"%>
<%@page import="java.io.Serializable"%>
<%@page import="br.edu.ifg.ime.controllers.ActivityController"%>
<%@page import="org.imsglobal.jaxb.ld.Staff"%>
<%@page import="br.edu.ifg.ime.controllers.RolesController"%>
<%@page
	import="br.edu.ifg.ime.controllers.ProjetoController"%>
<%@page import="org.imsglobal.jaxb.ld.LearningDesign"%>
<%@page import="org.imsglobal.jaxb.ld.Learner"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	Usuario u = UsuarioController.getUsuarioConectado(session);

if (u == null)
	return; 

	ImeWorkspace w = ImeWorkspace
	.getImeWorkspace(request);

	String identifier = Suport.r(request, "identifier");
	String idRef = Suport.r(request, "id-ref");

	if (idRef != null) {
		identifier = idRef;
	}

	String strLdep = Suport.r(request, "ldep");
	String actRef = Suport.r(request, "act-ref");
	String ldepRef = Suport.r(request, "ldep-ref");
	String playRef = Suport.r(request, "play-ref-port");

	LdProject el = (LdProject) request.getAttribute("el");

	request.removeAttribute("el");

	List<LdProject> lldep = null;

	if (el == null) {
		lldep = w.getMasterListLdProject();
		w.constructViewTree = 0;

	} else {
		lldep = new ArrayList<LdProject>();
		lldep.add(el);
	}
%>

<%
	if (w.constructViewTree == 0) {
%>

<%
	} else if (w.constructViewTree >= 32) {
		return;
	}
%>
<ul <%if (el == null) {%> id="raizMIEdition" <%}%>
	class="arvoreRaiz <%if (el == null && u != null && u.hasPermissoes("ldep.switch.pos.ldep")) {%>moveLdep<%}%>">

	<%
		w.constructViewTree++;

			for (LdProject ldep : lldep) {

		LearningDesign ld = ldep.getLd();

		List<ArrayList<Learner>> lLearners = RolesController.getLearners(ldep);
		List<ArrayList<Staff>> lStaffs = RolesController.getStaffs(ldep);

		List<Serializable> lActivities = ActivityController
		.getActivities(ldep)
		.getLearningActivityOrSupportActivityOrActivityStructure();

		List<Environment> lEnvs = EnvironmentsController
		.getEnvironments(ldep).getEnvironmentList();

		List<Play> lPlays = MethodController.getMethod(ldep)
		.getPlayList();
		
			int countActionLdeps = 2;
			if (!ldep.isAgregado() && !u.hasPermissoes("act.remove.agregado")) countActionLdeps--;
			if (!ldep.isAgregado() && !u.hasPermissoes("ldep.comp.mi.roles.config")) countActionLdeps--;
	%> 

	<li id="ldepId-<%=ldep.getId()%>" onmouseover="liHover(event, <%=countActionLdeps%>);" onmouseout="liOut(event);"><a 
		class="<%=(ldep.flagTreeView ? "arvoreMenos" : "arvoreMais")%>"
		href="app.do?action=ldep.ftv&identifier=<%=ldep.getIdentifier()%>">
	</a> <a
		class="<%=(ldep == w.getLdProject(request)) ? "selected"
						: ""%> <%=ldep.getIdentifier().equals(strLdep)
						&& identifier == null ? "selectedEdit" : ""%> <%=(el == null ? "" : "acts")%>"
		href="app.do?action=ldep.edit&ldep=<%=ldep.getIdentifier()%>"><%=ld.getTitle()%><b style="font-size: 120%;" title="<%=u.getTexto("at.db.ldep.nao.salvo")%>"><%=ldep.flagAlterado && w.constructViewTree==1?"* ":""%></b></a>


 		<%
 			if (ldep.isAgregado() && u.hasPermissoes("act.remove.agregado")) {
 		%> <span style="float: right; padding-right: 5px;"> <a
			class="controls"
			href="act.do?action=act.remove.agregado&act-ref=<%=actRef%>&ldep=<%=ldepRef%>&ldep-ref=<%=ldep.getIdentifier()%>&play-ref=<%=playRef%>"
			title="<%=u.getTexto("at.mt.act.mi.btn.excluir.titulo")%>"><%=u.getTexto("at.mt.act.mi.btn.excluir.ico")%></a></span> <%
 	}
 %>
 
		<%
 			if (ldep.isAgregado() && u.hasPermissoes("ldep.comp.mi.roles.config")) {
 		%> <span style="float: right; padding-right: 5px;"> <a
			class="controls"
			href="app.do?action=ldep.comp.mi.roles.config&ldep=<%=ldepRef%>&ldepAgr=<%=ldep.getIdentifier()%>"
			title="<%=u.getTexto("at.mt.act.mi.btn.intr.papeis.titulo")%>"><%=u.getTexto("at.mt.act.mi.btn.intr.papeis.ico")%></a></span> <%
 	}
 %>
			
 <%
			 	if (u.hasPermissoes("view.construction")) {
			 %>

		<ul <%=ldep.flagTreeView ? "" : "class=\"ulFechado\""%>>
		
		
			<li>
			<a class="arvoreMenos" href="app.do?action=ldep.ftv"> </a><a
				href="#"><%=u.getTexto("at.cp")%></a>
				<ul>
					
					<%
											int countActionRoles = 1;			
																													if (!u.hasPermissoes("roles.edit.new")) countActionRoles--;
										%>
				
					<li onmouseover="liHover(event, <%=countActionRoles%>);" onmouseout="liOut(event);">
						<a class="arvoreMenos" href="#"> </a>
						<a href="roles.do?action=roles.list&ldep=<%=ldep.getIdentifier()%>"><%=u.getTexto("at.cp.role.titulo.leg")%></a>
 						<%
 							if (u.hasPermissoes("roles.edit.new")) {
 						%>
							<span style="float: right; padding-right: 5px;">
							    <a class="controls" href="<%=Urls.urlAppBase%>roles.do?action=roles.edit.new&ldep=<%=ldep.getIdentifier()%>"
								title="<%=u.getTexto("at.cp.role.btn.novo.titulo")%>"><%=u.getTexto("at.cp.role.btn.novo.ico")%></a>
							</span>
						<%
							}
						%>
					
						<ul>
							<li><a
								class="<%=(lLearners != null && lLearners.size() > 0) ? "arvoreMenos"
						: "folhaLearners"%>"
								href="#"> </a> <a
								href="roles.do?action=roles.list&ldep=<%=ldep.getIdentifier()%>"><%=u.getTexto("at.cp.role.learner.leg")%></a>
								<ul>
									<%
										boolean flagFirst = true;
																														
																														for (ArrayList<Learner> gLearners : lLearners) {

																															for (Learner learner : gLearners) {
																																
																																countActionRoles = 1;

																																if (!u.hasPermissoes("roles.remove")) countActionRoles--;
									%>
									<li onmouseover="liHover(event, <%=countActionRoles%>);" onmouseout="liOut(event);">
									
									<a class="learner <%=learner.getIdentifier().equals(identifier) ? "selectedEdit" : ""%><%=flagFirst?"":" inherit"%>"
										href="roles.do?action=roles.edit&ldep=<%=ldep.getIdentifier()%>&identifier=<%=learner.getIdentifier()%>"><%=learner.getTitle()%></a>
										
										
										<%
																															if (u.hasPermissoes("roles.remove")) {
																														%>
										<span class="learner" style="float: right; padding-right: 5px;"><a
											class="controls"
											href="roles.do?action=roles.remove&identifier=<%=learner.getIdentifier()%>&ldep=<%=ldep.getIdentifier()%>"
											title="<%=u.getTexto("at.cp.role.btn.excluir.titulo")%>"><%=u.getTexto("at.cp.role.btn.excluir")%></a></span> 
										<%
 											}
 										%>	
										</li>
									<%
										}
																															flagFirst = false;
																							 										}
									%>
								</ul></li>
								
							<li><a
								class="<%=(lStaffs != null && lStaffs.size() > 0) ? "arvoreMenos" : "folhaStaffs"%>"
								href="#"> </a><a
								href="roles.do?action=roles.list&ldep=<%=ldep.getIdentifier()%>"><%=u.getTexto("at.cp.role.staff.leg")%></a>

								<ul>
									<%
										flagFirst = true;
																														for (ArrayList<Staff> gStaffs : lStaffs) {

																															for (Staff staff : gStaffs) {

																																countActionRoles = 1;

																																if (!u.hasPermissoes("roles.remove")) countActionRoles--;
									%>
									<li onmouseover="liHover(event, <%=countActionRoles%>);" onmouseout="liOut(event);"><a
										class="staff <%=staff.getIdentifier().equals(identifier) ? "selectedEdit" : ""%><%=flagFirst?"":" inherit"%>"
										href="roles.do?action=roles.edit&ldep=<%=ldep.getIdentifier()%>&identifier=<%=staff.getIdentifier()%>"><%=staff.getTitle()%></a>
										
											
										<%
																																if (u.hasPermissoes("roles.remove")) {
																															%>
										<span class="staff"  style="float: right; padding-right: 5px;"><a
											class="controls"
											href="roles.do?action=roles.remove&identifier=<%=staff.getIdentifier()%>&ldep=<%=ldep.getIdentifier()%>"
											title="<%=u.getTexto("at.cp.role.btn.excluir.titulo")%>"><%=u.getTexto("at.cp.role.btn.excluir")%></a></span> 
										<%
 											}
 										%>	
											</li>
									<%
										}
																															flagFirst = false;
																														}
									%>
								</ul></li>

						</ul></li>
						
						<%
													int countActionActivities = 1;			
																																			if (!u.hasPermissoes("activities.edit")) countActionActivities--;
												%>
				
					<li onmouseover="liHover(event, <%=countActionActivities%>);" onmouseout="liOut(event);">
						<a class="arvoreMenos" href="#"> </a>
						<a href="activities.do?action=activities.list&ldep=<%=ldep.getIdentifier()%>"><%=u.getTexto("at.cp.at.tit.leg")%></a>
 						<%
 							if (u.hasPermissoes("activities.edit")) {
 						%>
							<span style="float: right; padding-right: 5px;">
							    <a class="controls" href="<%=Urls.urlAppBase%>activities.do?action=activities.edit&ldep=<%=ldep.getIdentifier()%>"
								title="<%=u.getTexto("at.cp.at.btn.novo.titulo")%>"><%=u.getTexto("at.cp.at.btn.novo.ico")%></a>
							</span>
						<%
							}
						%>
						<ul>

							<li><a class="arvoreMenos" href="app.do?action=ldep.ftv">
							</a><a href="activities.do?action=activities.list&ldep=<%=ldep.getIdentifier()%>"><%=u.getTexto("at.cp.la.tit.leg")%></a>
								<ul>
									<%
										for (Serializable activity : lActivities) {

																																	if (LearningDesignUtils.isLearningActivity(activity)) {
																																		LearningActivity la = (LearningActivity) activity;
																																		
																																		countActionActivities = 1;
																																		if (!u.hasPermissoes("activities.remove")) countActionActivities--;
									%>
									<li class="activities"  onmouseover="liHover(event, <%=countActionActivities%>);" onmouseout="liOut(event);"><a
										class="learning-activity <%=la.getIdentifier().equals(identifier) ? "selectedEdit"
								: ""%>"
										href="activities.do?action=activities.edit.la&ldep=<%=ldep.getIdentifier()%>&identifier=<%=la.getIdentifier()%>"><%=la.getTitle()%></a>
										
										<%
																					if (u.hasPermissoes("activities.remove")) {
																				%>
										    <span class="activities" style="float: right; padding-right: 5px;"><a
											class="controls"
											href="activities.do?action=activities.remove&identifier=<%=la.getIdentifier()%>&ldep=<%=ldep.getIdentifier()%>"
											title="<%=u.getTexto("at.cp.at.btn.excluir.titulo")%>"><%=u.getTexto("at.cp.at.btn.excluir.ico")%></a></span>
										<%
											}
										%>
											</li>
									<%
										}
																																}
									%>
								</ul></li>
							<li><a class="arvoreMenos" href="app.do?action=ldep.ftv">
							</a><a
								href="activities.do?action=activities.list&ldep=<%=ldep.getIdentifier()%>"><%=u.getTexto("at.cp.sa.tit.leg")%></a>
								<ul>
									<%
										for (Serializable activity : lActivities) {

																																	if (LearningDesignUtils.isSupportActivity(activity)) {
																																		SupportActivity sa = (SupportActivity) activity;
																																		
																																		countActionActivities = 1;
																																		if (!u.hasPermissoes("activities.remove")) countActionActivities--;
									%>
									<li class="activities" onmouseover="liHover(event, <%=countActionActivities%>);" onmouseout="liOut(event);"><a
										class="support-activity <%=sa.getIdentifier().equals(identifier) ? "selectedEdit"
								: ""%>"
										href="activities.do?action=activities.edit.sa&ldep=<%=ldep.getIdentifier()%>&identifier=<%=sa.getIdentifier()%>"><%=sa.getTitle()%></a>
										
										<%
																					if (u.hasPermissoes("activities.remove")) {
																				%>
										    <span class="activities"  style="float: right; padding-right: 5px;"><a
											class="controls"
											href="activities.do?action=activities.remove&identifier=<%=sa.getIdentifier()%>&ldep=<%=ldep.getIdentifier()%>"
											title="<%=u.getTexto("at.cp.sa.btn.excluir.titulo")%>"><%=u.getTexto("at.cp.sa.btn.excluir.ico")%></a></span>
										<%
											}
										%>
										</li>
									<%
										}
																																}
									%>
								</ul></li>
							<li><a class="arvoreMenos" href="app.do?action=ldep.ftv">
							</a><a
								href="activities.do?action=activities.list&ldep=<%=ldep.getIdentifier()%>"><%=u.getTexto("at.cp.as.tit.leg")%></a>
								<ul>
									<%
										for (Serializable activity : lActivities) {

																																	if (LearningDesignUtils.isActivityStructure(activity)) {
																																		ActivityStructure as = (ActivityStructure) activity;
																																		
																																		countActionActivities = 1;
																																		if (!u.hasPermissoes("activities.remove")) countActionActivities--;
									%>
									<li class="activities" onmouseover="liHover(event, <%=countActionActivities%>);" onmouseout="liOut(event);"><a
										class="activity-structure <%=as.getIdentifier().equals(identifier) ? "selectedEdit"
								: ""%>"
										href="activities.do?action=activities.edit.as&ldep=<%=ldep.getIdentifier()%>&identifier=<%=as.getIdentifier()%>"><%=as.getTitle()%></a>
										
										<%
																					if (u.hasPermissoes("activities.remove")) {
																				%>
										    <span class="activities" style="float: right; padding-right: 5px;"><a
											class="controls"
											href="activities.do?action=activities.remove&identifier=<%=as.getIdentifier()%>&ldep=<%=ldep.getIdentifier()%>"
											title="<%=u.getTexto("at.cp.as.btn.excluir.titulo")%>"><%=u.getTexto("at.cp.as.btn.excluir.ico")%></a></span>
										<%
											}
										%>
										
										</li>
									<%
										}
																																}
									%>
								</ul></li>

						</ul></li>
						<%
							int countActionForEnvs = 1;			
																	if (!u.hasPermissoes("environment.edit.new")) countActionForEnvs--;
						%>
				
					<li onmouseover="liHover(event, <%=countActionForEnvs%>);" onmouseout="liOut(event);">
					<a class="arvoreMenos" href="app.do?action=ldep.ftv">
					</a>
					<a href="environment.do?action=environment.list&ldep=<%=ldep.getIdentifier()%>"><%=u.getTexto("at.cp.env.tit.leg")%></a>
					     <%
					     	if (u.hasPermissoes("environment.edit.new")) {
					     %>
							<span style="float: right; padding-right: 5px;">
							    <a class="controls" href="<%=Urls.urlAppBase%>environment.do?action=environment.edit.new&ldep=<%=ldep.getIdentifier()%>"
								title="<%=u.getTexto("at.cp.env.btn.novo.titulo")%>"><%=u.getTexto("at.cp.env.btn.novo.ico")%></a>
							</span>
						<%
							}
						%>	
						<ul>
						    <%
						    	for (Environment env : lEnvs) {
						    				    				    							
    							countActionForEnvs = 1;
    							if (!u.hasPermissoes("environment.remove")) countActionForEnvs--;
						    %>
							<li  onmouseover="liHover(event, <%=countActionForEnvs%>);" onmouseout="liOut(event);"><a
								class="environment <%=env.getIdentifier().equals(identifier) ? "selectedEdit"
							: ""%>"
								href="environment.do?action=environment.edit&ldep=<%=ldep.getIdentifier()%>&identifier=<%=env.getIdentifier()%>"><%=env.getTitle()%></a>
								
								
		   						 <%
		   						 	if (u.hasPermissoes("environment.remove")) {
		   						 %>
									<span class="environment" style="float: right; padding-right: 5px;"><a
										class="controls"
										href="environment.do?action=environment.remove&identifier=<%=env.getIdentifier()%>&ldep=<%=ldep.getIdentifier()%>"
										title="<%=u.getTexto("at.cp.env.btn.excluir.titulo")%>"><%=u.getTexto("at.cp.env.btn.excluir.ico")%></a>
									</span>
								<%
									}
								%>
								
								
								
								
								
								
								
								</li>
							<%
								}
							%>
						</ul></li>
				</ul></li>
			<%
				int countActionForPlay = lPlays!=null && lPlays.size() == 0?1:0;
			%>
			<li onmouseover="liHover(event, <%=countActionForPlay%>);" onmouseout="liOut(event);">
			<a class="arvoreMenos" href="app.do?action=ldep.ftv"> </a>
			<a href="play.do?action=play.list&ldep=<%=ldep.getIdentifier()%>"><%=u.getTexto("at.mt.titulo")%></a>
			<%
				if (u.hasPermissoes("play.edit.new") && countActionForPlay > 0) {
			%>
						<span style="float: right; padding-right: 5px;">
						   <a class="controls"
							  href="play.do?action=play.edit.new&ldep=<%=ldep.getIdentifier()%>"
							  title="<%=u.getTexto("at.mt.play.btn.novo.titulo")%>"><%=u.getTexto("at.mt.play.btn.novo.ico")%></a>
						</span>
						<%
							} 
															 }
						%>
			
				<ul class="<%=u.hasPermissoes("play.switch.pos.play")?"movePlays ":""%><%=u.hasPermissoes("view.construction") ? "" : (ldep.flagTreeView ? "" : "ulFechado")%>">
					<%
						for (Play play : lPlays) {
																
																int countActionForPlay = 4;
																
																if (!u.hasPermissoes("act.edit.new")) countActionForPlay--;
																if (!u.hasPermissoes("act.new.for.mi")) countActionForPlay--;
																if (!u.hasPermissoes("play.remove")) countActionForPlay--;
																if (!u.hasPermissoes("play.edit.new")) countActionForPlay--;
					%>
					<li id="playId-<%=play.getId()%>-ldepId-<%=ldep.getId()%>"
						onmouseover="liHover(event, <%=countActionForPlay%>);" onmouseout="liOut(event);">
						   <a class="arvoreMenos" href="app.do?action=ldep.ftv"> </a>
						   
						   <a class="plays <%=play.getIdentifier().equals(identifier) ? "selectedEdit": ""%>"
							  href="play.do?action=play.edit&identifier=<%=play.getIdentifier()%>&ldep=<%=ldep.getIdentifier()%>"><%=play.getTitle()%></a>
						
						<%
													if (u.hasPermissoes("play.remove")) {
												%>
						<span style="float: right; padding-right: 5px;">
						   <a class="controls"
							  href="play.do?action=play.remove&identifier=<%=play.getIdentifier()%>&ldep=<%=ldep.getIdentifier()%>"
							 title="<%=u.getTexto("at.mt.play.btn.excluir.titulo")%>"><%=u.getTexto("at.mt.play.btn.excluir.ico")%></a></span>
						<%
							} if (u.hasPermissoes("play.edit.new")) {
						%>
						<span style="float: right; padding-right: 5px;">
						   <a class="controls"
							  href="play.do?action=play.edit.new&ldep=<%=ldep.getIdentifier()%>"
							  title="<%=u.getTexto("at.mt.play.btn.novo.titulo")%>"><%=u.getTexto("at.mt.play.btn.novo.ico")%></a></span>
						<%
							} if (u.hasPermissoes("act.new.for.mi")) {
						%>
						<span style="float: right; padding-right: 5px;"> <a class="controls"
							  href="<%=Urls.urlAppBase%>act.do?action=act.new.for.mi&play-ref=<%=play.getIdentifier()%>&ldep=<%=ldep.getIdentifier()%>"
						     title="<%=u.getTexto("at.mt.act.mi.btn.novo.titulo")%>"><%=u.getTexto("at.mt.act.mi.btn.novo.ico")%></a></span>	
						<%
								} if (u.hasPermissoes("act.edit.new")) {
							%>
							<span style="float: right; padding-right: 5px;"> <a class="controls"
								  href="<%=Urls.urlAppBase%>act.do?action=act.edit.new&play-ref=<%=play.getIdentifier()%>&ldep=<%=ldep.getIdentifier()%>"
							           title="<%=u.getTexto("at.mt.act.btn.novo.titulo")%>"><%=u.getTexto("at.mt.act.btn.novo.ico")%></a></span>
						<%
							}
						%>
						
						<ul class="<%=u.hasPermissoes("play.switch.pos.act")?"moveActs":""%>">

							<%
								for (Act act : play.getActList()) {

																															if (act.getLearningDesignRef() != null) {

																																	if (act
																																			.getLearningDesignRef().getRef() == null)
																																		continue;

																																	LdProject elProject = ImeWorkspace
																																			.getImeWorkspace(request)
																																			.getLdProjectByIdentifierOfLD(
																																					LearningDesignUtils
																																							.getIdentifier(act
																																									.getLearningDesignRef()
																																									.getRef()));

																																	if (elProject == null)
																																		continue;

																																	request.setAttribute("el", elProject);
																																	//request.setAttribute("el", LearningDesignUtils.getIdentifier(ldr.getRef()));
							%>
							<li
								id="actId-<%=act.getId()%>-playId-<%=play.getId()%>-ldepId-<%=ldep.getId()%>"
								class="acts"
								style="background-color: rgba(0, 0, 0, 0.1); margin-top: 0px; margin-right: 2px;">
								<%
									String linkRecursive = "/arvore/arvoreMIConstructions.jsp?act-ref="
																+ act.getIdentifier()
																+ "&ldep-ref="
																+ ldep.getIdentifier()
																+ "&play-ref-port="
																+ play.getIdentifier();
								%> <jsp:include page="<%=linkRecursive%>"></jsp:include>
							</li>
							<%
								
											} else {
												
												int countActRemove = 2;
												if (!u.hasPermissoes("act.remove")) countActRemove--;
												if (!u.hasPermissoes("rp.edit.new")) countActRemove--;
							%>
							<li id="actId-<%=act.getId()%>-playId-<%=play.getId()%>-ldepId-<%=ldep.getId()%>" 
							    onmouseover="liHover(event, <%=countActRemove %>);" onmouseout="liOut(event);">
									
								<a class="arvoreMenos rp" href="app.do?action=ldep.ftv"> </a>
								
								<a class="acts <%=act.getIdentifier().equals(identifier) ? "selectedEdit": ""%>" href="act.do?action=act.edit&identifier=<%=act.getIdentifier()%>&play-ref=<%=play.getIdentifier()%>&ldep=<%=ldep.getIdentifier()%>"><%=act.getTitle()%></a>
								
								
								<% if (u.hasPermissoes("act.remove")) { %>
								<span style="float: right; padding-right: 5px;">
								    <a class="controls"
									   href="act.do?action=act.remove&identifier=<%=act.getIdentifier()%>&play-ref=<%=play.getIdentifier()%>&ldep=<%=ldep.getIdentifier()%>"
									  title="<%=u.getTexto("at.mt.act.btn.excluir.titulo") %>"><%=u.getTexto("at.mt.act.btn.excluir.ico") %></a></span>
							    </span>
							    <%} %>
								<% if (u.hasPermissoes("rp.edit.new")) { %>
								<span style="float: right; padding-right: 5px;">
								    <a class="controls"
									   href="<%=Urls.urlAppBase%>rp.do?action=rp.edit.new&act-ref=<%=act.getIdentifier() %>&ldep=<%=ldep.getIdentifier()%>"
									  title="<%=u.getTexto("at.mt.rp.btn.novo.titulo") %>"><%=u.getTexto("at.mt.rp.btn.novo.ico") %></a></span>	
							    </span>
							    <%} %>
								
								<ul class="<%=u.hasPermissoes("act.switch.pos.rp")?"moveRPs":"" %>">

							<% for (RolePart rp : act.getRolePartList()) {

													Object ob = rp.getRoleRef() != null ? rp.getRoleRef().getRef():null;
							%>
							<li class="rp"
								id="rpId-<%=rp.getId()%>-actId-<%=act.getId()%>-playId-<%=play.getId()%>-ldepId-<%=ldep.getId()%>"
								onmouseover="liHover(event, <%=rp.getRoleRef() != null && rp.getRoleRef().getRef() != null ? 1:0 %>);" onmouseout="liOut(event);">

								<% if (ob != null && u.hasPermissoes("roles.edit")) { %>
								<span class="right" style="padding-right: 5px;"><a
									class="<%=ob.getClass().getSimpleName()
										.toLowerCase()%> <%=LearningDesignUtils.getIdentifier(ob)
										.equals(identifier) ? "selectedEdit"
										: "controls"%>"
									href="roles.do?action=roles.edit&ldep=<%=ldep.getIdentifier()%>&identifier=<%=LearningDesignUtils.getIdentifier(ob)%>"><%=ob.getClass().getMethod("getTitle")
										.invoke(ob)%></a></span>
								<% } %>
										 <a
								class="rp <%=LearningDesignUtils.getIdentifier(rp)
										.equals(identifier) ? "selectedEdit"
										: ""%>"
								href="rp.do?action=rp.edit&identifier=<%=rp.getIdentifier()%>&act-ref=<%=act.getIdentifier()%>&ldep=<%=ldep.getIdentifier()%>"><%=rp.getTitle()%></a>

								<ul>

									<%
										Object objRef = rp.getEnvironmentRef() != null
																	&& rp.getEnvironmentRef().getRef() != null ? rp
																	.getEnvironmentRef().getRef()
																	: rp.getLearningActivityRef() != null
																			&& rp.getLearningActivityRef()
																					.getRef() != null ? rp
																			.getLearningActivityRef()
																			.getRef()
																			: rp.getSupportActivityRef() != null
																					&& rp.getSupportActivityRef()
																							.getRef() != null ? rp
																					.getSupportActivityRef()
																					.getRef()
																					: rp.getActivityStructureRef() != null
																							&& rp.getActivityStructureRef()
																									.getRef() != null ? rp
																							.getActivityStructureRef()
																							.getRef()
																							: null;

															request.setAttribute("objRef", objRef);
															String iddRef = Suport.utfToIso(identifier);

															String link = "arvoreASEdition.jsp?ldep="
																	+ ldep.getIdentifier() + "&identifier="
																	+ iddRef;
									%>
									<jsp:include page="<%=link%>"></jsp:include>


								</ul>
							</li>
							<%
								}
							%>
						</ul></li>

							<%
								}
										}
							%>


						</ul></li>
					<%
						}
					%>

				</ul>
				
 <% if (u.hasPermissoes("view.construction")) { %>
				</li>

		</ul>
		<%} %>
		</li>

	<%
		}
		w.constructViewTree--;
	%>
</ul>