package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

import kit.BaseResponse;
import kit.ResultCodeEnum;
import model.Auditorium;
import model.Showing;
import model.Theater;
import service.MovieService;
import service.ShowingService;
import service.TheaterService;

public class TheaterController extends Controller{
  SimpleDateFormat strDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //Date + Time
  
	TheaterService theaterService = new TheaterService();
	ShowingService showingService = new ShowingService();
	MovieService movieService = new MovieService();
	
	//新增影厅
	public void addAuditorium()
	{
		BaseResponse baseResponse = new BaseResponse();
		String name = this.getPara("name");
		String seats = this.getPara("seats");
		String theaterid = this.getSessionAttr("theater_id");
		System.out.println(theaterid);
		
		try {
			if(StrKit.isBlank(name) || StrKit.isBlank(seats)) {
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}else {
				int judge = theaterService.addAuditorium(name,seats,theaterid);
				if(judge==0) {
					baseResponse.setResult(ResultCodeEnum.SUCCESS);
				}else if (judge==2) {
					baseResponse.setResult(ResultCodeEnum.NAME_EXIST);
				} else {
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
		String movies = this.getPara("movies");
		
		try {
			if(StrKit.isBlank(movies)) {
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}else {
				String[] allMovies = movies.split(",");
				ArrayList<Integer> conflicts = new ArrayList<>();
				ArrayList<Showing> showings = new ArrayList<>();
				for(int i=0; i<allMovies.length; i++) {
					String[] info = allMovies[i].split("-");
					String movie_id = info[0];
					String auditorium_id = info[1];							
					String show_date = info[2].replace("/", "-");        
          String show_time = info[3];
					String price = info[4];
					
//					Calendar cal=Calendar.getInstance();  
					
//					int year = cal.get(Calendar.YEAR);
//					int month = cal.get(Calendar.MONTH) + 1;
//					int today = cal.get(Calendar.DATE);
					
					show_time = show_date + " " + show_time;
					System.out.println(show_time);
					if(!showingService.showTimeAvailable(show_time, Integer.parseInt(auditorium_id))) {
					  conflicts.add(i);
					}
					Showing newShowing = showingService.addShowing(movie_id, show_time, auditorium_id, price);
					if(newShowing!=null)
					  showings.add(newShowing);
				}
				if(!conflicts.isEmpty()) {
				  for (Showing showing : showings) {
				    showing.delete();
          }
	        baseResponse.setResult(ResultCodeEnum.SHOWTIME_CONFLICT);
	        baseResponse.setData(conflicts);
				}else {
				  for (Showing showing : showings) {
				    System.out.println(showing.getStr("show_time"));
				    movieService.setMovieDate(showing.getMovieId(), showing.getStr("show_time"));
          }
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
	
	//获取影院有哪些影厅
	public void getAuditoriumInfo() {
		BaseResponse baseResponse = new BaseResponse();
		String theaterid = this.getSessionAttr("theater_id");
		
		try {
			List<Auditorium> allAuditoriums = theaterService.getAuditoriumInfo(theaterid);
			if(allAuditoriums != null) {
				baseResponse.setData(allAuditoriums);
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
	
	//获取影院有哪些影厅
	public void getAuditorium() {
		BaseResponse baseResponse = new BaseResponse();
		String theaterid = this.getSessionAttr("theater_id");
		
		try {
			List<Auditorium> allAuditoriums = theaterService.getAuditorium(theaterid);
			if(allAuditoriums != null) {
				baseResponse.setData(allAuditoriums);
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
	
	//根据电影id、城市及放映时间获取所有的影院及放映时间
	public void getTheaterByCityAndTime() {
		BaseResponse baseResponse = new BaseResponse();
		String movie_id = this.getPara("movie_id");
		String city = this.getPara("city");
		String time = this.getPara("time");
		
		try {
			if(StrKit.isBlank(time) ||StrKit.isBlank(city) || StrKit.isBlank(movie_id)) {
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}else {
				//根据电影id、城市及放映时间获取所有的影院及放映时间
				List<Showing> auditoriums = showingService.getAllAuditoriums(time,city,movie_id);
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
	
	public void login()   
	{
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String account = this.getPara("account");	//获取前端传过来的参数  用户名和密码
			String password = this.getPara("password");

			if (StrKit.isBlank(account) || StrKit.isBlank(password))
			{
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}
			else
			{
				Record result = theaterService.validateTheaterByPhone(account);
				if (result == null)
				{
					result = theaterService.validateTheaterByEmail(account);
					if(result == null) {
						
						result = theaterService.validateTheaterByAccount(account);
						if(result == null) {
							baseResponse.setResult(ResultCodeEnum.NO_ACCOUNT);
						}
						else {
							if (!result.get("password").equals(password))
							{
								baseResponse.setResult(ResultCodeEnum.ACCOUNT_PASS_ERROR);
							}
							else
							{
								String theaterid = result.get("theater_id").toString();
								this.setSessionAttr("theater_id", theaterid);
								this.setSessionAttr("theaterName", result.get("theater"));
								Record theatername = new Record();
								theatername.set("theaterName", result.get("theater"));
								baseResponse.setData(theatername);
								baseResponse.setResult(ResultCodeEnum.SUCCESS);
							}
						}
					}else {
						if (!result.get("password").equals(password))
						{
							baseResponse.setResult(ResultCodeEnum.ACCOUNT_PASS_ERROR);
						}
						else
						{
							String theaterid = result.get("theater_id").toString();
							this.setSessionAttr("theater_id", theaterid);
							this.setSessionAttr("theaterName", result.get("theater"));
							Record theatername = new Record();
							theatername.set("theaterName", result.get("theater"));
							baseResponse.setData(theatername);
							baseResponse.setResult(ResultCodeEnum.SUCCESS);
						}
					}
				}
				else
				{
					if (!result.get("password").equals(password))
					{
						baseResponse.setResult(ResultCodeEnum.ACCOUNT_PASS_ERROR);
					}
					else
					{
						String theaterid = result.get("theater_id").toString();
						this.setSessionAttr("theater_id", theaterid);
						this.setSessionAttr("theaterName", result.get("theater"));
						Record theatername = new Record();
						theatername.set("theaterName", result.get("theater"));
						baseResponse.setData(theatername);
						baseResponse.setResult(ResultCodeEnum.SUCCESS);
					}
				}
			}
			System.out.println(baseResponse);
			this.renderJson(baseResponse);	//将数据回传给前端
		}
		catch (Exception e)
		{
			baseResponse.setResult(ResultCodeEnum.FAILED);
			e.printStackTrace();
		}
	}
	
	public void register()
	{
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String account = this.getPara("account");
			String password = this.getPara("password");
			String theater = this.getPara("theater");
			String phone = this.getPara("phone");
			String email = this.getPara("email");
			String city = this.getPara("city");
			String address = this.getPara("address");
			
			if (StrKit.isBlank(account)||StrKit.isBlank(password)||StrKit.isBlank(theater)||StrKit.isBlank(phone)||StrKit.isBlank(email)||StrKit.isBlank(city)||StrKit.isBlank(address))
			{
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}
			else
			{
				Record result = theaterService.validateTheaterByPhone(account);
				if (result == null)
				{
					result = theaterService.validateTheaterByEmail(account);
					if(result == null)
					{
						result = theaterService.validateTheaterByAccount(account);
						{
							if(result == null) {
								boolean ifAdd = theaterService.addTheater(account, password, email, theater, city, address, phone);
								if(ifAdd)
								{
									baseResponse.setResult(ResultCodeEnum.SUCCESS);
								}else {
									baseResponse.setResult(ResultCodeEnum.FAILED);
								}
							}
							else {
								baseResponse.setResult(ResultCodeEnum.PROPERTY_ALREADY);
							}
						}
	
					}else {
						baseResponse.setResult(ResultCodeEnum.MAIL_EXIST);
					}
				}
				else
				{
					baseResponse.setResult(ResultCodeEnum.PHONE_EXIST);
				}
			}
			System.out.println(baseResponse);
			this.renderJson(baseResponse);
		}
		catch (Exception e)
		{
			baseResponse.setResult(ResultCodeEnum.FAILED);
			e.printStackTrace();
		}
	}
	
	public void getTheaterInfo() 
	{
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String theaterid = this.getSessionAttr("theater_id");
			if(StrKit.isBlank(theaterid))
			{
				baseResponse.setResult(ResultCodeEnum.UN_LOGIN);
			}else
			{
				Theater result = theaterService.getTheaterInfo(Integer.parseInt(theaterid));
				if(result!=null)
				{
					baseResponse.setData(result);
					baseResponse.setResult(ResultCodeEnum.SUCCESS);
				}
			}
			System.out.println(baseResponse);
			this.renderJson(baseResponse);
		}
		catch (Exception e)
		{
			baseResponse.setResult(ResultCodeEnum.FAILED);
			e.printStackTrace();
		}
		
	}
	
	public void getInfoByTid() 
	{
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String tid = this.getPara("theater_id");
			Theater result = theaterService.getTheaterInfo(Integer.parseInt(tid));
			if(result!=null)
			{
				baseResponse.setData(result);
			}
			System.out.println(baseResponse);
			this.renderJson(baseResponse);
		}
		catch (Exception e)
		{
			baseResponse.setResult(ResultCodeEnum.FAILED);
			e.printStackTrace();
		}
		
	}
	
	public void changeTheaterInfo() 
	{
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String theater = this.getPara("theater");
			String email = this.getPara("email");
			String phone = this.getPara("phone");
			String city = this.getPara("city");
			String address = this.getPara("address");
			String theater_id = this.getSessionAttr("theater_id");

			if (StrKit.isBlank(theater) && StrKit.isBlank(email) && StrKit.isBlank(phone) && StrKit.isBlank(city) && StrKit.isBlank(address))
			{
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}
			else
			{
				Record result = theaterService.validateTheaterByPhone(phone);
				if (result == null)
				{
					result = theaterService.validateTheaterByEmail(email);
					if(result == null)
					{
						boolean ifChange = theaterService.changeTheaterInfo(theater_id, email, theater_id, city, address, phone);
						if(ifChange)
						{
							baseResponse.setResult(ResultCodeEnum.SUCCESS);
						}else {
							baseResponse.setResult(ResultCodeEnum.FAILED);
						}
					}else {
						baseResponse.setResult(ResultCodeEnum.MAIL_EXIST);
					}
				}
				else
				{
					baseResponse.setResult(ResultCodeEnum.PHONE_EXIST);
				}
			}
			System.out.println(baseResponse);
			this.renderJson(baseResponse);
		}
		catch (Exception e)
		{
			baseResponse.setResult(ResultCodeEnum.FAILED);
			e.printStackTrace();
		}
	}
	public void changePassword()
	{
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String oldPassword = this.getPara("oldPassword");
			String newPassword = this.getPara("newPassword");
			System.out.println(oldPassword+"~~"+newPassword);
			String tid = this.getSessionAttr("theater_id");
			if (StrKit.isBlank(oldPassword) || StrKit.isBlank(newPassword))
			{
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}
			else
			{
				Record passwordResult = theaterService.getPassword(tid);
				String password = passwordResult.get("password");
				if(oldPassword.equals(password))
				{
					boolean ifChange = theaterService.changePassword(tid,newPassword);
					if(ifChange)
					{
						baseResponse.setResult(ResultCodeEnum.SUCCESS);
					}
					else
					{
						baseResponse.setResult(ResultCodeEnum.FAILED);
					}
				}
				else
				{
					baseResponse.setResult(ResultCodeEnum.ACCOUNT_PASS_ERROR);
				}
			}
			System.out.println(baseResponse);
			this.renderJson(baseResponse);
		}
		catch (Exception e)
		{
			baseResponse.setResult(ResultCodeEnum.FAILED);
			e.printStackTrace();
		}
	}
	public void exit()
	{
		try
		{
			this.removeSessionAttr("theater_id");
			this.redirect("/views/pages/manager/manager.html"); // 璺宠浆鍒發ogin鐣岄潰
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void deleteTheater() {
		try
		{
			String theaterid = this.getSessionAttr("theater_id");
			theaterService.deleteTheaterById(Integer.parseInt(theaterid));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		exit();
	}
	
	public void deleteAuditorium() {
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String auditoriumid = this.getPara("auditorium_id");
			int deleteType = theaterService.deleteAuditoriumById(auditoriumid);
			if (deleteType==1) {
				baseResponse.setResult(ResultCodeEnum.DELETE_THEATER);
			} else if (deleteType==2) {
				baseResponse.setResult(ResultCodeEnum.SHOW_EXIST);
			} else {
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		this.renderJson(baseResponse);
	}
	
	public void checkLogin() {
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String theaterid = this.getSessionAttr("theater_id");
			Record result = new Record();
			if(StrKit.isBlank(theaterid)) {
				result.set("isLogin", 0);
			}else {
				result.set("isLogin", 1);
				result.set("theaterName", this.getSessionAttr("theaterName"));
			}
			baseResponse.setData(result);
			baseResponse.setResult(ResultCodeEnum.SUCCESS);
		}
		catch (Exception e)
		{
			baseResponse.setResult(ResultCodeEnum.FAILED);
			e.printStackTrace();
		}
		this.renderJson(baseResponse);
	}
	
}
