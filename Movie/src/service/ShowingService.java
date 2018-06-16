package service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Movie;
import model.Showing;
import model.Theater;

public class ShowingService {
  
  SimpleDateFormat strDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //Date + Time
  
	//根据时间获取所有的放映厅及放映时间
	public List<Showing> getAllAuditoriums(String time,String city,String movie_id) {
		String sql = "select theater.theater_id,theater,show_time,showing_id from showing,auditorium,theater where showing.auditorium_id = auditorium.auditorium_id and auditorium.theater_id = theater.theater_id and Date(show_time) = '"+time+"' and movie_id = "+Integer.parseInt(movie_id)+" and city = '"+city+"'";
		System.out.println(sql);
		return Showing.dao.find(sql); 
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
	
	private int getTheaterIdByShowingId(int showingId) {
	  Theater theater = Theater.dao.findFirst("select theater.theater_id from showing,auditorium,theater where showing.auditorium_id = auditorium.auditorium_id   AND auditorium.theater_id = theater.theater_id   AND showing_id = ?", showingId);
	  return theater.getTheaterId();
	}
	
	public HashMap<String, ArrayList<Showing>> getShowingsOfTheaters(int movieId, String city){
	  HashMap<Integer, ArrayList<Showing>> map = new HashMap<>();
//	  sql_theater = "select theater, theater.theater_id from showing,auditorium,movie,theater where showing.movie_id = movie.movie_id   AND showing.auditorium_id = auditorium.auditorium_id   AND auditorium.theater_id = theater.theater_id   AND theater.city = ?   AND showing.movie_id = ?";
	  ArrayList<Showing> showings = (ArrayList<Showing>)Showing.dao.find("select showing.showing_id,movie.title,showing.show_time from showing,movie,auditorium,theater where showing.movie_id = movie.movie_id   AND showing.auditorium_id = auditorium.auditorium_id   AND auditorium.theater_id = theater.theater_id   AND showing.movie_id = ?   AND theater.city = ?", movieId, city);;
	  for (Showing showing : showings) {
      int theaterId = this.getTheaterIdByShowingId(showing.getShowingId());
      if(!map.containsKey(theaterId)) {
        ArrayList<Showing> temp = new ArrayList<>();
        temp.add(showing);
        map.put(theaterId, temp);
      }else {
        ArrayList<Showing> temp = map.get(theaterId);
        temp.add(showing);
        map.put(theaterId, temp);
      }
    }
	  
	  HashMap<String, ArrayList<Showing>> result = new HashMap<>();
	  for (HashMap.Entry<Integer, ArrayList<Showing>> keyValue : map.entrySet()) {
      String newTheater = Theater.dao.findById(keyValue.getKey()).getTheater();
      result.put(newTheater, keyValue.getValue());
    }
	  
	  return result;
	}
	
	
	public boolean showTimeAvailable(String time, int auditoriumId) {
	  List<Showing> find = Showing.dao.find("select * from showing where auditorium_id=? AND show_time <= ? AND end_time >= ?",auditoriumId, time, time);
	  if(find != null && !find.isEmpty()) {
//	    System.out.println(Showing.dao.find("select * from showing where auditorium_id=? AND show_time <= ? AND end_time >= ?",auditoriumId, time, time));
	    return false;
	  }
	  return true;
	}
	
	public Date calEndTime(int movie_id, String showTime) {
	  try {
      Date time = strDateTime.parse(showTime);
      Movie movie = Movie.dao.findById(movie_id);
      String duration = movie.getDuration();
      String regex = "[^0-9]";
      Matcher m = Pattern.compile(regex).matcher(duration);
      duration = duration.replaceAll("\\D", "");
      System.out.println(duration);
      Calendar cal = Calendar.getInstance();
      cal.setTime(time);
      cal.add(Calendar.MINUTE, Integer.parseInt(duration));
      return cal.getTime();
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
	}
	
	//新增排片
	public Showing addShowing(String movie_id,String show_time,String auditorium_id,String price) {
	  if(!this.showTimeAvailable(show_time, Integer.parseInt(auditorium_id))) {
	    return null;
	  }
	  Showing showing = new Showing();
	  showing.set("movie_id", Integer.parseInt(movie_id));
	  showing.set("show_time", show_time);
	  showing.set("auditorium_id", Integer.parseInt(auditorium_id));
	  showing.set("price", price);
	  showing.setEndTime(this.calEndTime(Integer.parseInt(movie_id), show_time));
	  showing.save();
	  return showing;
	}

}
