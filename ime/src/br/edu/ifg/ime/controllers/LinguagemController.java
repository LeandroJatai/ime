package br.edu.ifg.ime.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.imsglobal.jaxb.ld.Act;
import org.imsglobal.jaxb.ld.ActivityStructure;
import org.imsglobal.jaxb.ld.Components;
import org.imsglobal.jaxb.ld.Learner;
import org.imsglobal.jaxb.ld.LearningActivity;
import org.imsglobal.jaxb.ld.LearningDesign;
import org.imsglobal.jaxb.ld.Play;
import org.imsglobal.jaxb.ld.Roles;
import org.imsglobal.jaxb.ld.Staff;
import org.imsglobal.jaxb.ld.SupportActivity;

import br.edu.ifg.ime.ImeWorkspace;
import br.edu.ifg.ime.dao.LinguagemDAO;
import br.edu.ifg.ime.dao.DAOConnection;
import br.edu.ifg.ime.dao.PermissoesDAO;
import br.edu.ifg.ime.dao.PerspectivaDAO;
import br.edu.ifg.ime.dao.ProjetoDAO;
import br.edu.ifg.ime.dao.ServicoDAO;
import br.edu.ifg.ime.dao.UsuarioDAO;
import br.edu.ifg.ime.dto.Chave_textual;
import br.edu.ifg.ime.dto.Linguagem;
import br.edu.ifg.ime.dto.Projeto;
import br.edu.ifg.ime.dto.Servico;
import br.edu.ifg.ime.dto.Texto;
import br.edu.ifg.ime.dto.Usuario;
import br.edu.ifg.ime.ld.LdProject;
import br.edu.ifg.ime.suport.Suport;
//LinguagemController
public class LinguagemController {

	/*
	 * tipos:
	 * 1 - display tela
	 * 2 - display bd
	 * 3 - json
	 * 4 - xsd
	 *  
	 */

	public static String chaveTl = "%IME%1";
	public static String chaveDb = "%IME%2";
	public static String chaveJs = "%IME%3";
	public static String chaveXs = "%IME%4";
			
	private static LinguagemDAO ctDao = null;	
	private static ServicoDAO servicoDao = null;

	private static TreeMap<String, String> chavesPadrao = null;	
	
	private static boolean cachevalido = false;

	public static boolean isValidCache() {
		return cachevalido;
	}
	
	private static LinguagemDAO getLinguagemDAO() {

		if (ctDao == null)
			sync();

		return ctDao;    	
	}	
	private static  TreeMap<String, String> getChavesPadrao() {

		if (chavesPadrao == null)
			sync();

		return chavesPadrao;
	}

	private synchronized static void sync() {

		if (ctDao == null)
			ctDao = new LinguagemDAO();		
		if (servicoDao == null)
			servicoDao = new ServicoDAO();			

		if (chavesPadrao == null)
			chavesPadrao = getLinguagemDAO().getListaChavesPadrao();
		
		cachevalido = true;

	}	
	private synchronized static void reset() {
		ctDao = null;
		chavesPadrao = null;
		cachevalido = false;
		sync();
	}

	public static String getTextoPadrao(String chave) {	

		String ct = getChavesPadrao().get(chave);		 
		if (ct != null)
			return ct;
		else {
			reset(); 
			ct = getChavesPadrao().get(chave);	
			if (ct != null)
				return ct;
			else 
				return "{CHAVE DE TEXTO NAO ENCONTRADA}";			
		} 
	}
	
	public static String getTextoPadraoDev(String chave) {	//dev

		String ct = null; //getChavesPadrao().get(chave);		  
		if (ct != null)
			return ct;
		else {
			reset(); 
			ct = getChavesPadrao().get(chave);	


			if (ct != null) {
				if (ct.equals("{COLOCAR TEXTO}"))  
					return chave+ct;  
				else   
					return ct;
			}
			else {
				getLinguagemDAO().insert(chave, "{COLOCAR TEXTO}", 1);  

				return "{CHAVE DE TEXTO NAO ENCONTRADA}";

			}
		} 
	}

	public static TreeMap<String, String> getTextosUsuario(Usuario usuario) {	

		TreeMap<String, String> t = getLinguagemDAO().getTextos(usuario);

		if (t != null)
			return t;

		return getChavesPadrao();
	}

	public static List<Linguagem> getLinguagens() {
		return getLinguagemDAO().getLinguagens();
	}

	public static Linguagem getLinguagem(String id) {
		return getLinguagemDAO().getLinguagem(id);
	}

	public static List<Chave_textual> getChavesTextuais() {

		return getLinguagemDAO().getChavesTextuais();
	}

	public static TreeMap<String, String> getTextos(Linguagem lang) {
		return getLinguagemDAO().getTextos(lang);
	}
	public static void update(Linguagem linguagem, String[] chave,
			String[] texto) {


		if (linguagem.getId() != 0)
			getLinguagemDAO().deleteTextos(linguagem);


		try {
			getLinguagemDAO().upDate(linguagem);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Chave_textual> chaves = getChavesTextuais();
		
		for (int i = 0; i < texto.length; i++) {
			
			if (texto[i] == null || texto[i].length() == 0)
				continue;
		
			Texto t = new Texto();
			
			Chave_textual ct = null;
			for (int j = 0; j < chaves.size(); j++) {
				ct = chaves.get(j);
				if (ct.getChave().equals(chave[i]))
					break;
			}
			     
			t.setChave_textual(ct);
			t.setLinguagem(linguagem);
			t.setTexto(Suport.r(texto[i]));
			
			try {
				getLinguagemDAO().upDate(t);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
		reset();
	}
	
	public static void invalidarCache() {
		reset();
	}

	public static void delete(String idLang) {
		getLinguagemDAO().delete(idLang);
	}
	
	
	public static void transferirTextoServicosParaChavesTextuais() {
		List<Servico> lServs = UsuarioController.getServicosAutenticaveis();
		
		for (Servico s : lServs) {

			if (!getLinguagemDAO().insert("sv."+s.getServico(), s.getDescricao(), 2))
				continue;
			
			
			String ref = chaveDb+"sv."+s.getServico();
			s.setDescricao(ref);
			
			UsuarioController.updateServico(s);
		}
		
		
	}

	public static void updateSchema(Chave_textual ct) {
		
		try {
			invalidarCache();
			getLinguagemDAO().upDate(ct);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean hasSchema(String chave) {

		return getLinguagemDAO().existeChaveTextual(chave);
	}

}
