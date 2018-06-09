package config;
//lja
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.Const;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

import controller.IndexController;
import controller.MovieController;
import controller.UserController;
import model._MappingKit;

public class CommonConfig extends JFinalConfig
{
//	public final static String relativePath = "upload/images/"
//	            + CommonUtils.getCurrentDate();   //相对路径
	public final static String absolutePath = "/usr/local/images";

	@Override
	public void configConstant(Constants me)
	{
		// TODO Auto-generated method stub

		PropKit.use("prop.properties");
		me.setDevMode(true);
		me.setEncoding("utf-8");
		me.setViewType(ViewType.JSP);
		
		me.setBaseUploadPath(absolutePath);// 文件上传保存路径
	    me.setMaxPostSize(1 * Const.DEFAULT_MAX_POST_SIZE);// 上传文件最大为10M
	}

	@Override
	public void configRoute(Routes me)
	{
		// TODO Auto-generated method stub
		me.add("/",IndexController.class);
		me.add("/movie",MovieController.class);
		me.add("/user",UserController.class);
		
	}

	@Override
	public void configEngine(Engine me)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void configPlugin(Plugins me)
	{
		// TODO Auto-generated method stub
		//任务调度
		Cron4jPlugin cron = new Cron4jPlugin("task.properties");
		me.add(cron);
		//数据库
		DruidPlugin dp = new DruidPlugin(PropKit.use("prop.properties").get("url"), PropKit.get("user"), PropKit.get("password"));
		me.add(dp);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp); 
		me.add(arp);
		_MappingKit.mapping(arp);
	}

	@Override
	public void configInterceptor(Interceptors me)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configHandler(Handlers me)
	{
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args) {
		JFinal.start("WebRoot", 80, "/", 5);
	}

}
