package service;

import java.util.List;

import model.Collection;

public class CollectionService {
	//用户添加观影清单（收藏夹）
	public int addCollection(String user_id,String movie_id) {
		  Collection collection =new Collection();
		  collection.setUserId(Integer.parseInt(user_id));
		  collection.setMovieId(Integer.parseInt(user_id));
		  collection.save();
		  return collection.getCollectionId();
	  }
	
	//获取用户全部收藏电影信息
	 public List<Collection> getCollectionInfos(Integer uid) 
		{
			return Collection.dao.find("select * from collection where user_id=?", uid);
		}
	
	 //批量删除收藏夹
	 public boolean deleteCollection(String collection_id){
		 return Collection.dao.deleteById(collection_id);
	 }
	
	
}
