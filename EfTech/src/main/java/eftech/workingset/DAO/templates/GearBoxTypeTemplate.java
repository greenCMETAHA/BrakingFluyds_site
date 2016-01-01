package eftech.workingset.DAO.templates;

import eftech.workingset.DAO.interfaces.InterfaceGearBoxTypeDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.GearBoxType;
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

public class GearBoxTypeTemplate implements InterfaceGearBoxTypeDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public  int getCountRows(){
		String sqlQuery="select count(*) from gearboxtype where gearboxtype.name!=:empty";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("empty", Service.EMPTY);

		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}
	}

	@Override
	public GearBoxType getGearBoxType(int id) {
		String sqlQuery="select * from gearboxtype as gbt where gbt.id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new GearBoxTypeRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new GearBoxType();
		}
	}

	@Override
	public GearBoxType getGearBoxTypeByName(String engineType) {
		String sqlQuery="select * from gearboxtype as gbt where gbt.name=:name";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", engineType);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new GearBoxTypeRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new GearBoxType();
		}
	}

	@Override
	public GearBoxType createGearBoxType(String name) {
		GearBoxType result=getGearBoxTypeByName(name);
		if (result.getId()==0){
			String sqlUpdate="INSERT INTO gearboxtype (name) VALUES (:name)";

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("name", name);

			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(sqlUpdate, params, keyHolder);

			try{
				result=(GearBoxType)getGearBoxType(keyHolder.getKey().intValue());
			}catch (EmptyResultDataAccessException e){
				return new GearBoxType();
			}
		}

		return result;
	}

	@Override
	public GearBoxType createGearBoxType(GearBoxType gearBoxType) {
		GearBoxType result=new GearBoxType();
		GearBoxType currentGearBoxType = null;
		if (gearBoxType.getId()>0){
			currentGearBoxType=getGearBoxType(gearBoxType.getId()); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}else{
			currentGearBoxType=gearBoxType;
		}
		String sqlUpdate="insert into gearboxtype (name) Values (:name)";
		if (currentGearBoxType.getId()>0){ // В БД есть такой элемент
			sqlUpdate="update gearboxtype set name=:name where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", gearBoxType.getName());

		if (currentGearBoxType.getId()>0){ // В БД есть элемент
			params.addValue("id", currentGearBoxType.getId());
		}

		KeyHolder keyHolder=new GeneratedKeyHolder();

		jdbcTemplate.update(sqlUpdate, params, keyHolder);

		try{
			if (keyHolder.getKey()!=null) {
				result=getGearBoxType(keyHolder.getKey().intValue());
			}

		}catch (EmptyResultDataAccessException e){
			result = new GearBoxType();
		}

		return result;
	}

	@Override
	public ArrayList<GearBoxType> getGearBoxTypes() {
		return getGearBoxTypes(0,0);
	}

	@Override
	public ArrayList<GearBoxType> getGearBoxTypes(int num, int nextRows) {
		String sqlQuery="select * from gearboxtype where gearboxtype.name!=:empty order by name"
				+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("empty", Service.EMPTY);

		try{
			return (ArrayList<GearBoxType>) jdbcTemplate.query(sqlQuery, params, new GearBoxTypeRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<GearBoxType>();
		}
	}

	@Override
	public void deleteGearBoxType(GearBoxType engineType) {
		deleteGearBoxType(engineType.getId());

	}

	@Override
	public void deleteGearBoxType(int id) {
		String sqlUpdate="delete from gearboxtype where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id",  id);

		jdbcTemplate.update(sqlUpdate, params);
	}

	private static final class GearBoxTypeRowMapper implements RowMapper<GearBoxType> {

		@Override
		public GearBoxType mapRow(ResultSet rs, int rowNum) throws SQLException {
			GearBoxType result=new GearBoxType();

			result.setId(rs.getInt("id"));
			result.setName(rs.getString("name"));

			return result;
		}

	}
}
