package br.edu.ifg.ime.controllers;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.imsglobal.jaxb.ld.Components;
import org.imsglobal.jaxb.ld.EnvironmentRef;
import org.imsglobal.jaxb.ld.Learner;
import org.imsglobal.jaxb.ld.LearningDesign;
import org.imsglobal.jaxb.ld.RoleRef;
import org.imsglobal.jaxb.ld.Roles;
import org.imsglobal.jaxb.ld.Staff;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.dao.DAOConnection;
import br.edu.ifg.ime.dto.Usuario;
import br.edu.ifg.ime.ld.LdProject;
import br.edu.ifg.ime.ld.interfaces.Role;
import br.edu.ifg.ime.suport.Suport;

public class RolesController {



	public static void requestRemoveRole(HttpServletRequest request) {


		Roles roles = getRoles(request);
		String identifier = Suport.r(request, "identifier");
		
		Learner learner = getLearnerByIdentifier(request, identifier);
		if (learner != null) {
			roles.getLearnerList().remove(learner);
			ImeWorkspace.getImeWorkspace(request).excluirOfWorkspace(learner);
		}

		Staff staff = getStaffByIdentifier(request, identifier);
		if (staff != null) {
			roles.getStaffList().remove(staff);

			ImeWorkspace.getImeWorkspace(request).excluirOfWorkspace(staff);
		}

		//LdEditorWorkspace.getLdPlayerWorkspace(request).notifyRemoveObject(identifier);

	}

	public static String requestUpDateRole(HttpServletRequest request) {

		String titulo = Suport.r(request, "titulo");
		String tipo = Suport.r(request, "tipo");
		String identifier = Suport.r(request, "identifier");
		String createNew = Suport.r(request, "create-new");
		String matchPersons = Suport.r(request, "match-persons");
		String minPersons = Suport.r(request, "min-persons");
		String maxPersons = Suport.r(request, "max-persons");


		if (identifier != null && identifier.length() == 0) {

			Roles roles = getRoles(request);

			if (tipo.equals("learner")) {

				Learner learner = new Learner();
				learner.parent = roles;

				learner.setTitle(titulo);
				learner.setIdentifier(ImeWorkspace.getImeWorkspace(request).newIdentifier(learner, tipo));
				learner.setMatchPersons(matchPersons);
				learner.setCreateNew(createNew);				
				if (minPersons != null && minPersons.length() > 0) learner.setMinPersons(new BigInteger(minPersons));
				if (maxPersons != null && maxPersons.length() > 0) learner.setMaxPersons(new BigInteger(maxPersons));

				identifier = learner.getIdentifier();

				roles.getLearnerList().add(learner);
			}
			else if (tipo.equals("staff")) {
				Staff staff = new Staff();
				staff.parent = roles;

				staff.setTitle(titulo);
				staff.setIdentifier(ImeWorkspace.getImeWorkspace(request).newIdentifier(staff, tipo));
				staff.setMatchPersons(matchPersons);
				staff.setCreateNew(createNew);				
				if (minPersons != null && minPersons.length() > 0) staff.setMinPersons(new BigInteger(minPersons));
				if (maxPersons != null && maxPersons.length() > 0) staff.setMaxPersons(new BigInteger(maxPersons));

				identifier = staff.getIdentifier();

				roles.getStaffList().add(staff);

			}

		}
		else {

			if (tipo.equals("learner")) {

				Learner learner = getLearnerByIdentifier(request, identifier);
				if (learner != null) {
					learner.setTitle(titulo);
					learner.setMatchPersons(matchPersons);
					learner.setCreateNew(createNew);
					if (minPersons != null && minPersons.length() > 0) learner.setMinPersons(new BigInteger(minPersons));
					if (maxPersons != null && maxPersons.length() > 0) learner.setMaxPersons(new BigInteger(maxPersons));
				}
			}
			else if (tipo.equals("staff")) {

				Staff staff = getStaffByIdentifier(request, identifier);
				if (staff != null) {
					staff.setTitle(titulo);
					staff.setMatchPersons(matchPersons);
					staff.setCreateNew(createNew);				
					if (minPersons != null && minPersons.length() > 0) staff.setMinPersons(new BigInteger(minPersons));
					if (maxPersons != null && maxPersons.length() > 0) staff.setMaxPersons(new BigInteger(maxPersons));					
				}

			}
		}
		return identifier;
	}

