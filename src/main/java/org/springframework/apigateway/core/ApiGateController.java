package org.springframework.apigateway.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.apigateway.exception.ApiGateWayError;
import org.springframework.stereotype.Controller;
import org.springframework.util.PropertiesUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.talk51.common.mapper.JsonMapper;

@Controller
public class ApiGateController {


	/**
	 * proxy:默认透明代理方式
	 * @author cailin
	 * Date:2016年11月15日下午7:42:08
	 * @param request 通过request获取请求参数,请求URI
	 * @return 目标系统的执行结果
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@RequestMapping(value = "/gate/**")
	public @ResponseBody String proxy(HttpServletRequest request)
			throws IOException, InterruptedException, ExecutionException {
		String path = request.getParameter("app_name");
		if (!StringUtils.isNotEmpty(path)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("code", ApiGateWayError.APP_NOT_NULL);
			map.put("message", ApiGateWayError.MSG_APP_NOT_NULL);
			return JsonMapper.toJsonString(map);
		}
		String appHost = PropertiesUtil.getProperty("routes." + path + ".url");
		if (!StringUtils.isNotEmpty(appHost)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("code", ApiGateWayError.APP_HOST_IS_NULL);
			map.put("message", ApiGateWayError.MSG_API_GATEWAY_HOST_NULL);
			return JsonMapper.toJsonString(map);
		}
		String requestURI = request.getRequestURI();// http://ip:port/url?param=a&param=b中的url
		if (requestURI != null && !"".equalsIgnoreCase(requestURI) && requestURI.indexOf("gate") > 0) {
			requestURI = requestURI.substring(requestURI.indexOf("gate") + 4);
		}
		String host = appHost + requestURI;
		HttpResponse response = null;
		// 参数拼装
		Map<String, String[]> paramMap = request.getParameterMap();
		Map<String, String> params = convertMap(paramMap);
		String method = request.getMethod();
		if (method.equalsIgnoreCase("get")) {
			String proxyurl = host + "?" + convertParameters(params);
			Future<HttpResponse> future = AsyncHttpClientUtils.asyncGet(proxyurl);
			response = future.get();
		} else {
			Future<HttpResponse> future = AsyncHttpClientUtils.asyncPost(host, params);
			response = future.get();
		}
		return IOUtils.toString(response.getEntity().getContent());

	}
	
    /**
     * 
     * excute:根据请求头中的方法类型，异步调用目标系统
     * @author cailin
     * Date:2016年11月15日下午7:36:23
     * @param applicationHost 实际系统的访问地址  http://targethost/path 不含参数
     * @param params request请求头中的参数map
     * @param method request请求中的方法类型 get或者post
     * @return 返回目标系统的json数据
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
	private HttpResponse excute(String applicationHost,Map<String, String> params, String method)
			throws IOException, InterruptedException, ExecutionException {
		HttpResponse response;
		if (method.equalsIgnoreCase("get")) {
			String proxyurl = applicationHost + "?" + convertParameters(params);
			Future<HttpResponse> future = AsyncHttpClientUtils.asyncGet(proxyurl);
			response = future.get();
		} else {
			Future<HttpResponse> future = AsyncHttpClientUtils.asyncPost(applicationHost, params);
			response = future.get();
		}
		return response;
	}

	/**
	 * callable:异步调用接口
	 *  http://proxydomain/asyn/targetpath/?param=value[..];
	 * @author cailin Date:2016年11月11日下午8:22:09
	 * @param request
	 * @return
	 */
	@RequestMapping("/asyn/**")
	public @ResponseBody Callable<String> callable(HttpServletRequest request) {
		String path = request.getParameter("app_name");
		if (!StringUtils.isNotEmpty(path)) {
			return new Callable<String>() {
				@Override
				public String call() throws Exception {
					Map<String, String> map = new HashMap<String, String>();
					map.put("code", ApiGateWayError.APP_NOT_NULL);
					map.put("message", ApiGateWayError.MSG_APP_NOT_NULL);
					return JsonMapper.toJsonString(map);
				}
			};
		}
		String appHost = PropertiesUtil.getProperty("routes." + path + ".url");
		String requestURI = request.getRequestURI();// http://ip:port/url?param=a&param=b中的url
		if (requestURI != null && !"".equalsIgnoreCase(requestURI) && requestURI.indexOf("asyn") > 0) {
			requestURI = requestURI.substring(requestURI.indexOf("asyn") + 4);
		}
		String host = appHost + requestURI;
		if (!StringUtils.isNotEmpty(appHost)) {
			return new Callable<String>() {
				@Override
				public String call() throws Exception {
					Map<String, String> map = new HashMap<String, String>();
					map.put("code", ApiGateWayError.APP_HOST_IS_NULL);
					map.put("message", ApiGateWayError.MSG_API_GATEWAY_HOST_NULL);
					return JsonMapper.toJsonString(map);
				}
			};
		}
		// 参数拼装
		Map<String, String[]> paramMap = request.getParameterMap();
		Map<String, String> params = convertMap(paramMap);
		final String method = request.getMethod();
		return new Callable<String>() {
			@Override
			public String call() throws Exception {
				HttpResponse response = null;
				if (method.equalsIgnoreCase("get")) {
					String proxyurl = host + "?" + convertParameters(params);
					Future<HttpResponse> future = AsyncHttpClientUtils.asyncGet(proxyurl);
					response = future.get();
				} else {
					Future<HttpResponse> future = AsyncHttpClientUtils.asyncPost(host, params);
					response = future.get();
				}
				return IOUtils.toString(response.getEntity().getContent());
			}
		};
	}

	@RequestMapping("/group/**")
	/**
	 * http://proxydomain/group?url1=;url2=;url3=;
	 * group:通过聚合接口实现接口数据的聚合，采用guava的future实现接口聚合
	 * 
	 * @author Administrator Date:2016年11月15日下午6:58:30
	 * @param request
	 * @param urls 多个url的聚合
	 * @return 多个url的聚合结果的json
	 */
	public @ResponseBody String group(HttpServletRequest request, String... urls) {
		return "hello";
	}

	public String convertParameters(Map<String, String> parameters) {
		StringBuffer sb = new StringBuffer();
		String urlParameters = "";
		for (Entry<String, String> entry : parameters.entrySet()) {
			sb.append(entry.getKey().toString() + "=" + entry.getValue().toString() + "&");
		}
		urlParameters = sb.toString();
		if (urlParameters != null && urlParameters.length() > 0) {
			urlParameters = urlParameters.substring(0, urlParameters.lastIndexOf("&"));
		}
		return urlParameters;
	}

	public Map<String, String> convertMap(Map<String, String[]> parameters) {
		Map<String, String> returnMap = new LinkedHashMap<>();
		// 转换为Entry
		Set<Map.Entry<String, String[]>> entries = parameters.entrySet();
		// 遍历
		for (Map.Entry<String, String[]> entry : entries) {
			String key = entry.getKey();
			StringBuffer value = new StringBuffer("");
			String[] val = entry.getValue();
			if (null != val && val.length > 0) {
				for (String v : val) {
					value.append(v);
				}
			}
			System.out.println("key=" + key + "value=" + value);
			returnMap.put(key, value.toString());
		}

		return returnMap;
	}

}
