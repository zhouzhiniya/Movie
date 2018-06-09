package service;

import model.Seat;

public class SeatService {
	//根据showing_id获取放映厅的座位
	public Seat getSeatsById(String showingid) {
		return Seat.dao.findFirst("select * from seat,showing where seat.auditorium_id = showing.auditorium_id and showing_id = ?",Integer.parseInt(showingid));
	}
}
