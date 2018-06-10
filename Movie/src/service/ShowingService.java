package service;


import java.util.Date;
import java.util.List;

import model.Showing;

public class ShowingService {
	//根据时间获取所有的放映厅及放映时间
	public List<Showing> getAllAuditoriums(String time,String city,String movie_id) {
		return Showing.dao.find("select theater.theater_id,show_time,showing_id from showing,auditorium,theater where showing.auditorium_id = auditorium.auditorium_id and auditorium.theater_id = theater.theater_id and date_format(show_time,'%Y-%m-%d') = ? and movie_id = ? and city = ?",time,Integer.parseInt(movie_id),city); 
	}
	
	//判断某天是否有排片
	public List<Showing> ifHasShowing(Date date,String theaterid){
		return Showing.dao.find("select showing.movie_id,show_time,title from showing,movie,auditorium where showing.movie_id = movie.movie_id and auditorium.auditorium_id = showing.auditorium_id and DATE(show_time) = ? and theater_id = ?",date,Integer.parseInt(theaterid));
	}
	
	public List<Showing> getShowingInfoByMovieId(Date date,String movie_id,String theaterid){
		return Showing.dao.find("select * from showing,movie,auditorium where showing.movie_id = movie.movie_id and auditorium.auditorium_id = showing.auditorium_id and DATE(show_time) = ? and theater_id = ? and movie.movie_id = ?",date,Integer.parseInt(theaterid),Integer.parseInt(movie_id));
	}
}
