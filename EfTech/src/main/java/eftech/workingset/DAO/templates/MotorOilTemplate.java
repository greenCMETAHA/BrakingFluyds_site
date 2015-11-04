package eftech.workingset.DAO.templates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import eftech.workingset.DAO.interfaces.InterfaceMotorOilDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.ManufacturerSelected;
import eftech.workingset.beans.MotorOil;

public class MotorOilTemplate implements InterfaceMotorOilDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	

	@Override
	public ArrayList<MotorOil> getMotorOils() {
		String sqlQuery="select *, fc.name as fc_name, man.name as man_name from MotorOils  as bf"
				+ " left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ " left join manufacturer as man on bf.manufacturer=man.id ORDER BY bf.name";
		
		try{ 
			return (ArrayList<MotorOil>)jdbcTemplate.query(sqlQuery,new MotorOilRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<MotorOil>();
		}
	}

	@Override
	public MotorOil getMotorOil(int id) {
		String sqlQuery="select *, fc.name as fc_name, man.name as man_name from MotorOils  as bf"
				+ " left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ " left join manufacturer as man on bf.manufacturer=man.id"
				+ " where (bf.id=:id)";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new MotorOilRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new MotorOil();
		}

	}
	
	@Override
	public MotorOil getMotorOilByName(String name) {
		String sqlQuery="select *, fc.name as fc_name, man.name as man_name from MotorOils  as bf"
				+ " left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ " left join manufacturer as man on bf.manufacturer=man.id"
				+ " where (bf.name=:name)";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", name);
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new MotorOilRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new MotorOil();
		}			
	}		
	
	@Override
	public MotorOil createMotorOil(MotorOil oil) {
		MotorOil result=new MotorOil();
		MotorOil currentOil = null;
		if (oil.getId()>0){
			currentOil=getMotorOil(oil.getId()); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}else{
			currentOil=getMotorOilByName(oil.getName());
		}
		String sqlUpdate="insert into MotorOils (name, boilingTemperatureDry, boilingTemperatureWet, description, fluidClass, judgement, manufacturer"
				+ ", photo, price, specification, value, viscosity40, viscosity100) Values (:name, :boilingTemperatureDry" 
		 		+ ", :boilingTemperatureWet, :description, :fluidClass, :judgement, :manufacturer, :photo, :price, :specification, :value"
		 		+ ", :viscosity40, :viscosity100)";
		if (currentOil.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update MotorOils set name=:name, boilingTemperatureDry=:boilingTemperatureDry"
			 		+ ", boilingTemperatureWet=:boilingTemperatureWet, description=:description, fluidClass=:fluidClass, judgement=:judgement"
			 		+ ", manufacturer=:manufacturer, photo=:photo, price=:price, specification=:specification, value=:value"
			 		+ ", viscosity40=:viscosity40, viscosity100=:viscosity100 where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", oil.getName());
		params.addValue("description", oil.getDescription());
		
		FluidClass fluidClass=(FluidClass)oil.getFluidClass();
		params.addValue("fluidClass", fluidClass.getId());
		params.addValue("judgement", oil.getJudgement());
		
		Manufacturer manufacturer=(Manufacturer)oil.getManufacturer();
		params.addValue("manufacturer", manufacturer.getId());
		
		params.addValue("photo", oil.getPhoto());
		params.addValue("price", oil.getPrice());
		params.addValue("specification", oil.getSpecification());
		params.addValue("value", oil.getValue());
		params.addValue("viscosity", oil.getViscosity());
		if (currentOil.getId()>0){ // В БД есть элемент
			params.addValue("id", oil.getId());
		}
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getMotorOil(keyHolder.getKey().intValue());
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new MotorOil();
		}		
		
		return result;
	}
	
	@Override
	public MotorOil createMotorOilWithoutPrice(MotorOil oil) {
		MotorOil result=new MotorOil();
		MotorOil currentOil = null;
		if (oil.getId()>0){
			currentOil=getMotorOil(oil.getId()); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}else{
			currentOil=getMotorOilByName(oil.getName());
		}
		String sqlUpdate="insert into MotorOils (name, boilingTemperatureDry, boilingTemperatureWet, description, fluidClass, judgement, manufacturer"
				+ ", photo, specification, value, viscosity40, viscosity100) Values (:name, :boilingTemperatureDry" 
		 		+ ", :boilingTemperatureWet, :description, :fluidClass, :judgement, :manufacturer, :photo, :specification, :value"
		 		+ ", :viscosity40, :viscosity100)";
		if (currentOil.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update MotorOils set name=:name, boilingTemperatureDry=:boilingTemperatureDry"
			 		+ ", boilingTemperatureWet=:boilingTemperatureWet, description=:description, fluidClass=:fluidClass, judgement=:judgement"
			 		+ ", manufacturer=:manufacturer, photo=:photo, specification=:specification, value=:value"
			 		+ ", viscosity40=:viscosity40, viscosity100=:viscosity100 where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", oil.getName());
		params.addValue("description", oil.getDescription());
		
		FluidClass fluidClass=(FluidClass)oil.getFluidClass();
		params.addValue("fluidClass", fluidClass.getId());
		params.addValue("judgement", oil.getJudgement());
		
		Manufacturer manufacturer=(Manufacturer)oil.getManufacturer();
		params.addValue("manufacturer", manufacturer.getId());
		
		params.addValue("photo", oil.getPhoto());
		params.addValue("specification", oil.getSpecification());
		params.addValue("value", oil.getValue());
		params.addValue("viscosity", oil.getViscosity());
		if (currentOil.getId()>0){ // В БД есть элемент
			params.addValue("id", oil.getId());
		}
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getMotorOil(keyHolder.getKey().intValue());
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new MotorOil();
		}		
		
		return result;
	}
	
	@Override
	public MotorOil fillPrices(MotorOil oil) {
		MotorOil result=new MotorOil();
		MotorOil currentOil = getMotorOilByName(oil.getName());
		if (currentOil.getId()>0){
			String sqlUpdate="update MotorOils set price=:price where id=:id";
			
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id", oil.getId());
			params.addValue("price", oil.getPrice());
	
			KeyHolder keyHolder=new GeneratedKeyHolder(); 
			
			jdbcTemplate.update(sqlUpdate, params, keyHolder);
			
			try{
				if (keyHolder.getKey()!=null) {
					result=getMotorOil(keyHolder.getKey().intValue());
				}
			}catch (EmptyResultDataAccessException e){
				result = new MotorOil();
			}					
		}
			
		return result;
	}
	
	@Override
	public double minData(String param){
		double result = 0;
		
		String sqlQuery="select MIN("+param+") as "+param+" from MotorOils";
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try{
			result=jdbcTemplate.queryForObject(sqlQuery,params,Double.class);
			return result;
		}catch (EmptyResultDataAccessException e){
			return 0.0;
		}				
	}
	
	@Override
	public  double maxData(String param){
		double result = 0;
		
		String sqlQuery="select MAX("+param+") as "+param+" from MotorOils";
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try{
			result=jdbcTemplate.queryForObject(sqlQuery,params,Double.class);
			return result;
		}catch (EmptyResultDataAccessException e){
			return 100.0;
		}				

	}
	
	@Override
	public  int getCountRows(int currentPage, int elementsInList
			, LinkedList<ManufacturerSelected> manufacturersSelected, LinkedList<FluidClassSelected>fluidClassFilter
			,double minPrice, double maxPrice
			,double currentMinBoilingTemperatureDryFilter,double currentMaxBoilingTemperatureDryFilter
			,double currentMinBoilingTemperatureWetFilter,double currentMaxBoilingTemperatureWetFilter
			,double currentMinValueFilter,double currentMaxValueFilter
			,double currentMinViscosity40Filter,double currentMaxViscosity40Filter
			,double currentMinViscosity100Filter,double currentMaxViscosity100Filter
			,double currentMinJudgementFilter,double currentMaxJudgementFilter){
		int result = 0;
		
		StringBuilder strManufacturerFilter=new StringBuilder();
		if (manufacturersSelected.size()>0){
			boolean bFind=false;
			for (ManufacturerSelected currentMan:manufacturersSelected){
				if (currentMan.isSelected()){
					bFind=true;
					break;
				}
			}
			if (bFind){
				for (ManufacturerSelected currentMan:manufacturersSelected){
					if (currentMan.isSelected()){
						if (strManufacturerFilter.length()!=0){
							strManufacturerFilter.append(", ");
						}

						strManufacturerFilter.append(currentMan.getId());
					}
				}
				strManufacturerFilter.insert(0, " and (man.id IN (");
				strManufacturerFilter.append("))");
			}
		
		}
		
		StringBuilder strFluidClassFilter=new StringBuilder();
		if (fluidClassFilter.size()>0){
			boolean bFind=false;
			for (FluidClassSelected currentFC:fluidClassFilter){
				if (currentFC.isSelected()){
					bFind=true;
					break;
				}
			}
			if (bFind){
				for (FluidClassSelected currentFC:fluidClassFilter){
					if (currentFC.isSelected()){
						if (strFluidClassFilter.length()!=0){
							strFluidClassFilter.append(", ");
						}

						strFluidClassFilter.append(currentFC.getId());
					}
				}
				strFluidClassFilter.insert(0, " and (fc.id IN (");
				strFluidClassFilter.append("))");
			}
		
		}	
		String sqlQuery="select count(*) from"
				+ "(select bf.id as id from MotorOils  as bf"
				+ "		left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ "		left join manufacturer as man on bf.manufacturer=man.id "
				+ "				where (bf.price>=:minPrice) and (bf.price<=:maxPrice)"
				+ "				 and  (bf.BoilingTemperatureDry>=:currentMinBoilingTemperatureDryFilter) and (bf.BoilingTemperatureDry<=:currentMaxBoilingTemperatureDryFilter)"
				+ "				 and  (bf.BoilingTemperatureWet>=:currentMinBoilingTemperatureWetFilter) and (bf.BoilingTemperatureWet<=:currentMaxBoilingTemperatureWetFilter)"				
				+ "				 and  (bf.Value>=:currentMinValueFilter) and (bf.Value<=:currentMaxValueFilter)"
				+ "				 and  (bf.Viscosity40>=:currentMinViscosity40Filter) and (bf.Viscosity40<=:currentMaxViscosity40Filter)"
				+ "				 and  (bf.Viscosity100>=:currentMinViscosity100Filter) and (bf.Viscosity100<=:currentMaxViscosity100Filter)"
				+ "				 and  (bf.Judgement>=:currentMinJudgementFilter) and (bf.Judgement<=:currentMaxJudgementFilter)"
				+ " "+strFluidClassFilter+strManufacturerFilter+" ORDER BY bf.name ) as rez";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("minPrice", minPrice);
		params.addValue("maxPrice", maxPrice);		
		params.addValue("currentMinBoilingTemperatureDryFilter", currentMinBoilingTemperatureDryFilter);
		params.addValue("currentMaxBoilingTemperatureDryFilter", currentMaxBoilingTemperatureDryFilter);		
		params.addValue("currentMinBoilingTemperatureWetFilter", currentMinBoilingTemperatureWetFilter);
		params.addValue("currentMaxBoilingTemperatureWetFilter", currentMaxBoilingTemperatureWetFilter);		
		params.addValue("currentMinValueFilter", currentMinValueFilter);
		params.addValue("currentMaxValueFilter", currentMaxValueFilter);
		params.addValue("currentMinViscosity40Filter", currentMinViscosity40Filter);
		params.addValue("currentMaxViscosity40Filter", currentMaxViscosity40Filter);		
		params.addValue("currentMinViscosity100Filter", currentMinViscosity100Filter);		
		params.addValue("currentMaxViscosity100Filter", currentMaxViscosity100Filter);
		params.addValue("currentMinJudgementFilter", currentMinJudgementFilter);		
		params.addValue("currentMaxJudgementFilter", currentMaxJudgementFilter);
		
		try{
			result=jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
			return result;
		}catch (EmptyResultDataAccessException e){
			return 0;
		}				

	}	
	
	@Override
	public ArrayList<MotorOil> getMotorOils(int currentPage, int elementsInList
			,LinkedList<ManufacturerSelected> manufacturersSelected, LinkedList<FluidClassSelected>fluidClassFilter
			,double minPrice, double maxPrice
			,double currentMinBoilingTemperatureDryFilter,double currentMaxBoilingTemperatureDryFilter
			,double currentMinBoilingTemperatureWetFilter,double currentMaxBoilingTemperatureWetFilter
			,double currentMinValueFilter,double currentMaxValueFilter
			,double currentMinViscosity40Filter,double currentMaxViscosity40Filter
			,double currentMinViscosity100Filter,double currentMaxViscosity100Filter
			,double currentMinJudgementFilter,double currentMaxJudgementFilter){
		StringBuilder strManufacturerFilter=new StringBuilder();
		if (manufacturersSelected.size()>0){
			boolean bFind=false;
			for (ManufacturerSelected currentMan:manufacturersSelected){
				if (currentMan.isSelected()){
					bFind=true;
					break;
				}
			}
			if (bFind){
				for (ManufacturerSelected currentMan:manufacturersSelected){
					if (currentMan.isSelected()){
						if (strManufacturerFilter.length()!=0){
							strManufacturerFilter.append(", ");
						}

						strManufacturerFilter.append(currentMan.getId());
					}
				}
				strManufacturerFilter.insert(0, "and (man.id IN (");
				strManufacturerFilter.append("))");
			}
		
		}
		
		StringBuilder strFluidClassFilter=new StringBuilder();
		if (fluidClassFilter.size()>0){
			boolean bFind=false;
			for (FluidClassSelected currentFC:fluidClassFilter){
				if (currentFC.isSelected()){
					bFind=true;
					break;
				}
			}
			if (bFind){
				for (FluidClassSelected currentFC:fluidClassFilter){
					if (currentFC.isSelected()){
						if (strFluidClassFilter.length()!=0){
							strFluidClassFilter.append(", ");
						}

						strFluidClassFilter.append(currentFC.getId());
					}
				}
				strFluidClassFilter.insert(0, " and (fc.id IN (");
				strFluidClassFilter.append("))");
			}
		
		}
		
		String sqlQuery="select * from"
				+ "(select bf.id as id, bf.name as name, bf.price as price, bf.boilingTemperatureDry AS boilingTemperatureDry"
				+ ", bf.boilingTemperatureWet AS boilingTemperatureWet, bf.description AS description, bf.judgement AS judgement"
				+ ", bf.photo AS photo, bf.specification AS specification, bf.viscosity40 AS viscosity40"
				+ ", bf.viscosity100 AS viscosity100, bf.value AS value, bf.fluidclass AS fluidclass, bf.manufacturer AS manufacturer"
				+ ", fc.name as fc_name, man.name as man_name from MotorOils  as bf"
				+ "		left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ "		left join manufacturer as man on bf.manufacturer=man.id "
				+ "				where (bf.price>=:minPrice) and (bf.price<=:maxPrice) "
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""+strFluidClassFilter+strManufacturerFilter+" ORDER BY bf.name ) as rez"
				+ " LIMIT :firstRow, :number";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("firstRow", (currentPage-1)*elementsInList);
		params.addValue("number", elementsInList);		
		params.addValue("minPrice", minPrice);
		params.addValue("maxPrice", maxPrice);	
		params.addValue("currentMinBoilingTemperatureDryFilter", currentMinBoilingTemperatureDryFilter);
		params.addValue("currentMaxBoilingTemperatureDryFilter", currentMaxBoilingTemperatureDryFilter);		
		params.addValue("currentMinBoilingTemperatureWetFilter", currentMinBoilingTemperatureWetFilter);
		params.addValue("currentMaxBoilingTemperatureWetFilter", currentMaxBoilingTemperatureWetFilter);		
		params.addValue("currentMinValueFilter", currentMinValueFilter);
		params.addValue("currentMaxValueFilter", currentMaxValueFilter);
		params.addValue("currentMinViscosity40Filter", currentMinViscosity40Filter);
		params.addValue("currentMaxViscosity40Filter", currentMaxViscosity40Filter);		
		params.addValue("currentMinViscosity100Filter", currentMinViscosity100Filter);		
		params.addValue("currentMaxViscosity100Filter", currentMaxViscosity100Filter);
		params.addValue("currentMinJudgementFilter", currentMinJudgementFilter);		
		params.addValue("currentMaxJudgementFilter", currentMaxJudgementFilter);
		
		
		try{ 
			return (ArrayList<MotorOil>)jdbcTemplate.query(sqlQuery,params,new MotorOilRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<MotorOil>();
		}		
	}
	
	@Override
	public ArrayList<MotorOil> getMotorOilsRecommended(){
		String sqlQuery="select *, fc.name as fc_name, man.name as man_name from MotorOils  as bf"
				+ " left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ " left join manufacturer as man on bf.manufacturer=man.id ORDER BY bf.judgement DESC LIMIT :firstRow, :number";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("firstRow", 0);
		params.addValue("number", Service.ELEMENTS_IN_RECOMMENDED);		
		
		try{ 
			return (ArrayList<MotorOil>)jdbcTemplate.query(sqlQuery,params,new MotorOilRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<MotorOil>();
		}				
	}
	

	private static final class MotorOilRowMapper implements RowMapper<MotorOil> {

		@Override
		public MotorOil mapRow(ResultSet rs, int rowNum) throws SQLException {
			 MotorOil oil=new MotorOil();

			 oil.setId(rs.getInt("id"));
			 oil.setName(rs.getString("name"));
			 oil.setDescription(rs.getString("description"));
			 oil.setSpecification(rs.getString("Specification"));
			 oil.setJudgement(rs.getDouble("judgement"));
			 
			 Manufacturer manufacturer =new Manufacturer();
			 manufacturer.setId(rs.getInt("manufacturer"));
			 manufacturer.setName(rs.getString("man_name"));
			 oil.setManufacturer(manufacturer);
			 
			 oil.setPhoto(rs.getString("photo"));
			 oil.setPrice(rs.getDouble("price"));
			 oil.setSpecification(rs.getString("specification"));
			 oil.setValue(rs.getDouble("value"));
			 oil.setViscosity(rs.getString("viscosity"));
			 
		 	 return oil;
		 }
	}
	
}
