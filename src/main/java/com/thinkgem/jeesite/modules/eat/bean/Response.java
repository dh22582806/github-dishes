package com.thinkgem.jeesite.modules.eat.bean;

import java.util.HashMap;
import java.util.Map;

import com.thinkgem.jeesite.modules.eat.exception.BusinessException;

public class Response {
	public static final int SUCCESS = 0;
	public static final int NOTLOGIN = 1;
	public static final int UNKNOW = 9;
	/**
	 * 需要刷新订单状态
	 */
	public static final int CODE_NEEDREFRESH_ORDER = 10;
	private int code = 0;
	private String msg;
	private Object data;

	public static Response success() {
		return success(null);
	}

	public static Response success(Object data) {
		return new Response(SUCCESS, null, data);
	}

	public static Response failure(int code, String msg) {
		return new Response(code, msg, null);
	}

	public static Response failure(String msg) {
		return new Response(UNKNOW, msg, null);
	}

	private Response(int code, String msg, Object data) {
		this.msg = msg;
		this.code = code;
		this.data = data;
	}

	public static Response failure(Throwable throwable) {
		if (throwable instanceof BusinessException) {
			BusinessException busE = (BusinessException) throwable;
			return new Response(busE.getCode(), busE.getMessage(),
					busE.getObj());
		} else {
			return new Response(UNKNOW, throwable.getMessage(), null);
		}
	}

	@SuppressWarnings("unchecked")
	public Response putValue(String key, Object value) {
		if (this.data == null) {
			data = new HashMap<String, Object>();
		}
		if (data instanceof Map) {
			((Map<String, Object>) data).put(key, value);
		}
		return this;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
