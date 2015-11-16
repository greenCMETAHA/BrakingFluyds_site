package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import java.util.LinkedList;

import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

public interface InterfaceWishlistDAO {
	LinkedList<Wishlist> getWishList(int id);
	LinkedList<Wishlist> getWishList(int id, int num, int nextRows);

	Wishlist getWish(int user_id, int goodId, String goodPrefix);
	Wishlist getWishById(int id);
	Wishlist addToWishlist(Wishlist wish);
	int getCountRows();
	
	void deleteFromWishlist(InterfaceGood good, User user);
	void deleteFromWishlist(Wishlist wish);
	void deleteFromWishlist(int id);
	

}
