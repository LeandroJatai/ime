package br.edu.ifg.ime;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.imsglobal.jaxb.content.Manifest;
import org.imsglobal.jaxb.content.Organizations;
import org.imsglobal.jaxb.content.Resource;
import org.imsglobal.jaxb.content.Resources;
import org.imsglobal.jaxb.ld.Act;
import org.imsglobal.jaxb.ld.Item;
import org.imsglobal.jaxb.ld.Learner;
import org.imsglobal.jaxb.ld.LearningDesign;
import org.imsglobal.jaxb.ld.Method;
import org.imsglobal.jaxb.ld.ObjectFactory;
import org.imsglobal.jaxb.ld.Play;
import org.imsglobal.jaxb.ld.Properties;
import org.imsglobal.jaxb.ld.RoleRef;
import org.imsglobal.jaxb.ld.Roles;
import org.imsglobal.jaxb.ld.Staff;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import br.edu.ifg.ime.controllers.ActivityController;
import br.edu.ifg.ime.controllers.ArquivoController;
import br.edu.ifg.ime.controllers.EnvironmentsController;
import br.edu.ifg.ime.controllers.LinguagemController;
import br.edu.ifg.ime.controllers.MethodController;
import br.edu.ifg.ime.controllers.PlaysController;
import br.edu.ifg.ime.controllers.RolesController;
import br.edu.ifg.ime.dao.DAOConnection;
import br.edu.ifg.ime.ld.LdProject;
import br.edu.ifg.ime.ld.interfaces.Role;
import br.edu.ifg.ime.suport.LearningDesignUtils;
import br.edu.ifg.ime.suport.Suport;
import br.edu.ifg.ime.suport.Urls;
import br.edu.ifg.ime.suport.xsd.SimpleResolver;
import br.edu.ifg.ime.test.ImeTest;

import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;

public class ImeWorkspace implements Serializable {

	public ImeTest t;


	private static org.imsglobal.jaxb.content.ObjectFactory imscpFactory = new org.imsglobal.jaxb.content.ObjectFactory();
	private static ObjectFactory imsldFactory = new ObjectFactory();

	private static final long serialVersionUID = 12345L;

	//public static final int MODE_CONSTRUCTION = 1;
	//public static final int MODE_EDITOR = 2;

	//private int stateMode = MODE_CONSTRUCTION;
	
	
	

	private List<LdProject> repositorio = new ArrayList<LdProject>();

	
	
	
	
	
	private TreeMap<String, Object> objsWkspc = new TreeMap<String, Object>();
	
	
	
	
	

	//Dado Elm idenficado por String guarde todos os objetos que usem Elm
	TreeMap<String, TreeMap<String, Object>> referencias = new TreeMap<String, TreeMap<String, Object>>();
	
	
	
	
	
	
	

	private TreeMap<String, Long> lIds = new TreeMap<String, Long>();//new GregorianCalendar().getTimeInMillis();

	
	
	
	
	
	
	
	
	private TreeMap<String, Object> tmpListImport = new TreeMap<String, Object>();


	public int countNewProject = 0;

	public int countNivel = 0;

	public int constructViewTree = 0;

	private static ImeWorkspace workspace = null;

	//LdEditorProject master = null;

	public ImeWorkspace() {

		//master =;		
		//repositorio.add( new LdEditorProject(this));

	}
	/*
	public void modEditor() {

		stateMode = MODE_EDITOR;
	}

	public void modConstruction() {

		stateMode = MODE_CONSTRUCTION;
	}

	public boolean isModeEditor() {

		return stateMode == MODE_EDITOR;
	}

	public boolean isModeConstruction() {

		return stateMode == MODE_CONSTRUCTION;
	}*/


	public synchronized String newIdentifier(Object ob, String prefixo) {

		if (!lIds.containsKey(prefixo)) {
			lIds.put(prefixo, new Long(0));
		}
		String id ="";


		if (ob != null) {
			do {
				lIds.put(prefixo, new Long(lIds.get(prefixo)+1));
				id = prefixo+"-"+(lIds.get(prefixo).longValue());
			}
			while (objsWkspc.containsKey(id));
			objsWkspc.put(id, ob);	
		}
		else {
			lIds.put(prefixo, new Long(lIds.get(prefixo)+1));
			id = prefixo+"-"+(lIds.get(prefixo).longValue());
		}

		return id;


		//String id = prefixo+"-"+Long.toHexString(identifier++);

	}

	public Object getObject(String identifier) {

		return objsWkspc.get(identifier);

	}	

	public List<LdProject>  getListLdProject() {
		return repositorio;
	}

	public List<LdProject>  getMasterListLdProject() {

		ArrayList<LdProject> result = new ArrayList<LdProject>();

		for (LdProject ld : repositorio) {

			if (ld.isAgregado())
				continue;

			result.add(ld);

		}
		repositorio.removeAll(result);
		repositorio.addAll(0, result);

		return result;
	}

	public synchronized static ImeWorkspace resetLdPlayerWorkspace(HttpServletRequest request) {

		request.getSession().removeAttribute("workspace");
		workspace = null;
		return getImeWorkspace(request);
	}


	public void referenciar(String key, Object agregante) {

		// Elemento de Identifier <key> é agregado por referencia ao objeto <agregante>

		/*
		 * Quer dizer que:
		 * 
		 * 
		 * agregante tem:
		 * 
		 * 		attr: ElementoRef
		 * 		    	e este tem um attr: Elemento que tem a <key> identifier
		 * 
		 * */


		TreeMap<String, Object> lAgregantes = null;

		if (!referencias.containsKey(key)) {
			lAgregantes = new TreeMap<String, Object> ();
			referencias.put(key, lAgregantes);
		}
		else {
			lAgregantes = referencias.get(key);
		}

		// Dado Elemento <key> constroi-se uma lista com todos objetos que o agregam
		lAgregantes.put(LearningDesignUtils.getIdentifier(agregante), agregante);
	}

	public void desvincularReferencia(String key, String agregante) {

		TreeMap<String, Object> lAgregantes = referencias.get(key);

		if (lAgregantes == null)
			return;

		lAgregantes.remove(agregante);
	}

	@SuppressWarnings("unchecked")
	public void excluirOfWorkspace(Serializable ob) {

		String key = LearningDesignUtils.getIdentifier(ob);

		TreeMap<String, Object> lAgregantes = referencias.get(key);
		objsWkspc.remove(key);

		if (lAgregantes == null) {
			return;
		} 
		referencias.remove(key);		
		Collection<Object> lAg = lAgregantes.values(); // lista de objetos que referenciam OB

		for (Object item : lAg) {

			recursiveRemove(ob, item);// remover ob de item e de qualquer outro objeto constante em item


		}
	}

