package service;

import java.util.List;

import model.Auditorium;
import model.Seat;
import model.Showing;
import model.Theater;

public class TheaterService {
	//新增影厅
	public boolean addAuditorium(String name,String seats,String theaterid) {
		Auditorium auditorium = new Auditorium();
		auditorium.set("auditorium", name);
		auditorium.set("theater_id", theaterid);
		boolean ifAddAuditorium = auditorium.save();
		if(ifAddAuditorium) {
			int auditoriumId = auditorium.getAuditoriumId();
			String[] allSeats = seats.split(",");
			for(int i=0; i<allSeats.length; i++) {
				Seat seat = new Seat();
				seat.set("seat", allSeats[i]);
				seat.set("auditorium_id", auditoriumId);
				seat.set("seat_state", 0);
				seat.save();
				if(!seat.save()) {
					return false;
				}
			}
			return true;
		}else {
			return ifAddAuditorium;
		}
	}
	
	//新增排片
	public boolean addShowing(String movie_id,String show_time,String auditorium_id,String price) {
		Showing showing = new Showing();
		showing.set("movie_id", Integer.parseInt(movie_id));
		showing.set("show_time", show_time);
		showing.set("auditorium_id", Integer.parseInt(auditorium_id));
		showing.set("price", price);
		return showing.save();
	}
	
	//获取所有 影院所在的城市
	public List<Theater> getAllCity() {
		return Theater.dao.find("select city from theater group by city");
	}
}
