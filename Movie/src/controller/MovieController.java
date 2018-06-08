package controller;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import kit.BaseResponse;
import kit.ResultCodeEnum;
import model.Movie;
import service.MovieService;

public class MovieController extends Controller
{
	MovieService movieService = new MovieService();
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
}
