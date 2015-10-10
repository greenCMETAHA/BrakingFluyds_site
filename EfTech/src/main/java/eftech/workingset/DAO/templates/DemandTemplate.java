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

import eftech.workingset.DAO.interfaces.InterfaceDemandDAO;
import eftech.workingset.beans.Basket;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Demand;
import eftech.workingset.beans.Price;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.User;

public class DemandTemplate implements InterfaceDemandDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}	

	public int getCountRows(Date begin, Date end) {
		String sqlQuery="select count(*) from Demand where (Demand.time<=:end) and (Demand.time>=:begin)";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("begin", begin);
		params.addValue("end", end);
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}				
		
	}
	
	public ArrayList<Demand> getDemandsLast(int numPage, int quantity) {
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join demandstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " order by of.time desc limit :numPage, :quantity";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage);
		
		try{
			return (ArrayList<Demand>)jdbcTemplate.query(sqlQuery,params,new DemandRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Demand>();
		}				
		
	}	
	

	@Override
	public ArrayList<Demand> getDemand(String demand_id) {
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join demandstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " where of.demand_id=:demand_id";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("demand_id", demand_id);
		
		try{
			return (ArrayList<Demand>)jdbcTemplate.query(sqlQuery,params,new DemandRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Demand>();
		}				
	}

	@Override
	public Demand getDemandById(int id) {
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join demandstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " where of.id=:id";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		
		
		try{ 
			return (Demand)jdbcTemplate.queryForObject(sqlQuery,params,new DemandRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Demand();
		}
	}
	
	@Override
	public Demand getDemandByDemandId(String demand_id, int braking_fluid_id) {
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join demandstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " where (of.demand_id=:demand_id) and (of.braking_fluid=:braking_fluid_id)";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("demand_id", demand_id);
		params.addValue("braking_fluid_id", braking_fluid_id);
		
		
		try{ 
			return (Demand)jdbcTemplate.queryForObject(sqlQuery,params,new DemandRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Demand();
		}
	}
	
	private Demand createDemand(Demand demand){
		
		Demand result=new Demand();
		Demand currentDemand = demand;

		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String sqlUpdate="insert into review (name, email, judgement, review, brakingfluid) Values (:name, :email, :judgement, :review, :brakingfluid)";
		if (currentDemand.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update review set name=:name, email=:email, judgement=:judgement, review=:review, brakingfluid=:brakingfluid where id=:id";
			 params.addValue("id", currentDemand.getId());
		}
		
		params.addValue("time", demand.getTime());
		params.addValue("demand_id", demand.getDemand_id());
		params.addValue("brakingfluid", ((BrakingFluid)demand.getBrakingFluid()).getId());
		params.addValue("quantity", demand.getQuantity());
		params.addValue("price", ((BrakingFluid)demand.getBrakingFluid()).getPrice());
		params.addValue("user", ((User)demand.getUser()).getId());
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getDemandById(keyHolder.getKey().intValue());
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new Demand();
		}		
		
		return result;			
	}

	@Override
	public ArrayList<Demand> createDemand(String demand_id, LinkedList<Basket> basket, User user) {
		ArrayList<Demand> list=getDemand(demand_id);	//все строки документа получили
		ArrayList<Demand> result=new ArrayList<Demand>();
		
		Date dataDoc=new GregorianCalendar().getTime(); 
		for (Demand demand:list){						//проверим уже записанные реквизиты...
			dataDoc=demand.getTime();
			boolean bFind=false;
			for (Basket currentFluid:basket){
				if ((((BrakingFluid)demand.getBrakingFluid()).getId()==((BrakingFluid)currentFluid.getBrakingFluid()).getId())){
					bFind=true;
					result.add(demand);
					basket.remove(currentFluid);
					break;
					
				}
			}
		}
		for (Basket currentFluid:basket){			//добавим новые...
			Demand demand=new Demand();
			demand.setDemand_id(demand_id);
			demand.setPrice(((BrakingFluid)currentFluid.getBrakingFluid()).getPrice());
			demand.setQuantity(currentFluid.getQauntity());
			demand.setTime(dataDoc);
			demand.setUser(user);
			demand.setBrakingFluid((BrakingFluid)currentFluid.getBrakingFluid());
			
			result.add(demand);
		}
		list.clear();
		
		for (Demand demand:result){						//и запишем всё в базу данных
			list.add(createDemand(demand));
		}
		return result;
	}				
	
	
	
	
	
	private static final class DemandRowMapper implements RowMapper<Demand> {

		@Override
		public Demand mapRow(ResultSet rs, int rowNum) throws SQLException {
			Demand demand = new Demand();
			demand.setId(rs.getInt("id"));
			Timestamp timestamp = rs.getTimestamp("time");
			if (timestamp != null){
				demand.setTime(new java.util.Date(timestamp.getTime()));
			}
			demand.setDemand_id(rs.getString("demand_id"));
			BrakingFluid fluid=new BrakingFluid();
			fluid.setId(rs.getInt("fluid_id"));
			fluid.setName(rs.getString("fluid_name"));
			fluid.setPrice(rs.getDouble("fluid_price"));
			demand.setBrakingFluid(fluid);
			demand.setQuantity(rs.getInt("quantity"));
			demand.setPrice(rs.getDouble("price"));
			demand.setUser(new User(rs.getInt("user_id"),rs.getString("user_name"),rs.getString("user_email"),rs.getString("user_login")));
			
			return demand;
		}
	}
}
