/**
 * File Name:CharacterFilter.java
 * Package Name:org.springframework.filter
 * Date:2016年11月12日上午11:33:54
 * Function: 动态添加参数给包装器 <br/>
 * Copyright (c) 2016, 北京大生知行科技有限公司.
 *
 */
package org.springframework.filter;

/**
 * ClassName: WrapperFilter <br/>
 * Function:采用filter动态的添加参数 <br/>
 * date: 2016年11月12日 上午11:33:54 <br/>
 * @author cailin
 * @version 

 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * 使用此过滤器解决reqeust/response乱码问题
 * 
 * @author 马隆博
 */
public class WrapperFilter implements Filter {
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		req.setCharacterEncoding("utf-8");// 解决request乱码
		resp.setContentType("text/html;charset=utf-8"); // 解决response乱码
		if ("get".equalsIgnoreCase(request.getMethod())) {
			WrapperServlet wapperServlet = new WrapperServlet(request); // 使用包装的request
			request = wapperServlet;
			chain.doFilter(request, response);// 放行
			return;
		}
		doFilter(request, response, chain);
	}

	@Override
	public void destroy() {
	}
}

/**
 * 
 * ClassName: WrapperServlet <br/>
 * Function:包装器Servlet <br/>
 * Reason:动态的添加请求参数给servlet对象<br/>
 * date: 2016年11月12日 下午12:12:17 <br/>
 * @author cailin
 */
class WrapperServlet<E> extends HttpServletRequestWrapper {
	public WrapperServlet(HttpServletRequest request) {
		super(request);
	}


	@SuppressWarnings("finally")
	private String convertCharacter(String value) {
		String resultValue = value;
		try {
			resultValue = new String(value.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			return resultValue;
		}
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);// 调用被包装对象的方法
		if (value != null) {
			value = convertCharacter(value);// 转换字符
		}
		return value;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		//Map<String, String[]> map = super.getParameterMap();
		//必须重写这个map才可以
		Map<String, String[]> map = new HashMap(super.getParameterMap());
		map.put("path", new String[]{"teacher"});
		if (map != null) {
			// 遍历转换字符
			for (String[] values : map.values()) {
				for (String value : values) {
					value = convertCharacter(value);
				}
			}

		}
		return map;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values != null) {
			// 遍历转换字符
			for (String value : values) {
				value = convertCharacter(value);
			}
		}
		return values;
	}


}
