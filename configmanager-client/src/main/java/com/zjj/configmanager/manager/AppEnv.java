package com.zjj.configmanager.manager;

import java.io.File;

/**
 * Created by admin on 2017/12/6.
 */
public class AppEnv {
	private static AppEnv appEnv;
	private String appName;
	private String appCode;
	/**
	 * 环境配置(开发，测试，正式)
	 */
	private String enviroment;
	private File propFile;
	private Boolean needLoadFromRemote;
	public AppEnv(String appName,String appCode,String enviroment,File propFile,Boolean needLoadFromRemote){
		this.appName=appName;
		this.appCode=appCode;
		this.enviroment=enviroment;
		this.propFile=propFile;
		this.needLoadFromRemote=needLoadFromRemote;
	}

	public static void create(String appName,String appCode,String enviroment,File propFile,Boolean needLoadFromRemote){
		AppEnv.appEnv=new AppEnv(appName,appCode,enviroment,propFile,needLoadFromRemote);
	}

	public static String getAppName() {
		return appEnv.appName;
	}

	public static String getAppCode() {
		return appEnv.appCode;
	}

	public static String getEnviroment() {
		return appEnv.enviroment;
	}

	public static File getPropFile() {
		return appEnv.propFile;
	}

	public static Boolean getNeedLoadFromRemote() {
		return appEnv.needLoadFromRemote;
	}

	public static AppEnv getAppEnv() {
		return appEnv;
	}
}
