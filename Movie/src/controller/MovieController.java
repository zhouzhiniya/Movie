package controller;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import kit.BaseResponse;
import kit.ResultCodeEnum;
import model.Comment;
import model.DoubanComment;
import model.Movie;
import model.MovieTop250;
import model.Recommendation;
import service.MovieCommentService;
import service.MovieService;

public class MovieController extends Controller
{
	MovieService movieService = new MovieService();
	MovieCommentService commServ = new MovieCommentService();
	
	//获取所有的电影信息
	public void getAllMovies()
	{
		BaseResponse baseResponse = new BaseResponse();
		
		try {
			List<Movie> movieList = movieService.getTodayMovies();
			baseResponse.setData(movieList);
			baseResponse.setResult(ResultCodeEnum.SUCCESS);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseResponse.setResult(ResultCodeEnum.FAILED);
		}
		
		this.renderJson(baseResponse);
	}
	
	//获取评分最高的6部电影
	public void getHigherGradeMovies()
	{
		BaseResponse baseResponse = new BaseResponse();
		
		try {
			List<Movie> movieList = movieService.getHigherGradeMovies();
			List<Movie> finalMovies = new ArrayList<Movie>();
			if(movieList.size()>=6){
				for(int i=0; i<6; i++) {
					finalMovies.add(movieList.get(i));
				}
				baseResponse.setData(finalMovies);
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}else {
				baseResponse.setData(movieList);
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseResponse.setResult(ResultCodeEnum.FAILED);
		}
		
		this.renderJson(baseResponse);
	}
	
	//获取上映时间最近的3部电影
	public void getRecentMovies()
	{
		BaseResponse baseResponse = new BaseResponse();
		
		try {
			List<Movie> movieList = movieService.getRecentMovies();
			List<Movie> finalMovies = new ArrayList<Movie>();
			if(movieList.size()>=3){
				for(int i=0; i<3; i++) {
					finalMovies.add(movieList.get(i));
				}
				baseResponse.setData(finalMovies);
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}else {
				baseResponse.setData(movieList);
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseResponse.setResult(ResultCodeEnum.FAILED);
		}
		
		this.renderJson(baseResponse);
	}
	
	//根据电影id获取电影的详细信息
	public void getMovieInfoById()
	{
		BaseResponse baseResponse = new BaseResponse();
		
		try {
			String movieId = this.getPara("movie_id");
			if(StrKit.isBlank(movieId)) {
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}else {
				Movie movie = movieService.getMovieInfoById(movieId);
				if(movie != null) {
					baseResponse.setData(movie);
					baseResponse.setResult(ResultCodeEnum.SUCCESS);
				}else {
					baseResponse.setResult(ResultCodeEnum.FAILED);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseResponse.setResult(ResultCodeEnum.FAILED);
		}
		
		this.renderJson(baseResponse);
	}
	
	//搜索电影类型、演员、导演对应的电影
	public void searchMovie()
	{
		BaseResponse baseResponse = new BaseResponse();
		
		try {
			String movieType = this.getPara("movie_type");
			String actor = this.getPara("actor");
			String director = this.getPara("director");
			String title = this.getPara("title");
			if(StrKit.isBlank(movieType) && StrKit.isBlank(actor) && StrKit.isBlank(director) && StrKit.isBlank(title)) {
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}else {
				List<Movie> movie = new ArrayList<Movie>();
				if(!StrKit.isBlank(movieType)) {
					//根据电影类型搜索
					movie = movieService.searchMovie(movieType,"type");
				}else if(!StrKit.isBlank(actor)) {
					//根据演员搜索
					movie = movieService.searchMovie(actor,"actors");
				}else if(!StrKit.isBlank(director)) {
					//根据导演搜索
					movie = movieService.searchMovie(director,"director");
				}else if(!StrKit.isBlank(title)) {
					//根据电影名搜索
					movie = movieService.searchMovie(title, "title");
				}
				if(movie != null) {
					baseResponse.setData(movie);
					baseResponse.setResult(ResultCodeEnum.SUCCESS);
				}else {
					baseResponse.setResult(ResultCodeEnum.FAILED);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseResponse.setResult(ResultCodeEnum.FAILED);
		}
		
		this.renderJson(baseResponse);
	}
	
	
	 //根据用户观影记录推荐，若未登录或无记录则推荐评分最高的五部电影
  public void RecommendbyType()
  {
    BaseResponse baseResponse = new BaseResponse();
    try
    {
      String uid = this.getSessionAttr("user_id");//从前端获取当前用户id
      if (StrKit.isBlank(uid))//没登录呢
      {
        List<Movie> movieList = movieService.getHigherGradeMovies();
        List<Movie> finalMovies = new ArrayList<Movie>();
        if(movieList.size()>=5)
        {
          for(int i=0; i<5; i++)
          {
            finalMovies.add(movieList.get(i));
          }
          baseResponse.setData(finalMovies);
          baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }else{
          baseResponse.setData(movieList);
          baseResponse.setResult(ResultCodeEnum.SUCCESS);
        }
      }else//登录了
      {
        List<Movie> result = movieService.getMovieType(Integer.parseInt(uid));
        if(result!= null)//订过票
        {
          Object[] type = new Object[result.size()];
          Object[] btype = new Object[result.size()*5];
          for(int m = 0 ; m < result.size() ; m++)
          {
              type[m] = result.get(m);
              StringBuffer str = new StringBuffer();
              /*for (int n =0 ; n < StringUtils.join(type[m]).split(",").length ; n++ )
              {
                btype[m+n] = StringUtils.join(type[m]).split(",");
              }*/
          }
          List<Movie> best = movieService.getRecommendMovies(type);//得到该用户最喜欢的电影类型
          List<Movie> movie = new ArrayList<Movie>();
          if(type!= null)
          {
            movie = movieService.searchMovieTop(type,"type");
          }else {
            baseResponse.setResult(ResultCodeEnum.FAILED);
          }
          if(movie != null)
          {
            baseResponse.setData(movie);
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
          }else {
            baseResponse.setResult(ResultCodeEnum.FAILED);
          }
        }else//没订过票
        {
          List<Movie> movieList = movieService.getHigherGradeMovies();
          List<Movie> finalMovies = new ArrayList<Movie>();
          if(movieList.size()>=5)
          {
            for(int j=0; j<5; j++)
            {
              finalMovies.add(movieList.get(j));
            }
            baseResponse.setData(finalMovies);
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
          }else{
            baseResponse.setData(movieList);
            baseResponse.setResult(ResultCodeEnum.SUCCESS);
          }
        }
      }
      this.renderJson(baseResponse);
    }catch (Exception e)
    {
      baseResponse.setResult(ResultCodeEnum.FAILED);
      e.printStackTrace();
    }
  }
  
  /**
   * 获取电影的豆瓣评论，传入参数movie_id
   */
  public void commentDouban() {
    BaseResponse baseResponse = new BaseResponse();
    String movieId = this.getPara("movie_id");
    if(StrKit.notBlank(movieId)) {
      List<DoubanComment> result = commServ.getDoubanCommentByMovieId(Integer.parseInt(movieId));
      if(result != null) {
        baseResponse.setResult(ResultCodeEnum.SUCCESS);
        baseResponse.setData(result);
      }else {
        baseResponse.setResult(ResultCodeEnum.FAILED);
      }
    }else {
      baseResponse.setResult(ResultCodeEnum.FAILED);
    }
    this.renderJson(baseResponse);
  }
  
  /**
   * 获取电影的评论，传入参数movie_id
   */
  public void commentOfMovie() {
    BaseResponse baseResponse = new BaseResponse();
    String movieId = this.getPara("movie_id");
    if(StrKit.notBlank(movieId)) {
      List<Comment> result = commServ.getCommentByMovieId(Integer.parseInt(movieId));
      if(result != null) {
        baseResponse.setResult(ResultCodeEnum.SUCCESS);
        baseResponse.setData(result);
      }else {
        baseResponse.setResult(ResultCodeEnum.FAILED);
      }
    }else {
      baseResponse.setResult(ResultCodeEnum.FAILED);
    }
    this.renderJson(baseResponse);
  }
  
  
  /**
   * 添加评论，传入参数movie_id, user_id, content
   */
  public void addComment() {
    BaseResponse baseResponse = new BaseResponse();
    String movieId = this.getPara("movie_id");
    String userId = this.getSessionAttr("user_id");
    String content = this.getPara("content");
    if(StrKit.notBlank(movieId) && StrKit.notBlank(userId) && StrKit.notBlank(content)) {
      boolean saved = commServ.addComment(Integer.parseInt(movieId), Integer.parseInt(userId), content);
      if(saved == true) {
        baseResponse.setResult(ResultCodeEnum.SUCCESS);
      }else {
        baseResponse.setResult(ResultCodeEnum.FAILED);
      }
    }else if(StrKit.isBlank(userId)){
    	baseResponse.setResult(ResultCodeEnum.UN_LOGIN);
    }else{
      baseResponse.setResult(ResultCodeEnum.FAILED);
    }
    this.renderJson(baseResponse);
  }
  
  /**
   * 给用户推荐电影
   */
  public void recommend() {
    BaseResponse baseResponse = new BaseResponse();
    String userId = this.getSessionAttr("user_id");//改成直接前端传userID了
    if(StrKit.notBlank(userId)) {
      ArrayList<MovieTop250> recommendations = movieService.getRecommendationsByUserId(Integer.parseInt(userId));
      if(recommendations != null) {
        baseResponse.setResult(ResultCodeEnum.SUCCESS);
        baseResponse.setData(recommendations);
      }else {
        baseResponse.setResult(ResultCodeEnum.FAILED);
      }
    }else {
      baseResponse.setResult(ResultCodeEnum.FAILED);
    }
    this.renderJson(baseResponse);
  }
  
  /**
   * 根据推荐的电影id查询该电影详情
   */
  public void recommendMovieDetail() {
    BaseResponse baseResponse = new BaseResponse();
    String movieId = this.getPara("movie_id");
    if(StrKit.notBlank(movieId)) {
      MovieTop250 recommendations = movieService.getTop250MovieById(Integer.parseInt(movieId));
      if(recommendations != null) {
        baseResponse.setResult(ResultCodeEnum.SUCCESS);
        baseResponse.setData(recommendations);
      }else {
        baseResponse.setResult(ResultCodeEnum.FAILED);
      }
    }else {
      baseResponse.setResult(ResultCodeEnum.FAILED);
    }
    this.renderJson(baseResponse);
  }
  
  public void tagsOfMovie() {
    BaseResponse baseResponse = new BaseResponse();
    String movieId = this.getPara("movie_id");
    if(StrKit.notBlank(movieId)) {
      JSONArray tags = movieService.getMovieTagsByIdExecptOthers(Integer.parseInt(movieId));
      if(tags != null) {
        baseResponse.setData(tags);
        baseResponse.setResult(ResultCodeEnum.SUCCESS);
      }else {
        baseResponse.setResult(ResultCodeEnum.FAILED);
      }
    }else {
      baseResponse.setResult(ResultCodeEnum.FAILED);
    }
    this.renderJson(baseResponse);
  }
  
}
