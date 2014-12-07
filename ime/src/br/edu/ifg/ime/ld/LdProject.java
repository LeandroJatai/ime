package br.edu.ifg.ime.ld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.imsglobal.jaxb.content.File;
import org.imsglobal.jaxb.content.Resource;
import org.imsglobal.jaxb.content.Resources;
import org.imsglobal.jaxb.ld.Item;
import org.imsglobal.jaxb.ld.LearningDesign;

import br.edu.ifg.ime.ImeProject;
import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.controllers.ComponentsController;
import br.edu.ifg.ime.controllers.MethodController;
import br.edu.ifg.ime.dto.Projeto;
import br.edu.ifg.ime.suport.Suport;

public class LdProject extends ImeObject implements Serializable, ImeProject {

	private static final long serialVersionUID = 12345L;

	private String identifier; 

	//@XmlElement(name="learning-design")
	private LearningDesign ld = new LearningDesign();

	//  #@XmlElement(name="ld-editor-project")
	public List<LdProject> ldAgregados = new ArrayList<LdProject>();

	@XmlTransient
	private boolean agregado = false; 

	@XmlTransient
	public boolean flagTreeView = false; 

	@XmlTransient
	public ImeWorkspace workspace = null;


	private List<String> undo = new ArrayList<String>();
	private List<String> redo = new ArrayList<String>();

	private Resources resources;

	private Projeto persistencia = null;
	public String skin = null;
	public boolean publico = false;

	public boolean flagAlterado = true;

	public LdProject() {
		ld.parent = this;
	}

	public LdProject(ImeWorkspace works) {

		workspace = works;
		identifier = workspace.newIdentifier(ld, "ldep");

		ld.setTitle("MI - Sem Título "+(workspace.countNewProject++));
		ld.setLevel("A");
		ld.setIdentifier(workspace.newIdentifier(ld, "ld"));
		ld.parent = this;

		ComponentsController.getComponents(this);
		MethodController.getMethod(this);
		//ld.setInheritRoles(false);

		agregarLd(this);
	}

	public LdProject(ImeWorkspace works, String titulo) {

		workspace = works;
		identifier = workspace.newIdentifier(ld, "ldep");

		ld.setTitle(titulo);
		ld.setLevel("A");
		ld.setIdentifier(workspace.newIdentifier(ld, "ld"));	
		//ld.setInheritRoles(true);

		agregarLd(this);
	}
	public LearningDesign getLd() {	
		ld.parent = this;
		return ld;
	}

