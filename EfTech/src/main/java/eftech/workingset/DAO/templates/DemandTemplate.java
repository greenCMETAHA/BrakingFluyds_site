package eftech.workingset.DAO.templates;

import eftech.workingset.DAO.interfaces.InterfaceDemandDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class DemandTemplate implements InterfaceDemandDAO{
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public int getCountRows(Date begin, Date end) {
		String sqlQuery="select count(*) from demand"+ (Service.isDate(begin,end)?" where (demand.time<=:end) and (demand.time>=:begin)":"");

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
		ArrayList<Demand> result=null;

		String sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join users AS u on (of.user=u.id)"
				+ " left join users AS exe on (of.executer=exe.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ " where of.goodprefix=:goodPrefix "
				//+ " order by of.time desc, of.demand_id limit :numPage, :quantity";
				+ " order by of.time desc, of.demand_id";


		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage-1);
		params.addValue("goodPrefix", Service.BRAKING_FLUID_PREFIX);

		result=(ArrayList<Demand>)jdbcTemplate.query(sqlQuery,params,new DemandRowMapper());

		sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join users AS u on (of.user=u.id)"
				+ " left join users AS exe on (of.executer=exe.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join motoroils AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ " where of.goodprefix=:goodPrefix "
				//+ " order by of.time desc, of.demand_id limit :numPage, :quantity";
				+ " order by of.time desc, of.demand_id";

		params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage-1);
		params.addValue("goodPrefix", Service.MOTOR_OIL_PREFIX);

		result.addAll((ArrayList<Demand>)jdbcTemplate.query(sqlQuery,params,new DemandRowMapper()));

		try{
			return result;
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

		ArrayList<Demand> result=null;

		String sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join users AS u on (of.user=u.id)"
				+ " left join users AS exe on (of.executer=exe.id)"
				+ " left join client AS cl on (of.client=cl.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ " where (of.goodprefix=:goodPrefix)"
				+ ((begin.getTime()<end.getTime())?" and (of.time>=:begin) ":"")
				+(executer_id>0?" and (of.executer=:executer_id) ":"")
				//+ " order by of.time desc, of.demand_id limit :numPage, :quantity";
				+ " order by of.time desc, of.demand_id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage-1);
		params.addValue("begin", begin);
		params.addValue("end", end);
		params.addValue("executer_id", executer_id);
		params.addValue("goodPrefix", Service.BRAKING_FLUID_PREFIX);

		result=(ArrayList<Demand>)jdbcTemplate.query(sqlQuery,params,new DemandRowMapper());

		sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join users AS u on (of.user=u.id)"
				+ " left join users AS exe on (of.executer=exe.id)"
				+ " left join client AS cl on (of.client=cl.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join motoroils AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ " where (of.goodprefix=:goodPrefix)"
				+ ((begin.getTime()<end.getTime())?" and (of.time>=:begin) ":"")
				+(executer_id>0?" and (of.executer=:executer_id) ":"")
				//+ " order by of.time desc, of.demand_id limit :numPage, :quantity";
				+ " order by of.time desc, of.demand_id";

		params = new MapSqlParameterSource();
		params.addValue("quantity", quantity);
		params.addValue("numPage", numPage-1);
		params.addValue("begin", begin);
		params.addValue("end", end);
		params.addValue("executer_id", executer_id);
		params.addValue("goodPrefix", Service.MOTOR_OIL_PREFIX);

		result.addAll((ArrayList<Demand>)jdbcTemplate.query(sqlQuery,params,new DemandRowMapper()));

		try{
			return result;
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Demand>();
		}
	}


	@Override
	public ArrayList<Demand> getDemand(String demand_id) {
		ArrayList<Demand> result=null;

		String sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join users AS u on (of.user=u.id)"
				+ " left join users AS exe on (of.executer=exe.id)"
				+ " left join client AS cl on (of.client=cl.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ " where of.demand_id=:demand_id and of.goodprefix=:goodPrefix";


		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("demand_id", demand_id);
		params.addValue("goodPrefix", Service.BRAKING_FLUID_PREFIX);

		result=(ArrayList<Demand>)jdbcTemplate.query(sqlQuery,params,new DemandRowMapper());

		sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join users AS u on (of.user=u.id)"
				+ " left join users AS exe on (of.executer=exe.id)"
				+ " left join client AS cl on (of.client=cl.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join motoroils AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ " where of.demand_id=:demand_id and of.goodprefix=:goodPrefix";


		params = new MapSqlParameterSource();
		params.addValue("demand_id", demand_id);
		params.addValue("goodPrefix", Service.MOTOR_OIL_PREFIX);

		result=(ArrayList<Demand>)jdbcTemplate.query(sqlQuery,params,new DemandRowMapper());

		try{
			return result;
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Demand>();
		}
	}

	@Override
	public Demand getDemandById(int id) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		String goodPrefix=null;
		String sqlQuery="select goodprefix from demand where demand.id=:id";
		try{
			goodPrefix=(String)jdbcTemplate.queryForObject(sqlQuery,params,String.class);
		}catch (EmptyResultDataAccessException e){
			return new Demand();
		}

		Demand result=null;

		if (goodPrefix!=null){
			sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.photo AS good_photo"
					+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
					+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
					+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name"
					+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
					+ ", status.id AS status_id, status.name AS status_name from demand as of "
					+ " left join users AS u on (of.user=u.id)"
					+ " left join users AS exe on (of.executer=exe.id)"
					+ " left join client AS cl on (of.client=cl.id)"
					+ " left join offerstatus AS status on (of.status=status.id)";

			if (Service.BRAKING_FLUID_PREFIX.equals(goodPrefix)){
				sqlQuery+=" left join brakingfluids AS bf on (of.good=bf.id)";
			}else if (Service.MOTOR_OIL_PREFIX.equals(goodPrefix)){
				sqlQuery+=" left join MotorOils AS bf on (of.good=bf.id)";
			}
			sqlQuery +=" left join manufacturer AS man on (bf.manufacturer=man.id)"
					+ " where of.id=:id";

			result=(Demand)jdbcTemplate.queryForObject(sqlQuery,params,new DemandRowMapper());
		}
		try{
			return result;
		}catch (EmptyResultDataAccessException e){
			return new Demand();
		}
	}

	@Override
	public Demand getDemandByDemandId(String demand_id, int good_id, String goodPrefix) {
		String sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join users AS u on (of.user=u.id)"
				+ " left join users AS exe on (of.executer=exe.id)"
				+ " left join client AS cl on (of.client=cl.id)"
				+ " left join offerstatus AS status on (of.status=status.id)";

		if (Service.BRAKING_FLUID_PREFIX.equals(goodPrefix)){
			sqlQuery+=" left join brakingfluids AS bf on (of.good=bf.id)";
		}else if (Service.MOTOR_OIL_PREFIX.equals(goodPrefix)){
			sqlQuery+=" left join MotorOils AS bf on (of.good=bf.id)";
		}
		sqlQuery+= " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ " where (of.demand_id=:demand_id) and (of.braking_fluid=:braking_good_id)";


		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("demand_id", demand_id);
		params.addValue("good_id", good_id);
		params.addValue("goodPrefix", goodPrefix);


		try{
			return (Demand)jdbcTemplate.queryForObject(sqlQuery,params,new DemandRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Demand();
		}
	}



	@Override
	public ArrayList<Demand> getDemandsByClient(int client_id) {
		ArrayList<Demand> result=null;

		String sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join users AS u on (of.user=u.id)"
				+ " left join users AS exe on (of.executer=exe.id)"
				+ " left join client AS cl on (of.client=cl.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join brakingfluids AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ " where of.client=:client_id order by time desc";


		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("client_id", client_id);
		params.addValue("goodPrefix", Service.BRAKING_FLUID_PREFIX);

		result=(ArrayList<Demand>)jdbcTemplate.query(sqlQuery,params,new DemandRowMapper());

		sqlQuery="select *, bf.id AS good_id, bf.name AS good_name, bf.price AS good_price, bf.photo AS good_photo"
				+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
				+ ", exe.id AS executer_id, exe.name AS executer_name, exe.email AS executer_email, exe.login AS executer_login"
				+ ", man.id AS good_manufacturer_id, man.name AS good_manufacturer_name"
				+ ", cl.id AS client_id, cl.name AS client_name, cl.email AS client_email, cl.address AS client_address"
				+ ", status.id AS status_id, status.name AS status_name from demand as of "
				+ " left join users AS u on (of.user=u.id)"
				+ " left join users AS exe on (of.executer=exe.id)"
				+ " left join client AS cl on (of.client=cl.id)"
				+ " left join offerstatus AS status on (of.status=status.id)"
				+ " left join motoroils AS bf on (of.good=bf.id)"
				+ " left join manufacturer AS man on (bf.manufacturer=man.id)"
				+ " where of.client=:client_id order by time desc";


		params = new MapSqlParameterSource();
		params.addValue("client_id", client_id);
		params.addValue("goodPrefix", Service.MOTOR_OIL_PREFIX);

		result.addAll((ArrayList<Demand>)jdbcTemplate.query(sqlQuery,params,new DemandRowMapper()));

		try{
			return result;
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Demand>();
		}					}



	private Demand createDemand(Demand demand){

		Demand result=new Demand();
		Demand currentDemand = demand;

		MapSqlParameterSource params = new MapSqlParameterSource();

		String sqlUpdate="insert into demand (time, demand_id, good, quantity, price, user, status, executer, client, goodprefix)"
				+ " Values (:time, :demand_id, :good, :quantity, :price, :user, :status, :executer, :client, :goodPrefix)";
		if (currentDemand.getId()>0){ // В БД есть такой элемент
			sqlUpdate="update demand set time=:time, demand_id=:demand_id, good=:good, quantity=:quantity"
					+ ", price=:price, user=:user, status=:status, executer=:executer , client=:client, goodprefix=:goodPrefix where id=:id";
			params.addValue("id", currentDemand.getId());
		}

		params.addValue("time", demand.getTime());
		params.addValue("demand_id", demand.getDemand_id());
		params.addValue("good", demand.getGood().getId());
		params.addValue("goodPrefix", demand.getGood().getGoodName());
		params.addValue("quantity", demand.getQuantity());
		params.addValue("price", demand.getGood().getPrice());
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
			for (Basket current:copyBasket){
				if ((demand.getGood().getId()==current.getGood().getId()) && (demand.getGood().getGoodName().equals(current.getGood().getGoodName()))){
					bFind=true;
					result.add(demand);
					basket.remove(current);
					break;

				}
			}
		}
		for (Basket current:copyBasket){			//добавим новые...
			Demand demand=new Demand();
			demand.setDemand_id(demand_id);
			demand.setPrice(current.getGood().getPrice());
			demand.setQuantity(current.getQauntity());
			demand.setTime(dataDoc);
			demand.setStatus(status);
			demand.setUser(user);
			demand.setGood(current.getGood());
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
				demand.setGood(current);
			}else if (Service.MOTOR_OIL_PREFIX.equals(rs.getString("goodPrefix"))){
				MotorOil current=new MotorOil();
				current.setId(rs.getInt("good_id"));
				current.setName(rs.getString("good_name"));
				current.setPrice(rs.getDouble("good_price"));
				current.setPhoto(rs.getString("good_photo"));
				current.setManufacturer(manufacturer);
				demand.setGood(current);
			}

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
