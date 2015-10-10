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
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Info;
import eftech.workingset.beans.Log;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.Price;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;

public class PriceTemplate implements InterfacePriceDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	
	@Override
	public int getCountRows(int id) {
		String sqlQuery="select count(*) from Price where brakingfluid=:id";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}				
		
	}
	
	@Override
	public ArrayList<Price> getPrices() {
		String sqlQuery="select *  from prices as p "
				+ " left join Users AS u on (p.user=u.id)"
				+ " left join brakingfluids AS bf on (p.brakingfluid=bf.id)";
		
		MapSqlParameterSource params = new MapSqlParameterSource();

		try{
			return ( ArrayList<Price>)jdbcTemplate.query(sqlQuery, params, new PriceRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new  ArrayList<Price>();
		}		
	}	

	@Override
	public ArrayList<Price> getPrices(int id) {
		String sqlQuery="select *, u.id as user_id, u.name as user_name, u.login as user_login, u.email as user_email from prices as p "
				+ " left join Users AS u on (p.user=u.id) "
				+ " left join brakingfluids AS bf on (p.brakingfluid=bf.id)"
				+ "where p.brakingfluid=:id order by time DESC";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return (ArrayList<Price>)jdbcTemplate.query(sqlQuery, params, new PriceRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new  ArrayList<Price>();
		}		
	}

	@Override
	public Price getPriceById(int id) {
		String sqlQuery="select *, u.id as user_id, u.name as user_name, u.login as user_login, u.email as user_email from prices as p "
				+ " left join Users AS u on (p.user=u.id) "
				+ " left join brakingfluids AS bf on (p.brakingfluid=bf.id)"
				+ "where p.id=:id order by time DESC";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{ 
			return (Price)jdbcTemplate.queryForObject(sqlQuery,params,new PriceRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Price();
		}
	}	
	
	@Override
	public Price createPrice(Price price){
		Price result=new Price();
		Price currentPrice = price;
		if (price.getId()>0){
			currentPrice=getPriceById(price.getId()); //���� ��� ��������������, � ��������� ��� ����� Id. ����� �������������, ��� ����� ������� ���� � ��
		}
		String sqlUpdate="insert into Price (id, time, price, user, brakingFluid)"
				+ " Values (:id, :time, :price, :user, :brakingFluid)";
		if (currentPrice.getId()>0){ // � �� ���� ����� �������
			sqlUpdate="update Price set user=:user, time=:time, price=:price, brakingFluid=:brakingFluid where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("user", ((User)price.getUser()).getId());
		params.addValue("brakingFluid", ((BrakingFluid)price.getBrakingFluid()).getId());
		params.addValue("price", price.getPrice());
		params.addValue("time", price.getTime());
		params.addValue("id", price.getId());
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getPriceById(keyHolder.getKey().intValue());
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
			BrakingFluid fluid=new BrakingFluid();
			fluid.setId(rs.getInt("fluid_id"));
			fluid.setName(rs.getString("fluid_name"));
			fluid.setPrice(rs.getDouble("fluid_price"));
			
			price.setBrakingFluid(fluid);
		
			return price;
		}
	}		
	
}
