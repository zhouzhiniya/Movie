package service;

import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import model.Auditorium;
import model.Seat;
import model.Showing;
import model.Theater;

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
	//新增影厅
	public boolean addAuditorium(String name,String seats,String theaterid) {
		Auditorium auditorium = new Auditorium();
		auditorium.set("auditorium", name);
		auditorium.set("theater_id", theaterid);
		boolean ifAddAuditorium = auditorium.save();
		if(ifAddAuditorium) {
			int auditoriumId = auditorium.getAuditoriumId();
			String[] allSeats = seats.split(",");
			for(int i=0; i<allSeats.length; i++) {
				Seat seat = new Seat();
				seat.set("seat", allSeats[i]);
				seat.set("auditorium_id", auditoriumId);
				seat.set("seat_state", 0);
				seat.save();
				if(!seat.save()) {
					return false;
				}
			}
			return true;
		}else {
			return ifAddAuditorium;
		}
	}
	
	//新增排片
	public boolean addShowing(String movie_id,String show_time,String auditorium_id,String price) {
		Showing showing = new Showing();
		showing.set("movie_id", Integer.parseInt(movie_id));
		showing.set("show_time", show_time);
		showing.set("auditorium_id", Integer.parseInt(auditorium_id));
		showing.set("price", price);
		return showing.save();
	}
	
	//获取所有 影院所在的城市
	public List<Theater> getAllCity() {
		return Theater.dao.find("select city from theater group by city");
	}
}
