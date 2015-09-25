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

import eftech.workingset.DAO.interfaces.InterfaceManufacturerDAO;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.Role;

public class ManufacturerTemplate implements InterfaceManufacturerDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	CountryTemplate countryDAO;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	
	
	@Override
	public ArrayList<Manufacturer> getManufacturers() {
		String sqlQuery="select *, c.name AS c_name from manufacturer as man, country AS c where (man.country=c.id) order by man.name";
		
		try{
			return (ArrayList<Manufacturer>)jdbcTemplate.query(sqlQuery, new ManufacturerRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Manufacturer>();
		}	
	}

	@Override
	public Manufacturer getManufacturer(int id) {
		String sqlQuery="select *, c.name AS c_name from manufacturer as man, country AS c "
				+ "where (man.country=c.id) and (man.id=:id) group by man.name";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new ManufacturerRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Manufacturer();
		}	
	}
	
	@Override
	public Manufacturer getManufacturerByName(String name) {
		String sqlQuery="select *, c.name AS c_name from manufacturer as man"
				+ " left join country AS c  on (man.country=c.id)"
				+ " where (man.name=:name) group by man.name";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", name);
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new ManufacturerRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Manufacturer();
		}	
	}		
	
	@Override
	public Manufacturer createManufacturer(String name) {
		Manufacturer result=getManufacturerByName(name);
		if (result.getName().length()==0){
			String sqlUpdate="INSERT INTO manufacturer (name) VALUES (:name)";

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("name", name);
			
			try{
				result=jdbcTemplate.queryForObject(sqlUpdate, params, new ManufacturerRowMapper());
			}catch (EmptyResultDataAccessException e){
				return new Manufacturer();
			}	
		}  
		 		
		return result;	
	}
	
	@Override
	public Manufacturer createManufacturer(String name, int countryId) {
		Manufacturer result=getManufacturerByName(name);
		if (result.getId()==0){
			String sqlUpdate="INSERT INTO manufacturer (name, country) VALUES (:name,:countryId)";

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("name", name);
			params.addValue("countryId", countryId);
			
			KeyHolder keyHolder=new GeneratedKeyHolder(); 
			
			jdbcTemplate.update(sqlUpdate, params, keyHolder);
			
			try{
				if (keyHolder.getKey()!=null) {
					result=getManufacturer(keyHolder.getKey().intValue());
				}
			}catch (EmptyResultDataAccessException e){
				return new Manufacturer();
			}				
		}  
		 		
		return result;	
		
	}

	private static final class ManufacturerRowMapper implements RowMapper<Manufacturer> {

		@Override
		public Manufacturer mapRow(ResultSet rs, int rowNum) throws SQLException {
			 Manufacturer result=new Manufacturer();

			 result.setId(rs.getInt("id"));
			 result.setName(rs.getString("name"));
			 Country country=new Country(rs.getInt("country"),rs.getString("c_name"));
			 result.setCountry(country);
			 
			 return result;
		}

	}	
}
