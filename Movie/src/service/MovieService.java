package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Booking;
import model.DoubanComment;
import model.Movie;
import model.MovieTop250;
import model.Recommendation;
import model.Showing;

/**
 * 这个类是用来【从数据库里】获取电影信息的，Movie表会每天自动更新，一般情况下电影数据只需从此类中获取
 * @author Jiaan LIU
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
	  return Movie.dao.findById(Integer.parseInt(id));
  }
  
  //根据搜索条件搜索电影
  public List<Movie> searchMovie(String keyword,String column) {
	  Calendar cal = Calendar.getInstance();
	  String today = strDate.format(cal.getTime());
	  String sql = "select * from movie where date = '"+today+"' and "+column+" like '%"+keyword+"%'";
	  System.out.println(sql);
	  return Movie.dao.find(sql);
  }
  
  
  
  /**
   * 根据豆瓣 movie_id 获取对应电影的所有豆瓣评论
   * @param DoubanMovieID
   * @return
   */
  public List<DoubanComment> movieComments_Douban(String DoubanMovieID) {
    List<DoubanComment> result = DoubanComment.dao.find("select * from douban_comment where movie_id = ?", DoubanMovieID);
    return result;
  }
  
  
  //统计观影记录中出现次数最多的类型
  public List<Movie> getRecommendMovies(Object array[])
  {
    Set<Object> s = new HashSet<Object>();//HashSet用来去掉重复
    for (Object o : array){
      s.add(o);
      } //现在的集合s中无重复的包含array中的所有元素
    Object[] obj = s.toArray();//把集合s中的元素存入数组obj2中
    int[] n = new int[obj.length];//这个数组用来存放每一个元素出现的次数
    int max = 0;
    for (int i = 0; i < obj.length; i++)
    {
      int cout = 0;
      for (int j = 0; j < array.length; j++)
      {
        if (obj[i].equals(array[j]))
          cout++;//用obj中的元素跟array中的每一个比较，如果相同cout自增
      }
      n[i] = cout;//每一个元素出现的次数存入数组n,数组n的下标i跟数组obj的下标是一一对应的。
        if (max < cout)
        {//得到元素出现次数最多是多少次
          max = cout;
        }
    }
    List<Movie> best = null;
    for (int i = 0; i < n.length; i++)
    {
      if (max == n[i])
      {
        //如果出现的次数等于最大次数，就输出对应数组obj中的元素
        best = (List<Movie>) obj[i];
        }
    }
    return best;
  }
  

  //根据类型在top250搜索电影
  public List<Movie> searchMovieTop(Object[] keyword,String column) {
    Calendar cal = Calendar.getInstance();
    String today = strDate.format(cal.getTime());
    ArrayList<Movie> result = new ArrayList<>();
    for (Object object : keyword) {
      String sql = "select * from movie where date = '"+today+"' and "+column+" like '%" + object + "%'";
      List<Movie> movies = Movie.dao.find(sql);
      for (Movie movie : movies) {
        result.add(movie);
      }
    }
//    System.out.println(sql);
    return result;
  }
  
  /**
   * 根据用户id推荐电影
   * @param userId
   * @return
   */
  public ArrayList<MovieTop250> recommendByUserId(int userId){
    ArrayList<MovieTop250> result = new ArrayList<>();
    ArrayList<MovieTop250> matchMovies = this.getExoMoviesByUserId(userId);
    //TODO
    return result;
  }
  
  
  public ArrayList<MovieTop250> getExoMoviesByUserId(int userId) {
    List<Booking> userBookings = Booking.dao.find("select * from booking where user_id=?", userId);
    ArrayList<String> userFaTypeses = new ArrayList<>();
    for (Booking booking : userBookings) {
      Showing showing = Showing.dao.findById(booking.getShowingId());
      Movie moive = Movie.dao.findById(showing.getMovieId());
      userFaTypeses.add(moive.getType());
    }
    ArrayList<String> userFaTypes = new ArrayList<>();
    for (String str : userFaTypeses) {
      System.out.println(str);
      String[] types = str.split(",");
      for (String string : types) {
        userFaTypes.add(string);
      }
    }
    ArrayList<MovieTop250> relaMovs = new ArrayList<>();
    for (String type : userFaTypes) {
//      String sql = "select * from movie_top250 where type like %?%" ;
      System.out.println(type);
      List<MovieTop250> match = MovieTop250.dao.find("select * from movie_top250 where type like %?%", type);
      relaMovs.addAll(match);
    }
    for (MovieTop250 movieTop250 : relaMovs) {
      System.out.println(movieTop250);
    }
    return null;
  }
  
  
  //根据订票记录筛选电影类型
  public List<Movie> getMovieType(Integer uid) 
  {
    return Movie.dao.find("select type from movie inner join showing on movie.movie_id = showing.movie_id inner join booking on showing.showing_id = booking.showing_id where user_id= ?", uid);
  }


  public List<MovieTop250> getAllMatchesMovie(int userId){
    String sql = "select * from movie_top250 where   (SUBSTRING_INDEX(type, ',', 1) in (select SUBSTRING_INDEX(type, ',', 1) AS type1 from booking,showing,movie where booking.showing_id = showing.showing_id AND showing.movie_id = movie.movie_id AND booking.user_id = 1)   or   SUBSTRING_INDEX(type, ',', 1) in (select SUBSTRING_INDEX(SUBSTRING_INDEX(type, ',', 2), ',', -1) AS type2 from booking,showing,movie where booking.showing_id = showing.showing_id AND showing.movie_id = movie.movie_id AND booking.user_id = 1))     AND    (SUBSTRING_INDEX(SUBSTRING_INDEX(type, ',', 2), ',', -1) in (select SUBSTRING_INDEX(type, ',', 1) AS type1 from booking,showing,movie where booking.showing_id = showing.showing_id AND showing.movie_id = movie.movie_id AND booking.user_id = 1)   OR   SUBSTRING_INDEX(SUBSTRING_INDEX(type, ',', 2), ',', -1) in (select SUBSTRING_INDEX(SUBSTRING_INDEX(type, ',', 2), ',', -1) AS type2 from booking,showing,movie where booking.showing_id = showing.showing_id AND showing.movie_id = movie.movie_id AND booking.user_id = 1))";
    List<MovieTop250> matches = MovieTop250.dao.find(sql);
    
    ArrayList<MovieTop250> result = new ArrayList<>();
    result.addAll(matches);
    result.sort(new Comparator<MovieTop250>() {  
      @Override  
      public int compare(MovieTop250 o1, MovieTop250 o2) {  
          // TODO Auto-generated method stub  
          if(o1.getDoubanRating() > o2.getDoubanRating())  
              return 1;  
          if(o1.getDoubanRating() == o2.getDoubanRating()) {
            if(o1.getWishCount() > o2.getWishCount()) {
              return 1;
            }
            return -1;
          }
          else return -1;  
      }  
    });
    
    return result;
  }
  
  public boolean generateRecommendation(int userId) {
    List<Recommendation> records = Recommendation.dao.find("select * from recommendation where user_id=?", userId);
    for (Recommendation recommendation : records) {
      recommendation.delete();
    }
    List<MovieTop250> result = this.getAllMatchesMovie(userId);
    for (MovieTop250 movie : result) {
      Recommendation newRecomm = new Recommendation();
      newRecomm.setUserId(userId);
      newRecomm.setTop250Id(movie.getId());
      newRecomm.save();
    }
    return true;
  }
  
  public ArrayList<MovieTop250> getRecommendationsByUserId(int userId){
    List<Recommendation> records = Recommendation.dao.find("select * from recommendation where user_id=?", userId);
    ArrayList<MovieTop250> movies = new ArrayList<MovieTop250>();
    for (Recommendation record : records) {
      MovieTop250 newMovie = MovieTop250.dao.findFirst("select * from movie_top250 where id=?",record.getTop250Id());
      if(newMovie != null) {
        movies.add(newMovie);
      }
    }
    return movies;
  }
}
