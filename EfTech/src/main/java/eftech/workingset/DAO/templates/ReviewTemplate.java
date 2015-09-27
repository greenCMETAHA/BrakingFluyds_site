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
	public ArrayList<Review> getReviews(int id) { //id brakingFluid
		String sqlQuery="select * from review where brakingfluid=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{ 
			return (ArrayList<Review>)jdbcTemplate.query(sqlQuery,new ReviewRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new ArrayList<Review>();
		}
	}
	
	@Override
	public Review getReviewById(int id) {  //id review
		String sqlQuery="select * from review where id=:id";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);

		try{ 
			return (Review)jdbcTemplate.query(sqlQuery,new ReviewRowMapper());
		}catch (EmptyResultDataAccessException e){
			return new Review();
		}
	}	

	@Override
	public Review createReview(Review review) {
		Review result=new Review();
		Review currentReview = null;
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

	private static final class ReviewRowMapper implements RowMapper<Review> {

	@Override
	public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
		Review result=new Review();

		result.setId(rs.getInt("id"));
		result.setName(rs.getString("name"));
		result.setEmail(rs.getString("email"));
		result.setJudgement(rs.getDouble("judgement"));
		BrakingFluid brFluid=new BrakingFluid();
		brFluid.setId(rs.getInt("brakingFluid"));
		
		return result;
	}

}	

}
