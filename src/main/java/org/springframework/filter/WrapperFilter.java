/**
 * File Name:CharacterFilter.java
 * Package Name:org.springframework.filter
 * Date:2016��11��12������11:33:54
 * Function: ��̬��Ӳ�������װ�� <br/>
 * Copyright (c) 2016, ��������֪�пƼ����޹�˾.
 *
 */
package org.springframework.filter;

/**
 * ClassName: WrapperFilter <br/>
 * Function:����filter��̬����Ӳ��� <br/>
 * date: 2016��11��12�� ����11:33:54 <br/>
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
 * ʹ�ô˹��������reqeust/response��������
 * 
 * @author ��¡��
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
		req.setCharacterEncoding("utf-8");// ���request����
		resp.setContentType("text/html;charset=utf-8"); // ���response����
		if ("get".equalsIgnoreCase(request.getMethod())) {
			WrapperServlet wapperServlet = new WrapperServlet(request); // ʹ�ð�װ��request
			request = wapperServlet;
			chain.doFilter(request, response);// ����
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
 * Function:��װ��Servlet <br/>
 * Reason:��̬��������������servlet����<br/>
 * date: 2016��11��12�� ����12:12:17 <br/>
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
		String value = super.getParameter(name);// ���ñ���װ����ķ���
		if (value != null) {
			value = convertCharacter(value);// ת���ַ�
		}
		return value;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		//Map<String, String[]> map = super.getParameterMap();
		//������д���map�ſ���
		Map<String, String[]> map = new HashMap(super.getParameterMap());
		map.put("path", new String[]{"teacher"});
		if (map != null) {
			// ����ת���ַ�
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
			// ����ת���ַ�
			for (String value : values) {
				value = convertCharacter(value);
			}
		}
		return values;
	}


}
