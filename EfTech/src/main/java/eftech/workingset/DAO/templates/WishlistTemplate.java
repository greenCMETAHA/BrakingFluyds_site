package eftech.workingset.DAO.templates;

import eftech.workingset.DAO.interfaces.InterfaceWishlistDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.*;
import eftech.workingset.beans.intefaces.base.InterfaceGood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class WishlistTemplate implements InterfaceWishlistDAO {
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public  int getCountRows(){
		String sqlQuery="select count(*) from wishlist";

		MapSqlParameterSource params = new MapSqlParameterSource();

		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}
	}

	private String returnPrefix(int id){
		String sqlQuery="select goodprefix from wishlist where wishlist.id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,String.class);
		}catch (EmptyResultDataAccessException e){
			return "BrF";
		}
	}

	@Override
	public Wishlist getWishById(int id){
		if (id==0){
			return new Wishlist();
		} else{
			String sqlQuery=null;
			String goodPrefix=returnPrefix(id);
			if (Service.BRAKING_FLUID_PREFIX.equals(goodPrefix)){
				sqlQuery="select w.id AS wish_id, w.goodPrefix as goodPrefix"
						+ ", u.id AS User_id, u.name AS User_name, u.email AS user_email, u.login AS user_login"
						+ ", fc.id AS fluidclass, fc.name AS fc_name"
						+ ", m.id AS man_id, m.name AS man_name"
						+ ", bf.id AS good_id, bf.name AS good_name, bf.price AS price, bf.photo AS photo, bf.value AS value, bf.judgement AS judgement"
						+ " from wishlist w"
						+ " left join users AS u ON (w.user=u.id)"
						+ " left join brakingfluids AS bf ON (w.good=bf.id)"
						+ " left join fluidclass as fc on (bf.fluidclass=fc.id)"
						+ " left join manufacturer as m on (bf.manufacturer=m.id) where w.id=:id";
			}else if (Service.MOTOR_OIL_PREFIX.equals(goodPrefix)){
				sqlQuery="select w.id AS wish_id, w.goodPrefix as goodPrefix"
						+ ", u.id AS User_id, u.name AS User_name, u.email AS user_email, u.login AS user_login"
						+ ", os.id AS oilstuff, os.name AS os_name"
						+ ", et.id AS engineType, et.name AS et_name"
						+ ", m.id AS man_id, m.name AS man_name"
						+ ", mo.id AS good_id, mo.name AS good_name, mo.price AS price, mo.photo AS photo, mo.value AS value, mo.judgement AS judgement"
						+ " from wishlist w"
						+ " left join users AS u ON (w.user=u.id)"
						+ " left join motoroils AS mo ON (w.good=mo.id)"
						+ " left join oilstuff as os on (mo.oilstuff=os.id)"
						+ " left join enginetype as et on (mo.engineType=et.id)"
						+ " left join manufacturer as m on (mo.manufacturer=m.id) where w.id=:id";
			}else if (Service.GEARBOX_OIL_PREFIX.equals(goodPrefix)){
				sqlQuery="select w.id AS wish_id, w.goodPrefix as goodPrefix"
						+ ", u.id AS User_id, u.name AS User_name, u.email AS user_email, u.login AS user_login"
						+ ", os.id AS oilstuff, os.name AS os_name"
						+ ", gbt.id AS gearBoxType, gbt.name AS gbt_name"
						+ ", m.id AS man_id, m.name AS man_name"
						+ ", go.id AS good_id, go.name AS good_name, go.price AS price, go.photo AS photo, go.value AS value, go.judgement AS judgement"
						+ " from wishlist w"
						+ " left join users AS u ON (w.user=u.id)"
						+ " left join gearboxoils AS go ON (w.good=go.id)"
						+ " left join oilstuff as os on (go.oilstuff=os.id)"
						+ " left join gearboxtype as gbt on (go.gearBoxType=gbt.id)"
						+ " left join manufacturer as m on (go.manufacturer=m.id) where w.id=:id";
			}

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id", id);

			try{
				return (Wishlist)jdbcTemplate.queryForObject(sqlQuery,params,new WishlistRowMapper());
			}catch (EmptyResultDataAccessException e){
				return new Wishlist();
			}
		}
	}

	public Wishlist getWish(int user_id, int goodId, String goodPrefix){
		String sqlQuery="select * from wishlist where user=:user_id AND good=:goodId and goodprefix=:goodPrefix";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("user_id", user_id);
		params.addValue("goodId", goodId);
		params.addValue("goodPrefix", goodPrefix);

		try{
			return (Wishlist)jdbcTemplate.query(sqlQuery,new WishRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Wishlist();
		}

	}

	@Override
	public Wishlist addToWishlist(Wishlist wish){
		Wishlist result=new Wishlist();
		Wishlist currentWishlist = wish;
		if (wish.getId()>0){
			currentWishlist=getWish(((User)wish.getUser()).getId(),wish.getGood().getId(),wish.getGood().getGoodName()); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}
		String sqlUpdate="insert into wishlist (user, good, goodprefix) Values (:user, :good, :goodPrefix)";
		if (currentWishlist.getId()>0){ // В БД есть такой элемент
			sqlUpdate="update review set user=:user, good=:good, goodprefix=:goodPrefix  where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("user", ((User)wish.getUser()).getId());
		params.addValue("good", wish.getGood().getId());
		params.addValue("goodPrefix", wish.getGood().getGoodName());
		if (currentWishlist.getId()>0){
			params.addValue("id", wish.getId());
		}

		KeyHolder keyHolder=new GeneratedKeyHolder();

		jdbcTemplate.update(sqlUpdate, params, keyHolder);

		try{
			if (keyHolder.getKey()!=null) {
				result=getWishById(keyHolder.getKey().intValue());
			}

		}catch (EmptyResultDataAccessException e){
			result = new Wishlist();
		}

		return result;
	}

	@Override
	public void deleteFromWishlist(InterfaceGood good, User user){
		String sqlUpdate="delete from wishlist where user=:user and good=:good and goodprefix=:goodPrefix";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("user",  user.getId());
		params.addValue("good", good.getId());
		params.addValue("goodPrefix", good.getGoodName());

		jdbcTemplate.update(sqlUpdate, params);
	}

	@Override
	public void deleteFromWishlist(Wishlist wish) {
		deleteFromWishlist(wish.getId());
	}

	@Override
	public void deleteFromWishlist(int id) {
		String sqlUpdate="delete from wishlist where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		jdbcTemplate.update(sqlUpdate, params);

	}

	public LinkedList<Wishlist> getWishList(int id){
		return getWishList(id,0,0);
	}

	@Override
	public LinkedList<Wishlist> getWishList(int User_id, int num, int nextRows) {
		ArrayList<Wishlist> result=null;

		if (User_id>0){

			String sqlQuery="select w.id AS wish_id, w.goodprefix as goodprefix"                          //сначала пройдёмся по тормозным жидкостям
					+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
					+ ", fc.id AS fluidclass, fc.name AS fc_name"
					+ ", m.id AS man_id, m.name AS man_name"
					+ ", bf.id AS good_id, bf.name AS good_name, bf.price AS price, bf.photo AS photo, bf.value AS value, bf.judgement AS judgement"
					+ " from wishlist w"
					+ " left join users AS u ON (w.user=u.id)"
					+ " left join brakingfluids AS bf ON (w.good=bf.id)"
					+ " left join fluidclass as fc on (bf.fluidclass=fc.id)"
					+ " left join manufacturer as m on (bf.manufacturer=m.id) "
					+ " where w.goodprefix=:goodPrefix"+(User_id!=0?" and u.id=:User_id":"")
					+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);


			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("User_id", User_id);
			params.addValue("goodPrefix", Service.BRAKING_FLUID_PREFIX);

			result=(ArrayList<Wishlist>)jdbcTemplate.query(sqlQuery,params,new WishlistRowMapper());

			sqlQuery="select w.id AS wish_id, w.goodPrefix as goodprefix"                          //теперь - по MotorOil
					+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
					+ ", os.id AS oilstuff, os.name AS os_name"
					+ ", et.id AS enginetype, et.name AS et_name"
					+ ", m.id AS man_id, m.name AS man_name"
					+ ", mo.id AS good_id, mo.name AS good_name, mo.price AS price, mo.photo AS photo, mo.value AS value, mo.judgement AS judgement"
					+ " from wishlist w"
					+ " left join users AS u ON (w.user=u.id)"
					+ " left join motoroils AS mo ON (w.good=mo.id)"
					+ " left join oilstuff as os on (mo.oilstuff=os.id)"
					+ " left join enginetype as et on (mo.enginetype=et.id)"
					+ " left join manufacturer as m on (mo.manufacturer=m.id) "
					+ " where w.goodprefix=:goodPrefix"+(User_id!=0?" and u.id=:User_id":"")
					+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);


			params = new MapSqlParameterSource();
			params.addValue("User_id", User_id);
			params.addValue("goodPrefix", Service.MOTOR_OIL_PREFIX);

			result.addAll((ArrayList<Wishlist>)jdbcTemplate.query(sqlQuery,params,new WishlistRowMapper()));
			
			sqlQuery="select w.id AS wish_id, w.goodPrefix as goodprefix"                          //теперь - по MotorOil
					+ ", u.id AS user_id, u.name AS user_name, u.email AS user_email, u.login AS user_login"
					+ ", os.id AS oilstuff, os.name AS os_name"
					+ ", gbt.id AS gearboxtype, gbt.name AS gbt_name"
					+ ", m.id AS man_id, m.name AS man_name"
					+ ", go.id AS good_id, go.name AS good_name, go.price AS price, go.photo AS photo, go.value AS value, go.judgement AS judgement"
					+ " from wishlist w"
					+ " left join users AS u ON (w.user=u.id)"
					+ " left join gearboxoils AS go ON (w.good=go.id)"
					+ " left join oilstuff as os on (go.oilstuff=os.id)"
					+ " left join gearboxtype as gbt on (go.gearboxtype=gbt.id)"
					+ " left join manufacturer as m on (go.manufacturer=m.id) "
					+ " where w.goodprefix=:goodPrefix"+(User_id!=0?" and u.id=:User_id":"")
					+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);


			params = new MapSqlParameterSource();
			params.addValue("User_id", User_id);
			params.addValue("goodPrefix", Service.GEARBOX_OIL_PREFIX);

			result.addAll((ArrayList<Wishlist>)jdbcTemplate.query(sqlQuery,params,new WishlistRowMapper()));			

			try{
				return new LinkedList<Wishlist>(result);
			}catch (EmptyResultDataAccessException e){
				return new LinkedList<Wishlist>();
			}catch (InvalidDataAccessApiUsageException e){
				return new LinkedList<Wishlist>();
			}
		}else{
			return new LinkedList<Wishlist>();
		}

	}

	private static final class WishRowMapper implements RowMapper<Wishlist> {

		@Override
		public Wishlist mapRow(ResultSet rs, int rowNum) throws SQLException { //некоторые поля не описаны. Их в общем списке выводить не будем. Если юзер захочет посмотреть - откроет карточку жидкости
			Wishlist result=new Wishlist();

			result.setId(rs.getInt("id"));

			User user=new User();
			user.setId(rs.getInt("user"));

			InterfaceGood currentGood=null;
			if (Service.BRAKING_FLUID_PREFIX.equals(rs.getInt("goodPrefix"))){
				currentGood=new BrakingFluid();
			}else if (Service.MOTOR_OIL_PREFIX.equals(rs.getInt("goodPrefix"))){
				currentGood=new MotorOil();
			}else if (Service.GEARBOX_OIL_PREFIX.equals(rs.getInt("goodPrefix"))){
				currentGood=new GearBoxOil();
			}
			currentGood.setId(rs.getInt("good"));

			result.setGood(currentGood);

			return result;
		}
	}

	private static final class WishlistRowMapper implements RowMapper<Wishlist> {

		@Override
		public Wishlist mapRow(ResultSet rs, int rowNum) throws SQLException { //некоторые поля не описаны. Их в общем списке выводить не будем. Если юзер захочет посмотреть - откроет карточку жидкости
			Wishlist result=new Wishlist();

			result.setId(rs.getInt("wish_id"));

			User user=new User();
			user.setId(rs.getInt("user_id"));
			user.setName(rs.getString("user_name"));
			user.setEmail(rs.getString("user_email"));
			user.setLogin(rs.getString("user_login"));

			Manufacturer manufacturer =new Manufacturer();
			manufacturer.setId(rs.getInt("man_id"));
			manufacturer.setName(rs.getString("man_name"));

			InterfaceGood good=null;
			String goodPrefix=rs.getString("goodPrefix");
			if (Service.BRAKING_FLUID_PREFIX.equals(goodPrefix)){
				BrakingFluid brFluid=new BrakingFluid();

				FluidClass fClass=new FluidClass(rs.getInt("fluidclass"),rs.getString("fc_name"));
				brFluid.setFluidClass(fClass);

				brFluid.setManufacturer(manufacturer);

				brFluid.setPhoto(rs.getString("photo"));
				brFluid.setValue(rs.getDouble("value"));
				brFluid.setJudgement(rs.getDouble("judgement"));

				good=brFluid;
			}else if (Service.MOTOR_OIL_PREFIX.equals(goodPrefix)){
				MotorOil oil=new MotorOil();

				oil.setManufacturer(manufacturer);

				OilStuff oilStuff =new OilStuff();
				oilStuff.setId(rs.getInt("oilstuff"));
				oilStuff.setName(rs.getString("os_name"));
				oil.setOilStuff(oilStuff);

				EngineType engineType =new EngineType();
				engineType.setId(rs.getInt("engineType"));
				engineType.setName(rs.getString("et_name"));
				oil.setEngineType(engineType);

				oil.setPhoto(rs.getString("photo"));
				oil.setValue(rs.getDouble("value"));
				oil.setJudgement(rs.getDouble("judgement"));

				good=oil;
			}else if (Service.GEARBOX_OIL_PREFIX.equals(goodPrefix)){
				GearBoxOil oil=new GearBoxOil();

				oil.setManufacturer(manufacturer);

				OilStuff oilStuff =new OilStuff();
				oilStuff.setId(rs.getInt("oilstuff"));
				oilStuff.setName(rs.getString("os_name"));
				oil.setOilStuff(oilStuff);

				GearBoxType gearBoxType =new GearBoxType();
				gearBoxType.setId(rs.getInt("gearBoxType"));
				gearBoxType.setName(rs.getString("gbt_name"));
				oil.setGearBoxType(gearBoxType);

				oil.setPhoto(rs.getString("photo"));
				oil.setValue(rs.getDouble("value"));
				oil.setJudgement(rs.getDouble("judgement"));

				good=oil;				
			}
			good.setId(rs.getInt("good_id"));
			good.setName(rs.getString("good_name"));
			good.setPrice(rs.getDouble("price"));

			result.setGood(good);

			return result;
		}
	}
}
