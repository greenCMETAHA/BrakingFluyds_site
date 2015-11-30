package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import eftech.workingset.beans.Review;

public interface InterfaceReviewDAO {
	ArrayList<Review> getReviews(int id, String goodPrefix);
	ArrayList<Review> getReviews(int id, String goodPrefix, int num, int nextRows);
	Review getReviewById(int id);
	int getCountRows();
	
	Review createReview(Review review, String goodPrefix);
	void deleteReview(Review review);
	void deleteReview(int id);
}
