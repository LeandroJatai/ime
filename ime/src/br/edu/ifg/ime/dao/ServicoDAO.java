package br.edu.ifg.ime.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import br.edu.ifg.ime.dto.Grp_servicos;
import br.edu.ifg.ime.dto.Servico;
import br.edu.ifg.ime.suport.Suport;


public class ServicoDAO extends DAO<Servico> {


	Statement stm;       

	public ServicoDAO() {


		try {
			Connection conn = DAOConnection.getConnection();
			Statement stm = null;
			try {
				stm = conn.createStatement();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			Servico serv = new Servico();    	
			serv.setServico("CONTROLVERSION");

			if (select(serv) == null) {
				serv.setDescricao("06/08/2013 00:01:00");
				serv.setAtivo(true);
				serv.setAutenticar(false);
				try {
					stm.execute("insert into servico (id, descricao, ativo, servico, autenticar, tipo) values " +
							"(1, '06/08/2013 00:01:00', true, 'CONTROLVERSION', false, 1);");

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}    	

			Servico servVersionAtual = new Servico();   
			servVersionAtual.setServico("CONTROLVERSION");    	
			select(servVersionAtual);    	
			GregorianCalendar dataVersionAtual = Suport.strBRToDate(servVersionAtual.getDescricao());    	

			GregorianCalendar version;


			version = Suport.strBRToDate("06/08/2013 00:01:00");
			if ( version.getTimeInMillis() > dataVersionAtual.getTimeInMillis()) {
				dataVersionAtual = version;
				servVersionAtual.setDescricao("06/08/2013 00:01:00");
				upDate(servVersionAtual);			
				//stm.execute("alter table \"Pessoa\" add column numsus character varying;");
			}

			serv = new Servico();    	
			serv.setServico("ldep.comp.mi.roles.config");

			if (select(serv) == null) {
				serv.setDescricao("Vincular Papéis dos MIs. Substitui os papeis do MI agregado pelos papeis dos MI superiores. Papeis indiretos só são visíveis se o projeto superior herdar de seus superiores.");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1300));
				insert(serv);			
			} 

			serv = new Servico();    	
			serv.setServico("activities.itemmodel.la.oncompletion");

			if (select(serv) == null) {
				serv.setDescricao("Edição da Aba [Ao concluir].");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1600));
				insert(serv);			
			} 
			serv = new Servico();    	
			serv.setServico("activities.itemmodel.sa.oncompletion");

			if (select(serv) == null) {
				serv.setDescricao("Edição da Aba [Ao concluir].");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1700));
				insert(serv);			
			} 

			serv = new Servico();    	
			serv.setServico("act.aba.conclusao");

			if (select(serv) == null) {
				serv.setDescricao("Edição da Aba [Conclusão do Ato].");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(2200));
				insert(serv);			
			} 


			serv = new Servico();    	
			serv.setServico("act.aba.oncompletion");

			if (select(serv) == null) {
				serv.setDescricao("Edição da Aba [Ao concluir].");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(2200));
				insert(serv);			
			} 


			serv = new Servico();    	
			serv.setServico("complete.act.save");

			if (select(serv) == null) {
				serv.setDescricao("Guardar alterações na Aba [Conclusão do Ato].");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(2200));
				insert(serv);			
			} 
			

			serv = new Servico();    	
			serv.setServico("play.aba.conclusao");

			if (select(serv) == null) {
				serv.setDescricao("Edição da Aba [Conclusão do Play].");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(2100));
				insert(serv);			
			} 


			serv = new Servico();    	
			serv.setServico("play.aba.oncompletion");

			if (select(serv) == null) {
				serv.setDescricao("Edição da Aba [Ao concluir].");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(2100));
				insert(serv);			
			} 


			serv = new Servico();    	
			serv.setServico("complete.play.save");

			if (select(serv) == null) {
				serv.setDescricao("Guardar alterações na Aba [Conclusão do Play].");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(2100));
				insert(serv);			
			} 

			serv = new Servico();    	
			serv.setServico("ldep.aba.conclusao");

			if (select(serv) == null) {
				serv.setDescricao("Edição da Aba [Conclusão da Unidade de Aprendizagem].");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1300));
				insert(serv);			
			} 


			serv = new Servico();    	
			serv.setServico("ldep.aba.oncompletion");

			if (select(serv) == null) {
				serv.setDescricao("Edição da Aba [Ao concluir].");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1300));
				insert(serv);			
			} 


			serv = new Servico();    	
			serv.setServico("complete.ldep.save");

			if (select(serv) == null) {
				serv.setDescricao("Guardar alterações na Aba [Conclusão da Unidade de Aprendizagem].");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1300));
				insert(serv);			
			} 