	public static Learner getLearnerByIdentifier(HttpServletRequest request, String identifier) {

		LdProject ldep = ImeWorkspace.getImeWorkspace(request).getLdProject(request);

		while (ldep != null) {

			List<Learner> lLearners = getRoles(ldep).getLearnerList();	
			for (Learner learner : lLearners) {		
				if (learner.getIdentifier().equals(identifier))
					return learner;
			}	
				ldep = (LdProject)ldep.parent;
		
		}
		return null;
	}

	public static Staff getStaffByIdentifier(HttpServletRequest request, String identifier) {

		LdProject ldep = ImeWorkspace.getImeWorkspace(request).getLdProject(request);

		while (ldep != null) {
			

			List<Staff> lStaffs = getRoles(ldep).getStaffList();				
			for (Staff staff : lStaffs) {		
				if (staff.getIdentifier().equals(identifier))
					return staff;
			}			

				ldep = (LdProject)ldep.parent;
			
		}
		return null;

	}
	public static List<ArrayList<Learner>> getLearners(HttpServletRequest request) {

		LdProject ldep = ImeWorkspace.getImeWorkspace(request).getLdProject(request);
		return getLearners(ldep);				

	}

	public static List<ArrayList<Learner>> getLearners(LdProject ldep) {
		List<ArrayList<Learner>> result = new ArrayList<ArrayList<Learner>>();

		while (ldep != null) {

				Roles roles = getRoles(ldep);
				ArrayList<Learner> rLearners = (ArrayList<Learner>) getRoles(ldep).getLearnerList();
				for (Learner l: rLearners)
					l.parent = roles;				
				
				result.add(rLearners);
				
			if (ldep.getLd().getInheritRoles() != null && ldep.getLd().getInheritRoles())
				ldep = (LdProject)ldep.parent;
			else
				break;
		}		
		return result;
	}


	public static List<ArrayList<Learner>> getAllLearners(LdProject ldep) {
		List<ArrayList<Learner>> result = new ArrayList<ArrayList<Learner>>();

		while (ldep != null) {

			Roles roles = getRoles(ldep);
			ArrayList<Learner> rLearners = (ArrayList<Learner>) getRoles(ldep).getLearnerList();
			for (Learner l: rLearners)
				l.parent = roles;				
			
			result.add(rLearners);
			
			ldep = (LdProject)ldep.parent;
		}		
		return result;
	}


	public static List<ArrayList<Staff>> getStaffs(HttpServletRequest request) {


		LdProject ldep = ImeWorkspace.getImeWorkspace(request).getLdProject(request);
		return getStaffs(ldep);			

	}

	public static List<ArrayList<Staff>> getStaffs(LdProject ldep) {
		List<ArrayList<Staff>> result = new ArrayList<ArrayList<Staff>>();

		while (ldep != null) {

			Roles roles = getRoles(ldep);
			ArrayList<Staff> rStaffs = (ArrayList<Staff>) getRoles(ldep).getStaffList();
			for (Staff s: rStaffs)
				s.parent = roles;				
			
			result.add(rStaffs);
			 
			if (ldep.getLd().getInheritRoles() != null && ldep.getLd().getInheritRoles())
				ldep = (LdProject)ldep.parent;
			else
				break;
		}
		return result;
	}
	public static List<ArrayList<Staff>> getAllStaffs(LdProject ldep) {
		List<ArrayList<Staff>> result = new ArrayList<ArrayList<Staff>>();

		while (ldep != null) {

			Roles roles = getRoles(ldep);
			ArrayList<Staff> rStaffs = (ArrayList<Staff>) getRoles(ldep).getStaffList();
			for (Staff s: rStaffs)
				s.parent = roles;				
			
			result.add(rStaffs);
			
			ldep = (LdProject)ldep.parent;

		}
		return result;
	}

	private static Roles getRoles(HttpServletRequest request) {

		Roles roles = getRoles(ImeWorkspace.getImeWorkspace(request).getLdProject(request));
		return roles;	

	}


