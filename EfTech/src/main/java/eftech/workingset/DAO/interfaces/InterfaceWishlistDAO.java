package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;

public interface InterfaceWishlistDAO {
	ArrayList<Wishlist> getWishList(int id);
	Wishlist getWish(int user_id, int brakingFluid_id);
	Wishlist getWish(int id);
	Wishlist addToWishlist(Wishlist wish);
	void deleteFromWishlist(Wishlist wish);

}
