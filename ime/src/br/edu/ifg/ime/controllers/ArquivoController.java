package br.edu.ifg.ime.controllers;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TreeMap;

import br.edu.ifg.ime.dao.ArquivoDAO;
import br.edu.ifg.ime.dto.Arquivo;
import br.edu.ifg.ime.suport.ImeException;
import br.edu.ifg.ime.suport.Suport;

public class ArquivoController {

	private static ArquivoDAO arquivoDao = null;	

	public static ArquivoDAO getArquivoDAO() {

		if (arquivoDao == null)
			sync();

		return arquivoDao;    	
	}	

	private synchronized static void sync() {

		if (arquivoDao == null)
			arquivoDao = new ArquivoDAO();	

	}	

	public static Arquivo refreshBytes(Arquivo a) {

		try {
			getArquivoDAO().refreshBytes(a);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;

	}


	public static TreeMap<String, Object> getArquivos() {

		List<Arquivo> l = getArquivoDAO().selectAll();

		TreeMap<String, Object> r = new TreeMap<String, Object>();
		int nivel = 0;
		String pathAtual = "";

		for (Arquivo a: l) {

			String nome = a.getNome().startsWith("/")?a.getNome().substring(1):a.getNome();

			String path[] = nome.split("/");

			TreeMap<String, Object> aux = r;
			Object ob;
			for (int j = 0; j < path.length; j++) {	

				if (path.length == 1) {
					aux.put(path[0], a);
					continue;
				}

				ob = aux.get(path[j]);

				if (ob == null) {
					if (j == path.length-1) {
						aux.put(path[j], a);
					}
					else {
						TreeMap<String, Object> novaAux = new TreeMap<String, Object>();
						aux.put(path[j], novaAux);
						aux = novaAux;
					}

				}
				else if (ob instanceof TreeMap) {
					aux = (TreeMap<String, Object>) ob;
					continue;
				}




			}





		}


		return r;

	}

	public static byte[] getBytesArquivo(String path){
		
		try {
			return getArquivoDAO().getBytesArquivo(path);
		} catch (SQLException e) {
			return new byte[0];
		}
		
	}
	public static byte[] getBytesXSD(String xsd){

		xsd = "/_xsd/"+xsd;
		return getBytesArquivo(xsd);
		
	}
	
	public static boolean existeXSD(String xsd) {
		
		xsd = "/_xsd/"+xsd;
		
		return getArquivoDAO().exiteArquivo(xsd,true, 0);
			
	}
	
	
	public static void updateArquivo(TreeMap<String, Object> lParams) throws ImeException {

		Arquivo a = new Arquivo();

		String id = (String)lParams.get("id");
		a.setId(Integer.parseInt(id));
		if (a.getId() != 0) {
			a = getArquivoDAO().selectArquivo(a);
			a.setAlterado(new Timestamp(new GregorianCalendar().getTimeInMillis()));
		}

		a.setTitulo((String)lParams.get("arquivo.titulo"));
		a.setNome(Suport.r((String)lParams.get("arquivo.nome")));
		a.setArquivo(    (byte[]) ((TreeMap<String, Object>)lParams.get("fileData")).get("data")  );

		String texto = (String)lParams.get("arquivo.texto");


		if (a.getNome() == null || a.getNome().length() == 0)
			throw new ImeException("Um nome para o arquivo, com caminho, deve ser informado.");

		if (!a.getNome().startsWith("/"))
			a.setNome("/"+a.getNome());


		if (a.getNome().endsWith("/"))
			a.setNome(a.getNome().substring(0, a.getNome().length()-1));

		String path[] = a.getNome().substring(1).split("/");

		String pathPart = "";


		for (int p= 0; p < path.length; p++) {

			if (path[p].length() == 0)
				continue;
			
			pathPart += "/"+path[p];

			if (getArquivoDAO().exiteArquivo(pathPart,true, a.getId())) 
				throw new ImeException("Já existe um arquivo com o nome: "+pathPart);

			if (p == path.length-1 && getArquivoDAO().exiteArquivo(pathPart,false, a.getId()))
				throw new ImeException("Já existe uma pasta com o nome: "+pathPart);
		}
		a.setNome(pathPart);
		

		if (a.getId() == 0 && (texto == null || texto.length() == 0) && (a.getArquivo() == null || a.getArquivo().length == 0)) {
			throw new ImeException("Adicione um arquivo de seu computador, ou um texto/código...");
		}

		if ((a.getArquivo() == null || a.getArquivo().length == 0) && (texto != null && texto.length() != 0)) {

			a.setArquivo(texto.getBytes());

			if (a.getNome().endsWith("css"))
				a.setContent_type("text/css");
			else if (a.getNome().endsWith("js"))
				a.setContent_type("application/javascript");
			else if (a.getNome().endsWith("xsd"))
				a.setContent_type("application/xml");
			else if (a.getNome().endsWith("xml"))
				a.setContent_type("application/xml");
			else 			
				a.setContent_type("text/plain");
		}
		else if ((a.getArquivo() != null && a.getArquivo().length != 0))
				a.setContent_type(    (String) ((TreeMap<String, Object>)lParams.get("fileData")).get("content_type")  );


		try {

			try {			
				if (a.getId() != 0 && a.getArquivo() != null && a.getArquivo().length > 0) 
					getArquivoDAO().removeOidArquivo(a);
				getArquivoDAO().update(a);
			}
			catch (SQLException ee) {

			}		
			if (a.getArquivo() != null && a.getArquivo().length > 0)
				getArquivoDAO().updateBytes(a);

			lParams.put("id",String.valueOf(a.getId()));
			//	getArquivoDAO().updateArquivo(a);
		} catch (SQLException e) {
			throw new ImeException("Erro ao persistir informação");
		}



	}

	public static List<Arquivo> getSkins() {
		
		String skin = "/_skins/";
		
		return getArquivoDAO().selectAllStartWith(skin);
	}
	

}
