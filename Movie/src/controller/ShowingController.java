package controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

import kit.BaseResponse;
import kit.ResultCodeEnum;
import model.Showing;
import model.Theater;
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
				String todayDate = "";
				Calendar cal = Calendar.getInstance();    
				if(day.equals("0")) {
					//今天
					todayDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
				}else {
					// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
				     int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
				     if (1 == dayWeek) {  
				        cal.add(Calendar.DAY_OF_MONTH, -1);  
				     }  
				     System.out.println("要计算日期为:" + cal.getTime()); // 输出要计算日期  
				     // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
				     cal.setFirstDayOfWeek(Calendar.MONDAY);  
				     // 获得当前日期是一个星期的第几天  
				     int today = cal.get(Calendar.DAY_OF_WEEK);  
				     // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
				     cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - today);  
				     Date imptimeBegin = cal.getTime();  
				     System.out.println("所在周星期一的日期：" + imptimeBegin);
				     cal.add(Calendar.DATE, Integer.parseInt(day)-1);
				     System.out.println(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE));
				     todayDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
				}
			     
				List<Showing> showings = showingService.ifHasShowing(todayDate,theaterId);
				Record result = new Record();
				if(showings.size() == 0) {
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
				String todayDate = "";
				Calendar cal = Calendar.getInstance();    
				if(day.equals("0")) {
					//今天
					todayDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
				}else {
					// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
				     int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
				     if (1 == dayWeek) {  
				        cal.add(Calendar.DAY_OF_MONTH, -1);  
				     }  
				     System.out.println("要计算日期为:" + cal.getTime()); // 输出要计算日期  
				     // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
				     cal.setFirstDayOfWeek(Calendar.MONDAY);  
				     // 获得当前日期是一个星期的第几天  
				     int today = cal.get(Calendar.DAY_OF_WEEK);  
				     // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
				     cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - today);  
				     Date imptimeBegin = cal.getTime();  
				     System.out.println("所在周星期一的日期：" + imptimeBegin);
				     cal.add(Calendar.DATE, Integer.parseInt(day)-1);
				     todayDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
				}  
				List<Showing> showings = showingService.getShowingInfoByMovieId(todayDate,movie_id,theaterId);
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
	
	/**
	 * 通过 movie_id 和 城市 获取所有电影院的该影片的场次列表
	 */
	public void showingsOfTheaters() {
    BaseResponse baseResponse  = new BaseResponse();
    String movieId = this.getPara("movie_id");
    String city = this.getPara("city");
    String time = this.getPara("time");
    
    if(StrKit.notBlank(movieId) && StrKit.notBlank(city)) {
      HashMap<String, ArrayList<Showing>> result = showingService.getShowingsOfTheaters(Integer.parseInt(movieId), city, time);
      if(result != null && !result.isEmpty()) {
        baseResponse.setData(result);
        baseResponse.setResult(ResultCodeEnum.SUCCESS);
      }else {
        baseResponse.setData(null);
        baseResponse.setResult(ResultCodeEnum.SUCCESS);
      }
    }else {
      baseResponse.setResult(ResultCodeEnum.FAILED);
    }
    this.renderJson(baseResponse);
	}
	
	public void deleteShowing() {
		BaseResponse baseResponse  = new BaseResponse();
		String showingId = this.getPara("showing_id");
		if (StrKit.notBlank(showingId)) {
			if (!showingService.deleteShowing(showingId)) {
				baseResponse.setResult(ResultCodeEnum.BOOK_EXIST);
			} else {
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}
		}
		this.renderJson(baseResponse);
	}
}
