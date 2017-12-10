package com.zjj.configmanager.manager;


import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by admin on 2017/12/6.
 */
public class ConfigerLoader {
	private static final Logger logger = LoggerFactory.getLogger(ConfigerLoader.class);
	private String appName;
	private static ConfigerLoader configerLoader;
	private String env;

	private static final String READ_FROM_SUPER = "is_read_from_super";
	private static final String APP_CODE = "app_code";
	private static final String ENVIRONMENT = "environment";

	private ConfigerLoader(String appName) {
		this.appName = appName;
	}

	public static ConfigerLoader getInstance(String appName) {
		if (configerLoader == null) {
			synchronized (ConfigerLoader.class) {
				if (configerLoader == null) {
					configerLoader = new ConfigerLoader(appName);
				}
			}
		}
		return configerLoader;
	}

	public String getEnvPropPath() {
		return "/data/config/" + appName + "/";
	}

	public File getEnvPropFile() {
		return new File(getEnvPropPath() + appName + "_env.properties");
	}

	public File getPropFile() {
		String fileName = Joiner.on("_").join(appName, "config", env + ".properties");
		return new File(getEnvPropPath() + fileName);
	}

	public void loadAndSaveConfig() {
		//加载环境配置信息
		loadEnv();
		if (AppEnv.getAppEnv() == null) {
			throw new ApplicationContextException("必须配置项目编号和项目环境");
		}
		//判断是否需要从配置中心读取远程配置
		if (!AppEnv.getNeedLoadFromRemote()) {
			return;
		}
		loadFromSuper();
	}

	public void loadEnv() {
		//获取环境配置文件
		File file = getEnvPropFile();
		if (file.exists()) {
			try {
				loadEnvProp(file);
			} catch (Exception e) {
				logger.error("加载环境配置文件异常", e);
			}
		} else {
			logger.error("the file " + getEnvPropFile().getAbsolutePath() + " not exits, please create file first");
		}
	}

	/**
	 * 获取应用的环境配置参数信息
	 *
	 * @param file
	 */
	public void loadEnvProp(File file) throws Exception {
		Properties envProp = new Properties();
		envProp.load(new FileInputStream(file));
		//获取环境配置信息
		String readFromSuper = envProp.getProperty(READ_FROM_SUPER);
		String appCode = envProp.getProperty(APP_CODE);
		String env = envProp.getProperty(ENVIRONMENT);
		this.env = env;
		//判断是否需要从远程读取数据
		if (readFromSuper != null && "off".equals(readFromSuper)) {
			AppEnv.create(appName, appCode, env, getPropFile(), false);
		} else {
			AppEnv.create(appName, appCode, env, getPropFile(), true);
		}
	}

	public void loadFromSuper() {
		//从zookeeper上获取配置信息，并注册监听
		Map<String, String> map = HostConfig.tryLockGetData(appName);
		Properties prop = new Properties();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			prop.setProperty(entry.getKey(), entry.getValue());
		}
		//将远程配置保存到本地配置文件中
		if (prop.size() > 0) {
			try {
				File file = getPropFile();
				if (!file.exists()) {
					file.createNewFile();
				}
				prop.store(new FileOutputStream(file), Joiner.on("_").join(appName,env,"properties"));
			} catch (Exception e) {
				logger.error("保存本地配置异常", e);
			}
		}
	}

	public static void main(String[] args) {
		ConfigerLoader loader = new ConfigerLoader("");
//		System.out.println(loader.getUserLocalPath());
	}
}
