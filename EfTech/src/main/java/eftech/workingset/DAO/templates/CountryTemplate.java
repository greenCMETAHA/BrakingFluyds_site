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
import eftech.workingset.Services.Service;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.Role;

public class CountryTemplate implements InterfaceCountryDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	
	
	public  int getCountRows(){
		String sqlQuery="select count(*) from country";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}				
	}	
	
	@Override
	public Country getCountry(int id) {
		String sqlQuery="select * from country as c where c.id=:id";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new CountryRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Country();
		}		
	}

	@Override
	public Country getCountryByName(String country) {
		String sqlQuery="select * from country as c where c.name=:name";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", country);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new CountryRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Country();
		}		
	}
	
	@Override
	public Country createCountry(String name) {
		Country result=getCountryByName(name);
		if (result.getId()==0){
			String sqlUpdate="INSERT INTO country (name) VALUES (:name)";
			
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("name", name);
			
			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
			try{
				result=(Country)getCountry(keyHolder.getKey().intValue());
			}catch (EmptyResultDataAccessException e){
				return new Country();
			}		
		}  
		 		
		return result;
	}
	
	@Override
	public ArrayList<Country> getCountries() {
		return getCountries(0,0);	
	}

	@Override
	public ArrayList<Country> getCountries(int num, int nextRows) {
		String sqlQuery="select * from country order by name"
				+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);
		
		try{
			return (ArrayList<Country>) jdbcTemplate.query(sqlQuery, new CountryRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Country>();
		}
	}

	@Override
	public void deleteCountry(Country country) {
		deleteCountry(country.getId());
		
	}

	@Override
	public void deleteCountry(int id) {
		String sqlUpdate="delete from Country where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id",  id);
		
		jdbcTemplate.update(sqlUpdate, params);
	}
	
	private static final class CountryRowMapper implements RowMapper<Country> {

		@Override
		public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
			Country result=new Country();

			result.setId(rs.getInt("id"));
			result.setName(rs.getString("name"));
			
			return result;
		}

	}
}
