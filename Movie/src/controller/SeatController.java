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
		String showingid = this.getSessionAttr("showing_id");
		
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

}
