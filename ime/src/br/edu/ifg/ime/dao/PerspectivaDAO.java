package br.edu.ifg.ime.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

import br.edu.ifg.ime.dto.Linguagem;
import br.edu.ifg.ime.dto.Perspectiva;
import br.edu.ifg.ime.dto.Servico;
import br.edu.ifg.ime.dto.Usuario;


public class PerspectivaDAO extends DAO<Perspectiva> {


	public PerspectivaDAO() {


	}

	public void upDate(Perspectiva perspectiva) throws SQLException {
		String sql = "";

		if (perspectiva.getId() == 0)
			sql = DAOConnection.constructSQLInsert(perspectiva);
		else 
			sql = DAOConnection.constructSQLUpDate(perspectiva);	
		
		int result = DAOConnection.executeInsertUpDateDelete(sql);

		if (perspectiva.getId() == 0)
			perspectiva.setId(result);


	}

	public void deleteTodasPerspectiva(Usuario usuario) {	

		String sql = "delete from perspectiva where usuario = "+usuario.getId()+";";
		try {
			int result = DAOConnection.executeInsertUpDateDelete(sql);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	public void delete(Perspectiva pp) {	

		String sql = "delete from perspectiva where id = "+pp.getId()+";";
		try {
			int result = DAOConnection.executeInsertUpDateDelete(sql);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}


	public boolean getPerspectivas(Usuario usuario) {
		usuario.getL_perspectivas().clear();

		ResultSet rs = null;
		//String sql;
		PreparedStatement pStm;

		String sql = "select * from perspectiva p where p.usuario = "+usuario.getId()+" order by id";

		pStm = DAOConnection.createPreparedStatement(sql);

		Servico servico;
		try {
			rs = pStm.executeQuery();

			if (rs != null)
				while (rs.next()) {

					Perspectiva p = new Perspectiva(rs.getInt("id"));
					p.setTitulo(rs.getString("titulo"));
					p.setUsuario(usuario);
					
					if(rs.getInt("linguagem") != 0)
						p.setLinguagem(new Linguagem(rs.getInt("linguagem")));

					usuario.getL_perspectivas().add(p);

					/*servico = new Servico();
					servico.setId(rs.getInt("id"));
					servico.setDescricao(rs.getString("descricao"));
					servico.setServico(rs.getString("servico"));
					servico.setAtivo(rs.getBoolean("ativo"));
					servico.setAutenticar(rs.getBoolean("autenticar"));
					servico.setTipo(rs.getInt("tipo"));	

					Permissoes p = new Permissoes();
					p.setId(rs.getInt("pid"));
					p.setUsuario(usuario);
					p.setServico(servico);
					p.setLiberado(rs.getBoolean("liberado"));
					usuario.getL_permissoes().add(p);*/
				}

			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException e1) {
			return false;
		}
		return true;

	}

	public Perspectiva getPerspectiva(String id) {
		Perspectiva p = null;
		ResultSet rs = null;
		//String sql;
		PreparedStatement pStm;

		String sql = "select * from perspectiva p where p.id = "+id+";";

		pStm = DAOConnection.createPreparedStatement(sql);

		Servico servico;
		try {
			rs = pStm.executeQuery();

			if (rs != null && rs.next()) {

				p = new Perspectiva(rs.getInt("id"));
				p.setTitulo(rs.getString("titulo"));
				p.setUsuario(new Usuario(rs.getInt("usuario")));
				
				if(rs.getInt("linguagem") != 0)
					p.setLinguagem(new Linguagem(rs.getInt("linguagem")));

			}
			DAOConnection.closeObjects(rs, pStm);

		} catch (SQLException e1) {
			return p;
		}
		return p;

	}

	@Override
	public List<Perspectiva> selectAll() {


		return null;
	}
}
