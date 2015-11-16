package eftech.workingset.DAO.templates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import eftech.workingset.DAO.interfaces.InterfacePayDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Demand;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.Offer;
import eftech.workingset.beans.OfferStatus;
import eftech.workingset.beans.Pay;
import eftech.workingset.beans.User;

public class PayTemplate implements InterfacePayDAO {
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public int getCountRows(Date begin, Date end) {
		String sqlQuery="select count(*) from pays"+ (Service.isDate(begin,end)?" where (pays.time<=:end) and (pays.time>=:begin)":"");
		
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
	public ArrayList<Pay> getPaysLast(int numPage, int quantity) {
		String sqlQuery="select *"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS manufacturer_id, man.name AS manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ " from pays "
				+ " left join Users AS u on (pays.user=u.id)"
				+ " left join Client AS cl on (pays.client=cl.id)"
				+ " left join manufacturer AS man on (pays.manufacturer=man.id)"
				+ " order by pays.time desc, pays.demand_id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage-1);
		
		try{
			return (ArrayList<Pay>)jdbcTemplate.query(sqlQuery,params,new PayRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Pay>();
		}				
			}

	@Override
	public ArrayList<Pay> getPaysIn(Date begin, Date end, int numPage, int quantity) {
		begin.setHours(0);
		begin.setMinutes(0);
		begin.setSeconds(0);
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		
		String sqlQuery="select *"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS manufacturer_id, man.name AS manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ " from pays "
				+ " left join Users AS u on (pays.user=u.id)"
				+ " left join Client AS cl on (pays.client=cl.id)"
				+ " left join manufacturer AS man on (pays.manufacturer=man.id)"
				+ ((begin.getTime()<end.getTime())?" where (pays.time<=:end) and (pays.time>=:begin) ":"")
				+ " order by pays.time desc, pays.demand_id";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage-1);
		params.addValue("begin", begin);
		params.addValue("end", end);
		
		try{
			return (ArrayList<Pay>)jdbcTemplate.query(sqlQuery,params,new PayRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Pay>();
		}		
	}

	@Override
	public ArrayList<Pay> getPay(String demand_id) {
		String sqlQuery="select *"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS manufacturer_id, man.name AS manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ " from pays"
				+ " left join Users AS u on (pays.user=u.id)"
				+ " left join Client AS cl on (pays.client=cl.id)"
				+ " left join manufacturer AS man on (pays.manufacturer=man.id)"
				+ " where pays.demand_id=:demand_id";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("demand_id", demand_id);
		
		try{
			return (ArrayList<Pay>)jdbcTemplate.query(sqlQuery,params,new PayRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Pay>();
		}	
	}
	
	@Override
	public ArrayList<Pay> getPayByDemandId(String demand_id) {
		String sqlQuery="select *"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS manufacturer_id, man.name AS manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ " from pays "
				+ " left join Users AS u on (pays.user=u.id)"
				+ " left join Client AS cl on (pays.client=cl.id)"
				+ " left join manufacturer AS man on (pays.manufacturer=man.id)"
				+ " where (pays.demand_id=:demand_id)";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("demand_id", demand_id);
		
		try{ 
			return (ArrayList<Pay>)jdbcTemplate.query(sqlQuery,params,new PayRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Pay>();
		}	
	}


	@Override
	public Pay getPayById(int id) {
		String sqlQuery="select *"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS manufacturer_id, man.name AS manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ " from pays "
				+ " left join Users AS u on (pays.user=u.id)"
				+ " left join Client AS cl on (pays.client=cl.id)"
				+ " left join manufacturer AS man on (pays.manufacturer=man.id)"
				+ " where pays.id=:id";

		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		
		
		try{ 
			return (Pay)jdbcTemplate.queryForObject(sqlQuery,params,new PayRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Pay();
		}
	}
	
	@Override
	public ArrayList<Pay> getPrepayByContracter(int client_id, int manufacturer_id) {
		String sqlQuery="select *"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS manufacturer_id, man.name AS manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ " from pays "
				+ " left join Users AS u on (pays.user=u.id)"
				+ " left join Client AS cl on (pays.client=cl.id)"
				+ " left join manufacturer AS man on (pays.manufacturer=man.id)"
				+ " where (pays.client=:client_id) and (pays.manufacturer=:manufacturer_id)"
				+ "				and ((pays.demand_id is NULL) or (pays.demand_id=:nullik))"; 
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("client_id", client_id);
		params.addValue("manufacturer_id", manufacturer_id); //т.к. клиент платит только посреднику
		params.addValue("nullik", ""); 
		
		try{ 
			return (ArrayList<Pay>)jdbcTemplate.query(sqlQuery,params,new PayRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Pay>();
		}	
	}
	
	@Override
	public ArrayList<Pay> getPaysByClient(int client_id, int manufacturer_id) {
		String sqlQuery="select *"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS manufacturer_id, man.name AS manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ " from pays "
				+ " left join Users AS u on (pays.user=u.id)"
				+ " left join Client AS cl on (pays.client=cl.id)"
				+ " left join manufacturer AS man on (pays.manufacturer=man.id)"
				+ " where (pays.client=:client_id) and (pays.manufacturer=:manufacturer_id)"
				+ "				and ((pays.demand_id is not NULL) and (pays.demand_id!=:nullik))";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("client_id", client_id);
		params.addValue("manufacturer_id", manufacturer_id); //т.к. клиент платит только посреднику
		params.addValue("nullik", "");
		
		try{ 
			return (ArrayList<Pay>)jdbcTemplate.query(sqlQuery,params,new PayRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Pay>();
		}	
	}	
	

	@Override
	public ArrayList<Pay> getPaysByNumDoc(String numDoc) {
		String sqlQuery="select *"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS manufacturer_id, man.name AS manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ " from pays "
				+ " left join Users AS u on (pays.user=u.id)"
				+ " left join Client AS cl on (pays.client=cl.id)"
				+ " left join manufacturer AS man on (pays.manufacturer=man.id)"
				+ " where (pays.numDoc=:numDoc)"
				+ " and (((pays.summ>0) and (pays.storno=0)) or ((pays.summ<0) and (pays.storno=1)))";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("numDoc", numDoc);
		
		try{ 
			return (ArrayList<Pay>)jdbcTemplate.query(sqlQuery,params,new PayRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Pay>();
		}	
	}
	
	@Override
	public ArrayList<Pay> getPaysByNumDocOnleMarketing(String numDoc, int marketing_id) {
		String sqlQuery="select *"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS manufacturer_id, man.name AS manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ " from pays "
				+ " left join Users AS u on (pays.user=u.id)"
				+ " left join Client AS cl on (pays.client=cl.id)"
				+ " left join manufacturer AS man on (pays.manufacturer=man.id)"
				+ " where (pays.numDoc=:numDoc) and (pays.manufacturer=:marketing_id) "
				+ " and (((pays.summ>0) and (pays.storno=0)) or ((pays.summ<0) and (pays.storno=1)))";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("numDoc", numDoc);
		params.addValue("marketing_id", marketing_id);
		
		try{ 
			return (ArrayList<Pay>)jdbcTemplate.query(sqlQuery,params,new PayRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Pay>();
		}	
	}	
	
	@Override
	public ArrayList<Pay> getPaysForReport(Date begin, Date end, int marketing_id, int mainFirm_id) {
		begin.setHours(0);
		begin.setMinutes(0);
		begin.setSeconds(0);
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		
		String sqlQuery="select *"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", man.id AS manufacturer_id, man.name AS manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ " from pays "
				+ " left join Users AS u on (pays.user=u.id)"
				+ " left join Client AS cl on (pays.client=cl.id)"
				+ " left join manufacturer AS man on (pays.manufacturer=man.id)"
				+ " where (pays.time>=:begin) and (pays.time<=:end) and ((pays.manufacturer=:marketing_id) or (pays.manufacturer=:mainFirm_id)) "
				+ " and (((pays.summ>0) and (pays.storno=0)) or ((pays.summ<0) and (pays.storno=1))) order by pays.time, pays.manufacturer";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("marketing_id", marketing_id);
		params.addValue("mainFirm_id", mainFirm_id);
		params.addValue("begin", begin);
		params.addValue("end", end);
		
		try{ 
			return (ArrayList<Pay>)jdbcTemplate.query(sqlQuery,params,new PayRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Pay>();
		}	
	}

	
	@Override
	public void clearPaysDemand_id(String doc_id) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String sqlUpdate="update pays set demand_id=:clearDemand_id where demand_id=:demand_id";
		params.addValue("clearDemand_id", "");
		params.addValue("demand_id", doc_id);
		
		jdbcTemplate.update(sqlUpdate, params);
		
	}

	@Override
	public Pay createPay(Pay pay) {
		
		Pay result=new Pay();
		Pay currentPay = pay;

		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String sqlUpdate="insert into pays (time, demand_id, manufacturer, storno, summ, user, NumDoc, client)"
				+ " Values (:time, :demand_id, :manufacturer, :storno, :summ, :user, :NumDoc, :client)";
		if (currentPay.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update pays set time=:time, demand_id=:demand_id, manufacturer=:manufacturer, storno=:storno"
			 		+ ", summ=:summ, user=:user, NumDoc=:NumDoc, client=:client  where id=:id";
			 params.addValue("id", currentPay.getId());
		}
		
		params.addValue("time", pay.getTime());
		params.addValue("demand_id", pay.getDemand_id());
		params.addValue("user", (((User)pay.getUser()).getId()==0?Service.ID_CUSTOMER:((User)pay.getUser()).getId()));
		params.addValue("manufacturer", ((Manufacturer)pay.getManufacturer()).getId());
		params.addValue("storno", (pay.isStorno()?1:0));
		params.addValue("summ", (pay.isStorno()?-1:1)*pay.getSumm());
		params.addValue("NumDoc", pay.getNumDoc());
		params.addValue("client", ((Client)pay.getClient()).getId());
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getPayById(keyHolder.getKey().intValue());
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new Pay();
		}		
		
		return result;			
	}	
	

	@Override
	public void deletePay(int id) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String sqlUpdate="delete from pays where id=:id";
		params.addValue("id", id);
		
		jdbcTemplate.update(sqlUpdate, params);
	}

	@Override
	public Pay changeSumm(int id, double summ) {
		Pay result=null;
		
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("id", id);
		params.addValue("summ", summ);
		String sqlUpdate="update pays set summ=:summ where id=:id";

		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getPayById(keyHolder.getKey().intValue());
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new Pay();
		}		
		
		return result;		
	}	

	@Override
	public void deletePaysWithNumDoc(String doc_id) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String sqlUpdate="delete from pays where NumDoc=:doc_id";
		params.addValue("doc_id", doc_id);
		
		jdbcTemplate.update(sqlUpdate, params);
	}
	
	
	
	
	
	private static final class PayRowMapper implements RowMapper<Pay> {

		@Override
		public Pay mapRow(ResultSet rs, int rowNum) throws SQLException {
			Pay pay = new Pay();
			pay.setId(rs.getInt("id"));
			Timestamp timestamp = rs.getTimestamp("time");
			if (timestamp != null){
				pay.setTime(new java.util.Date(timestamp.getTime()));
			}
			pay.setDemand_id(rs.getString("demand_id"));
			Manufacturer manufacturer=new Manufacturer(rs.getInt("manufacturer_id"), rs.getString("manufacturer_name"), null);
			pay.setManufacturer(manufacturer);
			User user = new User(rs.getInt("user_id"), rs.getString("user_name"), rs.getString("user_email"), rs.getString("user_login"));
			pay.setUser(user);
			pay.setStorno((rs.getInt("storno")==0?false:true));
			pay.setSumm(rs.getDouble("summ"));
			pay.setNumDoc(rs.getString("numDoc"));
			pay.setClient(new Client(rs.getInt("client_id"),rs.getString("client_name")
					,rs.getString("client_email"),rs.getString("client_address"),null));
			
			return pay;
		}
	}




}
