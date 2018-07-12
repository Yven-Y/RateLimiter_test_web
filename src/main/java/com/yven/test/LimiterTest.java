package com.yven.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.common.util.concurrent.RateLimiter;

public class LimiterTest {
	
	private static Logger logger = Logger.getLogger(LimiterTest.class);


	static void submitTasks(List<Runnable> tasks, Executor executor) {
		RateLimiter rateLimiter = RateLimiter.create(10.0);// 每秒产生的令牌数量
		for (Runnable task : tasks) {
			// 阻塞的方式
			double waitTime = rateLimiter.acquire(5); // may wait  阻塞等待，获取令牌
			logger.info("wait time = "+waitTime);
			// 非阻塞的方式
			/*if(rateLimiter.tryAcquire()) { //未请求到limiter则立即返回false
				doSomething();
			}else{
				doSomething();
			}*/
			//执行任务
			executor.execute(task);

		}

	}
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		logger.info("begin===========");
		List<Runnable> taskList = new ArrayList<Runnable>();
		int threadCount = 15;
		for (int i = 0; i < threadCount; i++) {
			taskList.add(new Task());
		}
		ExecutorService es = Executors.newCachedThreadPool();
		submitTasks(taskList, es);
		logger.info("end================"+(System.currentTimeMillis() - startTime));
	}
	
		@Test
		public void testRateLimiter() throws InterruptedException{
			RateLimiter limiter = RateLimiter.create(4);
			double waitTime = limiter.acquire(1); // 获取1令牌
			logger.info("wait time = "+waitTime);// 0.0

			Thread.sleep(1000);
			 waitTime = limiter.acquire(10); //  获取10令牌,预消费3个令牌
			logger.info("wait time = "+waitTime);// 0.0
			
			Thread.sleep(1000);
			 waitTime = limiter.acquire(1); //  获取1令牌，为之前预消费的3个令牌埋单
			logger.info("wait time = "+waitTime);// 0.75
			
			
		}
		
}
	class Task implements Runnable{
		private static Logger logger = Logger.getLogger(Task.class);
		@Override
		public void run() {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Auto-generated method stub
			logger.info("woking...");
		}
		
		
}
