package controller;

import com.jfinal.plugin.cron4j.ITask;

import service.MovieAPIService;

public class TaskController implements ITask{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("----任务开始调度----");
		MovieAPIService ser = new MovieAPIService();
		ser.saveTodayMovies();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		System.out.println("----任务结束调度----");
	}

}
