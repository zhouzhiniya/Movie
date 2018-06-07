package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import com.alibaba.druid.pool.vendor.SybaseExceptionSorter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.StrKit;

import model.Movie;

public class MovieAPIService {
  

  //豆瓣 API key
  final private String key_Douban = "0b2bdeda43b5688921839c8ecb20399b";
  //调用HTTP request
  private HTTPRequestService requester = new HTTPRequestService();
  
  
  //日期格式化
  SimpleDateFormat strDate = new SimpleDateFormat("yyyy-MM-dd"); //只有Date
  SimpleDateFormat strDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //Date + Time
  SimpleDateFormat strDate_noSpace = new SimpleDateFormat("yyyyMMdd"); //eg：20170210
  
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
    if(!response.isEmpty()) {
      return response.getJSONArray("subjects").getJSONObject(0).getString("id");    
    }else {
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
      JSONObject DoubanDetail = this.movieDetail_Douban(this.searchDoubanMovieID(MTimeDetail.getJSONObject("basic").getString("name")));
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
  
}
