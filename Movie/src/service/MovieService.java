package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import model.Movie;

/**
 * 这个类是用来【从数据库里】获取电影信息的，Movie表会每天自动更新，一般情况下电影数据只需从此类中获取
 * @author lja67
 *
 */
public class MovieService {

  
  //日期格式化
  SimpleDateFormat strDate = new SimpleDateFormat("yyyy-MM-dd"); //只有Date
  SimpleDateFormat strDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //Date + Time
  
  /**
   * 从数据库获取今日上映电影列表及详情
   * @return List<Movie>
   */
  public List<Movie> getTodayMovies(){
    Calendar cal = Calendar.getInstance();
    String today = strDate.format(cal.getTime());
    List<Movie> movies = Movie.dao.find("select * from movie where date = ?", today);
    return movies;
  }
  
  //获取评分由高到低的电影列表
  public List<Movie> getHigherGradeMovies() {
	Calendar cal = Calendar.getInstance();
    String today = strDate.format(cal.getTime());
    List<Movie> movies = Movie.dao.find("select * from movie where date = ? order by douban_rating desc", today);
    return movies;
  }
  
//获取上映时间由近到远的电影列表
  public List<Movie> getRecentMovies() {
	Calendar cal = Calendar.getInstance();
    String today = strDate.format(cal.getTime());
    List<Movie> movies = Movie.dao.find("select * from movie where date = ? order by release_date desc", today);
    return movies;
  }
  
  //根据电影id获取电影详情
  public Movie getMovieInfoById(String id) {
	  return Movie.dao.findById("select * from movie where movie_id = ?", Integer.parseInt(id));
  }
  
  
  
  
}
