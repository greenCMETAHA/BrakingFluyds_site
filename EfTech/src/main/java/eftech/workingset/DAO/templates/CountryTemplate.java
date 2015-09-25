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
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.Role;

public class CountryTemplate implements InterfaceCountryDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	
	
	@Override
	public ArrayList<Country> getCountries() {
		String sqlQuery="select * from country order by name";
		
		try{
			return (ArrayList<Country>) jdbcTemplate.query(sqlQuery, new CountryRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Country>();
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
