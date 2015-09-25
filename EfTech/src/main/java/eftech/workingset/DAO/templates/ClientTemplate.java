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

import eftech.workingset.DAO.interfaces.InterfaceClientDAO;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Role;

public class ClientTemplate implements InterfaceClientDAO {
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	

	@Override
	public ArrayList<Client> getClients() {
		String sqlQuery="select * from client order by Name";
		try{
			return (ArrayList<Client>) jdbcTemplate.query(sqlQuery, new ClientRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Client>();
		}		
	}

	
	@Override
	public Client getClient(int id) {
		String sqlQuery="select * from Client as c where c.id=:id";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return jdbcTemplate.queryForObject(sqlQuery, params, new ClientRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Client();
		}		
	}
	
	private static final class ClientRowMapper implements RowMapper<Client> {

		@Override
		public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
			Client result=new Client();

			result.setId(rs.getInt("id"));
			result.setName(rs.getString("name"));
			result.setEmail(rs.getString("email"));
			
			return result;
		}
	}	
}
