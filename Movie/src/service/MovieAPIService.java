package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.HanLP;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;

import model.DoubanComment;
import model.DoubanReviewTop250;
//import model.DobanComment;
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
   * 从豆瓣获取电影短评详情
   * @param DoubanMovieID
   * @return
   */
  private JSONObject movieComments100_Douban(String DoubanMovieID) {
    String url = "http://api.douban.com/v2/movie/subject/" + DoubanMovieID + "/comments?count=100&apikey=" + key_Douban;
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
  
  public Movie searchMovieByName(String movieTitle) {
    return Movie.dao.findFirst("select * from movie where movie.title = ?", movieTitle);
  }
  
  /**
   * 向数据库保存今日上映的电影，若电影已存在则修改其日期
   * @return
   */
  public boolean saveTodayMovies() {
    Calendar cal = Calendar.getInstance();
    ArrayList<Movie> todayMovies = this.getTodayMovies();
    for (Movie movie : todayMovies) {
      Movie existed = this.searchMovieByName(movie.getTitle());
      if(existed != null) {
        existed.setDate(cal.getTime());
        if(!existed.update()) {
          return false;
        }
      }else {
        if(!movie.save()) {
          return false;
        }
      }
    }
    return true;
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
      
      //竟然还有一些电影政策原因被删了所以查不到会报错
      if(DoubanDetail == null) {
        continue;
      }
      
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
      if(StrKit.notBlank(DoubanDetail.getString("mainland_pubdate"))) {
        try {
          movie.setReleaseDate(strDate_dashed.parse(DoubanDetail.getString("mainland_pubdate")));
        } catch (ParseException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
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
  
  public List<Movie> getAllTodayMovies(){
    Calendar cal = Calendar.getInstance();
    String today = strDate.format(cal.getTime());
    List<Movie> movies = Movie.dao.find("select * from movie where date = ?", today);
    return movies;
  }
  
  public boolean saveMovieComments100_Douban() {
    List<Movie> todayMovies = getAllTodayMovies();
    
    //清空表
    if(todayMovies != null) {
      Db.update("truncate table douban_comment"); 
    }
    
    //保存记录
    for (Movie todayMovie : todayMovies) {
      JSONObject DoubanComments = this.movieComments100_Douban(todayMovie.getDoubanId());
      String totalComment = "";
      
      //防止还有一些电影政策原因被删了所以查不到会报错
      if(DoubanComments == null) {
        continue;
      }
      
      for (Iterator iterator = DoubanComments.getJSONArray("comments").iterator(); iterator.hasNext();) {
        JSONObject sbject = (JSONObject) iterator.next();
        DoubanComment comment = new DoubanComment();
        comment.setMovieId(todayMovie.getMovieId());
        comment.setDoubanId(sbject.getString("subject_id"));
        comment.setRating(sbject.getJSONObject("rating").getInteger("value"));
        comment.setUsefulCount(sbject.getInteger("useful_count"));
        comment.setUserName(sbject.getJSONObject("author").getString("name"));
        comment.setAvatar(sbject.getJSONObject("author").getString("avatar"));
        comment.setContent(sbject.getString("content"));
        comment.setCommentId(sbject.getString("id"));
        
        //获取上映日期
        if(StrKit.notBlank(sbject.getString("created_at"))) {
          try {
            comment.setCreatedAt(strDateTime.parse(sbject.getString("created_at")));
          } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
        
        List<String> keywordList = HanLP.extractKeyword(sbject.getString("content"), 10);
        totalComment += sbject.getString("content");
        comment.setTags(String.join(",", keywordList));
        try {
          comment.save();
        } catch (Exception e) {
          continue;
        }
      }
      
      List<String> keywordList = HanLP.extractKeyword(totalComment, 100);
      Map<String, Integer> keywordMap = new HashMap<String, Integer>();
      int count = 0;
      for (Iterator iterator = keywordList.iterator(); iterator.hasNext()&&count<20;) {
	    String sbject = (String) iterator.next();
    	if (sbject.length()>1) {
          keywordMap.put(sbject, 0);
          ++count;
    	}
      }
      keywordMap.put("其他", 0);
      
      for (Iterator iterator = DoubanComments.getJSONArray("comments").iterator(); iterator.hasNext();) {
        JSONObject sbject = (JSONObject) iterator.next();
        String content =  sbject.getString("content");
        boolean flag = false;
        for (String entry : keywordMap.keySet()) {
          if (content.indexOf(entry)!=-1) {
            count = keywordMap.get(entry);
            keywordMap.put(entry, ++count);
            flag = true;
          }
        }
        if (!flag) {
          count = keywordMap.get("其他");
          keywordMap.put("其他", ++count);
        }
      }
      System.out.println(JSON.toJSON(keywordMap));
      try {
        Db.update("update movie set tag_json=? where movie_id=?", JSON.toJSON(keywordMap).toString(), todayMovie.getMovieId());
      } catch (Exception e) {
        // TODO: handle exception
        System.out.println("false");
      e.printStackTrace();
        continue;
      }
    }
    
    return true;
  }
  
  public List<MovieTop250> getTop250Movies(){
    List<MovieTop250> movies = MovieTop250.dao.find("select * from movie_top250");
    return movies;
  }
  
  public boolean saveTop250MovieComments100_Douban() {
    List<MovieTop250> topMovies = getTop250Movies();
    
    //保存记录
    for (MovieTop250 topMovie : topMovies) {
      JSONObject DoubanComments = this.movieComments100_Douban(topMovie.getDoubanId());
      String totalComment = "";
      
      //防止还有一些电影政策原因被删了所以查不到会报错
      if(DoubanComments == null) {
        continue;
      }
      
      for (Iterator iterator = DoubanComments.getJSONArray("comments").iterator(); iterator.hasNext();) {
        JSONObject sbject = (JSONObject) iterator.next();
        totalComment += sbject.getString("content");
      }
      
      List<String> keywordList = HanLP.extractKeyword(totalComment, 100);
      Map<String, Integer> keywordMap = new HashMap<String, Integer>();
      int count = 0;
      for (Iterator iterator = keywordList.iterator(); iterator.hasNext()&&count<20;) {
        String sbject = (String) iterator.next();
      	if (sbject.length()>1) {
          keywordMap.put(sbject, 0);
          ++count;
      	}
      }
      keywordMap.put("其他", 0);
      
      for (Iterator iterator = DoubanComments.getJSONArray("comments").iterator(); iterator.hasNext();) {
        JSONObject sbject = (JSONObject) iterator.next();
        String content =  sbject.getString("content");
        boolean flag = false;
        for (String entry : keywordMap.keySet()) {
          if (content.indexOf(entry)!=-1) {
            count = keywordMap.get(entry);
            keywordMap.put(entry, ++count);
            flag = true;
          }
        }
        if (!flag) {
          count = keywordMap.get("其他");
          keywordMap.put("其他", ++count);
        }
      }
      System.out.println(JSON.toJSON(keywordMap));
      try {
        Db.update("update movie_top250 set tag_json=? where id=?", JSON.toJSON(keywordMap), topMovie.getId());
      } catch (Exception e) {
        // TODO: handle exception
        System.out.println("false");
        continue;
      }
    }
    
    return true;
  }
  
  /**
   * 向数据库存储top 250电影的影片（review，每个电影只存一个）
   * （可能）用作推荐，其他勿用
   * 如遇到报错：“Incorrect string value: '\xE9\x81\x93\xE8\x8E\xB1...' for column 'content' at row 1”
   * 请在Navicat右键单击数据库，打开命令行，输入 ALTER TABLE 【douban_review_top250】 CONVERT TO CHARACTER SET utf8mb4;
   * @return
   */
  public boolean saveTop250Reviews() {
    List<MovieTop250> movies = MovieTop250.dao.find("select id,douban_id from movie_top250");
    for (MovieTop250 movie : movies) {
      String url = "https://api.douban.com/v2/movie/subject/" + movie.getDoubanId() + "/reviews?apikey=" + key_Douban + "&count=1";
      JSONObject responce = requester.doGet(url);
      //竟然还有一些电影政策原因被删了所以查不到会报错
      if(responce == null) {
        continue;
      }
      String reviewContent = responce.getJSONArray("reviews").getJSONObject(0).getString("content");
      DoubanReviewTop250 newReview = new DoubanReviewTop250();
      newReview.setMovieId(movie.getId());
      newReview.setContent(reviewContent);
      newReview.save();
    }
    return true;
  }
}
