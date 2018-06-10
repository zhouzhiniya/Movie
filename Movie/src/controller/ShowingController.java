package controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

import kit.BaseResponse;
import kit.ResultCodeEnum;
import model.Showing;
import service.ShowingService;

public class ShowingController extends Controller{
	ShowingService showingService = new ShowingService();
	//判断某天是否有排片
	public void ifHasShowing() {
		BaseResponse baseResponse  = new BaseResponse();
		String day = this.getPara("day");
		String theaterId = this.getSessionAttr("theater_id");
		
		try {
			if(StrKit.isBlank(day)) {
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}else {
				Date date = new Date();
				Calendar today = Calendar.getInstance();
				if(day.equals("0")) {
					date = today.getTime();
				}else {
					int n = today.get(Calendar.DAY_OF_WEEK) - 1;
			        if (n == 0) {
			            n = 7;
			        }
			        today.add(Calendar.DATE, -(7 + (n - 1)));// 上周一的日期
			        int Day = Integer.parseInt(day);	//表示上周几
		        	for(int i=0; i<Day-1; i++) {
		        		today.add(Calendar.DATE , 1);
		        	}
		        	date = today.getTime();
				}
				List<Showing> showings = showingService.ifHasShowing(date,theaterId);
				Record result = new Record();
				if(showings == null) {
					result.set("haveShowing", 0);
					baseResponse.setData(result);
					baseResponse.setResult(ResultCodeEnum.SUCCESS);
				}else {
					result.set("haveShowing", 1);
					result.set("showinginfo", showings);
					baseResponse.setData(result);
					baseResponse.setResult(ResultCodeEnum.SUCCESS);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			baseResponse.setResult(ResultCodeEnum.FAILED);
		}
		
		this.renderJson(baseResponse);
	}
	
	//获取某部电影在某天的排片情况
	public void getShowingInfoByMovieId() {
		BaseResponse baseResponse  = new BaseResponse();
		String day = this.getPara("day");
		String movie_id = this.getPara("id");
		String theaterId = this.getSessionAttr("theater_id");
		
		try {
			if(StrKit.isBlank(day) || StrKit.isBlank(movie_id)) {
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}else {
				Date date = new Date();
				Calendar today = Calendar.getInstance();
				if(day.equals("0")) {
					date = today.getTime();
				}else {
					int n = today.get(Calendar.DAY_OF_WEEK) - 1;
			        if (n == 0) {
			            n = 7;
			        }
			        today.add(Calendar.DATE, -(7 + (n - 1)));// 上周一的日期
			        int Day = Integer.parseInt(day);	//表示上周几
		        	for(int i=0; i<Day-1; i++) {
		        		today.add(Calendar.DATE , 1);
		        	}
		        	date = today.getTime();
				}
				List<Showing> showings = showingService.getShowingInfoByMovieId(date,movie_id,theaterId);
				baseResponse.setData(showings);
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
