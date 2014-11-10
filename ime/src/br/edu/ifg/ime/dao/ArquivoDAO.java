package br.edu.ifg.ime.dao;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.print.attribute.standard.Media;

import org.postgresql.jdbc4.Jdbc4Connection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import br.edu.ifg.ime.dto.Arquivo;
import br.edu.ifg.ime.dto.Usuario;

public class ArquivoDAO extends DAO<Arquivo> {

	public ArquivoDAO() {

	}
/*
	public void update(Arquivo arquivo) throws SQLException {

		String sql = DAOConnection.constructSQLUpDate(arquivo);

		int result = DAOConnection.executeInsertUpDateDelete(sql);

		if (result > 0)
			arquivo.setId(result);

	}*/

	public void update(Arquivo arquivo) throws SQLException {

			if (arquivo.getId() == 0) {
				insert(arquivo);		
			}
			else		{
				String sql = DAOConnection.constructSQLUpDate(arquivo);

				int result = DAOConnection.executeInsertUpDateDelete(sql);

				if (result > 0)
					arquivo.setId(result);
			}
	
	}

	public void updateBytes(Arquivo arquivo) throws SQLException {

		String sql;		
		PreparedStatement pStm;
		Connection conn;
		LargeObjectManager mObj;
		LargeObject obj;
		long oid;
		Object ob; 

		sql = "update arquivo set arquivo = ? where id = ?;";
		conn = DAOConnection.getConnection();
		conn.setAutoCommit(false);
		pStm = conn.prepareStatement(sql);

		mObj = null;
		try {
			//mObj = conn.getLargeObjectAPI();
			mObj = ((org.postgresql.PGConnection)conn).getLargeObjectAPI();
		} catch (Exception e) {
			throw new SQLException();
		}

		oid = mObj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);
		obj = mObj.open(oid, LargeObjectManager.WRITE);

		obj.write(arquivo.getArquivo());

		obj.close();

		pStm.setLong(1, oid);
		pStm.setInt(2, arquivo.getId());
		pStm.executeUpdate();
		
