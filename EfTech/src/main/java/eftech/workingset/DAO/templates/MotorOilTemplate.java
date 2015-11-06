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
import eftech.workingset.beans.EngineType;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.ManufacturerSelected;
import eftech.workingset.beans.MotorOil;
import eftech.workingset.beans.OilStuff;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

public class MotorOilTemplate implements InterfaceMotorOilDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	

	@Override
	public ArrayList<MotorOil> getMotorOils() {
		String sqlQuery="select *, os.name as os_name, et.name as et_name, man.name as man_name from MotorOils  as mo"
				+ " LEFT JOIN oilstuff AS os ON (mo.oilStuff=os.id)"
				+ " LEFT JOIN engineType AS et ON (mo.engineType=et.id)"
				+ " left join manufacturer as man on mo.manufacturer=man.id ORDER BY mo.name";
		
		try{ 
			return (ArrayList<MotorOil>)jdbcTemplate.query(sqlQuery,new MotorOilRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<MotorOil>();
		}
	}

	@Override
	public MotorOil getMotorOil(int id) {
		String sqlQuery="select *, os.name as os_name, et.name as et_name, man.name as man_name from MotorOils  as mo"
				+ " LEFT JOIN oilstuff AS os ON (mo.oilStuff=os.id)"
				+ " LEFT JOIN engineType AS et ON (mo.engineType=et.id)"
				+ " left join manufacturer as man on mo.manufacturer=man.id"
				+ " where (mo.id=:id)";
		
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
		String sqlQuery="select *, os.name as os_name, et.name as et_name, man.name as man_name from MotorOils  as mo"
				+ " LEFT JOIN oilstuff AS os ON (mo.oilStuff=os.id)"
				+ " LEFT JOIN engineType AS et ON (mo.engineType=et.id)"
				+ " left join manufacturer as man on mo.manufacturer=man.id"
				+ " where (mo.name=:name)";
		
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
		String sqlUpdate="insert into MotorOils (name, description, enginetype, oilstuff, manufacturer, judgement, photo, price"
				+ ", specification, value, viscosity) Values (:name, :description, :enginetype, :oilstuff, :manufacturer, :judgement, :photo, :price"
				+ ", :specification, :value, :viscosity)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		if (currentOil.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update MotorOils set name=:name, description=:description, enginetype=:enginetype, oilstuff=:oilstuff, judgement=:judgement"
			 		+ ", manufacturer=:manufacturer, photo=:photo, price=:price, specification=:specification, value=:value"
			 		+ ", viscosity=:viscosity where id=:id";
			 params.addValue("id", oil.getId());
		}
		
		params.addValue("name", oil.getName());
		params.addValue("description", oil.getDescription());
		
		params.addValue("enginetype", ((EngineType)oil.getEngineType()).getId());
		params.addValue("oilstuff",   ((OilStuff)oil.getOilStuff()).getId());
		params.addValue("judgement", oil.getJudgement());
		params.addValue("manufacturer", ((Manufacturer)oil.getManufacturer()).getId());
		
		params.addValue("photo", oil.getPhoto());
		params.addValue("price", oil.getPrice());
		params.addValue("specification", oil.getSpecification());
		params.addValue("value", oil.getValue());
		params.addValue("viscosity", oil.getViscosity());
		
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
		String sqlUpdate="insert into MotorOils (name, description, enginetype, oilstuff, manufacturer, judgement, photo"
				+ ", specification, value, viscosity) Values (:name, :description, :enginetype, :oilstuff, :manufacturer, :judgement, :photo"
				+ ", :specification, :value, :viscosity)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		if (currentOil.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update MotorOils set name=:name, description=:description, enginetype=:enginetype, oilstuff=:oilstuff, judgement=:judgement"
			 		+ ", manufacturer=:manufacturer, photo=:photo, specification=:specification, value=:value"
			 		+ ", viscosity=:viscosity where id=:id";
			 params.addValue("id", oil.getId());
		}

		params.addValue("name", oil.getName());
		params.addValue("description", oil.getDescription());
		
		params.addValue("enginetype", ((EngineType)oil.getEngineType()).getId());
		params.addValue("oilstuff",   ((OilStuff)oil.getOilStuff()).getId());
		params.addValue("judgement", oil.getJudgement());
		params.addValue("manufacturer", ((Manufacturer)oil.getManufacturer()).getId());
		
		params.addValue("photo", oil.getPhoto());
		params.addValue("specification", oil.getSpecification());
		params.addValue("value", oil.getValue());
		params.addValue("viscosity", oil.getViscosity());
		
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
	
	//@Override
