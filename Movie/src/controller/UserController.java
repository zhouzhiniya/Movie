package controller;

import com.jfinal.core.Controller;

import java.sql.Date;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;

import kit.BaseResponse;
import kit.ResultCodeEnum;
import model.UserInfo;
import service.UserService;

public class UserController extends Controller{
	UserService userService = new UserService();
	public void login()   
	{
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String username = this.getPara("username");	//获取前端传过来的参数  用户名和密码
			String password = this.getPara("password");

			if (StrKit.isBlank(username) || StrKit.isBlank(password))
			{
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}
			else
			{
				Record result = userService.validateUserByPhone(username);
				if (result == null)
				{
					result = userService.validateUserByMail(username);
					if(result == null) {
						
						result = userService.validateUserByUsername(username);
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
								String uid = result.get("user_id").toString();
								this.setSessionAttr("user_id", uid);
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
							String uid = result.get("user_id").toString();
							this.setSessionAttr("user_id", uid);
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
						String uid = result.get("user_id").toString();
						this.setSessionAttr("user_id", uid);
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
			String username = this.getPara("username");
			String password = this.getPara("password");
			String name = this.getPara("name");
			String mobile = this.getPara("mobile");
			String email = this.getPara("email");
			String gender = this.getPara("gender");
			java.util.Date birthday = (Date) this.getParaToDate("birthday");
			java.util.Date register_time = new java.util.Date();//register_time!!!! 两个时间处理

			if (StrKit.isBlank(username)||StrKit.isBlank(password)||StrKit.isBlank(name)||StrKit.isBlank(mobile)||StrKit.isBlank(email)||StrKit.isBlank(gender)||StrKit.isBlank(birthday.toString()))
			{
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}
			else
			{
				Record result = userService.validateUserByPhone(mobile);
				if (result == null)
				{
					result = userService.validateUserByMail(email);
					if(result == null)
					{
						result = userService.validateUserByUsername(username);
						{
							if(result == null) {
								boolean ifAdd = userService.addUser(username, password, name,mobile, email, gender,birthday,register_time);
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
	
	public void getUserInfo() 
	{
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String uid = this.getSessionAttr("user_id");
			if(StrKit.isBlank(uid))
			{
				baseResponse.setResult(ResultCodeEnum.UN_LOGIN);
			}else
			{
				UserInfo result = userService.getUserInfo(Integer.parseInt(uid));
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
	
	public void getInfoByUid() 
	{
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String uid = this.getPara("user_id");
			UserInfo result = userService.getUserInfo(Integer.parseInt(uid));
			if(result!=null)
			{
				baseResponse.setData(result);
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
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
	
	public void changeUserInfo() 
	{
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String name = this.getPara("name");
			String email = this.getPara("email");
			String mobile = this.getPara("mobile");
			String gender = this.getPara("gender");
			Date birthday = (Date) this.getParaToDate("birthday");
			String user_id = this.getSessionAttr("user_id");

			if (StrKit.isBlank(name) && StrKit.isBlank(email) && StrKit.isBlank(mobile) && StrKit.isBlank(gender) && StrKit.isBlank(birthday.toString()))
			{
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}
			else
			{
				Record result = userService.validateUserByPhone(mobile);
				if (result == null)
				{
					result = userService.validateUserByMail(email);
					if(result == null)
					{
						boolean ifChange = userService.changeUserInfo(name, mobile, email,birthday, gender, user_id);
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
			String uid = this.getSessionAttr("user_id");
			if (StrKit.isBlank(oldPassword) || StrKit.isBlank(newPassword))
			{
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}
			else
			{
				Record passwordResult = userService.getPassword(uid);
				String password = passwordResult.get("password");
				if(oldPassword.equals(password))
				{
					boolean ifChange = userService.changePassword(uid,newPassword);
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
			this.removeSessionAttr("user_id");
			this.redirect("/"); // 璺宠浆鍒發ogin鐣岄潰
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void checkLogin() 
	{
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String uid = this.getSessionAttr("user_id");
			if (StrKit.isBlank(uid))
			{
				baseResponse.setResult(ResultCodeEnum.UN_LOGIN);
			}
			else
			{
				baseResponse.setResult(ResultCodeEnum.ALREADY_LOGIN);
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
}
