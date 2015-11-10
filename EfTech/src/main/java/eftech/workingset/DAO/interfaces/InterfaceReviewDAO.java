package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import java.util.LinkedList;

import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.Role;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

public interface InterfaceReviewDAO {
	ArrayList<Review> getReviews(int id, String goodPrefix);
	ArrayList<Review> getReviews(int id, String goodPrefix, int num, int nextRows);
	Review getReviewById(int id);
	int getCountRows();
	
	Review createReview(Review review, String goodPrefix);
	void deleteReview(Review review);
	void deleteReview(int id);
}
