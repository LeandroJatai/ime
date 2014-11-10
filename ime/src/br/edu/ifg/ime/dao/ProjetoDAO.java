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

import br.edu.ifg.ime.dto.Projeto;
import br.edu.ifg.ime.dto.Usuario;

public class ProjetoDAO extends DAO<Projeto> {

	public ProjetoDAO() {

	}

	public void update(Projeto projeto) throws SQLException {

		String sql = DAOConnection.constructSQLUpDate(projeto);

		int result = DAOConnection.executeInsertUpDateDelete(sql);

		if (result > 0)
			projeto.setId(result);

	}

	public void update(Collection<Projeto> listaUpdates) throws SQLException {

		Iterator<Projeto> it = listaUpdates.iterator();
		Projeto projeto;

		while (it.hasNext()) {

			projeto = it.next();
			if (projeto.getId() == 0) {
				insert(projeto);				
			}
			else		
				update(projeto);

			if (projeto.getManifesto() != null)
				updateManifesto(projeto);
		}	
	}

	public void updateManifesto(Projeto projeto) throws SQLException {

		String sql;		
		PreparedStatement pStm;
		Connection conn;
		LargeObjectManager mObj;
		LargeObject obj;
		long oid;
		Object ob; 

		sql = "update projeto set manifesto = ? where id = ?;";
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

		obj.write(projeto.getManifesto());

		obj.close();

		pStm.setLong(1, oid);
		pStm.setInt(2, projeto.getId());
		pStm.executeUpdate();

		DAOConnection.closeObjects(null, pStm);


	}



	public Projeto refreshManifesto(Projeto projeto) throws SQLException {

		String sql;		
		PreparedStatement pStm;
		Connection conn;
		LargeObjectManager mObj;
		LargeObject obj;
		long oid;
		Object ob; 
		ResultSet rs;

		sql = "select manifesto from projeto where id = ?;";
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

		pStm.setInt(1, projeto.getId());
		rs = pStm.executeQuery();


		if (!rs.next()) {
			
			DAOConnection.closeObjects(rs, pStm);
			
			return null;
		}


		oid = rs.getInt(1);
		obj = mObj.open(oid, LargeObjectManager.READ);



		byte[] buf = new byte[obj.size()];
		obj.read(buf,0,buf.length);
		projeto.setManifesto(buf);



		obj.close();

		rs.close();
		pStm.close();

		DAOConnection.closeObjects(rs, pStm);

		return projeto;


	}

	public Projeto removeManifesto(Projeto projeto) throws SQLException {

		String sql;		
		PreparedStatement pStm;
		Connection conn;
		LargeObjectManager mObj;
		LargeObject obj;
		long oid;
		Object ob; 
		ResultSet rs;

		sql = "select manifesto from projeto where id = ?;";
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

		pStm.setInt(1, projeto.getId());
		rs = pStm.executeQuery();

		if (!rs.next()) {
			
			DAOConnection.closeObjects(rs, pStm);
			
			return null;
		}


		oid = rs.getLong(1);

		if (oid != 0) {
			mObj.delete(oid);		
			
		}
		projeto.setManifesto(null);


		rs.close();
		pStm.close();

		DAOConnection.closeObjects(rs, pStm);

		return projeto;


	}

	public void delete(Projeto projeto) throws SQLException {
		
        removeManifesto(projeto);
		String sql = "delete from projeto where id = "+projeto.getId();

		int result = DAOConnection.executeInsertUpDateDelete(sql);

		if (result > 0)
			projeto.setId(result);
	}

	public void delete(Collection<Projeto> listaRemoves) throws SQLException {

		Iterator<Projeto> it = listaRemoves.iterator();
		Projeto projeto;

		while (it.hasNext()) {			
			projeto = it.next();
			delete(projeto);
		}
	}

	public synchronized void insert(Projeto projeto) throws SQLException {

		String sql = "insert into projeto (skin, "
				+ "titulo, " +
				"cadastrado, " +
				"alterado, " +
				"publico, " +
				"usuario) values ("+(projeto.getSkin()!=null?"'":"")
				+ projeto.getSkin() +(projeto.getSkin()!=null?"'":"")+", '" +
				projeto.getTitulo()+"', " +
				"now(), " +
				"now(), " +
				projeto.getPublico()+", "+
				projeto.getUsuario().getId()+") " +
				"RETURNING \"id\" AS \"returnID\";";

		int result = DAOConnection.executeInsertUpDateDelete(sql);

		if (result > 0)
			projeto.setId(result);

	}		

	@Override
	public List<Projeto> selectAll() {		
		
		return selectAll(null);
		
	}
	public List<Projeto> selectAll(Usuario usuarioEspecifico) {		

		List<Projeto> lProjetos = new ArrayList<Projeto>();
		try {

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;

			sql = "select * from projeto where "+(usuarioEspecifico == null?"publico = true":"usuario = "+usuarioEspecifico.getId())+" order by alterado desc;";


			pStm = DAOConnection.createPreparedStatement(sql);
			//pStm.setInt(1, doc.getId());
			rs = pStm.executeQuery();

			Projeto projeto;

			while (rs.next()) {			

				projeto = new Projeto();		

				projeto.setId(rs.getInt("id"));
				projeto.setTitulo(rs.getString("titulo"));
				projeto.setSkin(rs.getString("skin"));
				projeto.setCadastrado(rs.getTimestamp("cadastrado"));
				projeto.setAlterado(rs.getTimestamp("alterado"));
				projeto.setPublico(rs.getBoolean("publico"));		
				
				if (rs.getInt("usuario") != 0) 
					projeto.setUsuario(new Usuario(rs.getInt("usuario")));

				lProjetos.add(projeto);
			}

			DAOConnection.closeObjects(rs, pStm);

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return lProjetos;
	}
	
	public Projeto selectProjeto(Projeto projeto) {		

		try {

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;

			sql = "select id, titulo, cadastrado, alterado, publico, usuario, skin " +
					"from projeto where id = ?;";
			pStm = DAOConnection.createPreparedStatement(sql);
			pStm.setInt(1, projeto.getId());
			rs = pStm.executeQuery();


			if (rs.next()) {			

				projeto.setId(rs.getInt("id"));
				projeto.setTitulo(rs.getString("titulo"));
				projeto.setCadastrado(rs.getTimestamp("cadastrado"));
				projeto.setAlterado(rs.getTimestamp("alterado"));
				projeto.setPublico(rs.getBoolean("publico"));	
				projeto.setSkin(rs.getString("skin"));	
				
				if (rs.getInt("usuario") != 0) 
					projeto.setUsuario(new Usuario(rs.getInt("usuario")));
			}
			DAOConnection.closeObjects(rs, pStm);

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return projeto;
	}


}
