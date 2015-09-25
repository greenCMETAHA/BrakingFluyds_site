package eftech.workingset.DAO.templates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import eftech.workingset.DAO.interfaces.InterfaceUserDAO;
import eftech.workingset.beans.Role;
import eftech.workingset.beans.User;

public class UserTemplate implements InterfaceUserDAO  {
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public User getUser(String login, String password) {
		String sqlQuery="select us.id, us.name, us.login, us.email, us.role, rol.name as rol_name "
				+ "from users as us, roles as rol where login = :login and password=:password and us.role=rol.id";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("login", login);
		params.addValue("password", password);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new UserRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new User();
		}		
	}

	@Override
	public User getUser(int id) {
		String sqlQuery="select us.id, us.name, us.login, us.email, us.role, rol.name as rol_name "
				+ "from users as us, roles as rol where us.id = :id and us.role=rol.id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new UserRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new User();
		}		
	}
	
	@Override
	public User getUser(String login) {
		String sqlQuery="select us.id, us.name, us.login, us.email, us.role, rol.name as rol_name "
				+ "from users as us, roles as rol where us.login = :login and us.role=rol.id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("login", login);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new UserRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new User();
		}		

	}	
	
	@Override
	public ArrayList<User> getUsers() {
		String sqlQuery="select us.id, us.name, us.login, us.email, us.role, rol.name as rol_name "
				+ "from users as us, roles as rol where us.role=rol.id";

		try{
			return (ArrayList<User>) jdbcTemplate.query(sqlQuery, new UserRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<User>();
		}		
	}	

	private static final class UserRowMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setLogin(rs.getString("login"));
			user.setEmail(rs.getString("email"));
			Role role=new Role(rs.getInt("role"),rs.getString("rol_name"));
			user.setRole(role);
			
			return user;
		}

	}
	
}