		DAOConnection.closeObjects(null, pStm);
		}



	public Arquivo refreshBytes(Arquivo arquivo) throws SQLException {

		String sql;		
		PreparedStatement pStm;
		Connection conn;
		LargeObjectManager mObj;
		LargeObject obj;
		long oid;
		Object ob; 
		ResultSet rs;

		sql = "select arquivo from arquivo where id = ?;";
		conn = DAOConnection.getConnection();
		conn.setAutoCommit(false);
		pStm = conn.prepareStatement(sql);

		mObj = null;
		try {
			mObj = ((org.postgresql.PGConnection)conn).getLargeObjectAPI();
			//mObj = (LargeObjectManager) ob.getClass().getMethod("getLargeObjectAPI", null).invoke(ob,  new Object[0]);
			//mObj = conn.getLargeObjectAPI();
		} catch (Exception e) {
			throw new SQLException();
		}

		pStm.setInt(1, arquivo.getId());
		rs = pStm.executeQuery();

		if (!rs.next()) {
			
			
			DAOConnection.closeObjects(rs, pStm);
			
			return null;
		}

		oid = rs.getInt(1);
		obj = mObj.open(oid, LargeObjectManager.READ);



		byte[] buf = new byte[obj.size()];
		obj.read(buf,0,buf.length);
		arquivo.setArquivo(buf);

		obj.close();

		
		DAOConnection.closeObjects(rs, pStm);

		return arquivo;


	}


	public byte[] getBytesArquivo(String path) throws SQLException {

		String sql;		
		PreparedStatement pStm;
		Connection conn;
		LargeObjectManager mObj;
		LargeObject obj;
		long oid;
		Object ob; 
		ResultSet rs;

		sql = "select arquivo from arquivo where nome = ?;";
		conn = DAOConnection.getConnection();
		conn.setAutoCommit(false);
		pStm = conn.prepareStatement(sql);

		mObj = null;
		try {
			mObj = ((org.postgresql.PGConnection)conn).getLargeObjectAPI();
			//mObj = (LargeObjectManager) ob.getClass().getMethod("getLargeObjectAPI", null).invoke(ob,  new Object[0]);
			//mObj = conn.getLargeObjectAPI();
		} catch (Exception e) {
			throw new SQLException();
		}

		pStm.setString(1, path );
		rs = pStm.executeQuery();

		if (!rs.next()) {
			
			DAOConnection.closeObjects(rs, pStm);
			
			return null;
		}

		oid = rs.getInt(1);
		obj = mObj.open(oid, LargeObjectManager.READ);



		byte[] buf = new byte[obj.size()];
		obj.read(buf,0,buf.length);
		
		obj.close();
		

		DAOConnection.closeObjects(rs, pStm);
		return buf;


	}

	public Arquivo removeOidArquivo(Arquivo arquivo) throws SQLException {

		String sql;		
		PreparedStatement pStm;
		Connection conn;
		LargeObjectManager mObj;
		LargeObject obj;
		long oid;
		Object ob; 
		ResultSet rs;

		sql = "select arquivo from arquivo where id = ?;";
		conn = DAOConnection.getConnection();
		conn.setAutoCommit(false);
		pStm = conn.prepareStatement(sql);

		mObj = null;
		try {
			mObj = ((org.postgresql.PGConnection)conn).getLargeObjectAPI();
			//mObj = (LargeObjectManager) ob.getClass().getMethod("getLargeObjectAPI", null).invoke(ob,  new Object[0]);
			//mObj = conn.getLargeObjectAPI();
		} catch (Exception e) {
			throw new SQLException();
		}

		pStm.setInt(1, arquivo.getId());
		rs = pStm.executeQuery();


		if (!rs.next()) {
			
			DAOConnection.closeObjects(rs, pStm);
			
			return null;
		}


		oid = rs.getLong(1);

		if (oid != 0) {
			mObj.delete(oid);		
	
		}
		
		
		DAOConnection.closeObjects(rs, pStm);

		return arquivo;


	}

	public void delete(Arquivo arquivo) throws SQLException {
		
        removeOidArquivo(arquivo);
		String sql = "delete from arquivo where id = "+arquivo.getId();

		int result = DAOConnection.executeInsertUpDateDelete(sql);

		if (result > 0)
			arquivo.setId(result);
	}

	public void delete(Collection<Arquivo> listaRemoves) throws SQLException {

		Iterator<Arquivo> it = listaRemoves.iterator();
		Arquivo arquivo;

		while (it.hasNext()) {			
			arquivo = it.next();
			delete(arquivo);
		}
	}

	public synchronized void insert(Arquivo arquivo) throws SQLException {

		String sql = "insert into arquivo (nome, " +
				"titulo, " +
				"content_type, " +
				"cadastrado, " +
				"alterado, "
				+ "unidade) values ('" +
				arquivo.getNome()+"', '" +
				arquivo.getTitulo()+"', '" +
				arquivo.getContent_type()+"', " +
				"now(), " +
				"now(), "
				+ arquivo.getUnidade()+") " +
				"RETURNING \"id\" AS \"returnID\";";

		int result = DAOConnection.executeInsertUpDateDelete(sql);

		if (result > 0)
			arquivo.setId(result);

	}		

	@Override
	public List<Arquivo> selectAll() {		

		List<Arquivo> lArquivos = new ArrayList<Arquivo>();
		try {

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;

			sql = "select * from arquivo order by nome;";

			pStm = DAOConnection.createPreparedStatement(sql);
			//pStm.setInt(1, doc.getId());
			rs = pStm.executeQuery();

			Arquivo arquivo;

			while (rs.next()) {			

				arquivo = new Arquivo();		

				arquivo.setId(rs.getInt("id"));
				arquivo.setNome(rs.getString("nome"));
				arquivo.setCadastrado(rs.getTimestamp("cadastrado"));
				arquivo.setAlterado(rs.getTimestamp("alterado"));
				arquivo.setContent_type(rs.getString("content_type"));	
				arquivo.setTitulo(rs.getString("titulo"));		
								lArquivos.add(arquivo);
			}

			DAOConnection.closeObjects(rs, pStm);

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return lArquivos;
	}
 
	public List<Arquivo> selectAllStartWith(String path) {		

		List<Arquivo> lArquivos = new ArrayList<Arquivo>();
		try {

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;

			sql = "select * from arquivo where nome like ? order by nome;";

			pStm = DAOConnection.createPreparedStatement(sql);
			//pStm.setInt(1, doc.getId());
			pStm.setString(1, path+"%");
			rs = pStm.executeQuery();

			Arquivo arquivo;

			while (rs.next()) {			

				arquivo = new Arquivo();		

				arquivo.setId(rs.getInt("id"));
				arquivo.setNome(rs.getString("nome"));
				arquivo.setCadastrado(rs.getTimestamp("cadastrado"));
				arquivo.setAlterado(rs.getTimestamp("alterado"));
				arquivo.setContent_type(rs.getString("content_type"));	
				arquivo.setTitulo(rs.getString("titulo"));		
								lArquivos.add(arquivo);
			}

			DAOConnection.closeObjects(rs, pStm);

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return lArquivos;
	}

	public Arquivo selectArquivo(Arquivo arquivo) {		

		try {

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;

			sql = "select id, nome, cadastrado, alterado, content_type, unidade " +
					"from arquivo where id = ?;";
			pStm = DAOConnection.createPreparedStatement(sql);
			pStm.setInt(1, arquivo.getId());
			rs = pStm.executeQuery();


			if (rs.next()) {			

				arquivo.setId(rs.getInt("id"));
				arquivo.setNome(rs.getString("nome"));
				arquivo.setCadastrado(rs.getTimestamp("cadastrado"));
				arquivo.setAlterado(rs.getTimestamp("alterado"));
				arquivo.setContent_type(rs.getString("content_type"));	
				arquivo.setUnidade(rs.getInt("unidade"));		
				
			}
			DAOConnection.closeObjects(rs, pStm);

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return arquivo;
	}


	public boolean exiteArquivo(String path, boolean nomeExato, int id) {		

		try {

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;

			sql = "select id " +
					"from arquivo where "+(nomeExato?"nome = ? and id != ? ;":" nome like ? and id != ? ;");
			pStm = DAOConnection.createPreparedStatement(sql);
			
			if (nomeExato) 
				pStm.setString(1, path);
			else
			pStm.setString(1, path+'%');
			pStm.setInt(2, id);
			
			rs = pStm.executeQuery();


			if (rs.next()) {			
				DAOConnection.closeObjects(rs, pStm);
				return true;
			}
			DAOConnection.closeObjects(rs, pStm);

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return false;
	}


}
