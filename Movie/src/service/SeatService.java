package service;

import java.util.List;

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
}