/*
			serv = new Servico();    	
			serv.setServico("environment.aba.environment.refs");

			if (select(serv) == null) {
				serv.setDescricao("Edição da Aba [Outros Ambientes].");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1900));
				insert(serv);			
			} */
/*
			serv = new Servico();    	
			serv.setServico("environment.aba.learning.objects");

			if (select(serv) == null) {
				serv.setDescricao("%IME%2sv.environment.aba.learning.objects");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1900));
				insert(serv);			
			} */

/*
			serv = new Servico();    	
			serv.setServico("environment.remove.lo");

			if (select(serv) == null) {
				serv.setDescricao("%IME%2sv.environment.remove.lo");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1900));
				insert(serv);			
			} 
			*/

/*
			serv = new Servico();    	
			serv.setServico("environment.aba.services");

			if (select(serv) == null) {
				serv.setDescricao("%IME%2sv.environment.aba.services");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1900));
				insert(serv);			
			} 
*/
			
/*

			serv = new Servico();    	
			serv.setServico("environment.sv.email.edit");

			if (select(serv) == null) {
				serv.setDescricao("%IME%2sv.environment.sv.email.edit");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1900));
				insert(serv);			
			} 



			serv = new Servico();    	
			serv.setServico("environment.sv.conference.edit");

			if (select(serv) == null) {
				serv.setDescricao("%IME%2sv.environment.sv.conference.edit");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1900));
				insert(serv);			
			} 



			serv = new Servico();    	
			serv.setServico("environment.sv.index.edit");

			if (select(serv) == null) {
				serv.setDescricao("%IME%2sv.environment.sv.index.edit");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1900));
				insert(serv);			
			}  
			*/
			
			serv = new Servico();    	
			serv.setServico("env.lo.edit");

			if (select(serv) == null) {
				serv.setDescricao("%IME%2sv.env.lo.edit");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1900));
				insert(serv);			
			}  

			serv = new Servico();    	
			serv.setServico("env.lo.edit.new");

			if (select(serv) == null) {
				serv.setDescricao("%IME%2sv.env.lo.edit.new");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1900));
				insert(serv);			
			} 

			
			serv = new Servico();    	
			serv.setServico("env.sv.edit");

			if (select(serv) == null) {
				serv.setDescricao("%IME%2sv.env.sv.edit");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1900));
				insert(serv);			
			}  

			serv = new Servico();    	
			serv.setServico("env.sv.edit.new");

			if (select(serv) == null) {
				serv.setDescricao("%IME%2sv.env.sv.edit.new");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(1900));
				insert(serv);			
			}  
			
			version = Suport.strBRToDate("24/08/2013 00:01:00");
			if ( version.getTimeInMillis() > dataVersionAtual.getTimeInMillis()) {
				dataVersionAtual = version;
				servVersionAtual.setDescricao("24/08/2013 00:01:00");
				upDate(servVersionAtual);	
				stm.execute("ALTER TABLE usuario ADD COLUMN perfil integer;");
				stm.execute("ALTER TABLE usuario ADD CONSTRAINT usuario_perfil_fkey FOREIGN KEY (perfil) REFERENCES usuario (id) MATCH SIMPLE ON UPDATE CASCADE ON DELETE SET NULL;");
			}

			version = Suport.strBRToDate("24/08/2013 00:01:01");
			if ( version.getTimeInMillis() > dataVersionAtual.getTimeInMillis()) {
				dataVersionAtual = version;
				servVersionAtual.setDescricao("24/08/2013 00:01:01");
				upDate(servVersionAtual);	
				stm.execute("ALTER TABLE usuario ADD COLUMN tipo_perfil character varying DEFAULT 'perfil.livre';");
			}
			
			
			
			
			

		/*	serv = new Servico();    	
			serv.setServico("im.item.edit.new");

			if (select(serv) == null) {
				serv.setDescricao("Edição de itens novos. (Dependerá se o usuário tem acesso ao modelo. Pré-requisitos e Objetivos de Aprendizagem são exemplos de modelos.)");
				serv.setAtivo(true);
				serv.setAutenticar(true);
				serv.setTipo(1);
				serv.setGrupo(new Grp_servicos(200));
				insert(serv);			
			} */


			stm.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean insert(Servico serv) {

		String sql = DAOConnection.constructSQLInsert(serv);

		try {
			int result = DAOConnection.executeInsertUpDateDelete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean upDate(Servico serv) {


		String sql = DAOConnection.constructSQLUpDate(serv);

		try {
			int result = DAOConnection.executeInsertUpDateDelete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}


		return true;
	}

	public Servico select(Servico servico) {

		try {	

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;			

			if (servico.getId() != 0)
				sql = "select * from servico s, grp_servicos gs where (grupo = gs.id or grupo is null)  and s.id = " + servico.getId() + ";";
			else 
				sql = "select * from servico s, grp_servicos gs where (grupo = gs.id or grupo is null) and s.servico = '" + servico.getServico() + "';";			

			pStm = DAOConnection.createPreparedStatement(sql);
			rs = pStm.executeQuery();

			if (rs == null) {
				return null;
			}

			if (!rs.next()) {
				return null;
			}

			servico.setId(rs.getInt("id"));
			servico.setDescricao(rs.getString("descricao"));
			servico.setServico(rs.getString("servico"));
			servico.setAtivo(rs.getBoolean("ativo"));
			servico.setAutenticar(rs.getBoolean("autenticar"));
			servico.setTipo(rs.getInt("tipo"));

			if (rs.getInt("grupo") != 0) {
				servico.setGrupo(new Grp_servicos(rs.getInt("grupo")));
				servico.getGrupo().setTitulo(rs.getString("titulo"));
			} 
			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException ex) {
			return null;
		}
		return servico;
	}

	@Override
	public List<Servico> selectAll() {

		List<Servico> result = new ArrayList<Servico>();
		Servico servico;

		try {	

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;			

			sql = "select * from servico s, grp_servicos gs where (grupo = gs.id)  and ativo = true order by grupo, descricao;";

			pStm = DAOConnection.createPreparedStatement(sql);
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


					if (rs.getInt("grupo") != 0) {
						servico.setGrupo(new Grp_servicos(rs.getInt("grupo")));
						servico.getGrupo().setTitulo(rs.getString("titulo"));
					} 
					result.add(servico);
				}
			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException ex) {

		}
		return result;
	}

	public List<Servico> selectAllAutenticaveis() {

		List<Servico> result = new ArrayList<Servico>();
		Servico servico;

		try {	

			ResultSet rs = null;
			String sql;
			PreparedStatement pStm;			

			sql = "select * from servico s, grp_servicos gs where (grupo = gs.id) and autenticar = true and ativo = true order by grupo, descricao;";

			pStm = DAOConnection.createPreparedStatement(sql);
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


					if (rs.getInt("grupo") != 0) {
						servico.setGrupo(new Grp_servicos(rs.getInt("grupo")));
						servico.getGrupo().setTitulo(rs.getString("titulo"));
					} 
					result.add(servico);
				}
			DAOConnection.closeObjects(rs, pStm);


		} catch (SQLException ex) {

		}
		return result;
	}
}
