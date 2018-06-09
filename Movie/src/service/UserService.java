package service;


import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import model.UserInfo;

public class UserService {
	public Record validateUserByPhone(String mobile)
	{
		return Db.findFirst("select user_id,username,mobile,password from user_info where mobile=?", mobile);
	}
	
	public Record validateUserByMail(String email)
	{
		return Db.findFirst("select user_id,username,email,password from user_info where email=?", email);
	}
	
	public Record validateUserByUsername(String username)
	{
		return Db.findFirst("select user_id,username,name,password from user_info where email=?", username);
	}
	
	public boolean addUser(String username,String password,String name,String mobile,String email,String gender,Date birthday,Date registerTime)
	{
		UserInfo user=new UserInfo();
		user.setUsername(username);
		user.setPassword(password);
		user.setMobile(mobile);
		user.setEmail(email);
		user.setName(name);
		user.setGender(Integer.parseInt(gender));
		user.setBirthday(birthday);
		user.setRegisterTime(registerTime);
		return user.save();
	}
	
	public UserInfo getUserInfo(Integer uid) 
	{
		return UserInfo.dao.findFirst("select * from user_info where user_id=?", uid);
	}
	
	public boolean changeUserInfo(String name,String mobile,String email,Date birthday,String gender,String uid)
	{
		UserInfo user=UserInfo.dao.findById(Integer.parseInt(uid));
		if(!name.equals(""))
			user.setName(name);
		if(!mobile.equals(""))
			user.setMobile(mobile);
		if(!email.equals(""))
			user.setEmail(email);
		if(!birthday.equals(""))
			user.setBirthday(birthday);
		if(!gender.equals("-1"))
			user.setGender(Integer.parseInt(gender));
		return user.update();
	}
	
	public Record getPassword(String uid)
	{
		return Db.findFirst("select password from user_info where user_id=?", Integer.parseInt(uid)); 
	}
	
	public boolean changePassword(String uid,String newPassword)
	{
		UserInfo user=UserInfo.dao.findById(Integer.parseInt(uid));
		user.setPassword(newPassword);
		return user.update();
	}


}
