package controller;

import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import kit.BaseResponse;
import kit.ResultCodeEnum;
import model.Movie;
import model.Showing;
import model.Theater;
import service.ShowingService;
import service.TheaterService;

public class TheaterController extends Controller{
	TheaterService theaterService = new TheaterService();
	ShowingService showingService = new ShowingService();
	
	//新增影厅
	public void addAuditorium()
	{
		BaseResponse baseResponse = new BaseResponse();
		String name = this.getPara("name");
		String seats = this.getPara("seats");
		String theaterid = this.getSessionAttr("user_id");
		
		try {
			if(StrKit.isBlank(name) || StrKit.isBlank(seats)) {
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}else {
				if(theaterService.addAuditorium(name,seats,theaterid)) {
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
	
	//新增影片
	public void addShowing() {
		BaseResponse baseResponse = new BaseResponse();
		String movie_id = this.getPara("movie_id");
		String show_time = this.getPara("show_time");
		String auditorium_id = this.getPara("auditorium_id");
		String price = this.getPara("price");
		
		try {
			if(StrKit.isBlank(movie_id) || StrKit.isBlank(show_time) || StrKit.isBlank(auditorium_id) || StrKit.isBlank(price)) {
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}else {
				if(theaterService.addShowing(movie_id,show_time,auditorium_id,price)) {
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
	
	//获取所有影院所在的城市
	public void getAllCity() {
		BaseResponse baseResponse = new BaseResponse();
		
		try {
			List<Theater> allCity = theaterService.getAllCity();
			if(allCity != null) {
				baseResponse.setData(allCity);
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}else {
				baseResponse.setResult(ResultCodeEnum.FAILED);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseResponse.setResult(ResultCodeEnum.FAILED);
		}
		
		this.renderJson(baseResponse);
	}
	
	//根据城市及放映时间获取所有的影院及放映时间
	public void getTheaterByCityAndTime() {
		BaseResponse baseResponse = new BaseResponse();
		String city = this.getPara("city");
		String time = this.getPara("time");
		
		try {
			if(StrKit.isBlank(time) ||StrKit.isBlank(city)) {
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}else {
				//根据城市及放映时间获取所有的影院及放映时间
				List<Showing> auditoriums = showingService.getAllAuditoriums(time,city);
				baseResponse.setData(auditoriums);
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseResponse.setResult(ResultCodeEnum.FAILED);
		}
		
		this.renderJson(baseResponse);
	}

}
