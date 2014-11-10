<%@page import="org.imsglobal.jaxb.ld.WhenPlayCompleted"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="org.imsglobal.jaxb.ld.CompleteUnitOfLearning"%>
<%@page import="org.imsglobal.jaxb.ld.WhenRolePartCompleted"%>
<%@page import="br.edu.ifg.ime.controllers.UsuarioController"%>
<%@page import="br.edu.ifg.ime.dto.Usuario"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
<%@page import="org.imsglobal.jaxb.ld.RolePart"%>
<%@page import="br.edu.ifg.ime.controllers.ActsController"%>
<%@page import="org.imsglobal.jaxb.ld.Act"%>
<%@page import="br.edu.ifg.ime.controllers.PlaysController"%>
<%@page import="br.edu.ifg.ime.controllers.MethodController"%>
<%@page import="org.imsglobal.jaxb.ld.Method"%>
<%@page import="org.imsglobal.jaxb.ld.Play"%>
<%@page import="br.edu.ifg.ime.controllers.EnvironmentsController"%>
<%@page import="org.imsglobal.jaxb.ld.Environment"%>
<%@page import="br.edu.ifg.ime.suport.Urls"%>
<%@page import="org.imsglobal.jaxb.ld.ActivityStructure"%>
<%@page import="org.imsglobal.jaxb.ld.SupportActivity"%>
<%@page import="org.imsglobal.jaxb.ld.LearningActivity"%>
<%@page import="br.edu.ifg.ime.suport.LearningDesignUtils"%>
<%@page import="java.io.Serializable"%>
<%@page import="br.edu.ifg.ime.controllers.ActivityController"%>
<%@page import="org.imsglobal.jaxb.ld.Staff"%>
<%@page import="br.edu.ifg.ime.controllers.RolesController"%>
<%@page import="br.edu.ifg.ime.controllers.ProjetoController"%>
<%@page import="org.imsglobal.jaxb.ld.LearningDesign"%>
<%@page import="org.imsglobal.jaxb.ld.Learner"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	Usuario userConnect = UsuarioController
	.getUsuarioConectado(session);

	String idRef = Suport.r(request, "ldep");

	ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);


	LdProject ldep = w.getLdProjectByIdentifier(idRef);
	Method m = MethodController.getMethod(ldep);


	List<Play> lPlays = m.getPlayList();

	List<WhenPlayCompleted> listPlaysCompleteUol = null;

	if (m.getCompleteUnitOfLearning() != null) {

		listPlaysCompleteUol = m.getCompleteUnitOfLearning()
		.getWhenPlayCompletedList();

	}
%>
<%
	for (Play p : lPlays) {
		boolean checked = false;
		if (listPlaysCompleteUol != null)
			for (WhenPlayCompleted wrp : listPlaysCompleteUol)
				if (wrp.getRef() != null
						&& ((Play) wrp.getRef()).getIdentifier()
								.equals(p.getIdentifier())) {
					checked = true;
					break;
				}
%>
<label class="list"> <input type="checkbox" name="play-ref"
	value="<%=p.getIdentifier()%>" <%=checked ? "checked" : ""%>><%=p.getTitle()%>
</label>
<%
	}
%>
