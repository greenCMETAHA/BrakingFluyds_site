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

import eftech.workingset.DAO.interfaces.InterfaceInfoDAO;
import eftech.workingset.beans.Info;
import eftech.workingset.beans.Role;

public class InfoTemplate implements InterfaceInfoDAO {
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	
	@Override
	public String getInfo(String name) {
		String sqlQuery="select * from information where name=:name order by name";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", name);

		try{
			Info info=jdbcTemplate.queryForObject(sqlQuery, params, new InfoRowMapper());
			return info.getName();
		}catch (EmptyResultDataAccessException e){
			return "";
		}		
	}
	
	private static final class InfoRowMapper implements RowMapper<Info> {

		@Override
		public Info mapRow(ResultSet rs, int rowNum) throws SQLException {
			Info result=new Info();

			result.setId(rs.getInt("id"));
			result.setName(rs.getString("name"));
			result.setValue(rs.getString("value"));
			
			return result;
		}

	}	

}
