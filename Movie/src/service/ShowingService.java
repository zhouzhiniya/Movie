package service;


import java.util.List;

import model.Showing;

public class ShowingService {
	//根据时间获取所有的放映厅及放映时间
	public List<Showing> getAllAuditoriums(String time,String city) {
		return Showing.dao.find("select theater.theater_id,show_time from showing,auditorium,theater where showing.auditorium_id = auditorium.auditorium_id and auditorium.theater_id = theater.theater_id and date_format(show_time,'%Y-%m-%d') = ? and city = ?",time,city); 
	}
}
