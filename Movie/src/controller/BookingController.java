package controller;

import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import kit.BaseResponse;
import kit.ResultCodeEnum;
import model.Booking;
import service.BookingService;

public class BookingController extends Controller{
	BookingService bookingService = new BookingService();
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
				String str = seat_id;
				String[] seatid = str.split(",");
				for(int i=0;i<seatid.length;i++) {
					int bookId = bookingService.addBooking(user_id, showing_id, seatid[i]);
					baseResponse.setData(bookId);
				}
			
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
