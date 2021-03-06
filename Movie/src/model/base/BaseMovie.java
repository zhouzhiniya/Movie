package model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseMovie<M extends BaseMovie<M>> extends Model<M> implements IBean {

	public void setMovieId(java.lang.Integer movieId) {
		set("movie_id", movieId);
	}

	public java.lang.Integer getMovieId() {
		return get("movie_id");
	}

	public void setDate(java.util.Date date) {
		set("date", date);
	}

	public java.util.Date getDate() {
		return get("date");
	}

	public void setTitle(java.lang.String title) {
		set("title", title);
	}

	public java.lang.String getTitle() {
		return get("title");
	}

	public void setTitleEn(java.lang.String titleEn) {
		set("title_en", titleEn);
	}

	public java.lang.String getTitleEn() {
		return get("title_en");
	}

	public void setDoubanId(java.lang.String doubanId) {
		set("douban_id", doubanId);
	}

	public java.lang.String getDoubanId() {
		return get("douban_id");
	}

	public void setMtimeId(java.lang.String mtimeId) {
		set("mtime_id", mtimeId);
	}

	public java.lang.String getMtimeId() {
		return get("mtime_id");
	}

	public void setDuration(java.lang.String duration) {
		set("duration", duration);
	}

	public java.lang.String getDuration() {
		return get("duration");
	}

	public void setDirector(java.lang.String director) {
		set("director", director);
	}

	public java.lang.String getDirector() {
		return get("director");
	}

	public void setActors(java.lang.String actors) {
		set("actors", actors);
	}

	public java.lang.String getActors() {
		return get("actors");
	}

	public void setType(java.lang.String type) {
		set("type", type);
	}

	public java.lang.String getType() {
		return get("type");
	}

	public void setImage(java.lang.String image) {
		set("image", image);
	}

	public java.lang.String getImage() {
		return get("image");
	}

	public void setVideo(java.lang.String video) {
		set("video", video);
	}

	public java.lang.String getVideo() {
		return get("video");
	}

	public void setReleaseDate(java.util.Date releaseDate) {
		set("release_date", releaseDate);
	}

	public java.util.Date getReleaseDate() {
		return get("release_date");
	}

	public void setForecastRating(java.lang.Float forecastRating) {
		set("forecast_rating", forecastRating);
	}

	public java.lang.Float getForecastRating() {
		return get("forecast_rating");
	}

	public void setDoubanRating(java.lang.Float doubanRating) {
		set("douban_rating", doubanRating);
	}

	public java.lang.Float getDoubanRating() {
		return get("douban_rating");
	}

	public void setWishCount(java.lang.Integer wishCount) {
		set("wish_count", wishCount);
	}

	public java.lang.Integer getWishCount() {
		return get("wish_count");
	}

	public void setCollectCount(java.lang.Integer collectCount) {
		set("collect_count", collectCount);
	}

	public java.lang.Integer getCollectCount() {
		return get("collect_count");
	}

	public void setReviewsCount(java.lang.Integer reviewsCount) {
		set("reviews_count", reviewsCount);
	}

	public java.lang.Integer getReviewsCount() {
		return get("reviews_count");
	}

	public void setCommentsCount(java.lang.Integer commentsCount) {
		set("comments_count", commentsCount);
	}

	public java.lang.Integer getCommentsCount() {
		return get("comments_count");
	}

	public void setSummary(java.lang.String summary) {
		set("summary", summary);
	}

	public java.lang.String getSummary() {
		return get("summary");
	}

	public void setTotalBox(java.lang.Long totalBox) {
		set("total_box", totalBox);
	}

	public java.lang.Long getTotalBox() {
		return get("total_box");
	}

	public void setTagJson(java.lang.String tagJson) {
		set("tag_json", tagJson);
	}

	public java.lang.String getTagJson() {
		return get("tag_json");
	}

}
