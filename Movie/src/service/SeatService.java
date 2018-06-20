package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Booking;
import model.Seat;
import model.Showing;

public class SeatService {
  
	//根据showing_id获取放映厅的座位
	public List<Seat> getSeatsByShowingId(String showingid) {
		String sql = "select * from seat,showing where seat.auditorium_id = showing.auditorium_id and showing_id = " + showingid;
		System.out.print(sql);
		return Seat.dao.find(sql);
	}
	
	/**
	 * 根据Auditorium_id获取放映厅所有座位详情
	 * @param auditoriumId
	 * @return
	 */
	public List<Seat> getAllSeatByAuditorium(int auditoriumId){
	  List<Seat> result = Seat.dao.find("SELECT * FROM seat where auditorium_id = ?", auditoriumId);
	  return result;
	}


	
	public Seat getSeatByNameAndShowing(String seatName, int showingId) {
	  Seat result = Seat.dao.findFirst("select * from seat,showing where seat.auditorium_id = showing.auditorium_id AND showing.showing_id=? AND seat.seat=?", showingId, seatName);
//	  Seat avail = Seat.dao.findFirst("SELECT seat.seat_id,seat.auditorium_id,seat.seat FROM seat, auditorium, showing WHERE seat.seat_id not IN ( SELECT seat_id FROM booking WHERE booking.showing_id = ? ) AND seat.auditorium_id = auditorium.auditorium_id AND showing.auditorium_id = auditorium.auditorium_id AND showing.showing_id = ? AND seat.seat = ?",showingId, showingId, seatName);
//	  if(avail != null) {
//	    result.put("available", 0);
//	  }else {
//      result.put("available", 1);
//	  }
	  List<Seat> notAvail = this.notAvailableSeats(showingId);
	  result.put("available", 0);
	  for (Seat notAvailSeat : notAvail) {
      if(notAvailSeat.getSeat().equals(seatName)) {
        result.remove("available");
        result.put("available", 1);
      }
    }
	  
	  return result;
	}
	
	/**
	 * 根据showing_id获取所有可用的seat
	 * @param showingId
	 * @return
	 */
	public List<Seat> availableSeats(int showingId){
	  List<Booking> bookings = Booking.dao.find("select * from booking where showing_id = ?", showingId);
	  ArrayList<String> seats = new ArrayList<>();
	  for (Booking booking : bookings) {
	    String[] seats_id = booking.getSeatId().split(",");
      for (String string : seats_id) {
        seats.add(string);
      }
    }
	  String seats_str ="";
	  for (String seat : seats) {
	    seats_str += seats_str.equals("") ? seat : ("," + seat);
    }
	  if(seats_str.equals("")) {
	    seats_str = "0";
	  }
	    String sql = "SELECT seat.seat_id,seat.auditorium_id,seat.seat FROM seat, auditorium, showing WHERE seat.seat_id NOT IN ( ? ) AND seat.auditorium_id = auditorium.auditorium_id AND showing.auditorium_id = auditorium.auditorium_id AND showing.showing_id = ?";
	    List<Seat> result = Seat.dao.find(sql, seats_str, showingId);
	  
	  
	  return result;
	}  
	
  /**
   * 根据showing_id获取所有不可用的seat
   * @param showingId
   * @return
   */
  public List<Seat> notAvailableSeats(int showingId){
    List<Booking> bookings = Booking.dao.find("select * from booking where showing_id = ?", showingId);
    ArrayList<String> seats = new ArrayList<>();
    for (Booking booking : bookings) {
      String[] seats_id = booking.getSeatId().split(",");
      for (String string : seats_id) {
        seats.add(string);
      }
    }
    String seats_str ="";
    for (String seat : seats) {
      seats_str += seats_str.equals("") ? seat : ("," + seat);
    }
    if(seats_str.equals("")) {
      seats_str = "0";
    }
      String sql = "SELECT seat.seat_id,seat.auditorium_id,seat.seat FROM seat, auditorium, showing WHERE seat.seat_id IN ( ? ) AND seat.auditorium_id = auditorium.auditorium_id AND showing.auditorium_id = auditorium.auditorium_id AND showing.showing_id = ?";
      List<Seat> result = Seat.dao.find(sql, seats_str, showingId);
    
    
    return result;
  }
  
  /**
   * 获取所有位置，包含是否可用的信息
   * available = 1 为不可用，available = 0 为可用
   * @param showingId
   * @return
   */
  public ArrayList<Seat> seatWithState(int showingId){
    ArrayList<Seat> seats = new ArrayList<>();
    List<Seat> available = this.availableSeats(showingId);
    List<Seat> notAvailable = this.notAvailableSeats(showingId);
    if(available != null && !available.isEmpty()) {
      seats.addAll(available);
      for (Seat seat : seats) {
        seat.put("available", 0);
        System.out.println(seat);
      }
    }
    if(notAvailable != null && !notAvailable.isEmpty()) {
      seats.addAll(notAvailable);
      for (Seat seat : seats) {
        seat.put("available", 1);
        System.out.println(seat);
      }
    }
    if(seats.isEmpty()) {
      return null;
    }
    return seats;
  }
  
  public Seat getSeat(int seatId) {
    return Seat.dao.findById(seatId);
  }
  
  public List<Seat> getSeatsByAuditoriumId(String roomid){
	  return Seat.dao.find("select * from seat,auditorium where seat.auditorium_id = auditorium.auditorium_id and seat.auditorium_id = ?",Integer.parseInt(roomid));
  }
}
