package com.lxd.atcrowfunding.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerStartupListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
		

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		//��webӦ��·�����浽application��Χ��
				ServletContext application = arg0.getServletContext();
				String path = application.getContextPath();
				application.setAttribute("APP_PATH", path);

	}

}
