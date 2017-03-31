package org.zky.tool.magnetsearch.utils.http;


/**
 * @author Zhang
 *   2015-1-16
 *  登陆返回信息
 */
public class LoginState {
	public String message;
	public String statusCode;

	@Override
	public String toString() {
		return "LoginState{" +
				"message='" + message + '\'' +
				", statusCode='" + statusCode + '\'' +
				'}';
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