	private void recursiveRemove(Serializable obRemove, Object recursiveItem) {

		Field[] lFields = recursiveItem.getClass().getDeclaredFields(); // lista de atributos do objeto item
		//Method[] lMethods = item.getClass().getDeclaredMethods(); // lista de atributos do objeto item

		/*if (recursiveItem instanceof Conference)
			System.out.format("Type: %s%n", recursiveItem.getClass().getName());

		 */
		for (Field f : lFields) {

			//System.out.format("Type: %s%n", f.getType());
			//System.out.format("GenericType: %s%n", f.getGenericType());
			if (f.getGenericType().toString().equals("java.util.List<java.lang.String>")) { 

			}
			else if (f.getType().getCanonicalName().equals("java.util.List")) {
				String s = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
				try {
					List<Serializable> lObjs  = (List<Serializable>) recursiveItem.getClass().getMethod(s).invoke(recursiveItem);

					for (int i = 0; i < lObjs.size(); i++) {

						Object o = LearningDesignUtils.getRef(lObjs.get(i));
						if (o == obRemove) {
							lObjs.remove(i);
							i--;
						}
						else if (lObjs.get(i) == obRemove) {
							lObjs.remove(i);
							i--;
						}
						else {
							recursiveRemove(obRemove, lObjs.get(i));
						}

					}
				} catch (Exception e) {

					//e.printStackTrace();
				}
			}
			else if (f.getType().getCanonicalName().startsWith("org.imsglobal.jaxb.ld")) {
				try{

					Object o = recursiveItem.getClass().getMethod( Suport.mGetOfField(f) ).invoke(recursiveItem);

					if (o == obRemove || LearningDesignUtils.getRef(o) == obRemove) {
						recursiveItem.getClass().getMethod( Suport.mSetOfField(f) , Class.forName(f.getType().getCanonicalName())).invoke(recursiveItem, new Object[]{ null });
					}
					else if (o != null) {				
						recursiveRemove(obRemove, o);
					}



				} catch (Exception e) {

					//e.printStackTrace();
				}
			}
			else {
				try{

					Object o = recursiveItem.getClass().getMethod( Suport.mGetOfField(f) ).invoke(recursiveItem);
					Class c = o.getClass();

					if (c.getPackage() != imsldFactory.getClass().getPackage())
						continue;

					if (o == obRemove || LearningDesignUtils.getRef(o) == obRemove) {
						recursiveItem.getClass().getMethod( Suport.mSetOfField(f) , Class.forName(f.getType().getCanonicalName())).invoke(recursiveItem, new Object[]{ null });
					}
					else if (o != null) {				
						recursiveRemove(obRemove, o);
					}

				} catch (Exception e) {

					//e.printStackTrace();
				}

			}
		}
	}


	/*
	 * Dado objeto de chave <key>, o <objeto> a referencia?  
	 */
	public boolean existeVinculo(String key, String agregante) {

		if (agregante == null)
			return false;

		TreeMap<String, Object> lRefs = referencias.get(key);

		if (lRefs == null )
			return false;

		return lRefs.containsKey(agregante);		
	}


	public LdProject getLdProject(HttpServletRequest request) {

		String ldep = Suport.r(request, "ldep");

		for (LdProject aux: repositorio) {

			if (aux.getIdentifier().equals(ldep)) {
				return aux;
			}

		}
		return null;
	}
	public LdProject getLdProjectByIdentifier(String identifier) {


		for (LdProject aux: repositorio) {

			if (aux.getIdentifier().equals(identifier)) {
				return aux;
			}
		}
		return null;
	}

	public LdProject getLdProjectByIdentifierOfLD(String identifier) {


		for (LdProject aux: repositorio) {

			if (aux.getLd() != null && aux.getLd().getIdentifier() != null)
				if (aux.getLd().getIdentifier().equals(identifier)) {
					return aux;
				}
		}
		return null;
	}

	public void removeLdProject(String identifier) {


		if (identifier != null) {

			LdProject aux = getLdProjectByIdentifier(identifier);

			//aux.setResources(null);

			repositorio.remove(aux);
			objsWkspc.remove(aux.getIdentifier());

			for (String key :referencias.keySet()) {
				referencias.get(key).remove(identifier);
			}

			aux.getLd().setInheritRoles(null);
			List<ArrayList<Learner>> lLearners= RolesController.getLearners(aux);
			List<ArrayList<Staff>> lStaffs= RolesController.getStaffs(aux);

			List<Role> lRoles = new ArrayList<Role>();
			lRoles.addAll(lLearners.get(0));
			lRoles.addAll(lStaffs.get(0));

			for (Role r: lRoles) {
				excluirOfWorkspace((Serializable) r);
			}


			Class[] stopClass = {RoleRef.class, Learner.class, Staff.class};
			List<Object> lista = Suport.listaDeObjetos(aux, stopClass);

			for (Object item: lista) {

				try {
					if (item instanceof LdProject)
						repositorio.remove(item);

					if (!item.getClass().getCanonicalName().startsWith("org.imsglobal.jaxb"))
						continue;

					excluirOfWorkspace((Serializable) item);
				}
				catch (Exception e) {

				}
			}
			return;

		}

	}

	public void newLdProject() {

		repositorio.add( new LdProject(this));

	}

	public LdProject newLdProject(String titulo) {

		LdProject ld = new LdProject(this, titulo);		

		repositorio.add(ld);
		return ld;

	}

	public void toogleFlagTreeView(String _identifier) {

		for (LdProject ld : repositorio) {

			if (ld == null)
				return;

			if (ld.getIdentifier().equals(_identifier))		{	
				ld.flagTreeView = !ld.flagTreeView;

				//System.out.println(Boolean.toString(ld.flagTreeView));
			}
		}
	}

	public Manifest createManifest(LdProject ldep) {
		Manifest manifest = null;
		ArrayList<Resource> listaResources = new ArrayList<Resource>();
		manifest = createManifestRecursiveRun(ldep, listaResources);
		return manifest;
	}


