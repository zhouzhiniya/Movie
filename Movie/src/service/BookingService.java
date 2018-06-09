package service;

import java.util.List;
import java.util.Random;

import com.jfinal.plugin.activerecord.Db;

import model.Booking;

public class BookingService {
  public boolean addBooking(String user_id, String showing_id, String seat_id) {
	  Booking book =new Booking();
	  Random random = new Random();  
	  String token="";  
	  for (int i=0;i<6;i++)  
	  {  
	      token+=random.nextInt(10);  
	  }  
	  System.out.println(token);
	  book.setUserId(Integer.parseInt(user_id));
	  book.setSeatId(Integer.parseInt(seat_id));
	  book.setShowingId(Integer.parseInt(showing_id));
	  book.setToken(token);
	  return book.save();
  }
  
  public List<Booking> getBookingInfo(Integer uid) 
	{
		return Booking.dao.find("select * from booking where user_id=?", uid);
	}
 
}
