package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Review;
import eftech.workingset.beans.Role;

public interface InterfaceReviewDAO {
	ArrayList<Review> getReviews(int id);
	Review getReviewById(int id);
	Review createReview(Review review);
}
