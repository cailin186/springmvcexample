/**
 * Project Name:spring-mvc-showcase
 * File Name:LogFilter.java
 * Package Name:org.springframework.filter
 * Date:2016��11��12������11:23:47
 * Function: TODO ADD FUNCTION.(������һ�仰��������������) <br/>
 * Copyright (c) 2016, ��������֪�пƼ����޹�˾.
 *
 */
package org.springframework.filter;

/**
 * ClassName: LogFilter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(��ѡ). <br/>
 * date: 2016��11��12�� ����11:23:47 <br/>
 * @author Administrator
 * @version 

 */
import javax.servlet.*;
import java.util.*;

//ʵ�� Filter ��
public class LogFilter implements Filter  {
	public void  init(FilterConfig config) throws ServletException {
		// ��ȡ��ʼ������
		String site = config.getInitParameter("Site"); 
		// �����ʼ������
		System.out.println("��վ����: " + site); 
	}
	public void  doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException, ServletException {
		
		
		
		
		// �����󴫻ع�����
		chain.doFilter(request,response);
	}
	public void destroy( ){
		/* �� Filter ʵ���� Web �����ӷ����Ƴ�֮ǰ���� */
	}
}
