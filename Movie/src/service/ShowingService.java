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
	public List<Showing> ifHasShowing(String date,String theaterid){
		String sql = ("select showing.movie_id,show_time,title from showing,movie,auditorium where showing.movie_id = movie.movie_id and auditorium.auditorium_id = showing.auditorium_id and DATE(show_time) = '"+date+"' and theater_id = "+theaterid);
		return Showing.dao.find(sql);
	}
	
	public List<Showing> getShowingInfoByMovieId(String date,String movie_id,String theaterid){
		String sql = ("select * from showing,movie,auditorium where showing.movie_id = movie.movie_id and auditorium.auditorium_id = showing.auditorium_id and DATE(show_time) = '"+date+"' and theater_id = "+theaterid+" and movie.movie_id = "+movie_id);
		return Showing.dao.find(sql);
	}
}
