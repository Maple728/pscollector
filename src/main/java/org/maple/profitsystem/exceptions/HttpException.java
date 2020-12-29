package org.maple.profitsystem.exceptions;

public class HttpException extends Exception{

	private static final long serialVersionUID = -6638125900771022308L;
	
	private String url;
	
	private int retryTimes = 0;
	
	private String method = null;
	
	private String errorMsg = null;
	
	public HttpException(String url, String method, int retryTimes, String errorMsg) {
		this.url = url;
		this.method = method;
		this.retryTimes = retryTimes;
		this.errorMsg = errorMsg;
	}
	
	@Override
	public String getMessage() {
		return "Fail:" + method + " " + url + " after retry " + retryTimes + " times: " + errorMsg;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
