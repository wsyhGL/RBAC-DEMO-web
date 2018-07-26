package com.lxd.atcrowfunding.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lxd.atcrowdfunding.bean.Permission;
import com.lxd.atcrowdfunding.service.PermissionService;

public class AuthInterceptor extends HandlerInterceptorAdapter{
	
	@Autowired
	private PermissionService permissionService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//��ȡ�û��������ַ
		String uri = request.getRequestURI();
		
		String path = request.getSession().getServletContext().getContextPath();
		
		//�жϵ�ǰ·���Ƿ���Ҫ����Ȩ����֤
		//��ѯ������Ҫ��֤��·������
		List<Permission> permissions = permissionService.queryAll();
		Set<String> uriSet = new HashSet<>();
		for(Permission permission : permissions) {
			if(permission.getUrl()!=null && !"".equals(permission.getUrl())) {
				uriSet.add(path+permission.getUrl());
			}
		}
		if(uriSet.contains(uri)) {
			Set<String> authUriSet = (Set<String>)request.getSession().getAttribute("authUriSet");
			if(authUriSet.contains(uri)) {
				return true;
			}else {
				response.sendRedirect(path+"/error");
				return false;
			}
		}else {
			return true;
		}
	}

}
