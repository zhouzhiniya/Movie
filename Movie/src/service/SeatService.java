package service;

import java.util.List;

import model.Seat;

public class SeatService {
  
	//根据showing_id获取放映厅的座位
	public List<Seat> getSeatsByShowingId(String showingid) {
		return Seat.dao.find("select * from seat,showing where seat.auditorium_id = showing.auditorium_id and showing_id = ?",Integer.parseInt(showingid));
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
	
	/**
	 * 根据 seat_id 查询座位是否可用（0为可预订，1为已经被预订），查不到座位也返回false
	 * @param seatId
	 * @return boolean
	 */
	public boolean isSeatAvailable(String seatId) {
	  try {
	    if(Seat.dao.findById(seatId).getSeatState() == 0) {
	      return true;
	    }
      return false;
    } catch (Exception e) {
      return false;
    }
	}
	
	/**
	 * 用于预订座位后锁定座位，将state设置为1
	 * @param seatId
	 * @return boolean
	 */
	public boolean lockSeat(String seatId) {
	  //座位已被预订或id有误
	  if(this.isSeatAvailable(seatId) == false)
	    return false;
	  
	  Seat seat = Seat.dao.findById(seatId);
	  seat.setSeatState(1);
	  return seat.save();
	}
	
	public Seat getSeatByNameAndShowing(String seatName, int showingId) {
	  Seat result = Seat.dao.findFirst("select * from seat,showing where seat.seat_id = showing.seat_id AND showing.showing_id=? AND seat.seat=?", showingId, seatName);
	  return result;
	}
}
