package eftech.workingset.DAO.templates;

import eftech.workingset.DAO.interfaces.InterfaceGearBoxOilDAO;
import eftech.workingset.DAO.interfaces.InterfaceGearBoxOilDAO;
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

public class GearBoxOilTemplate implements InterfaceGearBoxOilDAO{
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
	public ArrayList<GearBoxOil> getGearBoxOils() {
		String sqlQuery="select *, os.name as os_name, gbt.name as gbt_name, man.name as man_name from   as mo"
				+ " LEFT JOIN oilstuff AS os ON (mo.oilstuff=os.id)"
				+ " LEFT JOIN engineType AS gbt ON (mo.gearboxtype=gbt.id)"
				+ " left join manufacturer as man on mo.manufacturer=man.id ORDER BY mo.name";

		try{
			return (ArrayList<GearBoxOil>)jdbcTemplate.query(sqlQuery,new GearBoxOilRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<GearBoxOil>();
		}
	}

	@Override
	public GearBoxOil getGearBoxOil(int id) {
		String sqlQuery="select *, os.name as os_name, gbt.name as gbt_name, man.name as man_name from motoroils  as mo"
				+ " LEFT JOIN oilstuff AS os ON (mo.oilstuff=os.id)"
				+ " LEFT JOIN gearboxtype AS gbt ON (mo.gearboxtype=gbt.id)"
				+ " left join manufacturer as man on mo.manufacturer=man.id"
				+ " where (mo.id=:id)";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new GearBoxOilRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new GearBoxOil();
		}

	}

