package br.edu.ifg.ime.dao;

import java.beans.PropertyVetoException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;









import br.edu.ifg.ime.dto.Arquivo;
import br.edu.ifg.ime.dto.Dto;


public class CopyOfDAOConnection  implements Runnable{

	public static final int IGUAL = 1;
	public static final int CONTEM = 2;
	public static final int INICIA = 3;
	public static final int TERMINA = 4;

	public static final int ERROR_EXECUTE = -1;
	public static final int OK_EXECUTE = -2;
	public static final int UNIQUE_ERROR_EXECUTE = -3;

	private static ComboPooledDataSource cpds = null;

	private static String driver = "org.postgresql.Driver";
	private static String jdbcUrl = "jdbc:postgresql://10.3.161.2:5432/ime";
	private static String usuario = "postgres";
	private static String senha = "PostSky2.34";
	//private static String senha = "PostSky2.34";

	public final static boolean dev = false;


	private static CopyOfDAOConnection connect = null;

	private List<Connection> singleConnectLivres = null;
	private List<Connection> singleConnectDevolvidas = null;

	private boolean flagConnect = true;

	private Thread th;

	private CopyOfDAOConnection() {
		singleConnectLivres = new ArrayList<Connection>();
		singleConnectDevolvidas = new ArrayList<Connection>();

	}


	private static void setVariaveis() {
		if (dev) {
			driver = "org.postgresql.Driver";
			jdbcUrl = "jdbc:postgresql://localhost:5432/ime";
			usuario = "postgres";
			senha = "infoCmj";
		}
		else {
			driver = "org.postgresql.Driver";
			jdbcUrl = "jdbc:postgresql://10.3.161.2:5432/ime";
			usuario = "postgres";
			senha = "PostSky2.34";
		}
	}

	private synchronized static void syncSingle() {

		if (connect == null) {
			setVariaveis();
			connect = new CopyOfDAOConnection();
			connect.th = new Thread(connect);
			connect.th.start();
		}

	}

