package model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("auditorium", "auditorium_id", Auditorium.class);
		arp.addMapping("booking", "booking_id", Booking.class);
		arp.addMapping("comment", "comment_id", Comment.class);
		arp.addMapping("douban_comment", "comment_id", DoubanComment.class);
		arp.addMapping("movie", "movie_id", Movie.class);
		arp.addMapping("movie_top250", "id", MovieTop250.class);
		arp.addMapping("seat", "seat_id", Seat.class);
		arp.addMapping("showing", "showing_id", Showing.class);
		arp.addMapping("theater", "theater_id", Theater.class);
		arp.addMapping("user_info", "user_id", UserInfo.class);
	}
}

