package model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSeat<M extends BaseSeat<M>> extends Model<M> implements IBean {

	public void setSeatId(java.lang.Integer seatId) {
		set("seat_id", seatId);
	}

	public java.lang.Integer getSeatId() {
		return get("seat_id");
	}

	public void setSeat(java.lang.String seat) {
		set("seat", seat);
	}

	public java.lang.String getSeat() {
		return get("seat");
	}

	public void setAuditoriumId(java.lang.Integer auditoriumId) {
		set("auditorium_id", auditoriumId);
	}

	public java.lang.Integer getAuditoriumId() {
		return get("auditorium_id");
	}

}
