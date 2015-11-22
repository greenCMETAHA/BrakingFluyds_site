package eftech.workingset.DAO.templates;

import eftech.workingset.DAO.interfaces.InterfaceOfferStatusDAO;
import eftech.workingset.beans.OfferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OfferStatusTemplate implements InterfaceOfferStatusDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public OfferStatus getOfferStatus(int id) {
		String sqlQuery="select * from offerstatus where offerstatus.id=:id order by offerstatus.id";

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
		String sqlQuery="select * from offerstatus order by offerstatus.id";

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
