package controller;

import com.jfinal.core.Controller;

import service.MovieAPIService;
import service.MovieService;

public class IndexController extends Controller{

	public void index()
	{
		this.render("views/index.html");
	}
	
	//这是我用来测试的，不许动！！
	public void haha() {
	  MovieAPIService ser = new MovieAPIService();
//	  ser.getTodayMovies();
    ser.saveTodayMovies();
	}
	
	public void xixi() {

    MovieService servvvvv = new MovieService();
    servvvvv.getTodayMovies();
	}
	
	public void heihei() {
    MovieAPIService ser = new MovieAPIService();
    ser.saveTop250();
	}
	

}