	public Manifest createManifestRecursiveRun(LdProject ldep, ArrayList<Resource> listaResources) {

		Manifest manifest = new Manifest();
		manifest.setIdentifier(ldep.getIdentifier());
		if (manifest.getOrganizations() == null) {
			manifest.setOrganizations(new Organizations());
		}

		manifest.getOrganizations().getAny().add(
				imsldFactory.createLearningDesign((LearningDesign) ldep.getLd()));


		/* ldep.getResources traz todos os resources do ImeProject
		 * Porém, devido ao fato de haver herança de roles, os recursos dessas roles são carregados para cada submanifesto, o que inconsistencia no xml
		 *  por isso foi criado a lista de resources para, garantir que um recurso não seja adicionado nos sub-manifestos, ficando assim apenas no
		 *  manifesto onde se contra a Role que possui um item que aponta para tal recurso. 
		 */
		Resources rs = ldep.getResources();
		for (Iterator<Resource> itR = rs.getResourceList().iterator(); itR.hasNext();) {

			Resource r = itR.next();

			if (listaResources.contains(r)) {
				itR.remove();
			}
			else {
				listaResources.add(r);
			}
		}
		manifest.setResources(rs);

		for (LdProject ldAgregados : ldep.getLdAgregados())
			manifest.getManifestList().add(createManifestRecursiveRun(ldAgregados, listaResources));

		return manifest;
	}
	private LdProject createLdep(Manifest m) {

		LdProject ldep = new LdProject();
		ldep.workspace = this;

		ldep.setIdentifier(m.getIdentifier());
		ldep.setLd(((JAXBElement<LearningDesign>) m.getOrganizations().getAny().get(0)).getValue());

		for (Resource r: m.getResources().getResourceList()) {
			r.setIdentifier(this.newIdentifier(r, "resource"));  
		}

		//ldep.setResources(m.getResources());

		for (Manifest subM: m.getManifestList()) {
			ldep.agregarLd(createLdep(subM));
		}

		return ldep;

	}


	public LdProject clonarLdep(LdProject ldep){

		try {

			Manifest manifest = createManifest(ldep);


			ByteArrayOutputStream out = new ByteArrayOutputStream();			
			putManifest(out, manifest, "cp", "ld", null);

			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
			manifest = getManifest(in);

			return createLdep(manifest);

		}
		catch (Exception e) {
			return null;
		}
	}

	public String criarCopiaLdep(String identifier){

		LdProject ldep = getLdProjectByIdentifier(identifier);
		String result = criarCopiaLdep(ldep);


		return result;


	}

	public String criarCopiaLdep(LdProject ldep){

		try {

			Manifest manifest = createManifest(ldep);


			ByteArrayOutputStream out = new ByteArrayOutputStream();			
			putManifest(out, manifest, "cp", "ld", null);

			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
			manifest = getManifest(in);

			this.tmpListImport.clear();
			this.registrarObjetos(manifest, null);
			this.tmpListImport.clear();

			LdProject ldepResult = getLdProjectByIdentifier(manifest.getIdentifier());
			ldepResult.skin = ldep.skin;

			return manifest.getIdentifier();

		}
		catch (Exception e) {
			return null;
		}


	}

	public void xmlBackupProject(OutputStream out, String ldep) throws Exception {

		Manifest manifest = createManifest(this.getLdProjectByIdentifier(ldep));

		putManifest(out, manifest, "cp", "ld", null);

	}

	public void xmlBackupWorkspace(OutputStream out) throws Exception {

		List<LdProject> lldep = this.getMasterListLdProject();	

		Manifest manifest = new Manifest();
		manifest.setIdentifier("exportAll");

		for (LdProject ldep : lldep) {

			manifest.getManifestList().add(createManifest(ldep));

		}

		putManifest(out, manifest, "cp", "ld", null);
	}

	public  List<ValidationEvent> putManifest(OutputStream out, Manifest manifest, final String prefixCP, final String prefixLD, Schema sf) throws Exception{

		JAXBContext jaxbContext;
		jaxbContext = JAXBContext
				.newInstance("org.imsglobal.jaxb.content:org.imsglobal.jaxb.ld");


		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		NamespacePrefixMapper mapper = new NamespacePrefixMapper() {

			@Override
			public String getPreferredPrefix(String arg0, String arg1, boolean arg2) {

				if (arg0.equals("http://www.imsglobal.org/xsd/imscp_v1p1"))
					return prefixCP; //
				else if (arg0.equals("http://www.imsglobal.org/xsd/imsld_v1p0"))
					return prefixLD; //
				else 
					return "";
			}
		};  
		marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", mapper);


		marshaller.setEventHandler(new ValidationEventHandler() {

			private List<ValidationEvent> vEvents = new ArrayList<ValidationEvent>();
			@Override
			public boolean handleEvent(ValidationEvent ve) {
				//	 if (ve.getSeverity() != ValidationEvent.WARNING) {  
				ValidationEventLocator vel = ve.getLocator();
				Object ob = vel.getObject();
				System.out.println("Line:Col[" + vel.getLineNumber() +  
						":" + vel.getColumnNumber() +  
						"]:" + ve.getMessage());  
				//      }  

				vEvents.add(ve);
				return true;  
			}

			public List<ValidationEvent> getVEvents() {
				return vEvents;
			}

		});


		marshaller.setSchema(sf);
		//marshaller.setSchema(null);
		marshaller.marshal(imscpFactory.createManifestJaxbElement(manifest),  out  );





		Object ob = marshaller.getEventHandler();

		Field[] fields = ob.getClass().getDeclaredFields();

		for (Field f : fields) {
			if (!f.getName().equals("vEvents"))
				continue;

			List<ValidationEvent> list = (List<ValidationEvent>) ob.getClass().getMethod(Suport.mGetOfField(f)).invoke(ob);
			return list;


		}


		return null;
	}

	public Manifest getManifest(InputStream in) throws Exception{

		JAXBContext jaxbContext;
		jaxbContext = JAXBContext
				.newInstance("org.imsglobal.jaxb.content:org.imsglobal.jaxb.ld");

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		JAXBElement<Manifest> object = (JAXBElement<Manifest>) unmarshaller
				.unmarshal(in);

		Object ob = object.getValue();

		return (Manifest) ob;
	}


	public String importXMLStream(InputStream in) throws Exception {

		Manifest m = getManifest(in);
 
		this.tmpListImport.clear();
		this.registrarObjetos(m, null);
		this.tmpListImport.clear();

		return m.getIdentifier();

	}

