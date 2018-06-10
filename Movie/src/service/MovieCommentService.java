package service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Comment;
import model.DoubanComment;

public class MovieCommentService {
  
  
  /**
   * 根据电影 movie_id 获取豆瓣评论
   * @param movieId
   * @return
   */
  public List<DoubanComment> getDoubanCommentByMovieId(int movieId){
    List<DoubanComment> result = DoubanComment.dao.find("select * from douban_comment where movie_id = ?", movieId);
    return result;
  }
  
  /**
   * 根据电影 movie_id 获取本站评论
   * @param movieId
   * @return
   */
  public List<Comment> getCommentByMovieId(int movieId){
    List<Comment> result = Comment.dao.find("select * from comment where movie_id = ?", movieId);
    return result;
  }
  
  public boolean addComment(int movieId, int userId, String content) {
    Comment newComment = new Comment();
    Date now = new Date();
    newComment.setUserId(userId);
    newComment.setMovieId(movieId);
    newComment.setContent(content);
    newComment.setCreatedAt(now);
    return newComment.save();
  }

}