	public void setLd(LearningDesign ld) {
		this.ld = ld;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public boolean isAgregado() {
		return agregado;
	}

	public void agregadoON(LdProject _parent) {
		agregado = true;
		parent = _parent;
	}

	public void agregadoOFF() {
		agregado = false;
		parent = null;
	}

	public List<LdProject> getLdAgregados() {
		if (ldAgregados == null)
			ldAgregados = new  ArrayList<LdProject>();
		return ldAgregados;
	}

	public void setLdRefs(List<LearningDesign> lLds) {
		for (LearningDesign auxLds : lLds) {
			//System.out.println(auxLds.getTitle());
		}
	}
	public void agregarLd(LdProject ldep) {

		if (ldep == this)
			return;

		ldep.agregadoON(this);

		ldAgregados.add(ldep);

	}

	public LdProject removerLdAgregado(String ldep) {

		for (LdProject item : ldAgregados) {

			if (item.getIdentifier().equals(ldep)) {
				if (ldAgregados.remove(item)) {
					item.agregadoOFF();
					return item;
				}					
			}			
		}
		return null;		

		//getListLds();
	}

	public LdProject removerLdAgregadoByIdLD(String idLD) {

		for (LdProject item : ldAgregados) {

			if (item.getLd().getIdentifier().equals(idLD)) {
				if (ldAgregados.remove(item)) {
					item.agregadoOFF();
					return item;
				}					
			}			
		}

		return null;		

		//getListLds();
	}

	public void requestUpDateLdProject(HttpServletRequest request) {

		String reqSkin = Suport.r(request, "ldepSkin");

		if (reqSkin != null)
			skin = reqSkin;
		else if (reqSkin == null || reqSkin.length() == 0)
			skin = null;

		String title = Suport.r(request, "title");
		String level = Suport.r(request, "level");
		Boolean sequenceUsed =  Boolean.valueOf(Suport.r(request, "sequenceUsed"));
		String uri = Suport.r(request, "uri");
		String version = Suport.r(request,"version");
		String inheritRoles = Suport.r(request,"inherit-roles");

		ld.setTitle(title);
		ld.setLevel(level);
		ld.setSequenceUsed(sequenceUsed);
		ld.setUri(uri);
		ld.setVersion(version);
		ld.setInheritRoles(inheritRoles != null? true: null);

		//	ld.validateImsLd();
	}

	public Collection<Item> getAllItemsWithOutItemsSubProjects() {

		ArrayList<Item> items = new ArrayList<Item>();
		Class [] stopClass = {LearningDesign.class};
		Collection<Object> objs = Suport.listaDeObjetos(ld, stopClass);

		for (Iterator it = objs.iterator(); it.hasNext();) {
			Object ob = (Object) it.next();

			if (ob instanceof Item) {
				Item item = (Item) ob;

				items.add(item);
			}

		}


		return items;
	}

	public Collection<Item> getAllItemsWithItemsSubProjects() {

		ArrayList<Item> items = new ArrayList<Item>();
		Collection<Object> objs = Suport.listaDeObjetos(ld, null);

		for (Iterator it = objs.iterator(); it.hasNext();) {
			Object ob = (Object) it.next();

			if (ob instanceof Item) {
				Item item = (Item) ob;

				items.add(item);



			}

		}


		return items;
	}

	public Resources getResources() {


		resources = new Resources();

		Collection<Item> items = getAllItemsWithOutItemsSubProjects();

		for (Item item : items) {

			if (item.getIdentifierref() == null) {
				Resource r = new Resource();
				r.setIdentifier(workspace.newIdentifier(r, "resource"));
				r.setType("webcontent");
				item.setIdentifierref(r);

				if (item.getType().equals("file")) {
					File f = new File();
					f.setHref(item.getNameFile());
					r.getFileList().add(f);
					r.setHref(item.getNameFile());
				} 
				else if (item.getType().equals("text")) {
					File f = new File();
					f.setHref(item.getIdentifier()+".html");
					r.getFileList().clear();
					r.getFileList().add(f);
					r.setHref(item.getIdentifier()+".html");
				}
				else if (item.getType().equals("link") ){
					r.setHref(item.getLink());

				}
			}
			if (item != item.getIdentifierref() && item.getIdentifierref() instanceof Resource) {
				/*if (item.getType().equals("text") && ((Resource) item.getIdentifierref()).getFileList().size() > 0) {
					String name = item.getIdentifier()+".html";
					((Resource) item.getIdentifierref()).getFileList().get(0).setHref(name);
					((Resource) item.getIdentifierref()).setHref(name);
				}*/

				//Resource r = (Resource) item.getIdentifierref();
				//r.setIdentifier(workspace.newIdentifier(r, "resource")); 

				resources.getResourceList().add((Resource) item.getIdentifierref());
			}
		}
		return resources;
	}

	public void setResources(Resources resources) {
		//this.resources = resources;
	}

	public void setFileItem(String nameFile, byte[] data) {

		Collection<Item> items = getAllItemsWithItemsSubProjects();

		for (Item item : items) {

			if (item.getIdentifierref() == null)
				continue;

			Resource r = (Resource) item.getIdentifierref();

			//	r.setIdentifier(workspace.newIdentifier(r, "resource"));  

			if (r.getFileList() == null || r.getFileList().size() == 0) {


				if (r.getHref() != null && r.getHref().equals(nameFile)) {
					r.setHref(r.getHref().replaceAll(" (", "-"));
					r.setHref(r.getHref().replaceAll("(", "-"));
					r.setHref(r.getHref().replaceAll(")", ""));

					boolean isTexto = Suport.isByteArrayIsFileText(data);


					if (!isTexto) {
						item.setFile(data);
						item.setNameFile(r.getHref());
					}
					else {
						item.setText(new String(data));
					}

				}				
				continue;
			}


			if (r.getFileList().get(0).getHref() != null && r.getFileList().get(0).getHref().equals(nameFile)) {

				r.getFileList().get(0).setHref(r.getFileList().get(0).getHref().replaceAll(" \\(", "-"));
				r.getFileList().get(0).setHref(r.getFileList().get(0).getHref().replaceAll("\\(", "-"));
				r.getFileList().get(0).setHref(r.getFileList().get(0).getHref().replaceAll("\\)", ""));
				r.setHref(r.getFileList().get(0).getHref());


				boolean isTexto = Suport.isByteArrayIsFileText(data);


				if (!isTexto) { 	
					item.setFile(data);
					item.setNameFile(r.getHref());
				}
				else {
					item.setText(new String(data));
				}

			}	

		}
	}




	public Projeto getPersistencia() {
		return persistencia;
	}

	public void setPersistencia(Projeto persistencia) {
		this.persistencia = persistencia;
	}

	public int sizeUndo() {

		LdProject ldep = this;

		while (ldep.parent != null)
			ldep = (LdProject)ldep.parent;

		return ldep.undo.size(); 
	}

	public void undo() {

		LdProject ldep = this;

		while (ldep.parent != null)
			ldep = (LdProject)ldep.parent;

		if (ldep.undo.size() == 0)
			return;


		ldep.redo.add(  workspace.getCodeIMSCPOfLdepProject(ldep.getIdentifier())  );

		String codigo = ldep.undo.remove(ldep.undo.size()-1);

		workspace.updateByCodeIMSCP(ldep.getIdentifier(), codigo);

		LdProject newLdep = workspace.getLdProjectByIdentifier(ldep.getIdentifier());
		newLdep.undo = ldep.undo;
		newLdep.redo = ldep.redo;

	}

	public int sizeRedo() {

		LdProject ldep = this;

		while (ldep.parent != null)
			ldep = (LdProject)ldep.parent;

		return ldep.redo.size(); 
	}

	public void redo() {

		LdProject ldep = this;

		while (ldep.parent != null)
			ldep = (LdProject)ldep.parent;

		if (ldep.redo.size() == 0)
			return;


		ldep.undo.add(  workspace.getCodeIMSCPOfLdepProject(ldep.getIdentifier())  );

		String codigo = ldep.redo.remove(ldep.redo.size()-1);

		workspace.updateByCodeIMSCP(ldep.getIdentifier(), codigo);

		LdProject newLdep = workspace.getLdProjectByIdentifier(ldep.getIdentifier());
		newLdep.undo = ldep.undo;
		newLdep.redo = ldep.redo;

	}

	public void oldState() {


		LdProject ldep = this;

		while (ldep.parent != null)
			ldep = (LdProject)ldep.parent;
		ldep.flagAlterado = true;

		ldep.undo.add(  workspace.getCodeIMSCPOfLdepProject(ldep.getIdentifier())  );

		if (ldep.undo.size() > 5)
			ldep.undo.remove(0);


		ldep.redo.clear();
	}

	public String toString() {
		if (ld == null)
			return null;

		return ld.getTitle();
	}

	@Override
	public void validateImsLd() {

		clearStructureOfValidationMessages();

		if (ld != null) {
			ld.validateImsLd();
			putERRORs(ld.getERRORs());
			putWARNINGs(ld.getWARNINGs());
		}
		if (ldAgregados != null)
			for (LdProject ldep: ldAgregados) {
				ldep.validateImsLd();
				for (int i = 0; i < ldep.getERRORs().size(); i++) {
					ldep.getERRORs().set(i, ldep.getLd().getTitle()+": "+ldep.getERRORs().get(i));
				}
				for (int i = 0; i < ldep.getWARNINGs().size(); i++) {
					ldep.getWARNINGs().set(i, ldep.getLd().getTitle()+": "+ldep.getWARNINGs().get(i));
				}
					
				putERRORs(ldep.getERRORs());
				putWARNINGs(ldep.getWARNINGs());
			}

	}

}
