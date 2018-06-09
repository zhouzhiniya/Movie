package controller;

import java.sql.Date;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

import kit.BaseResponse;
import kit.ResultCodeEnum;
import model.Theater;
import model.UserInfo;
import service.TheaterService;



public class TheaterController extends Controller{
	TheaterService theaterService = new TheaterService();
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
			java.util.Date register_time = new java.util.Date();//register_time!!!! 两个时间处理

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
								boolean ifAdd = theaterService.addTheater(account, password, email, theater, city, address, phone, register_time);
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
			this.redirect("/"); // 璺宠浆鍒發ogin鐣岄潰
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
}
