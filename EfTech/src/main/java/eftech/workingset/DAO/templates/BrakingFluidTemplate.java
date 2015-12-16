package eftech.workingset.DAO.templates;

import eftech.workingset.DAO.interfaces.InterfaceBrakingFluidDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class BrakingFluidTemplate implements InterfaceBrakingFluidDAO {
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	private String strSearchFilter(String searchField){
		String result=null;
		if (searchField.length()>0){
			result=" and ((bf.name like '%"+searchField+"%') or (bf.description like '%"+searchField+"%')"
					+ " or (bf.Specification like '%"+searchField+"%') or (man.name like '%"+searchField+"%') or (fc.name like '%"+searchField+"%'))";
		}
		return result;
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
		String sqlUpdate="insert into brakingfluids (name, boilingtemperaturedry, boilingtemperaturewet, description, fluidclass, judgement, manufacturer"
				+ ", photo, price, specification, value, viscosity40, viscosity100, discount, instock) Values (:name, :boilingTemperatureDry"
				+ ", :boilingTemperatureWet, :description, :fluidClass, :judgement, :manufacturer, :photo, :price, :specification, :value"
				+ ", :viscosity40, :viscosity100, :discount, :instock)";
		if (currentBrFluid.getId()>0){ // В БД есть такой элемент
			sqlUpdate="update brakingfluids set name=:name, boilingtemperaturedry=:boilingTemperatureDry"
					+ ", boilingtemperaturewet=:boilingTemperatureWet, description=:description, fluidclass=:fluidClass, judgement=:judgement"
					+ ", manufacturer=:manufacturer, photo=:photo, price=:price, specification=:specification, value=:value"
					+ ", viscosity40=:viscosity40, viscosity100=:viscosity100, discount=:discount, instock=:instock where id=:id";
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
		params.addValue("discount", brFluid.getDiscount());
		params.addValue("instock", brFluid.getInStock());

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
	public BrakingFluid createBrakingFluidWithoutPrice(BrakingFluid brFluid) {
		BrakingFluid result=new BrakingFluid();
		BrakingFluid currentBrFluid = null;
		if (brFluid.getId()>0){
			currentBrFluid=getBrakingFluid(brFluid.getId()); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}else{
			currentBrFluid=getBrakingFluidByName(brFluid.getName());
		}
		String sqlUpdate="insert into brakingfluids (name, boilingtemperaturedry, boilingtemperaturewet, description, fluidclass, judgement, manufacturer"
				+ ", photo, specification, value, viscosity40, viscosity100, discount, instock) Values (:name, :boilingTemperatureDry"
				+ ", :boilingTemperatureWet, :description, :fluidClass, :judgement, :manufacturer, :photo, :specification, :value"
				+ ", :viscosity40, :viscosity100, :discount, :instock)";
		if (currentBrFluid.getId()>0){ // В БД есть такой элемент
			sqlUpdate="update brakingfluids set name=:name, boilingtemperaturedry=:boilingTemperatureDry"
					+ ", boilingtemperaturewet=:boilingTemperatureWet, description=:description, fluidclass=:fluidClass, judgement=:judgement"
					+ ", manufacturer=:manufacturer, photo=:photo, specification=:specification, value=:value"
					+ ", viscosity40=:viscosity40, viscosity100=:viscosity100, discount=:discount, instock=:instock where id=:id";
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
		params.addValue("specification", brFluid.getSpecification());
		params.addValue("value", brFluid.getValue());
		params.addValue("viscosity40", brFluid.getViscosity40());
		params.addValue("viscosity100", brFluid.getViscosity100());
		params.addValue("discount", brFluid.getDiscount());
		params.addValue("instock", brFluid.getInStock());
		
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
			String sqlUpdate="update brakingfluids set price=:price where id=:id";

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id", brFluid.getId());
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

	@Override
	public double minData(String param){
		double result = 0;

		String sqlQuery="select MIN("+param+") as "+param+" from brakingfluids";
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

		String sqlQuery="select MAX("+param+") as "+param+" from brakingfluids";
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
			,double currentMinJudgementFilter,double currentMaxJudgementFilter, String searchField){
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
		
		String strSearchFilter=null;
		if (searchField.length()>0){
			strSearchFilter=" and (("+strSearchFilter+" in bf.name) or ("+strSearchFilter+" in bf.description) or ("
					+strSearchFilter+" in bf.Specification) or ("+strSearchFilter+" in man.name) or ("+strSearchFilter+" in fc.name)) ";
		}
		
		
		String sqlQuery="select count(*) from"
				+ "(select bf.id as id from brakingfluids  as bf"
				+ "		left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ "		left join manufacturer as man on bf.manufacturer=man.id "
				+ "				where (bf.price>=:minPrice) and (bf.price<=:maxPrice)"
				+ "				 and  (bf.boilingtemperaturedry>=:currentMinBoilingTemperatureDryFilter) and (bf.boilingtemperaturedry<=:currentMaxBoilingTemperatureDryFilter)"
				+ "				 and  (bf.boilingtemperaturewet>=:currentMinBoilingTemperatureWetFilter) and (bf.boilingtemperaturewet<=:currentMaxBoilingTemperatureWetFilter)"
				+ "				 and  (bf.value>=:currentMinValueFilter) and (bf.value<=:currentMaxValueFilter)"
				+ "				 and  (bf.viscosity40>=:currentMinViscosity40Filter) and (bf.viscosity40<=:currentMaxViscosity40Filter)"
				+ "				 and  (bf.viscosity100>=:currentMinViscosity100Filter) and (bf.viscosity100<=:currentMaxViscosity100Filter)"
				+ "				 and  (bf.judgement>=:currentMinJudgementFilter) and (bf.judgement<=:currentMaxJudgementFilter)"
				+ " "+strFluidClassFilter+strManufacturerFilter+strSearchFilter(searchField)+" ORDER BY bf.name ) as rez";

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
	public ArrayList<BrakingFluid> getBrakingFluids(int currentPage, int elementsInList
			,LinkedList<ManufacturerSelected> manufacturersSelected, LinkedList<FluidClassSelected>fluidClassFilter
			,double minPrice, double maxPrice
			,double currentMinBoilingTemperatureDryFilter,double currentMaxBoilingTemperatureDryFilter
			,double currentMinBoilingTemperatureWetFilter,double currentMaxBoilingTemperatureWetFilter
			,double currentMinValueFilter,double currentMaxValueFilter
			,double currentMinViscosity40Filter,double currentMaxViscosity40Filter
			,double currentMinViscosity100Filter,double currentMaxViscosity100Filter
			,double currentMinJudgementFilter,double currentMaxJudgementFilter, String searchField){
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
				+ "(select bf.id as id, bf.name as name, bf.price as price, bf.boilingtemperaturedry AS boilingtemperaturedry"
				+ ", bf.boilingtemperaturewet AS boilingtemperaturewet, bf.description AS description, bf.judgement AS judgement"
				+ ", bf.photo AS photo, bf.specification AS specification, bf.viscosity40 AS viscosity40, bf.instock AS instock, bf.discount AS discount"
				+ ", bf.viscosity100 AS viscosity100, bf.value AS value, bf.fluidclass AS fluidclass, bf.manufacturer AS manufacturer"
				+ ", fc.name as fc_name, man.name as man_name from brakingfluids  as bf"
				+ "		left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ "		left join manufacturer as man on bf.manufacturer=man.id "
				+ "				where (bf.price>=:minPrice) and (bf.price<=:maxPrice) "
				+ ""
				+ ""
				+ ""
				+ ""
				+ ""+strFluidClassFilter+strManufacturerFilter+strSearchFilter(searchField)+" ORDER BY bf.name ) as rez"
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
			return (ArrayList<BrakingFluid>)jdbcTemplate.query(sqlQuery,params,new BrakingFluidRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<BrakingFluid>();
		}
	}

	@Override
	public ArrayList<BrakingFluid> getBrakingFluidsRecommended(){
		String sqlQuery="select *, fc.name as fc_name, man.name as man_name from brakingfluids  as bf"
				+ " left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ " left join manufacturer as man on bf.manufacturer=man.id ORDER BY bf.judgement DESC LIMIT :firstRow, :number";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("firstRow", 0);
		params.addValue("number", Service.ELEMENTS_IN_RECOMMENDED);

		try{
			return (ArrayList<BrakingFluid>)jdbcTemplate.query(sqlQuery,params,new BrakingFluidRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<BrakingFluid>();
		}
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
			brFluid.setDiscount(rs.getDouble("discount"));
			brFluid.setInStock(rs.getInt("instock"));

			return brFluid;
		}
	}

}
