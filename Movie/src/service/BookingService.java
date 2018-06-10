package service;

import java.util.List;
import java.util.Random;

import model.Booking;

public class BookingService {
  
  private SeatService seatServ = new SeatService();
  
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
	  
	  //预订座位后还要将座位锁定
	  if(seatServ.lockSeat(seat_id) == false)
	    return false;
	  
	  return book.save();
  }
  
  /**
   * 获取用户的全部订单信息
   * @param uid
   * @return
   */
  public List<Booking> getUserBookingInfos(Integer uid) 
	{
		return Booking.dao.find("select * from booking where user_id=?", uid);
	}
  
  /**
   * 根据 booking_id 获取订单详细信息
   * @param bookingId
   * @return
   */
  public Booking getBookingInfoById(int bookingId) {
    return Booking.dao.findFirst("select * from booking where booking_id=?", bookingId);
  }
}
