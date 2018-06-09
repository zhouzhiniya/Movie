package service;

import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import model.Theater;
import model.UserInfo;

public class TheaterService {
	public Record validateTheaterByPhone(String phone)
	{
		return Db.findFirst("select theater_id,theater,phone,password from theater where phone=?", phone);
	}
	
	public Record validateTheaterByEmail(String email)
	{
		return Db.findFirst("select theater_id,theater,email,password from theater where email=?", email);
	}
	
	public Record validateTheaterByAccount(String account)
	{
		return Db.findFirst("select theater_id,theater,account,password from theater where account=?", account);
	}
	
	public boolean addTheater(String account,String password,String email,String theater,String city,String address,String phone,Date registerTime)
	{
		Theater aTheater=new Theater();
		aTheater.setAccount(account);
		aTheater.setPassword(password);
		aTheater.setEmail(email);
		aTheater.setTheater(theater);
		aTheater.setAddress(address);
		aTheater.setCity(city);
		aTheater.setPhone(phone);
		aTheater.setRegisterTime(registerTime);
		return aTheater.save();
	}
	
	public Theater getTheaterInfo(Integer theater_id) 
	{
		return Theater.dao.findFirst("select * from theater where theater_id=?", theater_id);
	}
	
	public boolean changeTheaterInfo(String theater_id,String email,String theater,String city,String address,String phone)
	{
		Theater aTheater=Theater.dao.findById(Integer.parseInt(theater_id));
		if(!theater.equals(""))
			aTheater.setTheater(theater);
		if(!email.equals(""))
			aTheater.setEmail(email);
		if(!city.equals(""))
			aTheater.setCity(city);
		if(!address.equals(""))
			aTheater.setAddress(address);
		if(!phone.equals("-1"))
			aTheater.setPhone(phone);
		return aTheater.update();
	}
	
	public Record getPassword(String theater_id)
	{
		return Db.findFirst("select password from theater where theater_id=?", Integer.parseInt(theater_id)); 
	}
	
	public boolean changePassword(String theater_id,String newPassword)
	{
		Theater aTheater=Theater.dao.findById(Integer.parseInt(theater_id));
		aTheater.setPassword(newPassword);
		return aTheater.update();
	}
}
