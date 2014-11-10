package br.edu.ifg.ime.suport;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imsglobal.jaxb.ld.Environment;
import org.imsglobal.jaxb.ld.ItemModel;

import br.edu.ifg.ime.ld.ImeObject;

public class Suport {

	public static final int LEFT = 37;
	public static final int RIGHT = 39;

	public static final int diasMes[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};	
	public static final String monthNames[] = { "JANEIRO", "FEVEREIRO", "MARÇO", "ABRIL", "MAIO", "JUNHO", "JULHO", "AGOSTO", "SETEMBRO", "OUTUBRO", "NOVEMBRO", "DEZEMBRO"};

	public static float round(float value, int precision) {

		double vFloat = value;

		for (int i = 0; i < precision; i++) {
			vFloat *= 10;
		}

		long vInt = Math.round(vFloat);
		vFloat = (double) vInt;
		for (int i = 0; i < precision; i++) {
			vFloat /= 10;
		}

		return (float) vFloat;
	}

	public static String roundToString(float value, int size, int precision) {

		double vFloat = value;

		for (int i = 0; i < precision; i++) {
			vFloat *= 10;
		}

		long vInt = Math.round(vFloat);
		vFloat = (double) vInt;
		for (int i = 0; i < precision; i++) {
			vFloat /= 10;
		}

		String s = Float.toString((float) vFloat);
		if (s.length() == 0) {
			s = "0";
		}

		s = s.substring(0, s.indexOf(".")) + "," + s.substring(s.indexOf(".") + 1);
		if (s.length() - s.indexOf(",") < precision) {
			s = s.substring(0, s.indexOf(",") + precision);
		}

		while (s.length() - s.indexOf(",") < precision+1) {
			s += "0";
		}

		while (s.length() < size) {
			s = " " + s;
		}
		return s;
	}
	public static String complete(String s,String complemento,int tamanho, int lado) {

		if (lado == LEFT) 
			while (s.length() < tamanho)
				s = complemento + s;
		else if (lado == RIGHT)
			while (s.length() < tamanho)
				s += complemento;


		return s;
	}
	public static float strToFloat(String s) throws Exception{
		s = "0" + s;
		int i = s.indexOf(",");
		if (i != -1) {
			String ss[] = s.split(",", 2);
			s = ss[0] + "." + ss[1];
		}
		return Float.valueOf(s).floatValue();
	}

	public static boolean validaCpf(String cpf) {
		if (cpf.length() != 11)
			return false;

		String digVer = "";
		int soma1 = 0, soma2 = 0;
		int resto1 = 0, resto2 = 0;

		digVer = cpf.substring(9, 11);
		int c = 11;			

		for (int i = 0; i < (cpf.length() - 1); i++, c--) {
			if (c == 2) {
				soma2 += (Integer.parseInt (cpf.substring(i, i+1)) * c);
			}

			else {
				soma1 += (Integer.parseInt (cpf.substring(i, i+1)) * (c - 1));
				soma2 += (Integer.parseInt (cpf.substring(i, i+1)) * c);
			}
		}

		//Calculando os dígitos verificadores
		resto1 = soma1 % 11;
		resto2 = soma2 % 11;

		if (resto1 < 2){
			resto1 = 0;
		}

		else {
			resto1 = 11 - resto1;
		}

		if (resto2 < 2){
			resto2 = 0;
		}

		else {
			resto2 = 11 - resto2;
		}

		return (digVer.equals (String.valueOf (resto1) + "" + String.valueOf (resto2)));
	}

	public static String dateToStrBR(GregorianCalendar g) {
		if (g != null) {
			String 	s = Suport.complete(String.valueOf(g.get(GregorianCalendar.DAY_OF_MONTH)), "0", 2, Suport.LEFT) + "/";
			s+= Suport.complete(String.valueOf(g.get(GregorianCalendar.MONTH)+1), "0", 2, Suport.LEFT) + "/";
			s+= String.valueOf(g.get(GregorianCalendar.YEAR));
			return s;
		}
		return "";
	}

