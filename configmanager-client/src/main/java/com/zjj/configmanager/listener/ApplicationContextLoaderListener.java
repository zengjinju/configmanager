package com.zjj.configmanager.listener;

import com.zjj.configmanager.manager.ConfigerLoader;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * Created by admin on 2017/12/6.
 */
public class ApplicationContextLoaderListener extends ContextLoaderListener {

	public ApplicationContextLoaderListener(){}

	public ApplicationContextLoaderListener(WebApplicationContext context){
		super(context);
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		//获取AppName,如果没有配置直接报错(web.xml中的display-name)
		String appName=event.getServletContext().getServletContextName();
		if(appName==null||"".equals(appName)){
			throw new ApplicationContextException("appName is null pleans config appName in web.xml");
		}
		//加载配置文件
		ConfigerLoader configerLoader=ConfigerLoader.getInstance(appName);
		configerLoader.loadAndSaveConfig();
		WebApplicationContext wcc=initWebApplicationContext(event.getServletContext());
		this.customizeContext(event.getServletContext(),(ConfigurableWebApplicationContext) wcc);
	}

	@Override
	protected void customizeContext(ServletContext sc, ConfigurableWebApplicationContext wac) {
		StringBuilder locationBuilder=new StringBuilder();
		String locations=sc.getInitParameter(CONFIG_LOCATION_PARAM);
		locationBuilder.append(locations);
		locationBuilder.append(",");
		locationBuilder.append("classpath*:spring-config.xml");
		wac.setConfigLocation(locationBuilder.toString());
		super.customizeContext(sc, wac);
	}
}
