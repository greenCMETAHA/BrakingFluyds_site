package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.Role;

public interface InterfaceReviewDAO {
	ArrayList<Review> getReviews(int id);
	ArrayList<Review> getReviews(int id, int num, int nextRows);
	Review getReviewById(int id);
	int getCountRows();
	
	Review createReview(Review review);
	void deleteReview(Review review);
	void deleteReview(int id);
	
}
