package com.yven.web;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.yven.entity.User;
import com.yven.service.UserService;

public class InitServlet extends HttpServlet {  
	  
	private final static Logger logger = Logger.getLogger(InitServlet.class);
    /**  
     */  
    private static final long serialVersionUID = 1L;  
  
    @Autowired
    private UserService userService;
    
    @Override  
    public void init(ServletConfig config) {  
        // super.init();  
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());  
        System.out.println("================>[Servlet]自动加载启动开始.");  
        //testFindUser();
        //test();
        System.out.println("================>[Servlet]自动加载启动结束.");  
    }  
    
    /**
     * 
     */
    public void testFindUser(){
    	int count = 10;
    	for (int i = 0; i < count; i++) {
    		long startTime = System.currentTimeMillis();
			User user = (User) userService.findUserByUsername("abc");
			logger.info(user.toString()+" 查询用时："+(System.currentTimeMillis() - startTime)+" 毫秒");
		}
    }
    /**
     * 多线程  druid 压测
     */
    public void test() {
		int countThread = 1000;
		long startTime = System.currentTimeMillis();
		logger.info("程序启动时间：" + startTime);
		for (int i = 0; i < countThread; i++) {
			new Thread() {
				
				long threadStartTime = 0;
				public void run() {
					while (true) {
						try {
							threadStartTime = System.currentTimeMillis();
							logger.info("current thread:" + Thread.currentThread().getName() + " ==threadStartTime:" + threadStartTime);
							User user = (User) userService.findUserByUsername("abc");// 执行查询操作
							logger.info(user.toString());

							Thread.sleep(1000);// 休眠1s
						} catch (Exception e) {
							logger.error(e.getMessage());
							logger.info("current thread:" + Thread.currentThread().getName() + " ==threadRunTime:" + (System.currentTimeMillis() - threadStartTime));
							logger.info("程序运行时间：" + (System.currentTimeMillis() - startTime) / 1000 + " 秒");
							e.printStackTrace();
							System.exit(0);// 退出程序
							
						}
					}
				}
			}.start();
		}

	}
}
