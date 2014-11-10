package br.edu.ifg.ime.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.edu.ifg.ime.dto.Linguagem;
import br.edu.ifg.ime.dto.Usuario;


public class UsuarioDAO extends DAO<Usuario> {

	public UsuarioDAO() {

	}

	public List<Usuario> selectAll() {		

		return selectUsuarios();
	}	

	public void update(Usuario usuario) {

		String sql = DAOConnection.constructSQLUpDate(usuario);

		try {

			int result = DAOConnection.executeInsertUpDateDelete(sql);
			result++;
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	public void delete(Usuario usuario) {

		String sql = "delete from usuario where id =" + usuario.getId();

		try {

			int result = DAOConnection.executeInsertUpDateDelete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	public void insert(Usuario usuario) {

		String sql = DAOConnection.constructSQLInsert(usuario);

		try {			
			int result = DAOConnection.executeInsertUpDateDelete(sql);
			if (result > 0)
				usuario.setId(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}		

	public List<Usuario> selectUsuarios() {		

		List<Usuario> lUsuarios = new ArrayList<Usuario>(); 

		try {

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;

			sql = "select * from usuario where id > "+Usuario.wmx_convidado+" order by nome;";
			pStm = DAOConnection.createPreparedStatement(sql);
			rs = pStm.executeQuery();

			Usuario usuario;

			while (rs.next()) {			

				usuario = new Usuario();				

				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setLogin(rs.getString("login"));
				usuario.setAtivo(rs.getBoolean("ativo"));
				usuario.setTipo_perfil(rs.getString("tipo_perfil"));

				if (rs.getInt("perfil") != 0)
					usuario.setPerfil(new Usuario(rs.getInt("perfil")));

				if (rs.getInt("linguagem") != 0)
					usuario.setLinguagem(new Linguagem(rs.getInt("linguagem")));
				lUsuarios.add(usuario);
			}

			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException e) {

		}
		return lUsuarios;
	}

	public List<Usuario> selectTiposDeUsuarios() {		

		List<Usuario> lUsuarios = new ArrayList<Usuario>(); 

		try {

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;

			sql = "select * from usuario where id > "+Usuario.wmx_root+" and id < "+Usuario.wmx_convidado+" order by id;";
			pStm = DAOConnection.createPreparedStatement(sql);
			rs = pStm.executeQuery();

			Usuario usuario;

			while (rs.next()) {			

				usuario = new Usuario();				

				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setLogin(rs.getString("login"));
				usuario.setAtivo(rs.getBoolean("ativo"));
				usuario.setTipo_perfil(rs.getString("tipo_perfil"));

				if (rs.getInt("perfil") != 0)
					usuario.setPerfil(new Usuario(rs.getInt("perfil")));

				if (rs.getInt("linguagem") != 0)
					usuario.setLinguagem(new Linguagem(rs.getInt("linguagem")));

				lUsuarios.add(usuario);

			}

			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException e) {

		}
		return lUsuarios;
	}
	public List<Usuario> selectUsuariosComPerfil(Usuario perfil) {		

		List<Usuario> lUsuarios = new ArrayList<Usuario>(); 

		try {

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;

			sql = "select * from usuario where perfil = "+perfil.getId()+" order by id;";
			pStm = DAOConnection.createPreparedStatement(sql);
			rs = pStm.executeQuery();

			Usuario usuario;

			while (rs.next()) {			

				usuario = new Usuario();				

				usuario.setId(rs.getInt("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setLogin(rs.getString("login"));
				usuario.setAtivo(rs.getBoolean("ativo"));
				usuario.setTipo_perfil(rs.getString("tipo_perfil"));

				usuario.setPerfil(perfil);

				if (rs.getInt("linguagem") != 0)
					usuario.setLinguagem(new Linguagem(rs.getInt("linguagem")));

				lUsuarios.add(usuario);

			}

			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException e) {

		}
		return lUsuarios;
	}

	public Usuario selectUsuarioByLogin(String login) {		 
		Usuario usuario = null;
		try {

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;

			sql = "select * from usuario where login = ?;";
			pStm = DAOConnection.createPreparedStatement(sql);

			pStm.setString(1, login);
			rs = pStm.executeQuery();

			usuario = new Usuario();

			if (!rs.next())
				return null;

			usuario.setId(rs.getInt("id"));
			usuario.setNome(rs.getString("nome"));
			usuario.setSenha(rs.getString("senha"));
			usuario.setLogin(rs.getString("login"));
			usuario.setAtivo(rs.getBoolean("ativo"));
			usuario.setTipo_perfil(rs.getString("tipo_perfil"));

			if (rs.getInt("perfil") != 0)
				usuario.setPerfil(new Usuario(rs.getInt("perfil")));

			if (rs.getInt("linguagem") != 0)
				usuario.setLinguagem(new Linguagem(rs.getInt("linguagem")));

			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException e) {

			return usuario;
		}
		return usuario;
	}

	public Usuario selectUsuarioById(String id) {		 
		Usuario usuario = null;
		try {

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;

			sql = "select * from usuario where id = ?;";
			pStm = DAOConnection.createPreparedStatement(sql);

			pStm.setInt(1, Integer.parseInt(id));
			rs = pStm.executeQuery();

			usuario = new Usuario();

			if (!rs.next())
				return null;

			usuario.setId(rs.getInt("id"));
			usuario.setNome(rs.getString("nome"));
			usuario.setSenha(rs.getString("senha"));
			usuario.setLogin(rs.getString("login"));
			usuario.setAtivo(rs.getBoolean("ativo"));
			usuario.setTipo_perfil(rs.getString("tipo_perfil"));

			if (rs.getInt("perfil") != 0)
				usuario.setPerfil(new Usuario(rs.getInt("perfil")));

			if (rs.getInt("linguagem") != 0)
				usuario.setLinguagem(new Linguagem(rs.getInt("linguagem")));

			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException e) {

			return usuario;
		}
		return usuario;
	}

}
