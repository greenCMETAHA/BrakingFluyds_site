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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import eftech.workingset.DAO.interfaces.InterfaceCountryDAO;
import eftech.workingset.DAO.interfaces.InterfaceEngineTypeDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.EngineType;

public class EngineTypeTemplate implements InterfaceEngineTypeDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	
	
	public  int getCountRows(){
		String sqlQuery="select count(*) from engineType where engineType.name!=:empty";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("empty", Service.EMPTY);
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}				
	}	
	
	@Override
	public EngineType getEngineType(int id) {
		String sqlQuery="select * from EngineType as c where c.id=:id";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new EngineTypeRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new EngineType();
		}		
	}

	@Override
	public EngineType getEngineTypeByName(String engineType) {
		String sqlQuery="select * from EngineType as et where et.name=:name";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", engineType);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new EngineTypeRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new EngineType();
		}		
	}
	
	@Override
	public EngineType createEngineType(String name) {
		EngineType result=getEngineTypeByName(name);
		if (result.getId()==0){
			String sqlUpdate="INSERT INTO EngineType (name) VALUES (:name)";
			
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("name", name);
			
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
			try{
				result=(EngineType)getEngineType(keyHolder.getKey().intValue());
			}catch (EmptyResultDataAccessException e){
				return new EngineType();
			}		
		}  
		 		
		return result;
	}
	
	@Override
	public EngineType createEngineType(EngineType engineType) {
		EngineType result=new EngineType();
		EngineType currentEngineType = null;
		if (engineType.getId()>0){
			currentEngineType=getEngineType(engineType.getId()); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}else{
			currentEngineType=engineType;
		}
		String sqlUpdate="insert into EngineType (name) Values (:name)";
		if (currentEngineType.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update EngineType set name=:name where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", engineType.getName());
	
		if (currentEngineType.getId()>0){ // В БД есть элемент
			params.addValue("id", currentEngineType.getId());
		}
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getEngineType(keyHolder.getKey().intValue());
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new EngineType();
		}		
		
		return result;
	}
	
	@Override
	public ArrayList<EngineType> getEngineTypes() {
		return getEngineTypes(0,0);	
	}

	@Override
	public ArrayList<EngineType> getEngineTypes(int num, int nextRows) {
		String sqlQuery="select * from EngineType where engineType.name!=:empty order by name"
				+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("empty", Service.EMPTY);
		
		try{
			return (ArrayList<EngineType>) jdbcTemplate.query(sqlQuery, params, new EngineTypeRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<EngineType>();
		}
	}

	@Override
	public void deleteEngineType(EngineType engineType) {
		deleteEngineType(engineType.getId());
		
	}

	@Override
	public void deleteEngineType(int id) {
		String sqlUpdate="delete from EngineType where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id",  id);
		
		jdbcTemplate.update(sqlUpdate, params);
	}
	
	private static final class EngineTypeRowMapper implements RowMapper<EngineType> {

		@Override
		public EngineType mapRow(ResultSet rs, int rowNum) throws SQLException {
			EngineType result=new EngineType();

			result.setId(rs.getInt("id"));
			result.setName(rs.getString("name"));
			
			return result;
		}

	}
}