	public static String dateToStrBROnlyTime(GregorianCalendar g) {
		if (g != null) {
			String 	s = Suport.complete(String.valueOf(g.get(GregorianCalendar.HOUR_OF_DAY)),"0",2,LEFT) + ":";
			s+= Suport.complete(String.valueOf(g.get(GregorianCalendar.MINUTE)),"0",2,LEFT) + ":";
			s+= Suport.complete(String.valueOf(g.get(GregorianCalendar.SECOND)),"0",2,LEFT);
			return s;
		}
		return "";
	}

	public static GregorianCalendar strBRToDate(String s) {
		try {
			String dataHora[] = s.split(" ");

			String ss[] = dataHora[0].split("/");
			int a, m, d, h, mm, sss;
			a = Integer.parseInt(ss[2]);
			m = Integer.parseInt(ss[1])-1;
			d = Integer.parseInt(ss[0]);

			h = 0; mm = 0; sss = 0;
			if (dataHora.length > 1) { 
				ss = dataHora[1].split(":");
				h = Integer.parseInt(ss[0]);
				mm = Integer.parseInt(ss[1]);
				sss = Integer.parseInt(ss[2]);
			}

			GregorianCalendar g = new GregorianCalendar(a,m,d,h,mm,sss);	
			return g;
		} catch (Exception e) {
			return null;
		}
	}

	public static GregorianCalendar strToDate(String s) {
		try {
			String dataHora[] = s.split(" ");

			String ss[] = dataHora[0].split("/");
			int a, m, d, h, mm, sss;
			a = Integer.parseInt(ss[0]);
			m = Integer.parseInt(ss[1])-1;
			d = Integer.parseInt(ss[2]);

			h = 0; mm = 0; sss = 0;
			if (dataHora.length > 1) { 
				ss = dataHora[1].split(":");
				h = Integer.parseInt(ss[0]);
				mm = Integer.parseInt(ss[1]);
				sss = Integer.parseInt(ss[2]);
			}

			GregorianCalendar g = new GregorianCalendar(a,m,d,h,mm,sss);	
			return g;
		} catch (Exception e) {
			return null;
		}
	}

	public static Timestamp getTimestampNow() {
		return new Timestamp(new GregorianCalendar().getTimeInMillis());
	}

	public static Timestamp strBRToTimeStamp(String s) {
		try {
			String dataHora[] = s.split(" ");

			String ss[] = dataHora[0].split("/");
			int a, m, d, h, mm, sss;
			a = Integer.parseInt(ss[2]);
			m = Integer.parseInt(ss[1])-1;
			d = Integer.parseInt(ss[0]);

			h = 0; mm = 0; sss = 0;
			if (dataHora.length > 1) { 
				ss = dataHora[1].split(":");
				h = Integer.parseInt(ss[0]);
				mm = Integer.parseInt(ss[1]);
				sss = Integer.parseInt(ss[2]);
			}

			Timestamp g = new Timestamp(new GregorianCalendar(a,m,d,h,mm,sss).getTimeInMillis());	
			return g;

		} catch (Exception e) {
			return null;
		}
	}

	public static void setDayOfMonth(GregorianCalendar g, int day) {

		if (day > diasMes[g.get(GregorianCalendar.MONTH)])
			day = diasMes[g.get(GregorianCalendar.MONTH)];
		g.set(GregorianCalendar.DAY_OF_MONTH, day);
	}

	public static String dateTimeYYYYMMDDHHMMSS() {
		String arquivo = "";

		GregorianCalendar g = new GregorianCalendar();
		//g.set(GregorianCalendar.MINUTE, 0);
		//	g.set(GregorianCalendar.SECOND, 0);


		String 	s = Suport.complete(String.valueOf(g.get(GregorianCalendar.YEAR)), "0", 2, Suport.LEFT) +
				Suport.complete(String.valueOf(g.get(GregorianCalendar.MONTH)+1), "0", 2, Suport.LEFT) +
				String.valueOf(g.get(GregorianCalendar.DAY_OF_MONTH));

		arquivo += s + 

				Suport.complete(String.valueOf(g.get(GregorianCalendar.HOUR_OF_DAY)),"0",2,LEFT)+
				Suport.complete(String.valueOf(g.get(GregorianCalendar.MINUTE)),"0",2,LEFT)+
				Suport.complete(String.valueOf(g.get(GregorianCalendar.SECOND)),"0",2,LEFT);

		return arquivo;
	}