	@Override
	public GearBoxOil getGearBoxOilByName(String name) {
		String sqlQuery="select *, os.name as os_name, gbt.name as gbt_name, man.name as man_name from motoroils  as mo"
				+ " LEFT JOIN oilstuff AS os ON (mo.oilstuff=os.id)"
				+ " LEFT JOIN gearboxtype AS gbt ON (mo.gearboxtype=gbt.id)"
				+ " left join manufacturer as man on mo.manufacturer=man.id"
				+ " where (mo.name=:name)";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", name);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new GearBoxOilRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new GearBoxOil();
		}
	}

	@Override
	public GearBoxOil createGearBoxOil(GearBoxOil oil) {
		GearBoxOil result=new GearBoxOil();
		GearBoxOil currentOil = null;
		if (oil.getId()>0){
			currentOil=getGearBoxOil(oil.getId()); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}else{
			currentOil=getGearBoxOilByName(oil.getName());
		}
		String sqlUpdate="insert into motoroils (name, description, gearboxtype, oilstuff, manufacturer, judgement, photo, price"
				+ ", specification, value, viscosity, discount, instock) Values (:name, :description, :gearboxtype, :oilstuff, :manufacturer, :judgement, :photo, :price"
				+ ", :specification, :value, :viscosity, :discount, :instock)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		if (currentOil.getId()>0){ // В БД есть такой элемент
			sqlUpdate="update motoroils set name=:name, description=:description, gearboxtype=:gearboxtype, oilstuff=:oilstuff, judgement=:judgement"
					+ ", manufacturer=:manufacturer, photo=:photo, price=:price, specification=:specification, value=:value"
					+ ", viscosity=:viscosity, discount=:discount, instock=:instock where id=:id";
			params.addValue("id", oil.getId());
		}

		params.addValue("name", oil.getName());
		params.addValue("description", oil.getDescription());

		params.addValue("gearboxtype", ((GearBoxType)oil.getGearBoxType()).getId());
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

		KeyHolder keyHolder=new GeneratedKeyHolder();

		jdbcTemplate.update(sqlUpdate, params, keyHolder);

		try{
			if (keyHolder.getKey()!=null) {
				result=getGearBoxOil(keyHolder.getKey().intValue());
			}

		}catch (EmptyResultDataAccessException e){
			result = new GearBoxOil();
		}

		return result;
	}

	@Override
	public GearBoxOil createGearBoxOilWithoutPrice(GearBoxOil oil) {
		GearBoxOil result=new GearBoxOil();
		GearBoxOil currentOil = null;
		if (oil.getId()>0){
			currentOil=getGearBoxOil(oil.getId()); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}else{
			currentOil=getGearBoxOilByName(oil.getName());
		}
		String sqlUpdate="insert into motoroils (name, description, gearboxtype, oilstuff, manufacturer, judgement, photo"
				+ ", specification, value, viscosity, discount, instock) Values (:name, :description, :gearboxtype, :oilstuff, :manufacturer, :judgement, :photo"
				+ ", :specification, :value, :viscosity, :discount, :instock)";
		MapSqlParameterSource params = new MapSqlParameterSource();
		if (currentOil.getId()>0){ // В БД есть такой элемент
			sqlUpdate="update motoroils set name=:name, description=:description, gearboxtype=:gearboxtype, oilstuff=:oilstuff, judgement=:judgement"
					+ ", manufacturer=:manufacturer, photo=:photo, specification=:specification, value=:value"
					+ ", viscosity=:viscosity, discount=:discount, instock=:instock where id=:id";
			params.addValue("id", oil.getId());
		}

		params.addValue("name", oil.getName());
		params.addValue("description", oil.getDescription());

		params.addValue("gearboxtype", ((GearBoxType)oil.getGearBoxType()).getId());
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
				result=getGearBoxOil(keyHolder.getKey().intValue());
			}

		}catch (EmptyResultDataAccessException e){
			result = new GearBoxOil();
		}

		return result;
	}

	@Override
	public GearBoxOil fillPrices(GearBoxOil oil) {
		GearBoxOil result=new GearBoxOil();
		GearBoxOil currentOil = getGearBoxOilByName(oil.getName());
		if (currentOil.getId()>0){
			String sqlUpdate="update motoroils set price=:price where id=:id";

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id", oil.getId());
			params.addValue("price", oil.getPrice());

			KeyHolder keyHolder=new GeneratedKeyHolder();

			jdbcTemplate.update(sqlUpdate, params, keyHolder);

			try{
				if (keyHolder.getKey()!=null) {
					result=getGearBoxOil(keyHolder.getKey().intValue());
				}
			}catch (EmptyResultDataAccessException e){
				result = new GearBoxOil();
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
	public ArrayList<String> getGearBoxOilViscosities() {
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
			, LinkedList<ManufacturerSelected> manufacturersSelected,  LinkedList<GearBoxType>gearBoxTypeFilter, LinkedList<OilStuff>oilStuffFilter
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

		StringBuilder strGearBoxTypeFilter=new StringBuilder();
		if (gearBoxTypeFilter.size()>0){
			boolean bFind=false;
			for (GearBoxType current:gearBoxTypeFilter){
				if (current.isSelected()){
					bFind=true;
					break;
				}
			}
			if (bFind){
				for (GearBoxType current:gearBoxTypeFilter){
					if (current.isSelected()){
						if (strGearBoxTypeFilter.length()!=0){
							strGearBoxTypeFilter.append(", ");
						}

						strGearBoxTypeFilter.append(current.getId());
					}
				}
				strGearBoxTypeFilter.insert(0, " and (gbt.id IN (");
				strGearBoxTypeFilter.append("))");
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
				+ " 	LEFT JOIN gearboxtype AS gbt ON (mo.gearboxtype=gbt.id)"
				+ "		left join manufacturer as man on mo.manufacturer=man.id "
				+ "				where (mo.price>=:minPrice) and (mo.price<=:maxPrice)"
				+ "				 and  (mo.Value>=:currentMinValueFilter) and (mo.Value<=:currentMaxValueFilter)"
				+ "				 and  (mo.Judgement>=:currentMinJudgementFilter) and (mo.Judgement<=:currentMaxJudgementFilter)"
				+ " "+strGearBoxTypeFilter+strOilStuffFilter+strManufacturerFilter+strViscosityFilter+strSearchFilter(searchField)
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
	public ArrayList<GearBoxOil> getGearBoxOils(int currentPage, int elementsInList
			,LinkedList<ManufacturerSelected> manufacturersSelected,  LinkedList<GearBoxType>gearBoxTypeFilter, LinkedList<OilStuff>oilStuffFilter
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

		StringBuilder strGearBoxTypeFilter=new StringBuilder();
		if (gearBoxTypeFilter.size()>0){
			boolean bFind=false;
			for (GearBoxType current:gearBoxTypeFilter){
				if (current.isSelected()){
					bFind=true;
					break;
				}
			}
			if (bFind){
				for (GearBoxType current:gearBoxTypeFilter){
					if (current.isSelected()){
						if (strGearBoxTypeFilter.length()!=0){
							strGearBoxTypeFilter.append(", ");
						}

						strGearBoxTypeFilter.append(current.getId());
					}
				}
				strGearBoxTypeFilter.insert(0, " and (gbt.id IN (");
				strGearBoxTypeFilter.append("))");
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
				+ ", mo.gearboxtype AS gearboxtype, gbt.name AS gbt_name, mo.oilstuff AS oilstuff, os.name AS os_name"
				+ ", mo.description AS description, mo.judgement AS judgement, mo.instock AS instock, mo.discount AS discount"
				+ ", mo.photo AS photo, mo.specification AS specification, mo.viscosity AS viscosity"
				+ ", mo.value AS value, mo.manufacturer AS manufacturer, man.name as man_name "
				+ "   from motoroils  as mo"
				+ " 	LEFT JOIN oilstuff AS os ON (mo.oilstuff=os.id)"
				+ " 	LEFT JOIN gearboxtype AS gbt ON (mo.gearboxtype=gbt.id)"
				+ "		left join manufacturer as man on mo.manufacturer=man.id "
				+ "				where (mo.price>=:minPrice) and (mo.price<=:maxPrice)"
				+ "					 and (mo.value>=:currentMinValueFilter) and (mo.value<=:currentMaxValueFilter) "
				+ "					 and (mo.Judgement>=:currentMinJudgementFilter) and (mo.Judgement<=:currentMaxJudgementFilter)"
				+ strGearBoxTypeFilter+strOilStuffFilter+strManufacturerFilter+strViscosityFilter+strSearchFilter(searchField)
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
			return (ArrayList<GearBoxOil>)jdbcTemplate.query(sqlQuery,params,new GearBoxOilRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<GearBoxOil>();
		}
	}

	@Override
	public ArrayList<GearBoxOil> getGearBoxOilsRecommended(){
		String sqlQuery="select *, os.name as os_name, gbt.name as gbt_name, man.name as man_name from motoroils  as mo"
				+ " LEFT JOIN oilstuff AS os ON (mo.oilstuff=os.id)"
				+ " LEFT JOIN gearboxtype AS bgt ON (mo.gearboxtype=gbt.id)"
				+ " left join manufacturer as man on mo.manufacturer=man.id ORDER BY mo.judgement DESC LIMIT :firstRow, :number";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("firstRow", 0);
		params.addValue("number", Service.ELEMENTS_IN_RECOMMENDED);

		try{
			return (ArrayList<GearBoxOil>)jdbcTemplate.query(sqlQuery,params,new GearBoxOilRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<GearBoxOil>();
		}
	}


	private static final class GearBoxOilRowMapper implements RowMapper<GearBoxOil> {

		@Override
		public GearBoxOil mapRow(ResultSet rs, int rowNum) throws SQLException {
			GearBoxOil oil=new GearBoxOil();

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

			GearBoxType gearBoxType =new GearBoxType();
			gearBoxType.setId(rs.getInt("gearBoxType"));
			gearBoxType.setName(rs.getString("gbt_name"));
			oil.setGearBoxType(gearBoxType);

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