	private synchronized static void sync() {

		if (cpds != null)
			return;

		try {		
			setVariaveis();


			cpds = new ComboPooledDataSource();
			cpds.setDriverClass(driver);
			cpds.setJdbcUrl(jdbcUrl);
			cpds.setUser(usuario);
			cpds.setPassword(senha);

			cpds.setInitialPoolSize(1);
			cpds.setMinPoolSize(1);
			cpds.setMaxPoolSize(30);
			cpds.setAcquireIncrement(1);
			cpds.setMaxIdleTime(60);
			cpds.setMaxStatementsPerConnection(20);

			cpds.setDebugUnreturnedConnectionStackTraces(true);

			/*
			cpds.setInitialPoolSize(1);

			//cpds.setPreferredTestQuery("select id from grp_servicos;");

			cpds.setMinPoolSize(1);			
			cpds.setMaxPoolSize(10);
			cpds.setMaxStatementsPerConnection(100);
			cpds.setCheckoutTimeout(1800);
			cpds.setAcquireIncrement(2);
			cpds.setIdleConnectionTestPeriod(300);
			 */
			/*
			cpds.setMaxIdleTime(20);				
			cpds.setMaxIdleTimeExcessConnections(10);
			cpds.setMaxStatementsPerConnection(100);
			cpds.setTestConnectionOnCheckin(true);
			cpds.setAcquireRetryAttempts(5);
			cpds.setAcquireRetryDelay(3);
			cpds.setBreakAfterAcquireFailure(false);
			cpds.setMaxConnectionAge(0);
			cpds.setUnreturnedConnectionTimeout(30);
			cpds.setDebugUnreturnedConnectionStackTraces(false);*/

			/*
			cpds.setMinPoolSize(1);
			cpds.setMaxPoolSize(10);
			cpds.setAcquireIncrement(2);
			cpds.setMaxIdleTime(20);				
			cpds.setMaxIdleTimeExcessConnections(10);
			cpds.setMaxStatementsPerConnection(100);
			cpds.setIdleConnectionTestPeriod(300);
			cpds.setTestConnectionOnCheckin(true);
			cpds.setAcquireRetryAttempts(5);
			cpds.setAcquireRetryDelay(3);
			cpds.setBreakAfterAcquireFailure(false);
			cpds.setMaxConnectionAge(0);
			cpds.setCheckoutTimeout(1800);
			cpds.setUnreturnedConnectionTimeout(30);
			cpds.setDebugUnreturnedConnectionStackTraces(false);
			 */


			//cpds.setIdleConnectionTestPeriod(60);
			//cpds.setMaxStatements(0);
			//cpds.setCheckoutTimeout(1000);



		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	public static Connection getc3p0Connection() {

		if (cpds == null)
			sync();

		try {			
			return cpds.getConnection();
		} catch (SQLException e) {
			return null;
		}    	
	}

	public synchronized static Connection getConnection() {

		if (connect == null)
			syncSingle();

		try {			
			Connection conn = null;
			int timeout = 30;
			while (timeout > 0) {

				synchronized (connect.singleConnectLivres) {

					if (connect.singleConnectLivres.size() > 0) {
						conn = connect.singleConnectLivres.remove(0);
						connect.singleConnectLivres.notify();
						break;
					}
					else {
						connect.singleConnectLivres.notify();
						connect.singleConnectLivres.wait(1000);
						timeout--;
					}
				}

			}
			return conn;

		} catch (Exception e) {
			return null;
		}    	
	}

	public void run() {

		Connection singleConnect;

		while (true) {

			try {

				synchronized (singleConnectLivres) {
					System.out.println(singleConnectLivres.size() + " - "+ singleConnectDevolvidas.size());

					if (singleConnectLivres.size() < 5) {
						this.flagConnect = true;
						Class.forName(driver);
						singleConnect = DriverManager.getConnection(
								jdbcUrl, usuario, senha);

						singleConnectLivres.add(singleConnect);
						continue;
					}
					else {
						if (singleConnectDevolvidas.size() > 0) {
							singleConnect = singleConnectDevolvidas.remove(0);
							try {
								if (singleConnectLivres.size() > 30) {
									System.out.println(singleConnectLivres.size());
									singleConnect.close();
									continue;
								}
								else {
									ResultSet rs = null;
									String sql = "select id from grp_servicos order by id limit 1;";
									PreparedStatement pStm;
									pStm = singleConnect.prepareStatement(sql);
									rs = pStm.executeQuery();

									if (rs != null) {		

										System.out.println("Retorno válido");
										rs.close();
										pStm.close();
										singleConnectLivres.add(singleConnect);
										continue;
									} else {
										singleConnect.close();
									}	

								}

							}
							catch (Exception e) {
								continue;
							}



						}
					}
					singleConnectLivres.wait(10000);
				}


			} catch (Exception e) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
				}
				continue;
			}
			continue;
		}

	}


	public static PreparedStatement createPreparedStatement(String sql) {


		try {
			return getConnection().prepareStatement(sql);
		} catch (SQLException e) {
			return null;
		}    	
	}

	public static void closec3p0Objects(ResultSet rs, Statement stm) throws SQLException {

		if (rs != null)
			rs.close();

		Connection con = stm.getConnection();
		stm.close();
		con.close();

	}
	public static void closeObjects(ResultSet rs, Statement stm) throws SQLException {

		if (rs != null)
			rs.close();

		Connection con = stm.getConnection();
		stm.close();

		synchronized (connect.singleConnectLivres) {
			connect.singleConnectDevolvidas.add(con);
		}


	}


