package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;

import model.DobanComment;
import model.Movie;
import model.MovieTop250;

/**
 * 这个类用来获取电影 API 数据，包括时光网和豆瓣电影的数据
 * 如果只需要获取今日电影详情，【不要使用】这个类！
 * 请使用 MovieService 类中的 getTodayMovies()
 * @author Jiaan LIU
 *
 */
public class MovieAPIService {
  

  //豆瓣 API key
  final private String key_Douban = "0b2bdeda43b5688921839c8ecb20399b";
  //调用HTTP request
  private HTTPRequestService requester = new HTTPRequestService();
  
  
  //日期格式化
  SimpleDateFormat strDate = new SimpleDateFormat("yyyy-MM-dd"); //只有Date
  SimpleDateFormat strDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //Date + Time
  SimpleDateFormat strDate_noSpace = new SimpleDateFormat("yyyyMMdd"); //eg：20170210
  SimpleDateFormat strDate_dashed = new SimpleDateFormat("yyyy-MM-dd"); //eg：2017-02-10
  
  /**
   * 从时光网获取今日所有上映电影的时光网movie_id
   * @return
   */
  private ArrayList<String> todayMoviesMTimeID() {
    String url = "https://api-m.mtime.cn/Showtime/LocationMovies.api?locationId=561";
    JSONObject response = requester.doGet(url);
    JSONArray movies = response.getJSONArray("ms");
    ArrayList<String> result = new ArrayList<>();
    for(int i = 0; i < movies.size(); i++) {
      result.add(movies.getJSONObject(i).getString("id"));
    }
    return result;
  }
  
  /**
   * 从时光网获取电影详情
   * @param MTimeMovieID
   * @return
   */
  private JSONObject movieDetail_MTime(String MTimeMovieID) {
    String url = "https://ticket-api-m.mtime.cn/movie/detail.api?locationId=561&movieId=" + MTimeMovieID;
    JSONObject response = requester.doGet(url).getJSONObject("data");
    return response;
  }
  
  /**
   * 从豆瓣获取电影详情
   * @param DoubanMovieID
   * @return
   */
  private JSONObject movieDetail_Douban(String DoubanMovieID) {
    String url = "http://api.douban.com/v2/movie/subject/" + DoubanMovieID + "?apikey=" + key_Douban;
    JSONObject response = requester.doGet(url);
    return response;    
  }
  
  /**
   * 根据电影名称搜索其豆瓣movie_id
   * @param movieTitle
   * @return
   */
  private String searchDoubanMovieID(String movieTitle) {
    String url = "http://api.douban.com/v2/movie/search?count=1&q=" + movieTitle;
    JSONObject response = requester.doGet(url);
    if(response.getInteger("total") >= 0) {
      return response.getJSONArray("subjects").getJSONObject(0).getString("id");    
    }else {
      System.out.println("\"" + movieTitle + "\"在豆瓣电影的查询结果为空！");
      return null;
    }
  }
  
