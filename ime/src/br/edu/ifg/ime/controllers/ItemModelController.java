package br.edu.ifg.ime.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.imsglobal.jaxb.content.File;
import org.imsglobal.jaxb.content.Resource;
import org.imsglobal.jaxb.ld.Environment;
import org.imsglobal.jaxb.ld.Item;
import org.imsglobal.jaxb.ld.ItemModel;
import org.imsglobal.jaxb.ld.Learner;
import org.imsglobal.jaxb.ld.LearningActivity;
import org.imsglobal.jaxb.ld.LearningDesign;
import org.imsglobal.jaxb.ld.LearningObject;
import org.imsglobal.jaxb.ld.Staff;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.ld.ImeObject;
import br.edu.ifg.ime.suport.LearningDesignUtils;
import br.edu.ifg.ime.suport.Suport;

public class ItemModelController {



	public static boolean dowloadFileOfItem(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String identifier = Suport.r(request, "identifier");

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);

		Object obItem = w.getObject(identifier);
		Item item = null;

		if (obItem != null && obItem instanceof Item) {
			item = (Item) obItem;

			byte[] file = item.getFile();

			if (file != null) {
				response.setHeader("contentType", "application/octet-stream");
				response.setHeader("Content-Disposition", "inline; filename="+item.getNameFile());				
				response.getOutputStream().write(file);
				return true;

			}
		}
		return false;
	}
	public static boolean requestRemoveItem(HttpServletRequest request, HttpServletResponse response) throws IOException {

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
		String identifier = Suport.r(request, "identifier");
		Object obItem = w.getObject(identifier);
		Item item = null;

		if (obItem != null && obItem instanceof Item) {
			item = (Item) obItem;
			w.excluirOfWorkspace(item);
			return true;
		}
		return false;
	}

	public static void requestUpDateItem(HttpServletRequest request, TreeMap<String, Object> lParams) throws IOException {

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);
		String titleModel = (String)lParams.get("title.model");
		String title = (String)lParams.get("title");
		String idRef = (String)lParams.get("id-ref");
		String idEnv = (String)lParams.get("id-env");
		String field = (String)lParams.get("field");

		String identifier = (String)lParams.get("identifier");

		Object obItem = null;
		Item item = null;
		List<Item> lItens = null;
		
		if (idRef == null)
			idRef = idEnv;

		Object objParent = w.getObject(idRef);

		if (objParent instanceof Environment) {
			LearningObject lo= null;
			Environment env = (Environment) objParent;
			if (identifier == null || identifier.length() == 0) {
				lo = new LearningObject();
				lo.setIdentifier(w.newIdentifier(lo, "lo"));
				lParams.put("id-ref", lo.getIdentifier());
				
				
				
				lo.setTitle(titleModel);

				lo.setParameters((String)lParams.get("lo.parameters"));
				lo.setIsvisible(lParams.get("lo.isvisible") == null?false:null);
				lo.setType((String)lParams.get("lo.tipo"));

				if (lo.getType() == null || lo.getType().length() == 0)
					lo.setType(null);

				lParams.put("identifier-lo",lo.getIdentifier());
				env.getLearningObjectOrServiceOrEnvironmentRef().add(lo);

				w.referenciar(lo.getIdentifier(), env);				

				return;
			}
		}
		else if (objParent instanceof LearningObject) {
			LearningObject lo= null;

			lo = (LearningObject) objParent;
			lo.setTitle(titleModel);
			lo.setParameters((String)lParams.get("lo.parameters"));
			lo.setIsvisible(lParams.get("lo.isvisible") == null?false:null);
			lo.setType((String)lParams.get("lo.tipo"));
			if (lo.getType() == null || lo.getType().length() == 0)
				lo.setType(null);

			String clazz = (String)lParams.get("lo.clazz");
			lo.restartClazz();
			if (clazz != null && clazz.length() > 0) {
				String clazzs[] = clazz.trim().split(" ");
				lo.restartClazz();
				for (String c : clazzs)
					lo.getClazz().add(c);
			}

			if (title == null)
				return;

			objParent = lo;

			lItens = lo.getItemList();
		}
		else {

			ItemModel im = getItemModel(w, objParent, field, true);
			if (im == null)
				return;
			im.setTitle(titleModel);	
			
			if (titleModel == null || titleModel.length() == 0) {
				if (objParent instanceof ImeObject) {
					ImeObject imeObj = (ImeObject) objParent;
					//imeObj.validateImsLd();
					return;
				}  
			} 
			
			if (title == null)
				return;
			//objParent = im;
			lItens = im.getItemList(); 
		}

		if (identifier != null)
			obItem = w.getObject(identifier);

		if (obItem != null && obItem instanceof Item) {
			item = (Item) obItem;
		}
		else { 
			item = new Item();
			lItens.add(item);
			item.setIdentifier(w.newIdentifier(item, "item"));
			w.referenciar(item.getIdentifier(), objParent);
		}


		fillItem(item, w, lParams);

		return; 
	}
	
	
	public static void fillItem(Item item, ImeWorkspace w,
			TreeMap<String, Object> lParams) {
		if (item.getIdentifierref() == null) {
			Resource r = new Resource();
			r.setIdentifier(w.newIdentifier(r, "resource"));
			r.setType("webcontent");
			item.setIdentifierref(r);
		}
		item.setTitle((String)lParams.get("title"));

		if (((String)lParams.get("parameters")) != null && ((String)lParams.get("parameters")).length() > 0) 
			item.setParameters((String)lParams.get("parameters"));
		else 
			item.setParameters(null);

		if (lParams.containsKey("isvisible"))
			item.setIsvisible(null); // padrão null = true... 
		else 
			item.setIsvisible(false);

		if (((String)lParams.get("type")).equals("text")) {
			item.setFile(null);
			item.setLink(null);
			if (((String) lParams.get("dadosItem")) != null && ((String) lParams.get("dadosItem")).length() >= 0)
				item.setText((String) lParams.get("dadosItem"));
			else
				item.setText(null);

			File f = new File();
			f.setHref(item.getIdentifier()+".html");
			((Resource)item.getIdentifierref()).getFileList().clear();
			((Resource)item.getIdentifierref()).getFileList().add(f);
			((Resource)item.getIdentifierref()).setHref(item.getIdentifier()+".html");
		}

		else if (((String)lParams.get("type")).equals("link")) {
			item.setFile(null);
			if (((String) lParams.get("dadosItem")) != null && ((String) lParams.get("dadosItem")).length() >= 0)
				item.setLink((String) lParams.get("dadosItem"));
			else
				item.setLink(null);
			item.setText(null);


			((Resource)item.getIdentifierref()).setHref(item.getLink());
		}

		else if (((String)lParams.get("type")).equals("file")) {
			item.setText(null);
			item.setLink(null);

			TreeMap<String, Object> file = (TreeMap<String, Object>) lParams.get("dadosItem");

			if (file != null) {

				if (file.get("data") != null) {


					byte[] data = (byte[])file.get("data");		

					item.setFile(data);
					item.setNameFile((String)file.get("name"));


					File f = new File();
					f.setHref(item.getNameFile());
					((Resource)item.getIdentifierref()).getFileList().clear();
					((Resource)item.getIdentifierref()).getFileList().add(f);
					((Resource)item.getIdentifierref()).setHref(item.getNameFile());

				}
			}

		}
	}
	/*
	public static void requestUpDateItemModel(HttpServletRequest request) {

		String ldep = Suport.r(request, "ldep");
		String title = Suport.r(request, "title");
		String aba = Suport.r(request, "aba");

		ItemModel im = getItemModel(request);

		if (im == null)
			im = newItemModel(request);

		if (im == null)
			return;

		im.setTitle(title);

	}
	 */
	/*
	public static Item getItemByIdentifier(HttpServletRequest request) {

		String identifier = Suport.r(request, "identifier");
		if (identifier == null)
			return null;

		return null;




	}*/
	public static ItemModel getItemModel(HttpServletRequest request) {

		String idRef = Suport.r(request, "id-ref");
		String field = Suport.r(request, "field");

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);

		Object objParent = w.getObject(idRef);

		return getItemModel(w, objParent, field, false);

	}

	private static ItemModel getItemModel(ImeWorkspace w, Object objParent, String field, boolean criarNovoSeNaoExistir) {

		if (objParent == null)
			return null;

		Field[] lFields = objParent.getClass().getDeclaredFields(); // lista de atributos do objeto item

		if (field.indexOf("-") != -1) {

			String fieldAtual = field.substring(0,field.indexOf("-"));


			for (Field f: lFields) {

				if (!f.getName().equals(fieldAtual))
					continue;

				try {
					Object objField = objParent.getClass().getMethod( Suport.mGetOfField(f)  ).invoke(objParent);

					if (objField == null) {

						if (!criarNovoSeNaoExistir)
							return null;

						Class obClass = f.getType();

						objField = obClass.newInstance(); 

						String identifier = null;
						try {
							identifier = LearningDesignUtils.getIdentifierForImport(objField);
							if (identifier == null) 
								identifier = w.newIdentifier(objField,objField.getClass().getSimpleName().toUpperCase());
							objField.getClass().getMethod("setIdentifier", String.class).invoke(objField, new Object[]{ identifier});			
						}
						catch (Exception e) {
							identifier = null;
						}



						objParent.getClass().getMethod(Suport.mSetOfField(f), obClass).invoke(objParent, new Object[]{ objField });			

						//	objField = objParent.getClass().getMethod( Suport.mGetOfField(f)  ).invoke(objParent);

					}



					return getItemModel(w,  objField  , field.substring(field.indexOf("-")+1), criarNovoSeNaoExistir);


				} catch (Exception e) {
					return null;
				}

			}

		} 

		for (Field f: lFields) {

			if (!f.getType().getCanonicalName().equals("org.imsglobal.jaxb.ld.ItemModel") || !f.getName().equals(field))
				continue;
			Object objField = null;

			try {
				objField = objParent.getClass().getMethod(Suport.mGetOfField(f)).invoke(objParent);

				if (objField == null && criarNovoSeNaoExistir)
					objField = new ItemModel();
				else if (objField == null && !criarNovoSeNaoExistir)
					return null;

				objParent.getClass().getMethod(Suport.mSetOfField(f), ItemModel.class).invoke(objParent, new Object[]{ objField });			

			} catch (Exception e) {
				return null;
			}

			return (ItemModel) objField;
		}
		return null;
	}


	public static ItemModel newItemModel(HttpServletRequest request) {

		String idRef = Suport.r(request, "id-ref");
		String field = Suport.r(request, "field");

		ImeWorkspace w = ImeWorkspace.getImeWorkspace(request);

		Object objParent = w.getObject(idRef);

		return getItemModel(w, objParent, field, true);

	}


}
