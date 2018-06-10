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

}
