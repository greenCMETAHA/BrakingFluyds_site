package eftech.workingset.DAO.templates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import eftech.workingset.DAO.interfaces.InterfaceBrakingFluidDAO;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.Role;

public class BrakingFluidTemplate implements InterfaceBrakingFluidDAO {
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	ManufacturerTemplate manufacturerDAO;
	
	@Autowired
	FluidClassTemplate fluidClassDAO;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	

	@Override
	public ArrayList<BrakingFluid> getBrakingFluids() {
		String sqlQuery="select *, fc.name as fc_name, man.name as man_name from brakingfluids  as bf"
				+ " left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ " left join manufacturer as man on bf.manufacturer=man.id ORDER BY bf.name";
		
		try{ 
			return (ArrayList<BrakingFluid>)jdbcTemplate.query(sqlQuery,new BrakingFluidRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<BrakingFluid>();
		}
	}

	@Override
	public BrakingFluid getBrakingFluid(int id) {
		String sqlQuery="select *, fc.name as fc_name, man.name as man_name from brakingfluids  as bf"
				+ " left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ " left join manufacturer as man on bf.manufacturer=man.id"
				+ " where (bf.id=:id)";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new BrakingFluidRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new BrakingFluid();
		}

	}
	
	@Override
	public BrakingFluid getBrakingFluidByName(String name) {
		String sqlQuery="select *, fc.name as fc_name, man.name as man_name from brakingfluids  as bf"
				+ " left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ " left join manufacturer as man on bf.manufacturer=man.id"
				+ " where (bf.name=:name)";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", name);
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new BrakingFluidRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new BrakingFluid();
		}			
	}		
	
	@Override
	public BrakingFluid createBrakingFluid(BrakingFluid brFluid) {
		BrakingFluid result=new BrakingFluid();
		BrakingFluid currentBrFluid = null;
		if (brFluid.getId()>0){
			currentBrFluid=getBrakingFluid(brFluid.getId()); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}else{
			currentBrFluid=getBrakingFluidByName(brFluid.getName());
		}
		String sqlUpdate="insert into brakingfluids (name, boilingTemperatureDry, boilingTemperatureWet, description, fluidClass, judgement, manufacturer"
				+ ", photo, price, specification, value, viscosity40, viscosity100) Values (:name, :boilingTemperatureDry" 
		 		+ ", :boilingTemperatureWet, :description, :fluidClass, :judgement, :manufacturer, :photo, :price, :specification, :value"
		 		+ ", :viscosity40, :viscosity100)";
		if (currentBrFluid.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update brakingfluids set name=:name, boilingTemperatureDry=:boilingTemperatureDry"
			 		+ ", boilingTemperatureWet=:boilingTemperatureWet, description=:description, fluidClass=:fluidClass, judgement=:judgement"
			 		+ ", manufacturer=:manufacturer, photo=:photo, price=:price, specification=:specification, value=:value"
			 		+ ", viscosity40=:viscosity40, viscosity100=:viscosity100 where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", brFluid.getName());
		params.addValue("boilingTemperatureDry", brFluid.getBoilingTemperatureDry());
		params.addValue("boilingTemperatureWet", brFluid.getBoilingTemperatureWet());
		params.addValue("description", brFluid.getDescription());
		
		FluidClass fluidClass=(FluidClass)brFluid.getFluidClass();
		params.addValue("fluidClass", fluidClass.getId());
		params.addValue("judgement", brFluid.getJudgement());
		
		Manufacturer manufacturer=(Manufacturer)brFluid.getManufacturer();
		params.addValue("manufacturer", manufacturer.getId());
		
		params.addValue("photo", brFluid.getPhoto());
		params.addValue("price", brFluid.getPrice());
		params.addValue("specification", brFluid.getSpecification());
		params.addValue("value", brFluid.getValue());
		params.addValue("viscosity40", brFluid.getViscosity40());
		params.addValue("viscosity100", brFluid.getViscosity100());
		if (currentBrFluid.getId()>0){ // В БД есть элемент
			params.addValue("id", brFluid.getId());
		}
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getBrakingFluid(keyHolder.getKey().intValue());
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new BrakingFluid();
		}		
		
		return result;
	}
	
	@Override
	public BrakingFluid fillPrices(BrakingFluid brFluid) {
		BrakingFluid result=new BrakingFluid();
		BrakingFluid currentBrFluid = getBrakingFluidByName(brFluid.getName());
		if (currentBrFluid.getId()>0){
			String sqlUpdate="update brakingfluids set price=:price where name=:name";
			
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("name", brFluid.getName());
			params.addValue("price", brFluid.getPrice());
	
			KeyHolder keyHolder=new GeneratedKeyHolder(); 
			
			jdbcTemplate.update(sqlUpdate, params, keyHolder);
			
			try{
				if (keyHolder.getKey()!=null) {
					result=getBrakingFluid(keyHolder.getKey().intValue());
				}
			}catch (EmptyResultDataAccessException e){
				result = new BrakingFluid();
			}					
		}
			
		return result;
	}	

	private static final class BrakingFluidRowMapper implements RowMapper<BrakingFluid> {

		@Override
		public BrakingFluid mapRow(ResultSet rs, int rowNum) throws SQLException {
			 BrakingFluid brFluid=new BrakingFluid();

			 brFluid.setId(rs.getInt("id"));
			 brFluid.setName(rs.getString("name"));
			 brFluid.setBoilingTemperatureDry(rs.getDouble("boilingTemperatureDry"));
			 brFluid.setBoilingTemperatureWet(rs.getDouble("boilingTemperatureWet"));
			 brFluid.setDescription(rs.getString("description"));
			 FluidClass fClass=new FluidClass(rs.getInt("fluidclass"),rs.getString("fc_name"));
			 brFluid.setFluidClass(fClass);
			 brFluid.setJudgement(rs.getDouble("judgement"));
			 Manufacturer manufacturer =new Manufacturer();
			 manufacturer.setId(rs.getInt("manufacturer"));
			 manufacturer.setName(rs.getString("man_name"));
			 brFluid.setManufacturer(manufacturer);
			 brFluid.setPhoto(rs.getString("photo"));
			 brFluid.setPrice(rs.getDouble("price"));
			 brFluid.setSpecification(rs.getString("specification"));
			 brFluid.setValue(rs.getDouble("value"));
			 brFluid.setViscosity40(rs.getDouble("viscosity40"));
			 brFluid.setViscosity100(rs.getDouble("viscosity100"));
			 
		 	 return brFluid;
		 }
	}	
}
