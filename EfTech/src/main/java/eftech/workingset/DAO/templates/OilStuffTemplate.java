package eftech.workingset.DAO.templates;

import eftech.workingset.DAO.interfaces.InterfaceOilStuffDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.OilStuff;
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

public class OilStuffTemplate implements InterfaceOilStuffDAO {
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public  int getCountRows(){
		String sqlQuery="select count(*) from oilstuff where oilstuff.name!=:empty";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("empty", Service.EMPTY);

		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}
	}

	@Override
	public OilStuff getOilStuff(int id) {
		String sqlQuery="select * from oilstuff as c where c.id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new OilStuffRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new OilStuff();
		}
	}

	@Override
	public OilStuff getOilStuffByName(String oilStuff) {
		String sqlQuery="select * from oilstuff as et where et.name=:name";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", oilStuff);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new OilStuffRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new OilStuff();
		}
	}

	@Override
	public OilStuff createOilStuff(String name) {
		OilStuff result=getOilStuffByName(name);
		if (result.getId()==0){
			String sqlUpdate="INSERT INTO oilstuff (name) VALUES (:name)";

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("name", name);

			KeyHolder keyHolder = new GeneratedKeyHolder();

			jdbcTemplate.update(sqlUpdate, params, keyHolder);

			try{
				result=(OilStuff)getOilStuff(keyHolder.getKey().intValue());
			}catch (EmptyResultDataAccessException e){
				return new OilStuff();
			}
		}

		return result;
	}

	@Override
	public OilStuff createOilStuff(OilStuff oilStuff) {
		OilStuff result=new OilStuff();
		OilStuff currentOilStuff = null;
		if (oilStuff.getId()>0){
			currentOilStuff=getOilStuff(oilStuff.getId());
		}else{
			currentOilStuff=oilStuff;
		}
		String sqlUpdate="insert into oilstuff (name) Values (:name)";
		if (currentOilStuff.getId()>0){
			sqlUpdate="update oilstuff set name=:name where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", oilStuff.getName());

		if (currentOilStuff.getId()>0){
			params.addValue("id", currentOilStuff.getId());
		}

		KeyHolder keyHolder=new GeneratedKeyHolder();

		jdbcTemplate.update(sqlUpdate, params, keyHolder);

		try{
			if (keyHolder.getKey()!=null) {
				result=getOilStuff(keyHolder.getKey().intValue());
			}

		}catch (EmptyResultDataAccessException e){
			result = new OilStuff();
		}

		return result;
	}

	@Override
	public ArrayList<OilStuff> getOilStuffs() {
		return getOilStuffs(0,0);
	}

	@Override
	public ArrayList<OilStuff> getOilStuffs(int num, int nextRows) {
		String sqlQuery="select * from oilstuff where oilstuff.name!=:empty order by name"
				+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("empty", Service.EMPTY);

		try{
			return (ArrayList<OilStuff>) jdbcTemplate.query(sqlQuery, params, new OilStuffRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<OilStuff>();
		}
	}

	@Override
	public void deleteOilStuff(OilStuff oilStuff) {
		deleteOilStuff(oilStuff.getId());

	}

	@Override
	public void deleteOilStuff(int id) {
		String sqlUpdate="delete from oilstuff where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id",  id);

		jdbcTemplate.update(sqlUpdate, params);
	}

	private static final class OilStuffRowMapper implements RowMapper<OilStuff> {

		@Override
		public OilStuff mapRow(ResultSet rs, int rowNum) throws SQLException {
			OilStuff result=new OilStuff();

			result.setId(rs.getInt("id"));
			result.setName(rs.getString("name"));

			return result;
		}

	}
}
