package br.edu.ifg.ime.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

import br.edu.ifg.ime.controllers.UsuarioController;
import br.edu.ifg.ime.dto.Grp_servicos;
import br.edu.ifg.ime.dto.Permissoes;
import br.edu.ifg.ime.dto.Perspectiva;
import br.edu.ifg.ime.dto.Servico;
import br.edu.ifg.ime.dto.Usuario;


public class PermissoesDAO extends DAO<Permissoes> {


	public PermissoesDAO() {


	}

	public void insert(Permissoes permissoes) throws SQLException {

		String sql = DAOConnection.constructSQLInsert(permissoes);		
		int result = DAOConnection.executeInsertUpDateDelete(sql);

		
		if (permissoes.getUsuario().getId() > Usuario.wmx_root && permissoes.getUsuario().getId() <= Usuario.wmx_convidado) {

			List<Usuario> lUsers = UsuarioController.selectUsuariosComPerfil(permissoes.getUsuario());

			for (Usuario usuario : lUsers) {
				
				if (!usuario.getTipo_perfil().equals("perfil.fixo"))
					continue;
				
				permissoes.setUsuario(usuario);
				insert(permissoes);
			}



		}



	}

	public void upDate(Permissoes permissoes) throws SQLException {

		String sql = DAOConnection.constructSQLUpDate(permissoes);		
		int result = DAOConnection.executeInsertUpDateDelete(sql);

	}

	public void deleteTodasPermissoes(Usuario usuario) {	

		String sql = "delete from permissoes where usuario = "+usuario.getId()+";";
		try {
			int result = DAOConnection.executeInsertUpDateDelete(sql);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void deleteTodasPermissoes(Perspectiva p) {	

		String sql = "delete from permissoes where perspectiva = "+p.getId()+";";
		try {
			int result = DAOConnection.executeInsertUpDateDelete(sql);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void delete(Permissoes permissoes) {	

		String sql = "delete from permissoes where id = "+permissoes.getId()+";";
		try {
			int result = DAOConnection.executeInsertUpDateDelete(sql);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void deletePermissoesAoServico(Servico servico) {
		deletePermissoesPerspectivaPadrao(null);
	}

	public void deletePermissoesPerspectivaPadrao(Permissoes permissoes) {	

		String sql = "delete from permissoes where servico = "+permissoes.getServico().getId()+" and usuario = "+permissoes.getUsuario().getId()+";";
		try {
			int result = DAOConnection.executeInsertUpDateDelete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		//Se a exclusão estiver sendo feita em um Perfil, todos as permissões dos usuários ligados a esse perfil tb seram removidas
		// ligação acontece em perfil vinculado ou fixo;  
		if (permissoes.getUsuario().getId() > Usuario.wmx_root && permissoes.getUsuario().getId() <= Usuario.wmx_convidado) {

			List<Usuario> lUsers = UsuarioController.selectUsuariosComPerfil(permissoes.getUsuario());

			for (Usuario usuario : lUsers) {
				permissoes.setUsuario(usuario);
				deletePermissoesPerspectivaPadrao(permissoes);
			}



		}


	}

	public boolean existePermissao(Usuario usuario, Servico servico) {
		return existePermissao(usuario, servico, null);
	}

	public boolean existePermissao(Usuario usuario, Servico servico, Perspectiva perspectiva) {

		ResultSet rs = null;
		String sql;
		PreparedStatement pStm;

		sql = "select * from permissoes where usuario = "+usuario.getId()+" and servico = "+servico.getId()+" and liberado = true ";

		if (perspectiva != null)
			sql += "and perspectiva = "+perspectiva.getId()+";";
		else 
			sql += "and perspectiva is null;";

		pStm = DAOConnection.createPreparedStatement(sql);

		try {
			rs = pStm.executeQuery();
			if (rs == null || !rs.next()) {
				DAOConnection.closeObjects(rs, pStm);
				return false;			
			}
			DAOConnection.closeObjects(rs, pStm);
		} catch (SQLException e1) {
			return false;
		}
		return true;

	}

	public boolean getPermissoes(Usuario usuario, Perspectiva pp) {
		usuario.getL_permissoes().clear();
		

		ResultSet rs = null;
		//String sql;
		PreparedStatement pStm;

		String sql = "select s.*, p.liberado, p.id pid, gs.titulo gs_tit from permissoes p, servico s, grp_servicos gs "
				+ "where p.servico = s.id and s.grupo = gs.id and p.usuario = "+usuario.getId()+" and s.ativo = true";

		if (pp == null)
			sql += " and perspectiva is null order by grupo, descricao;";
		else 
			sql += " and perspectiva ="+pp.getId()+" order by grupo, descricao;";


		pStm = DAOConnection.createPreparedStatement(sql);

		Servico servico;
		try {
			rs = pStm.executeQuery();

			if (rs != null) 
				while (rs.next()) {
					servico = new Servico();
					servico.setId(rs.getInt("id"));
					servico.setDescricao(rs.getString("descricao"));
					servico.setServico(rs.getString("servico"));
					servico.setAtivo(rs.getBoolean("ativo"));
					servico.setAutenticar(rs.getBoolean("autenticar"));
					servico.setTipo(rs.getInt("tipo"));	
					servico.setGrupo(new Grp_servicos(rs.getInt("grupo")));
					servico.getGrupo().setTitulo(rs.getString("gs_tit"));

					Permissoes p = new Permissoes();
					p.setId(rs.getInt("pid"));
					p.setUsuario(usuario);
					p.setServico(servico);
					p.setLiberado(rs.getBoolean("liberado"));
					/*
					if (rs.getInt("perspectiva") != 0) {
						p.setPerspectiva(new Perspectiva(rs.getInt("perspectiva")));
						p.getPerspectiva().setTitulo(rs.getString("pp_tit"));
					}
					 */
					usuario.getL_permissoes().add(p);
				}

			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException e1) {
			return false;
		}
		usuario.validarCache();
		return true;

	}

	@Override
	public List<Permissoes> selectAll() {


		return null;
	}
}
