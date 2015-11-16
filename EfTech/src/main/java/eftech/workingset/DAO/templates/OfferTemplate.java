package eftech.workingset.DAO.templates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import eftech.workingset.DAO.interfaces.InterfaceOfferDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.Basket;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Demand;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.MotorOil;
import eftech.workingset.beans.Offer;
import eftech.workingset.beans.Price;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.User;

public class OfferTemplate implements InterfaceOfferDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	

	public int getCountRows(Date begin, Date end) {
		String sqlQuery="select count(*) from Offer"+ (Service.isDate(begin,end)?" where (Offer.time<=:end) and (Offer.time>=:begin)":"");
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("begin", begin);
		params.addValue("end", end);
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}				
		
	}
	
	@Override
	public ArrayList<Offer> getOffersLast(int numPage, int quantity) {
		ArrayList<Offer> result=null;
		
		String sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name from offer as of"
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join brakingfluids AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ "where of.goodPrefix=:goodPrefix"
				//+ " order by of.time desc, of.offer_id limit :numPage, :quantity";
				+ " order by of.time desc, of.offer_id";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage-1);
		params.addValue("goodPrefix", Service.BRAKING_FLUID_PREFIX);
		
		result=(ArrayList<Offer>)jdbcTemplate.query(sqlQuery,params,new OfferRowMapper());
		
		sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name from offer as of"
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join MotorOils AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ "where of.goodPrefix=:goodPrefix"
				//+ " order by of.time desc, of.offer_id limit :numPage, :quantity";
				+ " order by of.time desc, of.offer_id";

		
		params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage-1);
		params.addValue("goodPrefix", Service.MOTOR_OIL_PREFIX);	
		
		result.addAll((ArrayList<Offer>)jdbcTemplate.query(sqlQuery,params,new OfferRowMapper()));
		
		try{
			return result;
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Offer>();
		}				
		
	}	
	
	@Override
	public ArrayList<Offer> getOffersIn(Date begin, Date end, int numPage, int quantity) {
		begin.setHours(0);
		begin.setMinutes(0);
		begin.setSeconds(0);
		end.setHours(23);
		begin.setMinutes(59);
		begin.setSeconds(59);
		
		ArrayList<Offer> result=null;
		
		String sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name from offer as of"
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join brakingfluids AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ ((begin.getTime()<end.getTime())?" where (of.time<=:end) and (of.time>=:begin)":"")
				//+ " order by of.time desc, of.offer_id limit :numPage, :quantity";
				+ " order by of.time desc, of.offer_id";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage-1);
		params.addValue("begin", begin);
		params.addValue("end", end);
		params.addValue("goodPrefix", Service.BRAKING_FLUID_PREFIX);
		
		result=(ArrayList<Offer>)jdbcTemplate.query(sqlQuery,params,new OfferRowMapper());
		
		sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name from offer as of"
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join MotorOils AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ ((begin.getTime()<end.getTime())?" where (of.time<=:end) and (of.time>=:begin)":"")
				//+ " order by of.time desc, of.offer_id limit :numPage, :quantity";
				+ " order by of.time desc, of.offer_id";

		
		params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage-1);
		params.addValue("begin", begin);
		params.addValue("end", end);
		params.addValue("goodPrefix", Service.MOTOR_OIL_PREFIX);
		
		result.addAll((ArrayList<Offer>)jdbcTemplate.query(sqlQuery,params,new OfferRowMapper()));		
		
		try{
			return result;
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Offer>();
		}				
		
	}		
	

	@Override
	public ArrayList<Offer> getOffer(String offer_id) {
		ArrayList<Offer> result=null;
	
		String sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name from offer as of"
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join brakingfluids AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ " where of.offer_id=:offer_id";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("offer_id", offer_id);
		params.addValue("goodPrefix", Service.BRAKING_FLUID_PREFIX);
		
		result=(ArrayList<Offer>)jdbcTemplate.query(sqlQuery,params,new OfferRowMapper());
		
		sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name from offer as of"
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join MotorOils AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ " where of.offer_id=:offer_id";

		
		params = new MapSqlParameterSource();
		params.addValue("offer_id", offer_id);
		params.addValue("goodPrefix", Service.MOTOR_OIL_PREFIX);
		
		result.addAll((ArrayList<Offer>)jdbcTemplate.query(sqlQuery,params,new OfferRowMapper()));		
		
		try{
			return result;
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Offer>();
		}				
	}

	@Override
	public Offer getOfferById(int id) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		
		String goodPrefix=null;
		String sqlQuery="select goodPrefix from offer where offer.id=:id";
		try{ 
			goodPrefix=(String)jdbcTemplate.queryForObject(sqlQuery,params,String.class);
		}catch (EmptyResultDataAccessException e){
			return new Offer();
		}
		
		Offer result=null;
		
		if (goodPrefix!=null){		
			sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.price AS good_price, bf.photo AS good_photo"
					+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
					+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name from offer as of"
					+ " left join Users AS u on (of.user=u.id)";
			if (Service.BRAKING_FLUID_PREFIX.equals(goodPrefix)){
				sqlQuery+=" left join brakingfluids AS bf on (of.good=bf.id)";
			}else if (Service.MOTOR_OIL_PREFIX.equals(goodPrefix)){
				sqlQuery+=" left join MotorOils AS bf on (of.good=bf.id)";
			}
					sqlQuery+= " left join manufacturer AS man on (bf.manufacturer=man.id)"
					+ " where of.id=:id";
	
			
			params = new MapSqlParameterSource();
			params.addValue("id", id);
			
			result=(Offer)jdbcTemplate.queryForObject(sqlQuery,params,new OfferRowMapper());
		
		}
		
		try{ 
			return result;
		}catch (EmptyResultDataAccessException e){
			return new Offer();
		}
	}
	
	@Override
	public Offer getOfferByOfferId(String offer_id, int good_id, String goodPrefix) {
		String sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name from offer as of"
				+ " left join Users AS u on (of.user=u.id)";
				if (Service.BRAKING_FLUID_PREFIX.equals(goodPrefix)){
					sqlQuery+=" left join brakingfluids AS bf on (of.good=bf.id)";
				}else if (Service.MOTOR_OIL_PREFIX.equals(goodPrefix)){
					sqlQuery+=" left join MotorOils AS bf on (of.good=bf.id)";
				}
				sqlQuery+= " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ " where (of.offer_id=:offer_id) and (of.good=:good_id) and (of.goodPrefix=:goodPrefix)";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("offer_id", offer_id);
		params.addValue("good_id", good_id);
		params.addValue("goodPrefix", goodPrefix);
		
		
		try{ 
			return (Offer)jdbcTemplate.queryForObject(sqlQuery,params,new OfferRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Offer();
		}
	}
	
	private Offer createOffer(Offer offer){
		
		Offer result=new Offer();
		Offer currentOffer = offer;

		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String sqlUpdate="insert into offer (time, offer_id, good, goodPrefix, quantity, price, user ) "
				+ "Values (:time, :offer_id, :good, :goodPrefix, :quantity, :price, :user)";
		if (currentOffer.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update offer set time=:time, offer_id=:offer_id, good=:good, goodPrefix=:goodPrefix, quantity=:quantity"
			 		+ ", price=:price, user=:user where id=:id";
			 params.addValue("id", currentOffer.getId());
		}
		
		params.addValue("time", offer.getTime());
		params.addValue("offer_id", offer.getOffer_id());
		params.addValue("good", offer.getGood().getId());
		params.addValue("goodPrefix", offer.getGood().getGoodName());
		params.addValue("quantity", offer.getQuantity());
		params.addValue("price", offer.getGood().getPrice());
		params.addValue("user", (((User)offer.getUser()).getId()==0?Service.ID_CUSTOMER:((User)offer.getUser()).getId()));
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getOfferById(keyHolder.getKey().intValue());
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new Offer();
		}		
		
		return result;			
	}

	@Override
	public ArrayList<Offer> createOffer(String offer_id, LinkedList<Basket> basket, User user) {
		ArrayList<Offer> list=getOffer(offer_id);	//все строки документа получили
		ArrayList<Offer> result=new ArrayList<Offer>();
		
		Date dataDoc=new GregorianCalendar().getTime(); 
		for (Offer offer:list){						//проверим уже записанные реквизиты...
			dataDoc=offer.getTime();
			boolean bFind=false;
			for (Basket current:basket){
				if ((offer.getGood().getId()==current.getGood().getId()) && (offer.getGood().getGoodName().equals(current.getGood().getGoodName()))){
					bFind=true;
					result.add(offer);
					basket.remove(current);
					break;
					
				}
			}
		}
		for (Basket current:basket){			//добавим новые...
			Offer offer=new Offer();
			offer.setOffer_id(offer_id);
			offer.setPrice(current.getGood().getPrice());
			offer.setQuantity(current.getQauntity());
			offer.setTime(dataDoc);
			offer.setUser(user);
			offer.setGood(current.getGood());
			
			result.add(offer);
		}
		list.clear();
		
		for (Offer offer:result){						//и запишем всё в базу данных
			list.add(createOffer(offer));
		}
		return result;
	}

	
	private static final class OfferRowMapper implements RowMapper<Offer> {

		@Override
		public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Offer offer = new Offer();
			offer.setId(rs.getInt("id"));
			Timestamp timestamp = rs.getTimestamp("time");
			if (timestamp != null){
				offer.setTime(new java.util.Date(timestamp.getTime()));
			}
			offer.setOffer_id(rs.getString("offer_id"));
			
			Manufacturer manufacturer=new Manufacturer();
			manufacturer.setId(rs.getInt("good_manufacturer_id"));
			manufacturer.setName(rs.getString("good_manufacturer_name"));

			if (Service.BRAKING_FLUID_PREFIX.equals(rs.getString("goodPrefix"))){
				BrakingFluid current=new BrakingFluid();
				current.setId(rs.getInt("good_id"));
				current.setName(rs.getString("good_name"));
				current.setPrice(rs.getDouble("good_price"));
				current.setPhoto(rs.getString("good_photo"));
				current.setManufacturer(manufacturer);
				offer.setGood(current);
			}else if (Service.MOTOR_OIL_PREFIX.equals(rs.getString("goodPrefix"))){
				MotorOil current=new MotorOil();
				current.setId(rs.getInt("good_id"));
				current.setName(rs.getString("good_name"));
				current.setPrice(rs.getDouble("good_price"));
				current.setPhoto(rs.getString("good_photo"));
				current.setManufacturer(manufacturer);
				offer.setGood(current);
			}	
			
			offer.setQuantity(rs.getInt("quantity"));
			offer.setPrice(rs.getDouble("price"));
			offer.setUser(new User(rs.getInt("user_id"),rs.getString("user_name"),rs.getString("user_email"),rs.getString("user_login")));
			
			
			return offer;
		}
	}



}
