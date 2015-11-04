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
import eftech.workingset.Services.Service;
import eftech.workingset.beans.Basket;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Demand;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.Offer;
import eftech.workingset.beans.OfferStatus;
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
		String sqlQuery="select count(*) from Demand"+ (Service.isDate(begin,end)?" where (Demand.time<=:end) and (Demand.time>=:begin)":"");
		
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
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price, bf.photo AS fluid_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS fluid_manufacturer_id, man.name AS fluid_manufacturer_name"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join Users AS exe on (of.executer=exe.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				//+ " order by of.time desc, of.demand_id limit :numPage, :quantity";
				+ " order by of.time desc, of.demand_id";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage-1);
		
		try{
			return (ArrayList<Demand>)jdbcTemplate.query(sqlQuery,params,new DemandRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Demand>();
		}				
		
	}	
	
	@Override
	public ArrayList<Demand> getDemandsIn(Date begin, Date end, int numPage, int quantity, int executer_id) {
		begin.setHours(0);
		begin.setMinutes(0);
		begin.setSeconds(0);
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price, bf.photo AS fluid_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS fluid_manufacturer_id, man.name AS fluid_manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join Users AS exe on (of.executer=exe.id)"
				+ " left join Client AS cl on (of.client=cl.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ ((begin.getTime()<end.getTime())?" where (of.time<=:end) and (of.time>=:begin) ":"")
				+(executer_id>0?" and (of.executer=:executer_id) ":"")
				//+ " order by of.time desc, of.demand_id limit :numPage, :quantity";
				+ " order by of.time desc, of.demand_id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage-1);
		params.addValue("begin", begin);
		params.addValue("end", end);
		params.addValue("executer_id", executer_id);
		
		try{
			return (ArrayList<Demand>)jdbcTemplate.query(sqlQuery,params,new DemandRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Demand>();
		}				
	}	
	

	@Override
	public ArrayList<Demand> getDemand(String demand_id) {
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price, bf.photo AS fluid_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS fluid_manufacturer_id, man.name AS fluid_manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join Users AS exe on (of.executer=exe.id)"
				+ " left join Client AS cl on (of.client=cl.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
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
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price, bf.photo AS fluid_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS fluid_manufacturer_id, man.name AS fluid_manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join Users AS exe on (of.executer=exe.id)"
				+ " left join Client AS cl on (of.client=cl.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
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
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price, bf.photo AS fluid_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS fluid_manufacturer_id, man.name AS fluid_manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join Users AS exe on (of.executer=exe.id)"
				+ " left join Client AS cl on (of.client=cl.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
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
	


	@Override
	public ArrayList<Demand> getDemandsByClient(int client_id) {
		String sqlQuery="select *, bf.id AS fluid_id, bf.name AS fluid_name, bf.price AS fluid_price, bf.photo AS fluid_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS fluid_manufacturer_id, man.name AS fluid_manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join Users AS u on (of.user=u.id)"
				+ " left join Users AS exe on (of.executer=exe.id)"
				+ " left join Client AS cl on (of.client=cl.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.brakingfluid=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ " where of.client=:client_id order by time desc";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("client_id", client_id);
		
		try{
			return (ArrayList<Demand>)jdbcTemplate.query(sqlQuery,params,new DemandRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Demand>();
		}					}

	
	
	private Demand createDemand(Demand demand){
		
		Demand result=new Demand();
		Demand currentDemand = demand;

		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String sqlUpdate="insert into demand (time, demand_id, brakingfluid, quantity, price, user, status, executer, client)"
				+ " Values (:time, :demand_id, :brakingfluid, :quantity, :price, :user, :status, :executer, :client)";
		if (currentDemand.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update demand set time=:time, demand_id=:demand_id, brakingfluid=:brakingfluid, quantity=:quantity"
			 		+ ", price=:price, user=:user, status=:status, executer=:executer , client=:client where id=:id";
			 params.addValue("id", currentDemand.getId());
		}
		
		params.addValue("time", demand.getTime());
		params.addValue("demand_id", demand.getDemand_id());
		params.addValue("brakingfluid", ((BrakingFluid)demand.getBrakingFluid()).getId());
		params.addValue("quantity", demand.getQuantity());
		params.addValue("price", ((BrakingFluid)demand.getBrakingFluid()).getPrice());
		params.addValue("user", (((User)demand.getUser()).getId()==0?Service.ID_CUSTOMER:((User)demand.getUser()).getId()));
		params.addValue("status", ((OfferStatus)demand.getStatus()).getId());
		params.addValue("executer", (((User)demand.getExecuter()).getId()==0?Service.ID_EXECUTER:((User)demand.getExecuter()).getId()));
		params.addValue("client", (((Client)demand.getClient()).getId()==0?Service.ID_EMPTY_CLIENT:((Client)demand.getClient()).getId()));
		
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
	public ArrayList<Demand> changeStatus(String demand_id, OfferStatus status) {
		ArrayList<Demand> result=null;
		
		String  sqlUpdate="update demand set status=:status  where demand_id=:demand_id";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("demand_id", demand_id);
		params.addValue("status", status.getId());
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getDemand(demand_id);
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new ArrayList<Demand>();
		}
		
		return result;
	}
	
	@Override
	public ArrayList<Demand> changeExecuter(String demand_id, User executer) {
		ArrayList<Demand> result=null;
		
		String  sqlUpdate="update demand set executer=:executer  where demand_id=:demand_id";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("demand_id", demand_id);
		params.addValue("executer", executer.getId());
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getDemand(demand_id);
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new ArrayList<Demand>();
		}
		
		return result;
	}	
	
	
	@Override
	public ArrayList<Demand> createDemand(String demand_id, LinkedList<Basket> basket, User user, OfferStatus status, User executer, Client client) {
		ArrayList<Demand> list=getDemand(demand_id);	//все строки документа получили
		ArrayList<Demand> result=new ArrayList<Demand>();
		LinkedList<Basket> copyBasket=(LinkedList<Basket>)basket.clone();
		
		Date dataDoc=new GregorianCalendar().getTime(); 
		for (Demand demand:list){						//проверим уже записанные реквизиты...
			dataDoc=demand.getTime();
			boolean bFind=false;
			for (Basket currentFluid:copyBasket){
				if ((((BrakingFluid)demand.getBrakingFluid()).getId()==((BrakingFluid)currentFluid.getGood()).getId())){
					bFind=true;
					result.add(demand);
					basket.remove(currentFluid);
					break;
					
				}
			}
		}
		for (Basket currentFluid:copyBasket){			//добавим новые...
			Demand demand=new Demand();
			demand.setDemand_id(demand_id);
			demand.setPrice(((BrakingFluid)currentFluid.getGood()).getPrice());
			demand.setQuantity(currentFluid.getQauntity());
			demand.setTime(dataDoc);
			demand.setStatus(status);
			demand.setUser(user);
			demand.setBrakingFluid((BrakingFluid)currentFluid.getGood());
			demand.setExecuter(executer);
			demand.setClient(client);
			
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
			fluid.setPhoto(rs.getString("fluid_photo"));
			Manufacturer manufacturer=new Manufacturer();
			manufacturer.setId(rs.getInt("fluid_manufacturer_id"));
			manufacturer.setName(rs.getString("fluid_manufacturer_name"));
			fluid.setManufacturer(manufacturer);
			demand.setBrakingFluid(fluid);
			
			demand.setClient(new Client(rs.getInt("client_id"),rs.getString("client_name")
					,rs.getString("client_email"),rs.getString("client_address"),null));
			
			demand.setQuantity(rs.getInt("quantity"));
			demand.setPrice(rs.getDouble("price"));
			demand.setUser(new User(rs.getInt("user_id"),rs.getString("user_name"),rs.getString("user_email"),rs.getString("user_login")));
			demand.setStatus(new OfferStatus(rs.getInt("status_id"),rs.getString("status_name")));
			demand.setExecuter(new User(rs.getInt("executer_id"),rs.getString("executer_name"),rs.getString("executer_email"),rs.getString("executer_login")));
			
			return demand;
		}
	}

}
