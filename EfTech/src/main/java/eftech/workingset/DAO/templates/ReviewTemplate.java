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

import eftech.workingset.DAO.interfaces.InterfaceReviewDAO;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Info;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.MotorOil;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

public class ReviewTemplate implements InterfaceReviewDAO {
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public  int getCountRows(){
		String sqlQuery="select count(*) from review";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		try{
			return jdbcTemplate.queryForObject(sqlQuery,params,Integer.class);
		}catch (EmptyResultDataAccessException e){
			return 0;
		}				
	}	
	
	@Override
	public Review getReviewById(int id) {  //id review
		String sqlQuery="select * from review where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{ 
			return (Review)jdbcTemplate.queryForObject(sqlQuery,params,new ReviewRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Review();
		}
	}	

	@Override
	public Review createReview(Review review, String goodPrefix) {
		Review result=new Review();
		Review currentReview = review;
		if (review.getId()>0){
			currentReview=getReviewById(review.getId()); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}
		String sqlUpdate="insert into review (name, email, judgement, review, good, goodPrefix) Values (:name, :email, :judgement, :review, :good, :goodPrefix)";
		if (currentReview.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update review set name=:name, email=:email, judgement=:judgement, review=:review, good=:good, goodPrefix=:goodPrefix where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", review.getName());
		params.addValue("email", review.getEmail());
		params.addValue("judgement", review.getJudgement());
		params.addValue("review", review.getReview());
		params.addValue("good", review.getGood().getId());
		params.addValue("goodPrefix", review.getGood().getGoodName());
		
		KeyHolder keyHolder=new GeneratedKeyHolder(); 
		
		jdbcTemplate.update(sqlUpdate, params, keyHolder);
		
		try{
			if (keyHolder.getKey()!=null) {
				result=getReviewById(keyHolder.getKey().intValue());
			}
					
		}catch (EmptyResultDataAccessException e){
			result = new Review();
		}		
		
		return result;	
	}


	@Override
	public ArrayList<Review> getReviews(int id, String goodPrefix) { //id brakingFluid
		return getReviews(id, goodPrefix,0,0);
	}
	
	@Override
	public ArrayList<Review> getReviews(int id, String goodPrefix, int num, int nextRows) {
		String sqlQuery="select *, bf.name AS Bf_name  from review ";
				if (Service.BRAKING_FLUID_PREFIX.equals(goodPrefix)){
					sqlQuery+="left join brakingfluids as bf on  (bf.id=review.good) ";
				}else if (Service.MOTOR_OIL_PREFIX.equals(goodPrefix)){
					sqlQuery+="left join motorOils as bf on  (bf.id=review.good) ";
				}
					
				sqlQuery+= (id>0?" where review.good=:id and review.goodPrefix=:goodPrefix":"")
				+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		params.addValue("goodPrefix", goodPrefix);

		try{ 
			return (ArrayList<Review>)jdbcTemplate.query(sqlQuery,params,new ReviewRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Review>();
		}
	}

	@Override
	public void deleteReview(Review review) {
		deleteReview(review.getId());
	}

	@Override
	public void deleteReview(int id) {
		String sqlUpdate="delete from Review where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id",  id);
		
		jdbcTemplate.update(sqlUpdate, params);
	}	
	
	
	private static final class ReviewRowMapper implements RowMapper<Review> {
	
		@Override
		public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
			Review result=new Review();
	
			result.setId(rs.getInt("id"));
			result.setName(rs.getString("name"));
			result.setEmail(rs.getString("email"));
			result.setReview(rs.getString("review"));
			result.setJudgement(rs.getDouble("judgement"));

			InterfaceGood good=null;
			String goodPrefix=rs.getString("goodPrefix");
			if (Service.BRAKING_FLUID_PREFIX.equals(goodPrefix)){
				good=new BrakingFluid();
			}else if (Service.MOTOR_OIL_PREFIX.equals(goodPrefix)){
				good=new MotorOil();
			}
			good.setId(rs.getInt("good"));
			//good.setName(rs.getString("bf_name"));
			result.setGood(good);
			
			return result;
		}
	
	}

}