	public void importXMLRequest(InputStream filecontent, HttpServletRequest request, HttpServletResponse response) throws Exception {
 
		try {
			importXMLStream(filecontent);
		} catch (Exception e) {

			Urls.forward(request, response, e.getMessage(), "views/ldew/ldew.list.jsp", null); 
			return;
		}

		//System.out.println("Fim");

		Urls.forward(request, response, "Importação concluida com sucesso.", "views/ldew/ldew.list.jsp", null); 

		return;
	}

@SuppressWarnings("unchecked")
private boolean registrarObjetos(Object obImport, Object parent) throws Exception {

	if (obImport == null) {
		return false;
	}

	countNivel++;

	String identifier = null;
	try {
		identifier = LearningDesignUtils.getIdentifierForImport(obImport);
		if (identifier == null)
			identifier = this.newIdentifier(obImport,obImport.getClass().getSimpleName().toUpperCase());
	}
	catch (Exception e) {
		identifier = null;
	}

	if (obImport instanceof Manifest) {

		int sufix = 0;
		while (this.objsWkspc.containsKey(identifier+(sufix==0?"":"-"+sufix))) {
			sufix++;
		}			
		if (sufix > 0) {
			identifier += "-"+sufix;
			obImport.getClass().getMethod("setIdentifier", String.class).invoke(obImport, new Object[]{ identifier});			
		}
		tmpListImport.put(identifier, obImport);

		Manifest m = (Manifest)obImport;

		if (m.getOrganizations() != null) {
			LdProject ldep = new LdProject();
			this.putObjsWkspc(identifier, ldep);

			ldep.workspace = this;
			ldep.setIdentifier(m.getIdentifier());

			if (m.getOrganizations().getAny().size() == 0 ||
					((JAXBElement<LearningDesign>) m.getOrganizations().getAny().get(0)) == null ||
					((JAXBElement<LearningDesign>) m.getOrganizations().getAny().get(0)).getValue() == null)
				return false;				

			ldep.setLd(((JAXBElement<LearningDesign>) m.getOrganizations().getAny().get(0)).getValue());

			for (Resource r: m.getResources().getResourceList()) {
				registrarObjetos(r, ldep);

				//r.setIdentifier(this.newIdentifier(r, "resource"));  
				//this.tmpListImport.put(identifier, obImport);

			}
			//ldep.setResources(m.getResources());

			if (parent instanceof LdProject) {
				((LdProject) parent).agregarLd(ldep);					
			}
			this.repositorio.add(ldep);	
			parent = ldep;

			for (Manifest mm : m.getManifestList())
				registrarObjetos(mm, ldep);
		}
		else {
			for (Manifest mm : m.getManifestList())
				registrarObjetos(mm, null);
			return false;
		}
	}
	else if (identifier != null) {

		if (tmpListImport.containsKey(identifier)) {

			if (parent != null)
				this.referenciar(identifier, parent);

			countNivel--;
			return false;
		}			

		int sufix = 0;
		while (this.objsWkspc.containsKey(identifier+(sufix==0?"":"-"+sufix))) {
			sufix++;
		}			

		try {
			if (sufix > 0) {
				identifier += "-"+sufix;
				obImport.getClass().getMethod("setIdentifier", String.class).invoke(obImport, new Object[]{ identifier});			
			}

			if (obImport instanceof LdProject) {
				((LdProject) obImport).workspace = this;
				this.repositorio.add((LdProject) obImport);
			}
			this.tmpListImport.put(identifier, obImport);
			this.putObjsWkspc(identifier, obImport);

			if (parent == null)
				parent = obImport;
			else {
				// parent é sempre um objeto que tem identifier
				// parent é sempre o objeto agrengante do que está sendo importando
				this.referenciar(identifier, parent);

				//depois de referenciado, obImport passa a ser parent imediato para novas chamadas
				parent = obImport;
			}

		}
		catch (Exception e) {
			throw new Exception("Ocorreu um erro na importação!!!");
		}
	}

	Field[] lFields = obImport.getClass().getDeclaredFields(); // lista de atributos do objeto item
	//Method[] lMethods = item.getClass().getDeclaredMethods(); // lista de atributos do objeto item


	for (Field f : lFields) {
		/*System.out.println();
		for (int k =0; k < countNivel; k++)			
			System.out.print("  ");*/

		//System.out.print("Type: "+f.getType());
		//System.out.print("  GenericType: "+f.getGenericType());


		if (f.getGenericType().toString().equals("java.util.List<org.imsglobal.jaxb.content.Manifest>")) {

		}
		else if (f.getGenericType().toString().equals("java.util.List<br.edu.ifg.ime.ld.LdProject>")) {

		}
		else if (f.getGenericType().toString().equals("java.util.List<java.lang.String>")) { 

		}
		else if (f.getType().getCanonicalName().equals("java.util.List")) {
			String s = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
			try {
				List<Serializable> lObjs  = (List<Serializable>) obImport.getClass().getMethod(s).invoke(obImport);
				//System.out.print(" - "+s);
				for (int i = 0; i < lObjs.size(); i++) {
					registrarObjetos(lObjs.get(i), parent);						
				}
			} catch (Exception e) {
				throw new Exception("Ocorreu um erro na importação List!!!");
			}
		}
		else if (f.getType().getCanonicalName().startsWith("org.imsglobal.jaxb.ld") || f.getType().getCanonicalName().startsWith("org.imsglobal.jaxb.content") || f.getType().getCanonicalName().equals("java.lang.Object") || f.getType().getCanonicalName().equals("java.io.Serializable")) {
			try{
				String get = "get";
				if (f.getName().startsWith("_"))
					get = "get" + f.getName().substring(1, 2).toUpperCase() + f.getName().substring(2);
				else
					get = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
				//String set = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
				//System.out.print(" - "+get);


				Object o = obImport.getClass().getMethod(  Suport.mGetOfField(f)  ).invoke(obImport);

				registrarObjetos(o, parent);

			} catch (Exception e) {
				throw new Exception("Ocorreu um erro na importação start org.ims.... ou object... ou seriali...!!! "+f.getType().getCanonicalName());
			}
		}
	}
	countNivel--;
	return true;
}

