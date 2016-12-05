/**
 * Project Name:spring-mvc-showcase
 * File Name:LogFilter.java
 * Package Name:org.springframework.filter
 * Date:2016年11月12日上午11:23:47
 * Function: TODO ADD FUNCTION.(这里用一句话描述这个类的作用) <br/>
 * Copyright (c) 2016, 北京大生知行科技有限公司.
 *
 */
package org.springframework.filter;

/**
 * ClassName: LogFilter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016年11月12日 上午11:23:47 <br/>
 * @author Administrator
 * @version 

 */
import javax.servlet.*;
import java.util.*;

//实现 Filter 类
public class LogFilter implements Filter  {
	public void  init(FilterConfig config) throws ServletException {
		// 获取初始化参数
		String site = config.getInitParameter("Site"); 
		// 输出初始化参数
		System.out.println("网站名称: " + site); 
	}
	public void  doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {
		
		
		
		
		// 把请求传回过滤链
		chain.doFilter(request,response);
	}
	public void destroy( ){
		/* 在 Filter 实例被 Web 容器从服务移除之前调用 */
	}
}
