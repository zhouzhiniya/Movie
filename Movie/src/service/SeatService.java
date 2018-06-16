package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Seat;

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
	  return result;
	}
	
	/**
	 * 根据showing_id获取所有可用的seat
	 * @param showingId
	 * @return
	 */
	public List<Seat> availableSeats(int showingId){
	  String sql = "select * from seat where seat.seat_id in ( select seat_id from booking where   booking.showing_id = ?)";
	  List<Seat> seats = Seat.dao.find(sql, showingId);
	  return seats;
	}  
	
  /**
   * 根据showing_id获取所有不可用的seat
   * @param showingId
   * @return
   */
  public List<Seat> notAvailableSeats(int showingId){
    String sql = "select * from seat where seat.seat_id not in ( select seat_id from booking where   booking.showing_id = ?)";
    List<Seat> seats = Seat.dao.find(sql, showingId);
    return seats;
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
      }
    }
    if(notAvailable != null && !notAvailable.isEmpty()) {
      seats.addAll(notAvailable);
      for (Seat seat : seats) {
        seat.put("available", 1);
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
	  return Seat.dao.find("select * from seat,auditorium where seat.auditorium_id = auditorium.auditorium_id and seat.auditorium_id = ",Integer.parseInt(roomid));
  }
}
