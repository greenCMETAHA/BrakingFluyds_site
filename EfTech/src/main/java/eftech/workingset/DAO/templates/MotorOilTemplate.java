package eftech.workingset.DAO.templates;

import eftech.workingset.DAO.interfaces.InterfaceMotorOilDAO;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MotorOilTemplate implements InterfaceMotorOilDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	private String strSearchFilter(String searchField){
		String result="";
	
		if (searchField.length()>0){
			result=" and ((mo.name like '%"+searchField+"%') or (mo.description  like '%"+searchField+"%') or (et.name like '%"+searchField+"%') "
					+ "or (man.name like '%"+searchField+"%') or (os.name  like '%"+searchField+"%')) ";
		}
		
		return result;
	}
	
	
	
	@Override
	public ArrayList<MotorOil> getMotorOils() {
		String sqlQuery="select *, os.name as os_name, et.name as et_name, man.name as man_name from motoroils  as mo"
				+ " LEFT JOIN oilstuff AS os ON (mo.oilstuff=os.id)"
				+ " LEFT JOIN engineType AS et ON (mo.enginetype=et.id)"
				+ " left join manufacturer as man on mo.manufacturer=man.id ORDER BY mo.name";

		try{
			return (ArrayList<MotorOil>)jdbcTemplate.query(sqlQuery,new MotorOilRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<MotorOil>();
		}
	}

	@Override
	public MotorOil getMotorOil(int id) {
		String sqlQuery="select *, os.name as os_name, et.name as et_name, man.name as man_name from motoroils  as mo"
				+ " LEFT JOIN oilstuff AS os ON (mo.oilstuff=os.id)"
				+ " LEFT JOIN enginetype AS et ON (mo.enginetype=et.id)"
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
		String sqlQuery="select *, os.name as os_name, et.name as et_name, man.name as man_name from motoroils  as mo"
				+ " LEFT JOIN oilstuff AS os ON (mo.oilstuff=os.id)"
				+ " LEFT JOIN enginetype AS et ON (mo.enginetype=et.id)"
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
		String sqlUpdate="insert into motoroils (name, description, enginetype, oilstuff, manufacturer, judgement, photo, price"
				+ ", specification, value, viscosity, discount, instock, manufacturer_code) "
				+ " Values (:name, :description, :enginetype, :oilstuff, :manufacturer, :judgement, :photo, :price"
				+ ", :specification, :value, :viscosity, :discount, :instock, :manufacturer_code)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		if (currentOil.getId()>0){ // В БД есть такой элемент
			sqlUpdate="update motoroils set name=:name, description=:description, enginetype=:enginetype, oilstuff=:oilstuff, judgement=:judgement"
					+ ", manufacturer=:manufacturer, photo=:photo, price=:price, specification=:specification, value=:value"
					+ ", viscosity=:viscosity, discount=:discount, instock=:instock, manufacturer_code=:manufacturer_code where id=:id";
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
		params.addValue("discount", oil.getDiscount());
		params.addValue("instock", oil.getInStock());
		params.addValue("manufacturer_code", oil.getManufacturerCode());

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
		String sqlUpdate="insert into motoroils (name, description, enginetype, oilstuff, manufacturer, judgement, photo"
				+ ", specification, value, viscosity, discount, instock) Values (:name, :description, :enginetype, :oilstuff, :manufacturer, :judgement, :photo"
				+ ", :specification, :value, :viscosity, :discount, :instock)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		if (currentOil.getId()>0){ // В БД есть такой элемент
			sqlUpdate="update motoroils set name=:name, description=:description, enginetype=:enginetype, oilstuff=:oilstuff, judgement=:judgement"
					+ ", manufacturer=:manufacturer, photo=:photo, specification=:specification, value=:value"
					+ ", viscosity=:viscosity, discount=:discount, instock=:instock where id=:id";
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
		params.addValue("discount", oil.getDiscount());
		params.addValue("instock", oil.getInStock());


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
			String sqlUpdate="update motoroils set price=:price where id=:id";

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

		String sqlQuery="select MIN("+param+") as "+param+" from motoroils";
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

		String sqlQuery="select MAX("+param+") as "+param+" from motoroils";
		MapSqlParameterSource params = new MapSqlParameterSource();

		try{
			result=jdbcTemplate.queryForObject(sqlQuery,params,Double.class);
			return result;
		}catch (EmptyResultDataAccessException e){
			return 100.0;
		}

	}

	@Override
	public ArrayList<String> getMotorOilViscosities() {
		String sqlQuery="select distinct(viscosity) from motoroils order by viscosity";
		MapSqlParameterSource params = new MapSqlParameterSource();

		try{
			ArrayList<String> result=(ArrayList<String>) jdbcTemplate.queryForList(sqlQuery,params,String.class);
			return result;
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<String>();
		}
	}


	@Override
	public  int getCountRows(int currentPage, int elementsInList
			, LinkedList<ManufacturerSelected> manufacturersSelected,  LinkedList<EngineType>engineTypeFilter, LinkedList<OilStuff>oilStuffFilter
			,HashMap<String,Boolean> viscosityFilter, double minPrice, double maxPrice, double currentMinValueFilter,double currentMaxValueFilter
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

		StringBuilder strEngineTypeFilter=new StringBuilder();
		if (engineTypeFilter.size()>0){
			boolean bFind=false;
			for (EngineType current:engineTypeFilter){
				if (current.isSelected()){
					bFind=true;
					break;
				}
			}
			if (bFind){
				for (EngineType current:engineTypeFilter){
					if (current.isSelected()){
						if (strEngineTypeFilter.length()!=0){
							strEngineTypeFilter.append(", ");
						}

						strEngineTypeFilter.append(current.getId());
					}
				}
				strEngineTypeFilter.insert(0, " and (et.id IN (");
				strEngineTypeFilter.append("))");
			}

		}

		StringBuilder strOilStuffFilter=new StringBuilder();
		if (oilStuffFilter.size()>0){
			boolean bFind=false;
			for (OilStuff current:oilStuffFilter){
				if (current.isSelected()){
					bFind=true;
					break;
				}
			}
			if (bFind){
				for (OilStuff current:oilStuffFilter){
					if (current.isSelected()){
						if (strOilStuffFilter.length()!=0){
							strOilStuffFilter.append(", ");
						}

						strOilStuffFilter.append(current.getId());
					}
				}
				strOilStuffFilter.insert(0, " and (os.id IN (");
				strOilStuffFilter.append("))");
			}

		}

		StringBuilder strViscosityFilter=new StringBuilder();
		if (viscosityFilter.size()>0){
			boolean bFind=false;
			for (Map.Entry<String, Boolean> current:viscosityFilter.entrySet()){
				if (current.getValue()){
					bFind=true;
					break;
				}
			}
			if (bFind){
				for (Map.Entry<String, Boolean> current:viscosityFilter.entrySet()){
					if (current.getValue()){
						if (strViscosityFilter.length()!=0){
							strViscosityFilter.append(", ");
						}

						strViscosityFilter.append("'"+current.getKey()+"'");
					}
				}
				strViscosityFilter.insert(0, " and (mo.viscosity IN (");
				strViscosityFilter.append("))");
			}
		}

		String sqlQuery="select count(*) from"
				+ "(select mo.id as id from motoroils  as mo"
				+ " 	LEFT JOIN oilstuff AS os ON (mo.oilstuff=os.id)"
				+ " 	LEFT JOIN enginetype AS et ON (mo.enginetype=et.id)"
				+ "		left join manufacturer as man on mo.manufacturer=man.id "
				+ "				where (mo.price>=:minPrice) and (mo.price<=:maxPrice)"
				+ "				 and  (mo.Value>=:currentMinValueFilter) and (mo.Value<=:currentMaxValueFilter)"
				+ "				 and  (mo.Judgement>=:currentMinJudgementFilter) and (mo.Judgement<=:currentMaxJudgementFilter)"
				+ " "+strEngineTypeFilter+strOilStuffFilter+strManufacturerFilter+strViscosityFilter+strSearchFilter(searchField)
				+" ORDER BY mo.name ) as rez";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("minPrice", minPrice);
		params.addValue("maxPrice", maxPrice);
		params.addValue("currentMinValueFilter", currentMinValueFilter);
		params.addValue("currentMaxValueFilter", currentMaxValueFilter);
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
			,LinkedList<ManufacturerSelected> manufacturersSelected,  LinkedList<EngineType>engineTypeFilter, LinkedList<OilStuff>oilStuffFilter
			,HashMap<String,Boolean> viscosityFilter, double minPrice, double maxPrice, double currentMinValueFilter,double currentMaxValueFilter
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
				strManufacturerFilter.insert(0, " and (man.id IN (");
				strManufacturerFilter.append("))");
			}

		}

		StringBuilder strEngineTypeFilter=new StringBuilder();
		if (engineTypeFilter.size()>0){
			boolean bFind=false;
			for (EngineType current:engineTypeFilter){
				if (current.isSelected()){
					bFind=true;
					break;
				}
			}
			if (bFind){
				for (EngineType current:engineTypeFilter){
					if (current.isSelected()){
						if (strEngineTypeFilter.length()!=0){
							strEngineTypeFilter.append(", ");
						}

						strEngineTypeFilter.append(current.getId());
					}
				}
				strEngineTypeFilter.insert(0, " and (et.id IN (");
				strEngineTypeFilter.append("))");
			}

		}

		StringBuilder strOilStuffFilter=new StringBuilder();
		if (oilStuffFilter.size()>0){
			boolean bFind=false;
			for (OilStuff current:oilStuffFilter){
				if (current.isSelected()){
					bFind=true;
					break;
				}
			}
			if (bFind){
				for (OilStuff current:oilStuffFilter){
					if (current.isSelected()){
						if (strOilStuffFilter.length()!=0){
							strOilStuffFilter.append(", ");
						}

						strOilStuffFilter.append(current.getId());
					}
				}
				strOilStuffFilter.insert(0, " and (os.id IN (");
				strOilStuffFilter.append("))");
			}

		}

		StringBuilder strViscosityFilter=new StringBuilder();
		if (viscosityFilter.size()>0){
			boolean bFind=false;
			for (Map.Entry<String, Boolean> current:viscosityFilter.entrySet()){
				if (current.getValue()){
					bFind=true;
					break;
				}
			}
			if (bFind){
				for (Map.Entry<String, Boolean> current:viscosityFilter.entrySet()){
					if (current.getValue()){
						if (strViscosityFilter.length()!=0){
							strViscosityFilter.append(", ");
						}

						strViscosityFilter.append("'"+current.getKey()+"'");
					}
				}
				strViscosityFilter.insert(0, " and (mo.viscosity IN (");
				strViscosityFilter.append("))");
			}

		}

		String sqlQuery="select * from"
				+ "(select mo.id as id, mo.name as name, mo.price as price"
				+ ", mo.enginetype AS enginetype, et.name AS et_name, mo.oilstuff AS oilstuff, os.name AS os_name"
				+ ", mo.description AS description, mo.judgement AS judgement, mo.instock AS instock, mo.discount AS discount"
				+ ", mo.photo AS photo, mo.specification AS specification, mo.viscosity AS viscosity"
				+ ", mo.value AS value, mo.manufacturer AS manufacturer, man.name as man_name "
				+ "   from motoroils  as mo"
				+ " 	LEFT JOIN oilstuff AS os ON (mo.oilstuff=os.id)"
				+ " 	LEFT JOIN enginetype AS et ON (mo.enginetype=et.id)"
				+ "		left join manufacturer as man on mo.manufacturer=man.id "
				+ "				where (mo.price>=:minPrice) and (mo.price<=:maxPrice)"
				+ "					 and (mo.value>=:currentMinValueFilter) and (mo.value<=:currentMaxValueFilter) "
				+ "					 and (mo.Judgement>=:currentMinJudgementFilter) and (mo.Judgement<=:currentMaxJudgementFilter)"
				+ strEngineTypeFilter+strOilStuffFilter+strManufacturerFilter+strViscosityFilter+strSearchFilter(searchField)
				+ " ORDER BY mo.name ) as rez LIMIT :firstRow, :number";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("firstRow", (currentPage-1)*elementsInList);
		params.addValue("number", elementsInList);
		params.addValue("minPrice", minPrice);
		params.addValue("maxPrice", maxPrice);
		params.addValue("currentMinValueFilter", currentMinValueFilter);
		params.addValue("currentMaxValueFilter", currentMaxValueFilter);
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
		String sqlQuery="select *, os.name as os_name, et.name as et_name, man.name as man_name from motoroils  as mo"
				+ " LEFT JOIN oilstuff AS os ON (mo.oilstuff=os.id)"
				+ " LEFT JOIN enginetype AS et ON (mo.enginetype=et.id)"
				+ " left join manufacturer as man on mo.manufacturer=man.id ORDER BY mo.judgement DESC LIMIT :firstRow, :number";

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

			OilStuff oilStuff =new OilStuff();
			oilStuff.setId(rs.getInt("oilStuff"));
			oilStuff.setName(rs.getString("os_name"));
			oil.setOilStuff(oilStuff);

			EngineType engineType =new EngineType();
			engineType.setId(rs.getInt("engineType"));
			engineType.setName(rs.getString("et_name"));
			oil.setEngineType(engineType);

			oil.setPhoto(rs.getString("photo"));
			oil.setPrice(rs.getDouble("price"));
			oil.setValue(rs.getDouble("value"));
			oil.setViscosity(rs.getString("viscosity"));
			oil.setDiscount(rs.getDouble("discount"));
			oil.setInStock(rs.getInt("instock"));
			

			return oil;
		}
	}


	


}
