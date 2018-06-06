package controller;

import com.jfinal.core.Controller;

public class IndexController extends Controller{
	//跳转页面
	public void index()
	{
		this.render("views/index.html");
	}
	
}
