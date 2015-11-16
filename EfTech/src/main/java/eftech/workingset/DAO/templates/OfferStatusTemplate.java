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

import eftech.workingset.DAO.interfaces.InterfaceOfferStatusDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.OfferStatus;

public class OfferStatusTemplate implements InterfaceOfferStatusDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	
	
	@Override
	public OfferStatus getOfferStatus(int id) {
		String sqlQuery="select * from OfferStatus where OfferStatus.id=:id order by OfferStatus.id";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new OfferStatusRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new OfferStatus();
		}		
	}
	
	@Override
	public ArrayList<OfferStatus> getStatuses() {
		String sqlQuery="select * from OfferStatus order by OfferStatus.id";
		
		try{
			return (ArrayList<OfferStatus>)jdbcTemplate.query(sqlQuery, new OfferStatusRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<OfferStatus>();
		}		
	}
	
	
	private static final class OfferStatusRowMapper implements RowMapper<OfferStatus> {

		@Override
		public OfferStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
			OfferStatus result=new OfferStatus();

			result.setId(rs.getInt("id"));
			result.setName(rs.getString("name"));
			return result;
		}
	}	
	
}
