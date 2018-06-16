package controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import kit.BaseResponse;
import kit.ResultCodeEnum;
import model.Booking;
import service.BookingService;
import service.MovieService;
import service.SeatService;
import service.ShowingService;
import service.TheaterService;

public class BookingController extends Controller{
  SimpleDateFormat strDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //Date + Time
	BookingService bookingService = new BookingService();
	MovieService movieServ = new MovieService();
	ShowingService showingServ = new ShowingService();
	TheaterService theaterServ = new TheaterService();
	SeatService seatServ = new SeatService();
	
	public void addOneBooking() {
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String user_id = this.getSessionAttr("user_id");
			String showing_id = this.getPara("showing_id");
			String seat_id = this.getPara("seat_id");
			
			if (StrKit.isBlank(user_id)||StrKit.isBlank(showing_id)||StrKit.isBlank(seat_id))
			{
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}
			else
			{
				int bookId = bookingService.addBooking(user_id, showing_id, seat_id);
				baseResponse.setData(bookId);
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
	//根据用户ID查询订单
	public void getBookingInfoByUid() 
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
	      List<Booking> result = bookingService.getUserBookingInfos(Integer.parseInt(uid));
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
	
	public void allBookings() {
    BaseResponse baseResponse = new BaseResponse();
    String uid = this.getSessionAttr("user_id");
    if(StrKit.isBlank(uid))
    {
      baseResponse.setResult(ResultCodeEnum.UN_LOGIN);
    }else
    {
      ArrayList<HashMap<String,String>> result = new ArrayList<>();
      List<Booking> bookings = bookingService.getUserBookingInfos(Integer.parseInt(uid));
      for (Booking booking : bookings) {
        HashMap<String,String> map = new HashMap<>();
        map.put("purchase_time", strDateTime.format(booking.getPurchaseTime()));
        map.put("token", booking.getToken());
        map.put("movie", movieServ.getMovieInfoById(showingServ.getShowing(booking.getShowingId()).getMovieId().toString()).getTitle());
        map.put("img", movieServ.getMovieInfoById(showingServ.getShowing(booking.getShowingId()).getMovieId().toString()).getImage());
        map.put("show_time", strDateTime.format(showingServ.getShowing(booking.getShowingId()).getShowTime()));
        map.put("theater", theaterServ.getTheaterByAuditoriumId(showingServ.getShowing(booking.getShowingId()).getAuditoriumId()).getTheater());
        map.put("auditorium", theaterServ.getAuditoriumById(showingServ.getShowing(booking.getShowingId()).getAuditoriumId()).getAuditorium());
        //换成seat
        String[] bookingIds = booking.getSeatId().split(",");
        String seats = "";
        for (String seatId : bookingIds) {
          if(seats.equals("")) {
            seats += seatServ.getSeat(Integer.parseInt(seatId)).getSeat();
          }else {
            seats += ", " + seatServ.getSeat(Integer.parseInt(seatId)).getSeat();
          }
        }
        map.put("seat", seats);
        result.add(map);
      }
      if(result!=null)
      {
        baseResponse.setData(result);
        baseResponse.setResult(ResultCodeEnum.SUCCESS);
      }
      this.renderJson(baseResponse);
    }
	}
	
	//根据订单号查询订单
	public void getBookingInfoByBookid() 
	{
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String bookid = this.getPara("booking_id");
			if(StrKit.isBlank(bookid))
			{
				baseResponse.setResult(ResultCodeEnum.UN_LOGIN);
			}else
			{
				Booking result = bookingService.getBookingInfoById(Integer.parseInt(bookid));
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
}