	public synchronized static Roles getRoles(LdProject ldep) {

		Components componentes = ComponentsController.getComponents(ldep);

		if (componentes.getRoles() == null) {
			componentes.setRoles(new Roles());
		}
		componentes.getRoles().parent = componentes;
		
		return componentes.getRoles();		
	}

	public synchronized static List<RoleRef> getListRoleRef(Object ob) {

		Field[] lFields = ob.getClass().getDeclaredFields();

		for (Field f : lFields) {

			//System.out.format("Type: %s%n", f.getType());
			//System.out.println(f.getGenericType().toString());

			if (f.getGenericType().toString().equals("java.util.List<org.imsglobal.jaxb.ld.RoleRef>")) {

				try {
					return 	(List<RoleRef>) ob.getClass().getMethod( Suport.mGetOfField(f) ).invoke(ob);
				} catch (Exception e) {
					return null;
				}

			}
		}
		return null;
	}
	


	public static void vincularRoles(HttpServletRequest request) {

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);

		String strRole = Suport.r(request, "role-agregante");		
		String strRolesAgr[] = request.getParameterValues("role-agregada");
		String incluirItens =  Suport.r(request, "incluir-itens");		

		if (strRolesAgr == null || strRolesAgr.length == 0)
			return;
		
		Object roleNew = strRole==null? null :w.getObject(strRole);
		Object rolesOld[] = new Object[strRolesAgr.length]; 	    
		for (int i = 0; i < strRolesAgr.length; i++) {
			rolesOld[i] = w.getObject(strRolesAgr[i]);

			if (incluirItens != null) {

				Role rNew = (Role) roleNew;
				Role rOld = (Role) rolesOld[i];

				if (rOld.getInformation() == null)
					continue;
				if (rNew.getInformation() == null) {
					rNew.setInformation(rOld.getInformation());
					continue;
				}
				rNew.getInformation().getItemList().addAll(rOld.getInformation().getItemList());
			}
		}

		String strLdepAgr = Suport.r(request, "ldepAgr");
		String strLdep = Suport.r(request, "ldep");
		LdProject ldep = w.getLdProjectByIdentifier(strLdep);
		LdProject ldepAgr = w.getLdProjectByIdentifier(strLdepAgr);

		if (roleNew == null) {

			for (Object obRole: rolesOld) {

				getRoles(ldepAgr).getLearnerList().remove(obRole);
				getRoles(ldepAgr).getStaffList().remove(obRole);

				if (obRole instanceof Learner)
					getRoles(ldep).getLearnerList().add((Learner) obRole);
				else if (obRole instanceof Staff)
					getRoles(ldep).getStaffList().add((Staff) obRole);
			}

		}
		else {
			Class[] attrComObjsDasClasses = {Learner.class, Staff.class};
			List<Object> lObjs = Suport.listaDeObjetos(ldepAgr, null, attrComObjsDasClasses);
			for (Object obItem : lObjs) {

				if (obItem instanceof RoleRef) {

					RoleRef roleRef = (RoleRef) obItem;

					for (Object obRole: rolesOld) {

						if (roleRef.getRef() != obRole)
							continue;

						getRoles(ldepAgr).getLearnerList().remove(obRole);
						getRoles(ldepAgr).getStaffList().remove(obRole);

						roleRef.setRef(roleNew);
						break;
					}
				} else {
					Field[] lFields = obItem.getClass().getDeclaredFields();
					for (Field f : lFields) {
						try {

							Object obAttr;
							obAttr = obItem.getClass().getMethod( Suport.mGetOfField(f) ).invoke(obItem);

							if (obAttr instanceof Learner || obAttr instanceof Staff) {

								for (Object obRole: rolesOld) {
									if (obAttr != obRole)
										continue;

									getRoles(ldepAgr).getLearnerList().remove(obRole);
									getRoles(ldepAgr).getStaffList().remove(obRole);

									Suport.set(obItem, f, roleNew);
									break;
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
					}
				}
			}

			for (Object obRole: rolesOld) {
				w.excluirOfWorkspace((Serializable) obRole);
			}
		}

	}

}
