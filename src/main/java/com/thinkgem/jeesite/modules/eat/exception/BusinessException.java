package com.thinkgem.jeesite.modules.eat.exception;

import com.thinkgem.jeesite.modules.eat.bean.Response;

public class BusinessException extends RuntimeException {
	private int code;
	private Object obj;

	public BusinessException() {
		super("操作失败");
		this.code = Response.UNKNOW;
	}

	public BusinessException(String string) {
		super(string);
		this.code = Response.UNKNOW;
	}

	public BusinessException(String msg, int code, Object obj) {
		super(msg);
		this.code = code;
		this.obj = obj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6325312867695822268L;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
