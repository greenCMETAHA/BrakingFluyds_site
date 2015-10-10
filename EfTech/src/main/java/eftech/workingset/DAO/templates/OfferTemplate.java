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
import eftech.workingset.beans.Basket;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Offer;
import eftech.workingset.beans.OfferStatus;
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
		String sqlQuery="select count(*) from Offer where (Offer.time<=:end) and (Offer.time>=:begin)";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("begin", begin);
		params.addValue("end", end);
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}				
		
	}
	
	public ArrayList<Offer> getOffersLast(int numPage, int quantity) {
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", status.id AS status_id, status.name AS status_name from offer as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " order by of.time desc limit :numPage, :quantity";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage);
		
		try{
			return (ArrayList<Offer>)jdbcTemplate.query(sqlQuery,params,new OfferRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Offer>();
		}				
		
	}	
	

	@Override
	public ArrayList<Offer> getOffer(String offer_id) {
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", status.id AS status_id, status.name AS status_name from offer as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " where of.offer_id=:offer_id";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("offer_id", offer_id);
		
		try{
			return (ArrayList<Offer>)jdbcTemplate.query(sqlQuery,params,new OfferRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Offer>();
		}				
	}

	@Override
	public Offer getOfferById(int id) {
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", status.id AS status_id, status.name AS status_name from offer as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " where of.id=:id";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		
		
		try{ 
			return (Offer)jdbcTemplate.queryForObject(sqlQuery,params,new OfferRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Offer();
		}
	}
	
	@Override
	public Offer getOfferByOfferId(String offer_id, int braking_fluid_id) {
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", status.id AS status_id, status.name AS status_name from offer as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " where (of.offer_id=:offer_id) and (of.braking_fluid=:braking_fluid_id)";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("offer_id", offer_id);
		params.addValue("braking_fluid_id", braking_fluid_id);
		
		
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
		
		String sqlUpdate="insert into review (name, email, judgement, review, brakingfluid) Values (:name, :email, :judgement, :review, :brakingfluid)";
		if (currentOffer.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update review set name=:name, email=:email, judgement=:judgement, review=:review, brakingfluid=:brakingfluid where id=:id";
			 params.addValue("id", currentOffer.getId());
		}
		
		params.addValue("time", offer.getTime());
		params.addValue("offer_id", offer.getOffer_id());
		params.addValue("brakingfluid", ((BrakingFluid)offer.getBrakingFluid()).getId());
		params.addValue("quantity", offer.getQuantity());
		params.addValue("price", ((BrakingFluid)offer.getBrakingFluid()).getPrice());
		params.addValue("user", ((User)offer.getUser()).getId());
		params.addValue("status", ((OfferStatus)offer.getStatus()).getId());
		
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
	public ArrayList<Offer> createOffer(String offer_id, LinkedList<Basket> basket, User user, OfferStatus status) {
		ArrayList<Offer> list=getOffer(offer_id);	//все строки документа получили
		ArrayList<Offer> result=new ArrayList<Offer>();
		
		Date dataDoc=new GregorianCalendar().getTime(); 
		for (Offer offer:list){						//проверим уже записанные реквизиты...
			dataDoc=offer.getTime();
			boolean bFind=false;
			for (Basket currentFluid:basket){
				if ((((BrakingFluid)offer.getBrakingFluid()).getId()==((BrakingFluid)currentFluid.getBrakingFluid()).getId())){
					bFind=true;
					result.add(offer);
					basket.remove(currentFluid);
					break;
					
				}
			}
		}
		for (Basket currentFluid:basket){			//добавим новые...
			Offer offer=new Offer();
			offer.setOffer_id(offer_id);
			offer.setPrice(((BrakingFluid)currentFluid.getBrakingFluid()).getPrice());
			offer.setQuantity(currentFluid.getQauntity());
			offer.setStatus(status);
			offer.setTime(dataDoc);
			offer.setUser(user);
			offer.setBrakingFluid((BrakingFluid)currentFluid.getBrakingFluid());
			
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
			BrakingFluid fluid=new BrakingFluid();
			fluid.setId(rs.getInt("fluid_id"));
			fluid.setName(rs.getString("fluid_name"));
			fluid.setPrice(rs.getDouble("fluid_price"));
			offer.setBrakingFluid(fluid);
			offer.setQuantity(rs.getInt("quantity"));
			offer.setPrice(rs.getDouble("price"));
			offer.setUser(new User(rs.getInt("user_id"),rs.getString("user_name"),rs.getString("user_email"),rs.getString("user_login")));
			offer.setStatus(new OfferStatus(rs.getInt("status_id"),rs.getString("status_name")));
			
			return offer;
		}
	}
}
