package com.yven.web;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yven.entity.LoginCommand;
import com.yven.entity.User;
import com.yven.service.RateLimiterService;
import com.yven.service.UserService;
import com.yven.test.HttpClientTest;

@Controller
public class LoginController {
	private static Logger logger = Logger.getLogger(LoginController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private RateLimiterService rateLimiterService;

	@RequestMapping(value = "/index.html")
	public String loginPage() {
		return "login";
	}
	
	@RequestMapping(value = "/loginCheck.html")
	public ModelAndView loginCheck(HttpServletRequest request, LoginCommand loginCommand){
		boolean isVaildUser = userService.hasMatchUser(loginCommand.getUsername(), loginCommand.getPassword());
		if(!isVaildUser){
			return new ModelAndView("login", "error", "用户名或密码错误");
		}else{
			User user = userService.findUserByUsername(loginCommand.getUsername());
			user.setLastIp(request.getLocalAddr());
			user.setLastVisit(new Date());
			userService.loginSuccess(user);
			request.getSession().setAttribute("user", user);
			return new ModelAndView("main");
		}
	}
	// 服务端计数
	AtomicInteger count = new AtomicInteger(0);
	
	/**
	 * 计数器清0
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/clearCount.html")
	@ResponseBody
	public String clearCount(HttpServletRequest request,HttpServletResponse response) {
		
		String setCount  = request.getParameter("setCount");
		if(setCount.equals("000")){
			count.set(0);
		}
		return "count = "+count;
		
	}
	
	/**
	 * 业务模拟
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/limiter.html")
	@ResponseBody
	public String testLimiter(HttpServletRequest request,HttpServletResponse response) {
		rateLimiterService.create(10);
		String message = "message";
		// 阻塞的方式
		double waitTime = rateLimiterService.acquire(); // 阻塞等待，获取令牌
		message = waitTime+"";
		
		// 非阻塞的方式
//		if(rateLimiter.tryAcquire()) { //未请求到limiter则立即返回false
//			message = "请求成功！";
//		}else{
//			message = "请求失败，请稍后再试！";
//		}
		logger.info("count="+count.incrementAndGet()+" --wait time: "+message);
		//System.out.println(("server doing somethig "+System.currentTimeMillis()));
		return message;
		
	}
}
