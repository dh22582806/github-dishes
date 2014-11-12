package com.thinkgem.jeesite.modules.sys.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.modules.eat.bean.Response;

@Service
public class CommonFormAuthenticationFilter extends FormAuthenticationFilter {

	private static final Logger log = LoggerFactory
			.getLogger(CommonFormAuthenticationFilter.class);

	/*
	 * 主要是针对登入成功的处理方法。对于请求头是AJAX的之间返回JSON字符串。
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		if (!isAjax((HttpServletRequest) request)) {// 不是ajax请求
			issueSuccessRedirect(request, response);
		} else {
			Response res = Response.success();
			printJson(response, res);
		}
		return false;
	}

	@Override
	protected boolean isLoginRequest(ServletRequest request,
			ServletResponse response) {
		if (pathsMatch("/api/doLogin", request)) {
			return true;
		}
		return super.isLoginRequest(request, response);
	}

	/**
	 * 主要是处理登入失败的方法
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		if (!isAjax((HttpServletRequest) request)) {// 不是ajax请求
			setFailureAttribute(request, e);
			return true;
		}
		String message = e.getClass().getSimpleName();
		if ("IncorrectCredentialsException".equals(message)) {
			printJson(response, Response.failure("密码错误"));
		} else if ("UnknownAccountException".equals(message)) {
			printJson(response, Response.failure("密码错误"));
		} else if ("LockedAccountException".equals(message)) {
			printJson(response, Response.failure("账号被锁定"));
		} else {
			printJson(response, Response.failure("未知错误"));
		}
		return false;
	}

	/**
	 * 所有请求都会经过的方法。
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				if (log.isTraceEnabled()) {
					log.trace("Login submission detected.  Attempting to execute login.");
				}
				return executeLogin(request, response);
			} else {
				if (log.isTraceEnabled()) {
					log.trace("Login page view.");
				}
				if (isAjax((HttpServletRequest) request)) {// 不是ajax请求
					printJson(response,
							Response.failure(Response.NOTLOGIN, null));
					return false;
				}
				return true;
			}
		} else {
			if (log.isTraceEnabled()) {
				log.trace("Attempting to access a path which requires authentication.  Forwarding to the "
						+ "Authentication url [" + getLoginUrl() + "]");
			}
			if (!isAjax((HttpServletRequest) request)) {// 不是ajax请求
				saveRequestAndRedirectToLogin(request, response);
			} else {
				printJson(response, Response.failure(Response.NOTLOGIN, null));
			}
			return false;
		}
	}

	/**
	 * 是否ajax请求
	 * 
	 * @param request
	 * @return
	 */
	private static boolean isAjax(HttpServletRequest request) {
		if (!"XMLHttpRequest".equalsIgnoreCase(request
				.getHeader("X-Requested-With"))) {// 不是ajax请求
			return false;
		}
		return true;
	}

	/**
	 * 打印json
	 * 
	 * @param response
	 * @param obj
	 */
	private static void printJson(ServletResponse response, Object obj) {
		response.setCharacterEncoding("UTF-8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(JsonMapper.toJsonString(obj));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 */
	public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;

	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	protected AuthenticationToken createToken(ServletRequest request,
			ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		if (password == null) {
			password = "";
		}
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		String captcha = getCaptcha(request);
		return new UsernamePasswordToken(username, password.toCharArray(),
				rememberMe, host, captcha);
	}
}