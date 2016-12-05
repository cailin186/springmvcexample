package org.springframework.samples.mvc.apigateway.exception;

import java.util.HashMap;
import java.util.Map;

import com.talk51.common.mapper.JsonMapper;

public class ApiGateWayError {
	private static final long serialVersionUID = 1L;

	public static final String CODE_APPID_EMPTY = "20020";// app_id为空

	public static final String CODE_JSONDATA_EMPTY = "20021";// json_data为空

	public static final String CODE_QUERY_OPEN_CLASS_EXCEPTION = "20022";// query_openclass_list
																			// 接口异常

	public static final String CODE_GET_USERINFO_EXCEPTION = "20023";// get_userinfo
																		// 接口异常

	public static final String CODE_TOKEN_SUCCESS = "10000";

	public static final String CODE_BAD_CLIENT_CREDENTIALS = "20001";// oauth2
																		// bad
																		// client
																		// credentials

	public static final String CODE_TOKEN_EXPIRED = "20002"; // oauth2 token过期

	public static final String CODE_INVALID_TOKEN = "20003"; // oauth2 无效token

	public static final String CODE_UNAUTHROIZED = "20004"; // oauth2 未认证

	public static final String APP_NOT_NULL = "20005"; // app不能为空

	public static final String API_GATE_WAY_ERROR = "20006"; // apigateway error

	public static final String APP_HOST_IS_NULL = "20007"; // app不能为空

	public static final String MSG_TOKEN_SUCCESS = "get token success";

	public static final String MSG_APPID_EMPTY = "app_id empty option";

	public static final String MSG_JSONDATA_EMPTY = "json_data empty option";

	public static final String MSG_UNAUTHROIZED = "unauthorized";

	public static final String MSG_QUERY_OPEN_CLASS_EXCEPTION = "query_openclass_list interface exception";

	public static final String MSG_GET_USERINFO_EXCEPTION = "get_userinfo interface exception";

	public static final String MSG_APP_NOT_NULL = "parameters [app_name] can't be null or empty"; // app不能为空

	public static final String MSG_API_GATEWAY_ERROR = "api gate way error"; // app不能为空
	
	public static final String MSG_API_GATEWAY_HOST_NULL = "api host is null or empty"; // app不能为空

	public ApiGateWayError() {
		super();
	}

	public ApiGateWayError(String errorMsg) {
		super();
		this.errorMsg = errorMsg;
	}

	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public static String toErrorMessage(String errorCode) {
		Map<String, String> map = new HashMap();
		switch (errorCode) {
		case ApiGateWayError.CODE_APPID_EMPTY:
			map.put("code", errorCode);
			map.put("message", MSG_APPID_EMPTY);
			return JsonMapper.toJsonString(map);
		case ApiGateWayError.CODE_JSONDATA_EMPTY:
			map.put("code", errorCode);
			map.put("message", MSG_JSONDATA_EMPTY);
			return JsonMapper.toJsonString(map);
		default:
			return JsonMapper.toJsonString(map);

		}
	}

}
