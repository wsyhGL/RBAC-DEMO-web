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
		session.invalidate();//session失效 
		return "redirect:/login";//浏览器的地址指向登陆页面
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
			
			//获取用户权限信息
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
		//将乱码的字符串按错误的方式转换为原始的字节码序列
		//byte[] bs = loginacct.getBytes("ISO8859-1");
		
		//loginacct = new String(bs, "UTF-8");
		//获取表单数据
		//HttpServletRequest
		//2.在方法列表中增加表单对应的参数，名称相同
		//3.将表单数据封装为实体对象 
		
		//查询用户信息
		System.out.println(user);
		User dbuser = userService.query4Login(user);
		System.out.println(dbuser);
		//判断用户信息是否存在
		if(dbuser!=null) {
			
			return "main";
		}else {
			String errorMsg = "登陆账号或密码不正确，请重新输入";
			model.addAttribute("errorMsg", errorMsg);
			return "redirect:/login";//重定向的方式
		}
		
	}

}
