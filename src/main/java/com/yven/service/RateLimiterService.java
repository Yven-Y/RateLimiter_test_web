package com.yven.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.RateLimiter;

/**
 * RateLimiter服务类
 * @author Administrator
 *
 */
@Service
public class RateLimiterService {
	
	private static Logger logger = Logger.getLogger(RateLimiterService.class);
	
	private static RateLimiter rateLimiter;
	
	/**
	 * 设置令牌数
	 * @param permitsPerSecond
	 * @return
	 */
	public RateLimiter create(double permitsPerSecond){
		rateLimiter = RateLimiter.create(permitsPerSecond);
		//logger.info("success create!");
		return rateLimiter;
	}
	
	public double acquire(){
		return this.acquire(1);
	}

	/**
	 * 阻塞时间
	 * @param permits
	 * @return
	 */
	public double acquire(int permits){
		double waitTime = rateLimiter.acquire(permits);
		//logger.info("waitTime:"+waitTime);
		return waitTime;
	}
}
