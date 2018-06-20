package controller;

import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

import kit.BaseResponse;
import kit.ResultCodeEnum;
import model.Seat;
import service.SeatService;

public class SeatController extends Controller{
	SeatService seatService = new SeatService();
	//根据showing_id获取放映厅的座位
	public void getSeatsByShowingId() {
		BaseResponse baseResponse = new BaseResponse();
		String showingid = this.getPara("showing_id");
		
		try {
			//根据showing_id获取放映厅的座位
			List<Seat> seats = seatService.getSeatsByShowingId(showingid);
			if(seats != null) {
				baseResponse.setData(seats);
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
	
	/**
	 * 根据座位名称和showing_id获取座位详细信息
	 */
	public void getSeatByNameAndShowing() {
    BaseResponse baseResponse = new BaseResponse();
    String seatName = this.getPara("seat");
    String showingId = this.getPara("showing_id");

    if(StrKit.notBlank(showingId) && StrKit.notBlank(seatName)) {
      Seat seat = seatService.getSeatByNameAndShowing(seatName, Integer.parseInt(showingId));
      baseResponse.setData(seat);
      baseResponse.setResult(ResultCodeEnum.SUCCESS);
    }else {
      baseResponse.setResult(ResultCodeEnum.MISS_PARA);
    }
    this.renderJson(baseResponse);
	  
	}
	
	
	//根据影厅id获取位置
	public void getSeatsByAuditoriumId() {
		BaseResponse baseResponse = new BaseResponse();
		String roomid = this.getPara("roomId");
		
		if(StrKit.isBlank(roomid)) {
			baseResponse.setResult(ResultCodeEnum.MISS_PARA);
		}else {
			List<Seat> result = seatService.getSeatsByAuditoriumId(roomid);
			if(result == null) {
				baseResponse.setResult(ResultCodeEnum.FAILED);
			}else {
				baseResponse.setData(result);
				baseResponse.setResult(ResultCodeEnum.SUCCESS);
			}
		}
		this.renderJson(baseResponse);
	}
	
	public void getSeat() {
    BaseResponse baseResponse = new BaseResponse();
    String seat_id = this.getPara("seat_id");
    if(StrKit.notBlank(seat_id)) {
      Seat result = seatService.getSeat(Integer.parseInt(seat_id));   
      if(result != null) {
        baseResponse.setData(result);
        baseResponse.setResult(ResultCodeEnum.SUCCESS);
      }else {
        baseResponse.setResult(ResultCodeEnum.FAILED);
      }
    }else {
      baseResponse.setResult(ResultCodeEnum.MISS_PARA);
    }
    
    this.renderJson(baseResponse);
	}

}
