package demo.tcp.http;

import io.netty.handler.codec.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class HttpModel {

	private HttpMethod httpMethod;
	private String uri;
	private Map<String, String> parameterMap = new  HashMap<String, String>();
	
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public void putParameter(String key, String value){
		parameterMap.put(key, value);
	}
	
	public String getParameter(String key){
		return parameterMap.get(key);
	}
	
	public boolean isParameterEmpty(){
		return parameterMap.size() > 0;
	}
	
}
