package com.zjj.configmanager.remote;

import com.zjj.configmanager.manager.AppEnv;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by admin on 2017/12/2.
 * @author zjj
 */
public class ZookeeperPropertyResourceConfig extends PropertySourcesPlaceholderConfigurer {

	@Override
	protected Properties mergeProperties() throws IOException {
		Properties properties=new Properties();
		File file= AppEnv.getPropFile();
		if(file.exists()){
			properties.load(new FileInputStream(file));
		}
		String appName=AppEnv.getAppName();
		if(appName==null){
			throw new ApplicationContextException("please config app_name in web.xml");
		}
		properties.setProperty("app_name",appName);
		return properties;
	}
}
