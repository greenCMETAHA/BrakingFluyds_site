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

import eftech.workingset.DAO.interfaces.InterfaceClientDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.Role;

public class ClientTemplate implements InterfaceClientDAO {
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	

	public  int getCountRows(){
		String sqlQuery="select count(*) from client";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
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
	
	@Override
	public ArrayList<Client> getClients() {
		return (ArrayList<Client>)getClients(0,0);
	}
	
	@Override
	public ArrayList<Client> getClients(int num, int nextRows) {
		String sqlQuery="select * from client order by Name "
				+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);
		try{
			return (ArrayList<Client>) jdbcTemplate.query(sqlQuery, new ClientRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Client>();
		}			}

	@Override
	public Client createClient(Client client) {
		Client result=new Client();
		Client currentClient = null;
		if (client.getId()>0){
			currentClient=getClient(client.getId()); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}else{
			currentClient=client;
		}
		String sqlUpdate="insert into client (name, email, address, country) Values (:name, :email, :address, :country)";
		if (currentClient.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update brakingfluids set name=:name, email=:email, address=:address, country=:country where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", currentClient.getName());
		params.addValue("email", currentClient.getEmail());
		params.addValue("address", currentClient.getAddress());
		params.addValue("country", ((Country)currentClient.getCountry()).getId());
	
		if (currentClient.getId()>0){ // В БД есть элемент
			params.addValue("id", currentClient.getId());
		}
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getClient(keyHolder.getKey().intValue());
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new Client();
		}		
		
		return result;
	}

	@Override
	public void deleteClient(Client client) {
		deleteClient(client.getId());
	}

	@Override
	public void deleteClient(int id) {
		String sqlUpdate="delete from Client where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id",  id);
		
		jdbcTemplate.update(sqlUpdate, params);
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
