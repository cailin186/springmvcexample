package org.springframework.apigateway.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.http.NameValuePair;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * http的异步调用类、get post两种请求方式
 * @author wangbaoyu
 * @version 
 * @since JDK 1.6
 */
public class AsyncHttpClientUtils {

	private static Logger logger = LoggerFactory.getLogger(AsyncHttpClientUtils.class);
	
	public static Future<HttpResponse> asyncGet(String url) throws IOException {
		HttpGet request = new HttpGet(url);
		return execHttpRequest(url, request);
	}

	private static Future<HttpResponse> execHttpRequest(String url, HttpUriRequest request) throws IOException {
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		try {
			httpclient.start();
			return httpclient.execute(request, null);
		} catch (Exception e) {
			httpclient.close();
		}
		return null;
	}

	  public static Future<HttpResponse> asyncPost(String url, Map<String, String> params) throws IOException 
	  { 
			CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();	
			try {
				List<NameValuePair> paramList = new ArrayList <NameValuePair>();			
				Set<String> keySet = params.keySet();
				for(String key : keySet) 
				{			
					paramList.add(new BasicNameValuePair(key, params.get(key)));
				}				
				HttpPost post = new HttpPost(url);			
				post.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
		        //发送请求并返回future		
				httpclient.start();
				return httpclient.execute(post, null);
			} catch (Exception e) {
				logger.error("ApiGateWay-AsyncHttpClientUtils-asyncPost exception:"+e.getMessage());
				e.printStackTrace();
			}
			finally
			{
				httpclient.close();
			}
			return null;
	    }
	  
	public static void main(String[] args) throws Exception {


	}
}
