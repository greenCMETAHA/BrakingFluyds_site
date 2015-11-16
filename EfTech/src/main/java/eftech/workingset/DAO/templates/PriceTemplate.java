package eftech.workingset.DAO.templates;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import eftech.workingset.DAO.interfaces.InterfacePriceDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Info;
import eftech.workingset.beans.Log;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.MotorOil;
import eftech.workingset.beans.Price;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

public class PriceTemplate implements InterfacePriceDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	
	@Override
	public int getCountRows(int id, String goodPrefix) {
		String sqlQuery="select count(*) from Prices where good=:id and Prices.goodPrefix=:goodPrefix";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		params.addValue("goodPrefix", goodPrefix);
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}				
		
	}
	
	@Override
	public ArrayList<Price> getPrices(String goodPrefix) {
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price, p.goodPrefix as goodPrefix"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login from prices as p "
				+ " left join Users AS u on (p.user=u.id)"
				+ " left join "+(goodPrefix.equals(Service.BRAKING_FLUID_PREFIX)?"brakingfluids":"motorOils")+" AS bf on (p.good=bf.id)"
				+ " where p.goodPrefix=:goodPrefix";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("goodPrefix", goodPrefix);

		try{
			return ( ArrayList<Price>)jdbcTemplate.query(sqlQuery, params, new PriceRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new  ArrayList<Price>();
		}		
	}	
	
	@Override
	public ArrayList<Price> getPrices(int id, String goodPrefix) {
		String sqlQuery="select *, u.id as user_id, u.name as user_name, u.login as user_login, u.email as user_email, p.goodPrefix as goodPrefix"
				+ ", bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price from prices as p "
				+ " left join Users AS u on (p.user=u.id) "
				+ " left join "+(goodPrefix.equals(Service.BRAKING_FLUID_PREFIX)?"brakingfluids":"motorOils")+" AS bf on (p.good=bf.id)"
				+ "where p.good=:id and p.goodPrefix=:goodPrefix order by time DESC";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		params.addValue("goodPrefix", goodPrefix);

		try{
			return (ArrayList<Price>)jdbcTemplate.query(sqlQuery, params, new PriceRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new  ArrayList<Price>();
		}		
	}

	@Override
	public Price getPriceById(int id, String goodPrefix) {
		String sqlQuery="select *, u.id as user_id, u.name as user_name, u.login as user_login, u.email as user_email, p.goodPrefix as goodPrefix"
				+ ", bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price from prices as p "
				+ " left join Users AS u on (p.user=u.id) "
				+ " left join "+(goodPrefix.equals(Service.BRAKING_FLUID_PREFIX)?"brakingfluids":"motorOils")+" AS bf on (p.good=bf.id)"
				+ "where p.id=:id and p.goodPrefix=:goodPrefix order by time DESC";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		params.addValue("goodPrefix", goodPrefix);

		try{ 
			return (Price)jdbcTemplate.queryForObject(sqlQuery,params,new PriceRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Price();
		}
	}	
	
	@Override
	public Price createPrice(Price price, String goodPrefix){
		Price result=new Price();
		Price currentPrice = price;
		if (price.getId()>0){
			currentPrice=getPriceById(price.getId(), goodPrefix); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}
		String sqlUpdate="insert into Prices (id, time, price, user, good, goodPrefix)"
				+ " Values (:id, :time, :price, :user, :good, :goodPrefix)";
		if (currentPrice.getId()>0){ // В БД есть такой элемент
			sqlUpdate="update Prices set user=:user, time=:time, price=:price, good=:good, goodPrefix=:goodPrefix where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("user", ((User)price.getUser()).getId());
		params.addValue("good", price.getGood().getId());
		params.addValue("price", price.getPrice());
		params.addValue("time", price.getTime());
		params.addValue("id", price.getId());
		params.addValue("goodPrefix", goodPrefix);
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getPriceById(keyHolder.getKey().intValue(), goodPrefix);
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new Price();
		}		
		
		return result;	
	}

	private static final class PriceRowMapper implements RowMapper<Price> {
		
		@Override
		public Price mapRow(ResultSet rs, int rowNum) throws SQLException {
			Price price = new Price();
			price.setId(rs.getInt("id"));
			Timestamp timestamp = rs.getTimestamp("time");
			if (timestamp != null){
				price.setTime(new java.util.Date(timestamp.getTime()));
			}
			price.setPrice(rs.getDouble("price"));
			price.setUser(new User(rs.getInt("user_id"),rs.getString("user_name"),rs.getString("user_email"),rs.getString("user_login")));
			InterfaceGood good=null;
			String goodPrefix=rs.getString("goodPrefix");
			if (Service.MOTOR_OIL_PREFIX.equals(goodPrefix)){
				good=new MotorOil();
			}else if (Service.BRAKING_FLUID_PREFIX.equals(goodPrefix)){
				good=new BrakingFluid();
			}
			good.setId(rs.getInt("fluid_id"));
			good.setName(rs.getString("fluid_name"));
			good.setPrice(rs.getDouble("fluid_price"));
			
			price.setGood(good);
			
			return price;
		}
	}		
	
}
