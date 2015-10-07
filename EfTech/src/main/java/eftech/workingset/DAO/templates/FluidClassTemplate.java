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

import eftech.workingset.DAO.interfaces.InterfaceFluidClassDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Role;

public class FluidClassTemplate implements InterfaceFluidClassDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	
	
	public  int getCountRows(){
		String sqlQuery="select count(*) from fluidclass";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}				
		
		
	}	
		
	@Override
	public FluidClass getFluidClass(int id) {
		String sqlQuery="select * from FluidClass where id=:id";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new FluidClassRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new FluidClass();
		}		
	}
	
	@Override
	public FluidClass getFluidClassByName(String name) {
		String sqlQuery="select * from FluidClass where name=:name";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", name);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new FluidClassRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new FluidClass();
		}		
	}	
	
	@Override
	public FluidClass createFluidClass(String name) {
		FluidClass result=getFluidClassByName(name);
		if (result.getName().length()==0){
			String sqlUpdate="INSERT INTO fluidClass (name) VALUES (:name)";
			
			KeyHolder keyHolder = new GeneratedKeyHolder();

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("name", name);
			jdbcTemplate.update(sqlUpdate, params, keyHolder);
			System.out.println(keyHolder.getKey().intValue());

			try{
				result=(FluidClass)getFluidClass(keyHolder.getKey().intValue());
			}catch (EmptyResultDataAccessException e){
				return new FluidClass();
			}		
		}  
		 		
		return result;
	}
	
	@Override
	public ArrayList<FluidClass> getFluidClassis() {
		return getFluidClassis(0,0);
	}

	@Override
	public ArrayList<FluidClass> getFluidClassis(int num, int nextRows) {
		String sqlQuery="select * from FluidClass  order by name "
				+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);
		
		try{
			return (ArrayList<FluidClass>) jdbcTemplate.query(sqlQuery, new FluidClassRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<FluidClass>();
		}		
	}

	@Override
	public void deleteFluidClass(FluidClass fluidClass) {
		deleteFluidClass(fluidClass.getId());
		
	}

	@Override
	public void deleteFluidClass(int id) {
		String sqlUpdate="delete from Client where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id",  id);
		
		jdbcTemplate.update(sqlUpdate, params);
	}
	
	
	private static final class FluidClassRowMapper implements RowMapper<FluidClass> {

		@Override
		public FluidClass mapRow(ResultSet rs, int rowNum) throws SQLException {
			 FluidClass result=new FluidClass();

			 result.setId(rs.getInt("id"));
			 result.setName(rs.getString("name"));
			 
			 return result;
		 }
	}
	
}