//	public  int getCountRows(int currentPage, int elementsInList
//			, LinkedList<ManufacturerSelected> manufacturersSelected, LinkedList<FluidClassSelected>fluidClassFilter
//			,double minPrice, double maxPrice
//			,double currentMinBoilingTemperatureDryFilter,double currentMaxBoilingTemperatureDryFilter
//			,double currentMinBoilingTemperatureWetFilter,double currentMaxBoilingTemperatureWetFilter
//			,double currentMinValueFilter,double currentMaxValueFilter
//			,double currentMinViscosity40Filter,double currentMaxViscosity40Filter
//			,double currentMinViscosity100Filter,double currentMaxViscosity100Filter
//			,double currentMinJudgementFilter,double currentMaxJudgementFilter){
//		int result = 0;
//		
//		StringBuilder strManufacturerFilter=new StringBuilder();
//		if (manufacturersSelected.size()>0){
//			boolean bFind=false;
//			for (ManufacturerSelected currentMan:manufacturersSelected){
//				if (currentMan.isSelected()){
//					bFind=true;
//					break;
//				}
//			}
//			if (bFind){
//				for (ManufacturerSelected currentMan:manufacturersSelected){
//					if (currentMan.isSelected()){
//						if (strManufacturerFilter.length()!=0){
//							strManufacturerFilter.append(", ");
//						}
//
//						strManufacturerFilter.append(currentMan.getId());
//					}
//				}
//				strManufacturerFilter.insert(0, " and (man.id IN (");
//				strManufacturerFilter.append("))");
//			}
//		
//		}
//		
//		StringBuilder strFluidClassFilter=new StringBuilder();
//		if (fluidClassFilter.size()>0){
//			boolean bFind=false;
//			for (FluidClassSelected currentFC:fluidClassFilter){
//				if (currentFC.isSelected()){
//					bFind=true;
//					break;
//				}
//			}
//			if (bFind){
//				for (FluidClassSelected currentFC:fluidClassFilter){
//					if (currentFC.isSelected()){
//						if (strFluidClassFilter.length()!=0){
//							strFluidClassFilter.append(", ");
//						}
//
//						strFluidClassFilter.append(currentFC.getId());
//					}
//				}
//				strFluidClassFilter.insert(0, " and (fc.id IN (");
//				strFluidClassFilter.append("))");
//			}
//		
//		}	
//		String sqlQuery="select count(*) from"
//				+ "(select mo.id as id from MotorOils  as mo"
//				+ "		left join fluidclass as fc on (mo.fluidclass=fc.id)"
//				+ "		left join manufacturer as man on mo.manufacturer=man.id "
//				+ "				where (mo.price>=:minPrice) and (mo.price<=:maxPrice)"
//				+ "				 and  (mo.Value>=:currentMinValueFilter) and (mo.Value<=:currentMaxValueFilter)"
//				+ "				 and  (mo.Viscosity>=:currentMinViscosity40Filter) and (mo.Viscosity40<=:currentMaxViscosity40Filter)"
//				+ "				 and  (mo.Viscosity100>=:currentMinViscosity100Filter) and (mo.Viscosity100<=:currentMaxViscosity100Filter)"
//				+ "				 and  (mo.Judgement>=:currentMinJudgementFilter) and (mo.Judgement<=:currentMaxJudgementFilter)"
//				+ " "+strFluidClassFilter+strManufacturerFilter+" ORDER BY mo.name ) as rez";
//		
//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue("minPrice", minPrice);
//		params.addValue("maxPrice", maxPrice);		
//		params.addValue("currentMinValueFilter", currentMinValueFilter);
//		params.addValue("currentMaxValueFilter", currentMaxValueFilter);
//		params.addValue("currentMinViscosity40Filter", currentMinViscosity40Filter);
//		params.addValue("currentMaxViscosity40Filter", currentMaxViscosity40Filter);		
//		params.addValue("currentMinViscosity100Filter", currentMinViscosity100Filter);		
//		params.addValue("currentMaxViscosity100Filter", currentMaxViscosity100Filter);
//		params.addValue("currentMinJudgementFilter", currentMinJudgementFilter);		
//		params.addValue("currentMaxJudgementFilter", currentMaxJudgementFilter);
//		
//		try{
//			result=jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
//			return result;
//		}catch (EmptyResultDataAccessException e){
//			return 0;
//		}				
//
//	}	
//	
//	@Override
//	public ArrayList<MotorOil> getMotorOils(int currentPage, int elementsInList
//			,LinkedList<ManufacturerSelected> manufacturersSelected, LinkedList<FluidClassSelected>fluidClassFilter
//			,double minPrice, double maxPrice
//			,double currentMinBoilingTemperatureDryFilter,double currentMaxBoilingTemperatureDryFilter
//			,double currentMinBoilingTemperatureWetFilter,double currentMaxBoilingTemperatureWetFilter
//			,double currentMinValueFilter,double currentMaxValueFilter
//			,double currentMinViscosity40Filter,double currentMaxViscosity40Filter
//			,double currentMinViscosity100Filter,double currentMaxViscosity100Filter
//			,double currentMinJudgementFilter,double currentMaxJudgementFilter){
//		StringBuilder strManufacturerFilter=new StringBuilder();
//		if (manufacturersSelected.size()>0){
//			boolean bFind=false;
//			for (ManufacturerSelected currentMan:manufacturersSelected){
//				if (currentMan.isSelected()){
//					bFind=true;
//					break;
//				}
//			}
//			if (bFind){
//				for (ManufacturerSelected currentMan:manufacturersSelected){
//					if (currentMan.isSelected()){
//						if (strManufacturerFilter.length()!=0){
//							strManufacturerFilter.append(", ");
//						}
//
//						strManufacturerFilter.append(currentMan.getId());
//					}
//				}
//				strManufacturerFilter.insert(0, "and (man.id IN (");
//				strManufacturerFilter.append("))");
//			}
//		
//		}
//		
//		StringBuilder strFluidClassFilter=new StringBuilder();
//		if (fluidClassFilter.size()>0){
//			boolean bFind=false;
//			for (FluidClassSelected currentFC:fluidClassFilter){
//				if (currentFC.isSelected()){
//					bFind=true;
//					break;
//				}
//			}
//			if (bFind){
//				for (FluidClassSelected currentFC:fluidClassFilter){
//					if (currentFC.isSelected()){
//						if (strFluidClassFilter.length()!=0){
//							strFluidClassFilter.append(", ");
//						}
//
//						strFluidClassFilter.append(currentFC.getId());
//					}
//				}
//				strFluidClassFilter.insert(0, " and (fc.id IN (");
//				strFluidClassFilter.append("))");
//			}
//		
//		}
//		
//		String sqlQuery="select * from"
//				+ "(select mo.id as id, mo.name as name, mo.price as price, mo.boilingTemperatureDry AS boilingTemperatureDry"
//				+ ", mo.boilingTemperatureWet AS boilingTemperatureWet, mo.description AS description, mo.judgement AS judgement"
//				+ ", mo.photo AS photo, mo.specification AS specification, mo.viscosity40 AS viscosity40"
//				+ ", mo.viscosity100 AS viscosity100, mo.value AS value, mo.fluidclass AS fluidclass, mo.manufacturer AS manufacturer"
//				+ ", fc.name as fc_name, man.name as man_name from MotorOils  as mo"
//				+ "		left join fluidclass as fc on (mo.fluidclass=fc.id)"
//				+ "		left join manufacturer as man on mo.manufacturer=man.id "
//				+ "				where (mo.price>=:minPrice) and (mo.price<=:maxPrice) "
//				+ ""
//				+ ""
//				+ ""
//				+ ""
//				+ ""+strFluidClassFilter+strManufacturerFilter+" ORDER BY mo.name ) as rez"
//				+ " LIMIT :firstRow, :number";
//		
//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue("firstRow", (currentPage-1)*elementsInList);
//		params.addValue("number", elementsInList);		
//		params.addValue("minPrice", minPrice);
//		params.addValue("maxPrice", maxPrice);	
//		params.addValue("currentMinBoilingTemperatureDryFilter", currentMinBoilingTemperatureDryFilter);
//		params.addValue("currentMaxBoilingTemperatureDryFilter", currentMaxBoilingTemperatureDryFilter);		
//		params.addValue("currentMinBoilingTemperatureWetFilter", currentMinBoilingTemperatureWetFilter);
//		params.addValue("currentMaxBoilingTemperatureWetFilter", currentMaxBoilingTemperatureWetFilter);		
//		params.addValue("currentMinValueFilter", currentMinValueFilter);
//		params.addValue("currentMaxValueFilter", currentMaxValueFilter);
//		params.addValue("currentMinViscosity40Filter", currentMinViscosity40Filter);
//		params.addValue("currentMaxViscosity40Filter", currentMaxViscosity40Filter);		
//		params.addValue("currentMinViscosity100Filter", currentMinViscosity100Filter);		
//		params.addValue("currentMaxViscosity100Filter", currentMaxViscosity100Filter);
//		params.addValue("currentMinJudgementFilter", currentMinJudgementFilter);		
//		params.addValue("currentMaxJudgementFilter", currentMaxJudgementFilter);
//		
//		
//		try{ 
//			return (ArrayList<MotorOil>)jdbcTemplate.query(sqlQuery,params,new MotorOilRowMapper());
//		}catch (EmptyResultDataAccessException e){
//			return new ArrayList<MotorOil>();
//		}		
//	}
//	
//	@Override
//	public ArrayList<MotorOil> getMotorOilsRecommended(){
//		String sqlQuery="select *, fc.name as fc_name, man.name as man_name from MotorOils  as mo"
//				+ " left join fluidclass as fc on (mo.fluidclass=fc.id)"
//				+ " left join manufacturer as man on mo.manufacturer=man.id ORDER BY mo.judgement DESC LIMIT :firstRow, :number";
//		
//		MapSqlParameterSource params = new MapSqlParameterSource();
//		params.addValue("firstRow", 0);
//		params.addValue("number", Service.ELEMENTS_IN_RECOMMENDED);		
//		
//		try{ 
//			return (ArrayList<MotorOil>)jdbcTemplate.query(sqlQuery,params,new MotorOilRowMapper());
//		}catch (EmptyResultDataAccessException e){
//			return new ArrayList<MotorOil>();
//		}				
//	}
	

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

			 OilStuff oilStuff =new OilStuff();
			 oilStuff.setId(rs.getInt("oilStuff"));
			 oilStuff.setName(rs.getString("os_name"));
			 oil.setOilStuff(oilStuff);
			 
			 EngineType engineType =new EngineType();
			 manufacturer.setId(rs.getInt("engineType"));
			 manufacturer.setName(rs.getString("et_name"));
			 oil.setEngineType(engineType);
			 
			 oil.setPhoto(rs.getString("photo"));
			 oil.setPrice(rs.getDouble("price"));
			 oil.setValue(rs.getDouble("value"));
			 oil.setViscosity(rs.getString("viscosity"));
			 
		 	 return oil;
		 }
	}
	
}
