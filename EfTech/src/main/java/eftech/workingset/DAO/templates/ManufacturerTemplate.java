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
import eftech.workingset.Services.Service;
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
	
	public  int getCountRows(){
		String sqlQuery="select count(*) from Manufacturer where isManufacturer=1";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
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
				+ " where (man.name=:name) and (isManufacturer=1) group by man.name";

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
			String sqlUpdate="INSERT INTO manufacturer (name, country, isManufacturer) VALUES (:name,:countryId,:isManufacturer)";

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("name", name);
			params.addValue("countryId", countryId);
			params.addValue("isManufacturer", 1);
			
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
	
	@Override
	public Manufacturer createManufacturer(Manufacturer manufacturer) {
		Manufacturer result=new Manufacturer();
		Manufacturer currentManufacturer = null;
		if (manufacturer.getId()>0){
			currentManufacturer=getManufacturer(manufacturer.getId()); //���� ��� ��������������, � ��������� ��� ����� Id. ����� �������������, ��� ����� ������� ���� � ��
		}else{
			currentManufacturer=manufacturer;
		}
		String sqlUpdate="insert into manufacturer (name, country, isManufacturer) Values (:name, :country, :isManufacturer)";
		if (currentManufacturer.getId()>0){ // � �� ���� ����� �������
			 sqlUpdate="update manufacturer set name=:name, country=:country, isManufacturer=:isManufacturer where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", manufacturer.getName());
		params.addValue("country", ((Country)manufacturer.getCountry()).getId());
		params.addValue("isManufacturer", 1);
	
		if (currentManufacturer.getId()>0){ // � �� ���� �������
			params.addValue("id", currentManufacturer.getId());
		}
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getManufacturer(keyHolder.getKey().intValue());
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new Manufacturer();
		}		
		
		return result;
	}	
	
	@Override
	public ArrayList<Manufacturer> getManufacturers() {
		return getManufacturers(0,0);
	}	

	@Override
	public ArrayList<Manufacturer> getManufacturers(int num, int nextRows) {
		String sqlQuery="select *, c.name AS c_name from manufacturer as man, country AS c "
				+ "where (man.country=c.id) and (isManufacturer=1) order by man.name"
				+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);
		
		try{
			return (ArrayList<Manufacturer>)jdbcTemplate.query(sqlQuery, new ManufacturerRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Manufacturer>();
		}	
	}
	
	@Override
	public void deleteManufacturer(Manufacturer manufacturer) {
		deleteManufacturer(manufacturer.getId());
	}

	@Override
	public void deleteManufacturer(int id) {
		String sqlUpdate="delete from Manufacturer where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id",  id);
		
		jdbcTemplate.update(sqlUpdate, params);
		
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
