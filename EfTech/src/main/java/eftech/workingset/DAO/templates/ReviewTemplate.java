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
import eftech.workingset.beans.Review;

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
	public Review createReview(Review review) {
		Review result=new Review();
		Review currentReview = review;
		if (review.getId()>0){
			currentReview=getReviewById(review.getId()); //если это редактирование, в структуре уже будет Id. ТОгда удостоверимся, что такой элемент есть в БД
		}
		String sqlUpdate="insert into review (name, email, judgement, review, brakingfluid) Values (:name, :email, :judgement, :review, :brakingfluid)";
		if (currentReview.getId()>0){ // В БД есть такой элемент
			 sqlUpdate="update review set name=:name, email=:email, judgement=:judgement, review=:review, brakingfluid=:brakingfluid where id=:id";
		}

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", review.getName());
		params.addValue("email", review.getEmail());
		params.addValue("judgement", review.getJudgement());
		params.addValue("review", review.getReview());
		
		BrakingFluid brFluid=(BrakingFluid)review.getBrakingFluid();
		params.addValue("brakingfluid", brFluid.getId());
		
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
	public ArrayList<Review> getReviews(int id) { //id brakingFluid
		return getReviews(id,0,0);
	}
	
	@Override
	public ArrayList<Review> getReviews(int id, int num, int nextRows) {
		String sqlQuery="select *, bf.name AS Bf_name  from review "
				+ "left join brakingfluids as bf on  (bf.id=review.brakingfluid) "
				+ (id==0?" where brakingfluid=:id ":"")
				+ ((num+nextRows)==0?"":" LIMIT "+((num-1)*Service.LOG_ELEMENTS_IN_LIST)+","+Service.LOG_ELEMENTS_IN_LIST);

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

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
			BrakingFluid brFluid=new BrakingFluid();
			brFluid.setId(rs.getInt("brakingFluid"));
			brFluid.setName(rs.getString("bf_name"));
			result.setBrakingFluid(brFluid);
			
			return result;
		}
	
	}

}