	private void putObjsWkspc(String identifier, Object obImport)  {

		this.objsWkspc.put(identifier, obImport);

		Field[] lFields = obImport.getClass().getDeclaredFields(); // lista de atributos do objeto item

		for (Field f : lFields) {

			if (f.getName().equals("title")) {

				String get = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
				String set = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);

				Object o;
				try {
					o = obImport.getClass().getMethod(get).invoke(obImport);


					if (o == null || ((String)o).length() == 0)
						obImport.getClass().getMethod(set, String.class).invoke(obImport, new Object[]{ identifier });
					//					obImport.getClass().getMethod(set, String.class).invoke(obImport, new Object[]{ obImport.getClass().getSimpleName().toUpperCase()+"- no title."});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<ValidationEvent>  zipImscpWithoutResumeMi(OutputStream out, String strLdep) throws Exception {

		return this.zipImscpWithoutResumeMi(out, strLdep, "cp", "ld", null);

	}

	public List<ValidationEvent>  zipImscpWithoutResumeMi(OutputStream out, String strLdep, String xmlnsCP, String xmlnsLD, String schemaBase) throws Exception {

		ImeWorkspace w = new ImeWorkspace();
		LdProject ldep = w.clonarLdep(this.getLdProjectByIdentifier(strLdep));
		ldep.workspace = w;

		w.reduzirIdentifiers(ldep);

		//ZipArchiveOutputStream zOut;

		//	zOut = new ZipArchiveOutputStream(out);
		ZipArchiveOutputStream zOut = new ZipArchiveOutputStream(new BufferedOutputStream(out));
		zOut.setLevel(9);
		zOut.setEncoding("UTF-8"); // This should handle your "special" characters
		zOut.setFallbackToUTF8(true); // For "unknown" characters!
		zOut.setUseLanguageEncodingFlag(true);                               
		zOut.setCreateUnicodeExtraFields(
				ZipArchiveOutputStream.UnicodeExtraFieldPolicy.NOT_ENCODEABLE );
		//zOut.setMethod(ZipArchiveOutputStream.DEFLATED);

		ZipArchiveEntry ze; 

		HashSet<String> keyNameFiles = new HashSet<String>();


		ldep.getResources();
		Collection<Item> items = ldep.getAllItemsWithItemsSubProjects();

		for (Item item : items) {

			Resource r = (Resource) item.getIdentifierref(); 

			if (r == null || r.getFileList().size() == 0) {
				item.setFile(null);
				item.setText(null);
				item.setLink(null);
				item.setNameFile(null);
				continue;
			}			

			r.getFileList().get(0).setHref(r.getFileList().get(0).getHref().replaceAll(" \\(", "-"));
			r.getFileList().get(0).setHref(r.getFileList().get(0).getHref().replaceAll("\\(", "-"));
			r.getFileList().get(0).setHref(r.getFileList().get(0).getHref().replaceAll("\\)", ""));
			r.setHref(r.getFileList().get(0).getHref());

			String nameFile = r.getFileList().get(0).getHref();
			String auxNameFile = nameFile;




			int count = 1;
			while (keyNameFiles.contains(auxNameFile)) {
				String partName[] = nameFile.split("\\.");

				String name = "";
				String ext = "";

				for (int i = 0; i < partName.length-1; i++)
					name += partName[i];

				if (partName.length > 1)
					ext = partName[partName.length-1];

				auxNameFile = name + "-"+(count++)+ (ext.length()>0?"."+ext:"");				

			}	

			nameFile = auxNameFile;
			keyNameFiles.add(nameFile);
			r.getFileList().get(0).setHref(nameFile);
			r.setHref(nameFile);
			ze = new ZipArchiveEntry(nameFile);

			try {	
				zOut.putArchiveEntry(ze);
			} catch (IOException e) {
				zOut.closeArchiveEntry();
				zOut.flush();
				zOut.close();
				throw new Exception("Erro em compactar arquivo!");

			}		

			if (item.getType().equals("file")) {
				zOut.write(item.getFile());
			}
			else if (item.getType().equals("text")) {
				if (item.getText() == null)
					item.setText("");
				zOut.write(item.getText().getBytes());
			}
			zOut.closeArchiveEntry();
			item.setFile(null);
			item.setText(null);
			item.setLink(null);
			item.setNameFile(null);
		}

		ze = new ZipArchiveEntry("imsmanifest.xml");

		try {	
			zOut.putArchiveEntry(ze);
		} catch (IOException e) {
			zOut.closeArchiveEntry();
			zOut.flush();
			zOut.close();
			throw new Exception("Erro em compactar arquivo!");
		}		

		Schema schemas = null; 
		if (schemaBase == null) {	//byte[] result = ArquivoController.getArquivoDAO().getBytesArquivo(url);		
			schemaBase = "IMS_LD_Level_"+ldep.getLd().getLevel().toUpperCase()+"_LMI_Import.xsd";

			if (DAOConnection.isActivated)
				if ( !ArquivoController.existeXSD(schemaBase) )
					schemaBase = "IMS_LD_Level_"+ldep.getLd().getLevel().toUpperCase()+".xsd";

		}
		
		schemas = SimpleResolver.createSchema(schemaBase);



		List<ValidationEvent> result =  w.putManifest(zOut, w.createManifest(ldep), xmlnsCP, xmlnsLD, schemas);

		zOut.closeArchiveEntry();

		zOut.flush();
		zOut.close();

		return result;
	}

	private void reduzirIdentifiers(LdProject ldep) { // usar apenas em workspaces paralelos com apenas um projeto principal
		this.tmpListImport.clear();

		Class[] stopClass = {LearningDesign.class, Resource.class, String.class, Resources.class, ImeWorkspace.class};
		List lista = Suport.listaDeObjetos(ldep, stopClass, null);

		for (Object ob: lista) {
			String identifier = null;
			try {
				identifier = LearningDesignUtils.getIdentifierForImport(ob);
				if (identifier == null) {
					continue;
				}
			}
			catch (Exception e) {
				identifier = null;
				continue;
			}

			tmpListImport.put(identifier, ob);

		}

		lista = Suport.listaDeObjetos(ldep.getLd(), null);

		for (Object ob: lista) {
			String identifier = null;
			try {
				identifier = LearningDesignUtils.getIdentifierForImport(ob);
				if (identifier == null) {
					continue;
				}
			}
			catch (Exception e) {
				identifier = null;
				continue;
			}

			tmpListImport.put(identifier, ob);

		}


		while (!tmpListImport.isEmpty()) {

			Entry<String, Object>  tmpEntry = tmpListImport.pollLastEntry();

			String identifier = Suport.organizarIdentifer(tmpEntry.getKey());			


			Object ob = tmpEntry.getValue();
			identifier = this.newIdentifier(ob, identifier);

			try {
				ob.getClass().getMethod("setIdentifier", String.class).invoke(ob, new Object[]{ identifier});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}



	public String getCodeIMSCPOfLdepProject(String strLdep) {
		String result = "";

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			xmlBackupProject(out, strLdep);

		} catch (Exception e) {
			return "";
		}

		return out.toString();
	}

	public void updateByCodeIMSCP(String ldep, String codigo) {

		try {
			int pos = this.repositorio.indexOf(this.getLdProjectByIdentifier(ldep));
			this.removeLdProject(ldep);
			int posRemove = this.repositorio.size();

			ByteArrayInputStream in = new ByteArrayInputStream(codigo.getBytes());

			importXMLStream(in);
			codigo = "";
			this.repositorio.add(pos,this.repositorio.remove(posRemove));

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void importZIPRequest(InputStream filecontent, HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			importZIPStream(filecontent);
		} catch (Exception e) {

			Urls.forward(request, response, e.getMessage(), "views/ldew/ldew.list.jsp", null); 
			return;
		}

		//System.out.println("Fim");

		Urls.forward(request, response, "Importação concluida com sucesso.", "views/ldew/ldew.list.jsp", null); 

		return;
	}


	public LdProject importZIPStream(InputStream filecontent) throws Exception {

		ArchiveInputStream zIn = new ArchiveStreamFactory()
		.createArchiveInputStream("zip", filecontent);
		ZipArchiveEntry zItem = null;

		TreeMap<String, byte[]> listaFiles = new TreeMap<String, byte[]>();

		while ((zItem = (ZipArchiveEntry) zIn.getNextEntry()) != null) {

			//String s = String.format("Entry: %s len %d added %TD",
			//		zItem.getName(), zItem.getSize(),
			//		new Date(zItem.getTime()));
			//System.out.println(s);

			listaFiles.put(zItem.getName(), Suport.toByteVector(zIn));
		}

		byte[] bManifest = listaFiles.remove("imsmanifest.xml");

		if (bManifest == null)
			throw new Exception("Arquivo [imsmanifest.xml] não encontrado!!!");

		ByteArrayInputStream bIn = new ByteArrayInputStream(bManifest);
		String mId = this.importXMLStream(bIn);

		LdProject ldep = this.getLdProjectByIdentifier(mId);

		while (!listaFiles.isEmpty()) {
			String keyFirst = listaFiles.firstKey();
			ldep.setFileItem(keyFirst,	listaFiles.remove(keyFirst));
		}

		return ldep;
	}



	public synchronized static ImeWorkspace getImeWorkspace(HttpServletRequest request) {

		//Contexto de sessão
		HttpSession session = request.getSession();

		Object ob = session.getAttribute("workspace");

		if (ob == null) {
			ImeWorkspace w = new ImeWorkspace();
			session.setAttribute("workspace", w);
			ob = w;
		} 
		ImeWorkspace w = (ImeWorkspace)ob;
		return w;


		//contexto de aplicação
		/*if (workspace == null) {
			workspace = new LdEditorWorkspace();			
		}
		return workspace;*/
	}

	public void oldState(String ldep) {

		this.getLdProjectByIdentifier(ldep).oldState();

	}

	public void oldStateById(String ldepId) {

		for (LdProject ldep : repositorio) {

			if (ldep.getId() != Integer.parseInt(ldepId))
				continue;

			ldep.oldState();
			return;
		}


	}

	public LdProject getLdProjectById(String ldepId) {

		for (LdProject aux: repositorio) {

			if (aux.getId() != Long.parseLong(ldepId))
				continue;

			return aux;
		}
		return null;
	}


	public void switchPosLdeps(String[] params, String start, String stop) {

		int stopPos = Integer.parseInt(stop);
		int startPos = Integer.parseInt(start);

		repositorio.add(stopPos, repositorio.remove(startPos));


		return;		


	}


	public List<ValidationEvent>  zipImscpWithResumeMi(OutputStream out, String strLdep ) throws Exception {



		ByteArrayOutputStream bOut = new ByteArrayOutputStream();	
		this.xmlBackupProject(bOut, strLdep);

		ImeWorkspace w = new ImeWorkspace();
		ByteArrayInputStream in = new ByteArrayInputStream(bOut.toByteArray());
		w.importXMLStream(in);

		LdProject ldep = w.getLdProjectByIdentifier(strLdep);


		ldep.getLd().setTitle(ldep.getLd().getTitle()+" (Copia LD).");

		w.reduzirElementosNaoDuplicaveis(ldep);

		w.reduzirElementosDuplicaveis(ldep);

		//out = new ByteArrayOutputStream();	
		//w.xmlBackupProject(out, strLdep);
		//in = new ByteArrayInputStream(bOut.toByteArray());
		//this.importXMLStream(in);

		return w.zipImscpWithoutResumeMi(out, strLdep, "imscp", "imsld", "IMS_LD_Level_"+ldep.getLd().getLevel().toUpperCase()+".xsd");

	}


	public void criarCopiaNoPadraoLd(String strLdep ) throws Exception {



		ByteArrayOutputStream out = new ByteArrayOutputStream();	
		this.xmlBackupProject(out, strLdep);

		ImeWorkspace w = new ImeWorkspace();
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		w.importXMLStream(in);

		LdProject ldep = w.getLdProjectByIdentifier(strLdep);


		ldep.getLd().setTitle(ldep.getLd().getTitle()+" (Copia LD).");

		w.reduzirElementosNaoDuplicaveis(ldep);

		w.reduzirElementosDuplicaveis( ldep);

		out = new ByteArrayOutputStream();	
		w.xmlBackupProject(out, strLdep);

		in = new ByteArrayInputStream(out.toByteArray());
		this.importXMLStream(in);
	}


	protected void reduzirElementosDuplicaveis( LdProject ldep) throws Exception {


		this.tmpListImport.clear();


		//Registra todos os componentes pois estes nao devem ser clonados
		for (Object obImport: Suport.listaDeObjetos(ldep.getLd().getComponents(), null)) {
			String identifier = null;
			try {
				identifier = LearningDesignUtils.getIdentifierForImport(obImport);
				if (identifier == null) {
					identifier = this.newIdentifier(obImport,obImport.getClass().getSimpleName().toUpperCase());
					continue;
				}
			}
			catch (Exception e) {
				identifier = null;
				continue;
			}

			this.tmpListImport.put(identifier, obImport);

		}
		//System.out.println("Numero de composições: "+(this.repositorio.size()-1)+"..... Num de Elementos: "+this.objsWkspc.size());
		this.reduzirMetodos( ldep, null, null);
	}

	public int getCountObjsWkspc() {
		return objsWkspc.size();
	}

	//private void reduzirMetodos(ImeWorkspace ww, LdProject ldep, Play pPlay, Act pAct) throws Exception {

	private void reduzirMetodos( LdProject ldep, Play pPlay, Act pAct) throws Exception {

		if (ldep == null)
			return;

		//Procura o MI mais perto da raiz que contem Plays sem composição 
		while (!ldep.getLdAgregados().isEmpty()) {
			boolean flagEscape = false;
			for (Play play : PlaysController.getPlays(ldep)) {
				for (Act act : play.getActList()) {
					if (act.getLearningDesignRef() != null) {

						reduzirMetodos( this.getLdProjectByIdentifierOfLD(  ((LearningDesign)act.getLearningDesignRef().getRef()).getIdentifier()  ), play, act  );
						System.gc();

						flagEscape = true;
					}
					if (flagEscape)
						break;					
				}
				if (flagEscape)
					break;
			}			
		}


		if (pPlay == null)
			return;

		// Se o MI encontrado tiver N Play's, o Play ao qual o MI está vinculado deverá ser replicado N vezes
		if (PlaysController.getPlays( ldep ).size() > 1) {


			// parent(MI anterior) ao qual o MI atual está vinculado.
			LdProject parent = ldep.parent;


			//Geral uma clone do projeto principal a fim de replicar o parent(MI anterior)
			LdProject raiz = ldep; 
			while (raiz.parent != null) 
				raiz = raiz.parent;  
			LdProject cloneRaiz = this.clonarLdep(raiz);



			//Encontra o Clone do parent(MI anterior)
			LdProject cloneParent = null; 
			boolean flagEscape = false;
			while (!flagEscape) {
				for (Play play : PlaysController.getPlays(cloneRaiz)) {
					boolean flagInterno = false;
					if (play.getIdentifier().equals(pPlay.getIdentifier())) {
						flagEscape = true;
						cloneParent = cloneRaiz;
						break;
					}
					for (Act act : play.getActList()) {
						if (act.getLearningDesignRef() != null) {
							for (LdProject aux: cloneRaiz.getLdAgregados()) {
								if (aux.getLd() != null && aux.getLd().getIdentifier() != null)
									if (aux.getLd().getIdentifier().equals( ((LearningDesign)act.getLearningDesignRef().getRef()).getIdentifier() )) {
										cloneRaiz = aux;
										flagInterno = true;
									}
								if (flagInterno)
									break;		
							}
						}
						if (flagInterno)
							break;					
					}
					if (flagInterno)
						break;
				}
			}

			// A replicação é feita Play a Play, sempre pelo topo, contendo o primeiro Play do original. 
			// O clone será inserido no resultado sempre antes de seu original.
			//E então o original sempre perde seu primeiro Play.
			PlaysController.getPlays( ldep ).remove(0);

			// Em que posição o CloneParent devera ser inserido
			int iPlay = 0;
			for (iPlay = 0; iPlay < PlaysController.getPlays(parent).size(); iPlay++) {
				if (PlaysController.getPlays(parent).get(iPlay) == pPlay)
					break;
			}
			int iAct = 0;
			for (iAct = 0; iAct < pPlay.getActList().size(); iAct++) {
				if (pPlay.getActList().get(iAct) == pAct)
					break;
			}

			// Como do Original foi retirado o primeiro, no clone deve permanecer apenas o primeiro
			List<Play> newPlaysCopiaLdep = ((LearningDesign)PlaysController.getPlays( cloneParent ).get(iPlay).getActList().get(iAct).getLearningDesignRef().getRef()).getMethod().getPlayList();
			while (newPlaysCopiaLdep.size() > 1) 
				newPlaysCopiaLdep.remove(newPlaysCopiaLdep.size()-1);

			//Move o Play do CloneParent para torná-lo um Play Original.
			Play copiaPlay =  PlaysController.getPlays(cloneParent).remove(iPlay);
			PlaysController.getPlays( parent ).add(iPlay,copiaPlay);

			//Adiciona na lista de agregados os MI's subsequentes ao Atual, que ainda não foram processados, mas que foram clonados
			for (LdProject ldepAg: cloneParent.getLdAgregados()) {
				for (Act act: copiaPlay.getActList()) {
					if (act.getLearningDesignRef() != null && ((LearningDesign)act.getLearningDesignRef().getRef()).getIdentifier().equals(ldepAg.getLd().getIdentifier())) {
						parent.agregarLd(ldepAg);
						break;
					}
				}
			}
			incluirAgregadosNoRepositorio(cloneParent);

			//Torna todos os objetos do Clone, objetos da lista de Objetos válidos do projeto Original
			for (Object obImport: Suport.listaDeObjetos(copiaPlay, null)) {
				String identifier = null;
				try {
					identifier = LearningDesignUtils.getIdentifierForImport(obImport);
					if (identifier == null) {
						identifier = this.newIdentifier(obImport,obImport.getClass().getSimpleName().toUpperCase());
						continue;
					}
				}
				catch (Exception e) {
					identifier = null;
					continue;
				}
				if (this.tmpListImport.containsKey(identifier))
					continue;


				int sufix = 0;
				while (this.objsWkspc.containsKey(identifier+(sufix==0?"":"-"+sufix))) {
					sufix++;
				}			
				if (sufix > 0) {
					identifier += "-"+sufix;
					obImport.getClass().getMethod("setIdentifier", String.class).invoke(obImport, new Object[]{ identifier});			
				}
				this.objsWkspc.put(identifier, obImport);

			}

		}
		else if (PlaysController.getPlays( ldep ).size() == 1) {

			//Caso exista apenas um Play no MI atual, todos os seus Acts são integrados no MI Parent.
			List<Act> lActs = PlaysController.getPlays( ldep ).get(0).getActList();
			ldep.parent.getLdAgregados().remove(ldep);
			this.repositorio.remove(ldep);

			int iAct = 0;
			for (iAct = 0; iAct < pPlay.getActList().size(); iAct++) {
				if (pPlay.getActList().get(iAct) == pAct)
					break;
			}
			pPlay.setTitle(pPlay.getTitle() + " ("+PlaysController.getPlays( ldep ).get(0).getTitle()+")");
			pPlay.getActList().remove(iAct);
			pPlay.getActList().addAll(iAct, lActs);
		} 
	}

	//	System.out.println(pPlay+" - "+pPlay.getActList());

	//	desenharLdep(ldep);

	/*
	// Debug em tela;;
	out = new ByteArrayOutputStream();	
	this.xmlBackupProject(out, ldep.parent.getIdentifier()); 
	in = new ByteArrayInputStream(out.toByteArray());
	ww.importXMLStream(in)*/;


	private void incluirAgregadosNoRepositorio(LdProject cloneParent) {

		if (!this.repositorio.contains(cloneParent)) {
			this.repositorio.add(cloneParent);
		}

		for (LdProject item: cloneParent.ldAgregados)
			incluirAgregadosNoRepositorio(item);

	}
	private  Manifest createManifestAllLds(List<LdProject> ldeps) {

		Manifest manifest = new Manifest();
		if (manifest.getOrganizations() == null) {
			manifest.setOrganizations(new Organizations());
		}

		for (LdProject ldep: ldeps)
			manifest.getOrganizations().getAny().add(
					imsldFactory.createLearningDesign((LearningDesign) ldep.getLd()));

		try {
			putManifest(System.out, manifest, "cp", "ld", null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return manifest;
	}


	protected void reduzirElementosNaoDuplicaveis(LdProject master) {

		for (LdProject ldep: repositorio) {

			if (ldep == master)
				continue;

			RolesController.getRoles(master).getLearnerList().addAll( RolesController.getRoles(ldep).getLearnerList() );
			RolesController.getRoles(master).getStaffList().addAll( RolesController.getRoles(ldep).getStaffList() );
			RolesController.getRoles(ldep).getLearnerList().clear();
			RolesController.getRoles(ldep).getStaffList().clear();

			ActivityController.getActivities(master).getLearningActivityOrSupportActivityOrActivityStructure().addAll(
					ActivityController.getActivities(ldep).getLearningActivityOrSupportActivityOrActivityStructure() );
			ActivityController.getActivities(ldep).getLearningActivityOrSupportActivityOrActivityStructure().clear();

			EnvironmentsController.getEnvironments(master).getEnvironmentList().addAll(
					EnvironmentsController.getEnvironments(ldep).getEnvironmentList() );
			EnvironmentsController.getEnvironments(ldep).getEnvironmentList().clear();
			
			if (ldep.getLd().getComponents().getProperties() != null) {
				if (master.getLd().getComponents().getProperties() == null)
					master.getLd().getComponents().setProperties( new Properties() );

				master.getLd().getComponents().getProperties().getLocPropertyOrLocpersPropertyOrLocroleProperty().addAll( 
						ldep.getLd().getComponents().getProperties().getLocPropertyOrLocpersPropertyOrLocroleProperty() );		
				ldep.getLd().getComponents().getProperties().getLocPropertyOrLocpersPropertyOrLocroleProperty().clear();
			}

			if (ldep.getLd().getLearningObjectives() != null) {
				if (master.getLd().getLearningObjectives() == null)
					master.getLd().setLearningObjectives(ldep.getLd().getLearningObjectives());
				else 
					master.getLd().getLearningObjectives().getItemList().addAll(ldep.getLd().getLearningObjectives().getItemList());	
				ldep.getLd().setLearningObjectives(null);
			}

			if (ldep.getLd().getPrerequisites() != null) {
				if (master.getLd().getPrerequisites() == null)
					master.getLd().setPrerequisites(ldep.getLd().getPrerequisites());
				else 
					master.getLd().getPrerequisites().getItemList().addAll(ldep.getLd().getPrerequisites().getItemList());	
				ldep.getLd().setPrerequisites(null);
			}

			Method mLdep =  MethodController.getMethod(ldep);

			if (mLdep.getOnCompletion() != null) {
				Method mMaster = MethodController.getMethod(master);
				if (mMaster.getOnCompletion() == null)
					mMaster.setOnCompletion(mLdep.getOnCompletion());
				else {
					if (mLdep.getOnCompletion().getFeedbackDescription() != null) {
						if (mMaster.getOnCompletion().getFeedbackDescription() == null)
							mMaster.getOnCompletion().setFeedbackDescription(mLdep.getOnCompletion().getFeedbackDescription());
						else
							mMaster.getOnCompletion().getFeedbackDescription().getItemList().addAll(
									mLdep.getOnCompletion().getFeedbackDescription().getItemList());
					}	
				}
			}
		}
		
		// Preparação de possíveis dummys
		EnvironmentsController.prepareEnvironmentsForExportLD(master);

		
	}
}


/* Não funciona para UTF-8 no jdk6... apenas no jdk7

private void importZIPStream2(InputStream filecontent) throws Exception {

	ZipInputStream zIn = new ZipInputStream(filecontent);

	ZipEntry zItem = null;

	TreeMap<String, byte[]> listaFiles = new TreeMap<String, byte[]>();

	while ((zItem = zIn.getNextEntry()) != null) {

		String s = String.format("Entry: %s len %d added %TD",
				zItem.getName(), zItem.getSize(),
				new Date(zItem.getTime()));
		System.out.println(s);

		listaFiles.put(zItem.getName(), Suport.toByteVector(zIn));
	}

	byte[] bManifest = listaFiles.remove("imsmanifest.xml");

	if (bManifest == null)
		throw new Exception("Arquivo [imsmanifest.xml] não encontrado!!!");

	ByteArrayInputStream bIn = new ByteArrayInputStream(bManifest);
	FileOutputStream fOut = new FileOutputStream("/home/leandro/Downloads/teste/teste.xml");
	fOut.write(bManifest);

	System.out.write(bManifest);

	this.importXMLStream(bIn);


}
private void zipImscpWithoutResumeMi2(OutputStream out, String strLdep) throws Exception {



	LdEditorWorkspace w = new LdEditorWorkspace();
	LdEditorProject ldep = clonarLdep(this.getLdPlayerProjectByIdentifier(strLdep));
	ldep.workspace = w;

	ZipOutputStream zOut;

	zOut = new ZipOutputStream(out);
	zOut.setLevel(9);
	zOut.setMethod(ZipOutputStream.DEFLATED);

	ZipEntry ze;

	ldep.getResources();
	Collection<Item> items = ldep.getAllItems();

	for (Item item : items) {
		Resource r = (Resource) item.getIdentifierref();

		if (r == null || r.getFileList().size() == 0) {
			item.setFile(null);
			item.setText(null);
			item.setLink(null);
			item.setNameFile(null);
			continue;
		}			

		ze = new ZipEntry(r.getFileList().get(0).getHref());
		ze.setMethod(ZipEntry.DEFLATED);

		try {	
			zOut.putNextEntry(ze);
		} catch (IOException e) {
			return;
		}		

		if (item.getType().equals("file")) {
			zOut.write(item.getFile());
		}
		else if (item.getType().equals("text")) {
			if (item.getText() == null)
				item.setText("");
			zOut.write(item.getText().getBytes());
		}
		item.setFile(null);
		item.setText(null);
		item.setLink(null);
		item.setNameFile(null);
	}


	ze = new ZipEntry("imsmanifest.xml");
	ze.setMethod(ZipEntry.DEFLATED);

	try {	
		zOut.putNextEntry(ze);
	} catch (IOException e) {
		return;
	}		
	putManifest(zOut, createManifest(ldep), "imscp", "imsld");

	zOut.flush();
	zOut.close();
}
 */