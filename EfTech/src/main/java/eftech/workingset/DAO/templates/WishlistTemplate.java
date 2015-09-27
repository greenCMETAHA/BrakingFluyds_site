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

import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;
import eftech.workingset.beans.intefaces.InterfaceWishlist;

public class WishlistTemplate implements InterfaceWishlist {
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public ArrayList<Wishlist> getWishList(int id){
		String sqlQuery="select w.id AS wich_id"
				+ ",u.id AS User_id, u.name AS User_name, u.email AS user_email, u.login AS User_login"
				+ ", fc.id AS fluidclass, fc.name AS fc_name"
				+ ", m.id AS man_id, m.name AS man_name"
				+ ", bf.id AS bf_id, bf.name AS bf_name, bf.price AS price, bf.photo AS photo, bf.value AS value, bf.judgement AS judgement"
				+ " from wishlist w"
				+ " left join users AS u ON (w.user=u.id)"
				+ " left join BrakingFluids AS bf ON (w.brakingFluid=bf.id)"
				+ " left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ " left join manufacturer as m on (bf.manufacturer=m.id) where u.id=:id";
		
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
	
		try{ 
			return (ArrayList<Wishlist>)jdbcTemplate.query(sqlQuery,new WishRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Wishlist>();
		}
	}
	
	public Wishlist getWishById(int id){
		String sqlQuery="select w.id AS wich_id"
				+ ",u.id AS User_id, u.name AS User_name, u.email AS user_email, u.login AS User_login"
				+ ", fc.id AS fluidclass, fc.name AS fc_name"
				+ ", m.id AS man_id, m.name AS man_name"
				+ ", bf.id AS bf_id, bf.name AS bf_name, bf.price AS price, bf.photo AS photo, bf.value AS value, bf.judgement AS judgement"
				+ " from wishlist w"
				+ " left join users AS u ON (w.user=u.id)"
				+ " left join BrakingFluids AS bf ON (w.brakingFluid=bf.id)"
				+ " left join fluidclass as fc on (bf.fluidclass=fc.id)"
				+ " left join manufacturer as m on (bf.manufacturer=m.id) where w.id=:id";
		
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
	
		try{ 
			return (Wishlist)jdbcTemplate.query(sqlQuery,new WishlistRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Wishlist();
		}
	}	

	public Wishlist getWish(int user_id, int brakingFluid_id){
		String sqlQuery="select * from wishlist where user=:user_id AND brakingFluid=:brakingFluid_id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("user_id", user_id);
		params.addValue("brakingFluid_id", brakingFluid_id);

		try{ 
			return (Wishlist)jdbcTemplate.query(sqlQuery,new WishRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Wishlist();
		}
		
	}

	public Wishlist addToWishlist(Wishlist wish){
		Wishlist result=new Wishlist();
		Wishlist currentWishlist = null;
		if (wish.getId()>0){
			currentWishlist=getWish(((User)wish.getUser()).getId(),((BrakingFluid)wish.getBrakingFluid()).getId()); //���� ��� ��������������, � ��������� ��� ����� Id. ����� �������������, ��� ����� ������� ���� � ��
		}
		String sqlUpdate="insert into Wishlist (user, brakingFluid) Values (:name, :brakingFluid)";
		if (currentWishlist.getId()>0){ // � �� ���� ����� �������
			 sqlUpdate="update review set user=:user, brakingfluid=:brakingfluid where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("user", ((User)wish.getUser()).getId());
		params.addValue("brakingFluid", ((BrakingFluid)wish.getBrakingFluid()).getId());
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
	
	public void deleteFromWishlist(BrakingFluid brakingFluid, User user){
		String sqlUpdate="delete from Wishlist where user=:user and brakingfluid=:brakingfluid";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("user",  user.getId());
		params.addValue("brakingFluid", brakingFluid.getId());
		
		jdbcTemplate.update(sqlUpdate, params);
	}	

	private static final class WishlistRowMapper implements RowMapper<Wishlist> {

		@Override
		public Wishlist mapRow(ResultSet rs, int rowNum) throws SQLException { //��������� ���� �� �������. �� � ����� ������ �������� �� �����. ���� ���� ������� ���������� - ������� �������� ��������
			Wishlist result=new Wishlist();
	
			result.setId(rs.getInt("wish_id"));
			
			User user=new User();
			user.setId(rs.getInt("user_id"));
			user.setName(rs.getString("user_name"));
			user.setEmail(rs.getString("user_email"));
			user.setLogin(rs.getString("user_login"));
			
			BrakingFluid brFluid=new BrakingFluid();
			brFluid.setId(rs.getInt("bf_id"));
			brFluid.setName(rs.getString("bf_name"));
	
		  	FluidClass fClass=new FluidClass(rs.getInt("fluidclass"),rs.getString("fc_name"));
			brFluid.setFluidClass(fClass);
			Manufacturer manufacturer =new Manufacturer();
			manufacturer.setId(rs.getInt("man_id"));
			manufacturer.setName(rs.getString("man_name"));
			brFluid.setManufacturer(manufacturer);
			brFluid.setPhoto(rs.getString("photo"));
			brFluid.setPrice(rs.getDouble("price"));
			brFluid.setValue(rs.getDouble("value"));
			brFluid.setJudgement(rs.getDouble("judgement"));
			result.setBrakingFluid(brFluid);
	
			return result;
		}
	}
	
	private static final class WishRowMapper implements RowMapper<Wishlist> {

		@Override
		public Wishlist mapRow(ResultSet rs, int rowNum) throws SQLException { //��������� ���� �� �������. �� � ����� ������ �������� �� �����. ���� ���� ������� ���������� - ������� �������� ��������
			Wishlist result=new Wishlist();

			result.setId(rs.getInt("id"));
			
			User user=new User();
			user.setId(rs.getInt("user"));
			
			BrakingFluid brFluid=new BrakingFluid();
			brFluid.setId(rs.getInt("brakingFluid"));
			result.setBrakingFluid(brFluid);

			return result;
		}	
	}	
}