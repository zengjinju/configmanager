package com.zjj.configmanager.manager;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by admin on 2017/12/2.
 */
public class HostConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(HostConfig.class);
	private static final String ZK_SERVER = "localhost:2181";
	private static ZkClient zkClient;
	private static Map<String, String> CONFIGS = new HashMap<String, String>();
	private static volatile Boolean isChange = false;
	public static ReentrantLock lock = new ReentrantLock();

	static {
		zkClient = new ZkClient(ZK_SERVER);
	}

	public static String get(String key, String defaultValue) {
		try {
			if (isChange) {
				LOGGER.info("get lock");
				lock.lock();
			}
			String value = CONFIGS.get(key);
			return value != null && !"".equals(value) ? value : defaultValue;
		} finally {
			if (lock.isLocked()) {
				lock.unlock();
				isChange = false;
			}
		}
	}

	public static Map<String, String> tryLockGetData(String rootPath) {
		try {
			if (isChange) {
				lock.lock();
			}
			return getData(rootPath);
		} finally {
			if (lock.isLocked()) {
				lock.unlock();
				isChange = false;
			}
		}
	}


	private static Map<String, String> getData(String rootPath) {
		LOGGER.info("begin load config from zookeeper ");
		if (!rootPath.startsWith("/")) {
			rootPath = "/" + rootPath;
		}
		int times = 0;
		//如果获取数据失败则默认重试3次
		while (times <= 3) {
			try {
				if (CONFIGS.size() > 0) {
					CONFIGS.clear();
				}
				List<String> list = zkClient.getChildren(rootPath);
				if (!CollectionUtils.isEmpty(list)) {
					for (String path : list) {
						String value = zkClient.readData(rootPath + "/" + path);
						String key = path.substring(path.indexOf("/") + 1);
						CONFIGS.put(key, value);
					}
				}
				registerWatcher(rootPath);
				break;
			} catch (Exception e) {
				LOGGER.error("getData from zookeeper fail ", e);
				times++;
			}
		}
		LOGGER.info("load config from zookeeper finished");
		return CONFIGS;
	}

	/**
	 * 注册节点监听事件
	 *
	 * @param path
	 */
	public static void registerWatcher(String path) {
		zkClient.subscribeDataChanges(path, new IZkDataListener() {
			public void handleDataChange(String dataPath, Object data) throws Exception {
				LOGGER.info("/" + dataPath + " data has changed");
				isChange = true;
				tryLockGetData(dataPath);
			}

			public void handleDataDeleted(String dataPath) throws Exception {
				LOGGER.info("/" + dataPath + " node has deleted");
				isChange = true;
				tryLockGetData(dataPath);
			}
		});

		zkClient.subscribeChildChanges(path, new IZkChildListener() {
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				LOGGER.info("the childen list has changed of " + parentPath + " node");
				isChange = true;
				tryLockGetData(parentPath);
			}
		});
	}

	public static Map<String, String> getAllConfigs() {
		return CONFIGS;
	}
}
