package com.lxd.atcrowfunding.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lxd.atcrowdfunding.bean.User;

public class LoginInterceptor implements HandlerInterceptor {

	/**
	 * �ڿ�����ִ��֮ǰ���ҵ���߼�����
	 * �����ķ���ֵ�����߼��Ƿ񣬼���ִ�У�true����ʾ����ִ�У�false��ʾ���ټ���ִ��
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		User loginUser = (User)session.getAttribute("loginUser");
		
		if(loginUser == null) {
			String path = session.getServletContext().getContextPath();
			response.sendRedirect(path+"/login");//�ض���
			return false;
		}else {
			return false;
		}
		
	}

	/**
	 * �ڿ�����ִ�����֮����߼�����
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * �����ͼ��Ⱦ֮��ִ�д˷���
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
