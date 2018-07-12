package com.yven.test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
 
/**
 * 模拟客户端请求测试
 * @author Administrator
 *
 */
public class HttpClientTest {
	
	private static Logger logger = Logger.getLogger(HttpClientTest.class);
	
	public static void main(String[] args) throws InterruptedException {
		setInfo();// 请求服务器计数器清0
		long startTime = System.currentTimeMillis();
		//模拟高并发 
		int threadCount = 15;
		for (int i = 0; i < threadCount; i++) {
		
				new Thread(){
					public void run() {
							while(true){
									try {
										getInfo(startTime);
										Thread.sleep(5000);// 5秒一次高并发
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
							}
					};
					
				}.start();
			}
		Thread.sleep(1000);// 
		// 模拟正常情况
		int threadCount2 = 5;
		for (int i = 0; i < threadCount2; i++) {
		
				new Thread(){
					public void run() {
							while(true){
									try {
										getInfo(startTime);
										Thread.sleep((long) (1000 * Math.random()));
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
							}
					};
					
				}.start();
			}
			

		
	}
	
public static  void getInfo(long startTime) {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// 创建httpget.  
			/*HttpGet httpget = new HttpGet("http://www.baidu.com/");*/
			HttpGet httpget = new HttpGet("http://182.61.46.233:8080/RateLimiter_test_web-0.0.1/limiter.html");
//			HttpGet httpget = new HttpGet("http://localhost:8080/druid_test_web/limiter.html");
			// 设置编码格式
			httpget.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
			httpget.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));

			logger.info("beging--------------"+"------------------------");
			logger.info("executing request " + httpget.getURI());
			// 执行get请求.  
			CloseableHttpResponse response = httpclient.execute(httpget);
			
			try {
				// 获取响应实体  
				HttpEntity entity = response.getEntity();
				
				// 打印响应状态  
				System.out.println(response.getStatusLine());
				if (entity != null) {
					// 打印响应内容长度  
					logger.info("Response content length: " + entity.getContentLength());
					// 打印响应内容  
					logger.info("Response content: " + EntityUtils.toString(entity, "utf-8"));
				}
				logger.info("end------------------------------------");
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("退出用时时间："+(System.currentTimeMillis() - startTime));
			e.printStackTrace();
			System.exit(1);// 连接失败，退出程序
		} finally {
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	public static void setInfo() {
		// 创建默认的httpClient实例.  
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost  
		HttpPost httppost = new HttpPost("http://182.61.46.233:8080/RateLimiter_test_web-0.0.1/clearCount.html");
		// 创建参数队列  
		List formparams = new ArrayList();
		formparams.add(new BasicNameValuePair("setCount", "000"));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					logger.info("Response content: " + EntityUtils.toString(entity, "UTF-8"));
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
 
	@Test
	public void jUnitTest() {
		int requestCount = 10000;
		System.out.println("测试开始");
		long start = System.currentTimeMillis();
		for (int i = 0; i < requestCount; i++) {
			get();
		}
		System.out.println("测试结束"+(System.currentTimeMillis() - start) / 1000 +" 秒");
	}
 
	/**
	 * post方式提交表单（模拟用户登录请求）
	 */
	@Test
	public void postForm() {
		// 创建默认的httpClient实例.  
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost  
		HttpPost httppost = new HttpPost("http://localhost:8080/druid_test_web/loginCheck.html");
		// 创建参数队列  
		List formparams = new ArrayList();
		formparams.add(new BasicNameValuePair("username", "abc"));
		formparams.add(new BasicNameValuePair("password", "123456"));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					System.out.println("--------------------------------------");
					System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
					System.out.println("--------------------------------------");
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 
	/**
	 * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
	 */
	@Test
	public void post() {
		// 创建默认的httpClient实例.  
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// 创建httppost  
		HttpPost httppost = new HttpPost("http://localhost:8080/druid_test_web/index.html");
		// 创建参数队列  
		List formparams = new ArrayList();
		formparams.add(new BasicNameValuePair("type", "house"));
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			System.out.println("executing request " + httppost.getURI());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					System.out.println("--------------------------------------");
					System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
					System.out.println("--------------------------------------");
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 
	/**
	 * 发送 get请求
	 */
	@Test
	public  void get() {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// 创建httpget.  
			/*HttpGet httpget = new HttpGet("http://www.baidu.com/");*/
			HttpGet httpget = new HttpGet("http://182.61.46.233:8080/druid_test_web-0.0.1-SNAPSHOT/limiter.html");
//			HttpGet httpget = new HttpGet("http://localhost:8080/druid_test_web/limiter.html");
			// 设置编码格式
			httpget.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
			httpget.setHeader(new BasicHeader("Accept", "text/plain;charset=utf-8"));

			System.out.println("beging--------------"+"------------------------");
			System.out.println("executing request " + httpget.getURI());
			// 执行get请求.  
			CloseableHttpResponse response = httpclient.execute(httpget);
			
			try {
				// 获取响应实体  
				HttpEntity entity = response.getEntity();
				
				// 打印响应状态  
				System.out.println(response.getStatusLine());
				if (entity != null) {
					// 打印响应内容长度  
					System.out.println("Response content length: " + entity.getContentLength());
					// 打印响应内容  
					System.out.println("Response content: " + EntityUtils.toString(entity, "utf-8"));
				}
				System.out.println("end------------------------------------");
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
 
	/**
	 * 上传文件
	 */
	public void upload() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost("http://localhost:8080/druid_test_web/Ajax/serivceFile.action");
 
			FileBody bin = new FileBody(new File("F:\\image\\sendpix0.jpg"));
			StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
 
			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("bin", bin).addPart("comment", comment).build();
 
			httppost.setEntity(reqEntity);
 
			System.out.println("executing request " + httppost.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				System.out.println("----------------------------------------");
				System.out.println(response.getStatusLine());
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					System.out.println("Response content length: " + resEntity.getContentLength());
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
