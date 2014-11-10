package br.edu.ifg.ime.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import br.edu.ifg.ime.controllers.LinguagemController;
import br.edu.ifg.ime.dto.Chave_textual;
import br.edu.ifg.ime.dto.Dto;
import br.edu.ifg.ime.dto.Linguagem;
import br.edu.ifg.ime.dto.Perspectiva;
import br.edu.ifg.ime.dto.Servico;
import br.edu.ifg.ime.dto.Texto;
import br.edu.ifg.ime.dto.Usuario;


public class LinguagemDAO extends DAO<Chave_textual> {


	public LinguagemDAO() {


	}

	public void upDate(Dto chave) throws SQLException {
		String sql = "";

		if (chave.getId() == 0)
			sql = DAOConnection.constructSQLInsert(chave);
		else 
			sql = DAOConnection.constructSQLUpDate(chave);		
		int result = DAOConnection.executeInsertUpDateDelete(sql);

		if (chave.getId() == 0)
			chave.setId(result);
	}

	public void deleteTextos(Linguagem linguagem) {

		String sql = "delete from texto where linguagem = " + linguagem.getId();

		try {

			int result = DAOConnection.executeInsertUpDateDelete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}



	public TreeMap<String, String> getListaChavesPadrao() {
		ResultSet rs = null;
		//String sql;
		PreparedStatement pStm;

		String sql = "select * from chave_textual order by chave";

		pStm = DAOConnection.createPreparedStatement(sql);


		TreeMap<String, String> chaves = new TreeMap<String, String>();

		try {
			rs = pStm.executeQuery();

			if (rs != null)
				while (rs.next()) {
					chaves.put(rs.getString("chave"), rs.getString("texto"));
					//Chave_textual p = new Chave_textual(rs.getInt("id"));
					//p.setChave(rs.getString("chave"));
					//p.setTexto(rs.getString("texto"));

					//chaves.put(p.getChave(), p);
				}

			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException e1) {
			return chaves;
		}
		return chaves;

	}

	@Override
	public List<Chave_textual> selectAll() {


		return null;
	}

	public TreeMap<String, String> getTextos(Usuario usuario) {

		ResultSet rs = null;

		PreparedStatement pStm;

		String sql = "select t.*, ct.chave chave from texto t, linguagem l, chave_textual ct where t.linguagem = l.id and t.chave_textual = ct.id and linguagem = ? order by chave";



		TreeMap<String, String> chaves = new TreeMap<String, String>();

		try {

			pStm = DAOConnection.createPreparedStatement(sql);

			if (usuario.wmx_perspectiva == null ||  usuario.wmx_perspectiva.getLinguagem() == null) {
				if (usuario.getLinguagem() != null)
					pStm.setInt(1, usuario.getLinguagem().getId());
				else {
					return null;
				}
			}
			else
				pStm.setInt(1, usuario.wmx_perspectiva.getLinguagem().getId());

			rs = pStm.executeQuery();

			if (rs != null)
				while (rs.next()) {
					chaves.put(rs.getString("chave"), rs.getString("texto"));
				}

			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException e1) {
			return chaves;
		}
		return chaves;
	}

	public TreeMap<String, String> getTextos(Linguagem lang) {

		if (lang == null)
			return null;

		ResultSet rs = null;

		PreparedStatement pStm;

		String sql = "select t.*, ct.chave chave from texto t, chave_textual ct where t.chave_textual = ct.id and linguagem = ?";



		TreeMap<String, String> chaves = new TreeMap<String, String>();

		try {

			pStm = DAOConnection.createPreparedStatement(sql);
			pStm.setInt(1, lang.getId());

			rs = pStm.executeQuery();

			if (rs != null)
				while (rs.next()) {
					chaves.put(rs.getString("chave"), rs.getString("texto"));
				}

			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException e1) {
			return chaves;
		}
		return chaves;
	}

	public List<Chave_textual> getChavesTextuais() {

		ResultSet rs = null;

		PreparedStatement pStm;

		String sql = "select * from chave_textual order by chave";



		List<Chave_textual> chaves = new ArrayList<Chave_textual>();

		try {

			pStm = DAOConnection.createPreparedStatement(sql);

			rs = pStm.executeQuery();

			Chave_textual ct = null;
			if (rs != null)
				while (rs.next()) {
					ct = new Chave_textual();
					ct.setId(rs.getInt("id"));
					ct.setChave(rs.getString("chave"));
					ct.setTexto(rs.getString("texto"));
					ct.setTipo(rs.getInt("tipo"));
					chaves.add(ct);
				}

			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException e1) {
			return chaves;
		}
		return chaves;
	}

	public boolean existeChaveTextual(String chave) {

		ResultSet rs = null;

		PreparedStatement pStm;

		String sql = "select * from chave_textual where chave = '"+chave+"' order by chave";

		try {

			pStm = DAOConnection.createPreparedStatement(sql);

			rs = pStm.executeQuery();

			if (rs != null && rs.next()) {
				DAOConnection.closeObjects(rs, pStm);
				return true;
				}

			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException e1) {
			return false;
		}
		return false;
	}

	public boolean insert(String chave, String texto, int tipo) {
		Chave_textual c = new Chave_textual();
		c.setChave(chave);
		c.setTexto(texto);
		c.setTipo(tipo);

		try {
			upDate(c);
			
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public List<Linguagem> getLinguagens() {

		ResultSet rs = null;

		PreparedStatement pStm;

		String sql = "select * from linguagem l order by id";


		List<Linguagem> lLing = new ArrayList<Linguagem>();
		try {

			pStm = DAOConnection.createPreparedStatement(sql);

			rs = pStm.executeQuery();

			Linguagem l ;
			if (rs != null)
				while (rs.next()) {
					l = new Linguagem();

					l.setId(rs.getInt("id"));
					l.setTitulo(rs.getString("titulo"));

					lLing.add(l);
				}

			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException e1) {
			return lLing;
		}
		return lLing;
	}

	public Linguagem getLinguagem(String id) {

		ResultSet rs = null;

		PreparedStatement pStm;

		String sql = "select * from linguagem l where id = "+id+" order by id";


		Linguagem l = null;
		try {

			pStm = DAOConnection.createPreparedStatement(sql);

			rs = pStm.executeQuery();

			if (rs.next()) {
				l = new Linguagem();

				l.setId(rs.getInt("id"));
				l.setTitulo(rs.getString("titulo"));					
			}	

			DAOConnection.closeObjects(rs, pStm);
		} catch (SQLException e1) {
			return null;
		}
		return l;
	}

	public void delete(String idLang) {

		String sql = "delete from linguagem where id = " + idLang+";";

		try {

			int result = DAOConnection.executeInsertUpDateDelete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}	

	}
}
