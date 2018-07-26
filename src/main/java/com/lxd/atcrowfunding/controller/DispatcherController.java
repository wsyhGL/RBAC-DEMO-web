package com.lxd.atcrowfunding.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.chainsaw.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lxd.atcrowdfunding.bean.AJAXResult;
import com.lxd.atcrowdfunding.bean.Permission;
import com.lxd.atcrowdfunding.bean.User;
import com.lxd.atcrowdfunding.service.PermissionService;
import com.lxd.atcrowdfunding.service.UserService;

@Controller
public class DispatcherController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		//session.removeAttribute("loginUser");
		session.invalidate();//sessionʧЧ 
		return "redirect:/login";//������ĵ�ַָ���½ҳ��
	}
	@RequestMapping("/main")
	public String main() {
		return "main";
	}
	
	@ResponseBody
	@RequestMapping("/doAJAXLogin")
	public Object doAJAXResult(User user,HttpSession session) {
		AJAXResult result = new AJAXResult();
		User dbUser = userService.query4Login(user);
		if(dbUser!=null) {
			session.setAttribute("loginUser", dbUser);
			
			//��ȡ�û�Ȩ����Ϣ
			List<Permission> permissions = permissionService.queryPermissionsByUsers(dbUser);
			Map<Integer, Permission> permissionMap = new HashMap<>();
			Permission root = null;
			Set<String> uriSet = new HashSet<>();
			for(Permission permission: permissions) {
				permissionMap.put(permission.getId(), permission);
				if(permission.getUrl() != null && !"".equals(permission.getUrl())) {
					uriSet.add(session.getServletContext().getContextPath()+permission.getUrl());
				}
			}
			for(Permission permission : permissions) {
				Permission child = permission;
				if(permission.getPid() == 0) {
					root = permission;
				}else {
					Permission parent = permissionMap.get(child.getPid());
					parent.getChildren().add(child);
				}
			}
			session.setAttribute("rootPermission", root);
		
			result.setSuccess(true);
		}else {
			result.setSuccess(false);
		}
		return result;
	}
	
	@RequestMapping("/doLogin")
	public String doLogin(User user,Model model) throws Exception {
		
		//String loginacct = user.getLoginacct();
		//��������ַ���������ķ�ʽת��Ϊԭʼ���ֽ�������
		//byte[] bs = loginacct.getBytes("ISO8859-1");
		
		//loginacct = new String(bs, "UTF-8");
		//��ȡ������
		//HttpServletRequest
		//2.�ڷ����б������ӱ���Ӧ�Ĳ�����������ͬ
		//3.�������ݷ�װΪʵ����� 
		
		//��ѯ�û���Ϣ
		System.out.println(user);
		User dbuser = userService.query4Login(user);
		System.out.println(dbuser);
		//�ж��û���Ϣ�Ƿ����
		if(dbuser!=null) {
			
			return "main";
		}else {
			String errorMsg = "��½�˺Ż����벻��ȷ������������";
			model.addAttribute("errorMsg", errorMsg);
			return "redirect:/login";//�ض���ķ�ʽ
		}
		
	}

}