	public void close() {   

		try {
			DataSources.destroy(cpds);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}    

	@SuppressWarnings("unused")
	private void  upDateByteArray( String sql, byte[] dados) throws SQLException {

		PreparedStatement ps = getConnection().prepareStatement(sql);

		ps.setBytes(1, dados);
		ps.setBoolean(2, dados != null);
		ps.executeUpdate();    	
		ps.close();

	}

	@SuppressWarnings("unused")
	private byte[] selectByteArray( String sql) throws SQLException {
		PreparedStatement ps = getConnection().prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getBytes(1);
	}

	public static int executeInsertUpDateDelete(String sql) throws SQLException  {


		PreparedStatement pStm = createPreparedStatement(sql);

		pStm.execute();

		if (sql.indexOf("RETURNING") != -1) {

			ResultSet rs = pStm.getResultSet();

			int r = 0;

			if (rs.next())
				r = rs.getInt("returnId");
			else 
				r = ERROR_EXECUTE;


			closeObjects(rs, pStm);

			return r;

		} else {

			closeObjects(null, pStm);

			return OK_EXECUTE;
		}

	}


	public static String constructSQLUpDate(Object ob) {

		try {
			String s = "update " + ob.getClass().getSimpleName().toLowerCase() + " set ";

			Field[] f = ob.getClass().getDeclaredFields();
			Method[] mt = ob.getClass().getDeclaredMethods();

			for (int i = 0; i < f.length; i++) {

				if (f[i].getName().startsWith("wmx_")) //tranziente
					continue;
				else if (f[i].getName().startsWith("manifesto")) //arquivo Midia
					continue;
				else if (f[i].getName().startsWith("arquivo")) //arquivo Midia
					continue;
				else if (f[i].getName().startsWith("l_")) //arquivo lista de Objetos
					continue;
				/*else if (f[i].getName().indexOf("STATE") != -1)
					continue;
				else if (f[i].getName().indexOf("onLine") != -1)
					continue;
				else if (f[i].getType().getName().indexOf("Collection") != -1)
					continue;*/

				s += "\"" + f[i].getName() + "\" = ";

				if (isString(f[i].getType())) {

					String aux[] = mt[getPos(f[i], mt)].invoke(ob,  new Object[0]).toString().split("'");
					s += "'";
					for (int j = 0; j < aux.length; j++) {
						if (j!=0)
							s += "''";
						s += aux[j];
					}
					s += "'";	
				} else if (isBoolean(f[i].getType())) {
					s += "'" + mt[getPos(f[i], mt)].invoke(ob,  new Object[0]).toString() + "'";
				} else if (isGregorianCalendar(f[i].getType())) {
					GregorianCalendar g = (GregorianCalendar) (mt[getPos(f[i], mt)].invoke(ob,  new Object[0]));
					if (g != null) {
						s += "'" + g.toString() + "'";
					} else {
						s += "null";
					}
				} else if (isTimeStamp(f[i].getType())) {
					Timestamp g = (Timestamp) (mt[getPos(f[i], mt)].invoke(ob,  new Object[0]));
					if (g != null) {
						s += "'" + g.toString() + "'";
					} else {
						s += "now()";
					}
					//	} else if (isAcao(f[i].getType())) {
					//		s += Acao.getIntAcao((Acao)mt[getPos(f[i], mt)].invoke(ob,  new Object[0]));

				} 
				else if (isFloatORInteger(f[i].getType())) {
					s += mt[getPos(f[i], mt)].invoke(ob,  new Object[0]);
				} else if (isDto(f[i].getType())) {
					Dto dto = (Dto) mt[getPos(f[i], mt)].invoke(ob,  new Object[0]);
					if (dto != null && dto.getId() != 0)
						s += (dto).getId();                
					else 
						s += "null";
				}
				s += ", ";
			}
			s = s.substring(0,s.length()-2);

			s += " where \"id\" = " + ((Dto) ob).getId() + " RETURNING \"id\" AS \"returnID\";";

			return s;
		} catch (Exception ex) {
			return  null;
		}
	}

	public static String constructSQLInsertWithID(Object ob, int id) {
		try {
			String s = "insert into " + ob.getClass().getSimpleName().toLowerCase() + " (id,  ";

			String dados = id+", ";

			Field[] f = ob.getClass().getDeclaredFields();
			Method[] mt = ob.getClass().getDeclaredMethods();

			for (int i = 0; i < f.length; i++) {

				if (f[i].getName().startsWith("wmx_")) //tranziente
					continue;
				else if (f[i].getName().indexOf("manifesto") != -1) //arquivo Midia
					continue;
				else if (f[i].getName().startsWith("l_")) //arquivo Lista
					continue;
				/*else if (f[i].getName().indexOf("onLine") != -1)
					continue;
				else if (f[i].getType().getName().indexOf("Collection") != -1)
					continue;*/

				s += "\"" + f[i].getName() + "\"";

				if (mt[getPos(f[i], mt)].invoke(ob,  new Object[0]) == null) {
					dados += "null";
				}                
				else if (isString(f[i].getType())) {

					String aux[] = mt[getPos(f[i], mt)].invoke(ob,  new Object[0]).toString().split("'");
					dados += "'";
					for (int j = 0; j < aux.length; j++) {
						if (j!=0)
							dados += "''";
						dados += aux[j];
					}
					dados += "'";


				} else if (isBoolean(f[i].getType())) {
					dados += "'" + mt[getPos(f[i], mt)].invoke(ob,  new Object[0]).toString() + "'";
				} else if (isGregorianCalendar(f[i].getType())) {
					GregorianCalendar g = (GregorianCalendar) (mt[getPos(f[i], mt)].invoke(ob,  new Object[0]));
					if (g != null) {
						dados += "'" + g.toString() + "'";
					} else {
						dados += "null";
					}
				} else if (isTimeStamp(f[i].getType())) {
					Timestamp g = (Timestamp) (mt[getPos(f[i], mt)].invoke(ob,  new Object[0]));
					if (g != null) {
						dados += "'" + g.toString() + "'";
					} else {
						dados += "now()";
					}
				} else if (isFloatORInteger(f[i].getType())) {
					dados += mt[getPos(f[i], mt)].invoke(ob,  new Object[0]);

					//	} else if (isAcao(f[i].getType())) {
					//		dados += Acao.getIntAcao((Acao)mt[getPos(f[i], mt)].invoke(ob,  new Object[0]));
				} else if (isDto(f[i].getType())) {
					Dto dto = (Dto) mt[getPos(f[i], mt)].invoke(ob,  new Object[0]);
					if (dto != null && dto.getId() != 0)
						dados += (dto).getId();                
					else 
						dados += "null";
				}
				else {
					dados += "null";
				}

				s += ", ";
				dados += ", ";
			}
			dados = dados.substring(0,dados.length()-2);
			s = s.substring(0,s.length()-2);

			s += ") values (" + dados + ") RETURNING \"id\" AS \"returnID\";";

			return s;
		} catch (Exception ex) {
			return null;
		}
	}
	public static String constructSQLInsert(Object ob) {
		try {
			String s = "insert into " + ob.getClass().getSimpleName().toLowerCase() + " ( ";

			String dados = "";

			Field[] f = ob.getClass().getDeclaredFields();
			Method[] mt = ob.getClass().getDeclaredMethods();

			for (int i = 0; i < f.length; i++) {

				if (f[i].getName().startsWith("wmx_")) //tranziente
					continue;
				else if (f[i].getName().indexOf("file_") != -1) //arquivo Midia
					continue;
				else if (f[i].getName().startsWith("l_")) //arquivo Lista
					continue;
				/*else if (f[i].getName().indexOf("onLine") != -1)
					continue;
				else if (f[i].getType().getName().indexOf("Collection") != -1)
					continue;*/

				s += "\"" + f[i].getName() + "\"";

				if (mt[getPos(f[i], mt)].invoke(ob,  new Object[0]) == null) {
					dados += "null";
				}                
				else if (isString(f[i].getType())) {

					String aux[] = mt[getPos(f[i], mt)].invoke(ob,  new Object[0]).toString().split("'");
					dados += "'";
					for (int j = 0; j < aux.length; j++) {
						if (j!=0)
							dados += "''";
						dados += aux[j];
					}
					dados += "'";


				} else if (isBoolean(f[i].getType())) {
					dados += "'" + mt[getPos(f[i], mt)].invoke(ob,  new Object[0]).toString() + "'";
				} else if (isGregorianCalendar(f[i].getType())) {
					GregorianCalendar g = (GregorianCalendar) (mt[getPos(f[i], mt)].invoke(ob,  new Object[0]));
					if (g != null) {
						dados += "'" + g.toString() + "'";
					} else {
						dados += "null";
					}
				} else if (isTimeStamp(f[i].getType())) {
					Timestamp g = (Timestamp) (mt[getPos(f[i], mt)].invoke(ob,  new Object[0]));
					if (g != null) {
						dados += "'" + g.toString() + "'";
					} else {
						dados += "null";
					}
				} else if (isFloatORInteger(f[i].getType())) {
					dados += mt[getPos(f[i], mt)].invoke(ob,  new Object[0]);

					//	} else if (isAcao(f[i].getType())) {
					//		dados += Acao.getIntAcao((Acao)mt[getPos(f[i], mt)].invoke(ob,  new Object[0]));
				} else if (isDto(f[i].getType())) {
					Dto dto = (Dto) mt[getPos(f[i], mt)].invoke(ob,  new Object[0]);
					if (dto != null && dto.getId() != 0)
						dados += (dto).getId();                
					else 
						dados += "null";
				}
				else {
					dados += "null";
				}

				s += ", ";
				dados += ", ";
			}
			dados = dados.substring(0,dados.length()-2);
			s = s.substring(0,s.length()-2);

			s += ") values (" + dados + ") RETURNING \"id\" AS \"returnID\";";

			return s;
		} catch (Exception ex) {
			return null;
		}
	}

	private static boolean isString(@SuppressWarnings("rawtypes") Class ob) {

		String s = ob.getSimpleName();

		if (s.compareTo("String") == 0) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isBoolean(@SuppressWarnings("rawtypes") Class ob) {

		String s = ob.getSimpleName();

		if (s.compareTo("boolean") == 0) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isGregorianCalendar(@SuppressWarnings("rawtypes") Class ob) {

		String s = ob.getSimpleName();

		if (s.compareTo("GregorianCalendar") == 0) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isTimeStamp(@SuppressWarnings("rawtypes") Class ob) {

		String s = ob.getSimpleName();

		if (s.compareTo("Timestamp") == 0) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isFloatORInteger(@SuppressWarnings("rawtypes") Class ob) {

		String s = ob.getSimpleName();

		if (s.compareTo("float") == 0 || s.compareTo("int") == 0) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isDto(@SuppressWarnings("rawtypes") Class ob) {

		//String s = ob.getName().substring(0, 7);

		if (ob.getName().indexOf("dto") != -1) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isAcao(@SuppressWarnings("rawtypes") Class ob) {

		//String s = ob.getName().substring(0, 7);

		if (ob.getName().indexOf("Acao") != -1) {
			return true;
		} else {
			return false;
		}
	}

	private static int getPos(Field field, Method[] mt) {

		String s = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
		for (int i = 0; i <
				mt.length; i++) {
			if (mt[i].getName().compareTo(s) == 0) {
				return i;
			}
		}
		return -1;
	}

	@SuppressWarnings("unused")
	private static String TransformCodding(String s) {

		String tipo = "UTF-8";
		String tipoconverte = "ISO-8859-1";

		try {
			s = new String(s.getBytes(tipoconverte),0,s.getBytes(tipoconverte).length,tipo);
			//s = s.toUpperCase();
			return s;
		} catch (UnsupportedEncodingException e) {
			return "";
		}

	}

}
