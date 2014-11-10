<%@page import="br.edu.ifg.ime.controllers.Lc"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.edu.ifg.ime.suport.Suport"%>
<%@page import="br.edu.ifg.ime.ld.LdProject"%>
<%@page import="br.edu.ifg.ime.ImeWorkspace"%>
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
<%@page
	import="br.edu.ifg.ime.controllers.ProjetoController"%>
<%@page import="org.imsglobal.jaxb.ld.LearningDesign"%>
<%@page import="org.imsglobal.jaxb.ld.Learner"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>



<%
	String identifier = Suport.r(request, "identifier");
String idRef = Suport.r(request, "id-ref");

if (idRef != null) {
	identifier = idRef;
}

	ActivityStructure asEdit = (ActivityStructure) ImeWorkspace.getImeWorkspace(request).getObject(identifier);
	
	if (asEdit == null)
		return;

	List<Serializable> eVinc = asEdit.getLearningActivityRefOrSupportActivityRefOrUnitOfLearningHref();
	

	List<Serializable> lActivities = ActivityController.getActivities(
	request)
	.getLearningActivityOrSupportActivityOrActivityStructure();

	List<Serializable> neVinc = new ArrayList<Serializable>();

	Boolean checked = false;
	//checked = LdPlayerWorkspace.getLdPlayerWorkspace(request).existeVinculo(env.getIdentifier(), identifier);
%>
<fieldset id="activitiesList">
	<legend><%=Lc.getTexto(session, "at.cp.as.list.sub.atcv") %></legend>

		<%
				String classType = "";
			for (Serializable activity : eVinc) {
				
				if (LearningDesignUtils.isLearningActivity(LearningDesignUtils.getRef(activity)))
					classType = "learning-activity";
				else if (LearningDesignUtils.isSupportActivity(LearningDesignUtils.getRef(activity)))
					classType = "support-activity";
				else if (LearningDesignUtils.isActivityStructure(LearningDesignUtils.getRef(activity)))
					classType = "activity-structure";
					
					
 
		%>

		<label class="list">
		 <span class="left" style="padding-right: 5px;"> 
		    <a style="padding-left: 20px;" class="<%=classType %> controls" title="<%=Lc.getTexto(session, "at.msg.move.for.seq")%>">&#8661;</a>
		 </span>
		
			
		<input type="checkbox" name="activity-ref" value="<%=LearningDesignUtils.getIdentifier(LearningDesignUtils.getRef(activity))%>" checked /><%=LearningDesignUtils.getRef(activity).getClass().getMethod("getTitle").invoke(LearningDesignUtils.getRef(activity))%></label>

		<%
			
			}
		%>
		<%
			for (Serializable activity : lActivities) {

				checked = false;
				

				for (Serializable activitySelect : eVinc) {

					if (LearningDesignUtils.getRef(activitySelect) == activity) {
						checked = true;
						break;
					}						
				}
				if (checked)
					continue;
				

				if (LearningDesignUtils.isLearningActivity(activity))
					classType = "learning-activity";
				else if (LearningDesignUtils.isSupportActivity(activity))
					classType = "support-activity";
				else if (LearningDesignUtils.isActivityStructure(activity))
					classType = "activity-structure";
					

		%>

		<label class="list">
		<span class="left" style="padding-right: 5px;"> 
			    <a style="padding-left: 20px;" class="<%=classType %> controls" title="<%=Lc.getTexto(session, "at.msg.move.for.seq")%>">&#8661;</a>
		 </span>
		
		<input type="checkbox" name="activity-ref" value="<%=LearningDesignUtils.getIdentifier(activity)%>" /><%=activity.getClass().getMethod("getTitle").invoke(activity)%></label>
		<%
			}
		%>
	</fieldset>