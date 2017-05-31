package com.server;

import java.util.Map;

public class WebServerRequestBody {

	private String method;
	private Map<String, String> requestParams;
	private String requestBody;
	private String requestUri;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(Map<String, String> requestParams) {
		this.requestParams = requestParams;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	@Override
	public String toString() {
		return "WebServerRequestBody{" + "method='" + method + '\'' + ", requestParams=" + requestParams
				+ ", requestBody='" + requestBody + '\'' + ", requestUri='" + requestUri + '\'' + '}';
	}
}