	public static String timeStampToStr(Timestamp time) {
		String s = "";
		GregorianCalendar c = new GregorianCalendar();
		if (time != null){
			c.setTime(time);

			s = dateToStrBR(c);
		}

		return s;
	}

	public static String timeStampToStrTime(Timestamp time) {
		String s = "";
		GregorianCalendar c = new GregorianCalendar();
		if (time != null){
			c.setTime(time);

			s = dateToStrBROnlyTime(c);
		}

		return s;
	}

	public static void validIntranet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (!isIntranet(request))
			response.sendRedirect("/portal/sislegisold/errorIntranet.jsp");


	}

	public static boolean isIntranet(HttpServletRequest request) {

		if (request == null)
			return false;

		return (	
				request.getRemoteAddr().substring(0, 8).equals("10.3.163") ||
				request.getRemoteAddr().equals("127.0.0.1") ||
				request.getRemoteAddr().equals("189.42.27.231") ||
				request.getRemoteAddr().equals("189.42.27.232")

				);
	}

	public static Object getObjectSession(String name, String classe, HttpServletRequest request) {


		Object ob =  request.getSession().getAttribute(name);
		Class cl;

		if (ob != null) return ob;

		try {
			try {

				ob = Class.forName(classe).getConstructors()[0].newInstance();

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}


			request.getSession().setAttribute(name, ob);


		} catch (ClassNotFoundException e) {
			ob = null;
		}

		return ob;
	}

	public static String utfToIso(String s) {

		if (s == null)
			return null;

		try {
			return new String(s.getBytes("UTF-8"),0,s.getBytes("UTF-8").length,"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public static String utfToCp437(String s) {

		if (s == null)
			return null;

		try {
			return new String(s.getBytes("UTF-8"),0,s.getBytes("UTF-8").length,"Cp437");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public static String IsoToUtf(String s) {

		if (s == null)
			return null;
		try {
			return new String(s.getBytes("ISO-8859-1"),0,s.getBytes("ISO-8859-1").length,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public static String r(HttpServletRequest request, String var) {

		String val = request.getParameter(var);
		if (val == null) {
			val = (String) request.getAttribute(var);

			if (val == null) {

				TreeMap<String, Object> lParams = ((TreeMap<String, Object>)request.getAttribute("lParams"));

				if (lParams != null) {

					if (lParams.get(var) instanceof String) 
						val = (String)lParams.get(var);
				}
			}	
			return val;
		} else if (val.equals("null"))
			return null;


		return IsoToUtf(val);

	}
	public static byte[] rb(HttpServletRequest request, String var) {

		String val = request.getParameter(var);
		if (val == null) {
			val = (String) request.getAttribute(var);

			if (val == null) {

				TreeMap<String, Object> lParams = ((TreeMap<String, Object>)request.getAttribute("lParams"));

				if (lParams != null) {

					if (lParams.get(var) instanceof byte[]) 
						return (byte[])lParams.get(var);
				}
			}	 
		} else if (val.equals("null"))
			return null;


		return IsoToUtf(val).getBytes();

	}
	public static String[] rs(HttpServletRequest request, String var) {

		String val[] = request.getParameterValues(var);
		if (val == null) {
			val = (String[]) request.getAttribute(var);

			if (val == null) {

				TreeMap<String, Object> lParams = ((TreeMap<String, Object>)request.getAttribute("lParams"));

				if (lParams != null) {

					if (lParams.get(var) instanceof String[]) 
						val = (String[])lParams.get(var);
				}
			}	
			return val;
		} else if (val.equals("null"))
			return null;


		return val;

	}
	public static String r(String var) {

		return IsoToUtf((var));

	}

	public static String getMessageWarning(HttpServletRequest request) {

		String msg = (String)request.getAttribute("msg");

		if (msg == null) {

			msg = request.getParameter("msg");

			if (msg == null)
				return "<span class=\"message_warning\" style=\"display: none;\"></span>";
		}

		if (msg.length()== 0)
			return "<span class=\"message_warning\" style=\"display: none;\"></span>";

		msg = "<span class=\"message_warning\">"+msg+"</span>";
		//System.out.println(msg);

		return msg;


	}
	public static String message(String msg) {

		msg = "<span class=\"message_warning\">"+msg+"</span>";
		//System.out.println(msg);

		return msg;
	}

	public static String mGetOfField(Field f) {
		String get = "get";
		if (f.getName().startsWith("_"))
			get = "get" + f.getName().substring(1, 2).toUpperCase() + f.getName().substring(2);
		else
			get = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
		return get;
	}
	public static String mSetOfField(Field f) {
		String get = "set";
		if (f.getName().startsWith("_"))
			get = "set" + f.getName().substring(1, 2).toUpperCase() + f.getName().substring(2);
		else
			get = "set" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
		return get;
	}

	public static void set(Object ob, Field f, Object value) throws Exception {
		ob.getClass().getMethod(Suport.mSetOfField(f), Object.class).invoke(ob, new Object[]{ value });			
	}

	public static byte[] toByteVector(InputStream in) {

		ArrayList<byte[]> result = new ArrayList<byte[]>();

		byte[] buffer = new byte[1024];

		int countRead = 0;

		try {
			while ((countRead = in.read(buffer)) > 0) {

				if (countRead == buffer.length) {
					result.add(buffer);
					buffer = new byte[1024];
					continue;
				}

				byte[] aux = new byte[countRead];
				result.add(aux);

				for (int i = 0; i < aux.length; i++) {
					aux[i] = buffer[i];
				}

				buffer = new byte[1024];

			}

		} catch (IOException e) {
			return null;
		}

		if (result.size() == 0)
			return null;

		countRead = 0;
		for (byte[] buf: result)
			countRead += buf.length;

		buffer = new byte[countRead]; 

		int pos = 0;
		for (byte[] bs : result) {
			for (byte b : bs) {
				buffer[pos++] = b;
			}
		}
		return buffer;		
	}


	public static List<Object> listaDeObjetos(Object ob, Class[] stopClass) {

		return listaDeObjetos(ob, stopClass, null);
	}

	public static List<Object> listaDeObjetos(Object ob, Class[] stopClass, Class[] attrComObjsDasClasses) { 

		List<Object> result = new ArrayList<Object>();

		listaDeObjetos(ob, result, stopClass, attrComObjsDasClasses);

		return result;
	}


	private static void listaDeObjetos(Object ob, List<Object> lista, Class[] stopClass, Class[] attrComObjsDasClasses) { // procurar em ob, adicionar em lista

		if (lista.contains(ob))
			return;

		if (attrComObjsDasClasses == null)
			lista.add(ob);

		Field[] lFields = ob.getClass().getDeclaredFields();

		for (Field f : lFields) {

			if (f.getGenericType().toString().equals("java.util.List<java.lang.String>"))
				continue;



			String get = Suport.mGetOfField(f);
			//System.out.println(get+" - ");

			Object o = null;
			try {
				o = ob.getClass().getMethod(get).invoke(ob);
			} catch (Exception e) {
			}

			if (o == null)
				continue;


			if (stopClass != null) {
				boolean flagContem = false;		

				for (Class item : stopClass) {		

					if (o.getClass() == item) {
						flagContem = true;
						break;
					}
					//System.out.println(o.getClass().getCanonicalName()+" - "+item.getCanonicalName());

				}
				if (flagContem)
					continue;			
			}

			if (attrComObjsDasClasses != null) {

				boolean flagContem = false;		

				for (Class item : attrComObjsDasClasses) {		

					if (o.getClass() == item) {
						flagContem = true;
						break;
					}

				}
				if (flagContem)
					lista.add(ob);
			}



			if (o instanceof Collection) {
				for (Object item: ((Collection<Object>) o)) {		


					if (stopClass != null) {
						boolean flagContem = false;		

						for (Class item1 : stopClass) {		

							if (o.getClass() == item1) {
								flagContem = true;
								break;
							}
							//System.out.println(o.getClass().getCanonicalName()+" - "+item.getCanonicalName());

						}
						if (flagContem)
							break;	
						else 

							listaDeObjetos(item, lista, stopClass, attrComObjsDasClasses);
					}
					else

						listaDeObjetos(item, lista, stopClass, attrComObjsDasClasses);					
				}
			}
			else if (o instanceof Map) {
				for (Object item: ((Map<Object, Object>) o).values()) {
					listaDeObjetos(item, lista, stopClass, attrComObjsDasClasses);					
				}
			}
			else {
				listaDeObjetos(o, lista, stopClass, attrComObjsDasClasses);
			}

		}






	}

	public static boolean isByteArrayIsFileText(byte[] data) {

		//ByteArrayInputStream bIn = new ByteArrayInputStream(data);
		//InputStreamReader iSr = new InputStreamReader(bIn);
		//BufferedReader bufReader = new BufferedReader(iSr);


		try {
			return Charset.forName("UTF-8").newEncoder().canEncode(new String(data,"UTF-8"));
		} catch (Exception e) {
			return false;
		}



	}


	public static String getStringIfByteArrayIsFileText(byte[] data) {

		ByteArrayInputStream bIn = new ByteArrayInputStream(data);
		InputStreamReader iSr = new InputStreamReader(bIn);
		BufferedReader bufReader = new BufferedReader(iSr);

		String texto = "";
		String linha = "";
		try {
			while ((linha = bufReader.readLine()) != null) {
				texto += linha + "\r\n";
			}
		}
		catch (Exception e) {
			return null;
		}



		return texto;



	}

	public static String join(String s[], int beginIndex, int endIndex, String meio )  {

		String result = "";

		if (endIndex > s.length)
			endIndex = s.length;

		boolean flagMeio = false;

		if (s != null)
			for (int i = beginIndex; i < endIndex; i++) {

				if (s[i].length() == 0)
					continue;

				if (flagMeio) {
					result += meio;
				}
				else flagMeio = true;

				result += s[i];
			}
		return result;

	}

	public static String organizarIdentifer(String identifier) {

		ArrayList<String> lPartesIdentifer = new ArrayList<String>();
		String partsId[] = identifier.split("-");

		for (String p: partsId) {

			if (p.indexOf(".") == -1) {
				long tmpNum = 0;
				try {
					tmpNum = Long.parseLong(p);
				}
				catch (NumberFormatException e) {
					try {
						tmpNum = Long.parseLong(p,16);
					}
					catch (NumberFormatException ee) {
						BigInteger bi ;
						try {
							bi = new BigInteger(p,16);
						}
						catch (Exception eee) {
							lPartesIdentifer.add(p);
						}
					}
				}
				continue;
			}
			String pp[] = p.split("\\.");

			for (String ppp: pp) {
				long tmpNum = 0;
				try {
					tmpNum = Long.parseLong(ppp);
				}
				catch (NumberFormatException e) {
					try {
						tmpNum = Long.parseLong(ppp,16);
					}
					catch (NumberFormatException ee) {
						BigInteger bi ;
						try {
							bi = new BigInteger(ppp,16);
						}
						catch (Exception eee) {
							if (ppp != pp[pp.length-1])
								lPartesIdentifer.add("."+ppp);
						}
					}
				}
			}				
		}
		partsId = (String[]) lPartesIdentifer.toArray(new String[lPartesIdentifer
		                                                         .size()]);


		identifier = Suport.join(partsId, 0, partsId.length, "");
		return identifier;
	}

	public static boolean existErrorOrWarningIn(ImeObject imgObject){
		return imgObject != null && (imgObject.getERRORs().size() > 0 || imgObject.getWARNINGs().size() > 0);
	}

	public static boolean existWarningIn(ImeObject imgObject){
		return imgObject != null && (imgObject.getWARNINGs().size() > 0);
	}
	

	public static boolean existErrorIn(ImeObject imgObject){
		return imgObject != null && (imgObject.getERRORs().size() > 0);
	}
	


}