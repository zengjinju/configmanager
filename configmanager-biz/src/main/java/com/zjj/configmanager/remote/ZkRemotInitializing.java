package com.zjj.configmanager.remote;

import com.zjj.configmanager.ZkBean;
import com.zjj.configmanager.dao.entity.AppServer;
import com.zjj.configmanager.dao.entity.ConfigItem;
import com.zjj.configmanager.service.AppServerService;
import com.zjj.configmanager.service.ConfigItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/12/2.
 * 初始化zookeeper配置管理(用于启动时的初始化)
 */
@Service
public class ZkRemotInitializing implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(ZkRemotInitializing.class);

	@Autowired
	private AppServerService appServerService;
	@Autowired
	private ConfigItemService configItemService;


	@Value("${zk_server}")
	private String serverString ;
	private int connectionTimeOut;
	private int sessionTimeOut;

	public void afterPropertiesSet() throws Exception {
		List<AppServer> appList = appServerService.getAll();
		if (CollectionUtils.isEmpty(appList)) {
			logger.info("应用列表为空");
			return;
		}
		List<ConfigItem> itemList = configItemService.getAllItem();
		if (CollectionUtils.isEmpty(itemList)) {
			logger.info("配置项列表为空");
			return;
		}
		Map<Integer, String> appMap = new HashMap<Integer, String>();
		Map<Integer, List<ConfigItem>> itemMap = new HashMap<Integer, List<ConfigItem>>();
		for (AppServer app : appList) {
			appMap.put(app.getId(), app.getAppCode());
		}
		for (ConfigItem item : itemList) {
			if (!itemMap.containsKey(item.getAppServerId())) {
				List<ConfigItem> list = new ArrayList<ConfigItem>();
				list.add(item);
				itemMap.put(item.getAppServerId(), list);
			} else {
				itemMap.get(item.getAppServerId()).add(item);
			}
		}
		ZkBean zkClient =ZkBean.getZkClient(serverString);
		try {
			for (Integer id : itemMap.keySet()) {
				String appName = appMap.get(id);
				zkClient.setParentPath(appName);
				if (!zkClient.isExit(appName)) {
					//创建根节点
					zkClient.createPersistent(appName);
				}
				for (ConfigItem item : itemMap.get(id)) {
					String path = item.getItemName();
					if (zkClient.isExit(zkClient.concatPath(path))) {
						logger.info("当前节点已经存在" + zkClient.concatPath(path));
						continue;
					}
					zkClient.createPersistent(path, item.getItemValue());
				}
			}
		} catch (Exception e) {
			logger.error("初始化配置中心配置项失败", e);
		}
	}
}
