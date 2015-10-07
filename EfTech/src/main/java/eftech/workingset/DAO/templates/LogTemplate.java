package eftech.workingset.DAO.templates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import eftech.workingset.DAO.interfaces.InterfaceLogDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Info;
import eftech.workingset.beans.Log;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.Role;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;

public class LogTemplate implements InterfaceLogDAO {
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	

	@Override
	public ArrayList<Log> getLog() {
		return getLog(0, 0);
	}
	
	public  int getCountRows(){
		String sqlQuery="select count(*) from Log";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}				
		
		
	}

	@Override
	public ArrayList<Log> getLog(int num, int nextRows) { //num - номер страницы
		String sqlQuery="select log.id AS id, log.info AS info, log.time AS time, log.object AS object, log.object_id AS object_id, log.object_name AS object_name"
					+ ", u.id AS User_id, u.name AS User_name, u.login AS User_login"
					+ " from log"
					+ " left join users AS u ON (log.user=u.id) where u.id=:id"
					+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);
			
		MapSqlParameterSource params = new MapSqlParameterSource();
	
		try{ 
			return (ArrayList<Log>)jdbcTemplate.query(sqlQuery,params,new LogRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Log>();
		}catch (InvalidDataAccessApiUsageException e){
			return new ArrayList<Log>();				
		}
	}

	@Override
	public Log getLogById(int id) {
		String sqlQuery="select * from Log where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{ 
			return (Log)jdbcTemplate.query(sqlQuery,new LogRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Log();
		}
	}
	
	@Override
	public Log createLog(Log log){
		Log result=new Log();
		Log currentLog = log;
		if (log.getId()>0){
			currentLog=getLogById(log.getId()); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}
		String sqlUpdate="insert into Log (user, time, object, object_id, object_name, info) Values (:user, :time, :object, :object_id, :object_name, :info)";
		if (currentLog.getId()>0){ // В БД есть такой элемент
			if (log.getObject()==null){
				sqlUpdate="update review set user=:user, info=:info where id=:id";
			}else{
				sqlUpdate="update review set user=:user, time=:time, object=:object, object_id=:object_id, object_name=:object_name, info=:info where id=:id";
			}
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("user", ((User)log.getUser()).getId());
		params.addValue("info", log.getInfo());
		if (log.getObject()!=null){
			params.addValue("time", log.getTime());
			Object obj=log.getObject();
			Class c = obj.getClass(); 
			
			params.addValue("object", c.getName());
			try {
				params.addValue("object_id", c.getField("id").get(obj));
				params.addValue("object_name", c.getField("name").get(obj));
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if (currentLog.getId()>0){ 
			params.addValue("id", log.getId());
		}
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getLogById(keyHolder.getKey().intValue());
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new Log();
		}		
		
		return result;	
	}
	
	@Override
	public void deleteLog(Log log) {
		deleteLog(log.getId());
		
	}

	@Override
	public void deleteLog(int id) {
		String sqlUpdate="delete from Log where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id",  id);
		
		jdbcTemplate.update(sqlUpdate, params);
	}

	private static final class LogRowMapper implements RowMapper<Log> {

		@Override
		public Log mapRow(ResultSet rs, int rowNum) throws SQLException {
			Log log = new Log();
			log.setId(rs.getInt("id"));
			log.setInfo(rs.getString("info"));
			String strObj=rs.getString("object");
			Object object=null;
			if ("User".equals(strObj)){
				object=new User();
				((User) object).setId(rs.getInt("object_id"));
				((User) object).setName(rs.getString("object_name"));
			}else if ("BrakingFluid".equals(strObj)){
				object=new BrakingFluid();
				((BrakingFluid) object).setId(rs.getInt("object_id"));
				((BrakingFluid) object).setName(rs.getString("object_name"));
			}else if ("Client".equals(strObj)){
				object=new Client();
				((Client) object).setId(rs.getInt("object_id"));
				((Client) object).setName(rs.getString("object_name"));
			}else if ("Country".equals(strObj)){
				object=new Country();
				((Country) object).setId(rs.getInt("object_id"));
				((Country) object).setName(rs.getString("object_name"));
			}else if ("FluidClass".equals(strObj)){
				object=new FluidClass();
				((FluidClass) object).setId(rs.getInt("object_id"));
				((FluidClass) object).setName(rs.getString("object_name"));
			}else if ("Info".equals(strObj)){
				object=new Info();
				((Info) object).setId(rs.getInt("object_id"));
				((Info) object).setName(rs.getString("object_name"));
			}else if ("Log".equals(strObj)){
				object=new Log();
				((Log) object).setId(rs.getInt("object_id"));
				((Log) object).setInfo(rs.getString("object_name"));
			}else if ("Manufacturer".equals(strObj)){
				object=new Manufacturer();
				((Manufacturer) object).setId(rs.getInt("object_id"));
				((Manufacturer) object).setName(rs.getString("object_name"));
			}else if ("Review".equals(strObj)){
				object=new Review();
				((Review) object).setId(rs.getInt("object_id"));
				((Review) object).setReview(rs.getString("object_name"));
			}else if ("Wishlist".equals(strObj)){
				object=new Wishlist();
				((Wishlist) object).setId(rs.getInt("object_id"));
				BrakingFluid currentBF=new BrakingFluid();
				currentBF.setId(new Integer(rs.getInt("object_id")));
				((Wishlist) object).setBrakingFluid(currentBF);
			}
			log.setObject(object);
			User user=new User();
			user.setId(rs.getInt("user_id"));
			user.setName(rs.getString("user_name"));
			user.setLogin(rs.getString("user_login"));
			log.setUser(user);
			
			return log;
		}
	}	
}
