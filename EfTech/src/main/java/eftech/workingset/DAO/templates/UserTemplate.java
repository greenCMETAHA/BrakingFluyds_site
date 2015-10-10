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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import eftech.workingset.DAO.interfaces.InterfaceUserDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.Role;
import eftech.workingset.beans.User;

public class UserTemplate implements InterfaceUserDAO  {
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public  int getCountRows(){
		String sqlQuery="select count(distinct login) from users";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}				
		
		
	}	

	public User getUser(String login, String password) {
		String sqlQuery="select us.id, us.name, us.login, us.email, us.password, us.role, rol.name as rol_name "
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
		String sqlQuery="select us.id, us.name, us.login, us.email , us.password, us.role, rol.name as rol_name "
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
		String sqlQuery="select us.id, us.name, us.login, us.email, us.password, us.role, rol.name as rol_name "
				+ "from users as us, roles as rol where us.login = :login and us.role=rol.id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("login", login);

		try{
			ArrayList<User> list=(ArrayList<User>)jdbcTemplate.query(sqlQuery, params, new UserRowMapper());
			
			return list.get(0);
		}catch (EmptyResultDataAccessException e){
			return new User();
		}		

	}	
	
	@Override
	public ArrayList<User> getUsers() {
		return getUsers(0,0);
	}	

	@Override
	public ArrayList<User> getUsers(int num, int nextRows) {
		String sqlQuery="select us.id, us.name, us.login, us.email, us.password	from users as us group by us.login"
				+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);

		try{
			return (ArrayList<User>) jdbcTemplate.query(sqlQuery, new UserRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<User>();
		}		
	}

	@Override
	public void deleteUser(User user) {
		deleteUser(user.getId());
		
	}

	@Override
	public void deleteUser(User user, Role role) {
		deleteUser(user.getLogin(), role.getId());
		
	}

	@Override
	public void deleteUser(int id) {
		String sqlUpdate="delete from users where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id",  id);
		
		jdbcTemplate.update(sqlUpdate, params);
	}
	
	@Override
	public void deleteUser(String login) {
		String sqlUpdate="delete from users where login=:login";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("login",  login);
		
		jdbcTemplate.update(sqlUpdate, params);
	}

	@Override
	public void deleteUser(String login, int role_id) {  //удалим только конкретную роль
		String sqlUpdate="delete from users where login=:login and role=:role_id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("login",  login);
		params.addValue("role_id",  role_id);
		
		jdbcTemplate.update(sqlUpdate, params);
		
	}
	
	@Override
	public User createUserWithRole(User user, Role role) {
			String sqlUpdate="INSERT INTO Users (name, email, login, password, role) VALUES (:name, :email, :login, :password, :role)";
			
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("name", user.getName());
			params.addValue("email", user.getEmail());
			params.addValue("login", user.getLogin());
			params.addValue("password", user.getPassword());
			params.addValue("role", role.getId());
			
			
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
			try{
				return (User)getUser(keyHolder.getKey().intValue());
			}catch (EmptyResultDataAccessException e){
				return new User();
			}		
	}
	
	@Override
	public void updateUsers(User user) {
		String sqlUpdate="update users set name=:name, email=:email, password=:password where login=:login";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", user.getName());
		params.addValue("email", user.getEmail());
		params.addValue("password", user.getPassword());
		params.addValue("login", user.getLogin());
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
	}


	private static final class UserRowMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setLogin(rs.getString("login"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			
			try {
				Role role=new Role(rs.getInt("role"),rs.getString("rol_name"));
				user.setRole(role);
			}catch(Exception e){
			}
			
			return user;
		}

	}

}
