package controller;

import java.util.List;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import kit.BaseResponse;
import kit.ResultCodeEnum;
import model.Collection;
import service.CollectionService;

public class CollectionController  extends Controller{
	CollectionService collectionService = new CollectionService();
	
	public void addOneColletion() {
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String user_id = this.getSessionAttr("user_id");
			String movie_id = this.getPara("movie_id");
			
			if (StrKit.isBlank(user_id)||StrKit.isBlank(movie_id))
			{
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}
			else
			{
				String str = movie_id;
				String[] movieid = str.split(",");
				for(int i=0;i<movieid.length;i++) {
					int collectionId = collectionService.addCollection(user_id, movieid[i]);
					baseResponse.setData(collectionId);
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
	//根据用户id查询收藏夹
	public void getCollectionByUid() 
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
				List<Collection> result = collectionService.getCollectionInfos(Integer.parseInt(uid));
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
	
	//删除收藏夹的内容
	public void deleteCollectionById() 
	{
		BaseResponse baseResponse = new BaseResponse();
		try
		{
			String collectionId = this.getPara("collection_id");
			if(StrKit.isBlank(collectionId))
			{
				baseResponse.setResult(ResultCodeEnum.MISS_PARA);
			}else
			{
				String str =collectionId;
				String[] collection_id = str.split(",");
				for(int i=0;i<collection_id.length;i++) {
					boolean cId = collectionService.deleteCollection(collection_id[i]);
					baseResponse.setData(cId);
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
	
}
