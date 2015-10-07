package eftech.workingset.DAO.templates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import eftech.workingset.DAO.interfaces.InterfaceRoleDAO;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.Role;
import eftech.workingset.beans.User;

public class RoleTemplate implements InterfaceRoleDAO {
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	
	
	@Override
	public ArrayList<Role> getRoles() {
		String sqlQuery="select * from role order by name";

		try{
			return (ArrayList<Role>) jdbcTemplate.query(sqlQuery, new RoleRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Role>();
		}		
	}
	
	@Override
	public Role getRole(int id) {
		String sqlQuery="select * from roles where id=:id";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new RoleRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Role();
		}		
	}
	
	@Override
	public ArrayList<Role> getRolesForUser(String user_login) {
		String sqlQuery="select roles.name, roles.id from roles"
				+" left join users on (roles.id=users.role) where users.login=:login group by roles.name";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", user_login);

		try{
			return (ArrayList<Role>)jdbcTemplate.query(sqlQuery, params, new RoleRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Role>();
		}		
	}
	
	private static final class RoleRowMapper implements RowMapper<Role> {

		@Override
		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role result=new Role();

			result.setId(rs.getInt("id"));
			result.setName(rs.getString("name"));
			
			return result;
		}

	}


}