  /**
   * 从时光网获取今日所有上映电影，整合时光网、豆瓣电影信息，返回包含每个电影详细model的列表
   * @return ArrayList<Movie>
   */
  public ArrayList<Movie> getTodayMovies(){
    Calendar cal = Calendar.getInstance();
    
    ArrayList<String> moviesMTimeID = this.todayMoviesMTimeID(); //今日所有电影的时光网movie_id
    ArrayList<JSONObject> details_MTime = new ArrayList<>(); //所有电影的时光网详情
    ArrayList<Movie> result = new ArrayList<>();
    
    //根据id添加详情
    for (String MTimeID : moviesMTimeID) {
      details_MTime.add(this.movieDetail_MTime(MTimeID));
    }
    
    //为每个电影创建Movie对象
    for (JSONObject MTimeDetail : details_MTime) {
      String DoubanMovieID = this.searchDoubanMovieID(MTimeDetail.getJSONObject("basic").getString("name"));
      if(StrKit.isBlank(DoubanMovieID))
        continue; //应急用：根据名称查不到豆瓣movie_id的直接跳过
      JSONObject DoubanDetail = this.movieDetail_Douban(DoubanMovieID);
      
      Movie movie = new Movie();
      movie.setDate(cal.getTime());
      movie.setTitle(MTimeDetail.getJSONObject("basic").getString("name"));
      movie.setTitleEn(MTimeDetail.getJSONObject("basic").getString("nameEn"));
      movie.setMtimeId(MTimeDetail.getJSONObject("basic").getString("movieId"));
      movie.setDoubanId(DoubanDetail.getString("id"));
      movie.setDuration(MTimeDetail.getJSONObject("basic").getString("mins"));
      movie.setDirector(MTimeDetail.getJSONObject("basic").getJSONObject("director").getString("name")); 
      
      //拼接演员字符串
      String actors = "";
      for (Iterator iterator = MTimeDetail.getJSONObject("basic").getJSONArray("actors").iterator(); iterator.hasNext();) { 
        JSONObject actor = (JSONObject) iterator.next();
        if(StrKit.isBlank(actors)) {
          actors += actor.getString("name");
        }else {
          actors = actors + "," + actor.getString("name");
        }
      }
      movie.setActors(actors);
     
      //拼接电影类型字符串
      String types = "";
      for (Iterator iterator = MTimeDetail.getJSONObject("basic").getJSONArray("type").iterator(); iterator.hasNext();) { 
        String type = (String) iterator.next();
        if(StrKit.isBlank(types)) {
          types += type;
        }else {
          types = types + "," + type;
        }
      }
      movie.setType(types);
      
      movie.setImage(MTimeDetail.getJSONObject("basic").getString("img"));
      movie.setVideo(MTimeDetail.getJSONObject("basic").getJSONObject("video").getString("url"));
      try {
        movie.setReleaseDate(strDate_noSpace.parse(MTimeDetail.getJSONObject("basic").getString("releaseDate")));
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      movie.setDoubanRating(DoubanDetail.getJSONObject("rating").getFloat("average"));
      movie.setWishCount(DoubanDetail.getInteger("wish_count"));
      movie.setCollectCount(DoubanDetail.getInteger("collect_count"));
      movie.setReviewsCount(DoubanDetail.getInteger("reviews_count"));
      movie.setCommentsCount(DoubanDetail.getInteger("comments_count"));
      movie.setSummary(DoubanDetail.getString("summary"));
      movie.setTotalBox(MTimeDetail.getJSONObject("boxOffice").getLong("totalBox"));
      
      result.add(movie);
      
    }//end For

    
    return result;
  }
  
  /**
   * 向数据库保存今日上映的电影
   * @return
   */
  public boolean saveTodayMovies() {
    ArrayList<Movie> todayMovies = this.getTodayMovies();
    boolean saved = false;
    for (Movie movie : todayMovies) {
      saved = movie.save();
    }
    if(saved) {
      return true;
    }
    return false;
  }
  
  /**
   * 向数据库保存豆瓣电影Top250，用一次就行了
   * @return boolean
   */
  public boolean saveTop250() {
    ArrayList<String> top250IDs = new ArrayList<>();

    //添加Top250的全部豆瓣movie_id
    for(int i = 0; i < 260; i += 20) {
      String url = "http://api.douban.com/v2/movie/top250?start=" + i;
      JSONObject response = requester.doGet(url);
      JSONArray subjects = response.getJSONArray("subjects");
      //TODO 判断是否还有
      //添加当前页面所有电影的豆瓣movie_id
      for (Iterator iterator = subjects.iterator(); iterator.hasNext();) { 
        JSONObject sbject = (JSONObject) iterator.next();
        String doubanID = sbject.getString("id");
        top250IDs.add(doubanID);
      }
    }
    
    //保存记录
    for (String doubanID : top250IDs) {
      JSONObject DoubanDetail = this.movieDetail_Douban(doubanID);
      
      MovieTop250 movie = new MovieTop250();
      movie.setTitle(DoubanDetail.getString("title"));
//      movie.setTitleEn(MTimeDetail.getJSONObject("basic").getString("nameEn"));
      movie.setDoubanId(doubanID);
      
      //获取电影时长
      if(DoubanDetail.getJSONArray("durations").size() >= 0) {
        movie.setDuration(DoubanDetail.getJSONArray("durations").getString(0));
      }
      movie.setDirector(DoubanDetail.getJSONArray("directors").getJSONObject(0).getString("name")); 
      
      //拼接演员字符串
      String actors = "";
      for (Iterator iterator = DoubanDetail.getJSONArray("casts").iterator(); iterator.hasNext();) { 
        JSONObject actor = (JSONObject) iterator.next();
        if(StrKit.isBlank(actors)) {
          actors += actor.getString("name");
        }else {
          actors = actors + "," + actor.getString("name");
        }
      }
      movie.setActors(actors);
     
      //拼接电影类型字符串
      String types = "";
      for (Iterator iterator = DoubanDetail.getJSONArray("genres").iterator(); iterator.hasNext();) { 
        String type = (String) iterator.next();
        if(StrKit.isBlank(types)) {
          types += type;
        }else {
          types = types + "," + type;
        }
      }
      movie.setType(types);
      
      movie.setImage(DoubanDetail.getJSONObject("images").getString("large"));
      
      //获取video URL
      if(DoubanDetail.getJSONArray("videos").size() > 0) {
        movie.setVideo(DoubanDetail.getJSONArray("videos").getJSONObject(0).getString("sample_link"));
      }
      
      //获取上映日期
       try {
        movie.setReleaseDate(strDate_dashed.parse(DoubanDetail.getString("mainland_pubdate")));
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
       
      movie.setDoubanRating(DoubanDetail.getJSONObject("rating").getFloat("average"));
      movie.setWishCount(DoubanDetail.getInteger("wish_count"));
      movie.setCollectCount(DoubanDetail.getInteger("collect_count"));
      movie.setReviewsCount(DoubanDetail.getInteger("reviews_count"));
      movie.setCommentsCount(DoubanDetail.getInteger("comments_count"));
      movie.setSummary(DoubanDetail.getString("summary"));
//      movie.setTotalBox(MTimeDetail.getJSONObject("boxOffice").getLong("totalBox"));
      movie.save();
    }
    
    return true;
  }
  
  public ArrayList<DobanComment> getMovieContents() {
    Calendar cal = Calendar.getInstance();
    
    ArrayList<String> moviesMTimeID = this.todayMoviesMTimeID(); //今日所有电影的时光网movie_id
    ArrayList<JSONObject> details_MTime = new ArrayList<>(); //所有电影的时光网详情
    ArrayList<Movie> result = new ArrayList<>();
    
    //根据id添加详情
    for (String MTimeID : moviesMTimeID) {
      details_MTime.add(this.movieDetail_MTime(MTimeID));
    }
    
    //为每个电影创建Movie对象
    for (JSONObject MTimeDetail : details_MTime) {
      String DoubanMovieID = this.searchDoubanMovieID(MTimeDetail.getJSONObject("basic").getString("name"));
      if(StrKit.isBlank(DoubanMovieID))
        continue; //应急用：根据名称查不到豆瓣movie_id的直接跳过
      JSONObject DoubanDetail = this.movieDetail_Douban(DoubanMovieID);
      
      Movie movie = new Movie();
      movie.setDate(cal.getTime());
      movie.setTitle(MTimeDetail.getJSONObject("basic").getString("name"));
      movie.setTitleEn(MTimeDetail.getJSONObject("basic").getString("nameEn"));
      movie.setMtimeId(MTimeDetail.getJSONObject("basic").getString("movieId"));
      movie.setDoubanId(DoubanDetail.getString("id"));
      movie.setDuration(MTimeDetail.getJSONObject("basic").getString("mins"));
      movie.setDirector(MTimeDetail.getJSONObject("basic").getJSONObject("director").getString("name")); 
      
      //拼接演员字符串
      String actors = "";
      for (Iterator iterator = MTimeDetail.getJSONObject("basic").getJSONArray("actors").iterator(); iterator.hasNext();) { 
        JSONObject actor = (JSONObject) iterator.next();
        if(StrKit.isBlank(actors)) {
          actors += actor.getString("name");
        }else {
          actors = actors + "," + actor.getString("name");
        }
      }
      movie.setActors(actors);
     
      //拼接电影类型字符串
      String types = "";
      for (Iterator iterator = MTimeDetail.getJSONObject("basic").getJSONArray("type").iterator(); iterator.hasNext();) { 
        String type = (String) iterator.next();
        if(StrKit.isBlank(types)) {
          types += type;
        }else {
          types = types + "," + type;
        }
      }
      movie.setType(types);
      
      movie.setImage(MTimeDetail.getJSONObject("basic").getString("img"));
      movie.setVideo(MTimeDetail.getJSONObject("basic").getJSONObject("video").getString("url"));
      try {
        movie.setReleaseDate(strDate_noSpace.parse(MTimeDetail.getJSONObject("basic").getString("releaseDate")));
      } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      movie.setDoubanRating(DoubanDetail.getJSONObject("rating").getFloat("average"));
      movie.setWishCount(DoubanDetail.getInteger("wish_count"));
      movie.setCollectCount(DoubanDetail.getInteger("collect_count"));
      movie.setReviewsCount(DoubanDetail.getInteger("reviews_count"));
      movie.setCommentsCount(DoubanDetail.getInteger("comments_count"));
      movie.setSummary(DoubanDetail.getString("summary"));
      movie.setTotalBox(MTimeDetail.getJSONObject("boxOffice").getLong("totalBox"));
      
      result.add(movie);
      
    }//end For

    
    return result;
  }
//  public 
}
